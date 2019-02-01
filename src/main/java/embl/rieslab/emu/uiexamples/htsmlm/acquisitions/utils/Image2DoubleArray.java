package main.java.embl.rieslab.emu.uiexamples.htsmlm.acquisitions.utils;

import org.micromanager.data.Image;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.DoubleArrayImage;

public class Image2DoubleArray {

	public DoubleArrayImage convert(Image im){

		double[] pixels = new double[im.getWidth()+im.getHeight()];
		for(int i=0;i<im.getWidth();i++){
			for(int j=0;j<im.getHeight();j++){
				pixels[j+i*im.getHeight()] = (double) im.getIntensityAt(i, j);
			}
		}
		DoubleArrayImage image = new DoubleArrayImage(im.getWidth(),im.getHeight(), pixels);
		
		return image;
	}
}
