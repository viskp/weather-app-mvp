package com.vishal.weatherapp;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Matcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static org.hamcrest.Matchers.allOf;

/**
 * Custom {@link ViewAction} that detects a drawable(left, top, right, bottom) on any EditText.
 *
 * @author Vishal - 25th Feb 2019
 * @since 1.0.0
 */
public class DrawableClickViewAction implements ViewAction {
    public static final int Left = 0;
    public static final int Top = 1;
    public static final int Right = 2;
    public static final int Bottom = 3;

    @Location
    private final int drawableLocation;

    public DrawableClickViewAction(@Location int drawableLocation) {
        this.drawableLocation = drawableLocation;
    }

    @Override
    public Matcher<View> getConstraints() {
        return allOf(isAssignableFrom(EditText.class), new BoundedMatcher<View, EditText>(
                EditText.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has drawable");
            }

            @Override
            protected boolean matchesSafely(final EditText editText) {
                return editText.requestFocusFromTouch() && editText.getCompoundDrawables()[
                        drawableLocation] != null;

            }
        });
    }

    @Override
    public String getDescription() {
        return "click drawable ";
    }

    @Override
    public void perform(final UiController uiController, final View view) {
        EditText tv = (EditText) view;
        if (tv != null && tv.requestFocusFromTouch()) {
            Rect drawableBounds = tv.getCompoundDrawables()[drawableLocation].getBounds();

            final Point[] clickPoint = new Point[4];
            clickPoint[Left] = new Point(tv.getLeft() + (drawableBounds.width() / 2), (int)
                    (tv.getPivotY() + (drawableBounds.height() / 2)));
            clickPoint[Top] = new Point((int) (tv.getPivotX() + (drawableBounds.width() / 2)),
                    tv.getTop() + (drawableBounds.height() / 2));
            clickPoint[Right] = new Point(tv.getRight() + (drawableBounds.width() / 2), (int)
                    (tv.getPivotY() + (drawableBounds.height() / 2)));
            clickPoint[Bottom] = new Point((int) (tv.getPivotX() + (drawableBounds.width() / 2)),
                    tv.getBottom() + (drawableBounds.height() / 2));

            if (tv.dispatchTouchEvent(MotionEvent.obtain(android.os.SystemClock.uptimeMillis(),
                    android.os.SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, clickPoint[
                            drawableLocation].x, clickPoint[drawableLocation].y, 0)))
                tv.dispatchTouchEvent(MotionEvent.obtain(android.os.SystemClock.uptimeMillis(),
                        android.os.SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, clickPoint[
                                drawableLocation].x, clickPoint[drawableLocation].y, 0));
        }
    }

    @IntDef({Left, Top, Right, Bottom})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Location {
    }
}