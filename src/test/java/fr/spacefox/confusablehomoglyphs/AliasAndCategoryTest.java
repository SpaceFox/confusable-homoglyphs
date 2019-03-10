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
class AliasAndCategoryTest {

    @Test void hashAndEqualsOk() {
        AliasAndCategory a = new AliasAndCategory("alias A", "category A");
        AliasAndCategory b = new AliasAndCategory("alias A", "category A");
        assertAll( "Should not be the same object but should be equals",
                () -> assertNotSame(a, b),
                () -> assertEquals(a, b),
                () -> assertEquals(a.hashCode(), b.hashCode()));
    }

    @ParameterizedTest
    @MethodSource("aliasAndCategoriesProvider")
    void hashAndEqualsDifferent(AliasAndCategory a, AliasAndCategory b) {
        assertAll( "Should be different and have different hashcode if alias or category is different",
                () -> assertNotSame(a, b),
                () -> assertNotEquals(a, b),
                () -> assertNotEquals(a.hashCode(), b.hashCode()));
    }

    static Stream<Arguments> aliasAndCategoriesProvider() {
        return Stream.of(
                arguments(
                        new AliasAndCategory("alias A", "category A"),
                        new AliasAndCategory("alias A", "category B")),
                arguments(
                        new AliasAndCategory("alias A", "category A"),
                        new AliasAndCategory("alias B", "category A")),
                arguments(
                        new AliasAndCategory("alias B", "category A"),
                        new AliasAndCategory("alias A", "category B"))
        );
    }
}
