bitmapfontcreator
=================

Create a bitmap font with a specific size, typeface and with a specific set of glyphs/characters

usage
=====
Usage:
```
java -jar bitmapfontcreator.jar [options...]
 -a (--antialias)       : Render font with anti alias enabled
 -c (--color) VAL       : Color to render font using. In hex format, including
                          alpha. (AARRGGBB)
 -f (--font, --ttf) VAL : TTF-file to create bitmap font from
 -g (--glyphs) VAL      : path to a single line text file containing all
                          glyphs/characters to render
 -o (--out) VAL         : Output directory to write PNG and JSON file to
 -s (--size) N          : Size to render font in (pixels)
```
Will write a png and a json file in the current directory, both named the same as the TTF-file and with the size appended to the filenames.

png info
========
The PNG will be rectangular and have a power-of-two width (and height). The PNG will have a transparent background and white, anti-aliased font.

json format
===========
The JSON format contains the size and an associative array/mapping between glyph and glyph meta-data. The glyph meta-data contains the top-left pixel of the glyph and the glyph width.

example
=======
java -jar bitmapfontcreator.jar --ttf impact.ttf --size 15 --glyphs glyphs_se.txt --antialias --color FF000000

Where glyphs_se.txt is a single line text file with the following characters:

 !?.,-+*_/1234567890abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ

Will output impact_15.png and impact_15.json

impact_15.png:

![impact_15.png](https://raw.githubusercontent.com/britzl/bitmapfontcreator/master/impact_15.png)

impact.json:
```JSON
{
	"size":15,
	"glyphs": {
		" ":{"glyph":" ","x":0,"y":16,"w":3},
		"!":{"glyph":"!","x":3,"y":16,"w":4},
		"*":{"glyph":"*","x":33,"y":16,"w":4},
		"+":{"glyph":"+","x":25,"y":16,"w":8},
		".":{"glyph":".","x":15,"y":16,"w":3},
		"/":{"glyph":"/","x":45,"y":16,"w":6},
		",":{"glyph":",","x":18,"y":16,"w":3},
		"-":{"glyph":"-","x":21,"y":16,"w":4},
		"3":{"glyph":"3","x":65,"y":16,"w":8},
		"2":{"glyph":"2","x":57,"y":16,"w":8},
		"1":{"glyph":"1","x":51,"y":16,"w":6},
		"0":{"glyph":"0","x":118,"y":16,"w":8},
		"7":{"glyph":"7","x":96,"y":16,"w":6},
		"6":{"glyph":"6","x":88,"y":16,"w":8},
		"5":{"glyph":"5","x":80,"y":16,"w":8},
		"4":{"glyph":"4","x":73,"y":16,"w":7},
		"9":{"glyph":"9","x":110,"y":16,"w":8},
		"8":{"glyph":"8","x":102,"y":16,"w":8},
		"?":{"glyph":"?","x":7,"y":16,"w":8},
		"D":{"glyph":"D","x":104,"y":52,"w":8},
		"E":{"glyph":"E","x":112,"y":52,"w":6},
		"F":{"glyph":"F","x":118,"y":52,"w":6},
		"G":{"glyph":"G","x":0,"y":70,"w":8},
		"A":{"glyph":"A","x":80,"y":52,"w":8},
		"B":{"glyph":"B","x":88,"y":52,"w":8},
		"C":{"glyph":"C","x":96,"y":52,"w":8},
		"Å":{"glyph":"Å","x":32,"y":88,"w":8},
		"L":{"glyph":"L","x":33,"y":70,"w":6},
		"Ä":{"glyph":"Ä","x":40,"y":88,"w":8},
		"M":{"glyph":"M","x":39,"y":70,"w":11},
		"N":{"glyph":"N","x":50,"y":70,"w":8},
		"O":{"glyph":"O","x":58,"y":70,"w":8},
		"H":{"glyph":"H","x":8,"y":70,"w":8},
		"I":{"glyph":"I","x":16,"y":70,"w":4},
		"J":{"glyph":"J","x":20,"y":70,"w":5},
		"K":{"glyph":"K","x":25,"y":70,"w":8},
		"U":{"glyph":"U","x":105,"y":70,"w":8},
		"T":{"glyph":"T","x":98,"y":70,"w":7},
		"W":{"glyph":"W","x":0,"y":88,"w":12},
		"V":{"glyph":"V","x":113,"y":70,"w":8},
		"Q":{"glyph":"Q","x":74,"y":70,"w":8},
		"P":{"glyph":"P","x":66,"y":70,"w":8},
		"S":{"glyph":"S","x":90,"y":70,"w":8},
		"R":{"glyph":"R","x":82,"y":70,"w":8},
		"Ö":{"glyph":"Ö","x":48,"y":88,"w":8},
		"_":{"glyph":"_","x":37,"y":16,"w":8},
		"Y":{"glyph":"Y","x":19,"y":88,"w":7},
		"X":{"glyph":"X","x":12,"y":88,"w":7},
		"Z":{"glyph":"Z","x":26,"y":88,"w":6},
		"f":{"glyph":"f","x":39,"y":34,"w":4},
		"g":{"glyph":"g","x":43,"y":34,"w":8},
		"d":{"glyph":"d","x":23,"y":34,"w":8},
		"e":{"glyph":"e","x":31,"y":34,"w":8},
		"b":{"glyph":"b","x":8,"y":34,"w":8},
		"c":{"glyph":"c","x":16,"y":34,"w":7},
		"a":{"glyph":"a","x":0,"y":34,"w":8},
		"n":{"glyph":"n","x":90,"y":34,"w":8},
		"o":{"glyph":"o","x":98,"y":34,"w":8},
		"l":{"glyph":"l","x":74,"y":34,"w":4},
		"å":{"glyph":"å","x":56,"y":52,"w":8},
		"m":{"glyph":"m","x":78,"y":34,"w":12},
		"ä":{"glyph":"ä","x":64,"y":52,"w":8},
		"j":{"glyph":"j","x":63,"y":34,"w":4},
		"k":{"glyph":"k","x":67,"y":34,"w":7},
		"h":{"glyph":"h","x":51,"y":34,"w":8},
		"i":{"glyph":"i","x":59,"y":34,"w":4},
		"w":{"glyph":"w","x":27,"y":52,"w":10},
		"v":{"glyph":"v","x":20,"y":52,"w":7},
		"u":{"glyph":"u","x":12,"y":52,"w":8},
		"t":{"glyph":"t","x":7,"y":52,"w":5},
		"s":{"glyph":"s","x":0,"y":52,"w":7},
		"r":{"glyph":"r","x":122,"y":34,"w":5},
		"q":{"glyph":"q","x":114,"y":34,"w":8},
		"p":{"glyph":"p","x":106,"y":34,"w":8},
		"ö":{"glyph":"ö","x":72,"y":52,"w":8},
		"z":{"glyph":"z","x":51,"y":52,"w":5},
		"y":{"glyph":"y","x":44,"y":52,"w":7},
		"x":{"glyph":"x","x":37,"y":52,"w":7}
	}
}
```
