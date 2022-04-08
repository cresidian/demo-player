package com.example.demoplayer.ui.currencyconverter

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.demoplayer.R
import com.example.demoplayer.adapters.CurrenciesAdapter
import com.example.demoplayer.base.BaseActivity
import com.example.demoplayer.base.setVisible
import com.example.demoplayer.databinding.ActivityCurrencyConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CurrencyConverterActivity : BaseActivity() {

    private lateinit var bind: ActivityCurrencyConverterBinding
    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCurrencyConverterBinding.inflate(layoutInflater)
        val view = bind.root
        setContentView(view)
        init()
        initViewModelObservers()
    }

    private fun init() {
        bind.rvCurrencies.layoutManager = GridLayoutManager(this, 3)
        bind.etAmount.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getConvertedRates()
                return@OnEditorActionListener true
            }
            false
        })
        viewModel.getCurrencies()
    }

    private fun initViewModelObservers() {
        with(viewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewStates.collect { uiState ->
                        when (uiState) {
                            is CurrencyConverterViewModel.CurrencyConverterViewStates.ShowLoad -> {
                                bind.loaderView.setVisible(uiState.isLoad)
                            }
                            is CurrencyConverterViewModel.CurrencyConverterViewStates.SetCurrencies -> {
                                val currencies = uiState.currencies.keys.toList()
                                setSpinner(currencies)
                            }
                            is CurrencyConverterViewModel.CurrencyConverterViewStates.SetCurrencyConversions -> {
                                bind.noRecordsView.setVisible(uiState.conversions.isEmpty())
                                bind.rvCurrencies.adapter = if (uiState.conversions.isNotEmpty()) {
                                    val conversions = uiState.conversions
                                    CurrenciesAdapter(sourceAmount, conversions)
                                } else {
                                    null
                                }
                            }
                            is CurrencyConverterViewModel.CurrencyConverterViewStates.ShowError -> {
                                showToast(uiState.message)
                                bind.noRecordsView.setVisible(true)
                                bind.rvCurrencies.adapter = null
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setSpinner(currencies: List<String>) {
        val spinnerAdapter = ArrayAdapter(
            this@CurrencyConverterActivity,
            android.R.layout.simple_list_item_1,
            currencies
        )
        bind.spinnerCurrencies.adapter = spinnerAdapter
        bind.spinnerCurrencies.setSelection(currencies.indexOf("USD"), false)
        bind.spinnerCurrencies.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    getConvertedRates()
                }
            }
    }

    private fun getConvertedRates() {
        /** Cannot select other than USD because of free account on CurrencyLayer **/
        val sourceCurrency = "USD" //bind.spinnerCurrencies.selectedItem.toString()
        if (bind.etAmount.text.toString().isNotEmpty()) {
            viewModel.getCurrencyConversions(sourceCurrency)
        } else {
            bind.etAmount.error = getString(R.string.hint_no_amount_error)
        }
    }

    private val sourceAmount:Double get() = bind.etAmount.text.toString().toDouble()

    /*private fun getSourceAmount(): Double {
        val amount = bind.etAmount.text.toString()
        return amount.toDouble()
    }*/

}