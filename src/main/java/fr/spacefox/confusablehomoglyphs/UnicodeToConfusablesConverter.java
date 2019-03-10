package fr.spacefox.confusablehomoglyphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class UnicodeToConfusablesConverter extends UnicodeToJsonConverter<Map<String, List<Homoglyphs>>> {

    private Map<String, List<Homoglyphs>> confusablesMap = new HashMap<>();

    UnicodeToConfusablesConverter() {
        super(
                "[0-9A-F ]+\\s+;\\s*[0-9A-F ]+\\s+;\\s*\\w+\\s*#\\*?\\s*\\( (.+) → (.+) \\) (.+) → (.+)\\t#",
                "http://unicode.org/Public/security/latest/confusables.txt");
    }

    @Override
    Map<String, List<Homoglyphs>> getData() {
        return confusablesMap;
    }

    @Override
    void onLineMatched(Matcher matcher) {
        String sequence1 = matcher.group(1);
        String sequence2 = matcher.group(2);
        String name1 = matcher.group(3);
        String name2 = matcher.group(4);
        put(sequence1, sequence2, name2);
        put(sequence2, sequence1, name1);
    }

    private void put(String sequence, String homoglyph, String name) {
        if (!confusablesMap.containsKey(sequence)) {
            confusablesMap.put(sequence, new ArrayList<>());
        }
        confusablesMap.get(sequence).add(new Homoglyphs(homoglyph, name));
    }
}
