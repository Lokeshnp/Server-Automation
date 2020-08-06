package com.hcl.sa.utils;

import vmware.samples.VsphereUtils;

public class Vsphere {

    public VsphereUtils vsphereUtils = new VsphereUtils(Credentials.VSPHERE.getUsername(),Credentials.VSPHERE.getPassword());

    public String getVirtualMachineIP(String vmName,String snapshotName){
        String vmIp = null;
        try {
            vsphereUtils.execute(vmName);
            vsphereUtils.revertTo(snapshotName);
            vsphereUtils.powerON();
            vmIp = vsphereUtils.getIP();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vmIp;
    }

}
