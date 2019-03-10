package fr.spacefox.confusablehomoglyphs;

import java.util.List;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
public class Output {

    /**
     * A character or character sequence
     */
    public final String character;

    /**
     * The ISO 15924 alias name of the writing system of the given character.
     * See https://en.wikipedia.org/wiki/ISO_15924
     */
    public final String alias;

    /**
     * A list of characters or character sequences that can be confused with the current character
     */
    public final List<Homoglyphs> homoglyphs;

    public Output(String character, String alias, List<Homoglyphs> homoglyphs) {
        this.character = character;
        this.alias = alias;
        this.homoglyphs = homoglyphs;
    }
}
