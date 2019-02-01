package main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.spectral;

import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.image.DoubleArrayImage;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.FocusMeasureInterface;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.FocusMeasures;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.DownScalingFocusMeasure;
import main.java.embl.rieslab.emu.uiexamples.htsmlm.autopilot.measures.implementations.statistic.LpSparsity;

/**
 * Discrete Cosine Transform Lp Sparsity focus measure.
 * 
 * @author royer
 */
public class DCTLpSparsity extends DownScalingFocusMeasure implements
																													FocusMeasureInterface
{
	public double mExponent = 2;

	/**
	 * @see autopilot.measures.FocusMeasureInterface#computeFocusMeasure(autopilot.image.DoubleArrayImage)
	 */
	@Override
	public double computeFocusMeasure(final DoubleArrayImage pDoubleArrayImage)
	{
		return compute(pDoubleArrayImage, mPSFSupportDiameter, mExponent);
	}

	/**
	 * Computes the Discrete Cosine Transform Lp Sparsity focus measure.
	 * 
	 * @param pDoubleArrayImage
	 *          image
	 * @return focus measure value
	 */
	public static final double compute(final DoubleArrayImage pDoubleArrayImage)
	{
		return compute(	pDoubleArrayImage,
										FocusMeasures.cPSFSupportDiameter,
										2);
	}

	/**
	 * Computes the Discrete Cosine Transform Lp Sparsity focus measure.
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
		return compute(pDoubleArrayImage, pPSFSupportDiameter, 2);
	}

	/**
	 * Computes the Discrete Cosine Transform Lp Sparsity focus measure.
	 * 
	 * @param pDoubleArrayImage
	 *          image
	 * @param pPSFSupportDiameter
	 *          PSF suppport diamteter
	 * @param pExponent
	 *          p exponent (in Lp)
	 * @return focus measure value
	 */
	public static final double compute(	final DoubleArrayImage pDoubleArrayImage,
																			final double pPSFSupportDiameter,
																			final double pExponent)
	{
		pDoubleArrayImage.dctforward();

		final double[] array = pDoubleArrayImage.getArray();
		final int width = pDoubleArrayImage.getWidth();
		final int height = pDoubleArrayImage.getHeight();
		final int otflimitx = (int) (width / pPSFSupportDiameter);
		final int otflimity = (int) (height / pPSFSupportDiameter);

		for (int y = 0; y < height; y++)
		{
			final int yi = y * width;
			for (int x = 0; x < width; x++)
			{
				final int i = yi + x;
				if (x > otflimitx | y > otflimity)
				{
					array[i] = 0;
				}
			}
		}

		return -LpSparsity.computeWithoutDownscaling(pExponent,pDoubleArrayImage);
	}

}
