package com.example.demoapp.presentation.currencyconverter

import android.widget.EditText
import androidx.test.rule.ActivityTestRule
import com.example.demoapp.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyConverterActivityTest {

    @Rule
    @JvmField
    val activityTestRule: ActivityTestRule<CurrencyConverterActivity> =
        ActivityTestRule(CurrencyConverterActivity::class.java)

    var activity: CurrencyConverterActivity? = null

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
        val view = activity?.findViewById<EditText>(R.id.etAmount)
        assert(view != null)
    }

    /** Check if the amount input field is not empty **/
    @Test
    fun amount_isNotEmpty() {
        val view = activity?.findViewById<EditText>(R.id.etAmount)
        view?.let {
            assert(it.text.toString().isNotEmpty())
        }
    }

}