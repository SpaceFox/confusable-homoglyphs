package fr.spacefox.confusablehomoglyphs;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A collection of tools to check if a given string contains characters that can be confused with other one.
 * @author SpaceFox
 * @version 1.0.0
 */
public class Confusables extends UsesJsonData<Map<String, List<Homoglyphs>>> implements ConfusablesApi {

    private Categories categories;
    private Map<String, List<Homoglyphs>> confusablesMap = new HashMap<>();

    // Public API

    /**
     * Create an instance of Confusables and load associated JSON data. As the loading is relatively long, this object
     * should be kept alive.
     * @throws IllegalStateException if the JSON data can't be loaded
     * @return an initialized instance of Confusables
     */
    public static Confusables fromInternal() {
        final Confusables confusables = new Confusables();
        confusables.loadJson();
        confusables.assertInitialization();
        return confusables;
    }

    public static Confusables fromJsons(String categoriesJsonFileName, String confusablesJsonFileName) {
        final Confusables confusables = new Confusables();
        confusables.loadJson(categoriesJsonFileName, confusablesJsonFileName);
        confusables.assertInitialization();
        return confusables;
    }

    @Override
    public boolean isMixedScript(String string, Collection<String> allowedAliases) {
        Set<String> allowedAliasesSet = cleanAliases(allowedAliases);
        Set<String> uniqueAliases = categories.uniqueAliases(string);
        if (uniqueAliases == null || uniqueAliases.isEmpty()) {
            return false;
        }
        uniqueAliases.removeAll(allowedAliasesSet);
        return uniqueAliases.size() > 1;
    }

    @Override
    public boolean isMixedScript(String string, String... allowedAliases) {
        return isMixedScript(string, Arrays.asList(allowedAliases));
    }

    @Override
    public boolean isMixedScript(String string) {
        return isMixedScript(string, Collections.singleton("COMMON"));
    }

    @Override
    public List<Output> isConfusable(String string, boolean greedy, Collection<String> preferredAliases) {
        final List<Output> outputs = new ArrayList<>();
        final Set<String> checked = new HashSet<>();
        final Set<String> preferredAliasesSet = cleanAliases(preferredAliases);

        // Same loop as Utils.forEachUnicodeChar() but copied due to break and return in loop.
        for (int offset = 0; offset < string.length(); ) {
            final int codePoint = string.codePointAt(offset);
            final String character = String.copyValueOf(Character.toChars(codePoint));
            offset += Character.charCount(codePoint);

            if (checked.contains(character)) {
                continue;
            }
            checked.add(character);

            String characterAlias = categories.alias(character);
            if (preferredAliasesSet.contains(characterAlias)) {
                // It's safe if the character might be confusable with homoglyphs from other categories than our
                // preferred categories (=aliases)
                continue;
            }
            List<Homoglyphs> found = confusablesMap.get(character);
            if (found == null) {
                continue;
            }

            // Character λ is considered confusable if λ can be confused with a character from preferredAliases,
            // e.g. if 'LATIN', 'ρ' is confusable with 'p' from LATIN. If 'LATIN', 'Γ' is not confusable because in all
            // the characters confusable with Γ, none of them is LATIN.
            List<Homoglyphs> potentiallyConfusable;
            if (preferredAliasesSet.isEmpty()) {
                potentiallyConfusable = found;
            } else {
                potentiallyConfusable = new ArrayList<>();
                for (Homoglyphs homoglyphs : found) {
                    Set<String> aliases = new HashSet<>();
                    Utils.forEachUnicodeChar(homoglyphs.sequence, (glyph) -> aliases.add(categories.alias(glyph)));
                    for (String alias : aliases) {
                        if (preferredAliasesSet.contains(alias)) {
                            potentiallyConfusable = found;
                            break;
                        }
                    }
                }
            }
            if (!potentiallyConfusable.isEmpty()) {
                Output output = new Output(character, characterAlias, potentiallyConfusable);
                if (!greedy) {
                    return Collections.singletonList(output);
                }
                outputs.add(output);
            }
        }
        return outputs;
    }

    @Override
    public List<Output> isConfusable(String string, boolean greedy, String... preferredAliases) {
        return isConfusable(string, greedy, Arrays.asList(preferredAliases));
    }

    @Override
    public List<Output> isConfusable(String string, boolean greedy) {
        return isConfusable(string, greedy, Collections.emptyList());
    }

    @Override
    public List<Output> isConfusable(String string, Collection<String> preferredAliases) {
        return isConfusable(string, false, preferredAliases);
    }

    @Override
    public List<Output> isConfusable(String string, String... preferredAliases) {
        return isConfusable(string, Arrays.asList(preferredAliases));
    }

    @Override
    public List<Output> isConfusable(String string) {
        return isConfusable(string, false);
    }

    @Override
    public boolean isDangerous(String string, Collection<String> preferredAliases) {
        return isMixedScript(string) && !isConfusable(string, preferredAliases).isEmpty();
    }

    @Override
    public boolean isDangerous(String string, String... preferredAliases) {
        return isDangerous(string, Arrays.asList(preferredAliases));
    }

    @Override
    public boolean isDangerous(String string) {
        return isDangerous(string, Collections.emptyList());
    }

    //region Private confusablehomoglyphs code

    Confusables() {
        // Only internal API can directly create Categories objects.
        super(new TypeToken<HashMap<String, ArrayList<Homoglyphs>>>(){}.getType());
    }

    @Override
    boolean loadJson() {
        categories = Categories.fromInternal();
        return super.loadJson();
    }

    @Override
    String getInternalResourceName() {
        return "confusables.json";
    }

    boolean loadJson(String categoriesJsonFileName, String confusablesJsonFileName) {
        categories = Categories.fromJson(categoriesJsonFileName);
        return super.loadJson(confusablesJsonFileName);
    }

    @Override
    void setData(Map<String, List<Homoglyphs>> fromJson) {
        confusablesMap = fromJson;
    }

    private Set<String> cleanAliases(Collection<String> aliases) {
        return aliases == null
                ? Collections.emptySet()
                : aliases.stream()
                        .map(alias -> alias.toUpperCase(Locale.US))
                        .collect(Collectors.toSet());
    }
    //endregion
}
