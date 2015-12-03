package com.example.epatrois.td2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by epatrois on 19/11/15.
 */
public class MyTask extends AsyncTask<String,Void,String> {

    VilleInfo villeInfo;
    String nomVille="";

    String infoTemps="";
    String infoVille="";

    MyTask(VilleInfo vleInfo,String nmVille){
        villeInfo=vleInfo;
        nomVille=nmVille;
    }

    @Override
    protected String doInBackground(String... strings) {

        Log.i("doInBackground Thread", Thread.currentThread().getId() + " " + Thread.currentThread().getName());


        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+nomVille+",uk&appid=2de143494c0b295cca9337e1e96b00e0");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            infoTemps=readStream(urlConnection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        url = null;
        try {

            url = new URL("http://public.opendatasoft.com/api/records/1.0/search?dataset=correspondance-code-insee-code-postal&q="+nomVille+"&facet=nom_dept&facet=nom_region&facet=statut");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            infoVille=readStream(urlConnection.getInputStream());;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String nothing) {
        super.onPostExecute(nothing);
        Log.i("onPostExecute Thread", Thread.currentThread().getId() + " " + Thread.currentThread().getName());

        if(infoTemps!=null){
            updateCityWeatherInfo(infoTemps);
        }

        if(infoVille!=null){
            updateCityInfos(infoVille);
        }

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("onPreExecute Thread", Thread.currentThread().getId() + " " + Thread.currentThread().getName());
    }

    String readStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    void updateCityWeatherInfo(String infos){
        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(infos);
            Log.i("JSON Parse",infos);
            JSONObject weather = mainObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = mainObject.getJSONObject("main");

            float temperatureK= Float.parseFloat(main.getString("temp").toString());
            int temperatureC= (int) (temperatureK - 273.15);
            Log.i("JSON Parse 2", String.valueOf(temperatureC));

            villeInfo.getTextViewTemps().setText(weather.getString("description"));
            villeInfo.getTextViewTemperature().setText(temperatureC+" °");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateCityInfos(String infoVil) {
        JSONObject mainObject = null;
        try {
            Log.i("onPostExecute Thread", Thread.currentThread().getId() + " " + Thread.currentThread().getName());
            mainObject = new JSONObject(infoVil);
            Log.i("JSON Parse",infoVil);
            JSONObject records = mainObject.getJSONArray("records").getJSONObject(0);
            JSONObject fields = records.getJSONObject("fields");
            Log.i("JSONF",fields.toString());
            String population=fields.getString("population");
            String superficie=fields.getString("superficie");
            String region=fields.getString("nom_region");



            villeInfo.getTextViewPopulation().setText(population);
            villeInfo.getTextViewSuperficie().setText(superficie);
            villeInfo.getTextViewRegion().setText(region);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}