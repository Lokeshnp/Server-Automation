package com.hcl.sa.constants;

public enum TimeOutConsts {


    WAIT_10_SECONDS(10), WAIT_1_SEC(1), WAIT_20_SECOND(20), WAIT_3_SEC(3), WAIT_60_SECOND(60);
    public int seconds;

    TimeOutConsts(int seconds) {
        this.seconds = seconds;
    }

}
