package model;

import java.util.Objects;

/**
 *  Feature function class.the index implies the order of the feature,
 *  value represents the feature, label indicates the class the instance
 *  may belong to.
 *  @author super-Tang
 */
class FeatureFunction {
    private int index;
    private int value;
    private int label;

    FeatureFunction(int index, int value, int label) {
        this.index = index;
        this.value = value;
        this.label = label;
    }

    /**
     *  Check whether an instance contains some features of the given label
     * @param feature the features of the instance
     * @param label the label of this instance
     * @return 1 if the features meet the requirements
     */
    int apply(Feature feature, int label) {
        if (feature.getValues()[index] == value && label == this.label) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FeatureFunction && (o == this || (((FeatureFunction) o).value == value) && ((FeatureFunction) o).index == index && ((FeatureFunction) o).label == label);
    }

    @Override
    public String toString() {
        return "[ index : " + index + ", value: " + value + ", label: " + label +" ]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(index,value,label);
    }
}