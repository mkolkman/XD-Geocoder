package core.features;

abstract public class Feature {

    protected float value;

    public float getFloatValue() {
        return value;
    }

    public boolean getBooleanValue() {
        return value > 0.5;
    }
}
