package com.jsutech.zyzz.smucard.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;

/**
 * Created by zyzz on 3/31/18.
 *
 */

abstract public class BaseFragment extends Fragment implements ICommunicator {

    @Override
    public void sendMessage(ICommunicator receiver, int msgID, Object data) {
        receiver.onMessageReceived(msgID, data, this);
    }

    public boolean filterMessage(int msgID){
        return true;
    }
}
