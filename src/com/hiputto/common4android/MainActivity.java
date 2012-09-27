package com.hiputto.common4android;


import com.hiputto.common4android.superclass.HP_BaseActivity;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends HP_BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
