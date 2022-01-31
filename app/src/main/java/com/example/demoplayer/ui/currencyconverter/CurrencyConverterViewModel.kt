package com.example.demoplayer.ui.currencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoplayer.base.Constants
import com.example.demoplayer.networking.responses.ConversionResponse
import com.example.demoplayer.networking.responses.CurrenciesResponse
import com.example.demoplayer.networking.responses.DemoBackendError
import com.example.demoplayer.networking.responses.ResponseReceivedListener
import com.example.demoplayer.repositories.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _viewStates: MutableStateFlow<CurrencyConverterViewStates> = MutableStateFlow(
        CurrencyConverterViewStates.Empty
    )
    val viewStates: StateFlow<CurrencyConverterViewStates> = _viewStates

    sealed class CurrencyConverterViewStates {
        object Empty : CurrencyConverterViewStates()
        data class ShowLoad(val isLoad: Boolean) : CurrencyConverterViewStates()
        data class SetCurrencies(val currencies: Map<String,String>) : CurrencyConverterViewStates()
        data class SetCurrencyConversions(val conversions: Map<String,Double>) : CurrencyConverterViewStates()
        data class ShowError(val message: String) : CurrencyConverterViewStates()
    }

    fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrenciesList(Constants.CURRENCY_LAYER_ACCESS_TOKEN, object : ResponseReceivedListener<CurrenciesResponse> {
                override fun onSuccess(response: CurrenciesResponse) {
                    _viewStates.value = CurrencyConverterViewStates.SetCurrencies(response.currencies)
                }

                override fun onError(error: DemoBackendError) {
                    _viewStates.value = CurrencyConverterViewStates.ShowError(error.message)
                }
            })
        }
    }

    fun getCurrencyConversions(sourceCurrency:String) {
        _viewStates.value = CurrencyConverterViewStates.ShowLoad(true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrencyConversions(Constants.CURRENCY_LAYER_ACCESS_TOKEN,sourceCurrency, object : ResponseReceivedListener<ConversionResponse> {
                override fun onSuccess(response: ConversionResponse) {
                    _viewStates.value = CurrencyConverterViewStates.SetCurrencyConversions(response.conversions)
                    _viewStates.value = CurrencyConverterViewStates.ShowLoad(false)
                }

                override fun onError(error: DemoBackendError) {
                    _viewStates.value = CurrencyConverterViewStates.ShowLoad(false)
                    _viewStates.value = CurrencyConverterViewStates.ShowError(error.message)
                }
            })
        }
    }

}