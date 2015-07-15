package br.ufpe.cin.writejapanese;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

import br.ufpe.cin.writejapanese.entity.Kanji;


public class MainActivity extends ActionBarActivity implements MainFragment.OnFragmentInteractionListener{

    private Button levelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment() )
                    .commit();
        }

        getSupportActionBar().hide();

        try {
            Database.init(this);

            DB db = Database.getInstance();

            db.del("kanjis");

            ArrayList<Kanji> kanjis = new ArrayList<Kanji>();

            kanjis.add(new Kanji("boi", "Boi", "easy"));
            kanjis.add(new Kanji("irmao", "Irmão", "easy"));
            kanjis.add(new Kanji("rei", "Rei", "easy"));
            kanjis.add(new Kanji("rio", "Rio", "easy"));
            kanjis.add(new Kanji("serpente", "Serpente", "easy"));
            kanjis.add(new Kanji("tres", "Três", "easy"));
            kanjis.add(new Kanji("estrela", "Estrela", "easy"));
            kanjis.add(new Kanji("rocha", "Rocha", "easy"));
            kanjis.add(new Kanji("tesouro", "Tesouro", "easy"));
            kanjis.add(new Kanji("azul", "Azul", "easy"));

            kanjis.add(new Kanji("lua", "Lua", "medium"));
            kanjis.add(new Kanji("braco", "Braço", "medium"));
            kanjis.add(new Kanji("agosto", "Agosto", "medium"));
            kanjis.add(new Kanji("montanha", "Montanha", "medium"));
            kanjis.add(new Kanji("dezembro", "Dezembro", "medium"));
            kanjis.add(new Kanji("felicidade", "Felicidade", "medium"));
            kanjis.add(new Kanji("fevereiro", "Fevereiro", "medium"));
            kanjis.add(new Kanji("japao", "Japão", "medium"));
            kanjis.add(new Kanji("paz", "Paz", "medium"));
            kanjis.add(new Kanji("vida", "Vida", "medium"));
            kanjis.add(new Kanji("fe", "Fê", "medium"));
            kanjis.add(new Kanji("abril", "Abril", "medium"));

            kanjis.add(new Kanji("futuro", "Futuro", "hard"));
            kanjis.add(new Kanji("honestidade", "Honestidade", "hard"));
            kanjis.add(new Kanji("sushi", "Sushi", "hard"));
            kanjis.add(new Kanji("liberdade", "Liberdade", "hard"));
            kanjis.add(new Kanji("sushi", "Sushi", "hard"));
            kanjis.add(new Kanji("decepcao", "Decepção", "hard"));
            kanjis.add(new Kanji("conhecimento", "Conhecimento", "hard"));
            kanjis.add(new Kanji("amizade", "Amizade", "hard"));

            Kanji[] kanjiArr = kanjis.toArray(new Kanji[kanjis.size()]);



            db.put("kanjis", kanjiArr);
        }catch(SnappydbException e){
            Log.e("SnappyDB", e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onFragmentInteraction(String fragment) {
        Intent intent = new Intent(this, IntroductionActivity.class);
        startActivity(intent);
    }


}
