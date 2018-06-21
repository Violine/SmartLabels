package alexander.korovin.com.smartlabels.OpenWeathetMapApi;

import alexander.korovin.com.smartlabels.Models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMapApi {
    @GET("weather")
    Call<Weather> getData(@Query("q") String cityName, @Query("APPID") String addID, @Query("units") String units);
}