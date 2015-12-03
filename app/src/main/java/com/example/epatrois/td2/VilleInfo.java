package com.example.epatrois.td2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class VilleInfo extends AppCompatActivity {

    private TextView textViewMaire;
    private TextView textViewPays;
    private TextView textViewPopulation;
    private TextView textViewSuperficie;
    private TextView textViewRegion;
    private TextView textViewTemps;
    private TextView textViewTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville_info);

        String ville = this.getIntent().getStringExtra("nomVille");

        textViewMaire=(TextView) findViewById(R.id.textViewMaire);
        textViewPays=(TextView) findViewById(R.id.textViewPays);
        textViewPopulation=(TextView) findViewById(R.id.textViewPopulation);
        textViewSuperficie=(TextView) findViewById(R.id.textViewSuperficie);
        textViewRegion=(TextView) findViewById(R.id.textViewRegion);
        textViewTemps=(TextView) findViewById(R.id.textViewTemps);
        textViewTemperature =(TextView) findViewById(R.id.textViewTemperature);

        setTitle(ville);
        setTexts();

        Bundle b = this.getIntent().getExtras();
        boolean alre=b.getBoolean("alreadyPass");

        if(!alre)
        {
            MyTask task = new MyTask(this,ville);
            task.execute();
            Bundle already = new Bundle();
            already.putBoolean("alreadyPass",true);
            this.getIntent().putExtras(already);
        }




    }


    public void setTexts(){

        getTextViewMaire().setText("Jean Claude Gaudin Skywalker");
        getTextViewPays().setText("France");
        getTextViewPopulation().setText("20 000");
        getTextViewSuperficie().setText("20 000km");
        getTextViewRegion().setText("PACA");
        getTextViewTemps().setText("Beau temps");

    }

    public TextView getTextViewMaire() {
        return textViewMaire;
    }

    public TextView getTextViewPays() {
        return textViewPays;
    }

    public TextView getTextViewPopulation() {
        return textViewPopulation;
    }

    public TextView getTextViewSuperficie() {
        return textViewSuperficie;
    }

    public TextView getTextViewRegion() {
        return textViewRegion;
    }

    public TextView getTextViewTemps() {
        return textViewTemps;
    }

    public TextView getTextViewTemperature() {
        return textViewTemperature;
    }
}
