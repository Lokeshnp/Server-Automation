package com.hcl.sa.constants;

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
    };


    public abstract  String getPassword();
    public abstract  String getUsername();
}
