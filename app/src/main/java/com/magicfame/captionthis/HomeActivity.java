package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

// Menu activity
public class HomeActivity extends AppCompatActivity {

    User u = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        u = (User) getIntent().getSerializableExtra("User");
        setContentView(R.layout.menu);
        String text = ((TextView) findViewById(R.id.textView3)).getText().toString();
        ((TextView) findViewById(R.id.textView3)).setText(text + " " + u.getUsername());

        addActionButton();
    }

    public void addActionButton() {

        // Just an exercice option
        findViewById(R.id.button_exercice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.list_exercices);
                findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (u.isCalibration() == false) {
                            Snackbar.make(view, "Error, you should do calibration before", Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
                /*
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();
                */
        });

        // Profile option
        findViewById(R.id.button_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Profile.class);
                intent.putExtra("User", u);
                startActivity(intent);

            }
        });
    }
}
