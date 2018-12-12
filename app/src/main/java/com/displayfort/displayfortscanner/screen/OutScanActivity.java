package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.displayfort.displayfortscanner.R;
import com.displayfort.displayfortscanner.model.ProfileDao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OutScanActivity extends AppCompatActivity {
    private static final String TAG = OutScanActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Context context = this;
    private TextView record_text_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (barcodeView != null) {
                    barcodeView.resume();
                }
            }
        });
    }

    private void init() {
        record_text_tv = findViewById(R.id.record_text_tv);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("Place a QRCode inside the viewfinder rectangle to scan it.");
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
//                beepManager.playBeepSoundAndVibrate();
//                beepManager = new BeepManager(InOutScanActivity.this);
//                showAlery(result.getText().toString());

                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkUniqueId(result.getText().toString());

            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        beepManager = new BeepManager(this);
    }


    private void checkUniqueId(String uniqueId) {
        barcodeView.pause();
        if (uniqueId != null && uniqueId.length() >= 8 && SyncActivity.daoHashMap != null) {
            ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
            if (profileDao != null) {
                setMultiSizeText("Welcome\n" + profileDao.Name);
            } else {
                setMultiSizeText("Welcome\n" + "Guest ");
            }
        } else {
            setMultiSizeText("Welcome\n" + "Guest ");
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                barcodeView.resume();
            }
        }, 1000);
        YoYo.with(Techniques.ZoomInUp)
                .duration(700)
                .playOn(record_text_tv);
    }

    private void setMultiSizeText(String s) {
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 7, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.HEAD_TEXT)), 0, 7, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(3f), 8, s.length(), 0); // set size
        record_text_tv.setText(ss1);
    }

    private void showAlery(String result) {
        barcodeView.pause();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Scan Result");
        builder1.setIcon(R.mipmap.ic_launcher);
        builder1.setMessage(result);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        barcodeView.resume();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }
}
