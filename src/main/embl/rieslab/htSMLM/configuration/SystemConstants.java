package main.embl.rieslab.htSMLM.configuration;

public class SystemConstants {
	
	// Mojo FPGA
	public final static int FPGA_MAX_PULSE = 65535;
	public final static int FPGA_MAX_SEQUENCE = 65535;
	public final static int FPGA_BIT_DEPTH = 16;
	public final static String[] FPGA_BEHAVIOURS = {"Off","On","Rising","Falling","Camera"};
	
	// NMS for activation
	public final static int gaussianMaskSize = 3;
	public final static double gaussianMaskPrecision = 0.02;
	public final static int nmsMaskSize = 7;

	
}
