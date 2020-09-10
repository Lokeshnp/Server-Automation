package com.hcl.sa.utils.vsphere;

import com.hcl.sa.utils.bigfix.Credentials;
import vmware.samples.VsphereUtils;

public class Vsphere {
public VsphereUtils vsphereUtils = new VsphereUtils(Credentials.VSPHERE.getUsername(),Credentials.VSPHERE.getPassword());

public String getVirtualMachineIP(String vmName,String snapshotName){
        String vmIP = null;
    try {
        vsphereUtils.execute(vmName);
        vsphereUtils.revertTo(snapshotName);
        vsphereUtils.powerON();
        vsphereUtils.getIP();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return vmIP;
}
}
