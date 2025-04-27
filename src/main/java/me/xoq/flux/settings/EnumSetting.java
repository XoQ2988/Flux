package me.xoq.flux.settings;

public class EnumSetting<E extends Enum<E>> extends BaseSetting<E> {
    private final Class<E> enumClass;

    private EnumSetting(Builder<E> b) {
        super(b.name, b.description, b.defaultValue);
        this.enumClass = b.enumClass;
    }

    public Class<E> getEnumType() {
        return enumClass;
    }

    public static class Builder<E extends Enum<E>> {
        private String name;
        private String description = "";
        private E defaultValue;
        private Class<E> enumClass;

        public Builder<E> name(String name) {
            this.name = name; return this;
        }

        public Builder<E> description(String desc) {
            this.description = desc; return this;
        }

        public Builder<E> defaultValue(E def) {
            this.defaultValue = def;
            this.enumClass = def.getDeclaringClass();
            return this;
        }

        public EnumSetting<E> build() {
            if (name == null || defaultValue == null)
                throw new IllegalStateException("name & default required");
            return new EnumSetting<>(this);
        }
    }
}

