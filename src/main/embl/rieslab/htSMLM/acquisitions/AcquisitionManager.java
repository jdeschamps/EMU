package main.embl.rieslab.htSMLM.acquisitions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import main.embl.rieslab.htSMLM.controller.SystemController;
import main.embl.rieslab.htSMLM.threads.TaskHolder;
import mmcorej.CMMCore;

import org.micromanager.api.MultiStagePosition;
import org.micromanager.api.PositionList;
import org.micromanager.api.ScriptInterface;
import org.micromanager.utils.MMScriptException;

public class AcquisitionManager {

	private ScriptInterface script_;
	private CMMCore core_;
	private boolean running_ =  false;
	private boolean stoploop_ =  false;
	private TaskHolder activation_;
	private SystemController system_;
	
	public AcquisitionManager(ScriptInterface script, SystemController system, TaskHolder activation){
		script_ = script;
		core_ = script_.getMMCore();
		activation_ = activation;
		system_ = system;
	}
	
	public boolean isRunning(){
		return running_;
	}
	
	public void startAcquisitions(ArrayList<Acquisition> acqlist){
		running_ = true;
	}
	
	public void stopAcquisitions(){
		stoploop_  = true;
	}
	
	class AcquisitionRun extends SwingWorker<Integer, Double[]> {


		@Override
		protected Integer doInBackground() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}		
		
		public void runAcquisitions(ArrayList<Acquisition> acqlist) {
			if (acqlist.size() > 0) {
				try {
					// clear all previous acquisitions
					script_.closeAllAcquisitions();
					script_.clearMessageWindow();

					PositionList poslist = script_.getPositionList();
					int numPosition = poslist.getNumberOfPositions();
					MultiStagePosition currPos;
					
					String xystage = script_.getXYStageName(); 

					for (int i = 0; i < numPosition; i++) {

						// move to next stage position
						currPos = poslist.getPosition(i);
						core_.setXYPosition(xystage, currPos.get(0).x,currPos.get(0).y);
						
						// let time for the stage to move to position
						Thread.sleep(1000);

						// perform each acquisition sequentially
						for (int k = 0; k < acqlist.size(); k++) {
							final Acquisition acq = acqlist.get(k);
							
							// set acquisition settings
							script_.setAcquisitionSettings(acqlist.get(k).getSettings());
							
							// pause/resume activation script if necessary
							if(acq.useActivation()){
								activation_.resumeTask();
							} else {
								activation_.pauseTask();
							}
								
							// set acquisition name
							final String acqname;
							if (i < 10) {
								acqname = "00" + i + "_" + acq.getName()+ "_" + acq.getType();
							} else if (i < 100) {
								acqname = "0" + i + "_" + acq.getName() + "_"+ acq.getType();
							} else if (i >= 100) {
								acqname = i + "_" + acq.getName() + "_"+ acq.getType();
							}

							// set configuration channel
							if(acq.useConfig()){
								core_.setConfig(acq.getConfigurationGroup(), acq.getConfigurationName());
							}
							
							// set-up system
							system_.setUpSystem(acq.getPropertyValues());
							
							// let time for the system to adjust
							Thread.sleep(1000);

							// run acq
							String currAcq;
							Thread t = new Thread() {
								public void run() {
									try {
										currAcq = script_.runAcquisition(acqname, acq.getPath());
									} catch (MMScriptException e) {
										e.printStackTrace();
									}
								}
							};
							t.start();

							while (t.isAlive()) {
								Thread.sleep(1000);
								if (uv.isUVatMax() && stopmaxUV_) { // if UV is at max or stop has been requested
									Thread.sleep(UVsleepTime_ * 1000);
									closeCurrAcq();
									// t.interrupt();
								}
							}

							// close acq window
							try {
								app.closeAcquisitionWindow(currAcq);
							} catch (MMScriptException e) {
								System.out.println("Cannot close");
							}

							// restart uv
							uv.restartUV();

							// end acq settings
							acqlist.get(k).endAcqSystem(sys_);

							if (stop_) {
								stop_ = false;
								stoploop = true; // one case use a "outer:"
													// before the big loop and
													// do "break outer;"
								break;
							}

						}

						// show progress
						result[1] = (double) (Math.floor(100 * (i + 1)
								/ numPosition));
						publish(result);

						if (stoploop) {
							stoploop = false;
							break;
						}

					}

					result[1] = (double) 100;
					publish(result);

				} catch (MMScriptException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected void process(List<Double[]> chunks) {
			for(Double[] result : chunks){
    			//System.out.println("EDT? "+SwingUtilities.isEventDispatchThread());
				
    			pb.setValue(result[1].intValue());
    			st.addFinishedExperiments(result[0].intValue());
    			
    			if(result[1].intValue()==0) {
    				st.setAcquiring(numPosition);
    			}else if(result[1].intValue()==100){
					st.setDone();
				}
			}
		}
	}
}
