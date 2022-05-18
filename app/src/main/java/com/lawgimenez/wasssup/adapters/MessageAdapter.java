package com.lawgimenez.wasssup.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawgimenez.wasssup.R;
import com.lawgimenez.wasssup.WasssupApplication;
import com.lawgimenez.wasssup.databinding.ItemReceivedBinding;
import com.lawgimenez.wasssup.databinding.ItemSentBinding;
import com.lawgimenez.wasssup.models.Message;

import java.util.ArrayList;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_SENT = 23;
    private static final int ITEM_TYPE_RECEIVED = 24;
    private ArrayList<Message> mListMessages;
    private String mUserId = "";

    public MessageAdapter(ArrayList<Message> listMessages) {
        mListMessages = listMessages;
        mUserId = WasssupApplication.getInstance().getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ITEM_TYPE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_received, parent, false);
            viewHolder = new MessageReceivedViewHolder(view);
        } else if (viewType == ITEM_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sent, parent, false);
            viewHolder = new MessageSentViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mListMessages.get(position);
        if (message.getUserId().equals(mUserId)) {
            return ITEM_TYPE_SENT;
        } else {
            return ITEM_TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mListMessages.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Message message = mListMessages.get(position);
        if (viewHolder instanceof MessageReceivedViewHolder) {
            MessageReceivedViewHolder messageReceivedViewHolder = (MessageReceivedViewHolder) viewHolder;
            messageReceivedViewHolder.mItemBinding.textviewMessageReceived.setText(message.getContent());
            messageReceivedViewHolder.mItemBinding.textviewUsername.setText(message.getUsername());
        } else if (viewHolder instanceof MessageSentViewHolder) {
            MessageSentViewHolder messageSentViewHolder = (MessageSentViewHolder) viewHolder;
            messageSentViewHolder.mItemBinding.textviewMessageSent.setText(message.getContent());
            messageSentViewHolder.mItemBinding.textviewUsername.setText(message.getUsername());
        }
    }

    private class MessageReceivedViewHolder extends RecyclerView.ViewHolder {

        ItemReceivedBinding mItemBinding;

        MessageReceivedViewHolder(View view) {
            super(view);
            mItemBinding = DataBindingUtil.bind(view);
        }
    }

    private class MessageSentViewHolder extends RecyclerView.ViewHolder {

        ItemSentBinding mItemBinding;

        MessageSentViewHolder(View view) {
            super(view);
            mItemBinding = DataBindingUtil.bind(view);
        }
    }
}
