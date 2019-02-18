package main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.util.List;

import javax.swing.SwingWorker;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.constants.HTSMLMConstants;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.utils.NMS;
import mmcorej.CMMCore;
import mmcorej.TaggedImage;

public class ActivationTask implements Task<Double> {

	public static int PARAM_SDCOEFF = 0;
	public static int PARAM_FEEDBACK = 1;
	public static int PARAM_CUTOFF = 2;
	public static int PARAM_AUTOCUTOFF = 3;
	public static int PARAM_dT = 4;
	public static int PARAM_N0 = 5;
	public static int PARAM_PULSE = 6;
	public static int PARAM_MAXPULSE = 7;
	public static int PARAM_ACTIVATE = 8;
	public static int OUTPUT_NEWCUTOFF = 0;
	public static int OUTPUT_N = 1;
	public static int OUTPUT_NEWPULSE = 2;
	public static int NUM_PARAMETERS = 9;
	public static int NUM_OUTPUTS = 3;
	
	private CMMCore core_;
	private TaskHolder<Double> holder_;
	private int idletime_;
	private AutomatedActivation worker_;
	private boolean running_;
	private Double[] output_;
	private ImageProcessor ip_;
	
	private double previous_pulse_;
	
	public ActivationTask(TaskHolder<Double> holder, CMMCore core, int idle){
		running_ = false;
		
		core_ = core;
		idletime_ = idle;
		
		registerHolder(holder);
		
		previous_pulse_ = 0.4;
		
		output_ = new Double[3];
		output_[0] = 0.;
		output_[1] = 0.;
		output_[2] = 0.;
	}
	
	@Override
	public void registerHolder(TaskHolder<Double> holder) {
		holder_ = holder;
	}

	@Override
	public void startTask() {
		worker_ = new AutomatedActivation();
		worker_.execute();
		running_ = true;
	}

	@Override
	public void stopTask() {
		running_ = false;
	}

	@Override
	public boolean isRunning() {
		return running_;
	}
	
	public void setIdleTime(int idle){
		idletime_ = idle;
	}

	private void getN(double sdcoeff, double cutoff, double dT, boolean autocutoff) {
		
		if (core_.isSequenceRunning() && core_.getBytesPerPixel() == 2) {
			int width, height;
			double tempcutoff;

			TaggedImage tagged1 = null, tagged2 = null;
			ShortProcessor ip, ip2;
			ImagePlus imp, imp2;
			ImageCalculator calcul = new ImageCalculator();
			ImagePlus imp3;
			GaussianBlur gau = new GaussianBlur();
			NMS NMSuppr = new NMS();

			width = (int) core_.getImageWidth();
			height = (int) core_.getImageHeight();

			int buffsize = core_.getImageBufferSize();

			if (buffsize > HTSMLMConstants.CAM_BIT_DEPTH) {
				try {
					tagged1 = core_.getLastTaggedImage();
					tagged2 = core_.getNBeforeLastTaggedImage(HTSMLMConstants.FPGA_SEQUENCE_LENGTH);
				} catch (Exception e) {
					// exit?
				}

				ip = new ShortProcessor(width, height);
				ip2 = new ShortProcessor(width, height);

				ip.setPixels(tagged1.pix);
				ip2.setPixels(tagged2.pix);

				imp = new ImagePlus("", ip);
				imp2 = new ImagePlus("", ip2);

				// Subtraction
				imp3 = calcul.run("Substract create", imp, imp2);

				// Gaussian filter
				gau.blurGaussian(imp3.getProcessor(),
						HTSMLMConstants.gaussianMaskSize,
						HTSMLMConstants.gaussianMaskSize,
						HTSMLMConstants.gaussianMaskPrecision);

				try {
					tempcutoff = imp3.getStatistics().mean + sdcoeff
							* imp3.getStatistics().stdDev;
				} catch (Exception e) {
					tempcutoff = cutoff;
				}

				double newcutoff;
				if (autocutoff) {
					newcutoff = (1 - 1 / dT) * cutoff + tempcutoff / dT;
					newcutoff = Math.floor(10 * newcutoff + 0.5) / 10;
				} else {
					newcutoff = cutoff;
					if (newcutoff == 0) {
						newcutoff = Math.floor(10 * tempcutoff + 0.5) / 10;;
					}
				}
				
				ip_ = NMSuppr.run(imp3, HTSMLMConstants.nmsMaskSize, newcutoff);
				output_[OUTPUT_NEWCUTOFF] = newcutoff;
				output_[OUTPUT_N] = (double) NMSuppr.getN();
			}
		}
	}
	
	private void getPulse(double feedback, double N0, double pulse, double maxpulse){
		double N = output_[OUTPUT_N];
		double temppulse=0;
		double min = 0.4;
		
		if(Math.abs(previous_pulse_-pulse)>1){ // in case of user intervention
			previous_pulse_ = pulse;
		}
		
		
		double npulse = previous_pulse_;// avoid getting stuck between 0 and 1 (otherwise newp=0.4+0.4*1.99*coeff < 1 unless coeff ~> 0.7 
										// which is not good for higher values of the pulse) by reusing the previous pulse and not the current pulse
		
		
		if(core_.isSequenceRunning()){
						
			if(previous_pulse_ < min){ 
				npulse = min;
			}
			
			// calculate new pulse
			if(N0 > 0){
				temppulse = npulse*(1+feedback*(1-N/N0));
			} else {
				output_[OUTPUT_NEWPULSE] = 0.;
				return;
			}
			
			if(temppulse < min){
				temppulse = min;
			}
	
			// if new pulse is higher than camera exposure
			double exp;
			try {
				exp = 1000*core_.getExposure();
				if(temppulse > exp) {
					temppulse = exp; 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			npulse = temppulse;

		} else {
			npulse = pulse;
		}
		
		if(npulse > maxpulse){
			npulse = maxpulse;
		}		
		
		
		previous_pulse_ = npulse;
		output_[OUTPUT_NEWPULSE] = Math.floor(npulse);
	}

	@Override
	public void notifyHolder(Double[] outputs) {
		holder_.update(outputs);
	}
	
	public ImageProcessor getNMSResult(){
		return ip_;
	}
	
	private class AutomatedActivation extends SwingWorker<Integer, Double[]> {
		
		@Override
		protected Integer doInBackground() throws Exception {
			Double[] params;
			
			while(running_){
				params = holder_.retrieveAllParameters();
								
				// sanity checks here?
				if(params[PARAM_AUTOCUTOFF] == 1){
					getN(params[PARAM_SDCOEFF],params[PARAM_CUTOFF],params[PARAM_dT],true);
				} else {
					getN(params[PARAM_SDCOEFF],params[PARAM_CUTOFF],params[PARAM_dT],false);
				}
				
				if(params[PARAM_ACTIVATE] == 1){
					getPulse(params[PARAM_FEEDBACK],params[PARAM_N0],params[PARAM_PULSE],params[PARAM_MAXPULSE]);
				} else {
					output_[OUTPUT_NEWPULSE] = params[PARAM_PULSE];
				}
				
				publish(output_);
				
				Thread.sleep(idletime_);
			}
			return 1;
		}

		@Override
		protected void process(List<Double[]> chunks) {
			for(Double[] result : chunks){
				notifyHolder(result);
			}
		}
	}

	@Override
	public boolean isPausable() {
		return false;
	}

	@Override
	public void pauseTask() {
		// do nothing
	}

	@Override
	public void resumeTask() {
		// do nothing
	}

}
