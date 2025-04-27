package me.xoq.flux.settings;

public class IntSetting extends BaseSetting<Integer> {
    private final int min, max;

    private IntSetting(Builder b) {
        super(b.name, b.description, b.defaultValue);
        this.min = b.min;
        this.max = b.max;
    }

    @Override
    public void setValue(Integer v) {
        super.setValue(Math.min(max, Math.max(min, v)));
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static class Builder {
        private String name;
        private String description = "";
        private int defaultValue;
        private int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;

        public Builder name(String name) {
            this.name = name; return this;
        }

        public Builder description(String desc) {
            this.description = desc; return this;
        }

        public Builder defaultValue(int def) {
            this.defaultValue = def; return this;
        }

        public Builder range(int min, int max) {
            this.min = min; this.max = max; return this;
        }

        public IntSetting build() {
            if (name == null) throw new IllegalStateException("name required");
            return new IntSetting(this);
        }
    }
}

