package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.Login;
import com.hcl.sa.utils.SuperClass;
import com.thoughtworks.gauge.AfterSpec;

import java.io.IOException;

public class After {
    @AfterSpec
    public void configAfterSpec() throws IOException, InterruptedException {
        Login login  = new Login();
        login.closeBigFixConsole();
        SuperClass.getInstance().killWinAppDriver();
    }
}
