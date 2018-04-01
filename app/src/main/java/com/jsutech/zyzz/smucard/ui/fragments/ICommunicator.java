package com.jsutech.zyzz.smucard.ui.fragments;

public interface ICommunicator {
    void onMessageReceived(int msgID, Object data, ICommunicator sender);
    void sendMessage(ICommunicator receiver, int msgID, Object data);
}
