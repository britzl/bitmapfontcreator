package com.bitfront.android.bitmapfont;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.bitfront.android.bitmapfont.BitmapFont.Glyph;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BitmapFontCreator {

	private static void log(String string, Object ... args) {
		System.out.println(String.format(string, args));
	}
	
	private static void err(String string, Object ... args) {
		System.err.println(String.format(string, args));
	}
	
	private static void showUsage() {
		log("Create a bitmap font with a specific size, typeface and with a specific set of glyphs/characters");
		log("");
		log("Usage: bitmapfontcreator [TTF-file] [size] [glyph-file]");
		log("");
		log("	TTF-file = path to a valid TTF font");
		log("	size = size to render font in (pixels)");
		log("	glyph-file = path to a single line text file containing all glyphs/characters to render");
		log("");
		log("	Will write a png and a json file in the current directory, both named the same as the TTF-file");
	}
	
	private static BitmapFont create(String fontFile, int size, String glyphs) throws FontFormatException, IOException {
		InputStream is = new FileInputStream(fontFile);
		Font font = Font.createFont(Font.TRUETYPE_FONT, is);
		font = font.deriveFont(Font.PLAIN, size);
		return create(font, size, glyphs);
	}
	
	private static BitmapFont create(Font font, int size, String glyphs) {
		final int verticalSpacing = 3;
	
		FontMetrics fm = new Canvas().getFontMetrics(font);
		final int area = fm.stringWidth(glyphs) * (size + verticalSpacing);	
		final int width = Integer.highestOneBit((int)Math.ceil(Math.sqrt(area))) << 1;
		final int height = width;

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

		Graphics2D graphics = (Graphics2D)image.getGraphics();
		graphics.setColor(new Color(0xFFFFFFFF, true));
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Map<Character, Glyph> glyphMap = new HashMap<Character, BitmapFont.Glyph>();
		
		final int ascent = (int)fm.getAscent();
		final int glyphCount = glyphs.length();
		int x = 0;
		int y = ascent;
		for(int i = 0; i < glyphCount; i++) {
			char glyph = glyphs.charAt(i);
			String glyphString = Character.toString(glyph);
			Rectangle2D r2d = fm.getStringBounds(glyphString, graphics);
			int glyphWidth = r2d.getBounds().width;
			if (x + glyphWidth > width) {
				x = 0;
				y = y + size + verticalSpacing;
			}
			graphics.drawString(glyphString, x, y);
			glyphMap.put(glyph, new BitmapFont.Glyph(glyph, x, y, glyphWidth));
			x += glyphWidth;
		}

		BitmapFont bitmapFont = new BitmapFont(image, size, glyphMap);
		return bitmapFont;
	}
	
	
	private static String getFileAsString(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new FileInputStream(filename)).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : null;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		if(args.length < 3) {
			showUsage();
			System.exit(1);
			return;
		}
		
		final String fontFile = args[0];
		final int size = Integer.parseInt(args[1]);
		final String glyphFile = args[2];
		
		final String glyphs = getFileAsString(glyphFile);
		
		File file = new File(fontFile);
		String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
		
		BitmapFont font;
		try {
			font = create(fontFile, size, glyphs);
		}
		catch (Exception e) {
			err("Unable to create bitmap font.");
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		log("Writing " + fileName + ".png");
		ImageIO.write(font.getImage(), "png", new FileOutputStream(new File(fileName + ".png")));
		
		log("Writing " + fileName + ".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(fileName + ".json"), font);
		
		log("Done");
	}
}
