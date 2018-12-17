package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.displayfort.displayfortscanner.PubNubUtils;
import com.displayfort.displayfortscanner.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PubnubScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private LinearLayout scanlRl, textRl, syncRl, outScanRl;
    private ImageView image, text, video, link, caution;
    private Handler mHandler;
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pubnub_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        init();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(context, message.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void init() {
        scanlRl = findViewById(R.id.scan_rl);
        textRl = findViewById(R.id.text_rl);
        syncRl = findViewById(R.id.sync_rl);
        outScanRl = findViewById(R.id.out_scan_rl);
        outScanRl.setOnClickListener(this);
        scanlRl.setOnClickListener(this);
        textRl.setOnClickListener(this);
        syncRl.setOnClickListener(this);


        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        video = findViewById(R.id.video);
        link = findViewById(R.id.link);
        caution = findViewById(R.id.caution);
        caution.setOnClickListener(this);
        video.setOnClickListener(this);
        link.setOnClickListener(this);
        text.setOnClickListener(this);
        image.setOnClickListener(this);

    }

    void workerThread(String Msg) {
        // And this is how you call it from the worker thread:
        Message message = mHandler.obtainMessage(1, Msg);
        message.sendToTarget();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.caution:
                PubNubUtils.unSubscribeAll();
                break;
            case R.id.image:
                PubNubUtils.publishMessage(PubNubUtils.CHANNEL_NAME_ONE, "https://jaguar.ssl.cdn.sdlmedia.com/636353687510047376OV.jpg?v=24", "I");
                break;
            case R.id.video:
                PubNubUtils.publishMessage(PubNubUtils.CHANNEL_NAME_TWO, "9Yam5B_iasY", "V");
                break;
            case R.id.text:
                PubNubUtils.publishMessage(PubNubUtils.CHANNEL_NAME_THREE, "JAQUAR", "T");
                break;
            case R.id.link:
                PubNubUtils.publishMessage(PubNubUtils.CHANNEL_NAME_FOUR, "https://www.jaquar.com/products", "L");
                break;
            case R.id.scan_rl:
                PubNubUtils.subscribe(PubNubUtils.CHANNEL_NAME_ONE, new Callback() {
                    @Override
                    public void successCallback(String channel, final Object message) {
                        super.successCallback(channel, message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workONMessage(message);
                            }
                        });
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        workerThread(error.toString());
                    }

                    @Override
                    public void connectCallback(String channel, Object message) {
                        super.connectCallback(channel, message);
                        workerThread(message.toString());
                    }

                    @Override
                    public void reconnectCallback(String channel, Object message) {
                        super.reconnectCallback(channel, message);
                        workerThread(message.toString());
                    }
                });
                Toast.makeText(context, "Connect to Screen 1", Toast.LENGTH_LONG).show();
                break;
            case R.id.out_scan_rl:
                PubNubUtils.subscribe(PubNubUtils.CHANNEL_NAME_TWO, new Callback() {
                    @Override
                    public void successCallback(String channel, final Object message) {
                        super.successCallback(channel, message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workONMessage(message);
                            }
                        });
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        workerThread(error.toString());
                    }

                    @Override
                    public void connectCallback(String channel, Object message) {
                        super.connectCallback(channel, message);
                        workerThread(message.toString());
                    }

                    @Override
                    public void reconnectCallback(String channel, Object message) {
                        super.reconnectCallback(channel, message);
                        workerThread(message.toString());
                    }
                });
                Toast.makeText(context, "Connect to Screen 2", Toast.LENGTH_LONG).show();
                break;
            case R.id.text_rl:
                PubNubUtils.subscribe(PubNubUtils.CHANNEL_NAME_THREE, new Callback() {
                    @Override
                    public void successCallback(String channel, final Object message) {
                        super.successCallback(channel, message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workONMessage(message);
                            }
                        });
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        workerThread(error.toString());
                    }

                    @Override
                    public void connectCallback(String channel, Object message) {
                        super.connectCallback(channel, message);
                        workerThread(message.toString());
                    }

                    @Override
                    public void reconnectCallback(String channel, Object message) {
                        super.reconnectCallback(channel, message);
                        workerThread(message.toString());
                    }
                });
                Toast.makeText(context, "Connect to Screen 3", Toast.LENGTH_LONG).show();
                break;
            case R.id.sync_rl:
                PubNubUtils.subscribe(PubNubUtils.CHANNEL_NAME_FOUR, new Callback() {
                    @Override
                    public void successCallback(String channel, final Object message) {
                        super.successCallback(channel, message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workONMessage(message);
                            }
                        });
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        workerThread(error.toString());
                    }

                    @Override
                    public void connectCallback(String channel, Object message) {
                        super.connectCallback(channel, message);
                        workerThread(message.toString());
                    }

                    @Override
                    public void reconnectCallback(String channel, Object message) {
                        super.reconnectCallback(channel, message);
                        workerThread(message.toString());
                    }
                });
                Toast.makeText(context, "Connect to Screen 4", Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void workONMessage(Object message) {
        Intent intent = null;
        try {
            JSONObject jsonObject = new JSONObject(message.toString());
            String Type = jsonObject.optString("Type");
            switch (Type) {
                case "T": {
                    intent = new Intent(context, ViewActivity.class);
                    String url = jsonObject.optString("Message");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("URL", url);
                    intent.putExtra("TYPE", Type);
                    startActivity(intent);

                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                }
                break;
                case "L": {
                    intent = new Intent(context, ViewActivity.class);
                    String url = jsonObject.optString("Message");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("URL", url);
                    intent.putExtra("TYPE", Type);
                    startActivity(intent);

                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                }
                break;
                case "I": {
                    intent = new Intent(context, ViewActivity.class);
                    String url = jsonObject.optString("Message");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("URL", url);
                    intent.putExtra("TYPE", Type);
                    startActivity(intent);

                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                }
                break;
                case "V": {
                    intent = new Intent(context, ViewActivity.class);
                    String url = jsonObject.optString("Message");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("URL", url);
                    intent.putExtra("TYPE", Type);
                    startActivity(intent);

                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                }


//                    String url = jsonObject.optString("Message");
//                    intent = YouTubeStandalonePlayer.createVideoIntent(
//                            this, Config.DEVELOPER_KEY, url, 0, true, false);
//                    if (intent != null) {
//                        if (canResolveIntent(intent)) {
//                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//                        } else {
//                            // Could not resolve the intent - must need to install or update the YouTube API service.
//                            YouTubeInitializationResult.SERVICE_MISSING
//                                    .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
//                        }
//                    }
//                }
                break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}
