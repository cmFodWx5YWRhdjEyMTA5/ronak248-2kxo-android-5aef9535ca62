package com.screamxo.Emoji;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.Friend;
import com.example.apimodule.ApiBase.ApiBean.FriendBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Emoji.tagging.ClickableForegroundColorSpan;
import com.screamxo.Emoji.tagging.ExampleCardPopup;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Emoji.tagging.RelativePopupWindow;
import com.screamxo.Emoji.tagging.TaggingAdapter;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class EmojixTextWatcher implements TextWatcher, ClickableForegroundColorSpan.OnHashTagClickListener {
    private static final String TAG = "EmojixTextWatcher";
    private TextView textView;
    private int start = -1;
    private int count = -1;
    private int mHashTagWordColor;
    private ExampleCardPopup exampleCardPopup;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private Call<FriendBean> friendBeanCall;
    private OnHashTagClickListener mOnHashTagClickListener;
    private int lastNextNotLetterDigitCharIndex = 0;

    public EmojixTextWatcher(TextView textView, OnHashTagClickListener listener) {
        this.textView = textView;
        mService = new FetchrServiceBase();
        mOnHashTagClickListener = listener;
        exampleCardPopup = new ExampleCardPopup(textView.getContext());
        preferences = new Preferences(textView.getContext());

        mHashTagWordColor = ContextCompat.getColor(textView.getContext(), R.color.colorPink);

        SpannableString s = new SpannableString(textView.getText());
        if (!TextUtils.isEmpty(s)) {
            // in order to use spannable we have to set buffer type
            this.textView.setText(this.textView.getText(), TextView.BufferType.SPANNABLE);
            if (mOnHashTagClickListener != null) {
                // we need to set this in order to get onClick event
                this.textView.setMovementMethod(LinkMovementMethod.getInstance());
                // after onClick clicked text become highlighted
                this.textView.setHighlightColor(Color.TRANSPARENT);
            }
            setColorsToAllHashTags(this.textView.getText());

            float textSize = textView.getTextSize();

            EmojiconHandler.addEmojis(textView.getContext(), s, (int) textSize, DynamicDrawableSpan.ALIGN_BASELINE, (int) textSize, 0, -1);
            textView.setText(s, TextView.BufferType.EDITABLE);

            if (textView instanceof EditText) {
                EditText editText = (EditText) textView;
                editText.setSelection(s.length());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (text.length() == 0) {
            lastNextNotLetterDigitCharIndex = 0;
        }
        if (text.length() > 0) {
            eraseAndColorizeAllText(text);
        }

        if (this.start == -2) {
            return;
        }
        if (this.start == -1 && count > 0) {
            this.start = start;
            this.count = count;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (start >= 0) {
            float textSize = textView.getTextSize();
            int pos = start;

            start = -2;
            EmojiconHandler.addEmojis(textView.getContext(), s, (int) textSize, DynamicDrawableSpan.ALIGN_BASELINE, (int) textSize, pos, count);
            start = -1;
        }
    }

    private void eraseAndColorizeAllText(CharSequence text) {
        Spannable spannable = ((Spannable) textView.getText());
        CharacterStyle[] spans = spannable.getSpans(0, text.length(), CharacterStyle.class);
        for (CharacterStyle span : spans) {
            spannable.removeSpan(span);
        }
        setColorsToAllHashTags(text);
    }

    private void setColorsToAllHashTags(CharSequence text) {
        int startIndexOfNextHashSign;

        int index = 0;
        while (index < text.length() - 1) {
            char sign = text.charAt(index);
            int nextNotLetterDigitCharIndex = index + 1; // we assume it is next. if if was not changed by findNextValidHashTagChar then index will be incremented by 1
            if (sign == '@') {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                setColorForHashTagToTheEnd(text, startIndexOfNextHashSign, nextNotLetterDigitCharIndex);
                showFriends(text, startIndexOfNextHashSign, nextNotLetterDigitCharIndex);
            }

            if (sign == '#') {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                setColorForHashTagToTheEnd(text, startIndexOfNextHashSign, nextNotLetterDigitCharIndex);
            }
            index = nextNotLetterDigitCharIndex;
        }
    }

    private void showFriends(CharSequence text, int startIndex, int nextNotLetterDigitCharIndex) {
        if (lastNextNotLetterDigitCharIndex < nextNotLetterDigitCharIndex) {
            lastNextNotLetterDigitCharIndex = nextNotLetterDigitCharIndex;
            String searchText = text.toString().subSequence(startIndex + 1, nextNotLetterDigitCharIndex).toString();
            // request only in case of Edit Text..
            if (textView instanceof EditText) {
                callGetFriends(searchText);
            }
        } else {
            if (exampleCardPopup != null) {
                exampleCardPopup.dismiss();
            }
        }
    }

    private int findNextValidHashTagChar(CharSequence text, int start) {

        int nonLetterDigitCharIndex = -1;
        // skip first sign '@"
        for (int index = start + 1; index < text.length(); index++) {
            char sign = text.charAt(index);
            boolean isValidSign = Character.isLetterOrDigit(sign); //check after @ letter or digit.
            if (!isValidSign) {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1) {
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        }

        return nonLetterDigitCharIndex;
    }

    private void setColorForHashTagToTheEnd(CharSequence text, int startIndex, int nextNotLetterDigitCharIndex) {
        Log.d(TAG, "setColorForHashTagToTheEnd: " + text);
        Log.d(TAG, "setColorForHashTagToTheEnd: " + startIndex);
        Log.d(TAG, "setColorForHashTagToTheEnd: " + nextNotLetterDigitCharIndex);

        Spannable spannable = (Spannable) textView.getText();
        CharacterStyle span;
        if (mOnHashTagClickListener != null) {
            span = new ClickableForegroundColorSpan(mHashTagWordColor, this);
        } else {
            span = new ForegroundColorSpan(mHashTagWordColor);
        }
        spannable.setSpan(span, startIndex, nextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public List<String> getAllHashTags(boolean withHashes) {

        String text = textView.getText().toString();
        Spannable spannable = (Spannable) textView.getText();

        // use set to exclude duplicates
        Set<String> hashTags = new LinkedHashSet<>();

        for (CharacterStyle span : spannable.getSpans(0, text.length(), CharacterStyle.class)) {
            hashTags.add(
                    text.substring(!withHashes ? spannable.getSpanStart(span) + 1/*skip "@" sign*/
                                    : spannable.getSpanStart(span),
                            spannable.getSpanEnd(span)));
        }

        return new ArrayList<>(hashTags);
    }

    public List<String> getAllHashTags() {
        return getAllHashTags(false);
    }

    @Override
    public void onHashTagClicked(String hashTag) {
        if (mOnHashTagClickListener != null) {
            mOnHashTagClickListener.onHashTagClicked(hashTag);
        }
    }

    private void callGetFriends(String text) {
        Log.d(TAG, "callGetFriends: " + text);
        if (friendBeanCall != null && !friendBeanCall.isCanceled()) {
            friendBeanCall.cancel();
        }


        if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("string", text);
            map.put("offset", "0");
            map.put("limit", "50");
            map.put("uid", preferences.getUserId());

            if (Utils.isInternetOn(textView.getContext())) {
                friendBeanCall = mService.getFetcherService(textView.getContext()).searchFrdList(map);
                friendBeanCall.enqueue(new Callback<FriendBean>() {
                    @Override
                    public void onResponse(Call<FriendBean> call, Response<FriendBean> response) {
                        if (response.code() == StaticConstant.RESULT_OK) {
                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                if (exampleCardPopup != null) {
                                    exampleCardPopup.setAdapter(response.body().getResult().getFriends(), new TaggingAdapter.TagginListener() {
                                        @Override
                                        public void onclick(Friend bean) {
                                            Log.d(TAG, "onclick: ");
                                            exampleCardPopup.dismiss();
                                            if (!TextUtils.isEmpty(bean.getUsername())) {
                                                textView.setText(textView.getText().toString().replace("@" + text, bean.getUsername()));
                                            } else if (!TextUtils.isEmpty(bean.getFname())) {
                                                textView.setText(textView.getText().toString().replace("@" + text, bean.getFname()));
                                            }

                                            if (textView instanceof EditText) {
                                                ((EditText) textView).setSelection(textView.getText().length());
                                            }
                                        }
                                    });
                                    if (textView instanceof EditText) {
                                        exampleCardPopup.showOnAnchor(textView, RelativePopupWindow.VerticalPosition.ABOVE, RelativePopupWindow.HorizontalPosition.CENTER);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendBean> call, Throwable t) {
                        if (!call.isCanceled()) {
                            Utils.showToast(textView.getContext(), t.toString());
                        }
                    }
                });
            } else {
                Utils.showToast(textView.getContext(), textView.getContext().getString(R.string.toast_no_internet));
            }
        }


    }
}
