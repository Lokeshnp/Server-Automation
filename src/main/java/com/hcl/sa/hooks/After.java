package com.hcl.sa.hooks;

import com.hcl.sa.utils.bigfix.Login;
import com.hcl.sa.utils.bigfix.SuperClass;
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
