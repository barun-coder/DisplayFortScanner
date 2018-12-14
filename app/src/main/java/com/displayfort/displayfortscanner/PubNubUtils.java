package com.displayfort.displayfortscanner;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on 14/12/2018 16:44.
 * DisplayFortScanner
 */
public class PubNubUtils {
    private static Pubnub pubNub;
    private static String CHANNEL_NAME = "DF";

    public static void subscribe() {
        pubNub = null;
        if (pubNub == null) {
            pubNub = new Pubnub("pub-c-dce848d6-488b-4fb2-95ff-13f12ca5317f","sub-c-f64970aa-ff6f-11e8-b528-9e80330262eb" );

            try {
                pubNub.subscribe(CHANNEL_NAME, new Callback() {
                    @Override
                    public void successCallback(String channel, Object message, String timetoken) {
                        super.successCallback(channel, message, timetoken);
                        Log.d("Pubnub successCallback", message.toString());
                    }

                    @Override
                    public void connectCallback(String channel, Object message) {
                        super.connectCallback(channel, message);
                        Log.d("Pubnub connectCallback", message.toString());
                    }

                    @Override
                    public void reconnectCallback(String channel, Object message) {
                        super.reconnectCallback(channel, message);
                        Log.d("Pubnub reconnectCallback", message.toString());
                    }

                    @Override
                    public void disconnectCallback(String channel, Object message) {
                        super.disconnectCallback(channel, message);
                        Log.d("Pubnub disconnectCallback", message.toString());
                    }
                });
            } catch (PubnubException e) {
                e.printStackTrace();
            }
        }
            }

    public static void publishMessage(String UniqueID) {
        if (pubNub != null) {
            JSONObject messageJsonObject = new JSONObject();
            try {
                messageJsonObject.put("uniqueID", UniqueID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pubNub.publish(CHANNEL_NAME, messageJsonObject, new Callback() {
                @Override
                public void successCallbackV2(String channel, Object message, JSONObject envelope) {
                    super.successCallbackV2(channel, message, envelope);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                }
            });
        }

    }
}
