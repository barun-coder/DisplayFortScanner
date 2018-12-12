package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.displayfort.displayfortscanner.R;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private RelativeLayout scanlRl, textRl, syncRl, reportRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        scanlRl = findViewById(R.id.scan_rl);
        textRl = findViewById(R.id.text_rl);
        syncRl = findViewById(R.id.sync_rl);
        reportRl = findViewById(R.id.report_rl);
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
                intent = new Intent(context, InOutScanActivity.class);
                startActivity(intent);
                break;
            case R.id.text_rl:
                intent = new Intent(context, InOutScanActivity.class);
                startActivity(intent);
                break;
            case R.id.sync_rl:
                intent = new Intent(context, InOutScanActivity.class);
                startActivity(intent);
                break;
            case R.id.report_rl:
                intent = new Intent(context, InOutScanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
