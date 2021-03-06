package com.bitfront.android.bitmapfont;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BitmapFont {
	
	public static class Glyph {
		@JsonProperty("glyph")
		public char glyph;
		
		@JsonProperty("x")
		public int x;
		
		@JsonProperty("y")
		public int y;

		@JsonProperty("w")
		public int w;
		
		public Glyph(char glyph, int x, int y, int w) {
			this.glyph = glyph;
			this.x = x;
			this.y = y;
			this.w = w;
		}
	}

	@JsonIgnore
	private BufferedImage bitmap;
	
	@JsonProperty("size")
	private int size;
	
	@JsonProperty("glyphs")
	private Map<Character, Glyph> glyphs;
	
	public BitmapFont(BufferedImage bitmap, int size, Map<Character, Glyph> glyphs) {
		this.bitmap = bitmap;
		this.size = size;
		this.glyphs = glyphs;
	}
	
	@JsonIgnore
	public BufferedImage getImage() {
		return bitmap;
	}
	
	@JsonIgnore
	public int getHeight() {
		return size;
	}
	
	public Glyph getGlyph(char c) {
		return glyphs.get(c);
	}
	
	public int stringWidth(String s) {
		int width = 0;
		final int length = s.length();
		for(int i = 0; i < length; i++) {
			Glyph g = getGlyph(s.charAt(i));
			if(g != null) {
				width += g.w;
			}
		}
		return width;
	}
}
