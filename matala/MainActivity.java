package il.co.freebie.matala;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ArticlesListFragment.OnArticleFragmentListener{

    AlarmManager alarmManager;
    private ArrayList<Article> articlesList = new ArrayList<>();
    private ArrayList<WeatherObject> weatherForecastsList = new ArrayList<>();
    private FusedLocationProviderClient client;
    private String notificationCategory = "all";
    private String[] notificationFrequencies = {"1 min", "1 hour", "1 day"};

    final String ARTICLES_LIST_FRAGMENT_TAG = "register_fragment";

    final int LOCATION_PERMISSION_REQUEST = 1;

    private String HEADLINES_NEWS_LINK = "https://newsapi.org/v2/top-headlines?country=us&apiKey=7fd2ac009e6d4982ba2bf6aef000d584";
    private String WEATHER_FORECAST_LINK = "http://api.openweathermap.org/data/2.5/forecast?APPID=8c009e480e8a97bb9f9be10fd75a4401";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("freebie NEWS");
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        //NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //mNotificationManager.cancel(NotifierService.NOTIF_ID);
        String category = "";
        if(getIntent().hasExtra("category_link"))
        {
            category = getIntent().getStringExtra("category_link");
            refreshNews(HEADLINES_NEWS_LINK + category);
        }
        else
        {
            getNews(HEADLINES_NEWS_LINK + category);
        }

        if(Build.VERSION.SDK_INT>=23) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(hasLocationPermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
            }
            else getLocationAndWeatherForecast();
        }
        else getLocationAndWeatherForecast();


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Can't display the weather forecast", Toast.LENGTH_SHORT).show();
            }
            else getLocationAndWeatherForecast();
        }
    }

    public void getLocationAndWeatherForecast(){
        client = LocationServices.getFusedLocationProviderClient(this);

        LocationCallback callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location lastLocation = locationResult.getLastLocation();
                getWeatherForecast(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        //request.setInterval(60000);
        //request.setFastestInterval(500);

        if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(request,callback,null);
        else if(Build.VERSION.SDK_INT<=22)
            client.requestLocationUpdates(request,callback,null);
    }

    private class WeatherForecastAdapter extends FragmentStatePagerAdapter {

        public WeatherForecastAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeatherFragment.newInstance(weatherForecastsList.get(position));
        }

        @Override
        public int getCount() {
            return weatherForecastsList.size();
        }
    }

    public void getWeatherForecast(Double latitude, Double longitude) {
        String linkToWeather = WEATHER_FORECAST_LINK + "&lat=" + latitude + "&lon=" + longitude + "&units=metric";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, linkToWeather, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray forecasts = response.getJSONArray("list");
                    for(int i = 0; i < forecasts.length(); i++){
                        JSONObject currentForecast = forecasts.getJSONObject(i);
                        JSONObject main = currentForecast.getJSONObject("main");
                        String temperature = main.getString("temp") + "°";
                        //String[] temperatureArr = temperature.split(".");
                        String description = currentForecast.getJSONArray("weather").getJSONObject(0).getString("description");
                        String iconCode = currentForecast.getJSONArray("weather").getJSONObject(0).getString("icon");
                        String time = i==0 ? "CURRENT" : currentForecast.getString("dt_txt");

                        WeatherObject weatherObject = new WeatherObject(time, description, temperature, iconCode);
                        weatherForecastsList.add(weatherObject);
                    }

                    if(weatherForecastsList.size() > 0){
                        ViewPager pager = findViewById(R.id.pager);
                        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getSupportFragmentManager());
                        pager.setAdapter(adapter);
                        //pager.setCurrentItem(0);
                    }


                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    public void getNews(String link){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray articles = response.getJSONArray("articles");
                    for(int i = 0; i < articles.length(); i++){
                        JSONObject currentArticle = articles.getJSONObject(i);
                        JSONObject source = currentArticle.getJSONObject("source");
                        String sourceName = source.getString("name");
                        String author = currentArticle.getString("author");
                        String title = currentArticle.getString("title");
                        String description = currentArticle.getString("description");
                        String url = currentArticle.getString("url");
                        String urlToImage = currentArticle.getString("urlToImage");
                        String publishedAt = currentArticle.getString("publishedAt");
                        String content = currentArticle.getString("content");

                        if(publishedAt != null){
                            publishedAt = publishedAt.substring(0, publishedAt.length() - 1);
                            String[] publishedAtArr = publishedAt.split("T");
                            publishedAt = publishedAtArr[1] + "  " + publishedAtArr[0];
                        }

                        Article article = new Article(sourceName, title, author, description, url,
                                urlToImage, publishedAt, content);
                        articlesList.add(article);
                    }

                    ArticlesListFragment articlesListFragment = ArticlesListFragment.newInstance(articlesList);
                    getFragmentManager().beginTransaction().add(R.id.root_container, articlesListFragment, ARTICLES_LIST_FRAGMENT_TAG).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
        //queue.start();
    }

    @Override
    public void onClicked(Article article) {
        Intent intent = new Intent(this, SelectedArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.all_category_action:
                refreshNews(HEADLINES_NEWS_LINK);
                notificationCategory = "all";
                break;
            case R.id.health_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=health");
                notificationCategory = "health";
                break;
            case R.id.sports_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=sports");
                notificationCategory = "sports";
                break;
            case R.id.technology_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=technology");
                notificationCategory = "technology";
                break;
            case  R.id.action_notify:
                //HERE SET NOTIFICATION
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You will get a notifications about " + notificationCategory + " news each:")
                       // .setMessage("If you want to get notification for another category, please change it in the menu and tap on the bell again.")
                        .setItems(notificationFrequencies, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this,"To get notification for another category, change it in the menu and tap on the bell again", Toast.LENGTH_LONG).show();


                                Integer freqMillis = 0;
                                switch (i){
                                    //case 0: freqMillis = 60 * 1000; break;
                                    case 0: freqMillis = 60 * 1000; break;
                                    case 1: freqMillis = 3600 * 1000; break;
                                    case 2: freqMillis = 24 * 3600 * 1000; break;
                                }

                                Intent intent = new Intent(MainActivity.this, NotifierService.class);
                                intent.putExtra("notif_freq", freqMillis);
                                intent.putExtra("notif_category", notificationCategory);
                                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freqMillis,pendingIntent);
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
                break;
            case R.id.action_notify_off:
                Intent intent = new Intent(MainActivity.this,NotifierService.class);

                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);
                stopService(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshNews(String link){
        android.app.Fragment fragment = getFragmentManager().findFragmentByTag(ARTICLES_LIST_FRAGMENT_TAG);
        if(fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
        articlesList.clear();
        getNews(link);
    }

    /*private class MyItemsListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == DialogInterface.BUTTON_NEGATIVE) {
            }

            else {
                Toast.makeText(MainActivity.this,"To get notification for another category, change it in the menu and tap on the bell again", Toast.LENGTH_LONG).show();


                Integer freqMillis = 0;
                switch (i){
                    //case 0: freqMillis = 60 * 1000; break;
                    case 0: freqMillis = 5 * 1000; break;
                    case 1: freqMillis = 3600 * 1000; break;
                    case 2: freqMillis = 24 * 3600 * 1000; break;
                }

                Intent intent = new Intent(MainActivity.this, NotifierService.class);
                intent.putExtra("notif_freq", freqMillis);
                intent.putExtra("notif_category", notificationCategory);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freqMillis,pendingIntent);

            }
        }
    }*/
}
