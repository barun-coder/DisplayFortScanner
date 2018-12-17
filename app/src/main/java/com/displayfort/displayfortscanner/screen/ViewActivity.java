package com.displayfort.displayfortscanner.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.displayfort.displayfortscanner.R;
import com.displayfort.displayfortscanner.Utils.Config;
import com.displayfort.displayfortscanner.youtube.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class ViewActivity extends YouTubeFailureRecoveryActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener {
    private Context context = this;
    private WebView webView;
    private TextView textView;
    private String URL;
    private YouTubePlayerView youtube_view;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RelativeLayout image_Rl;
    public ImageView thumbnail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        URL = getIntent().getStringExtra("URL");
        String type = getIntent().getStringExtra("TYPE");
        init();
        setViewAccordingToType(type);
    }

    private void init() {
        webView = findViewById(R.id.webview);
        textView = findViewById(R.id.text_view);
        youtube_view = findViewById(R.id.youtube_view);
        image_Rl = findViewById(R.id.image_Rl);
        thumbnail = findViewById(R.id.thumbnail);
        progressBar = findViewById(R.id.progress);
    }

    private void setViewAccordingToType(String type) {
        webView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        youtube_view.setVisibility(View.GONE);
        image_Rl.setVisibility(View.GONE);
        switch (type) {
            case "T": {
                textView.setVisibility(View.VISIBLE);
                textView.setText(URL);
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(textView);
            }
            break;
            case "L": {
                webView.setVisibility(View.VISIBLE);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(URL);
            }
            break;
            case "I": {
                image_Rl.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(thumbnail);
            }
            break;
            case "V": {
                youtube_view.setVisibility(View.VISIBLE);
//                Initializing video player with developer key
                Config.YOUTUBE_VIDEO_CODE = URL;
                youtube_view.initialize(Config.DEVELOPER_KEY, this);
            }
            break;
        }


    }


    @Override
    public void onClick(View view) {
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
            player.setFullscreen(true);
            player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {
                    Log.d("Yuotube", "onPlaying");
                }

                @Override
                public void onPaused() {
                    Log.d("Yuotube", "onPaused");
                }

                @Override
                public void onStopped() {
                    Log.d("Yuotube", "onStopped");
                }

                @Override
                public void onBuffering(boolean b) {
                    Log.d("Yuotube", "onBuffering");
                }

                @Override
                public void onSeekTo(int i) {
                    Log.d("Yuotube", "onSeekTo");
                }
            });
            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    Log.d("Yuotube", "onLoading");
                }

                @Override
                public void onLoaded(String s) {
                    Log.d("Yuotube", "onLoaded");
                }

                @Override
                public void onAdStarted() {
                    Log.d("Yuotube", "onAdStarted");
                }

                @Override
                public void onVideoStarted() {
                    Log.d("Yuotube", "onVideoStarted");
                }

                @Override
                public void onVideoEnded() {
                    Log.d("Yuotube", "onVideoEnded");
                    finish();
                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.d("Yuotube", "onError");
                }
            });
            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//        YouTubePlayer.PlayerStyle.CHROMELESS 	A style that shows no interactive player controls.
//        YouTubePlayer.PlayerStyle.DEFAULT 	The default style, showing all interactive controls.
//        YouTubePlayer.PlayerStyle.MINIMAL 	A minimal style, showing only a time bar and play/pause controls.
        }
    }

}
