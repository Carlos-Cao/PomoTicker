package com.example.pomoticker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);


    @Test
    public void testStartButtonChangesText() {
        Context context = ApplicationProvider.getApplicationContext();
        String pauseText = context.getString(R.string.pause);

        onView(withId(R.id.button_start_pause)).perform(click());

        onView(withId(R.id.button_start_pause)).check(matches(withText(pauseText)));
    }

    @Test
    public void testSessionLabelInitial() {
        Context context = ApplicationProvider.getApplicationContext();
        String initialLabel = context.getString(R.string.work_session);

        onView(withId(R.id.text_view_session_label))
                .check(matches(withText(initialLabel)));
    }
}