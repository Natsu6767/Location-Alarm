package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class SetAlarmActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", "" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                double lati = data.getDoubleExtra("latitude", 0);
                double lang = data.getDoubleExtra("longitude", 0);
                Toast.makeText(this, "" + lati, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
