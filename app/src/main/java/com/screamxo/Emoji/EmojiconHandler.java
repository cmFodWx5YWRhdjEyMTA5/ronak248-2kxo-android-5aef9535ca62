/*
 * Copyright 2016 nekocode
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.screamxo.Emoji;

import android.content.Context;
import android.text.Spannable;

import com.screamxo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {
    private EmojiconHandler() {
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length) {
        HashMap<String, ArrayList<Integer>> emojiIndexMap = new HashMap<>();

        if (text.toString().contains("[:angryface:]")) {
            emojiIndexMap.put("\\[:angryface:]", getEmojiIndexs(text.toString(), "\\[:angryface:]"));
        }
        if (text.toString().contains("[:bigsmile:]")) {
            emojiIndexMap.put("\\[:bigsmile:]", getEmojiIndexs(text.toString(), "\\[:bigsmile:]"));
        }
        if (text.toString().contains("[:cry:]")) {
            emojiIndexMap.put("\\[:cry:]", getEmojiIndexs(text.toString(), "\\[:cry:]"));
        }
        if (text.toString().contains("[:dizzy:]")) {
            emojiIndexMap.put("\\[:dizzy:]", getEmojiIndexs(text.toString(), "\\[:dizzy:]"));
        }
        if (text.toString().contains("[:rosy-chicks:]")) {
            emojiIndexMap.put("\\[:rosy-chicks:]", getEmojiIndexs(text.toString(), "\\[:rosy-chicks:]"));
        }
        if (text.toString().contains("[:tongue:]")) {
            emojiIndexMap.put("\\[:tongue:]", getEmojiIndexs(text.toString(), "\\[:tongue:]"));
        }
        if (text.toString().contains("[:wink:]")) {
            emojiIndexMap.put("\\[:wink:]", getEmojiIndexs(text.toString(), "\\[:wink:]"));
        }

        if (emojiIndexMap.size() > 0) {
            for (Map.Entry<String, ArrayList<Integer>> entry : emojiIndexMap.entrySet()) {
                int icon = getResourceDrawable(entry.getKey());
                ArrayList<Integer> indexes = entry.getValue();
                for (Integer x : indexes) {
                    text.setSpan(new EmojiconSpan(context, icon, emojiSize, emojiAlignment, textSize), x, x + (entry.getKey().length() - 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private static int getResourceDrawable(String emoji) {
        int resourceId = 0;

        switch (emoji) {
            case "\\[:angryface:]":
                resourceId = R.mipmap.emoji_angry;
                break;
            case "\\[:bigsmile:]":
                resourceId = R.mipmap.emoji_happy;
                break;
            case "\\[:cry:]":
                resourceId = R.mipmap.emoji_cry;
                break;
            case "\\[:dizzy:]":
                resourceId = R.mipmap.emoji_sad;
                break;
            case "\\[:rosy-chicks:]":
                resourceId = R.mipmap.emoji_blush;
                break;
            case "\\[:tongue:]":
                resourceId = R.mipmap.emoji_tongue;
                break;
            case "\\[:wink:]":
                resourceId = R.mipmap.emoji_wink;
                break;

        }

        return resourceId;
    }

    private static ArrayList<Integer> getEmojiIndexs(String text, String emoji) {
        ArrayList<Integer> emojiIndexs = new ArrayList<>();
        Pattern p = Pattern.compile(emoji);
        Matcher m = p.matcher(text);
        while (m.find()) {
            emojiIndexs.add(m.start());
        }
        return emojiIndexs;
    }
}
