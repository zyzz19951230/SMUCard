package com.jsutech.zyzz.smucard.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;

/**
 * Created by zyzz on 3/31/18.
 *
 */

abstract public class BaseSMUFragment extends Fragment implements ISMUClientReceiver{
    protected SMUHandler currentHandler;
    @Override
    public void onStart() {
        super.onStart();
        currentHandler = new SMUHandler(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getSMUClient().setSMUHandler(null);
    }

    @Override
    abstract public void onClientMessageReceived(int msgId, Object data);


    @Override
    public SMUClient getSMUClient() {
        Activity activity = getActivity();
        if (activity instanceof ISMUClientReceiver){
            return ((ISMUClientReceiver)activity).getSMUClient();
        }
        return null;
    }

    @Override
    public void switchContext() {
        SMUClient client = getSMUClient();
        if (client != null){
            client.setSMUHandler(currentHandler);
        }
    }
}
