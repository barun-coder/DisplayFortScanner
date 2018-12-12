package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.displayfort.displayfortscanner.R;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private LinearLayout scanlRl, textRl, syncRl, reportRl, outScanRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        scanlRl = findViewById(R.id.scan_rl);
        textRl = findViewById(R.id.text_rl);
        syncRl = findViewById(R.id.sync_rl);
        reportRl = findViewById(R.id.report_rl);
        outScanRl = findViewById(R.id.out_scan_rl);
        outScanRl.setOnClickListener(this);
        scanlRl.setOnClickListener(this);
        textRl.setOnClickListener(this);
        syncRl.setOnClickListener(this);
        reportRl.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.scan_rl:
                isServerDataAvail();
                intent = new Intent(context, InOutScanActivity.class);
                intent.putExtra("SCAN",true);
                startActivity(intent);
                break;
            case R.id.out_scan_rl:
                isServerDataAvail();
                intent = new Intent(context, InOutScanActivity.class);
                intent.putExtra("SCAN",false);
                startActivity(intent);
                break;
            case R.id.text_rl:
                intent = new Intent(context, TextFielActivity.class);
                startActivity(intent);
                break;
            case R.id.sync_rl:
                intent = new Intent(context, SyncActivity.class);
                startActivity(intent);
                break;
            case R.id.report_rl:
                intent = new Intent(context, ReportActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void isServerDataAvail() {
        if (SyncActivity.daoHashMap == null) {
            Toast.makeText(this, "Enter 8 digit Number", Toast.LENGTH_SHORT).show();
        }
    }
}
