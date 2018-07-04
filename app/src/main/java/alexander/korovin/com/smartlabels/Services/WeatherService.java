package alexander.korovin.com.smartlabels.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import alexander.korovin.com.smartlabels.Activities.MainActivity;
import alexander.korovin.com.smartlabels.Models.Weather;
import alexander.korovin.com.smartlabels.R;
import alexander.korovin.com.smartlabels.Utils.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService extends Service {
    private static final String APP_ID_KEY = "480977819e7a40036456f129028da3f7";
    private static final String UNITS = "metric";
    private final IBinder iBinder = new ServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void getWeather() {
        App.getApi().getData(MainActivity.currentLocality, APP_ID_KEY, UNITS).enqueue(new Callback<Weather>() {

            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather data = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), String.format("%s%s", getString(R.string.weather_in_your_city), data.getTempWithDegree()), Toast.LENGTH_SHORT).show();
                    // currentWeatherTextView.setText(String.format("%s%s", getString(R.string.weather_in_your_city), data.getTempWithDegree()));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ServiceBinder extends Binder {
        public WeatherService getWeatherService() {
            return WeatherService.this;
        }
    }
}
