package com.example.fukuoka.helloworld;


import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1000;
    private Button buttonmain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonmain = (Button)findViewById(R.id.button3);
        buttonmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //locationActivity();

            }
        });

    }




    // Intent でLocation
    private void locationActivity() {
        Intent intent = new Intent(getApplication(), LocationActivity.class);
        startActivity(intent);
    }

}