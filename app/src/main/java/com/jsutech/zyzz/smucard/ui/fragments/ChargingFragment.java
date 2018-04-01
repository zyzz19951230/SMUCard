package com.jsutech.zyzz.smucard.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jsutech.zyzz.smucard.BaseSMUActivity;
import com.jsutech.zyzz.smucard.R;
import com.jsutech.zyzz.smucard.network.SMUClient;

/**
 * Created by zyzz on 3/31/18.
 *
 */

public class ChargingFragment extends BaseFragment {
    private static final String TAG = "ChargingFragment";
    private SMUClient client;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.charging_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        client = ((BaseSMUActivity)getContext()).getClient();
        ImageButton btn = getActivity().findViewById(R.id.refresh_bal_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onMessageReceived(int msgID, Object data, ICommunicator sender) {

    }
}
