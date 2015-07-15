package br.ufpe.cin.writejapanese;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.paolorotolo.appintro.AppIntro;

import br.ufpe.cin.writejapanese.introduction.FirstSlide;
import br.ufpe.cin.writejapanese.introduction.SecondSlide;
import br.ufpe.cin.writejapanese.introduction.ThirdSlide;


public class IntroductionActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new FirstSlide(), getApplicationContext());
        addSlide(new SecondSlide(), getApplicationContext());
        addSlide(new ThirdSlide(), getApplicationContext());

        // OPTIONAL METHODS
        // Override bar/separator color
        //setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#f1f1f1"));

        // Hide Skip button
        showSkipButton(false);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, KanjiLevelActivity.class);
        startActivity(intent);
    }

}
