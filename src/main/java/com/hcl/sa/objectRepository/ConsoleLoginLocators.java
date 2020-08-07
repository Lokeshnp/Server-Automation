package com.hcl.sa.objectRepository;

public interface ConsoleLoginLocators {
    String server_ip_edit_xpath = "//*[@ClassName='Edit'][@Name='Server:']";
    String username_xpath = "//*[@ClassName='Edit'][@Name='User name:']";
    String password_xpath = "//*[@ClassName='Edit'][@Name='Password:']";
    String ok_btn_xpath = "//*[@ClassName='Button'][@Name='Login']";
}
