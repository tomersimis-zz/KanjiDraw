package br.ufpe.cin.writejapanese;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.SnappydbException;

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
            kanji = Database.getInstance().getObject(getArguments().getString("kanji_id"), Kanji.class);
        }catch(SnappydbException e){
            Log.e("SnappyDB", e.getMessage());
        }

        Button checkButton = (Button) rootView.findViewById(R.id.checkButton);

        pad = (SignaturePad) rootView.findViewById(R.id.signature_pad);

        TextView kanjiName = (TextView) rootView.findViewById(R.id.kanjiName);
        kanjiName.setText(kanji.getName());

        pad.setBackground(kanji.getImage(getActivity()));

        checkButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                CheckerResult res = new CheckerResult();
                Log.v("[KenjiDrawActivity]", "Sending image to cloudinary");
                //new CloudinaryTask(pad, res, fragment).execute();

            }
        });

        return rootView;
    }
}