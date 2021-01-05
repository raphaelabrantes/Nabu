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
    private  long amdMemoryTotal;
    private final String vendor;


    public GPU(String vendor) throws NotImplementedException{
        this.vendor = vendor;
        if(vendor.equals("amd")){
            this.amdMemoryTotal = (long) (Long.parseLong(
                                readFile(new File("/sys/class/drm/card0/device/mem_info_gtt_total"))) * 1e-6);

        }
        else throw new NotImplementedException("Intel and Nvidea not yet implemented");

    }

    public int getUsage() throws NotImplementedException {
        if(vendor.equals("amd")){
            return amdUsage();
        }
        else throw new NotImplementedException("Intel and Nvidea not yet implemented");
    }

    public String memoryUsageAndTotal(){
        long memoryUsed = Long.parseLong(readFile(new File("/sys/class/drm/card0/device/mem_info_gtt_used")));
        memoryUsed *= 1e-6;
        StringBuilder memory = new StringBuilder("");
        memory.append(memoryUsed).append("/").append(amdMemoryTotal).append("MB");
        return memory.toString();
    }

    private int amdUsage(){
        return Integer.parseInt(readFile(amdGpuBusy));
    }

    private String readFile(File file){
        try {
            Scanner scanner = new Scanner(file);
            String str = scanner.next();
            scanner.close();
            return str;

        } catch (FileNotFoundException e) {
            System.out.printf("Not able to open %s", file.getAbsolutePath());
            System.exit(-230);
            e.printStackTrace();
        }
        return "";
    }

    public int getAmdMemoryUsage(){
       return Integer.parseInt(readFile(amdGpuMem));
    }



    public double getGpuTemp() {
        return Double.parseDouble(readFile(gpuTemp)) / 1000;
    }
}
