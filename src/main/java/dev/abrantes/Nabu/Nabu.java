package dev.abrantes.Nabu;

import dev.abrantes.Nabu.APIs.WeatherApi;
import dev.abrantes.Nabu.metrics.CPU;
import dev.abrantes.Nabu.metrics.Core;

import java.util.ArrayList;
import java.util.Locale;

public class Nabu {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        WeatherApi weather = new WeatherApi("Santos");
        CPU cpu = new CPU();
        System.out.printf("Temperature in %s: %.2f°C%s%n%n",
                weather.getCity(),
                weather.getTemperature(),
                "\n".repeat(cpu.getNCores()+2));

        ArrayList<Core> cores = cpu.getCores();
        ArrayList<Double> cpuTemps = cpu.getCpuTemps();
        while (true) {
            try {
                Thread.sleep(500);
                cpu.get_metrics();
                System.out.print(CSI.eraseLineMoveUp(cpu.getNCores() + 3) + CSI.eraseLine());
                System.out.printf("Temperature Tdie (REAL) of CPU: %.2f°C\nTemperature CTL of CPU: %.2f°C%n",
                        cpuTemps.get(0), cpuTemps.get(1));
                for (Core core: cores) {
                    System.out.printf("%s: %.2f%s%n", core.getCoreName(), core.getUsage(), "%");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
