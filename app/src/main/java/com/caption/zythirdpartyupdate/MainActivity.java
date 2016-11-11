package com.caption.zythirdpartyupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.caption.update.UpdateUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //自动更新
        UpdateUtils.getInit().update(this,UpdateUtils.CHECK_AUTO);
    }
}
