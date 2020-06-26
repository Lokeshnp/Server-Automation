package com.hcl.sa.utils;

import com.hcl.sa.constants.ConsoleConsts;

public class AutomationPlans {

    public FixletDetails getPeInstallFixletDetails(String fixletID) {
        return new FixletDetails.Builder().setSiteName(ConsoleConsts.SITE_NAME.text)
               .setSiteType(ConsoleConsts.SITE_TYPE.text)
               .setFixletID(fixletID)
               .build();

   }
}
