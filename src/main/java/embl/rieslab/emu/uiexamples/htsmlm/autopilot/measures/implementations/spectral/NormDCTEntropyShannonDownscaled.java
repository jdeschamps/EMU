package main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.spectral;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.DoubleArrayImage;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.FocusMeasureInterface;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.DownScalingFocusMeasure;

/**
 * Normalized Discrete Cosine Tranform Entropy (Shannon) downscaled focus
 * measure.
 * 
 * @author royer
 */
public class NormDCTEntropyShannonDownscaled extends
																						DownScalingFocusMeasure	implements
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
	 * Computes the Normalized Discrete Cosine Transform Entropy (Shannon)
	 * downscaled focus measure.
	 * 
	 * @param pDoubleArrayImage
	 *          image
	 * @param pPSFSupportDiameter
	 *          PSF support diameter
	 * @return focus measure value
	 */
	public static final double compute(	final DoubleArrayImage pDoubleArrayImage,
																			final double pPSFSupportDiameter)
	{
		final DoubleArrayImage lDownscaledImage = getDownscaledImage(	pDoubleArrayImage,
																																	pPSFSupportDiameter);

		return NormDCTEntropyShannon.compute(lDownscaledImage, 1);
	}

}
