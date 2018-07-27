package com.liuerye.duidemo.observer;

import android.content.Context;
import android.location.Address;
import android.text.TextUtils;
import android.util.Log;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;
import com.csipsdk.sdk.Message;
import com.csipsdk.sdk.RXCallService;
import com.csipsdk.sdk.listener.RXCallSendMsgStatusListener;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 */
public class DuiCommandObserver implements CommandObserver {
    private String TAG = "DuiCommandObserver";

    private Context mContext;

    public DuiCommandObserver(Context context) {
        mContext = context;
    }


    @Override
    public void onCall(String topic, String data) {
        AILog.i(TAG, "topic: " + topic + ", data: " + data);
        Log.e("@@@", topic);
        switch (topic) {
            case "cmd.demo.start_asrengine":
                startAsrEngine();
                break;
            case "address":
                processDate(data);
                break;
        }
    }

    private void startAsrEngine() {
        try {
            DDS.getInstance().getAgent().getASREngine().startListening(new ASREngine.Callback() {
                @Override
                public void beginningOfSpeech() {

                }

                @Override
                public void endOfSpeech() {

                }

                @Override
                public void bufferReceived(byte[] bytes) {

                }

                @Override
                public void partialResults(String s) {
                    AILog.i(TAG, "partialResults:%! " + s);
                }

                @Override
                public void finalResults(String s) {
                    AILog.i(TAG, "finalResults:%! " + s);
                    try {
                        DDS.getInstance().getAgent().getASREngine().stopListening();
                    } catch (DDSNotInitCompleteException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(String s) {

                }

                @Override
                public void rmsChanged(float v) {

                }
            });
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void processDate(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            String building = jsonObject.optString("building");
            String unit = jsonObject.optString("unit");
            String floor = jsonObject.optString("floor");
            String house = jsonObject.optString("house");
            StringBuffer buffer = new StringBuffer();
            buffer.append("有访客要去").append(building).append(unit).append(floor).append(house).append("，是否给他开门么？");
            RXCallService.with().sendMessage(buffer.toString(), "1006", new RXCallSendMsgStatusListener() {
                @Override
                public void onSuccessful(String s, String s1) {
                    Log.e("@@@", "send msg success");
                }

                @Override
                public void onFailed(String s, String s1, Message message) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
