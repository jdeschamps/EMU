package main.embl.rieslab.htSMLM.threads;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import main.embl.rieslab.htSMLM.acquisitions.Acquisition;
import main.embl.rieslab.htSMLM.configuration.SystemController;
import mmcorej.CMMCore;

import org.micromanager.api.IAcquisitionEngine2010;
import org.micromanager.api.MultiStagePosition;
import org.micromanager.api.PositionList;
import org.micromanager.api.ScriptInterface;
import org.micromanager.utils.MMScriptException;

public class AcquisitionTask implements Task<Integer>{

	private ScriptInterface script_;
	private CMMCore core_;
	private boolean running_ =  false;
	private SystemController system_;
	private AcquisitionRun t;
	private TaskHolder<Integer> holder_;
	ArrayList<Acquisition> acqlist_;
	
	public AcquisitionTask(TaskHolder<Integer> holder, SystemController system){
		system_ = system;
		script_ = system_.getScriptInterface();
		core_ = script_.getMMCore();
		
		registerHolder(holder);
	}
	
	public boolean isRunning(){
		return running_;
	}
	
	public void setAcquisitionList(ArrayList<Acquisition> acqlist){
		acqlist_ = acqlist;
	}

	@Override
	public void registerHolder(TaskHolder<Integer> holder) {
		holder_ = holder;
	}

	@Override
	public void startTask() {
		if(acqlist_ != null && !acqlist_.isEmpty()){
			System.out.println("Acq start in Engine");

			t = new AcquisitionRun(acqlist_);
			t.execute();
			running_ = true;
		} else {
			System.out.println("Acquisition list is empty or null");
		}
	}

	@Override
	public void stopTask() {
		if(t != null && !t.isDone()){
			t.stop();
		}
	}

	public void notifyHolder(Integer[] outputs) {
		holder_.update(outputs);
	}

	@Override
	public boolean isPausable() {
		return false;
	}

	@Override
	public void pauseTask() {
		// Do nothing
	}

	@Override
	public void resumeTask() {
		// Do nothing
	}
	
	class AcquisitionRun extends SwingWorker<Integer, Integer> {

		private ArrayList<Acquisition> acqlist_;
		private String currAcq;
		private boolean stop_ = false;

		public AcquisitionRun(ArrayList<Acquisition> acqlist) {
			acqlist_ = acqlist;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			return runAcquisitions();
		}

		public Integer runAcquisitions() {
			Integer[] param = holder_.retrieveAllParameters();
			
			if(acqlist_ == null){
				System.out.println("Acquisition list is null");
				return 0;
			}
			
			if(acqlist_.size() > 0) {
				try {
					// clear all previous acquisitions
					script_.closeAllAcquisitions();
					script_.clearMessageWindow();

					PositionList poslist = script_.getPositionList();
					int numPosition = poslist.getNumberOfPositions();
					
					if(numPosition>0){		
						MultiStagePosition currPos;
	
						String xystage = script_.getXYStageName();
	
						// retrieve max number of positions
						int maxNumPosition = numPosition;
						if(param[1] > 0){
							maxNumPosition = param[1];
						}
						
						for (int i = 0; i < maxNumPosition; i++) {
							// move to next stage position
							currPos = poslist.getPosition(i);
							core_.setXYPosition(xystage, currPos.getX(),currPos.getY());
	
							// let time for the stage to move to position
							Thread.sleep(param[0]*1000);
	
							// perform each acquisition sequentially
							for (int k = 0; k < acqlist_.size(); k++) {
								final Acquisition acq = acqlist_.get(k);

								// set acquisition settings
								script_.setAcquisitionSettings(acq.getSettings());
	
								// set acquisition name
								final String acqname = createAcqName(acq, i);
	
								// set-up system
								system_.setUpSystem(acq.getPropertyValues());
								
								// set configuration channel
								String prev_config = null;
								if (acq.useConfig()) {
									prev_config = core_.getCurrentConfigFromCache(acq.getConfigGroup());									
									core_.setConfig(acq.getConfigGroup(),acq.getConfigName());
								}
								
								// set-up special acquisition state
								acq.preAcquisition();
	
								// let time for the system to adjust
								Thread.sleep(acq.getWaitingTime()*1000);
	
								// run acquisition
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
									if (acq.stopCriterionReached()) {
										interruptAcquistion();
									}
								}
	
								// set-up special post-acquisition state
								acq.postAcquisition();
	
								// Reset configuration
								if (acq.useConfig() && prev_config != null) {
									core_.setConfig(acq.getConfigGroup(),prev_config);
								}
								
								// close acq window
								try {
									script_.closeAcquisitionWindow(currAcq);
								} catch (MMScriptException e) {
									// ?
								}
	
								if (stop_) {
									break;
								}
							}
	
							if (stop_) {
								break;
							}
	
							// show progress
							publish(i);
						}
					} else {
						System.out.println("Position list empty");
					}

				} catch (MMScriptException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			publish(-1);
			
			return 0;
		}

		public String createAcqName(Acquisition acq, int i){
			String acqname;
			if (i < 10) {
				acqname = "00" + i + "_" + acq.getExperimentName() + "_"+ acq.getType();
			} else if (i < 100) {
				acqname = "0" + i + "_" + acq.getExperimentName() + "_"+ acq.getType();
			} else {
				acqname = i + "_" + acq.getExperimentName() + "_"+ acq.getType();
			}
			return acqname;
		}
		
		public void stop() {
			stop_ = true;
			interruptAcquistion();
		}

		protected void interruptAcquistion() {
			IAcquisitionEngine2010 aq = script_.getAcquisitionEngine2010();
			aq.stop();
			while (!aq.isFinished()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected void process(List<Integer> chunks) {
			for(Integer result : chunks){
				if(result>=0){
					Integer[] results = {result};
					notifyHolder(results);
				} else if(result == -1){
					running_ = false;
					holder_.taskDone();
				}
			}
		}
	}

	@Override
	public void setSaveData(boolean savedata) {
		// Ignore
	}
}

