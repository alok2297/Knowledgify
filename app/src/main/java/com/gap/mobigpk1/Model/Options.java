package com.gap.mobigpk1.Model;

import java.io.Serializable;

public class Options implements Serializable {

    String options;

    public Options(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }



}
