package com.app.tuskan.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.app.tuskan.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Yakup on 29.7.2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeClickTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeItem(){

        onView(ViewMatchers.withId(R.id.recipe_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));

    }
}
