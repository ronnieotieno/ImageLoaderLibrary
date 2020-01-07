package dev.ronnieotieno.imageloader

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dev.ronnieotieno.imageloader.adapter.ImagesAdapter
import dev.ronnieotieno.imageloader.ui.ImagesActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {

    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(ImagesActivity::class.java)

    @Before
    fun getData() {
        activityActivityTestRule.activity.imagesViewModel.init()


        CoroutineScope(Dispatchers.Main).launch {

            activityActivityTestRule.activity.observeAndSetImagesToRecyclerview()

        }

    }

    @Test
    fun testCaseForRecyclerViewClick() {

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ImagesAdapter.MyViewHolder>(
                    0,
                    click()
                )
            )
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dev.ronnieotieno.imageloader", appContext.packageName)
    }
}
