package com.pigdroid.simulation.editor;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerPropertyEditor<T> implements PropertyEditor<T> {

    private final String name;
    private final PropertyCategory category;
    private final String uiRequirements;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final T min;
    private final T max;

    public PlayerPropertyEditor(String name, PropertyCategory category, String uiRequirements,
                                Supplier<T> getter, Consumer<T> setter, T min, T max) {
        this.name = name;
        this.category = category;
        this.uiRequirements = uiRequirements;
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyCategory getCategory() {
        return category;
    }

    @Override
    public String getUiRequirements() {
        return uiRequirements;
    }

    @Override
    public T getValue() {
        return getter.get();
    }

    @Override
    public void setValue(T value) {
        setter.accept(value);
    }

    @Override
    public T getMin() {
        return min;
    }

    @Override
    public T getMax() {
        return max;
    }
}
