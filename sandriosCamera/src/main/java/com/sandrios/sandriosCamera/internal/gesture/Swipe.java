package com.sandrios.sandriosCamera.internal.gesture;

import android.view.MotionEvent;

public class Swipe {
    public final static int SWIPING_THRESHOLD = 40;
    public final static int SWIPED_THRESHOLD = 25;
    private SwipeListener swipeListener;
    private float xDown, xUp;
    private float yDown, yUp;
    private float xMove, yMove;

    public void addListener(SwipeListener swipeListener) {
        checkNotNull(swipeListener, "swipeListener == null");
        this.swipeListener = swipeListener;
    }

    public void dispatchTouchEvent(final MotionEvent event) {
        checkNotNull(event, "event == null");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // user started touching the screen
                onActionDown(event);
                break;
            case MotionEvent.ACTION_UP:   // user stopped touching the screen
                onActionUp(event);
                break;
            case MotionEvent.ACTION_MOVE: // user is moving finger on the screen
                onActionMove(event);
                break;
            default:
                break;
        }
    }

    private void onActionDown(final MotionEvent event) {
        xDown = event.getX();
        yDown = event.getY();
    }

    private void onActionUp(final MotionEvent event) {
        xUp = event.getX();
        yUp = event.getY();
        final boolean swipedHorizontally = Math.abs(xUp - xDown) > SWIPED_THRESHOLD;
        final boolean swipedVertically = Math.abs(yUp - yDown) > SWIPED_THRESHOLD;

        if (swipedHorizontally) {
            final boolean swipedRight = xUp > xDown;
            final boolean swipedLeft = xUp < xDown;

            if (swipedRight) {
                swipeListener.onSwipedRight(event);

            }
            if (swipedLeft) {
                swipeListener.onSwipedLeft(event);
            }
        }

        if (swipedVertically) {
            final boolean swipedDown = yDown < yUp;
            final boolean swipedUp = yDown > yUp;
            if (swipedDown) {
                swipeListener.onSwipedDown(event);
            }
            if (swipedUp) {
                swipeListener.onSwipedUp(event);
            }
        }
    }

    private void onActionMove(final MotionEvent event) {
        xMove = event.getX();
        yMove = event.getY();
        final boolean isSwipingHorizontally = Math.abs(xMove - xDown) > SWIPING_THRESHOLD;
        final boolean isSwipingVertically = Math.abs(yMove - yDown) > SWIPING_THRESHOLD;

        if (isSwipingHorizontally) {
            final boolean isSwipingRight = xMove > xDown;
            final boolean isSwipingLeft = xMove < xDown;

            if (isSwipingRight) {
                swipeListener.onSwipingRight(event);
            }
            if (isSwipingLeft) {
                swipeListener.onSwipingLeft(event);
            }
        }

        if (isSwipingVertically) {
            final boolean isSwipingDown = yDown < yMove;
            final boolean isSwipingUp = yDown > yMove;

            if (isSwipingDown) {
                swipeListener.onSwipingDown(event);
            }
            if (isSwipingUp) {
                swipeListener.onSwipingUp(event);
            }
        }
    }

    private void checkNotNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public float getxDown() {
        return xDown;
    }

    public float getxUp() {
        return xUp;
    }

    public float getyDown() {
        return yDown;
    }

    public float getyUp() {
        return yUp;
    }

    public float getxMove() {
        return xMove;
    }

    public float getyMove() {
        return yMove;
    }
}
