package fr.spacefox.confusablehomoglyphs;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * A holder to keep alias and categories for a character. Used as return values from Categories.
 * @see Categories
 * @author SpaceFox
 * @version 1.0.0
 */
public class AliasAndCategory {

    /**
     * The ISO 15924 alias name of the writing system of the given character.
     * See https://en.wikipedia.org/wiki/ISO_15924
     */
    @SerializedName("a") public final String alias;

    /**
     * The Unicode Category of the given character.
     * See https://en.wikipedia.org/wiki/Unicode_character_property#General_Category
     */
    @SerializedName("c") public final String category;

    AliasAndCategory(String alias, String category) {
        this.alias = alias;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasAndCategory aliasAndCategory1 = (AliasAndCategory) o;
        return Objects.equals(alias, aliasAndCategory1.alias) &&
                Objects.equals(category, aliasAndCategory1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, category);
    }

    @Override
    public String toString() {
        return "AliasAndCategory{" +
                "alias='" + alias + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
