package com.magicfame.captionthis;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Recapitulatif extends AppCompatActivity {


    private float valeurDebut, valeurFin;
    private int nombreDeRep;
    private double temps;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap);
        this.valeurDebut = getIntent().getExtras().getFloat("scoreDebut");
        this.valeurFin = getIntent().getExtras().getFloat("scoreFin");
        int repNumber = getIntent().getExtras().getInt("nombreRepetition");
        this.temps = getIntent().getExtras().getDouble("temps");
        this.nombreDeRep = (int) (repNumber + Math.sqrt((double) repNumber) / 2);
        setContent();
    }

    protected void setContent(){
        float score = calculeScore(valeurDebut, valeurFin, temps);
        RatingBar rating = findViewById(R.id.ratingBar);
        rating.setNumStars(5);
        rating.setIsIndicator(true);
        rating.setRating(score);

        TextView nombreRep = findViewById(R.id.textView8);
        nombreRep.setText(String.valueOf(nombreDeRep));

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected float calculeScore(float valeurDebut, float valeurFin, double temps){
        int scoreFin = calculeScoreFin(valeurFin);
        int scoreDebut = calculeScoreDebut(valeurDebut);
        int scoreTemps = calculeScoreTemps(temps);

        modifyConseilLayout(scoreDebut, scoreFin, scoreTemps);

        return (scoreFin + scoreDebut + scoreTemps) / 3;
    }

    protected void modifyConseilLayout(int scoreDebut, int scoreFin, int scoreTemps){
        TextView conseil = findViewById(R.id.textView4);
        // score début le plus petit
        if(scoreDebut < scoreFin && scoreDebut < scoreTemps) conseil.setText("Penser à bien mettre les bras en angle droit sur la position de départ");
        else if(scoreFin < scoreDebut && scoreFin < scoreTemps) conseil.setText("Penser à bien tendre les bras en position étendu");
        else if(scoreTemps < scoreDebut && scoreTemps < scoreFin) conseil.setText("Penser à prendre plus de temps durant chaque répétition");
        else conseil.setText("Travailler l'exercice doucement afin de corriger l'ensemble du mouvement");
    }

    protected int calculeScoreTemps(double temps){
        if(this.nombreDeRep > 0){
            double tempsParRep = temps / nombreDeRep;
            if(tempsParRep > 1.5) return 5;
            else if(tempsParRep > 1.2) return 4;
            else if(tempsParRep > 1) return 3;
            else if(tempsParRep > 0.75) return 2;
            else return 1;
        } else return 0;
    }

    protected int calculeScoreFin(float valeurFin){
        // calcul scoreFin
        if(this.nombreDeRep > 0) {
            if (valeurFin < 140) return 5;
            else if (valeurFin < 150) return 4;
            else if (valeurFin < 180) return 3;
            else if (valeurFin < 200) return 2;
            else return 1;
        } else return 0;
    }

    protected int calculeScoreDebut(float valeurDebut){
        if(this.nombreDeRep > 0) {
            if (valeurDebut < 175) return 5;
            else if (valeurDebut < 195) return 4;
            else if (valeurDebut < 215) return 3;
            else if (valeurDebut < 240) return 2;
            else return 1;
        } else return 0;
    }
}
