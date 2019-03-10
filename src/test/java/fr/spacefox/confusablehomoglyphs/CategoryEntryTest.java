package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class CategoryEntryTest {

    private final CategoryEntry ref = new CategoryEntry(1, 5, "alias", "category");


    @Test void compareSameOrNull() {
        assertAll("Cases where two CategoryEntries can't be ordered",
                () -> assertEquals(0, ref.compareTo(null)),
                () -> assertEquals(0, ref.compareTo(ref)),
                () -> assertEquals(0, ref.compareTo(new CategoryEntry(1, 5, "alias", "category")))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 3, 5, 6 })
    void compareOnMin(int max) {
        assertAll("CategoryEntries are sorted by min value first",
                () -> assertTrue(ref.compareTo(new CategoryEntry(0, max, "alias", "category")) > 0),
                () -> assertTrue(ref.compareTo(new CategoryEntry(2, max, "alias", "category")) < 0)
        );
    }

    @Test void compareOnMax() {
        assertAll("CategoryEntries are sorted by max only if min are equals",
                () -> assertTrue(ref.compareTo(new CategoryEntry(1, 4, "alias", "category")) > 0),
                () -> assertEquals(0, ref.compareTo(new CategoryEntry(1, 5, "alias", "category"))),
                () -> assertTrue(ref.compareTo(new CategoryEntry(1, 6, "alias", "category")) < 0)
        );
    }
}
