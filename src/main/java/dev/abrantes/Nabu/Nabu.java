package dev.abrantes.Nabu;

import dev.abrantes.Nabu.APIs.WeatherApi;
import dev.abrantes.Nabu.metrics.CPU;

public class Nabu {
    public static void main(String[] args) {
        WeatherApi weather = new WeatherApi("Taipei");
        weather.getTemperature();
        CPU cpu = new CPU();
        System.out.println(cpu.getCoreUsage(2));
        System.out.println(cpu.getCoreTemps());
    }
}
