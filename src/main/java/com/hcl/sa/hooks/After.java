package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.SuperClass;
import com.thoughtworks.gauge.AfterSpec;

import java.io.IOException;

public class After {
    @AfterSpec
    public void configAfterSpec() throws IOException, InterruptedException {
        SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text).quit();
        SuperClass.getInstance().killWinAppDriver();
    }
}
