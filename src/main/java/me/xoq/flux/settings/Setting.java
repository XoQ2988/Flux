package me.xoq.flux.settings;

public interface Setting <T>{
    String getName();
    String getTitle();
    String getKey();
    String getDescription();

    // getter
    T getValue();
    T getDefault();

    // setter
    void setValue(T value);

    default void reset() {
        setValue(getDefault());
    }

}