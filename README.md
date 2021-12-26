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

# How to use it in my project ?

This library [is available on Maven Central](https://search.maven.org/artifact/fr.spacefox/confusable-homoglyphs).

Import declarations:

```xml
<dependency>
    <groupId>fr.spacefox</groupId>
    <artifactId>confusable-homoglyphs</artifactId>
    <version>1.0.1</version>
</dependency>
```

```groovy
compile group: 'fr.spacefox', name: 'confusable-homoglyphs', version: '1.0.1'
```

Then create a `Categories` or a `Confusable` object, from internal data or from external JSON:

```java
Categories categories = Categories.fromInternal();
Confusables confusables = Confusables.fromInternal();
// Or
Categories categories = Categories.fromJson("/full/path/to/categories.json");
Confusables confusables = fromJsons("/full/path/to/categories.json", "/full/path/to/confusables.json");
// (Confusables uses Categories internally).
```

Please note: constructing these objects is quite long, you should keep them alive between two calls.

# Public API

See Javadoc for details.

## `Categories`

- `AliasAndCategory aliasAndCategory(String character)`
- `AliasAndCategory aliasAndCategory(char character)`
- `String alias(String character)`
- `String alias(char character)`
- `String category(String character)`
- `String category(char character)`
- `Set<String> uniqueAliases(String string)`

With `AliasAndCategory` return type:

- `public final String alias;`
- `public final String category;`

# `Confusables`

- `boolean isMixedScript(String string, Collection<String> allowedAliases)`
- `boolean isMixedScript(String string, String... allowedAliases)`
- `boolean isMixedScript(String string)`
- `List<Output> isConfusable(String string, boolean greedy, Collection<String> preferredAliases)`
- `List<Output> isConfusable(String string, boolean greedy, String... preferredAliases)`
- `List<Output> isConfusable(String string, boolean greedy)`
- `List<Output> isConfusable(String string, Collection<String> preferredAliases)`
- `List<Output> isConfusable(String string, String... preferredAliases)`
- `List<Output> isConfusable(String string)`
- `boolean isDangerous(String string, Collection<String> preferredAliases)`
- `boolean isDangerous(String string, String... preferredAliases)`
- `boolean isDangerous(String string)`

With `Output` return type:

- `public final String character;`
- `public final String alias;`
- `public final List<Homoglyphs> homoglyphs;`

With `Homoglyphs` object:

- `public final String sequence;`
- `public final String name;`

# Is the data up to date?

Embedded data are up-to-date with Unicode 14.

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
