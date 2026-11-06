package com.nwc.simulation.editor;

public interface PropertyEditor<T> {
    String getName();
    PropertyCategory getCategory();
    String getUiRequirements();
    T getValue();
    void setValue(T value);
    T getMin();
    T getMax();
}
