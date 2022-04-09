package com.example.demoplayer.presentation.gallery

import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.example.demoplayer.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GalleryActivityTest {

    @Rule
    @JvmField
    val activityTestRule: ActivityTestRule<GalleryActivity> =
        ActivityTestRule(GalleryActivity::class.java)

    var activity: GalleryActivity? = null

    @Before
    fun setUp() {
        activity = activityTestRule.activity
    }

    @After
    fun tearDown() {
        activity = null
    }

    /** Check if the activity is launched **/
    @Test
    fun activity_isLaunched() {
        val view = activity?.findViewById<RecyclerView>(R.id.rvGallery)
        assert(view != null)
    }
}