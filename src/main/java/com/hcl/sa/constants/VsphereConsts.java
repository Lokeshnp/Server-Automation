package com.hcl.sa.constants;

public enum VsphereConsts {
    VM_DETAILS_JSON_PATH(System.getenv("vsphere_json_path")),VIRTUAL_MACHINE_JSON_OBJ("virtualMachines"),BASELINE_FIXLETS("baselineFixlets"),
    APPLE_ITUNES("Apple iTunes 12.10.7 Available");

    public String text;

    VsphereConsts(String text){
        this.text = text;
    }
}
