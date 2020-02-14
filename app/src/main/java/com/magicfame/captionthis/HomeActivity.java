package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Menu activity
public class HomeActivity extends AppCompatActivity {
    static final int EDIT_PROFIL = 1;
    static final int EDIT_SETTING = 2;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        u = (User) getIntent().getSerializableExtra("User");
        setContentView(R.layout.menu);

        addActionButton();
    }

    public void addActionButton() {
        // Just an exercice option
        findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ListExercice.class);
                intent.putExtra("User", u);
                startActivity(intent);
            }
        });

        // Profile option
        findViewById(R.id.imageView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Profile.class);
                intent.putExtra("User", u);
                startActivityForResult(intent, EDIT_PROFIL);
            }
        });

        // Setting option
        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Settings.class);
                intent.putExtra("User", u);
                startActivityForResult(intent, EDIT_SETTING);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_PROFIL) {
            if(resultCode == HomeActivity.RESULT_OK) {
                User user = (User) data.getSerializableExtra("User");
                u.modifyObject(user);
            }
        } else if (requestCode == EDIT_SETTING){
            if(resultCode == HomeActivity.RESULT_OK){
                User user = (User) data.getSerializableExtra("User");
                u.modifyObject(user);
            }
        }
    }
}
