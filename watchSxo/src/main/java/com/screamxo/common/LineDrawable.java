package com.screamxo.common;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class LineDrawable extends Drawable {
    private Paint mPaint;
    private boolean isTop;

    public LineDrawable(boolean isTop, int color) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        this.isTop = isTop;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect b = getBounds();
        if(isTop)
            canvas.drawArc(new RectF(0 , 0, b.width(), b.height()), 180, 180, false, mPaint);
        else
            canvas.drawArc(new RectF(0 , 0, b.width(), b.height()), 0, 180, false, mPaint);
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}