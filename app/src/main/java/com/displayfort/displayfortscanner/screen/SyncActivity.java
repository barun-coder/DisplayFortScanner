package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.displayfort.displayfortscanner.R;
import com.displayfort.displayfortscanner.model.ProfileDao;

import java.util.HashMap;

public class SyncActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private TextView record_text_tv;
    private Button get_sync;
    public static HashMap<String, ProfileDao> daoHashMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_layout_activity);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        record_text_tv = findViewById(R.id.record_text_tv);
        get_sync = findViewById(R.id.get_sync);
        get_sync.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.get_sync:
                getInsertRecord();
                break;

        }
    }

    private void getInsertRecord() {
        daoHashMap = new HashMap<>();
        record_text_tv.setText("");
        for (int i = 0; i < 500; i++) {
            String toPad = "" + i;
            String padded = String.format("%8s", toPad).replace(' ', '0');
            ProfileDao profileDao = new ProfileDao(i, padded);
            daoHashMap.put(padded, profileDao);
            record_text_tv.append(profileDao.toString() + "\n");
            record_text_tv.setMovementMethod(new ScrollingMovementMethod());
        }
        record_text_tv.append("Finish");
    }
}
