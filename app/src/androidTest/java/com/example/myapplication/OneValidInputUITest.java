package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OneValidInputUITest {

    @Rule
    public ActivityScenarioRule<MapsActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MapsActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void oneValidInputUITest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.addLocation), withText("-"),
                        childAtPosition(
                                allOf(withId(R.id.constraint),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                7),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.latitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        editText.perform(replaceText("32"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.longitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        editText2.perform(replaceText("-117"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.custom),
                                        0),
                                2),
                        isDisplayed()));
        editText3.perform(replaceText("input1"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.latitude), withText("32"),
                        withParent(withParent(withId(com.google.android.material.R.id.custom))),
                        isDisplayed()));
        editText4.check(matches(withText("32")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.longitude), withText("-117"),
                        withParent(withParent(withId(com.google.android.material.R.id.custom))),
                        isDisplayed()));
        editText5.check(matches(withText("-117")));

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.name), withText("input1"),
                        withParent(withParent(withId(com.google.android.material.R.id.custom))),
                        isDisplayed()));
        editText6.check(matches(withText("input1")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.custom),
                                        0),
                                3),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.myImageViewText), withText("input1"),
                        withParent(allOf(withId(R.id.layoutTemplate),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView.check(matches(withText("input1")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
