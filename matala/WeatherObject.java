package il.co.freebie.matala;

/**
 * Created by one 1 on 12-Jan-19.
 */

public class WeatherObject {
    private String forecastTime;
    private String forecastDescription;
    private String temperature;
    private String urlToIcon;

    public WeatherObject(String forecastTime, String forecastDescription, String temperature, String iconCode) {
        this.forecastTime = forecastTime;
        this.forecastDescription = forecastDescription;
        this.temperature = temperature;
        this.urlToIcon = "http://openweathermap.org/img/w/" + iconCode + ".png";

    }

    public String getForecastTime() {
        return forecastTime;
    }

    public String getForecastDescription() {
        return forecastDescription;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getUrlToIcon() {
        return urlToIcon;
    }
}
