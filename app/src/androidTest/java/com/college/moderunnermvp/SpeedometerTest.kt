package com.college.moderunnermvp

import CountingIdlingResourceSingleton
import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.doubleClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule

import com.google.common.truth.Truth
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SpeedometerTest {




    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    @Test
   fun testFragments() {
       val activityScenario = ActivityScenario.launch(Speedometer::class.java)

        // Get the Activiity
        activityScenario.onActivity {
            var activity =  R.id.activityContainer
            //Assert Not Null
            assertNotNull(activity)
            var fragment = FragmentHome()
            // Check the fragment can be accessed when added
            it.supportFragmentManager.beginTransaction().add(activity, fragment)
                .addToBackStack(null).commit()
            //Asseert that the large button on hime screen is shown
            assertNotNull(R.id.button_go)
            val view: View =
                fragment.view?.findViewById(R.id.home_container) ?: View(it.applicationContext)
            //Assert the hime container is shown
            assertNotNull(view)
        }
        //Cycle the bottom navbar
        onView(withId(R.id.nav_home)).perform(click())
        Truth.assertThat(R.id.home_container).isNotNull()
        onView(withId(R.id.home_container)).check(matches(isDisplayed()))
        // Re assert that the button is shown
        onView(withId(R.id.home_container)).check(matches(isDisplayed()))
            //Perform Click on the button
        onView(withId(R.id.button_go)).perform(click())
        //Back to home
        pressBack()
        onView(withId(R.id.nav_settings)).perform(click())
   }

    @Test
    fun testFragments2() {
        val activityScenario = ActivityScenario.launch(Speedometer::class.java)

        // Get the Activiity

        //Cycle the bottom navbar
        //first: hove_home
        onView(withId(R.id.nav_home)).perform(click())
        Truth.assertThat(R.id.home_container).isNotNull()
        onView(withId(R.id.home_container)).check(matches(isDisplayed()))
        // Re assert that the button is shown
        onView(withId(R.id.home_container)).check(matches(isDisplayed()))
        //Go to settings
        onView(withId(R.id.nav_settings)).perform(click())
        Truth.assertThat(R.id.home_container).isNotNull()
        //Home
        onView(withId(R.id.nav_home)).perform(click())
        //HeartRate (Disabled)
        onView(withId(R.id.nav_heart)).perform(click())
        //Settings
        onView(withId(R.id.nav_settings)).perform(click())
        //Time Tracker
        onView(withId(R.id.nav_speed)).perform(click())
        //Home
        onView(withId(R.id.nav_home)).perform(click())
        onView(withId(R.id.home_container)).check(matches(isDisplayed()))
        //Top Tab Bar
        onView(withId(R.id.tab_layout)).perform(click())
        //Settings
        onView(withId(R.id.nav_settings)).perform(click())
        //Time Tracker
        onView(withId(R.id.nav_speed)).perform(click())
        // Home
        onView(withId(R.id.nav_home)).perform(click())
        // Layout x3
        onView(withId(R.id.tab_layout)).perform(click())
        onView(withId(R.id.tab_layout)).perform(click())
        onView(withId(R.id.tab_layout)).perform(click())
        //Time Tracker
        onView(withId(R.id.nav_speed)).perform(click())
    }
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }
}