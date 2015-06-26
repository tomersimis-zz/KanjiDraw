package br.ufpe.cin.writejapanese;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.Button;

import com.snappydb.DB;
import com.snappydb.SnappydbException;

import br.ufpe.cin.writejapanese.entity.Kanji;


public class MainActivity extends ActionBarActivity implements MainFragment.OnFragmentInteractionListener, KanjiFragment.OnFragmentInteractionListener{

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

        try {
            Database.init(this);

            DB db = Database.getInstance();

            db.put("success", new Kanji("success", "Sucesso"));
            db.put("reason", new Kanji("reason", "Razão"));
            db.put("superior", new Kanji("superior", "Superior"));
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
        if(fragment.equals("KenjiFragment")){
            Fragment newFragment = new KanjiFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

    }

    @Override
    public void onFragmentInteractionKanji(String id) {
        Intent intent = new Intent(this, KanjiDrawActivity.class);
        intent.putExtra("kanji_id", id);
        startActivity(intent);
    }
}
