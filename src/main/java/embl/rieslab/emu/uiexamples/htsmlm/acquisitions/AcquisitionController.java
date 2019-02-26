package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions;


import java.util.ArrayList;

import javax.swing.SwingUtilities;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.AcquisitionPanel;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionFactory.AcquisitionType;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.acquisitiontypes.Acquisition;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.ui.AcquisitionWizard;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils.AcquisitionInformationPanel;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.wrappers.Experiment;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.constants.HTSMLMConstants;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.AcquisitionTask;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.Task;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.tasks.TaskHolder;

public class AcquisitionController implements TaskHolder<Integer>{

	private static String TASK_NAME = "Unsupervised acquisitions";
	
	private AcquisitionPanel owner_;
	private SystemController controller_;
	private AcquisitionInformationPanel infopanel_;
	private Experiment exp_;
	private AcquisitionWizard wizard_;
	private AcquisitionTask task_;

	public AcquisitionController(SystemController controller, AcquisitionPanel owner, AcquisitionInformationPanel infopane){
		controller_ = controller;
		owner_ = owner;
		infopanel_ = infopane;
		
		infopanel_.setInitialText();
		
		// placeholder experiment
		exp_ = new Experiment(0, 0, new ArrayList<Acquisition>());
	}
	
	@Override
	public void update(Integer[] output) {
		if (SwingUtilities.isEventDispatchThread()) {
		    owner_.updateProgressBar(output[0]);
		    infopanel_.setPositionDoneText(output[0]);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				    owner_.updateProgressBar(output[0]);
				    infopanel_.setPositionDoneText(output[0]);
				}
			});
		}
	}

	@Override
	public Integer[] retrieveAllParameters() {
		Integer[] params = new Integer[1];
		params[0] = exp_.getPauseTime();
		return params;
	}

	@Override
	public boolean startTask() {
		// this is running on the EDT
		
		// set path and experiment name in acquisition
		final String name = owner_.getExperimentName();
		final String path = owner_.getExperimentPath();
		
		if(!isAcquisitionListEmpty() && path != null && name != null && !path.equals("")){	
			final AcquisitionFactory factory = new AcquisitionFactory(this,controller_);
			task_ = new AcquisitionTask(this, controller_, exp_, name, path);
	
			Thread t = new Thread("Set-up acquisition") {
				public void run() {

					// save the acquisition list to the destination folder
					boolean b = true;
					if (wizard_ != null) {
						b = saveAcquisitionList(exp_, name, path + "/");
					} else {
						b = factory.writeAcquisitionList(exp_, name, path + "/");
					}
	
					if (!b) {
						// report problem saving
						System.out.println("Error writting acquisition list");
					}
	
					System.out.println("Start task acq engine");
					task_.startTask();
				}
	
			};
			t.start();
	
			infopanel_.setStartText();
			owner_.updateProgressBar(0);
			
			return true;
		}
		return false;
	}	
	
	@Override
	public void stopTask() {
		if(task_ != null){
			task_.stopTask();
		}
		infopanel_.setStopText();
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

	@Override
	public boolean isTaskRunning() {
		return task_.isRunning();
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}

	@Override
	public boolean isCriterionReached() {
		return false;
	}

	@Override
	public void initializeTask() {
		// Do nothing
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Task getTask() {
		return task_;
	}
	
	@Override
	public void taskDone() {
		Runnable doDone = new Runnable() {
			public void run() {
				done();
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			doDone.run();
		} else {
			done();
		}
	}

	private void done(){
		if(!isTaskRunning()){		
			owner_.updateProgressBar(getNumberOfPositions());
			owner_.setStateButtonToStop();
			infopanel_.setStopText();
			
			// refresh all properties to make sure things are synchronized
			controller_.update();
		}
	}

	public void startWizard() {
		if(!isAcquisitionListEmpty()){
			System.out.println("is not empty");
			wizard_ = new AcquisitionWizard(controller_, this, exp_);
		} else {
			wizard_ = new AcquisitionWizard(controller_, this);	
		}
	}

	public void loadExperiment(String path) {
		Experiment exp = loadAcquisitionList(path);	// maybe try catch? to get the exception of null acquitisionlist

		if(exp.getAcquisitionList() != null){
			exp_ = exp;
			infopanel_.setAcquisitionLoaded();	
			infopanel_.setSummaryText(exp_);
		
			// for the moment there is no mechanism to get the expname and exppath
		}
	}
	
	private Experiment loadAcquisitionList(String path) {		
    	return (new AcquisitionFactory(this, controller_)).readAcquisitionList(path);
	}

	private boolean saveAcquisitionList(Experiment exp, String exppath, String path) {
		return (new AcquisitionFactory(this, controller_)).writeAcquisitionList(exp, exppath, path);
	}
	
	public void saveExperiment(String path) {
		if (!path.endsWith("." + HTSMLMConstants.ACQ_EXT)) {
			path = path + "." + HTSMLMConstants.ACQ_EXT;
		}
		saveAcquisitionList(exp_, owner_.getExperimentName()+"/"+owner_.getExperimentPath(), path);
	}

	public Experiment getExperiment() {
		return exp_;
	}
	
	public void setExperiment(Experiment exp){
		exp_ = exp;
		infopanel_.setSummaryText(exp_);
	}

	public void shutDown() {
		stopTask();
		if(wizard_ != null){
			wizard_.shutDown();
		}
	}

	public int getNumberOfPositions() {
		return controller_.getStudio().getPositionListManager().getPositionList().getNumberOfPositions();
	}

	public boolean isAcquisitionListEmpty() {
		return exp_.getAcquisitionList().isEmpty();
	}

	public boolean isAcquistionPropertyEnabled(AcquisitionType type) {
		if(type.equals(AcquisitionType.BFP) && !owner_.getParameterValues(AcquisitionPanel.PARAM_BFP).equals(UIPropertyParameter.NO_PROPERTY)){
			return true;
		} else if(type.equals(AcquisitionType.BF) && !owner_.getParameterValues(AcquisitionPanel.PARAM_BRIGHTFIELD).equals(UIPropertyParameter.NO_PROPERTY)){
			return true;
		}
		return false;  
	}
	
	public String getAcquisitionParameterValue(String param){
		return owner_.getParameterValues(param);
	}

	@SuppressWarnings("rawtypes")
	public TaskHolder getTaskHolder(String taskName) {
		return owner_.getTaskHolders().get(taskName);
	}
}
