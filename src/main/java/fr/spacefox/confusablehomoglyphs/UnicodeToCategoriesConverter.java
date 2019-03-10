package fr.spacefox.confusablehomoglyphs;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * Converts an Unicode Scripts datasource to a ready-to-use Categories JSON file.
 * @author SpaceFox
 * @version 1.0.0
 */
class UnicodeToCategoriesConverter extends UnicodeToJsonConverter<List<CategoryEntry>> {

    private List<CategoryEntry> entries = new ArrayList<>();

    UnicodeToCategoriesConverter() {
        super(
                "([0-9A-F]+)(?:\\.\\.([0-9A-F]+))?\\W+(\\w+)\\s*#\\s*(\\w+)",
                "http://unicode.org/Public/UNIDATA/Scripts.txt");
    }

    @Override
    List<CategoryEntry> getData() {
        return entries;
    }

    @Override
    void onLineMatched(Matcher matcher) {
        Integer codePointRangeFrom = safeParseHexInt(matcher.group(1));
        Integer codePointRangeTo = safeParseHexInt(matcher.group(2));
        String alias = matcher.group(3).toUpperCase(Locale.US);
        String category = matcher.group(4);
        if (codePointRangeTo == null) {
            this.put(codePointRangeFrom, alias, category);
        } else {
            this.put(codePointRangeFrom, codePointRangeTo, alias, category);
        }
    }

    private void put(Integer key, String alias, String category) {
        put(key, key, alias, category);
    }

    private void put(Integer min, Integer max, String alias, String category) {
        entries.add(new CategoryEntry(min, max, alias, category));
    }

    @Contract("null -> null")
    private static Integer safeParseHexInt(String s) {
        if (s == null || "".equals(s.trim())) {
            return null;
        }
        return Integer.parseUnsignedInt(s, 16);
    }
}
