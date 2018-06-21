package alexander.korovin.com.smartlabels.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public class WeatherTemp {
        Double temp;
    }

    public class WeatherDescription {
        String icon;
    }

    @SerializedName("main")
    private WeatherTemp temp;

    @SerializedName("weather")
    private List<WeatherDescription> desctiption;

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timestamp;

    public Weather(WeatherTemp temp, List<WeatherDescription> desctiption) {
        this.temp = temp;
        this.desctiption = desctiption;
    }

    public String getTempWithDegree() {
        return String.valueOf(temp.temp.intValue()) + "\u00B0";
    }

    public String getCity() {
        return city;
    }

    public String getIcon() {
        return desctiption.get(0).icon;
    }

}
