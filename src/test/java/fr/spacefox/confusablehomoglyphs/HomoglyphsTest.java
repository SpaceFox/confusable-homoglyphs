package fr.spacefox.confusablehomoglyphs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
class HomoglyphsTest {

    @Test void hashAndEqualsOk() {
        Homoglyphs a = new Homoglyphs("sequence A", "name A");
        Homoglyphs b = new Homoglyphs("sequence A", "name A");
        assertAll( "Should not be the same object but should be equals",
                () -> assertNotSame(a, b),
                () -> assertEquals(a, b),
                () -> assertEquals(a.hashCode(), b.hashCode()));
    }

    @ParameterizedTest
    @MethodSource("sequenceAndCategoriesProvider")
    void hashAndEqualsDifferent(Homoglyphs a, Homoglyphs b) {
        assertAll( "Should be different and have different hashcode if sequence or name is different",
                () -> assertNotSame(a, b),
                () -> assertNotEquals(a, b),
                () -> assertNotEquals(a.hashCode(), b.hashCode()));
    }

    static Stream<Arguments> sequenceAndCategoriesProvider() {
        return Stream.of(
                arguments(
                        new Homoglyphs("sequence A", "name A"),
                        new Homoglyphs("sequence A", "name B")),
                arguments(
                        new Homoglyphs("sequence A", "name A"),
                        new Homoglyphs("sequence B", "name A")),
                arguments(
                        new Homoglyphs("sequence B", "name A"),
                        new Homoglyphs("sequence A", "name B"))
        );
    }
}
