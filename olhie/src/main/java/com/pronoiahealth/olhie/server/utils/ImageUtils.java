package com.pronoiahealth.olhie.server.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

/**
 * ImageUtils.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author John DeStefano
 * @version 1.0
 * @since Oct 2, 2013
 *
 */
public class ImageUtils {

	/**
	 * Reduce a 24 bit image to the given number of colors. Support fully
	 * transparent pixels, but not partially transparent pixels.
	 * 
	 * @param bi
	 * @param colors
	 * @return BufferedImage with reduced colors.
	 */
	public static BufferedImage reduce24(BufferedImage bi, int colors) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		int[][] pixels = new int[width][height];
		boolean[][] transparent = new boolean[width][height];

		WritableRaster r1 = bi.getRaster();
		boolean inputHasAlpha = bi.getColorModel().hasAlpha();
		int[] argb = new int[4];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// this is easier than bi.getRGB, but still heavy
				argb = r1.getPixel(x, y, argb);
				int r = argb[0];
				int g = argb[1];
				int b = argb[2];
				pixels[x][y] = /* (a << 24) | */(r << 16) | (g << 8) | (b);

				if (inputHasAlpha) {
					int a = argb[3];
					pixels[x][y] |= (a << 24);
					// decide if pixel is transparent or not
					transparent[x][y] = (a < 128) ? true : false;
				}
			}
		}

		int[] palette = Quantize.quantizeImage(pixels, colors - 1);

		byte[] r = new byte[colors];
		byte[] g = new byte[colors];
		byte[] b = new byte[colors];
		byte[] a = new byte[colors];

		// need to have full(256) size array to get rid of ugly rare errors msg
		// from GIFImageWriter. We also need the *first* entry to be
		// transparent.
		// Arrays.fill(r, (byte) Color.clear.getRed());
		// Arrays.fill(g, (byte) Color.clear.getGreen());
		// Arrays.fill(b, (byte) Color.clear.getBlue());
		// Arrays.fill(a, (byte) Color.clear.getAlpha());

		for (int i = 0; i < palette.length; i++) {
			Color c = new Color(palette[i], true);
			r[i + 1] = (byte) c.getRed();
			g[i + 1] = (byte) c.getGreen();
			b[i + 1] = (byte) c.getBlue();
			a[i + 1] = (byte) c.getAlpha();
		}

		IndexColorModel colorModel = new IndexColorModel(8, r.length, r, g, b,
				a);

		// create a image with the reduced colors
		BufferedImage reducedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_INDEXED, colorModel);

		// manipulate raster directly for best performance & color match
		WritableRaster raster = reducedImage.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// add 1 as transparent is first
				int value = transparent[x][y] ? 0 : (pixels[x][y] + 1);
				raster.setSample(x, y, 0, value);
			}
		}

		return reducedImage;
	}

	/**
	 * Reduce a 32 bit image to the given number of colors. Support partially
	 * transparent pixels.
	 * 
	 * @param bi
	 * @param colors
	 * @return BufferedImage with reduced colors.
	 */
	public static BufferedImage reduce32(BufferedImage bi, int colors) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		int[][] pixels = new int[width][height];

		WritableRaster r1 = bi.getRaster();
		int[] argb = new int[4];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// this is easier than bi.getRGB, but still heavy
				argb = r1.getPixel(x, y, argb);
				int a = argb[3];
				int r = argb[0];
				int g = argb[1];
				int b = argb[2];
				pixels[x][y] = (a << 24) | (r << 16) | (g << 8) | (b);
			}
		}

		int[] palette = Quantize32.quantizeImage(pixels, colors);
		colors = palette.length;

		// ImageIO (at least on Mac) does not like to *read* png images with
		// only a single color in the color index
		boolean useExtraColors = false;
		int minimumColors = 2;
		if (colors < minimumColors) {
			colors = minimumColors;
			useExtraColors = true;
		}

		byte[] r = new byte[colors];
		byte[] g = new byte[colors];
		byte[] b = new byte[colors];
		byte[] a = new byte[colors];

		if (useExtraColors) {
			// can not be clear as ArcGIS does not handle PNG with multiple
			// clear entries in the color index
			Arrays.fill(r, (byte) Color.green.getRed());
			Arrays.fill(g, (byte) Color.green.getGreen());
			Arrays.fill(b, (byte) Color.green.getBlue());
			Arrays.fill(a, (byte) Color.green.getAlpha());
		}

		for (int i = 0; i < palette.length; i++) {
			Color c = new Color(palette[i], true);
			r[i] = (byte) c.getRed();
			g[i] = (byte) c.getGreen();
			b[i] = (byte) c.getBlue();
			a[i] = (byte) c.getAlpha();
		}

		IndexColorModel colorModel = new IndexColorModel(8, r.length, r, g, b,
				a);

		// create a image with the reduced colors
		BufferedImage reducedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_INDEXED, colorModel);

		// manipulate raster directly for best performance & color match
		WritableRaster raster = reducedImage.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int value = pixels[x][y];
				raster.setSample(x, y, 0, value);
			}
		}

		return reducedImage;
	}
}