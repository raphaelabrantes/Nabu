package dev.abrantes.Nabu.metrics.GPU;

import jdk.jshell.spi.ExecutionControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static jdk.jshell.spi.ExecutionControl.*;

public class GPU {
    private final String amdGpuBusyPath = "/sys/class/drm/card0/device/gpu_busy_percent";
    private final String amdGpuMemPath = "/sys/class/drm/card0/device/mem_busy_percent";
    private final String gpuTempPath = "/sys/class/hwmon/hwmon2/temp1_input";
    private final File amdGpuBusy = new File(amdGpuBusyPath);
    private final File amdGpuMem = new File(amdGpuMemPath);
    private final File gpuTemp = new File(gpuTempPath);
    private final String vendor;


    public GPU(String vendor){
        this.vendor = vendor;

    }

    public int getUsage() throws NotImplementedException {
        if(vendor.equals("amd")){
            return amdUsage();
        }
        else throw new NotImplementedException("Intel and Nvidea not yet implemented");
    }

    private int amdUsage(){
        try {
            Scanner amdGpuBusyScan = new Scanner(amdGpuBusy);
            int usage = Integer.parseInt(amdGpuBusyScan.next());
            amdGpuBusyScan.close();
            return usage;

        } catch (FileNotFoundException e) {
            System.out.println("Not able to open amdGpuBusy");
            System.exit(-230);
            e.printStackTrace();
        }
        return 0;
    }

    public double getGpuTemp() {
        try {
            Scanner gpuTempScan = new Scanner(gpuTemp);
            double tempTemp = Double.parseDouble(gpuTempScan.next()) / 1000;
            gpuTempScan.close();
            return tempTemp;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the gpu Temp File");
            e.printStackTrace();
            System.exit(-40);
        }
        return 0.0;
    }
}
