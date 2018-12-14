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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.displayfort.displayfortscanner.PubNubUtils;
import com.displayfort.displayfortscanner.R;
import com.displayfort.displayfortscanner.Utils.DFScanPrefrences;
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
import java.util.Set;

public class InOutScanActivity extends AppCompatActivity {
    private static final String TAG = InOutScanActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Context context = this;
    private TextView record_text_tv;
    private boolean isScanIN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isScanIN = getIntent().getBooleanExtra("SCAN", true);
        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String uniqueId = result.getText().toString();
                if (uniqueId != null && uniqueId.length() >= 8) {
                    if (isScanIN) {
                        checkUniqueIdForIn(uniqueId);
                        PubNubUtils.publishMessage(uniqueId);
                    } else {
                        checkUniqueIdForOut(uniqueId);
                    }
                } else {
                    Toast.makeText(InOutScanActivity.this, "Not a valid ID", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
        barcodeView.resume();
        beepManager = new BeepManager(this);
    }

    private void checkUniqueIdForOut(String uniqueId) {
        barcodeView.pause();
        Set<String> OutSet = new DFScanPrefrences(context).getUniqueIdSetOut();
        if (OutSet.contains(uniqueId)) {
            if (SyncActivity.daoHashMap != null && SyncActivity.daoHashMap.get(uniqueId) != null) {
                ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
                setMultiSizeTextOut("Already! " + profileDao.Name + " Leaved");
            } else {
                setMultiSizeTextOut("Already! " + "Guest " + "Leaved");
            }
        } else {
            Set<String> inSet = new DFScanPrefrences(context).getUniqueIdSetIn();
            if (inSet.contains(uniqueId)) {
                if (SyncActivity.daoHashMap != null && SyncActivity.daoHashMap.get(uniqueId) != null) {
                    ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
                    setMultiSizeTextOut("ThankYou! " + profileDao.Name + " Visit-Again");
                } else {
                    setMultiSizeTextOut("ThankYou! " + "" + "Visit-Again");
                }
                new DFScanPrefrences(context).setUniqueIdSetOut(uniqueId);
            } else {
                if (SyncActivity.daoHashMap != null && SyncActivity.daoHashMap.get(uniqueId) != null) {
                    ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
                    setMultiSizeTextOut("No InProxy " + profileDao.Name);
                } else {
                    setMultiSizeTextOut("Wrong " + "Guest");
                }
            }
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                barcodeView.resume();
            }
        }, 1000);
    }

    private void setMultiSizeTextOut(String s) {
        record_text_tv.setText("");
        SpannableString ss1 = null;
        String[] splitTxt = s.split(" ");
        for (int i = 0; i < splitTxt.length; i++) {
            switch (i) {
                case 0:
                    ss1 = new SpannableString(splitTxt[i]);
                    ss1.setSpan(new RelativeSizeSpan(2f), 0, splitTxt[i].length(), 0); // set size
                    break;
                case 1:
                    ss1 = new SpannableString(splitTxt[i]);
                    ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.HEAD_TEXT)), 0, splitTxt[i].length(), 0); // set size
                    ss1.setSpan(new RelativeSizeSpan(3f), 0, splitTxt[i].length(), 0); // set size
                    break;
                case 2:
                    ss1 = new SpannableString(splitTxt[i]);
                    ss1.setSpan(new RelativeSizeSpan(2f), 0, splitTxt[i].length(), 0); // set size
                    break;
                default:
                    ss1 = new SpannableString(splitTxt[i]);
                    ss1.setSpan(new RelativeSizeSpan(1f), 0, splitTxt[i].length(), 0); // set size
                    break;
            }
            Log.d("TAG", splitTxt[i] + " -" + i);
            record_text_tv.append(ss1.toString().replace("-", " ") + "\n");

        }

        YoYo.with(Techniques.ZoomInUp)
                .duration(700)
                .playOn(record_text_tv);
    }


    private void checkUniqueIdForIn(String uniqueId) {
        barcodeView.pause();
        Set<String> inSet = new DFScanPrefrences(context).getUniqueIdSetIn();
        if (inSet.contains(uniqueId)) {
            if (SyncActivity.daoHashMap != null && SyncActivity.daoHashMap.get(uniqueId) != null) {
                ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
                setMultiSizeTextOut("Already. " + profileDao.Name + " Inside");
            } else {
                setMultiSizeTextOut("Already. " + "Guest" + " Inside");
            }
        } else {
            if (SyncActivity.daoHashMap != null && SyncActivity.daoHashMap.get(uniqueId) != null) {
                ProfileDao profileDao = SyncActivity.daoHashMap.get(uniqueId);
                setMultiSizeTextOut("Welcome! " + profileDao.Name);
            } else {
                setMultiSizeTextOut("Welcome!" + " Guest ");
            }
            new DFScanPrefrences(context).setUniqueIdSetIn(uniqueId);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                barcodeView.resume();
            }
        }, 1000);
    }

    private void setMultiSizeText(String s) {
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 8, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.HEAD_TEXT)), 0, 7, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(3f), 9, s.length(), 0); // set size
        record_text_tv.setText(ss1);
        YoYo.with(Techniques.ZoomInUp)
                .duration(700)
                .playOn(record_text_tv);
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
