package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class CategoriesTest {

    @Nested
    @DisplayName("Public API test suite")
    @TestInstance(Lifecycle.PER_CLASS)
    class CategoriesApiTests {

        private static final String LATIN_A = "A";
        private static final String GREEK_A = "Α";
        private static final char LATIN_A_CHAR = 'A';
        private static final char GREEK_A_CHAR = 'Α';
        private static final String IS_GOOD = "Allo";
        private final String LOOKS_GOOD = IS_GOOD.replaceAll(LATIN_A, GREEK_A);

        private Categories categories;

        @BeforeAll void init() {
            categories = Categories.fromInternal();
            boolean initialized = categories.loadJson();
            assumeTrue(initialized);
        }

        @Test void aliases() {
            assertEquals("LATIN", categories.alias(LATIN_A));
            assertEquals("GREEK", categories.alias(GREEK_A));
            assertEquals("LATIN", categories.alias(LATIN_A_CHAR));
            assertEquals("GREEK", categories.alias(GREEK_A_CHAR));
        }

        @Test void categories() {
            assertEquals("L", categories.category(LATIN_A));
            assertEquals("L", categories.category(GREEK_A));
            assertEquals("L", categories.category(LATIN_A_CHAR));
            assertEquals("L", categories.category(GREEK_A_CHAR));
        }

        @Test void aliasAndCategory() {
            assertEquals(new AliasAndCategory("LATIN", "L"), categories.aliasAndCategory(LATIN_A));
            assertEquals(new AliasAndCategory("GREEK", "L"), categories.aliasAndCategory(GREEK_A));
            assertEquals(new AliasAndCategory("LATIN", "L"), categories.aliasAndCategory(LATIN_A_CHAR));
            assertEquals(new AliasAndCategory("GREEK", "L"), categories.aliasAndCategory(GREEK_A_CHAR));
        }

        @Test void uniqueAliases() {
            assertEquals(
                    new HashSet<>(Collections.singletonList("LATIN")),
                    categories.uniqueAliases(IS_GOOD));
            assertEquals(
                    new HashSet<>(Arrays.asList("LATIN", "GREEK")),
                    categories.uniqueAliases(LOOKS_GOOD));
        }

        @Test void higherCodePoint() {
            assertEquals(new AliasAndCategory("HAN", "Lo"), categories.aliasAndCategory("狐"));
            assertEquals(new AliasAndCategory("HAN", "Lo"), categories.aliasAndCategory('狐'));
        }

        // Non-characters (here Byte Order Mark) should not have alias nor aliasAndCategory
        @Test void nonCharacters() {
            assertNull(categories.alias("\uFFFE"));
            assertNull(categories.category("\uFFFE"));
            assertNull(categories.aliasAndCategory("\uFFFE"));
            assertNull(categories.alias('\uFFFE'));
            assertNull(categories.category('\uFFFE'));
            assertNull(categories.aliasAndCategory('\uFFFE'));
        }

        // Valid but private characters should not have alias nor aliasAndCategory
        @Test void validUnknownCharacter() {
            assertNull(categories.aliasAndCategory("\uE000"));
        }

        // Java Strings don't directly handle supplementary planes: characters are represented by a surrogate pair.
        // This application should handle them, let's test with 🦊
        @Test void supplementaryPlanes() {
            assertEquals(new AliasAndCategory("COMMON", "So"), categories.aliasAndCategory("\uD83E\uDD8A"));
        }

        // Check if default data are up-to-date with a Unicode 11 character (DNA double helix 🧬)
        @Test void unicode11() {
            assertEquals(new AliasAndCategory("COMMON", "So"), categories.aliasAndCategory("\uD83E\uDDEC"));
        }

        // Check if default data are up-to-date with a Unicode 12 character (Yawning Face 🥱🧬)
        @Test void unicode12() {
            assertEquals(new AliasAndCategory("COMMON", "So"), categories.aliasAndCategory("\uD83E\uDD71"));
        }

        // Check if supplementary plane character are properly detected when included in a string (a🦊b狐🧬).
        @Test void stringWithSupplementaryPlaneChars() {
            assertEquals(
                    new HashSet<>(Arrays.asList("COMMON", "LATIN", "HAN")),
                    categories.uniqueAliases("a\uD83E\uDD8Ab狐\uD83E\uDDEC"));
        }
    }

    @Nested
    @DisplayName("Inner API")
    class RangeTests {

        @InjectMocks
        private Categories categories;

        @Test void initializationCheck() {
            assertThrows(IllegalStateException.class, () -> categories.alias("A"));
            assertThrows(IllegalStateException.class, () -> categories.category("A"));
            assertThrows(IllegalStateException.class, () -> categories.aliasAndCategory("A"));
            assertThrows(IllegalStateException.class, () -> categories.uniqueAliases("A"));
            assertThrows(IllegalStateException.class, () -> categories.alias('A'));
            assertThrows(IllegalStateException.class, () -> categories.category('A'));
            assertThrows(IllegalStateException.class, () -> categories.aliasAndCategory('A'));
        }

        @Test void getSingle() {
            categories.setData(Collections.singletonList(new CategoryEntry(1, 1, "A", "Z")));

            assertNull(categories.get(0));
            assertEquals("A", categories.get(1).alias);
            assertNull(categories.get(2));
        }

        @Test void getInRange() {
            categories.setData(Collections.singletonList(new CategoryEntry(0, 2, "A", "Z")));

            assertNull(categories.get(-1));
            assertEquals("A", categories.get(0).alias);
            assertEquals("A", categories.get(1).alias);
            assertEquals("A", categories.get(2).alias);
            assertNull(categories.get(3));
        }

        @Test void getMultiple() {
            categories.setData(Arrays.asList(
                    new CategoryEntry(1, 2, "A", "Z"),
                    new CategoryEntry(4, 6, "B", "Y")));

            assertNull(categories.get(0));
            assertEquals("A", categories.get(1).alias);
            assertEquals("A", categories.get(2).alias);
            assertNull(categories.get(3));
            assertEquals("B", categories.get(4).alias);
            assertEquals("B", categories.get(5).alias);
            assertEquals("B", categories.get(6).alias);
            assertNull(categories.get(7));
        }

        @Test void hole() {
            categories.setData(Arrays.asList(
                    new CategoryEntry(1, 100, "A", "Z"),
                    new CategoryEntry(1_000_000, 1_000_100, "B", "Y")));
            assertNull(categories.get(0));
            assertEquals("A", categories.get(50).alias);
            assertNull(categories.get(500_000));
            assertEquals("B", categories.get(1_000_050).alias);
            assertNull(categories.get(10_000_000));
        }

        @Test void largeMap() {
            List<CategoryEntry> entries = new ArrayList<>(2_000_000);
            for (int i = 0; i < 2_000_000; i += 2) {
                entries.add( new CategoryEntry(i, i + 1, Integer.toString(i), Integer.toString(i + 1)));
            }
            assertEquals(1_000_000, entries.size());
            categories.setData(entries);
            String val;
            for (int i = 0; i < 2_000_000; i += 2) {
                val = categories.get(i).alias;
                assertEquals(Integer.toString(i), val);
            }
        }
    }
    //endregion
}
