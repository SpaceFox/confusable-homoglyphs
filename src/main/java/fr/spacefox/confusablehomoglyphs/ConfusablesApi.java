package fr.spacefox.confusablehomoglyphs;

import java.util.Collection;
import java.util.List;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
interface ConfusablesApi {

    /**
     * Checks if <code>string</code> contains mixed-scripts content, excluding script
     *     blocks aliases in <code>allowed_aliases</code>.<br/>
     *     E.g. <code>B. C</code> is not considered mixed-scripts by default: it contains characters
     *     from <strong>Latin</strong> and <strong>Common</strong>, but <strong>Common</strong> is excluded by default.
     * <pre>
     *     >>> confusables.isMixedScript("Abç")
     *     False
     *     >>> confusables.isMixedScript("ρτ.τ")
     *     False
     *     >>> confusables.isMixedScript("ρτ.τ", Collections.emptyList())
     *     True
     *     >>> confusables.isMixedScript("Alloτ")
     *     True
     * </pre>
     * @since 1.0.0
     * @param string A unicode string
     * @param allowedAliases Script blocks aliases not to consider.
     * @return Whether <code>string</code> is considered mixed-scripts or not.
     */
    boolean isMixedScript(String string, Collection<String> allowedAliases);

    /**
     * Same as isMixedScript(String, Collection) but with allowed aliases as string array.
     * @see #isMixedScript(String, Collection)
     * @since 1.0.0
     * @param string A unicode string
     * @param allowedAliases Script blocks aliases not to consider.
     * @return Whether <code>string</code> is considered mixed-scripts or not.
     */
    boolean isMixedScript(String string, String... allowedAliases);

    /**
     * Same as isMixedScript with default value ["COMMON"] for allowed aliases.
     * @since 1.0.0
     * @see #isMixedScript(String, Collection)
     * @param string A unicode string
     * @return Whether <code>string</code> is considered mixed-scripts or not.
     */
    boolean isMixedScript(String string);

    /**
     * Checks if <code>string</code> contains characters which might be confusable with
     *     characters from <code>preferredAliases</code>.<br/>
     *     If <code>greedy == false</code>, it will only return the first confusable character
     *     found without looking at the rest of the string, <code>greedy == true</code> returns
     *     all of them.<br/>
     *     <code>preferredAliases == []</code> can take a list of unicode block aliases to
     *     be considered as your "base" unicode blocks:
     *     <ul>
     *       <li>considering <code>paρa</code>,
     *          <ul>
     *        <li>with <code>preferredAliases == ["latin"]</code>, the 3rd character <code>ρ</code>
     *           would be returned because this greek letter can be confused with
     *           latin <code>p</code>.</li>
     *        <li>with <code>preferredAliases == ["greek"]</code>, the 1st character <code>p</code>
     *           would be returned because this latin letter can be confused with
     *           greek <code>ρ</code>.</li>
     *        <li>with <code>preferredAliases == []</code> and <code>greedy == true</code>, you'll discover
     *           the 29 characters that can be confused with <code>p</code>, the 23
     *           characters that look like <code>a</code>, and the one that looks like <code>ρ</code>
     *           (which is, of course, *p* aka *LATIN SMALL LETTER P*).</li>
     *           </ul>
     *      </li>
     *    </ul>
     * <pre>
     *     >>> confusables.isConfusable("paρa", Collections.singleton("latin"))[0].character
     *     "ρ"
     *     >>> confusables.isConfusable("paρa", Collections.singleton("greek"))[0].character
     *     "p"
     *     >>> confusables.isConfusable("Abç", Collections.singleton("latin"))
     *     []
     *     >>> confusables.isConfusable("AlloΓ", Collections.singleton("latin"))
     *     []
     *     >>> confusables.isConfusable("ρττ", Collections.singleton("greek"))
     *     []
     *     >>> confusables.isConfusable("ρτ.τ", Arrays.asList("greek", "common"))
     *     []
     *     >>> confusables.isConfusable("ρττp")
     *     [{"homoglyphs": [{"c": "p", "n": "LATIN SMALL LETTER P"}], "alias": "GREEK", "character": "ρ"}]
     * </pre>
     * @since 1.0.0
     * @param string A unicode string
     * @param greedy Don't stop on finding one confusable character - find all of them.
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string, boolean greedy, Collection<String> preferredAliases);

    /**
     * Same as isConfusable(String, boolean, Collection) but with allowed aliases as string array.
     * @see #isConfusable(String, boolean, Collection)
     * @since 1.0.0
     * @param string A unicode string
     * @param greedy Don't stop on finding one confusable character - find all of them.
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string, boolean greedy, String... preferredAliases);

    /**
     * Same as isConfusable with no preferred aliases
     * @since 1.0.0
     * @see #isConfusable(String, boolean, Collection)
     * @param string A unicode string
     * @param greedy Don't stop on finding one confusable character - find all of them.
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string, boolean greedy);

    /**
     * Same as isConfusable, no greedy
     * @since 1.0.0
     * @see #isConfusable(String, boolean, Collection)
     * @param string A unicode string
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string, Collection<String> preferredAliases);

    /**
     * Same as isConfusable(String, Collection) but with allowed aliases as string array.
     * @see #isConfusable(String, Collection)
     * @param string A unicode string
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string, String... preferredAliases);

    /**
     * Same as isConfusable, no greedy without preferred aliases
     * @since 1.0.0
     * @see #isConfusable(String, boolean, Collection)
     * @param string A unicode string
     * @return  all confusable characters and with what they are confusable otherwise.
     */
    List<Output> isConfusable(String string);

    /**
     * Checks if <code>string</code> can be dangerous, i.e. is it not only mixed-scripts
     *     but also contains characters from other scripts than the ones in <code>preferredAliases</code>
     *     that might be confusable with characters from scripts in <code>preferredAliases</code>.<br/>
     *     For <code>preferredAliases</code> examples, see <code>isConfusable</code>.
     * <pre>
     *     >>> bool(confusables.isDangerous("Allo"))
     *     False
     *     >>> bool(confusables.isDangerous("AlloΓ", Collections.singleton("latin")))
     *     False
     *     >>> bool(confusables.isDangerous("Alloρ"))
     *     True
     *     >>> bool(confusables.isDangerous("AlaskaJazz"))
     *     False
     *     >>> bool(confusables.isDangerous("ΑlaskaJazz"))
     *     True
     * </pre>
     * @since 1.0.0
     * @param string A unicode string
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return Is it dangerous.
     */
    boolean isDangerous(String string, Collection<String> preferredAliases);

    /**
     * Same as isDangerous(String, Collection) but with allowed aliases as string array.
     * @see #isDangerous(String, Collection)
     * @since 1.0.0
     * @param string A unicode string
     * @param preferredAliases Script blocks aliases which we don't want <code>string</code>'s characters to be confused with.
     * @return Is it dangerous.
     */
    boolean isDangerous(String string, String... preferredAliases);

    /**
     * Same as isDangerous with no preferred aliases
     * @since 1.0.0
     * @see #isDangerous(String, Collection)
     * @param string A unicode string
     * @return Is it dangerous.
     */
    boolean isDangerous(String string);
}
