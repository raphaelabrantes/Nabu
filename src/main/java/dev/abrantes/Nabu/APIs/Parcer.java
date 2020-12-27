package dev.abrantes.Nabu.APIs;

import java.util.ArrayList;

public class Parcer {
    Coord coord;
    ArrayList<Weather> weather;
    String base;
    Main main;
    double visibility;
    Wind wind;
    Clouds clouds;
    Sys sys;
    long dt;
    long timezone;
    long id;
    String name;
    int cod;
}

class Coord {
    double lon;
    double lat;
}

class Weather {
    int id;
    String main;
    String description;
    String icon;
}

class Main {
    double temp;
    double feels_like;
    double temp_min;
    double temp_max;
    int pressure;
    int humidity;
}

class Wind {
    double speed;
    int deg;
}

class Clouds {
    double all;
}

class Sys {
    int type;
    int id;
    double message;
    String country;
    long sunrise;
    long sunset;
}