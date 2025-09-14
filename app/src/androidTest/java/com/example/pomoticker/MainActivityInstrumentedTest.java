package com.example.pomoticker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

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

    @Test
    public void testStartAndPauseToggle() {
        Context context = ApplicationProvider.getApplicationContext();
        String startText = context.getString(R.string.start);
        String pauseText = context.getString(R.string.pause);

        onView(withId(R.id.button_start_pause)).perform(click());
        onView(withId(R.id.button_start_pause)).check(matches(withText(pauseText)));

        onView(withId(R.id.button_start_pause)).perform(click());
        onView(withId(R.id.button_start_pause)).check(matches(withText(startText)));
    }

    @Test
    public void testResetButtonResetsTimer() {
        Context context = ApplicationProvider.getApplicationContext();
        String defaultTime = context.getString(R.string.initial_time_display);

        onView(withId(R.id.button_start_pause)).perform(click());

        onView(withId(R.id.button_reset)).perform(click());

        onView(withId(R.id.text_view_countdown)).check(matches(withText(defaultTime)));
    }

    @Test
    public void testBreakButtonDisabledAtStart() {
        onView(withId(R.id.button_start_break)).check(matches(not(isEnabled())));
    }
}