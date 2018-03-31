package com.jsutech.zyzz.smucard.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;

/**
 * Created by zyzz on 3/31/18.
 *
 */

abstract public class BaseFragment extends Fragment{

    abstract public void onMessageReceived(int msgId, Object data);
    public boolean filterMessage(int msgID){
        return true;
    }
}
