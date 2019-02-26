package main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.statistic;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.DoubleArrayImage;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.FocusMeasureInterface;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.DownScalingFocusMeasure;

/**
 * Focus measure based on the maximum pixel intensity.
 * 
 * @author royer
 */
public class Max extends DownScalingFocusMeasure implements
																								FocusMeasureInterface
{

	/**
	 * @see autopilot.measures.FocusMeasureInterface#computeFocusMeasure(autopilot.image.DoubleArrayImage)
	 */
	@Override
	public double computeFocusMeasure(final DoubleArrayImage pDoubleArrayImage)
	{
		return compute(pDoubleArrayImage, mPSFSupportDiameter);
	}

	/**
	 * Computes the focus measure based on the maximum pixel intensity.
	 * 
	 * 
	 * @param pDoubleArrayImage
	 *          image
	 * @param pPSFSupportDiameter
	 *          PSf support diameter
	 * @return focus measure value
	 */
	public static final double compute(	final DoubleArrayImage pDoubleArrayImage,
																			final double pPSFSupportDiameter)
	{
		final DoubleArrayImage lDownscaledImage = getDownscaledImage(	pDoubleArrayImage,
																																	pPSFSupportDiameter);

		final double lMax = lDownscaledImage.max();
		return lMax;
	}
}
