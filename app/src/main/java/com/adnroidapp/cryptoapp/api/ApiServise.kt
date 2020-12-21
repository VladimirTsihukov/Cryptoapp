package com.adnroidapp.cryptoapp.api

import com.adnroidapp.cryptoapp.pojo.CoinInfoListOfData
import com.adnroidapp.cryptoapp.pojo.CoinPriceInfoRawData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top/totalvolfull")
    fun getTopCoinInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL_MONEY) symbolMoney: String = CURRENCY
    ): Single<CoinInfoListOfData>

    @GET("pricemultifull")
    fun getFullPriceCoins(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_TO_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS_MONEYS) symbolMoney: String = CURRENCY
    ): Single<CoinPriceInfoRawData>


    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val API_KEY = "2bd9ed0af351b986c1953edb7733d289777ae20841150eac3155d3e80037c746"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL_MONEY = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS_MONEYS = "tsyms"
        private const val QUERY_PARAM_TO_SYMBOLS = "fsyms"

        private const val CURRENCY = "USD"
    }
}