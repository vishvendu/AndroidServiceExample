package com.example.vishvendu.androidserviceexample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton, stopButton;
    TextView textView;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("current thread in activity"+Thread.currentThread().getId());

        startButton = (Button)findViewById(R.id.button);
        stopButton = (Button)findViewById(R.id.button2);
        textView = (TextView)findViewById(R.id.updatedposition);


        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        runtimePermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver !=null){

            unregisterReceiver(broadcastReceiver);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(broadcastReceiver == null){

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    System.out.println("onreceived called");

                    textView.append("\n" + intent.getExtras().get("coordinate"));

                }


            };

            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }




    }

    private boolean runtimePermission() {




            if(Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){


                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);

                return true;
            }

return false;



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 ){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_LONG).show();
            }
            else{

                runtimePermission();
            }

        }

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.button :

                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);

            case R.id.button2 :

                Intent i2 = new Intent(MainActivity.this, MyService.class);
                stopService(i2);

        }

    }
}
