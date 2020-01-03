package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        findViewById(R.id.button_exercice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
