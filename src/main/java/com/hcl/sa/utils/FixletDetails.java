package com.hcl.sa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixletDetails {
    private final String SiteName;
    private final String SiteType;
    private final String FixletID;

    public static class Builder {
        private String SiteName;
        private String SiteType;
        private String FixletID;

        private final Logger logger = LogManager.getLogger(FixletDetails.class);

        public Builder setSiteName(String siteName) {
            SiteName = siteName;
            logger.debug("Site Name =" +siteName);
            return this;
        }

        public Builder setSiteType(String siteType) {
            SiteType = siteType;
            logger.debug("Site Type =" +siteType);
            return this;
        }

        public Builder setFixletID(String fixletID) {
            FixletID = fixletID;
            logger.debug("Fixlet ID =" +fixletID);
            return this;
        }

        public FixletDetails build() { return new FixletDetails(this) ;}
    }

    private FixletDetails(Builder builder) {
        this.SiteName = builder.SiteName;
        this.SiteType = builder.SiteType;
        this.FixletID = builder.FixletID;
    }

    public String getSiteName () {
        return SiteName;
    }

    public String getSiteType () {
        return SiteType;
    }

    public String getFixletID () {
        return FixletID;
    }

}
