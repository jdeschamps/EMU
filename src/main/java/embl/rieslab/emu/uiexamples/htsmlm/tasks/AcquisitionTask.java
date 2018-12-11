package main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;

import org.micromanager.MultiStagePosition;
import org.micromanager.PositionList;
import org.micromanager.PositionListManager;
import org.micromanager.Studio;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.data.Datastore;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.tasks.Task;
import main.java.embl.rieslab.emu.tasks.TaskHolder;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.Acquisition;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.AcquisitionTaskOld.AcquisitionRun;
import mmcorej.CMMCore;

public class AcquisitionTask  implements Task<Integer>{

	private Studio studio_;
	private CMMCore core_;
	private AcquisitionManager acqmanager_;
	private PositionListManager posmanager_;
	private boolean running_ =  false;
	private SystemController system_;
	private AcquisitionRun t;
	private TaskHolder<Integer> holder_;
	ArrayList<Acquisition> acqlist_;
	
	public AcquisitionTask(TaskHolder<Integer> holder, SystemController system){
		system_ = system;
		studio_ = system_.getStudio();
		core_ = studio_.getCMMCore();
		acqmanager_ = studio_.getAcquisitionManager();
		posmanager_ = studio_.getPositionListManager();
				
		registerHolder(holder);
	}
	
	@Override
	public void registerHolder(TaskHolder<Integer> holder) {
		holder_ = holder;		
	}

	@Override
	public void notifyHolder(Integer[] outputs) {
		
	}

	@Override
	public void startTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPausable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pauseTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeTask() {
		// TODO Auto-generated method stub
		
	}
	

	class AcquisitionRun extends SwingWorker<Integer, Integer> {

		private ArrayList<Acquisition> acqlist_;
		private Datastore currAcq;
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
			
			if (acqlist_.size() > 0) {

				PositionList poslist = posmanager_.getPositionList();
				int numPosition = poslist.getNumberOfPositions();

				if (numPosition > 0) {
					MultiStagePosition currPos;

					String xystage = core_.getXYStageDevice();

					// retrieve max number of positions
					int maxNumPosition = numPosition;
					if (param[1] > 0) {
						maxNumPosition = param[1];
					}

					for (int i = 0; i < maxNumPosition; i++) {
						// move to next stage position
						currPos = poslist.getPosition(i);
						try {
							core_.setXYPosition(xystage, currPos.getX(), currPos.getY());
							
							// let time for the stage to move to position
							Thread.sleep(param[0] * 1000);

							// perform each acquisition sequentially
							for (int k = 0; k < acqlist_.size(); k++) {
								final Acquisition acq = acqlist_.get(k);

								// set-up system
								system_.setUpSystem(acq.getParameters().mmconfgroups_);

								// set configuration settings
								if (!acq.getParameters().mmconfgroups_.isEmpty()) {
									HashMap<String, String> configs = acq.getParameters().mmconfgroups_;
									Iterator<String> it = configs.keySet().iterator();
									while (it.hasNext()) {
										String group = it.next();
										core_.setConfig(group,configs.get(group));
									}
								}

								// run acquisition


								// set-up special post-acquisition state
								acq.postAcquisition();

								// close acq window
								studio_.getDisplayManager().closeDisplaysFor(currAcq);

								if (stop_) {
									System.out.println("Stop is true in acquisition");
									break;
								}
							}

							if (stop_) {
								System.out.println("Stop is true in position");
								break;
							}

							// show progress
							publish(i);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("Position list empty");
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
			System.out.println("interrupt acq requested");
			
			while (!acqmanager_.isAcquisitionRunning()) {
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

}
