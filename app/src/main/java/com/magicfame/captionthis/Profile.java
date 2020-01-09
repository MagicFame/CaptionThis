package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    User u = new User();
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
        Intent myIntent = new Intent(this, HomeActivity.class);
        myIntent.putExtra("User", u);
        startActivityForResult(myIntent, 0);
    }

    public void updateDataOnLayout() {
        ((TextView) findViewById(R.id.prenom)).setText(u.getUsername());
        //((TextView) findViewById(R.id.age)).setText(u.getAge());
    }

    public void addEventOnButton() {
        Button b = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, CameraActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                finish();
            }
        });
    }
}
