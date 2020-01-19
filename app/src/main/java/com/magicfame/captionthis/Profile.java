package com.magicfame.captionthis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    User u;
    static final int PICK_CALIBRATION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.profile);
        u = (User) getIntent().getSerializableExtra("User");
        updateDataOnLayout();
        addEventOnButton();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("User", u);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void updateDataOnLayout() {
        EditText username = findViewById(R.id.prenom);
        username.setText(u.getUsername());
        username.setEnabled(false);
        EditText age = findViewById(R.id.age);
        age.setText(String.valueOf(u.getAge()));
        age.setEnabled(false);
        EditText corps = findViewById(R.id.corps);
        corps.setText((u.getTailleRelle() != 0 ? String.valueOf(u.getTailleRelle()) : "Faites un calibrage"));
        corps.setEnabled(false);
        EditText bras = findViewById(R.id.bras);
        bras.setText((u.getTailleBras() != 0 ? String.valueOf(u.getTailleBras()) : "Faites un calibrage"));
        bras.setEnabled(false);
    }

    // Calibrage
    public void addEventOnButton() {
        Button b = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, CameraActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, PICK_CALIBRATION_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_CALIBRATION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int tailleBras = data.getIntExtra("tailleBrasReel", 0);
                int tailleCorps = data.getIntExtra("tailleCorpsReel",0);

                u.setCalibration(true);
                u.setTailleRelle(tailleCorps);
                u.setTailleBras(tailleBras);
                updateDataOnLayout();
            } if (resultCode == Activity.RESULT_CANCELED){

            }
        }
    }
}
