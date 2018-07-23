package com.nathanielbennett.android.patience;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.os.Debug.waitForDebugger;
import static com.nathanielbennett.android.patience.CustomReceivers.*;

public class PatienceBroadcastService extends Service {

    private final static String TAG = "Nathaniel";

    private static BroadcastReceiver mMinuteTickReceiver;

    public PatienceBroadcastService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "OnCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Started");

        if(intent != null) {
            if(intent.hasExtra("SHUTDOWN")) {
                boolean shouldShutdown = intent.getBooleanExtra("SHUTDOWN", false);
                Log.d(TAG, "Being shutdown...");
                if(shouldShutdown){

                    if(mMinuteTickReceiver != null) {
                        unregisterReceiver(mMinuteTickReceiver);
                        mMinuteTickReceiver=null;
                    }

                    stopSelf();
                    return START_STICKY;
                }
            }
        }

        if(mMinuteTickReceiver == null) {
            registerOnTickReceiver();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Being Destroyed");
        super.onDestroy();
    }

    private class MinuteTickRelayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(this.getClass().getName(), "Receiving Tick");
            Intent timeTick = new Intent(ACTION_COUNTDOWN_TICK);
            sendBroadcast(timeTick);
        }
    }

    private void registerOnTickReceiver(){
        Log.d(TAG, "Registering Receiver");
        mMinuteTickReceiver = new MinuteTickRelayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mMinuteTickReceiver, filter);
    }
}
