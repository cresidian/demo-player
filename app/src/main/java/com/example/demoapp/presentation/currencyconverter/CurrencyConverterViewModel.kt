package com.example.demoapp.presentation.currencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.core.Constants
import com.example.demoapp.core.networking.responses.ConversionResponse
import com.example.demoapp.core.networking.responses.CurrenciesResponse
import com.example.demoapp.core.networking.responses.DemoAppBackendError
import com.example.demoapp.core.networking.responses.ResponseReceivedListener
import com.example.demoapp.data.repositories.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
        data class SetCurrencies(val currencies: Map<String, String>) :
            CurrencyConverterViewStates()

        data class SetCurrencyConversions(val conversions: Map<String, Double>) :
            CurrencyConverterViewStates()

        data class ShowError(val message: String) : CurrencyConverterViewStates()
    }

    fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrenciesList(
                Constants.CURRENCY_LAYER_ACCESS_TOKEN,
                object : ResponseReceivedListener<CurrenciesResponse> {
                    override fun onSuccess(response: CurrenciesResponse) {
                        if (response.success) {
                            _viewStates.value =
                                CurrencyConverterViewStates.SetCurrencies(response.currencies)
                        } else {
                            _viewStates.value = CurrencyConverterViewStates.ShowError("Api error")
                        }
                    }

                    override fun onError(errorApp: DemoAppBackendError) {
                        _viewStates.value = CurrencyConverterViewStates.ShowError(errorApp.message)
                    }
                })
        }
    }

    fun getCurrencyConversions(sourceCurrency: String) {
        _viewStates.value = CurrencyConverterViewStates.ShowLoad(true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrencyConversions(
                Constants.CURRENCY_LAYER_ACCESS_TOKEN,
                sourceCurrency,
                object : ResponseReceivedListener<ConversionResponse> {
                    override fun onSuccess(response: ConversionResponse) {
                        _viewStates.value = CurrencyConverterViewStates.ShowLoad(false)
                        if (response.success) {
                            _viewStates.value =
                                CurrencyConverterViewStates.SetCurrencyConversions(response.conversions)
                        } else {
                            _viewStates.value = CurrencyConverterViewStates.ShowError("Api error")
                        }
                    }

                    override fun onError(errorApp: DemoAppBackendError) {
                        _viewStates.value = CurrencyConverterViewStates.ShowLoad(false)
                        _viewStates.value = CurrencyConverterViewStates.ShowError(errorApp.message)
                    }
                })
        }
    }

}