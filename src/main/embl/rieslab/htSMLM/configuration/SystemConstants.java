package main.embl.rieslab.htSMLM.configuration;

public class SystemConstants {
	
	// Mojo FPGA
	public static int FPGA_MAX_PULSE = 65535;
	public static int FPGA_MAX_SEQUENCE = 65535;
	public static int FPGA_BIT_DEPTH = 16;
	public static String[] FPGA_BEHAVIOURS = {"Off","On","Rising","Falling","Camera"};
	
	// NMS for activation
	public static int gaussianMaskSize = 3;
	public static double gaussianMaskPrecision = 0.02;
	public static int nmsMaskSize = 7;

	
}
