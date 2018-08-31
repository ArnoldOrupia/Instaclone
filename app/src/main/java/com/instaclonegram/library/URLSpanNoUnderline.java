package com.instaclonegram.library;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by lamine on 07/04/2016.
 */
public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String p_Url) {
        super(p_Url);
    }

    public void updateDrawState(TextPaint p_DrawState) {
        super.updateDrawState(p_DrawState);
        p_DrawState.setUnderlineText(false);
    }
}