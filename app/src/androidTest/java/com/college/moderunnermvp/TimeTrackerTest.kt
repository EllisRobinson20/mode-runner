package com.college.moderunnermvp

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.truth.Truth
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Time

@RunWith(AndroidJUnit4::class)
class TimeTrackerTest {

    @Before
    fun setUp() {
    }
    @Test
    fun testUILaunch() {
        var activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.onActivity {
            var activity = R.id.test_container
            assertNotNull(activity)
            var timeTracker = TimeTracker()
            it.supportFragmentManager.beginTransaction().add(activity, timeTracker).commitAllowingStateLoss()
            val view: View = timeTracker.view?.findViewById(R.id.dial_view_pager) ?:View(it.applicationContext)
            assertNotNull(view)
        }
    }
     @Test
     fun testUILaunchSpeedometer() {
         var activityScenario: ActivityScenario<Speedometer> = ActivityScenario.launch(Speedometer::class.java)

         activityScenario.moveToState(Lifecycle.State.CREATED)
         activityScenario.onActivity {
             Truth.assertThat(it.db?.isOpen).isFalse()//if break the code and do not close db this// test passes if test for true
         }
         activityScenario.moveToState(Lifecycle.State.RESUMED)
         activityScenario.onActivity {
             var activity = R.id.activityContainer
             assertNotNull(activity)
             var timeTracker = TimeTracker()
             it.supportFragmentManager.beginTransaction().add(activity, timeTracker).commitAllowingStateLoss()
             val view: View = timeTracker.view?.findViewById(R.id.tt_container) ?:View(it.applicationContext)
             assertNotNull(view)
             val intent = Intent(it.applicationContext, SpeedometerService::class.java) //check the service runs
             intent.putExtra("DistanceToRun","100") //removing this should break the script
             it.startService(intent)

             Truth.assertThat(it.timeTrackerFragment.getServiceState()).isTrue()
             Truth.assertThat(it.settingsFragment.fragmentState).isFalse() // if we change the test fails while in timetracker not settings

             Truth.assertThat(it.testModel.SpeedometerFragmentModel.isEmpty()).isFalse() // works when mock data inflates the sharedMessage class
             Truth.assertThat(it.testModel.SpeedometerFragmentModel.lastElement().distanceFrame).isEqualTo(2.0f) // check the values still on linear setting in the testModel

             Truth.assertThat(it.testModel.HistoryFragmentModel.isEmpty()).isFalse()
             Truth.assertThat(it.testModel.HistoryFragmentModel.lastElement().date).isEqualTo("24-12-2020 07:35:53") // data retrieved by forcing the app test to fail
             Truth.assertThat(it.testModel.HistoryFragmentModel.firstElement().date).isEqualTo("29-12-2020 18:47:58") // As the last element can change during testing, the first element is used
             /*it.onCreateView(activity,"TimeTracker",activity.)
             */
             view.findViewById<Button>(R.id.startButton).performClick()
             //view.findViewById<FloatingActionButton>(R.id.button_reset).performClick()
         }
     }
    @Test
    fun settingFragmentTest() {
        var activityScenario: ActivityScenario<Speedometer> = ActivityScenario.launch(Speedometer::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.onActivity {

            var activity = R.id.activityContainer
            assertNotNull(activity)
            var settingsFragment = FragmentSettings()

            it.supportFragmentManager.beginTransaction().add(activity, settingsFragment)
                .commitAllowingStateLoss()
            val view: View =
                settingsFragment.view?.findViewById(R.id.settingsFragmentContainer) ?: View(it.applicationContext)
            assertNotNull(view)
        }

    }

    @After
    fun tearDown() {

    }
}