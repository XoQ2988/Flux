package me.xoq.flux.settings;

import me.xoq.flux.utils.misc.Utils;

public abstract class BaseSetting<T> implements Setting<T> {
    private final String name;
    private final String title;
    private String key;
    private final String description;
    private final T defaultValue;
    private T value;

    protected BaseSetting(String name, String description, T defaultValue) {
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.key = name;    // will be the same as "name" until wrapped
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override public String getName()        { return name; }
    @Override public String getTitle()       { return title; }
    @Override public String getKey()         { return key; }
    @Override public String getDescription() { return description; }
    @Override public T getDefault()          { return defaultValue; }
    @Override public T getValue()            { return value; }
    @Override public void setKey(String value) { this.key = value; }
    @Override public void setValue(T value)  { this.value = value; }

    public void reset() { setValue(defaultValue); }
}
