package com.hcl.sa.utils;

public enum Credentials {
    CONSOLE{
        @Override
        public String getPassword() {
            return System.getenv("console_password");
        }

        @Override
        public String getUsername() {
            return  System.getenv("console_username");
        }

        @Override
        public String getServerIP() {
            return  System.getenv("bigfix_server_ip");
        }
    },VSPHERE{
        @Override
        public String getPassword() {
            return System.getenv("vmPassword");
        }

        @Override
        public String getUsername() {
            return System.getenv("vmUserName");
        }

        @Override
        public String getServerIP() {
            return System.getenv("vsphere_server_IP");
        }
    };
    public abstract  String getPassword();
    public abstract  String getUsername();
    public abstract String getServerIP();
}
