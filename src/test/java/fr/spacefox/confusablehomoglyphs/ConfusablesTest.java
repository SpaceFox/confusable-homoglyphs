package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class ConfusablesTest {

    @Nested
    @DisplayName("Public API test suite")
    @TestInstance(Lifecycle.PER_CLASS)
    class ConfusablesApiTest {

        private static final String LATIN_A = "A";
        private static final String GREEK_A = "Α";
        private static final String IS_GOOD = "Allo";
        // A valid character not in Unicode data (private character or character from a too recent Unicode version).
        private static final String UNKNOWN_CHAR = "\uE000";
        private final String LOOKS_GOOD = IS_GOOD.replaceAll(LATIN_A, GREEK_A);

        private Confusables confusables;

        @BeforeAll void init() {
            confusables = Confusables.fromInternal();
            boolean initialized = confusables.loadJson();
            assumeTrue(initialized);
        }

        @Test void isMixedScript() {
            assertTrue(confusables.isMixedScript(LOOKS_GOOD));
            assertTrue(confusables.isMixedScript(" ρττ a"));
            assertTrue(confusables.isMixedScript(" ρττ a", "COMMON"));

            assertFalse(confusables.isMixedScript(IS_GOOD));
            assertFalse(confusables.isMixedScript("ρτ.τ"));
            assertFalse(confusables.isMixedScript("ρτ.τ "));
            assertFalse(confusables.isMixedScript("ρτ.τ ", "COMMON"));

            assertFalse(confusables.isMixedScript(UNKNOWN_CHAR));
        }

        @Test void isConfusable() {
            List<Output> greek = confusables.isConfusable(LOOKS_GOOD, Collections.singleton("latin"));
            assertEquals(GREEK_A, greek.get(0).character);
            assertIterableEquals(
                    Collections.singletonList(new Homoglyphs("A", "LATIN CAPITAL LETTER A")),
                    greek.get(0).homoglyphs);
            List<Output> latin = confusables.isConfusable(IS_GOOD, Collections.singleton("latin"));
            assertIterableEquals(Collections.emptyList(), latin);

            assertIterableEquals(
                    Collections.emptyList(),
                    confusables.isConfusable("AlloΓ", Collections.singleton("latin"))
            );

            // Stop at first confusable character
            assertEquals(1, confusables.isConfusable("Αlloρ").size());
            assertEquals(1, confusables.isConfusable("Αlloρ", false).size());

            // Find all confusable characters
            // Α (greek), l, o, and ρ can be confused with other unicode characters
            assertEquals(4, confusables.isConfusable("Αlloρ", true).size());
            // Only Α (greek) and ρ (greek) can be confused with a latin character
            assertEquals(
                    2,
                    confusables.isConfusable("Αlloρ", true, Collections.singleton("latin")).size());
            assertEquals(
                    2,
                    confusables.isConfusable("Αlloρ", true, "latin").size());

            // For "Latin" readers, ρ is confusable!      ↓
            assertEquals("ρ", confusables.isConfusable("paρa", Collections.singleton("latin")).get(0).character);
            assertEquals("ρ", confusables.isConfusable("paρa", "latin").get(0).character);
            // For "Greek" readers, p is confusable!    ↓
            assertEquals("p", confusables.isConfusable("paρa", Collections.singleton("greek")).get(0).character);
            assertEquals("p", confusables.isConfusable("paρa", "greek").get(0).character);
        }

        @Test void isDangerous() {
            assertTrue(confusables.isDangerous(LOOKS_GOOD));
            assertTrue(confusables.isDangerous(" ρττ a"));
            assertTrue(confusables.isDangerous("ρττ a"));
            assertTrue(confusables.isDangerous("Alloτ"));
            assertTrue(confusables.isDangerous("www.micros﻿оft.com"));
            assertTrue(confusables.isDangerous("www.Αpple.com"));
            assertTrue(confusables.isDangerous("www.faϲebook.com"));

            assertFalse(confusables.isDangerous("AlloΓ", Collections.singleton("latin")));
            assertFalse(confusables.isDangerous("AlloΓ", "latin"));
            assertFalse(confusables.isDangerous(IS_GOOD));
            assertFalse(confusables.isDangerous(" ρτ.τ"));
            assertFalse(confusables.isDangerous("ρτ.τ"));

            // Unknown characters are not dangerous
            assertFalse(confusables.isDangerous(UNKNOWN_CHAR));
        }

        @Test void charNotFound() {
            assertFalse(confusables.isDangerous("Їt", Collections.singleton("latin")));

            assertTrue(confusables.isDangerous("Їtτ", Collections.singleton("latin")));
        }
    }
}
