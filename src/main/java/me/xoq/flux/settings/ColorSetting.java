package me.xoq.flux.settings;

public class ColorSetting extends BaseSetting<Integer> {
    private ColorSetting(Builder b) {
        super(b.name, b.description, b.defaultValue);
    }

    public static class Builder {
        private String name;
        private String description = "";
        private int defaultValue = 0xFFFFFFFF; // white

        public Builder name(String name) {
            this.name = name; return this;
        }

        public Builder description(String desc) {
            this.description = desc; return this;
        }

        /** Accepts 0xAARRGGBB or hex string "#AARRGGBB" */
        public Builder defaultValue(int argb) {
            this.defaultValue = argb; return this;
        }

        public ColorSetting build() {
            if (name == null) throw new IllegalStateException("name required");
            return new ColorSetting(this);
        }
    }
}

