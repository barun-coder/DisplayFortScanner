package com.displayfort.displayfortscanner.screen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.displayfort.displayfortscanner.PubNubUtils;
import com.displayfort.displayfortscanner.R;
import com.displayfort.displayfortscanner.Utils.DFScanPrefrences;
import com.displayfort.displayfortscanner.model.ProfileDao;
import com.goodiebag.pinview.Pinview;

import java.util.Set;

public class TextFielActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private EditText unique_id_et;
    private TextView record_text_tv;
    private Button In_btn, out_btn;
    private Pinview pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.text_field_layout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        pin = (Pinview) findViewById(R.id.pinview);
        unique_id_et = findViewById(R.id.unique_id_et);
        record_text_tv = findViewById(R.id.record_text_tv);
        In_btn = findViewById(R.id.enter_btn);
        In_btn.setOnClickListener(this);
        out_btn = findViewById(R.id.enter_out_btn);
        out_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_btn: {
                hideKeyboard(this);
                StartProcessInOut(true);
                pin.setValue("0");
            }
            break;
            case R.id.enter_out_btn: {
                hideKeyboard(this);
                StartProcessInOut(false);
                pin.setValue("0");
            }
            break;

        }
    }

    private void StartProcessInOut(boolean isScanIN) {
        unique_id_et.setText(pin.getValue());
        String uniqueId = unique_id_et.getText().toString();
        if (uniqueId != null && uniqueId.length() >= 8) {
            if (isScanIN) {
                checkUniqueIdForIn(uniqueId);
            } else {
                checkUniqueIdForOut(uniqueId);
            }
            PubNubUtils.publishMessage(uniqueId);
        } else {
            Toast.makeText(TextFielActivity.this, "Not a valid ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUniqueIdForOut(String uniqueId) {
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
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
