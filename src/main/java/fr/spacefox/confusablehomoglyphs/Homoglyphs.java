package fr.spacefox.confusablehomoglyphs;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
public class Homoglyphs {
    @SerializedName("s") public final String sequence;
    @SerializedName("n") public final String name;

    Homoglyphs(String sequence, String name) {
        this.sequence = sequence;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homoglyphs that = (Homoglyphs) o;
        return Objects.equals(sequence, that.sequence) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence, name);
    }
}
