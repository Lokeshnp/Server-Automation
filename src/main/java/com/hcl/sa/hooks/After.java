package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.SuperClass;
import com.thoughtworks.gauge.AfterSpec;

public class After {
    @AfterSpec
    public void cofigAfterSpec(){
        SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text).quit();
    }
}
