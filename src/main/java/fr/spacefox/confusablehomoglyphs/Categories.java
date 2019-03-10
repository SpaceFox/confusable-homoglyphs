package fr.spacefox.confusablehomoglyphs;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Retrieves data about unicode writing system {@link AliasAndCategory#alias alias} and character
 * {@link AliasAndCategory#category category} of a character or a string.
 * @author SpaceFox
 * @version 1.0.0
 */
public class Categories extends UsesJsonData<List<CategoryEntry>> implements CategoriesApi {

    private List<CategoryEntry> entries = new ArrayList<>();
    private boolean sorted = false;

    // Public API

    /**
     * Create an instance of Categories and load associated JSON data. As the loading is quite long, this object should
     * be kept alive.
     * @throws IllegalStateException if the JSON data can't be loaded
     * @return an initialized instance of Categories
     */
    public static Categories fromInternal() {
        final Categories categories = new Categories();
        categories.loadJson();
        categories.assertInitialization();
        return categories;
    }

    public static Categories fromJson(String jsonFileName) {
        final Categories categories = new Categories();
        categories.loadJson(jsonFileName);
        categories.assertInitialization();
        return categories;
    }

    @Override
    public AliasAndCategory aliasAndCategory(String character) {
        assertInitialization();
        return character == null ? null : get(Character.codePointAt(character, 0));
    }

    @Override
    public AliasAndCategory aliasAndCategory(char character) {
        return aliasAndCategory(String.valueOf(character));
    }

    @Override
    public String alias(String character) {
        assertInitialization();
        AliasAndCategory aliasAndCategory = aliasAndCategory(character);
        return aliasAndCategory == null ? null : aliasAndCategory.alias;
    }

    @Override
    public String alias(char character) {
        return alias(String.valueOf(character));
    }

    @Override
    public String category(String character) {
        assertInitialization();
        AliasAndCategory aliasAndCategory = aliasAndCategory(character);
        return aliasAndCategory == null ? null : aliasAndCategory.category;
    }

    @Override
    public String category(char character) {
        return category(String.valueOf(character));
    }

    @Override
    public Set<String> uniqueAliases(String string) {
        assertInitialization();
        final Set<String> uniqueAliases = new HashSet<>();
        Utils.forEachCodePoint(string, (codePoint) -> {
            final AliasAndCategory aliasAndCategory = get(codePoint);
            if (aliasAndCategory != null) {
                uniqueAliases.add(aliasAndCategory.alias);
            }
        });
        return uniqueAliases;
    }

    //region Private confusablehomoglyphs code

    Categories() {
        // Only internal API can directly create Categories objects.
        super(new TypeToken<ArrayList<CategoryEntry>>(){}.getType());
    }


    @Override
    String getInternalResourceName() {
        return "categories.json";
    }

    @Override
    void setData(List<CategoryEntry> fromJson) {
        entries = fromJson;
    }

    AliasAndCategory get(Integer key) {
        if (!sorted) {
            Collections.sort(entries);
            sorted = true;
        }
        // Binary search
        int low = 0;
        int position;
        int high = entries.size() - 1;
        CategoryEntry entry;
        while (high >= low) {
            position = (high + low) / 2;
            entry = entries.get(position);
            if (key.compareTo(entry.min) < 0) {
                high = position - 1;
            } else if (key.compareTo(entry.max) > 0) {
                low = position + 1;
            } else {
                return entry.aliasAndCategory;
            }
        }
        return null;
    }
    //endregion
}
