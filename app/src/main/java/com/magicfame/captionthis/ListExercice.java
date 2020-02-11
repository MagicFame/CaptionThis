package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public class ListExercice extends AppCompatActivity {

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.list_exercices);
        user = (User) getIntent().getSerializableExtra("User");
        define();
    }

    protected void define() {
        findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isCalibration() == false) {
                    Snackbar.make(view, "Error, you should do calibration before", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Intent intent = new Intent(ListExercice.this, CameraActivity.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.card_view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isCalibration() == false) {
                    Snackbar.make(view, "Error, you should do calibration before", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Intent intent = new Intent(ListExercice.this, CameraActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                }
            }
        });

    }
}
