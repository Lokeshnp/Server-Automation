package com.hcl.sa.constants;

public enum TimeOutConsts {

    //TODO WE NEED TO ADD SECONDS INSTEAD OF MILLI SECONDS
    WAIT_10_SECONDS("10000");

    public String text;

    TimeOutConsts(String text){
        this.text = text;
    }

    TimeOutConsts() {
    }
}
