package fr.spacefox.confusablehomoglyphs;

import java.util.Set;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
interface CategoriesApi {

    /**
     * Retrieves the script block alias and unicode category for a unicode character.
     * <pre>
     *     >>> categories.aliasAndCategory("A")
     *     ("LATIN", "L")
     *     >>> categories.aliasAndCategory("τ")
     *     ("GREEK", "L")
     *     >>> categories.aliasAndCategory("-")
     *     ("COMMON", 'Pd')
     * </pre>
     * @since 1.0.0
     * @param character A unicode character (if the string contains more than 1 character, only the 1st will be used)
     * @return The script block alias and unicode aliasAndCategory for a unicode character.
     */
    AliasAndCategory aliasAndCategory(String character);

    /**
     * Same as aliasAndCategory, directly on a character. <strong>Warning:</strong> does not handle characters on
     * supplementary planes like emojis.
     * @see #aliasAndCategory(String)
     * @since 1.0.0
     * @param character A unicode character
     * @return The script block alias and unicode aliasAndCategory for a unicode character.
     */
    AliasAndCategory aliasAndCategory(char character);

    /**
     * Retrieves the script block alias for a unicode character.
     * <pre>
     *     >>> categories.alias("A")
     *     "LATIN"
     *     >>> categories.alias("τ")
     *     "GREEK"
     *     >>> categories.alias("-")
     *     "COMMON"
     * </pre>
     * @since 1.0.0
     * @param character A unicode character (if the string contains more than 1 character, only the 1st will be used)
     * @return The script block alias.
     */
    String alias(String character);

    /**
     * Same as alias, directly on a character. <strong>Warning:</strong> does not handle characters on
     * supplementary planes like emojis.
     * @see #alias(String)
     * @since 1.0.0
     * @param character A unicode character
     * @return The script block alias.
     */
    String alias(char character);

    /**
     * Retrieves the unicode aliasAndCategory for a unicode character.
     * <pre>
     *     >>> categories.aliasAndCategory("A")
     *     "L"
     *     >>> categories.aliasAndCategory("τ")
     *     "L"
     *     >>> categories.aliasAndCategory("-")
     *     'Pd'
     * </pre>
     * @since 1.0.0
     * @param character A unicode character (if the string contains more than 1 character, only the 1st will be used)
     * @return The unicode aliasAndCategory for a unicode character.
     */
    String category(String character);

    /**
     * Same as aliasAndCategory, directly on a character. <strong>Warning:</strong> does not handle characters on
     * supplementary planes like emojis.
     * @see #category(String)
     * @since 1.0.0
     * @param character A unicode character
     * @return The unicode aliasAndCategory for a unicode character.
     */
    String category(char character);

    /**
     * Retrieves all unique script block aliases used in a unicode string.
     * <pre>
     *     >>> categories.uniqueAliases("ABC")
     *     {"LATIN"}
     *     >>> categories.uniqueAliases("ρAτ-")
     *     {"GREEK", "LATIN", "COMMON"}
     * </pre>
     * @since 1.0.0
     * @param string A unicode String
     * @return A set of the script block aliases used in a unicode string.
     */
    Set<String> uniqueAliases(String string);
}
