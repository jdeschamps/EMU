package main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.spectral;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.DoubleArrayImage;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.wavelets.HaarWavelet;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.FocusMeasureInterface;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.DownScalingFocusMeasure;

/**
 * Normalized Haar Entropy (Shannon) focus measure.
 * 
 * @author royer
 */
public class NormHaarEntropyShannon extends DownScalingFocusMeasure	implements
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
	 * Computes the Normalized Haar Entropy (Shannon) focus measure.
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
		HaarWavelet.transform(lDownscaledImage);
		lDownscaledImage.normalizeNormL2();
		final int lWidth = lDownscaledImage.getWidth();
		final int lHeight = lDownscaledImage.getHeight();
		final double lEntropy = lDownscaledImage.entropyShannonSubRectangle(0,
																																				0,
																																				lWidth,
																																				lHeight,
																																				true);
		return lEntropy;
	}

}
