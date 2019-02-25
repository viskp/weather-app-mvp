package com.vishal.weatherapp;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class for {@link WeatherActivity}. {@link android.support.test.espresso.Espresso} is
 * doing the instrumentation test.
 *
 * @author Vishal - 25th Feb 2019
 * @since 1.0.0
 */
@RunWith(AndroidJUnit4.class)
public class WeatherActivityTest {
    @Rule
    public ActivityTestRule<WeatherActivity> activityTestRule = new ActivityTestRule<>(
            WeatherActivity.class);

    /**
     * Performs text updating and click listener on search view of {@link WeatherActivity}
     */
    @Test
    public void testSearchBox() {
        onView(withId(R.id.search_city)).perform(ViewActions.clearText());
        onView(withId(R.id.search_city)).perform(ViewActions.typeText("Delhi"));
        onView(withId(R.id.search_city)).perform(new DrawableClickViewAction(
                DrawableClickViewAction.Right));
        onView(withId(R.id.city_name)).check(matches(withText("Delhi")));
    }

    /**
     * Ensures the forecast view is populated.
     */
    @Test
    public void testRecyclerView() {
        onView(withId(R.id.search_city)).perform(new DrawableClickViewAction(
                DrawableClickViewAction.Right));

        onView(nthChildOf(withId(R.id.forecast), 0))
                .check(matches(hasDescendant(withText("Today"))));
        onView(nthChildOf(withId(R.id.forecast), 1))
                .check(matches(hasDescendant(withText("Tomorrow"))));

    }

    /**
     * Returns the view at childPosition of a {@link android.support.v7.widget.RecyclerView}
     *
     * @param parentMatcher view to be matched
     * @param childPosition position of the child
     */
    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher,
                                           final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Position is " + childPosition);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).
                        equals(view);
            }
        };
    }
}