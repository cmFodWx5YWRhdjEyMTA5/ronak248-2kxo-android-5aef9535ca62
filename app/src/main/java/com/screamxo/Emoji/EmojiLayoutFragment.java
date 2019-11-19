package com.screamxo.Emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shubham Agarwal on 19/01/17.
 */

@SuppressLint("ValidFragment")
public class EmojiLayoutFragment extends Fragment {

    Context context;
    EmojiTextInterface emojiTextInterface;

    public EmojiLayoutFragment(EmojiTextInterface emojiTextInterface) {
        this.emojiTextInterface = emojiTextInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.emoji_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @OnClick({R.id.emoji_angry, R.id.emoji_happy, R.id.emoji_sad, R.id.emoji_tongue, R.id.emoji_wink, R.id.emoji_blush, R.id.emoji_cry})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emoji_angry:
                setEmoji("[:angryface:]");
                break;
            case R.id.emoji_happy:
                setEmoji("[:bigsmile:]");
                break;
            case R.id.emoji_sad:
                setEmoji("[:dizzy:]");
                break;
            case R.id.emoji_tongue:
                setEmoji("[:tongue:]");
                break;
            case R.id.emoji_wink:
                setEmoji("[:wink:]");
                break;
            case R.id.emoji_blush:
                setEmoji("[:rosy-chicks:]");
                break;
            case R.id.emoji_cry:
                setEmoji("[:cry:]");
                break;
        }
    }


    void setEmoji(String emojiText) {
        emojiTextInterface.getEmojiText(emojiText);
    }
}
