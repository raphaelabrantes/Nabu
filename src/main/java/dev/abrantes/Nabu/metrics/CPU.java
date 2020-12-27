package dev.abrantes.Nabu.metrics;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CPU {
    private final String[] command = new String[]{"grep", "cpu", "/proc/stat"};
    private final String tempPath = "/sys/class/hwmon/hwmon0/temp%d_input";
    private final File tempFile1 = new File(String.format(tempPath, 1));
    private final File tempFile2 = new File(String.format(tempPath, 2));

    private ArrayList<String> cpus;
    private ArrayList<Double> cpuTemp;

    public CPU() {
        get_metrics();
    }

    public void get_metrics() {
        try {
            Process proc = new ProcessBuilder(command).start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            cpus = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                cpus.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double temp1 = readTemp(tempFile1);
        double temp2 = readTemp(tempFile2);
        cpuTemp = new ArrayList<Double>();
        cpuTemp.add(temp1);
        cpuTemp.add(temp2);
    }

    public int getNCores() {
        return cpus.size() - 1;
    }

    public double getCoreUsage(int core) {
        int search = core != 13 ? core + 1 : 0;
        int index = core != 13 ? 1 : 2;
        String[] coreInfo = cpus.get(search).split(" ");
        System.out.println(coreInfo[0]);
        long user = parser(index++, coreInfo);
        long nice = parser(index++, coreInfo);
        long system = parser(index++, coreInfo);
        long idle = parser(index++, coreInfo);
        long iowait = parser(index++, coreInfo);
        long irq = parser(index++, coreInfo);
        long softirq = parser(index, coreInfo);
        long total = user + nice + idle + system + iowait + irq + softirq;
        return (total - idle) * 100.0 / total;
    }

    private long parser(int index, String[] coreinfo){
        return Long.parseLong(coreinfo[index]);
    }

    public ArrayList<Double> getCoreTemps() {
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
