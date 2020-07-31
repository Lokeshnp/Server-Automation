package com.hcl.sa.constants;

public enum WinAppConsts {
    WINAPP_DRIVER_INVOKE_CMD(System.getenv("start_win_app_server")),
    WIN_APP_DRIVER_REMOTE_SERVER_URL(System.getenv("win_app_driver_remote_server_url")),WIN_APP_DRIVER_CAP_APP("app"),ATTR_NAME("Name"),ATTR_VALUE("Value.Value"),
    CLOSE_WINAPP_EXE(System.getenv("close_winapp_exe")),CLOSE_CMD(System.getenv("close_cmd"));

    public String value;
    WinAppConsts(String value) {
        this.value = value;
    }
}
