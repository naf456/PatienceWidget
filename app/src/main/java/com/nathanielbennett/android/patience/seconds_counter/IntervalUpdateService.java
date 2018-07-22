package com.nathanielbennett.android.patience.seconds_counter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class IntervalUpdateService extends Service{

    private static final String PACKAGE_NAME = IntervalUpdateService.class.getPackage().getName();
    public static final String ACTION_INTERVAL_UPDATE = PACKAGE_NAME + ".INTERVAL_UPDATE";

    private static final int MINIMUM_INTERVAL = 400;

    private Handler mHandler;
    private WidgetUpdateBroadcasterRunnable widgetUpdateBroadcasterRunnable;
    
    private int mBroadcastInterval = 1000;

    public void setBroadcastInterval(int interval){
        mBroadcastInterval = mBroadcastInterval < MINIMUM_INTERVAL ? MINIMUM_INTERVAL : interval;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        widgetUpdateBroadcasterRunnable = new WidgetUpdateBroadcasterRunnable(
                getApplicationContext(), mHandler, mBroadcastInterval);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.post(widgetUpdateBroadcasterRunnable);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        widgetUpdateBroadcasterRunnable.stopBroadcasting();
        super.onDestroy();
    }

    class WidgetUpdateBroadcasterRunnable implements Runnable {

        private Context context;
        private Handler handler;
        private int broadcastInterval;

        WidgetUpdateBroadcasterRunnable(@NotNull Context context, @Nullable Handler handler, int broadcastInterval){
            this.context = context;
            this.handler = handler != null? handler: new Handler();
            this.broadcastInterval = broadcastInterval;
        }

        @Override
        public void run() {
            context.sendBroadcast(new Intent().setAction(ACTION_INTERVAL_UPDATE));
            resumeBroadcasting();
        }

        void stopBroadcasting(){
            handler.removeCallbacks(this);
        }

        void resumeBroadcasting() {handler.postDelayed(this, broadcastInterval);}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
