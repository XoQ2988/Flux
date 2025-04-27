package me.xoq.flux.settings;

public class StringSetting extends BaseSetting<String> {
    private StringSetting(Builder b) {
        super(b.name, b.description, b.defaultValue);
    }

    public static class Builder {
        private String name;
        private String description = "";
        private String defaultValue = "";

        public Builder name(String name) {
            this.name = name; return this;
        }

        public Builder description(String desc) {
            this.description = desc; return this;
        }

        public Builder defaultValue(String def) {
            this.defaultValue = def; return this;
        }

        public StringSetting build() {
            if (name == null) throw new IllegalStateException("name required");
            return new StringSetting(this);
        }
    }
}
