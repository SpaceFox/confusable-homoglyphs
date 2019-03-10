# Confusables Homoglyphs

_A JVM (Java) port of https://github.com/vhf/confusable_homoglyphs/_.

_A homoglyph is one of two or more graphemes, characters, or glyphs with shapes that appear identical or very similar
([Wikipedia](https://en.wikipedia.org/wiki/Homoglyph))_.

Unicode homoglyphs can be a nuisance on the web. Your most popular client, AlaskaJazz, might be upset to be impersonated
by a trickster who deliberately chose the username ΑlaskaJazz.

- `AlaskaJazz` is single script: only Latin characters.
- `ΑlaskaJazz` is mixed-script: the first character is a greek letter.

You might also want to avoid people being tricked into entering their password on `www.microsоft.com` or
`www.faϲebook.com` instead of `www.microsoft.com` or `www.facebook.com`.
[Here is a utility](http://unicode.org/cldr/utility/confusables.jsp) to play with these confusable homoglyphs.

Not all mixed-script strings have to be ruled out though, you could only exclude mixed-script strings containing
characters that might be confused with a character from some unicode blocks of your choosing.

- `Allo` and `ρττ` are fine: single script.
- `AlloΓ` is fine when our preferred script alias is "latin": mixed script, but `Γ` is not confusable.
- `Alloρ` is dangerous: mixed script and `ρ` could be confused with `p`.

This library is compatible Java 8 and above. It should work with any JVM language that allows use of pure Java libraries.

# Is the data up to date?

Embedded data are up-to-date with Unicode 12.

The unicode blocks aliases and names for each character are extracted from
[this file](http://www.unicode.org/Public/UNIDATA/Scripts.txt) provided by the unicode consortium.

The matrix of which character can be confused with which other characters is built using
[this file](http://www.unicode.org/Public/security/latest/confusables.txt) provided by the unicode consortium.

This data is stored in two JSON files: `categories.json` and `confusables.json`, embedded directly in the provided JAR.
As these files can be outdated by future Unicode relase, the JAR itsef provides a CLI tool to rebuild these files
directly from sources. You can therefore use this library from external JSON instead of provided one.

Please note the Unicode website is quite unstable and may not respond at all. This implies: 

- There is no auto-rebuild of JSON directly from Unicode data.
- You can provide your own copies of Unicode source file to the JSON-rebuilder tool.
