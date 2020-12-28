package dev.abrantes.Nabu.metrics;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CPU {
    private final String[] command = new String[]{"grep", "cpu", "/proc/stat"};
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
            Process proc = new ProcessBuilder(command).start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String allCpu = reader.readLine();
            String line = "";
            while ((line = reader.readLine()) != null) {
                Core newCore = createCore(line, false );
                cores.add(newCore);
            }
            reader.close();
            Core totalCore = createCore(allCpu, true);
            cores.add(totalCore);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshCores(){
        try {
            Process proc = new ProcessBuilder(command).start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String allCpu = reader.readLine();
            updateCore(allCpu, IDLE_INDEX + 1, 2, getNCores());
            for(int core = 0; core < getNCores(); core++){
                updateCore(reader.readLine(), IDLE_INDEX, 1, core);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCore(String line, int idleIndex, int startIndex, int core){
        String [] coreInfo = line.split(" ");
        long idle = parser(idleIndex, coreInfo);
        long total = calculateUsageTotal(startIndex, coreInfo);
        cores.get(core).refresh(total, idle);
    }

    public ArrayList<Core> getCores() {
        return cores;
    }

    public int getNCores() {
        return cores.size() - 1;
    }

    private Core createCore(String line, boolean isAll){
        String[] coreInfo = line.split(" ");
        int plus = isAll ? 1:0;
        long total = calculateUsageTotal(1+plus, coreInfo);
        String name = coreInfo[NAME_INDEX];
        long idle = parser(IDLE_INDEX + plus, coreInfo);
        return new Core(name, total, idle);
    }

    private long calculateUsageTotal(int index, String [] coreInfo) {
        long total = 0;
        for(int count = 0; count < PROCESSN; count++, index++){
            total += parser(index, coreInfo);
        }
        return total;
    }

    private long parser(int index, String[] coreinfo){
        return Long.parseLong(coreinfo[index]);
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
            e.printStackTrace();
        }
        return temp;

    }
}

