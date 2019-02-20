package main.java.embl.rieslab.emu.uiexamples.htsmlm;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.embl.rieslab.emu.controller.SystemController;
import main.java.embl.rieslab.emu.ui.ConfigurablePanel;
import main.java.embl.rieslab.emu.ui.uiparameters.UIPropertyParameter;
import main.java.embl.rieslab.emu.ui.uiproperties.flag.TwoStateFlag;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.AcquisitionController;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils.AcquisitionDialogs;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils.AcquisitionInformationPanel;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils.ExperimentTreeSummary;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.constants.HTSMLMConstants;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.flags.FocusStabFlag;

public class AcquisitionPanel extends ConfigurablePanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8529929695246469740L;
		
	private SystemController controller_;
	private AcquisitionController acqcontroller_;
    private MainFrame owner_;
    
    ///// Parameters
    public final static String PARAM_LOCKING = "Focus stabilization";
    public final static String PARAM_BFP = "BFP lens";
    public final static String PARAM_BRIGHTFIELD = "Bright field";
	
    ///// Convenience variables
	private String paramBFP_, paramLocking_, paramBrightField_;
	
    private JFrame summaryframe_;
    
    ///// UI
    private JButton jButton_setpath;
    private JToggleButton jToggle_startstop,jButton_showSummary;
    private JButton jButton_load,jButton_configAcq,jButton_saveAcq;
    private JLabel jLabel_expname, jLabel_path, jLabel_progress;
    private JProgressBar jProgressBar_progress;
    private JTextField jTextField_expname;
    private JTextField jTextField_path;
    private JTextPane jTextPane_progress;
    	
	public AcquisitionPanel(SystemController controller, MainFrame owner){
		super("Acquisitions");
		controller_ = controller;
		
		acqcontroller_ = new AcquisitionController(controller, this, new AcquisitionInformationPanel(jTextPane_progress), owner.getTaskHolders());
		
		// listen to window movement to place the summary panel at the right place
		owner_ = owner;
		owner_.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent evt) {
				Point newLoc = getSummaryButtonLocation();
				if (summaryframe_ != null) {
					summaryframe_.setLocation(newLoc);
					summaryframe_.toFront();
					summaryframe_.repaint();
				}
            }
          });
		
		owner_.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
				if (summaryframe_ != null) {
					summaryframe_.setAlwaysOnTop(false);
				}
            }

            @Override
            public void windowActivated(WindowEvent e) {
				if (summaryframe_ != null) {
					summaryframe_.setAlwaysOnTop(true);
				}
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            	if ((owner_.getExtendedState() & Frame.ICONIFIED) != 0) {
					if (summaryframe_ != null) {
						summaryframe_.setAlwaysOnTop(false);
						summaryframe_.toBack();
					}
                  }
             }
        });
       
		
	}
	
	private void initiPanel() {
	    jButton_setpath = new JButton("...");
	    jButton_setpath .addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showSelectPath();
            }
        });

	    jToggle_startstop = new JToggleButton("Start");
	    jToggle_startstop.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					String path = getExperimentPath();
					String name = getExperimentName();

					if(path == null || path.equals("")){
						AcquisitionDialogs.showNoPathMessage();
						jToggle_startstop.setSelected(false);
					} else if(name == null || name.equals("")){
						AcquisitionDialogs.showNoNameMessage();	
						jToggle_startstop.setSelected(false);
					} else if(acqcontroller_.isAcquisitionListEmpty()){
						AcquisitionDialogs.showNoAcqMessage();
						jToggle_startstop.setSelected(false);
					} else {
						boolean b = acqcontroller_.startTask();
						if(b){
							jToggle_startstop.setText("Stop");
							jProgressBar_progress.setMaximum(acqcontroller_.getNumberOfPositions());
						} else {
							jToggle_startstop.setSelected(false);
						}
					}
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					jToggle_startstop.setText("Start");
					acqcontroller_.stopTask();
				}
			}
        });
        
        jButton_load = new JButton("Load");
        jButton_load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAcquisitionList();
			}
        });
        
        jButton_configAcq = new JButton("Configure");
        jButton_configAcq.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAcquisitionConfiguration();
			}
        });
        
        jButton_saveAcq = new JButton("Save as");
        jButton_saveAcq.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAcquisitionList();
			}
        });

        jButton_showSummary = new JToggleButton(">>");
        jButton_showSummary.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					showSummary(true);
					jButton_showSummary.setText("<<");
				} else if(e.getStateChange()==ItemEvent.DESELECTED){
					showSummary(false);
					jButton_showSummary.setText(">>");
				}
			}
        });
        
        jLabel_path = new JLabel("Path");
        jLabel_expname = new JLabel("Experiment name");
        jLabel_progress = new JLabel("Progress");

	    jProgressBar_progress = new JProgressBar();
	    jProgressBar_progress.setMinimum(0);
        

	    jTextField_expname = new JTextField();
	    jTextField_path = new JTextField();
	    
		jTextPane_progress = new JTextPane();
	    jTextPane_progress.setBackground(this.getBackground());
	    
	    JScrollPane scroll = new JScrollPane(jTextPane_progress);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(50, 30, 300, 50);

	    
	    /////////////////////////////////////////////////
	    /// Grid bag layout
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,4,2,4);
		c.fill = GridBagConstraints.HORIZONTAL;

		// upper part
		c.gridx = 0;
		c.gridy = 1;
		this.add(jLabel_path, c);	    

		c.gridx = 0;
		c.gridy = 2;
		this.add(jLabel_expname, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		this.add(jTextField_path, c);
		
		c.gridx = 1;
		c.gridy = 2;
		this.add(jTextField_expname, c);

		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx =0.1;
		this.add(jButton_setpath, c);	
		
		// progress 
		c.gridx = 0;
		c.gridy = 3;
		this.add(jLabel_progress, c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		this.add(jProgressBar_progress, c);	
		
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx =0.1;
		this.add(jButton_showSummary, c);	 

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 3;
		c.weightx = 0.8;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		this.add(scroll, c);	 
		
		// lower part
		JPanel lower = new JPanel();
		GridLayout gridlayout = new GridLayout(0,4);
		lower.setLayout(gridlayout);

		lower.add(jToggle_startstop);
		lower.add(jButton_configAcq);
		lower.add(jButton_saveAcq);
		lower.add(jButton_load);
		
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;		
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(lower,c);
	}
		
	private void showSelectPath(){
    	JFileChooser fc = new JFileChooser();
    	fc.setCurrentDirectory(new java.io.File(".")); 
    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int returnVal = fc.showOpenDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    	    File folder = fc.getSelectedFile();
    	    jTextField_path.setText(folder.getAbsolutePath());  
    	}
	}

	private void showAcquisitionConfiguration(){
		acqcontroller_.startWizard();
	}
	
	private void loadAcquisitionList(){
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Acquisition list", HTSMLMConstants.ACQ_EXT);
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(new JFrame());
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    String path = selectedFile.getAbsolutePath();
		    
		    acqcontroller_.loadExperiment(path);
	    }
	}

	private void saveAcquisitionList() {
		if(acqcontroller_.isAcquisitionListEmpty()){
			AcquisitionDialogs.showNoAcqMessage();
		} else {	
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Acquisition list", HTSMLMConstants.ACQ_EXT);
			fileChooser.setFileFilter(filter);
			int result = fileChooser.showSaveDialog(new JFrame());
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				String path = selectedFile.getAbsolutePath();
	
				acqcontroller_.saveExperiment(path);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////
	//////
	////// Tree summary methods
	//////
	
	private Point getSummaryButtonLocation(){
		Point newLoc = jButton_showSummary.getLocation();

		newLoc.x += owner_.getAcquisitionPanelLocation().getX()+100;
		newLoc.y += owner_.getAcquisitionPanelLocation().getY()+48;
		
		return newLoc;
	}
	
	private void showSummary(boolean b){
		if(b){
			summaryframe_ = new JFrame("Acquisitions summary");
			summaryframe_.setLocation(getSummaryButtonLocation());
			summaryframe_.setUndecorated(true);
			summaryframe_.setContentPane(ExperimentTreeSummary.getExperiment(controller_, acqcontroller_.getExperiment()));
			summaryframe_.pack();
			summaryframe_.setVisible(true);
		} else {
			if(summaryframe_ != null){
				summaryframe_.dispose();
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	//////
	////// PropertyPanel methods
	//////


	@Override
	protected void initializeProperties() {
		// Do nothing
	}

	@Override
	protected void initializeInternalProperties() {
		// Do nothing
	}
  
	@Override
	protected void initializeParameters() {
		paramBFP_ = UIPropertyParameter.NO_PROPERTY;
		paramLocking_ = UIPropertyParameter.NO_PROPERTY;
		paramBrightField_ = UIPropertyParameter.NO_PROPERTY;	
		
		addUIParameter(new UIPropertyParameter(this, PARAM_BFP,"UIProperty corresponding to the insertion of the BFP lens.", new TwoStateFlag()));
		addUIParameter(new UIPropertyParameter(this, PARAM_LOCKING,"UIProperty corresponding to the locking of the focus stabilization.", new FocusStabFlag())); 
		addUIParameter(new UIPropertyParameter(this, PARAM_BRIGHTFIELD,"UIProperty corresponding to the triggering of the white light illumination.", new TwoStateFlag())); 
	}

	@Override
	public void setupPanel() {
		initiPanel();
	}

	@Override
	public void propertyhasChanged(String name, String newvalue) {
		// Do nothing
	}

	@Override
	public void parameterhasChanged(String label) {
		if(label.equals(PARAM_BFP)){
			paramBFP_ = getUIPropertyParameterValue(PARAM_BFP);
		} else if(label.equals(PARAM_LOCKING)){
			paramLocking_ = getUIPropertyParameterValue(PARAM_LOCKING);
		} else if(label.equals(PARAM_BRIGHTFIELD)){
			paramBrightField_ = getUIPropertyParameterValue(PARAM_BRIGHTFIELD);
		}
	}
	
	@Override
	public void internalpropertyhasChanged(String label) {
		// Do nothing
	}

	@Override
	public void shutDown() {
		acqcontroller_.shutDown();
		if(summaryframe_ != null){
			summaryframe_.dispose();
		}
	}

	@Override
	public String getDescription() {
		return "Acquisition tab";
	}
	
	public String getParameterValues(String param) {
		if(param.equals(PARAM_BFP)){
			return paramBFP_;
		} else if(param.equals(PARAM_LOCKING)){
			return paramLocking_;
		} else if(param.equals(PARAM_BRIGHTFIELD)){
			return paramBrightField_;
		}
		return null;
	}

	public void updateProgressBar(int integer) {
		jProgressBar_progress.setValue(integer);
	}

	public String getExperimentName() {
		return jTextField_expname.getText();
	}

	public String getExperimentPath() {
		return jTextField_path.getText();
	}
	
	public void setStateButtonToStop(){
		jToggle_startstop.setSelected(false);
		jToggle_startstop.setText("Start");
	}
}
