package alexander.korovin.com.smartlabels.Utils;

import android.app.Application;

import alexander.korovin.com.smartlabels.OpenWeathetMapApi.IOpenWeatherMapApi;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;

public class App extends Application {

    private static IOpenWeatherMapApi openWeatherMapApi;
    private Retrofit retrofit;
    private static final String baseUrl = "http://api.openweathermap.org/data/2.5/";

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherMapApi = retrofit.create(IOpenWeatherMapApi.class);
    }

    public static IOpenWeatherMapApi getApi() {
        return openWeatherMapApi;
    }
}
