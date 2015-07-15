package br.ufpe.cin.writejapanese;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.SnappydbException;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.writejapanese.entity.Kanji;
import br.ufpe.cin.writejapanese.pad.SignaturePad;

/**
 * Created by Tomer Simis on 25/06/2015.
 */
public class KanjiDrawFragment extends Fragment {

    KanjiDrawFragment fragment;

    FragmentActivity activity;

    SignaturePad pad;

    Kanji kanji;

    public KanjiDrawFragment() {
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

            Toast.makeText(getActivity(), "Similarity: " + similarity, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kanji_draw, container, false);

        fragment = this;
        activity = getActivity();

        try{
            Kanji[] kanjis = Database.getInstance().getObjectArray("kanjis", Kanji.class);// get array of string
            for(Kanji k : kanjis){
                if(k.getId().equals(getArguments().getString("kanji_id"))){
                    kanji = k;
                    break;
                }
            }

        }catch(SnappydbException e){
            Log.e("SnappyDB", e.getMessage());
        }

        Button checkButton = (Button) rootView.findViewById(R.id.checkButton);

        pad = (SignaturePad) rootView.findViewById(R.id.signature_pad);

        TextView kanjiName = (TextView) rootView.findViewById(R.id.kanjiName);
        kanjiName.setText(kanji.getName());

        Drawable kanjiImage = kanji.getImage(getActivity());

        pad.setBackground(kanjiImage);

        if(kanji.getLevel().equals("easy")){
            pad.setMinWidth(100);
            pad.setMaxWidth(120);
        }

        try {

            final Mat kanjiMat = Utils.loadResource(getActivity(), getResources().getIdentifier(kanji.getId() + "_black", "drawable", getActivity().getPackageName()));

            if(kanjiMat.empty()){
                Log.e("OpenCV", "Kanji Matrix is empty!!!!!!!!!!");
            }

            Imgproc.cvtColor(kanjiMat, kanjiMat, Imgproc.COLOR_RGB2GRAY, 1);

            //Imgproc.adaptiveThreshold(kanjiMat, kanjiMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 4);
            //Imgproc.threshold(kanjiMat, kanjiMat, -1, 255,
                   // Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU);

            Imgproc.threshold(kanjiMat, kanjiMat, 123, 255, Imgproc.THRESH_BINARY_INV);

            /*

           // Imgproc.erode(kanjiMat, kanjiMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(40,40)));
           // Imgproc.dilate(kanjiMat, kanjiMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(40,40)));

            final List<MatOfPoint> kanjiContours = new ArrayList<>();
            final Mat kanjiHier = new Mat();
            //Imgproc.findContours(kanjiMat, kanjiContours, kanjiHier, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < 1; i++) {
                //Imgproc.drawContours(kanjiMat, kanjiContours, i, new Scalar(255, 0, 0));
            }
            */

            checkButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.v("[KenjiDrawActivity]", "Findind drawed image contours");

                    /*
                    Bitmap img = pad.getSignatureBitmap();

                    Mat userImageMat = new Mat();
                    Utils.bitmapToMat(img, userImageMat);

                    Imgproc.cvtColor(userImageMat, userImageMat, Imgproc.COLOR_RGB2GRAY, 1);

                    Imgproc.erode(userImageMat, userImageMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(40,40)));
                    Imgproc.dilate(userImageMat, userImageMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(40,40)));

                    final List<MatOfPoint> userImageContours = new ArrayList<>();
                    final Mat hierarchy = new Mat();
                    Imgproc.findContours(userImageMat, userImageContours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                    for (int i = 0; i < userImageContours.size(); i++) {
                        Imgproc.drawContours(userImageMat, userImageContours, i, new Scalar(255, 0, 0));
                    }

                   /* double[] contours = new double[kanjiContours.size()];

                    for(int i=0; i < kanjiContours.size(); i++){
                        for(int j=0; j < userImageContours.size(); j++){
                            double res = Imgproc.matchShapes(kanjiContours.get(i), userImageContours.get(j), Imgproc.CV_CONTOURS_MATCH_I1, 0);
                            contours[i] = Math.max(res, contours[i]);
                        }
                    }

                    double sum = 0;
                    for(int i=0; i < contours.length; i++){
                        sum += contours[i];
                    }

                    sum = sum/((double) contours.length);*/
                    /*
                    double res = Imgproc.matchShapes(kanjiContours.get(0), userImageContours.get(0), Imgproc.CV_CONTOURS_MATCH_I1, 0);




                    Toast.makeText(getActivity(), "Similarity: " + res, Toast.LENGTH_LONG).show();

                    */

                    Bitmap img = pad.getSignatureBitmap();

                    Mat userImageMat = new Mat();

                    Utils.bitmapToMat(img, userImageMat);

                    Imgproc.cvtColor(userImageMat, userImageMat, Imgproc.COLOR_RGB2GRAY, 1);

                    Imgproc.threshold(userImageMat, userImageMat, 123, 255, Imgproc.THRESH_BINARY_INV);

                    Mat resz = new Mat();
                    Imgproc.resize(userImageMat, resz, new Size(img.getWidth(), img.getHeight()));

                    Imgproc.resize(kanjiMat, kanjiMat, new Size(img.getWidth(), img.getHeight()));

                    int equal = 0;

                    for(int i=0; i < kanjiMat.rows(); i++){
                        for(int j=0; j < kanjiMat.cols(); j++){
                            if(kanjiMat.get(i,j)[0] == 255 &&  kanjiMat.get(i,j)[0] == userImageMat.get(i,j)[0]) equal++;
                        }
                    }

                    double similarity = ((double) equal)/((double) kanjiMat.cols()*kanjiMat.rows());

                    //Log.i("SIMILARITY", "Value: " + similarity);

                    //Toast.makeText(getActivity(), "Similarity: " + similarity, Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    if(similarity < 0.10){
                        builder.setTitle("Oops... Tente novamente");
                        builder.setMessage("Você não conseguiu desenhar o Kanji.");
                    }else if(0.10 <= similarity && similarity <= 0.15){
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

                }
            });

        } catch(Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_clear){
            pad.clear();
        }

        return super.onOptionsItemSelected(item);
    }
}