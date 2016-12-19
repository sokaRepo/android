package com.example.soka.loginme;

/**
 * Created by soka on 18/12/16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class APIReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public APIReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
