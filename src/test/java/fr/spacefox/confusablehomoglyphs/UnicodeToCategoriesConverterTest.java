package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class UnicodeToCategoriesConverterTest {

    private UnicodeToCategoriesConverter converter;
    private Pattern pattern;

    @BeforeEach
    private void init() {
        converter = new UnicodeToCategoriesConverter();
        pattern = Pattern.compile(converter.getRegex());
    }

    @Test void onMatchOneEntryPoint() {
        final Matcher matcher = pattern.matcher("0020          ; Common # Zs       SPACE");
        if (matcher.find()) {
            converter.onLineMatched(matcher);
        }
        List<CategoryEntry> foundList = converter.getData();
        assertEquals(1, foundList.size());
        CategoryEntry found = foundList.get(0);
        assertAll("One entry point",
                () -> assertEquals(0x20, found.min),
                () -> assertEquals(0x20, found.max),
                () -> assertEquals("COMMON", found.aliasAndCategory.alias),
                () -> assertEquals("Zs", found.aliasAndCategory.category)
        );
    }

    @Test void onMatchMultipleEntryPoint() {
        final Matcher matcher = pattern.matcher("20000..2A6D6  ; Han # Lo [42711] CJK UNIFIED IDEOGRAPH-20000..CJK UNIFIED IDEOGRAPH-2A6D6");
        if (matcher.find()) {
            converter.onLineMatched(matcher);
        }
        List<CategoryEntry> foundList = converter.getData();
        assertEquals(1, foundList.size());
        CategoryEntry found = foundList.get(0);
        assertAll("Many entry points",
                () -> assertEquals(0x20000, found.min),
                () -> assertEquals(0x2A6D6, found.max),
                () -> assertEquals("HAN", found.aliasAndCategory.alias),
                () -> assertEquals("Lo", found.aliasAndCategory.category)
        );
    }
}
