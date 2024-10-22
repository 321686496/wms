package com.hongan.template.security.common;

public class StepAuthException extends Exception {
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;
    private final int step;

    public StepAuthException(int step) {
        super("Step:" + step);
        this.step = step;
    }

    public StepAuthException(int step, Object data) {
        super("Step:" + step);
        this.step = step;
        this.data = data;
    }

    public String toJSON() {
        return String.format("{\"status\": 0, \"step\": %d, \"data\": \"%s\" }", step, data);
    }

}
