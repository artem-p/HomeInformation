package ru.artempugachev.homeinformation;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideStatusBar();
        setUpDateView();

        TextView curWeatherTextView = (TextView) findViewById(R.id.curWeatherTextView);
        curWeatherTextView.setText(BuildConfig.DARK_SKY_API_KEY);
    }

    private void setUpDateView() {
        TextClock dateView = (TextClock) findViewById(R.id.dateView);
        DateText dateText = new DateText();
        dateView.setFormat12Hour(dateText.print());
        dateView.setFormat24Hour(dateText.print());
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
