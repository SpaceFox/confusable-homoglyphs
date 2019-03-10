package fr.spacefox.confusablehomoglyphs;

import com.google.gson.annotations.SerializedName;

/**
 * A kind of "range map" entry: if the character code point is between min and max (included), this character is
 * associated with the AliasAndCategory data.
 * @author SpaceFox
 * @version 1.0.0
 */
class CategoryEntry implements Comparable<CategoryEntry> {

    @SerializedName("l") final int min;
    @SerializedName("h") final int max;
    @SerializedName("c") final AliasAndCategory aliasAndCategory;

    CategoryEntry(int min, int max, String iso15924alias, String category) {
        this.min = min;
        this.max = max;
        this.aliasAndCategory = new AliasAndCategory(iso15924alias, category);
    }

    @Override
    public int compareTo(CategoryEntry entry) {
        if (entry == null || this == entry) {
            return 0;
        }
        int compMin = this.min - entry.min;
        return compMin == 0 ? (this.max - entry.max) : compMin;
    }
}
