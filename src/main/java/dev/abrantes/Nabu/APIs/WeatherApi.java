package dev.abrantes.Nabu.APIs;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;

public class WeatherApi {
    private String key = "";
    private String city;
    private static String query = "SELECT id FROM weather WHERE city=?";
    int cityID;

    public WeatherApi(String city) {
        this.city = city;
        this.cityID = getCityId();
        readKey();
    }

    public void getTemperature() {
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s&units=metric", this.cityID, this.key);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = (String) response.body();
            Parcer weather_info = new Gson().fromJson(body, Parcer.class);
            System.out.println(weather_info.main.temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readKey(){
        InputStream in = getClass().getResourceAsStream("/weatherKey");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            key = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCityId() {
        int id = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite::resource:Nabu.db");
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.city);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-10);
        }
        return id;
    }
}
