package com.magicfame.captionthis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.signin);

        // On click event on button
        Button signInButton = findViewById(R.id.button2);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)findViewById(R.id.editText2)).getText().toString();
                String password = ((EditText)findViewById(R.id.editText3)).getText().toString();

                // CALL BDD

                // To replace
                if(username.compareTo(password) == 0){
                    Intent intent = new Intent(SignIn.this, HomeActivity.class);

                    // To replace
                    intent.putExtra("User", new User(username, 22, false));

                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
