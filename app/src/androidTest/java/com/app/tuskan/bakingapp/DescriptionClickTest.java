package com.app.tuskan.bakingapp;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.app.tuskan.bakingapp.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Yakup on 29.7.2018.
 */
@RunWith(AndroidJUnit4.class)
public class DescriptionClickTest {
    public static final String SHORT_DESCRIPTION = "Recipe Introduction";

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void stubAllExternalIntents() {

        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void onClick() {
        onView(ViewMatchers.withId(R.id.recipe_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(ViewMatchers.withId(R.id.description_fragment_list_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText(SHORT_DESCRIPTION)).check(matches(isDisplayed()));

    }

}
