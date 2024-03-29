package com.screamxo.inlineplayer;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

class FloatingView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {

    private static final float SCALE_PRESSED = 0.9f;
    private static final float SCALE_NORMAL = 1.0f;
    private static final long MOVE_TO_EDGE_DURATION = 450L;
    private static final float MOVE_TO_EDGE_OVERSHOOT_TENSION = 1.25f;
    private static final float ANIMATION_SPRING_X_DAMPING_RATIO = 0.7f;
    private static final float ANIMATION_SPRING_X_STIFFNESS = 350f;
    private static final float ANIMATION_FLING_X_FRICTION = 1.7f;
    private static final float ANIMATION_FLING_Y_FRICTION = 1.7f;
    private static final int CURRENT_VELOCITY_UNITS = 1000;
    static final int STATE_NORMAL = 0;
    static final int STATE_INTERSECTING = 1;
    static final int STATE_FINISHING = 2;
    @IntDef({STATE_NORMAL, STATE_INTERSECTING, STATE_FINISHING})
    @Retention(RetentionPolicy.SOURCE)
    @interface AnimationState {
    }

    private static final int LONG_PRESS_TIMEOUT = (int) (1.5f * ViewConfiguration.getLongPressTimeout());

    private static final float MAX_X_VELOCITY_SCALE_DOWN_VALUE = 9;
    private static final float MAX_Y_VELOCITY_SCALE_DOWN_VALUE = 8;
    private static final float THROW_THRESHOLD_SCALE_DOWN_VALUE = 9;
    static final int DEFAULT_X = Integer.MIN_VALUE;
    static final int DEFAULT_Y = Integer.MIN_VALUE;
    static final int DEFAULT_WIDTH =340;
    static final int DEFAULT_HEIGHT = 280;
    private static final int OVERLAY_TYPE;
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mParams;

    private VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;

    private float mMoveThreshold;
    private float mMaximumFlingVelocity;

    private float mMaximumXVelocity;
    private float mMaximumYVelocity;
    private float mThrowMoveThreshold;
    private final DisplayMetrics mMetrics;
    private long mTouchDownTime;
    private float mScreenTouchDownX;
    private float mScreenTouchDownY;
    private boolean mIsMoveAccept;
    private float mScreenTouchX;
    private float mScreenTouchY;
    private float mLocalTouchX;
    private float mLocalTouchY;
    private int mInitX;
    private int mInitY;
    private boolean mIsInitialAnimationRunning;

    private boolean mAnimateInitialMove;
    private final int mBaseStatusBarHeight;
    private int mStatusBarHeight;
    private final int mBaseNavigationBarHeight;

    private final int mBaseNavigationBarRotatedHeight;
    private int mNavigationBarVerticalOffset;
    private int mNavigationBarHorizontalOffset;
    private int mTouchXOffset;
    private ValueAnimator mMoveEdgeAnimator;
    private final TimeInterpolator mMoveEdgeInterpolator;

    private final Rect mMoveLimitRect;
    private final Rect mPositionLimitRect;
    private boolean mIsDraggable;
    private float mShape;

    private final FloatingAnimationHandler mAnimationHandler;

    private final LongPressHandler mLongPressHandler;
    private int mOverMargin;

    private OnTouchListener mOnTouchListener;
    private boolean mIsLongPressed;
    private int mMoveDirection;
    private boolean mUsePhysics;
    private final boolean mIsTablet;
    private int mRotation;

    static {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            OVERLAY_TYPE = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        } else {
            OVERLAY_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
    }

    FloatingView(final Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        mParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.type = OVERLAY_TYPE;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mAnimationHandler = new FloatingAnimationHandler(this);
        mLongPressHandler = new LongPressHandler(this);
        mMoveEdgeInterpolator = new OvershootInterpolator(MOVE_TO_EDGE_OVERSHOOT_TENSION);
        mMoveDirection = FloatingViewManager.MOVE_DIRECTION_DEFAULT;
        mUsePhysics = false;
        final Resources resources = context.getResources();
        mIsTablet = (resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        mRotation = mWindowManager.getDefaultDisplay().getRotation();

        mMoveLimitRect = new Rect();
        mPositionLimitRect = new Rect();
        mBaseStatusBarHeight = getSystemUiDimensionPixelSize(resources, "status_bar_height");
        mStatusBarHeight = mBaseStatusBarHeight;

        updateViewConfiguration();

        // Detect NavigationBar
        if (hasSoftNavigationBar()) {
            mBaseNavigationBarHeight = getSystemUiDimensionPixelSize(resources, "navigation_bar_height");
            final String resName = mIsTablet ? "navigation_bar_height_landscape" : "navigation_bar_width";
            mBaseNavigationBarRotatedHeight = getSystemUiDimensionPixelSize(resources, resName);
        } else {
            mBaseNavigationBarHeight = 0;
            mBaseNavigationBarRotatedHeight = 0;
        }

        getViewTreeObserver().addOnPreDrawListener(this);
    }

    private boolean hasSoftNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            final DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
            return realDisplayMetrics.heightPixels > mMetrics.heightPixels || realDisplayMetrics.widthPixels > mMetrics.widthPixels;
        }

        // old device check flow
        // Navigation bar exists (config_showNavigationBar is true, or both the menu key and the back key are not exists)
        final Context context = getContext();
        final Resources resources = context.getResources();
        final boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        final boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        final int showNavigationBarResId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        final boolean hasNavigationBarConfig = showNavigationBarResId != 0 && resources.getBoolean(showNavigationBarResId);
        return hasNavigationBarConfig || (!hasMenuKey && !hasBackKey);
    }

    private static int getSystemUiDimensionPixelSize(Resources resources, String resName) {
        int pixelSize = 0;
        final int resId = resources.getIdentifier(resName, "dimen", "android");
        if (resId > 0) {
            pixelSize = resources.getDimensionPixelSize(resId);
        }
        return pixelSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshLimitRect();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateViewConfiguration();
        refreshLimitRect();
    }

    @Override
    public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);

        if (mInitX == DEFAULT_X) {
            mInitX = 0;
        }

        if (mInitY == DEFAULT_Y) {
            mInitY = mMetrics.heightPixels - mStatusBarHeight - getMeasuredHeight();
        }

        mParams.x = mInitX;
        mParams.y = mInitY;

        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_NONE) {
            moveTo(mInitX, mInitY, mInitX, mInitY, false);
        } else {
            mIsInitialAnimationRunning = true;
            moveToEdge(mInitX, mInitY, mAnimateInitialMove);
        }
        mIsDraggable = true;
        updateViewLayout();
        return true;
    }

    void onUpdateSystemLayout(boolean isHideStatusBar, boolean isHideNavigationBar, boolean isPortrait, boolean hasTouchXOffset) {
        // status bar
        mStatusBarHeight = isHideStatusBar ? 0 : mBaseStatusBarHeight;
        // touch X offset(navigation bar is displayed and it is on the left side of the device)
        mTouchXOffset = !isHideNavigationBar && hasTouchXOffset ? mBaseNavigationBarRotatedHeight : 0;
        // navigation bar
        updateNavigationBarOffset(isHideNavigationBar, isPortrait);
        refreshLimitRect();
    }

    private void updateNavigationBarOffset(boolean isHideNavigationBar, boolean isPortrait) {
        if (!isHideNavigationBar) {
            mNavigationBarVerticalOffset = 0;
            mNavigationBarHorizontalOffset = 0;
            return;
        }

        // If the portrait, is displayed at the bottom of the screen
        if (isPortrait) {
            mNavigationBarVerticalOffset = mBaseNavigationBarHeight;
            mNavigationBarHorizontalOffset = 0;
            return;
        }

        // If it is a Tablet, it will appear at the bottom of the screen.
        // If it is Phone, it will appear on the side of the screen
        if (mIsTablet) {
            mNavigationBarVerticalOffset = mBaseNavigationBarRotatedHeight;
            mNavigationBarHorizontalOffset = 0;
        } else {
            mNavigationBarVerticalOffset = 0;
            mNavigationBarHorizontalOffset = mBaseNavigationBarRotatedHeight;
        }
    }

    private void updateViewConfiguration() {
        mViewConfiguration = ViewConfiguration.get(getContext());
        mMoveThreshold = mViewConfiguration.getScaledTouchSlop();
        mMaximumFlingVelocity = mViewConfiguration.getScaledMaximumFlingVelocity();
        mMaximumXVelocity = mMaximumFlingVelocity / MAX_X_VELOCITY_SCALE_DOWN_VALUE;
        mMaximumYVelocity = mMaximumFlingVelocity / MAX_Y_VELOCITY_SCALE_DOWN_VALUE;
        mThrowMoveThreshold = mMaximumFlingVelocity / THROW_THRESHOLD_SCALE_DOWN_VALUE;
    }

    private void refreshLimitRect() {
        cancelAnimation();

        final int oldPositionLimitWidth = mPositionLimitRect.width();
        final int oldPositionLimitHeight = mPositionLimitRect.height();

        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int newScreenWidth = mMetrics.widthPixels;
        final int newScreenHeight = mMetrics.heightPixels;

        mMoveLimitRect.set(-width, -height * 2, newScreenWidth + width + mNavigationBarHorizontalOffset, newScreenHeight + height + mNavigationBarVerticalOffset);
        mPositionLimitRect.set(-mOverMargin, 0, newScreenWidth - width + mOverMargin + mNavigationBarHorizontalOffset, newScreenHeight - mStatusBarHeight - height + mNavigationBarVerticalOffset);

        // Initial animation stop when the device rotates
        final int newRotation = mWindowManager.getDefaultDisplay().getRotation();
        if (mAnimateInitialMove && mRotation != newRotation) {
            mIsInitialAnimationRunning = false;
        }

        // When animation is running and the device is not rotating
        if (mIsInitialAnimationRunning && mRotation == newRotation) {
            moveToEdge(mParams.x, mParams.y, true);
        } else {
            // If there is a screen change during the operation, move to the appropriate position
            if (mIsMoveAccept) {
                moveToEdge(mParams.x, mParams.y, false);
            } else {
                final int newX = (int) (mParams.x * mPositionLimitRect.width() / (float) oldPositionLimitWidth + 0.5f);
                final int goalPositionX = Math.min(Math.max(mPositionLimitRect.left, newX), mPositionLimitRect.right);
                final int newY = (int) (mParams.y * mPositionLimitRect.height() / (float) oldPositionLimitHeight + 0.5f);
                final int goalPositionY = Math.min(Math.max(mPositionLimitRect.top, newY), mPositionLimitRect.bottom);
                moveTo(mParams.x, mParams.y, goalPositionX, goalPositionY, false);
            }
        }
        mRotation = newRotation;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mMoveEdgeAnimator != null) {
            mMoveEdgeAnimator.removeAllUpdateListeners();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {

        if (getVisibility() != View.VISIBLE) {
            return true;
        }

        if (!mIsDraggable) {
            return true;
        }

        // Block while initial display animation is running
        if (mIsInitialAnimationRunning) {
            return true;
        }

        mScreenTouchX = event.getRawX();
        mScreenTouchY = event.getRawY();
        final int action = event.getAction();
        boolean isWaitForMoveToEdge = false;

        if (action == MotionEvent.ACTION_DOWN) {
            cancelAnimation();
            mScreenTouchDownX = mScreenTouchX;
            mScreenTouchDownY = mScreenTouchY;
            mLocalTouchX = event.getX();
            mLocalTouchY = event.getY();
            mIsMoveAccept = false;
            setScale(SCALE_PRESSED);

            if (mVelocityTracker == null) {
                // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker.clear();
            }

            mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mAnimationHandler.sendAnimationMessage(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            mLongPressHandler.sendEmptyMessageDelayed(LongPressHandler.LONG_PRESSED, LONG_PRESS_TIMEOUT);
            mTouchDownTime = event.getDownTime();
            // compute offset and restore
            addMovement(event);
            mIsInitialAnimationRunning = false;
        }

        else if (action == MotionEvent.ACTION_MOVE) {

            if (mIsMoveAccept) {
                mIsLongPressed = false;
                mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            }

            if (mTouchDownTime != event.getDownTime()) {
                return true;
            }

            if (!mIsMoveAccept && Math.abs(mScreenTouchX - mScreenTouchDownX) < mMoveThreshold && Math.abs(mScreenTouchY - mScreenTouchDownY) < mMoveThreshold) {
                return true;
            }
            mIsMoveAccept = true;
            mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
            // compute offset and restore
            addMovement(event);
        }

        else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            // compute velocity tracker
            if (mVelocityTracker != null) {
                mVelocityTracker.computeCurrentVelocity(CURRENT_VELOCITY_UNITS);
            }

            final boolean tmpIsLongPressed = mIsLongPressed;

            mIsLongPressed = false;
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            if (mTouchDownTime != event.getDownTime()) {
                return true;
            }
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            setScale(SCALE_NORMAL);

            // destroy VelocityTracker
            if (!mIsMoveAccept) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }

            // When ACTION_UP is done (when not pressed or moved)
            if (action == MotionEvent.ACTION_UP && !tmpIsLongPressed && !mIsMoveAccept) {
                final int size = getChildCount();
                for (int i = 0; i < size; i++) {
                    getChildAt(i).performClick();
                }
            } else {
                // Make a move after checking whether it is finished or not
                isWaitForMoveToEdge = true;
            }
        }

        if (mOnTouchListener != null) {
            mOnTouchListener.onTouch(this, event);
        }

        // Lazy execution of moveToEdge
        if (isWaitForMoveToEdge && mAnimationHandler.getState() != STATE_FINISHING) {
            // include device rotation
            moveToEdge(true);
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        return true;
    }

    private void addMovement(@NonNull MotionEvent event) {
        final float deltaX = event.getRawX() - event.getX();
        final float deltaY = event.getRawY() - event.getY();
        event.offsetLocation(deltaX, deltaY);
        mVelocityTracker.addMovement(event);
        event.offsetLocation(-deltaX, -deltaY);
    }

    private void onLongClick() {
        mIsLongPressed = true;
        // 長押し処理
        final int size = getChildCount();
        for (int i = 0; i < size; i++) {
            getChildAt(i).performLongClick();
        }
    }

    @Override
    public void setVisibility(int visibility) {

        if (visibility != View.VISIBLE) {

            cancelLongPress();
            setScale(SCALE_NORMAL);
            if (mIsMoveAccept) {
                moveToEdge(false);
            }
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
        }
        super.setVisibility(visibility);
    }

    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }

    private void moveToEdge(boolean withAnimation) {
        final int currentX = getXByTouch();
        final int currentY = getYByTouch();
        moveToEdge(currentX, currentY, withAnimation);
    }

    private void moveToEdge(int startX, int startY, boolean withAnimation) {
        final int goalPositionX = getGoalPositionX(startX, startY);
        final int goalPositionY = getGoalPositionY(startX, startY);
        moveTo(startX, startY, goalPositionX, goalPositionY, withAnimation);
    }


    private void moveTo(int currentX, int currentY, int goalPositionX, int goalPositionY, boolean withAnimation) {

        goalPositionX = Math.min(Math.max(mPositionLimitRect.left, goalPositionX), mPositionLimitRect.right);
        goalPositionY = Math.min(Math.max(mPositionLimitRect.top, goalPositionY), mPositionLimitRect.bottom);

        if (withAnimation) {
            // Use physics animation
            final boolean usePhysicsAnimation = mUsePhysics && mVelocityTracker != null && mMoveDirection != FloatingViewManager.MOVE_DIRECTION_NEAREST;
            if (usePhysicsAnimation) {
                startPhysicsAnimation(goalPositionX, currentY);
            } else {
                startObjectAnimation(currentX, currentY, goalPositionX, goalPositionY);
            }
        } else {

            if (mParams.x != goalPositionX || mParams.y != goalPositionY) {
                mParams.x = goalPositionX;
                mParams.y = goalPositionY;
                updateViewLayout();
            }
        }

        mLocalTouchX = 0;
        mLocalTouchY = 0;
        mScreenTouchDownX = 0;
        mScreenTouchDownY = 0;
        mIsMoveAccept = false;
    }

    private void startPhysicsAnimation(int goalPositionX, int currentY) {
        // start X coordinate animation
        final boolean containsLimitRectWidth = mParams.x < mPositionLimitRect.right && mParams.x > mPositionLimitRect.left;
        // If MOVE_DIRECTION_NONE, play fling animation
        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_NONE && containsLimitRectWidth) {
            final float velocityX = Math.min(Math.max(mVelocityTracker.getXVelocity(), -mMaximumXVelocity), mMaximumXVelocity);
            //startFlingAnimationX(velocityX);
        } else {
           // startSpringAnimationX(goalPositionX);
        }

        // start Y coordinate animation
        final boolean containsLimitRectHeight = mParams.y < mPositionLimitRect.bottom && mParams.y > mPositionLimitRect.top;
        final float velocityY = -Math.min(Math.max(mVelocityTracker.getYVelocity(), -mMaximumYVelocity), mMaximumYVelocity);
        if (containsLimitRectHeight) {
          //  startFlingAnimationY(velocityY);
        } else {
          //  startSpringAnimationY(currentY, velocityY);
        }
    }

    private void startObjectAnimation(int currentX, int currentY, int goalPositionX, int goalPositionY) {
        if (goalPositionX == currentX) {
            //to move only y coord
            mMoveEdgeAnimator = ValueAnimator.ofInt(currentY, goalPositionY);
            mMoveEdgeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mParams.y = (Integer) animation.getAnimatedValue();
                    updateViewLayout();
                    updateInitAnimation(animation);
                }
            });
        } else {
            // To move only x coord (to left or right)
            mParams.y = goalPositionY;
            mMoveEdgeAnimator = ValueAnimator.ofInt(currentX, goalPositionX);
            mMoveEdgeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mParams.x = (Integer) animation.getAnimatedValue();
                    updateViewLayout();
                    updateInitAnimation(animation);
                }
            });
        }

        mMoveEdgeAnimator.setDuration(MOVE_TO_EDGE_DURATION);
        mMoveEdgeAnimator.setInterpolator(mMoveEdgeInterpolator);
        mMoveEdgeAnimator.start();
    }

   /* private void startSpringAnimationX(int goalPositionX) {
        // springX
        final SpringForce springX = new SpringForce(goalPositionX);
        springX.setDampingRatio(ANIMATION_SPRING_X_DAMPING_RATIO);
        springX.setStiffness(ANIMATION_SPRING_X_STIFFNESS);
        // springAnimation
        final SpringAnimation springAnimationX = new SpringAnimation(new FloatValueHolder());
        springAnimationX.setStartVelocity(mVelocityTracker.getXVelocity());
        springAnimationX.setStartValue(mParams.x);
        springAnimationX.setSpring(springX);
        springAnimationX.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS);
        springAnimationX.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                final int x = Math.round(value);
                // Not moving, or the touch operation is continuing
                if (mParams.x == x || mVelocityTracker != null) {
                    return;
                }
                // update x coordinate
                mParams.x = x;
                updateViewLayout();
            }
        });
        springAnimationX.start();
    }

    private void startSpringAnimationY(int currentY, float velocityY) {
        // Create SpringForce
        final SpringForce springY = new SpringForce(currentY < mMetrics.heightPixels / 2 ? mPositionLimitRect.top : mPositionLimitRect.bottom);
        springY.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springY.setStiffness(SpringForce.STIFFNESS_LOW);

        // Create SpringAnimation
        final SpringAnimation springAnimationY = new SpringAnimation(new FloatValueHolder());
        springAnimationY.setStartVelocity(velocityY);
        springAnimationY.setStartValue(mParams.y);
        springAnimationY.setSpring(springY);
        springAnimationY.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS);
        springAnimationY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                final int y = Math.round(value);
                // Not moving, or the touch operation is continuing
                if (mParams.y == y || mVelocityTracker != null) {
                    return;
                }
                // update y coordinate
                mParams.y = y;
                updateViewLayout();
            }
        });
        springAnimationY.start();
    }

    private void startFlingAnimationX(float velocityX) {
        final FlingAnimation flingAnimationX = new FlingAnimation(new FloatValueHolder());
        flingAnimationX.setStartVelocity(velocityX);
        flingAnimationX.setMaxValue(mPositionLimitRect.right);
        flingAnimationX.setMinValue(mPositionLimitRect.left);
        flingAnimationX.setStartValue(mParams.x);
        flingAnimationX.setFriction(ANIMATION_FLING_X_FRICTION);
        flingAnimationX.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS);
        flingAnimationX.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                final int x = Math.round(value);
                // Not moving, or the touch operation is continuing
                if (mParams.x == x || mVelocityTracker != null) {
                    return;
                }
                // update y coordinate
                mParams.x = x;
                updateViewLayout();
            }
        });
        flingAnimationX.start();
    }

    private void startFlingAnimationY(float velocityY) {
        final FlingAnimation flingAnimationY = new FlingAnimation(new FloatValueHolder());
        flingAnimationY.setStartVelocity(velocityY);
        flingAnimationY.setMaxValue(mPositionLimitRect.bottom);
        flingAnimationY.setMinValue(mPositionLimitRect.top);
        flingAnimationY.setStartValue(mParams.y);
        flingAnimationY.setFriction(ANIMATION_FLING_Y_FRICTION);
        flingAnimationY.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS);
        flingAnimationY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                final int y = Math.round(value);
                // Not moving, or the touch operation is continuing
                if (mParams.y == y || mVelocityTracker != null) {
                    return;
                }
                // update y coordinate
                mParams.y = y;
                updateViewLayout();
            }
        });
        flingAnimationY.start();
    }*/


    private void updateViewLayout() {
        if (!ViewCompat.isAttachedToWindow(this)) {
            return;
        }
        mWindowManager.updateViewLayout(this, mParams);
    }

    private void updateInitAnimation(ValueAnimator animation) {
        if (mAnimateInitialMove && animation.getDuration() <= animation.getCurrentPlayTime()) {
            mIsInitialAnimationRunning = false;
        }
    }

    private int getGoalPositionX(int startX, int startY) {
        int goalPositionX = startX;

        // Move to left or right edges
        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_DEFAULT) {
            final boolean isMoveRightEdge = startX > (mMetrics.widthPixels - getWidth()) / 2;
            goalPositionX = isMoveRightEdge ? mPositionLimitRect.right : mPositionLimitRect.left;
        }
        // Move to left edges
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_LEFT) {
            goalPositionX = mPositionLimitRect.left;
        }
        // Move to right edges
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_RIGHT) {
            goalPositionX = mPositionLimitRect.right;
        }
        // Move to top/bottom/left/right edges
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_NEAREST) {
            final int distLeftRight = Math.min(startX, mPositionLimitRect.width() - startX);
            final int distTopBottom = Math.min(startY, mPositionLimitRect.height() - startY);
            if (distLeftRight < distTopBottom) {
                final boolean isMoveRightEdge = startX > (mMetrics.widthPixels - getWidth()) / 2;
                goalPositionX = isMoveRightEdge ? mPositionLimitRect.right : mPositionLimitRect.left;
            }
        }
        // Move in the direction in which it is thrown
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_THROWN) {
            if (mVelocityTracker != null && mVelocityTracker.getXVelocity() > mThrowMoveThreshold) {
                goalPositionX = mPositionLimitRect.right;
            } else if (mVelocityTracker != null && mVelocityTracker.getXVelocity() < -mThrowMoveThreshold) {
                goalPositionX = mPositionLimitRect.left;
            } else {
                final boolean isMoveRightEdge = startX > (mMetrics.widthPixels - getWidth()) / 2;
                goalPositionX = isMoveRightEdge ? mPositionLimitRect.right : mPositionLimitRect.left;
            }
        }

        return goalPositionX;
    }

    private int getGoalPositionY(int startX, int startY) {
        int goalPositionY = startY;

        // Move to top/bottom/left/right edges
        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_NEAREST) {
            final int distLeftRight = Math.min(startX, mPositionLimitRect.width() - startX);
            final int distTopBottom = Math.min(startY, mPositionLimitRect.height() - startY);
            if (distLeftRight >= distTopBottom) {
                final boolean isMoveTopEdge = startY < (mMetrics.heightPixels - getHeight()) / 2;
                goalPositionY = isMoveTopEdge ? mPositionLimitRect.top : mPositionLimitRect.bottom;
            }
        }

        return goalPositionY;
    }

    private void cancelAnimation() {
        if (mMoveEdgeAnimator != null && mMoveEdgeAnimator.isStarted()) {
            mMoveEdgeAnimator.cancel();
            mMoveEdgeAnimator = null;
        }
    }

    private void setScale(float newScale) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View targetView = getChildAt(i);
                targetView.setScaleX(newScale);
                targetView.setScaleY(newScale);
            }
        } else {
            setScaleX(newScale);
            setScaleY(newScale);
        }
    }

    void setDraggable(boolean isDraggable) {
        mIsDraggable = isDraggable;
    }

    void setShape(float shape) {
        mShape = shape;
    }

    float getShape() {
        return mShape;
    }

    void setOverMargin(int margin) {
        mOverMargin = margin;
    }

    void setMoveDirection(int moveDirection) {
        mMoveDirection = moveDirection;
    }

    void usePhysics(boolean usePhysics) {
        mUsePhysics = usePhysics && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    void setInitCoords(int x, int y) {
        mInitX = x;
        mInitY = y;
    }

    void setAnimateInitialMove(boolean animateInitialMove) {
        mAnimateInitialMove = animateInitialMove;
    }

    void getWindowDrawingRect(Rect outRect) {
        final int currentX = getXByTouch();
        final int currentY = getYByTouch();
        outRect.set(currentX, currentY, currentX + getWidth(), currentY + getHeight());
    }

    WindowManager.LayoutParams getWindowLayoutParams() {
        return mParams;
    }

    private int getXByTouch() {
        return (int) (mScreenTouchX - mLocalTouchX - mTouchXOffset);
    }

    private int getYByTouch() {
        return (int) (mMetrics.heightPixels + mNavigationBarVerticalOffset - (mScreenTouchY - mLocalTouchY + getHeight()));
    }

    void setNormal() {
        mAnimationHandler.setState(STATE_NORMAL);
        mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
    }

    void setIntersecting(int centerX, int centerY) {
        mAnimationHandler.setState(STATE_INTERSECTING);
        mAnimationHandler.updateTargetPosition(centerX, centerY);
    }

    void setFinishing() {
        mAnimationHandler.setState(STATE_FINISHING);
        mIsMoveAccept = false;
        setVisibility(View.GONE);
    }

    int getState() {
        return mAnimationHandler.getState();
    }

    static class FloatingAnimationHandler extends Handler {

        private static final long ANIMATION_REFRESH_TIME_MILLIS = 10L;
        private static final long CAPTURE_DURATION_MILLIS = 300L;
        private static final int ANIMATION_NONE = 0;
        private static final int ANIMATION_IN_TOUCH = 1;
        private static final int TYPE_FIRST = 1;
        private static final int TYPE_UPDATE = 2;
        private long mStartTime;
        private float mStartX;
        private float mStartY;
        private int mStartedCode;
        private int mState;
        private boolean mIsChangeState;
        private float mTouchPositionX;
        private float mTouchPositionY;
        private float mTargetPositionX;
        private float mTargetPositionY;
        private final WeakReference<FloatingView> mFloatingView;
        FloatingAnimationHandler(FloatingView floatingView) {
            mFloatingView = new WeakReference<>(floatingView);
            mStartedCode = ANIMATION_NONE;
            mState = STATE_NORMAL;
        }

        @Override
        public void handleMessage(Message msg) {
            final FloatingView floatingView = mFloatingView.get();
            if (floatingView == null) {
                removeMessages(ANIMATION_IN_TOUCH);
                return;
            }

            final int animationCode = msg.what;
            final int animationType = msg.arg1;
            final WindowManager.LayoutParams params = floatingView.mParams;
            if (mIsChangeState || animationType == TYPE_FIRST) {
                mStartTime = mIsChangeState ? SystemClock.uptimeMillis() : 0;
                mStartX = params.x;
                mStartY = params.y;
                mStartedCode = animationCode;
                mIsChangeState = false;
            }

            final float elapsedTime = SystemClock.uptimeMillis() - mStartTime;
            final float trackingTargetTimeRate = Math.min(elapsedTime / CAPTURE_DURATION_MILLIS, 1.0f);

            if (mState == FloatingView.STATE_NORMAL) {
                final float basePosition = calcAnimationPosition(trackingTargetTimeRate);
                final Rect moveLimitRect = floatingView.mMoveLimitRect;
                final float targetPositionX = Math.min(Math.max(moveLimitRect.left, (int) mTouchPositionX), moveLimitRect.right);
                final float targetPositionY = Math.min(Math.max(moveLimitRect.top, (int) mTouchPositionY), moveLimitRect.bottom);
                params.x = (int) (mStartX + (targetPositionX - mStartX) * basePosition);
                params.y = (int) (mStartY + (targetPositionY - mStartY) * basePosition);
                floatingView.updateViewLayout();
                sendMessageAtTime(newMessage(animationCode, TYPE_UPDATE), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            }

            else if (mState == FloatingView.STATE_INTERSECTING) {
                final float basePosition = calcAnimationPosition(trackingTargetTimeRate);
                final float targetPositionX = mTargetPositionX - floatingView.getWidth() / 2;
                final float targetPositionY = mTargetPositionY - floatingView.getHeight() / 2;
                params.x = (int) (mStartX + (targetPositionX - mStartX) * basePosition);
                params.y = (int) (mStartY + (targetPositionY - mStartY) * basePosition);
                floatingView.updateViewLayout();
                sendMessageAtTime(newMessage(animationCode, TYPE_UPDATE), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            }

        }

        private static float calcAnimationPosition(float timeRate) {
            final float position;
            // y=0.55sin(8.0564x-π/2)+0.55
            if (timeRate <= 0.4) {
                position = (float) (0.55 * Math.sin(8.0564 * timeRate - Math.PI / 2) + 0.55);
            }
            // y=4(0.417x-0.341)^2-4(0.417-0.341)^2+1
            else {
                position = (float) (4 * Math.pow(0.417 * timeRate - 0.341, 2) - 4 * Math.pow(0.417 - 0.341, 2) + 1);
            }
            return position;
        }

        void sendAnimationMessageDelayed(int animation, long delayMillis) {
            sendMessageAtTime(newMessage(animation, TYPE_FIRST), SystemClock.uptimeMillis() + delayMillis);
        }

        void sendAnimationMessage(int animation) {
            sendMessage(newMessage(animation, TYPE_FIRST));
        }

        private static Message newMessage(int animation, int type) {
            final Message message = Message.obtain();
            message.what = animation;
            message.arg1 = type;
            return message;
        }

        void updateTouchPosition(float positionX, float positionY) {
            mTouchPositionX = positionX;
            mTouchPositionY = positionY;
        }

        void updateTargetPosition(float centerX, float centerY) {
            mTargetPositionX = centerX;
            mTargetPositionY = centerY;
        }


        void setState(@AnimationState int newState) {

            if (mState != newState) {
                mIsChangeState = true;
            }
            mState = newState;
        }

        int getState() {
            return mState;
        }
    }

    static class LongPressHandler extends Handler {

        private final WeakReference<FloatingView> mFloatingView;
        private static final int LONG_PRESSED = 0;
        LongPressHandler(FloatingView view) {
            mFloatingView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            FloatingView view = mFloatingView.get();
            if (view == null) {
                removeMessages(LONG_PRESSED);
                return;
            }

            view.onLongClick();
        }
    }
}
