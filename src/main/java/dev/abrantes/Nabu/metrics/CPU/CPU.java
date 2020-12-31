package dev.abrantes.Nabu.metrics.CPU;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class CPU {
    private final String procStat = "/proc/stat";
    private final File procStateFile = new File(procStat);
    private final String tempPath = "/sys/class/hwmon/hwmon0/temp%d_input";
    private final File tempFileTdie = new File(String.format(tempPath, 1));
    private final File tempFileCTL = new File(String.format(tempPath, 2));
    private final int IDLE_INDEX = 4;
    private final int PROCESSN = 7;
    private final int NAME_INDEX = 0;
    private final ArrayList<Core> cores = new ArrayList<>();
    private final ArrayList<Double> cpuTemp = new ArrayList<>();

    public CPU() {
        generateCores();
        get_metrics();
    }

    public void get_metrics() {
        double tempDie = readTemp(tempFileTdie);
        double tempCTL = readTemp(tempFileCTL);
        cpuTemp.clear();
        cpuTemp.add(tempDie);
        cpuTemp.add(tempCTL);
        refreshCores();
    }

    public void generateCores(){
        try {
            Scanner readProcStat = new Scanner(procStateFile);
            String allCpu = readProcStat.nextLine();
            allCpu = allCpu.replace("  ", " ");
            String line = "";
            while ((line = readProcStat.nextLine()) != null  &&  line.contains("cpu")) {
                Core newCore = createCore(line);
                cores.add(newCore);
            }
            readProcStat.close();
            Core totalCore = createCore(allCpu);
            cores.add(totalCore);


        } catch (IOException e) {
            System.out.print("Not possible to generate cores");
            e.printStackTrace();
            System.exit(-240);
        }
    }

    private void refreshCores(){
        try {
            Scanner readProcStat = new Scanner(procStateFile);
            String allCpu = readProcStat.nextLine();
            updateCore(allCpu.replace("  ", " "), getNCores());

            String line = "";
            for(int core = 0; core < getNCores(); core++){
                updateCore(readProcStat.nextLine(), core);
            }

            readProcStat.close();
        } catch (IOException e) {
            System.out.println("Not possible to refresh cores");
            e.printStackTrace();
            System.exit(-240);
        }
    }

    private void updateCore(String line, int core){
        String [] coreInfo = line.split(" ");
        long idle = parser(IDLE_INDEX, coreInfo);
        long total = calculateUsageTotal(coreInfo);
        cores.get(core).refresh(total, idle);
    }

    public ArrayList<Core> getCores() {
        return cores;
    }

    public int getNCores() {
        return cores.size() - 1;
    }

    private Core createCore(String line){
        String[] coreInfo = line.split(" ");
        long total = calculateUsageTotal(coreInfo);
        String name = coreInfo[NAME_INDEX];
        long idle = parser(IDLE_INDEX, coreInfo);
        return new Core(name, total, idle);
    }

    private long calculateUsageTotal(String [] coreInfo) {
        long total = 0;
        for(int index=1; index <= PROCESSN; index++){
            total += parser(index, coreInfo);
        }
        return total;
    }

    private long parser(int index, String[] coreInfo){
        return Long.parseLong(coreInfo[index]);
    }

    public ArrayList<Double> getCpuTemps() {
        return cpuTemp;
    }

    private double readTemp(File tempFile) {
        double temp = 0;
        try {
            Scanner tempScan = new Scanner(tempFile);
            temp = Double.parseDouble(tempScan.next()) / 1000;
            tempScan.close();

        } catch (FileNotFoundException e) {
            System.out.println("Not possible to get CPU temperature");
            e.printStackTrace();
            System.exit(-240);
        }
        return temp;

    }
}

