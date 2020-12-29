package com.adnroidapp.cryptoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*


class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }

        val fromSymbol : String = intent.getStringExtra(EXTRA_FROM_SYMBOL).toString()

        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CoinViewModel::class.java)
        viewModel.getDetailInfo(fromSymbol).observe(this, {
            Picasso.get().load(it.getFullImageUrl()).into(ivLogoView)
            tvPrice.text = it.price.toString()
            tvFromSymbol.text = it.fromSymBol
            tvFromMoney.text = it.toSymbol
            tvMinPrice.text = it.lowDay.toString()
            tvMaxPrice.text = it.highDay.toString()
            tvLastMarket.text = it.lastMarket
            tvLastUpdate.text = it.getFormattedTime()
        })
    }

    companion object {
        const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fSymbol: String) : Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fSymbol)
            return intent
        }
    }
}