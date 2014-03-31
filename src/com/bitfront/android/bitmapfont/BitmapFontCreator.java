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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.bitfront.android.bitmapfont.BitmapFont.Glyph;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BitmapFontCreator {
	
	@Option(name="-f", aliases={"--font","--ttf"}, usage="TTF-file to create bitmap font from", required=true)
	private String ttf;
	
	@Option(name="-s", aliases={"--size"}, usage="Size to render font in (pixels)", required=true)
	private int size = -1;
	
	@Option(name="-g", aliases={"--glyphs"}, usage="path to a single line text file containing all glyphs/characters to render", required=true)
	private String glyphFile;
	
	@Option(name="-c", aliases={"--color"}, usage="Color to render font using. In hex format, including alpha. (AARRGGBB)", required=false)
	private String color = "FFFFFFFF";
	
	@Option(name="-a", aliases={"--antialias"}, usage="Render font with anti alias enabled", required=false)
	private boolean antiAlias = false;
	
	@Argument
	private List<String> arguments = new ArrayList<String>();
	
	public BitmapFontCreator() {
	}
	
	public BitmapFont create(String[] args) throws FontFormatException, IOException {
		CmdLineParser argsParser = new CmdLineParser(this);
		try {
			argsParser.parseArgument(args);
		}
		catch (CmdLineException e) {
			err("Usage: java -jar bitmapfontcreator.jar [options...]");
			argsParser.printUsage(System.err);
			return null;
		}

		final String glyphs = getFileAsString(glyphFile);

		BitmapFont font;
		try {
			font = create(ttf, size, glyphs, (int)Long.parseLong(color, 16), antiAlias);
		}
		catch (Exception e) {
			err("Unable to create bitmap font.");
			e.printStackTrace();
			argsParser.printUsage(System.err);
			return null;
		}
	
		File file = new File(ttf);
		String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));		

		log("Writing " + fileName + ".png");
		ImageIO.write(font.getImage(), "png", new FileOutputStream(new File(fileName + ".png")));
		
		log("Writing " + fileName + ".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(fileName + ".json"), font);
		
		log("Done");
		return font;
	}
	
	private BitmapFont create(String fontFile, int size, String glyphs, int argb, boolean antiAlias) throws FontFormatException, IOException {
		InputStream is = new FileInputStream(fontFile);
		Font font = Font.createFont(Font.TRUETYPE_FONT, is);
		font = font.deriveFont(Font.PLAIN, size);
		return create(font, size, glyphs, argb, antiAlias);
	}
	
	private BitmapFont create(Font font, int size, String glyphs, int argb, boolean antiAlias) {
		final int verticalSpacing = 3;
	
		FontMetrics fm = new Canvas().getFontMetrics(font);
		final int area = fm.stringWidth(glyphs) * (size + verticalSpacing);	
		final int width = Integer.highestOneBit((int)Math.ceil(Math.sqrt(area))) << 1;
		final int height = width;

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

		Graphics2D graphics = (Graphics2D)image.getGraphics();
		graphics.setColor(new Color(argb, true));
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
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
	
	
	public static void main(String[] args) {
		BitmapFontCreator bfc = new BitmapFontCreator();
		try {
			bfc.create(args);
		}
		catch (Exception e) {
			err("Unable to create bitmap font.");
			err(e.getMessage());
		}		
	}
	
	private String getFileAsString(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new FileInputStream(filename)).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : null;
	}
		
	private static void log(String string, Object ... args) {
		System.out.println(String.format(string, args));
	}
	
	private static void err(String string, Object ... args) {
		System.err.println(String.format(string, args));
	}

}
