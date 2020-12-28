package dev.abrantes.Nabu.metrics;

public class Core {
    private final String coreName;
    private long lastIdle;
    private long lastTotal;
    private double usage;
    public Core(String name, long total, long idle){
        coreName = name;
        lastTotal = total;
        lastIdle = idle;

    }
    public String toString(){
        return coreName + ": " + usage + "\n";
    }
    public void refresh(long total, long idle){
        long cleanTotal = total - lastTotal;
        long cleanIdle = idle - lastIdle;
        usage = (cleanTotal - cleanIdle) * 100.0 / cleanTotal;
        lastIdle = idle;
        lastTotal = total;
    }
    public String getCoreName(){return coreName;}
    public double getUsage(){return usage;}
}