package com.liuerye.duidemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.csipsdk.sdk.CallSession;
import com.csipsdk.sdk.Message;
import com.csipsdk.sdk.RXCallService;
import com.csipsdk.sdk.listener.RXCallServiceListener;
import com.csipsdk.utils.Log;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.androidannotations.annotations.App;

public class DuiApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(this);
        initRX();
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "noversion";
        }
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("Unknown Error");
        }
        return mContext;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private void initRX() {
        RXCallService.with().initRXCall(this, true, new RXCallServiceListener() {
            @Override
            public void onConnectFailed(Message msg) {
                Log.e("@@@", "onConnectFailed " + msg.getMsg() + " code " + msg.getCode());
            }

            @Override
            public void onConnectSuccess() {
                Log.e("@@@", "onConnectSuccess");
            }

            @Override
            public void onConnecting() {
                Log.e("@@@", "onConnecting");
            }

            @Override
            public void onIncomingCall(CallSession callSession) {
            }

            @Override
            public void onHangUp(CallSession callSession) {
            }

            @Override
            public void onDialFailed(Message msg) {
                Toast.makeText(DuiApplication.this, msg.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnswer(CallSession callSession) {
            }

            @Override
            public void onAlerting(CallSession callSession) {
                Log.e("App", "onAlerting");
            }
        });
    }

}
