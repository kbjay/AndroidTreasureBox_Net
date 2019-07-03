package com.kbjay.android.treasurebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Method testMethod = MainActivity.class.getMethod("testMethod", null);
            testMethod.invoke(this, null);
        } catch (Exception e) {

        }
    }

    public void testMethod() {
        Toast.makeText(this, "111", Toast.LENGTH_SHORT).show();
    }
}
