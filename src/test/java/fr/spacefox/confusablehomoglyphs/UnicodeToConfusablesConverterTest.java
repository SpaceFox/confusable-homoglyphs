package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class UnicodeToConfusablesConverterTest {

    private UnicodeToConfusablesConverter converter;
    private Pattern pattern;

    @BeforeEach
    private void init() {
        converter = new UnicodeToConfusablesConverter();
        pattern = Pattern.compile(converter.getRegex());
    }

    @Test void checkOneToOneMatch() {
        final Matcher matcher = pattern.matcher("0153 ;\t006F 0065 ;\tMA\t# ( œ → oe ) LATIN SMALL LIGATURE OE → LATIN SMALL LETTER O, LATIN SMALL LETTER E\t# ");
        if (matcher.find()) {
            converter.onLineMatched(matcher);
        }
        Map<String, List<Homoglyphs>> foundMap = converter.getData();

        assertEquals(2, foundMap.size());
        assertAll("Both mapping are properly inserted",
                () -> assertTrue(foundMap.containsKey("œ")),
                () -> assertEquals(1, foundMap.get("œ").size()),
                () -> assertEquals("oe", foundMap.get("œ").get(0).sequence),
                () -> assertEquals("LATIN SMALL LETTER O, LATIN SMALL LETTER E", foundMap.get("œ").get(0).name),
                () -> assertTrue(foundMap.containsKey("oe")),
                () -> assertEquals(1, foundMap.get("oe").size()),
                () -> assertEquals("œ", foundMap.get("oe").get(0).sequence),
                () -> assertEquals("LATIN SMALL LIGATURE OE", foundMap.get("oe").get(0).name)
        );
    }
}
