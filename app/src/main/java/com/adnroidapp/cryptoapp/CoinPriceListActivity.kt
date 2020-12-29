package com.adnroidapp.cryptoapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.adnroidapp.cryptoapp.adapter.AdapterCoinList
import com.adnroidapp.cryptoapp.pojo.CoinPriceInfo

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel
    private val recycler: RecyclerView by lazy {
        findViewById(R.id.rvCoinPriceList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)
        val adapter = AdapterCoinList(this)
        recycler.adapter = adapter

        adapter.onCoinClickListener = object : AdapterCoinList.OnCoinClickListener {
            override fun coinClick(coinPriceInfo: CoinPriceInfo) {
                val intent = CoinDetailActivity.newIntent(this@CoinPriceListActivity, coinPriceInfo.fromSymBol)
                startActivity(intent)
            }
        }

        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CoinViewModel::class.java)

        viewModel.priceList.observe(this, {
            adapter.bindListCoin(it)
        })
    }
}