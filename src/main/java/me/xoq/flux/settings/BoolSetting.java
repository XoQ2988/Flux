package me.xoq.flux.settings;

public class BoolSetting extends BaseSetting<Boolean> {
    public BoolSetting(Builder builder) {
        super(builder.name, builder.description, builder.defaultValue);
    }

    public static class Builder {
        private String name;
        private String description = "";
        private boolean defaultValue = false;

        public Builder name(String name) {
            this.name = name; return this;
        }
        public Builder description(String desc) {
            this.description = desc; return this;
        }
        public Builder defaultValue(boolean def) {
            this.defaultValue = def; return this;
        }

        public BoolSetting build() {
            if (name == null) {
                throw new IllegalStateException("name is required");
            }
            return new BoolSetting(this);
        }
    }
}
