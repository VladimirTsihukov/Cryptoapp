package com.adnroidapp.cryptoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adnroidapp.cryptoapp.R
import com.adnroidapp.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso

class AdapterCoinList(private val context: Context) : RecyclerView.Adapter<HolderCoinList>(){

    private var priceListCoin = listOf<CoinPriceInfo>()
    var onCoinClickListener : OnCoinClickListener? = null

    fun bindListCoin(listCoin: List<CoinPriceInfo>) {
        priceListCoin = listCoin
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCoinList {
        return HolderCoinList(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin_info, parent, false))
    }

    override fun onBindViewHolder(holder: HolderCoinList, position: Int) {
        holder.onBind(priceListCoin[position], context)
        holder.itemView.setOnClickListener {
            onCoinClickListener?.coinClick(priceListCoin[position])
        }
    }

    override fun getItemCount(): Int {
        return priceListCoin.size
    }

    interface OnCoinClickListener {
        fun coinClick(coinPriceInfo : CoinPriceInfo)
    }
}

class HolderCoinList(item: View) : RecyclerView.ViewHolder(item) {
    private val imageView = item.findViewById<ImageView>(R.id.imgLogoCoin)
    private val tvSymbol = item.findViewById<TextView>(R.id.tvSymbols)
    private val tvCost = item.findViewById<TextView>(R.id.tvCost)
    private val tvTimeUpdate = item.findViewById<TextView>(R.id.tvTimeUpdate)

    fun onBind (coin: CoinPriceInfo, context: Context) {
        with(coin) {
            Picasso.get().load(getFullImageUrl()).into(imageView)
            tvSymbol.text = String.format(context.resources.getString(R.string.symbols_template), fromSymBol, toSymbol)
            tvCost.text = String.format("%.3f", price)
            tvTimeUpdate.text = String.format(context.resources.getString(R.string.last_update_template), getFormattedTime())
        }
    }
}
