package model;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * Instance class. It represents a possible entity with a label
 *  and a series of features.
 * @author Super-Tang
 */
public class Instance {
    private int label;
    private Feature feature;

    public Instance(int label, int[] xs) {
        this.label = label;
        this.feature = new Feature(xs);
    }

    public int getLabel() {
        return label;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "label=" + label +
                ", feature=" + feature +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(label,feature);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Instance  && (o == this || label == ((Instance) o).label);
    }
}