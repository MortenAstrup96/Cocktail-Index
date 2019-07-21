package dev.astrup.cocktailindex.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import dev.astrup.cocktailindex.Fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();//finish your activity
        }
        return super.onOptionsItemSelected(item);
    }
}
