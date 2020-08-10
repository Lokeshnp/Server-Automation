package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.Login;
import com.hcl.sa.utils.SuperClass;
import com.thoughtworks.gauge.AfterSpec;
import com.thoughtworks.gauge.AfterSuite;

import java.io.IOException;

public class After {
    @AfterSuite
    public void configAfterSuite() throws IOException, InterruptedException {
        Login login  = new Login();
        login.closeBigFixConsole();
        SuperClass.getInstance().killWinAppDriver();
    }
}
