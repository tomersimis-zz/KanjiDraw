package br.ufpe.cin.writejapanese;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class KanjiDrawActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_draw);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kanji_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        PlaceholderFragment fragment;

        FragmentActivity activity;

        SignaturePad pad;

        public PlaceholderFragment() {
        }

        private double phashDistance(String s, String key){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < s.length(); i++)
                sb.append((char)(s.charAt(i) ^ key.charAt(i % key.length())));
            String result = sb.toString();

            Log.i("PhashDistance", result);
            byte[] arr = result.getBytes();

            int counter = 0;
            for(int i=0; i < arr.length; i++){
                for(int j=0; j < 8; j++){
                    if(((arr[i] >> j) & 1) == 1) counter++;
                }
            }
            return counter;
        }

        public void onPostCheck(String res){
            if( res != null){
                double similarity = 1 - (phashDistance(res, "2475d3225da15bda") / 64.0);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                if(similarity < 0.6){
                    builder.setTitle("Oops... Tente novamente");
                    builder.setMessage("Você não conseguiu desenhar o Kanji.");
                }else if(0.6 <= similarity && similarity <= 0.8){
                    builder.setTitle("Quase lá");
                    builder.setMessage("Seu Kanji está quase perfeito, treine mais um pouco.");
                }else{
                    builder.setTitle("Parabéns");
                    builder.setMessage("Seu Kanji está ótimo!");
                }

                builder.setPositiveButton("Escolher outro", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
                builder.setNegativeButton("Repetir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       pad.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Toast.makeText(getActivity(), "Similarity: " + similarity,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_kanji_draw, container, false);

            fragment = this;
            activity = getActivity();

            Button checkButton = (Button) rootView.findViewById(R.id.checkButton);

            pad = (SignaturePad) rootView.findViewById(R.id.signature_pad);

            checkButton.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    CheckerResult res = new CheckerResult();
                    Log.v("[KenjiDrawActivity]", "Sending image to cloudinary");
                    new CloudinaryTask(pad, res, fragment).execute();

                }
            });

            return rootView;
        }
    }
}
