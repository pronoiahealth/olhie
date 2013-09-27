/*******************************************************************************
 * Copyright (c) 2013 Pronoia Health LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pronoia Health LLC - initial API and implementation
 *******************************************************************************/
package com.pronoiahealth.olhie.server.services;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.servlet.api.Web;

import com.lowagie.text.pdf.codec.Base64;
import com.pronoiahealth.olhie.client.shared.vo.Book;
import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;

/**
 * BookCoverImageService.java<br/>
 * Responsibilities:<br/>
 * 1.
 * 
 * @author John DeStefano
 * @version 1.0
 * @since Sep 11, 2013
 * 
 */
@ApplicationScoped
public class BookCoverImageService {
	@Inject
	private Logger log;

	@Inject
	@Web
	private ServletContext servletContext;

	@Inject
	private TempThemeHolder coverDisplayData;

	public enum Cover {
		FRONT, BACK
	};

	private Font medulaOneRegularFont;
	private Font medulaOneRegularFont48;
	private Font arialBoldFont13;
	private Font arialBoldFont16;
	private Map<String, byte[]> coverMap;
	private Hashtable<TextAttribute, Object> authorFontMap;
	private Hashtable<TextAttribute, Object> titleFontMap;
	private Hashtable<TextAttribute, Object> backTitleFontMap;

	/**
	 * Constructor
	 * 
	 */
	public BookCoverImageService() {
	}

	/**
	 * Initialize the various maps, fonts, etc..
	 */
	@PostConstruct
	protected void postActivate() {
		try {
			// Init the coverMap and load the images
			coverMap = new HashMap<String, byte[]>();
			List<BookCover> covers = coverDisplayData.getCovers();
			for (BookCover cover : covers) {
				coverMap.put(
						cover.getCoverName(),
						readResourceToByteArray("/" + cover.getImgUrl(),
								servletContext));
			}

			// Load needed fonts
			medulaOneRegularFont = Font
					.createFont(
							Font.TRUETYPE_FONT,
							servletContext
									.getResourceAsStream("/Olhie/font/MedulaOne-Regular.ttf"));
			medulaOneRegularFont48 = medulaOneRegularFont.deriveFont(new Float(
					48.0));
			arialBoldFont13 = new Font("Arial Bold", Font.BOLD, 13);
			arialBoldFont16 = new Font("Arial Bold", Font.ITALIC, 16);

			// Init font maps
			// author
			authorFontMap = new Hashtable<TextAttribute, Object>();
			authorFontMap.put(TextAttribute.FONT, arialBoldFont16);
			authorFontMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

			// Title
			titleFontMap = new Hashtable<TextAttribute, Object>();
			titleFontMap.put(TextAttribute.FONT, medulaOneRegularFont48);
			titleFontMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

			// Back cover title
			backTitleFontMap = new Hashtable<TextAttribute, Object>();
			backTitleFontMap.put(TextAttribute.FONT, arialBoldFont13);
			backTitleFontMap.put(TextAttribute.WEIGHT,
					TextAttribute.WEIGHT_BOLD);
			backTitleFontMap.put(TextAttribute.UNDERLINE,
					TextAttribute.UNDERLINE_LOW_ONE_PIXEL);

		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Error occured during BookCoverImageService initialization.",
					e);
		}
	}

	/**
	 * Method to return both the front and back covers in a map
	 * 
	 * 
	 * @param coverId
	 * @param logoBytes
	 * @param authorStr
	 * @param titleStr
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Map<Cover, String> createBookCovers(String coverId,
			byte[] logoBytes, String authorStr, String titleStr,
			String spineColor, String authorTextColor, String titleTextColor,
			int width, int height, int type, ImageFormat imgFormat)
			throws Exception {
		// Create the return make
		Map<Cover, String> retMap = new HashMap<Cover, String>();

		// Front cover
		retMap.put(
				Cover.FRONT,
				createFrontCoverEncoded(coverId, logoBytes, authorStr,
						titleStr, spineColor, authorTextColor, titleTextColor,
						width, height, type, imgFormat));

		// Back cover
		retMap.put(
				Cover.BACK,
				createBackCoverEncoded(coverId, titleStr, titleTextColor,
						width, height, type, imgFormat));

		// Return the map with the front and back cover
		return retMap;
	}

	/**
	 * @param book
	 * @param category
	 * @param cover
	 * @param logo
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<Cover, String> createDefaultBookCovers(Book book,
			BookCategory category, BookCover cover, byte[] logo,
			String authorName) throws Exception {
		// Create the return make
		Map<Cover, String> retMap = new HashMap<Cover, String>();

		// Front cover
		retMap.put(
				Cover.FRONT,
				createDefaultFrontCoverEncoded(book, category, cover, logo,
						authorName));

		// Back cover
		retMap.put(Cover.BACK, createDefaultBackCoverEncoded(book, cover));

		// Return the map with the front and back cover
		return retMap;
	}

	/**
	 * Create the front cover
	 * 
	 * @param coverId
	 * @param logoBytes
	 * @param authorStr
	 * @param titleStr
	 * @param textColor
	 *            - ex. #FFFFFF
	 * @param width
	 * @param height
	 * @param type
	 *            - ex. BufferedImage.TYPE_INT_ARGB
	 * @param imgFormat
	 *            - ex. ImageFormat.IMAGE_FORMAT_PNG
	 * @return
	 * @throws Exception
	 */
	public byte[] createFrontCover(String coverId, byte[] logoBytes,
			String authorStr, String titleStr, String spineColor,
			String authorTextColor, String titleTextColor, int width,
			int height, int type, ImageFormat imgFormat) throws Exception {

		Graphics2D g = null;

		try {
			// Front cover first
			// Read in base cover image
			BufferedImage coverImg = Imaging.getBufferedImage(coverMap
					.get(coverId));

			// Resize cover image to the basic 300 X 400 for front cover
			BufferedImage frontCoverImg = resize(coverImg, 300, 400, type);
			g = (Graphics2D) frontCoverImg.getGraphics();

			// Draw logo if present
			if (logoBytes != null && logoBytes.length > 0) {
				// Resize logo to 200x100
				BufferedImage logoImg = Imaging.getBufferedImage(logoBytes);
				BufferedImage outLogo = null;
				int logoHeight = logoImg.getHeight();
				int logoWidth = logoImg.getWidth();

				if (logoHeight > 100 || logoWidth > 200) {
					outLogo = this.resize(logoImg, logoWidth > 200 ? 200
							: logoWidth, logoHeight > 100 ? 100 : logoHeight,
							type);
				} else {
					outLogo = logoImg;
				}

				// Add to coverImg
				g.drawImage(outLogo, 32, 25, null);
			}

			// Add spine if present
			if (spineColor != null && spineColor.length() > 0) {
				g.setColor(Color.decode(spineColor));
				g.fillRect(0, 0, 2, frontCoverImg.getHeight());
			}

			// Add author if present
			if (authorStr != null && authorStr.length() > 0) {
				// Add author text to image
				BufferedImage authorTextImg = createText(40, 220, authorStr,
						authorTextColor, false, authorFontMap, type);
				g.drawImage(authorTextImg, 30, 215, null);
			}

			// Add title if present
			if (titleStr != null && titleStr.length() > 0) {
				BufferedImage titleTextImg = createText(100, 220, titleStr,
						titleTextColor, false, titleFontMap, type);
				g.drawImage(titleTextImg, 30, 240, null);
			}

			// If the requested size is not 300X400 convert the image
			BufferedImage outImg = null;
			if (width != 300 || height != 400) {
				outImg = resize(frontCoverImg, width, height, type);
			} else {
				outImg = frontCoverImg;
			}

			// Return bytes
			Map<String, Object> params = new HashMap<String, Object>();
			byte[] outBytes = Imaging.writeImageToBytes(outImg, imgFormat,
					params);
			return outBytes;
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
	}

	/**
	 * Create the front cover and return as base 64 encoded string
	 * 
	 * @param coverId
	 * @param logoBytes
	 * @param authorStr
	 * @param titleStr
	 * @param textColor
	 *            - ex. #FFFFFF
	 * @param width
	 * @param height
	 * @param type
	 *            - ex. BufferedImage.TYPE_INT_ARGB
	 * @param imgFormat
	 *            - ex. ImageFormat.IMAGE_FORMAT_PNG
	 * @return
	 * @throws Exception
	 */
	public String createFrontCoverEncoded(String coverId, byte[] logoBytes,
			String authorStr, String titleStr, String spineColor,
			String authorTextColor, String titleTextColor, int width,
			int height, int type, ImageFormat imgFormat) throws Exception {

		return Base64.encodeBytes(createFrontCover(coverId, logoBytes,
				authorStr, titleStr, spineColor, authorTextColor,
				titleTextColor, width, height, type, imgFormat));
	}

	/**
	 * 
	 * @param book
	 * @param category
	 * @param cover
	 * @param logo
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public String createDefaultFrontCoverEncoded(Book book,
			BookCategory category, BookCover cover, byte[] logo,
			String authorName) throws Exception {
		return createFrontCoverEncoded(book.getCoverName(), logo, authorName,
				book.getBookTitle(), category.getColor(),
				cover.getAuthorTextColor(), cover.getCoverTitleTextColor(),
				300, 400, BufferedImage.TYPE_INT_ARGB,
				ImageFormat.IMAGE_FORMAT_PNG);
	}

	/**
	 * Create a back cover
	 * 
	 * @param coverId
	 * @param titleStr
	 * @param textColor
	 * @param width
	 * @param height
	 * @param type
	 * @param imgFormat
	 * @return
	 * @throws Exception
	 */
	public byte[] createBackCover(String coverId, String titleStr,
			String textColor, int width, int height, int type,
			ImageFormat imgFormat) throws Exception {

		Graphics2D g = null;

		try {
			// Front cover first
			// Read in base cover image
			BufferedImage coverImg = Imaging.getBufferedImage(coverMap
					.get(coverId));

			// Resize cover image to the basic 300 X 400 for front cover
			BufferedImage backCoverImg = resize(coverImg, 300, 400, type);
			g = (Graphics2D) backCoverImg.getGraphics();

			// Add title if present
			if (titleStr != null && titleStr.length() > 0) {
				BufferedImage titleTextImg = createText(82, 220, titleStr,
						textColor, true, backTitleFontMap, type);
				g.drawImage(titleTextImg, 40, 35, null);
			}

			// If the requested size is not 300X400 convert the image
			BufferedImage outImg = null;
			if (width != 300 || height != 400) {
				outImg = resize(backCoverImg, width, height, type);
			} else {
				outImg = backCoverImg;
			}

			// Return bytes
			Map<String, Object> params = new HashMap<String, Object>();
			byte[] outBytes = Imaging.writeImageToBytes(outImg, imgFormat,
					params);
			return outBytes;
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
	}

	/**
	 * Create a back cover
	 * 
	 * @param coverId
	 * @param titleStr
	 * @param textColor
	 * @param width
	 * @param height
	 * @param type
	 * @param imgFormat
	 * @return
	 * @throws Exception
	 */
	public String createBackCoverEncoded(String coverId, String titleStr,
			String textColor, int width, int height, int type,
			ImageFormat imgFormat) throws Exception {

		return Base64.encodeBytes(createBackCover(coverId, titleStr, textColor,
				width, height, type, imgFormat));
	}

	/**
	 * @param book
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public String createDefaultBackCoverEncoded(Book book, BookCover cover)
			throws Exception {
		return createBackCoverEncoded(book.getCoverName(), book.getBookTitle(),
				cover.getCoverTitleTextColor(), 300, 400, BufferedImage.TYPE_INT_ARGB,
				ImageFormat.IMAGE_FORMAT_PNG);
	}

	/**
	 * Create an image that contains text
	 * 
	 * @param height
	 * @param width
	 * @param text
	 * @param textColor
	 * @param center
	 * @param fontMap
	 * @param type
	 * @return
	 */
	private BufferedImage createText(int height, int width, String text,
			String textColor, boolean center,
			Map<TextAttribute, Object> fontMap, int type) {
		BufferedImage img = new BufferedImage(width, height, type);
		Graphics2D g2d = null;
		try {
			g2d = (Graphics2D) img.getGraphics();

			// Create attributed text
			AttributedString txt = new AttributedString(text, fontMap);

			// Set graphics color
			g2d.setColor(Color.decode(textColor));

			// Create a new LineBreakMeasurer from the paragraph.
			// It will be cached and re-used.
			AttributedCharacterIterator paragraph = txt.getIterator();
			int paragraphStart = paragraph.getBeginIndex();
			int paragraphEnd = paragraph.getEndIndex();
			FontRenderContext frc = g2d.getFontRenderContext();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
					frc);

			// Set break width to width of Component.
			float breakWidth = (float) width;
			float drawPosY = 0;
			// Set position to the index of the first character in the
			// paragraph.
			lineMeasurer.setPosition(paragraphStart);

			// Get lines until the entire paragraph has been displayed.
			while (lineMeasurer.getPosition() < paragraphEnd) {
				// Retrieve next layout. A cleverer program would also cache
				// these layouts until the component is re-sized.
				TextLayout layout = lineMeasurer.nextLayout(breakWidth);

				// Compute pen x position. If the paragraph is right-to-left we
				// will align the TextLayouts to the right edge of the panel.
				// Note: drawPosX is always where the LEFT of the text is
				// placed.
				float drawPosX = layout.isLeftToRight() ? 0 : breakWidth
						- layout.getAdvance();

				if (center == true) {
					double xOffSet = (width - layout.getBounds().getWidth()) / 2;
					drawPosX = drawPosX + new Float(xOffSet);
				}

				// Move y-coordinate by the ascent of the layout.
				drawPosY += layout.getAscent();

				// Draw the TextLayout at (drawPosX, drawPosY).
				layout.draw(g2d, drawPosX, drawPosY);

				// Move y-coordinate in preparation for next layout.
				drawPosY += layout.getDescent() + layout.getLeading();
			}
		} finally {
			if (g2d != null) {
				g2d.dispose();
			}
		}
		return img;
	}

	/**
	 * Resize an image
	 * 
	 * @param originalImage
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 */
	private BufferedImage resize(BufferedImage originalImage, int width,
			int height, int type) {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		try {
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(originalImage, 0, 0, width, height, null);
		} finally {
			if (g != null) {
				g.dispose();
			}
		}

		return resizedImage;
	}

	/**
	 * Assists in preloading images
	 * 
	 * @param resourceLocation
	 * @param loaderClass
	 * @return
	 * @throws Exception
	 */
	private byte[] readResourceToByteArray(String resourceLocation,
			ServletContext servletContext) throws Exception {
		return IOUtils.toByteArray(servletContext
				.getResourceAsStream(resourceLocation));
	}
}
