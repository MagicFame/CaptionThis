package com.magicfame.captionthis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        u = (User) getIntent().getSerializableExtra("User");
        setContentView(R.layout.settings);
        updateDataOnLayout();
        addEvent();
    }

    protected  void updateDataOnLayout(){
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        if(u.getNiveau() != 0){
            if(u.getNiveau() == 1){
                radioGroup.check(R.id.radioButton3);
            } else if(u.getNiveau() == 2){
                radioGroup.check(R.id.radioButton4);
            } else {
                radioGroup.check(R.id.radioButton5);
            }
        }

        ((Switch)findViewById(R.id.switch1)).setChecked(u.isNotifications());
        ((Switch)findViewById(R.id.switch2)).setChecked(u.isDonnes());

        ((SeekBar)findViewById(R.id.seekBar)).setProgress(u.getPoids());
        ((TextView)findViewById(R.id.textView19)).setText(String.valueOf(u.getPoids()));


    }

    protected void addEvent() {
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("User", u);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView)findViewById(R.id.textView19)).setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void save(){
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        if(radioGroup.getCheckedRadioButtonId() != -1){
            switch(radioGroup.getCheckedRadioButtonId()){
                case R.id.radioButton3:
                    u.setNiveau(1);
                    break;
                case R.id.radioButton4:
                    u.setNiveau(2);
                    break;
                case R.id.radioButton5:
                    u.setNiveau(3);
                    break;
            }
        }

        u.setNotifications(((Switch)findViewById(R.id.switch1)).isChecked());
        u.setDonnes(((Switch)findViewById(R.id.switch2)).isChecked());

        u.setPoids(((SeekBar)findViewById(R.id.seekBar)).getProgress());
    }
}
