package il.co.freebie.matala;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by one 1 on 12-Jan-19.
 */

public class WeatherFragment extends Fragment{
    private static WeatherObject weather;

    public static WeatherFragment newInstance(WeatherObject weatherObj){
        WeatherFragment fragment = new WeatherFragment();
        weather = weatherObj;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.weather_fragment, container, false);
        TextView timeTv = root.findViewById(R.id.wtime_tv);
        TextView descriptionTv = root.findViewById(R.id.wdescription_tv);
        TextView temperatureTv = root.findViewById(R.id.wtemp_tv);
        ImageView iconWeatherIv = root.findViewById(R.id.wicon_iv);

        timeTv.setText(weather.getForecastTime());
        descriptionTv.setText(weather.getForecastDescription());
        temperatureTv.setText(weather.getTemperature());
        Picasso.get().load(weather.getUrlToIcon()).into(iconWeatherIv);

        return root;
    }
}
