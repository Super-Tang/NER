package model;

import java.util.Arrays;

/**
 * Feature class, the features of each instance are store in an array
 * And the features are represents by a series of numbers
 * @author Super-Tang
 */
public class Feature {
    private int[] values;

    Feature(int[] xs) {
        values = new int[xs.length];
        System.arraycopy(xs, 0, values, 0, values.length);
    }

    public int[] getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Feature && this == o && Arrays.equals(values, ((Feature) o).values);
    }

    @Override
    public int hashCode() {
        return values != null ? Arrays.hashCode(values) : 0;
    }

    /**
     *   Set the label of the instance before the current ont,it is used to update the features of the instance
     *   when needed.
     * @param feature the label of the word
     * @param num the location of the label
     */
    public void setLabel(int feature, int num){
        values[values.length - num] = feature;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}

