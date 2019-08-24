package dev.astrup.cocktailindex.Modules.Various;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.astrup.cocktailindex.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ConstraintLayout mailLink = findViewById(R.id.about_list_contact);
        mailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create the Intent */
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","cocktail@astrup.dev", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Cocktail Index");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        ConstraintLayout rateButton = findViewById(R.id.about_list_rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }
        });

        ConstraintLayout shareButton = findViewById(R.id.about_list_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apppath = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Cocktail Index is an app which lets you easily organise all of your cocktail recipes! You can find it here: " + apppath);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        ConstraintLayout privacypolicyButton = findViewById(R.id.about_list_privacypolicy);
        privacypolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = "https://cocktail-index.flycricket.io/privacy.html";
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TextView textView_version = findViewById(R.id.about_textview_version);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            textView_version.setText("Version " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }






        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {

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
