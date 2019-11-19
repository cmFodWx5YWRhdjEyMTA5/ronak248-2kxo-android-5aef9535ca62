package com.screamxo.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.labo.kaji.swipeawaydialog.support.v4.SwipeAwayDialogFragment;
import com.screamxo.Adapter.DialogImageAdapter;
import com.screamxo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.labo.kaji.swipeawaydialog.support.v4.SwipeAwayDialogFragment;*/

public class ImageDialogFragment extends SwipeAwayDialogFragment /*DialogFragment*/ {

    private static final String TAG = "ImageDialogFragment";
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    GestureDetector gestureDetector;
    SwipeDismissDialog dialogFragment;
    private Context context;
    private Window.Callback windowCallback = new Window.Callback() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                getDialog().dismiss();
            }
            return false;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            return false;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (gestureDetector != null) {
                return gestureDetector.onTouchEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            return true;
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            return false;
        }

        @Nullable
        @Override
        public View onCreatePanelView(int featureId) {
            return null;
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            return false;
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            return false;
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {

        }

        @Override
        public void onContentChanged() {

        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {

        }

        @Override
        public void onAttachedToWindow() {

        }

        @Override
        public void onDetachedFromWindow() {

        }

        @Override
        public void onPanelClosed(int featureId, Menu menu) {

        }

        @Override
        public boolean onSearchRequested() {
            return false;
        }

        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return null;
        }

        @Override
        public void onActionModeStarted(ActionMode mode) {

        }

        @Override
        public void onActionModeFinished(ActionMode mode) {

        }
    };

    public static ImageDialogFragment newInstance(List<String> images) {
        Bundle args = new Bundle();
        args.putStringArrayList("images", (ArrayList<String>) images);
        args.putInt("position", 0);
        ImageDialogFragment fragment = new ImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageDialogFragment newInstance(List<String> images, int position) {
        Bundle args = new Bundle();
        args.putStringArrayList("images", (ArrayList<String>) images);
        args.putInt("position", position);
        ImageDialogFragment fragment = new ImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_viewer, container);
        //  gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        ButterKnife.bind(this, view);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getDialog().setCancelable(true);
          /*  WindowManager.LayoutParams wlp = window.getAttributes();
            window.setAttributes(wlp);
            getDialog().getWindow().setCallback(windowCallback);*/
        }
        /*view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean eventConsumed = gestureDetector.onTouchEvent(event);
                if (eventConsumed) {
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        // dialogFragment = new SwipeDismissDialog.Builder(context).setLayoutResId(R.layout.dialog_image_viewer).build().show();
        return view;
    }

    @Override
    public boolean onSwipedAway(boolean toTop) {
        try {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ImageDialogFragment.class.getSimpleName());
            if (fragment != null && fragment instanceof ImageDialogFragment) {
                ((ImageDialogFragment) fragment).dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        int position = getArguments().getInt("position");
        List<String> images = getArguments().getStringArrayList("images");
        if (images != null) {
            DialogImageAdapter adapter = new DialogImageAdapter(getChildFragmentManager());
            for (String imageUrl : images) {

                adapter.addFragment(DialogImageFragment.newInstance(imageUrl), DialogImageFragment.class.getSimpleName());

            }
            view_pager.setAdapter(adapter);
            view_pager.setCurrentItem(position);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // swipe right to left
                dismiss();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // swipe left to right
                dismiss();
            } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // top to bottom
                dismiss();
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // bottom to top
                dismiss();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}