package com.example.demoplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoplayer.databinding.ListItemCurrencyBinding

class CurrenciesAdapter(
    private val sourceAmount: Double = 0.0,
    private val conversions: Map<String, Double>
) :
    RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CurrenciesViewHolder {
        val binding =
            ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrenciesViewHolder(binding)
    }

    override fun getItemCount() = conversions.size

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        with(holder) {
            val currency = conversions.keys.elementAt(position)
            val currencyRate = conversions.getValue(currency)
            binding.tvCurrency.text = currency.drop(3)
            binding.tvAmount.text = (currencyRate * sourceAmount).toString()
        }
    }

    inner class CurrenciesViewHolder(val binding: ListItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root)
}