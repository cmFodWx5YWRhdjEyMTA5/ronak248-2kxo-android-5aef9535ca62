package com.screamxo.inlineplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.DisplayMetrics;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class FloatingViewManager implements ScreenChangedListener, View.OnTouchListener, TrashViewListener {

    public static final int DISPLAY_MODE_SHOW_ALWAYS = 1;
    public static final int DISPLAY_MODE_HIDE_ALWAYS = 2;
    public static final int DISPLAY_MODE_HIDE_FULLSCREEN = 3;
    @IntDef({DISPLAY_MODE_SHOW_ALWAYS, DISPLAY_MODE_HIDE_ALWAYS, DISPLAY_MODE_HIDE_FULLSCREEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayMode {
    }

    public static final int MOVE_DIRECTION_DEFAULT = 0;
    public static final int MOVE_DIRECTION_LEFT = 1;
    public static final int MOVE_DIRECTION_RIGHT = 2;
    public static final int MOVE_DIRECTION_NONE = 3;
    public static final int MOVE_DIRECTION_NEAREST = 4;
    public static final int MOVE_DIRECTION_THROWN = 5;

    @IntDef({MOVE_DIRECTION_DEFAULT, MOVE_DIRECTION_LEFT, MOVE_DIRECTION_RIGHT,
            MOVE_DIRECTION_NEAREST, MOVE_DIRECTION_NONE, MOVE_DIRECTION_THROWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoveDirection {
    }

    public static final float SHAPE_CIRCLE = 1.0f;
    public static final float SHAPE_RECTANGLE = 1.4142f;
    private final Context mContext;
    private final Resources mResources;
    private final WindowManager mWindowManager;
    private final DisplayMetrics mDisplayMetrics;
    private FloatingView mTargetFloatingView;
    private final FullscreenObserverView mFullscreenObserverView;
    private final TrashView mTrashView;
    private final FloatingViewListener mFloatingViewListener;
    private final Rect mFloatingViewRect;
    private final Rect mTrashViewRect;
    private boolean mIsMoveAccept;

    @DisplayMode
    private int mDisplayMode;

    private final ArrayList<FloatingView> mFloatingViewList;

    public FloatingViewManager(Context context, FloatingViewListener listener) {
        mContext = context;
        mResources = context.getResources();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = new DisplayMetrics();
        mFloatingViewListener = listener;
        mFloatingViewRect = new Rect();
        mTrashViewRect = new Rect();
        mIsMoveAccept = false;
        mDisplayMode = DISPLAY_MODE_HIDE_FULLSCREEN;
        mFloatingViewList = new ArrayList<>();
        mFullscreenObserverView = new FullscreenObserverView(context, this);
        mTrashView = new TrashView(context);
    }

    private boolean isIntersectWithTrash() {
        if (!mTrashView.isTrashEnabled()) {
            return false;
        }
        mTrashView.getWindowDrawingRect(mTrashViewRect);
        mTargetFloatingView.getWindowDrawingRect(mFloatingViewRect);
        return Rect.intersects(mTrashViewRect, mFloatingViewRect);
    }

    @Override
    public void onScreenChanged(Rect windowRect, int visibility) {
        // detect status bar
        final boolean isFitSystemWindowTop = windowRect.top == 0;
        boolean isHideStatusBar;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && visibility != FullscreenObserverView.NO_LAST_VISIBILITY) {
            // Support for screen rotation when setSystemUiVisibility is used
            isHideStatusBar = isFitSystemWindowTop || (visibility & View.SYSTEM_UI_FLAG_LOW_PROFILE) == View.SYSTEM_UI_FLAG_LOW_PROFILE;
        } else {
            isHideStatusBar = isFitSystemWindowTop;
        }

        // detect navigation bar
        final boolean isHideNavigationBar;
        if (visibility == FullscreenObserverView.NO_LAST_VISIBILITY) {
            // At the first it can not get the correct value, so do special processing
            mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
            isHideNavigationBar = windowRect.width() - mDisplayMetrics.widthPixels > 0 || windowRect.height() - mDisplayMetrics.heightPixels > 0;
        } else {
            isHideNavigationBar = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        final boolean isPortrait = mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // touch X offset(navigation bar is on the left side)
        final boolean hasTouchXOffset = windowRect.left > 0;

        // update FloatingView layout
        mTargetFloatingView.onUpdateSystemLayout(isHideStatusBar, isHideNavigationBar, isPortrait, hasTouchXOffset);

        if (mDisplayMode != DISPLAY_MODE_HIDE_FULLSCREEN) {
            return;
        }

        mIsMoveAccept = false;
        final int state = mTargetFloatingView.getState();
        if (state == FloatingView.STATE_NORMAL) {
            final int size = mFloatingViewList.size();
            for (int i = 0; i < size; i++) {
                final FloatingView floatingView = mFloatingViewList.get(i);
                floatingView.setVisibility(isFitSystemWindowTop ? View.GONE : View.VISIBLE);
            }
            mTrashView.dismiss();
        }

        else if (state == FloatingView.STATE_INTERSECTING) {
            mTargetFloatingView.setFinishing();
            mTrashView.dismiss();
        }
    }

    @Override
    public void onUpdateActionTrashIcon() {
        mTrashView.updateActionTrashIcon(mTargetFloatingView.getMeasuredWidth(), mTargetFloatingView.getMeasuredHeight(), mTargetFloatingView.getShape());
    }

    @Override
    public void onTrashAnimationStarted(@TrashView.AnimationState int animationCode) {
        if (animationCode == TrashView.ANIMATION_CLOSE || animationCode == TrashView.ANIMATION_FORCE_CLOSE) {
            final int size = mFloatingViewList.size();
            for (int i = 0; i < size; i++) {
                final FloatingView floatingView = mFloatingViewList.get(i);
                floatingView.setDraggable(false);
            }
        }
    }

    @Override
    public void onTrashAnimationEnd(@TrashView.AnimationState int animationCode) {

        final int state = mTargetFloatingView.getState();
        if (state == FloatingView.STATE_FINISHING) {
            removeViewToWindow(mTargetFloatingView);
        }

        final int size = mFloatingViewList.size();
        for (int i = 0; i < size; i++) {
            final FloatingView floatingView = mFloatingViewList.get(i);
            floatingView.setDraggable(true);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();

        if (action != MotionEvent.ACTION_DOWN && !mIsMoveAccept) {
            return false;
        }

        final int state = mTargetFloatingView.getState();
        mTargetFloatingView = (FloatingView) v;

        if (action == MotionEvent.ACTION_DOWN) {
            mIsMoveAccept = true;
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            final boolean isIntersecting = isIntersectWithTrash();
            final boolean isIntersect = state == FloatingView.STATE_INTERSECTING;
            if (isIntersecting) {
                mTargetFloatingView.setIntersecting((int) mTrashView.getTrashIconCenterX(), (int) mTrashView.getTrashIconCenterY());
            }
            if (isIntersecting && !isIntersect) {
                mTargetFloatingView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                mTrashView.setScaleTrashIcon(true);
            }
            else if (!isIntersecting && isIntersect) {
                mTargetFloatingView.setNormal();
                mTrashView.setScaleTrashIcon(false);
            }
        }

        else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {

            if (state == FloatingView.STATE_INTERSECTING) {
                mTargetFloatingView.setFinishing();
                mTrashView.setScaleTrashIcon(false);
            }
            mIsMoveAccept = false;

            // Touch finish callback
            if (mFloatingViewListener != null) {
                final boolean isFinishing = mTargetFloatingView.getState() == FloatingView.STATE_FINISHING;
                final WindowManager.LayoutParams params = mTargetFloatingView.getWindowLayoutParams();
                mFloatingViewListener.onTouchFinished(isFinishing, params.x, params.y);
            }
        }

        if (state == FloatingView.STATE_INTERSECTING) {
            mTrashView.onTouchFloatingView(event, mFloatingViewRect.left, mFloatingViewRect.top);
        } else {
            final WindowManager.LayoutParams params = mTargetFloatingView.getWindowLayoutParams();
            mTrashView.onTouchFloatingView(event, params.x, params.y);
        }

        return false;
    }

    public void setFixedTrashIconImage(@DrawableRes int resId) {
        mTrashView.setFixedTrashIconImage(resId);
    }

    public void setActionTrashIconImage(@DrawableRes int resId) {
        mTrashView.setActionTrashIconImage(resId);
    }

    public void setFixedTrashIconImage(Drawable drawable) {
        mTrashView.setFixedTrashIconImage(drawable);
    }

    public void setActionTrashIconImage(Drawable drawable) {
        mTrashView.setActionTrashIconImage(drawable);
    }

    public void setDisplayMode(@DisplayMode int displayMode) {
        mDisplayMode = displayMode;
        if (mDisplayMode == DISPLAY_MODE_SHOW_ALWAYS || mDisplayMode == DISPLAY_MODE_HIDE_FULLSCREEN) {
            for (FloatingView floatingView : mFloatingViewList) {
                floatingView.setVisibility(View.VISIBLE);
            }
        }

        else if (mDisplayMode == DISPLAY_MODE_HIDE_ALWAYS) {
            for (FloatingView floatingView : mFloatingViewList) {
                floatingView.setVisibility(View.GONE);
            }
            mTrashView.dismiss();
        }
    }

    public void setTrashViewEnabled(boolean enabled) {
        mTrashView.setTrashEnabled(enabled);
    }

    public boolean isTrashViewEnabled() {
        return mTrashView.isTrashEnabled();
    }

    public void addViewToWindow(View view, Options options) {
        final boolean isFirstAttach = mFloatingViewList.isEmpty();
        // FloatingView
        final FloatingView floatingView = new FloatingView(mContext);
        floatingView.setInitCoords(options.floatingViewX, options.floatingViewY);
        floatingView.setOnTouchListener(this);
        floatingView.setShape(options.shape);
        floatingView.setOverMargin(options.overMargin);
        floatingView.setMoveDirection(options.moveDirection);
        floatingView.usePhysics(options.usePhysics);
        floatingView.setAnimateInitialMove(options.animateInitialMove);

        // set FloatingView size
        final FrameLayout.LayoutParams targetParams = new FrameLayout.LayoutParams(options.floatingViewWidth, options.floatingViewHeight);
        view.setLayoutParams(targetParams);
        floatingView.addView(view);

        if (mDisplayMode == DISPLAY_MODE_HIDE_ALWAYS) {
            floatingView.setVisibility(View.GONE);
        }
        mFloatingViewList.add(floatingView);
        // TrashView
        mTrashView.setTrashViewListener(this);

        mWindowManager.addView(floatingView, floatingView.getWindowLayoutParams());
        if (isFirstAttach) {
            mWindowManager.addView(mFullscreenObserverView, mFullscreenObserverView.getWindowLayoutParams());
            mTargetFloatingView = floatingView;
        } else {
            mWindowManager.removeViewImmediate(mTrashView);
        }
        mWindowManager.addView(mTrashView, mTrashView.getWindowLayoutParams());
    }

    private void removeViewToWindow(FloatingView floatingView) {
    }

    public void removeAllViewToWindow() {

    }

    public static class Options {

        public float shape;

        public int overMargin;

        public int floatingViewX;

        public int floatingViewY;

        public int floatingViewWidth;

        public int floatingViewHeight;

        @MoveDirection
        public int moveDirection;

        public boolean usePhysics;

        public boolean animateInitialMove;

        public Options() {
            shape = SHAPE_CIRCLE;
            overMargin = 0;
            floatingViewX = FloatingView.DEFAULT_X;
            floatingViewY = FloatingView.DEFAULT_Y;
            floatingViewWidth = FloatingView.DEFAULT_WIDTH;
            floatingViewHeight = FloatingView.DEFAULT_HEIGHT;
            moveDirection = MOVE_DIRECTION_DEFAULT;
            usePhysics = true;
            animateInitialMove = true;
        }
    }
}
