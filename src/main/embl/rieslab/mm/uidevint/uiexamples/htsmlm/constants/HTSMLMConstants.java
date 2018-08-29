package main.embl.rieslab.mm.uidevint.uiexamples.htsmlm.constants;

public class HTSMLMConstants {
	
	// Mojo FPGA
	public final static int FPGA_MAX_PULSE = 65535;
	public final static int FPGA_MAX_SEQUENCE = 65535;
	public final static int CAM_BIT_DEPTH = 16;
	public final static int FPGA_SEQUENCE_LENGTH = 16;
	public final static String[] FPGA_BEHAVIOURS = {"Off","On","Rising","Falling","Camera"};
	
	// NMS for activation
	public final static int gaussianMaskSize = 3;
	public final static double gaussianMaskPrecision = 0.02;
	public final static int nmsMaskSize = 7;
	
	// Acquisition
	public final static String ACQ_EXT = "uiacq";
	public final static String ACQ_NAME = "AcquisitionList."+ACQ_EXT;
}
