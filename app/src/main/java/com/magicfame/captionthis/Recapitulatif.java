package com.magicfame.captionthis;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Recapitulatif extends AppCompatActivity {


    private float score;
    private int nombreDeRep;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap);
        this.score = getIntent().getExtras().getFloat("score");
        int repNumber = getIntent().getExtras().getInt("nombreRepetition");
        this.nombreDeRep = (int) (repNumber + Math.sqrt((double) repNumber) / 2);
        setContent();
    }

    protected void setContent(){
        RatingBar rating = findViewById(R.id.ratingBar);
        rating.setNumStars(5);
        rating.setIsIndicator(true);
        rating.setRating(this.score%5);

        TextView nombreRep = findViewById(R.id.textView8);
        nombreRep.setText(String.valueOf(nombreDeRep));

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
