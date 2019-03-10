package fr.spacefox.confusablehomoglyphs;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
public final class Utils {

    private Utils() { /* Don't create instances of utility class */ }

    /**
     *
     * @param string
     * @param consumer
     */
    public static void forEachCodePoint(String string, IntConsumer consumer) {
        for (int offset = 0; offset < string.length(); ) {
            final int codePoint = string.codePointAt(offset);
            consumer.accept(codePoint);
            offset += Character.charCount(codePoint);
        }
    }

    /**
     *
     * @param string
     * @param consumer
     */
    public static void forEachUnicodeChar(String string, Consumer<String> consumer) {
        forEachCodePoint(string, (codePoint) -> {
            // This stuff to handle characters in supplementary planes (represented by a surrogate pair).
            consumer.accept(String.copyValueOf(Character.toChars(codePoint)));
        });
    }
}
