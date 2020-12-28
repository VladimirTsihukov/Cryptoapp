package com.adnroidapp.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.adnroidapp.cryptoapp.api.ApiFactory
import com.adnroidapp.cryptoapp.database.AppDatabase
import com.adnroidapp.cryptoapp.pojo.CoinPriceInfo
import com.adnroidapp.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private final val TAG = "TEST_OF_LOADING_DATA"
    private val db = AppDatabase.getInstance(application)       //создаем базу данных

    val priceList = db.coinPriceInfoDao().getPriceList()        //создаем объект LiveDate, при подписки в Activity все изм. будут отображаться
    private val compositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo > {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    private fun loadData() {                //метод получение данных из сети
        val disposable = ApiFactory.apiService.getTopCoinInfo()     //загрузка популярных валют
            .map { listDatum ->
                listDatum.data?.map { listCoinIfo -> listCoinIfo.coinInfo?.name }?.joinToString(",")
            } //групируюм в строку имена популярных валют
            .flatMap { ApiFactory.apiService.getFullPriceCoins(fSyms = it) }    //загруска информации популярных валют
            .map { getPriceListFromRawData(it) }
            .delaySubscription(5, TimeUnit.SECONDS)                        //загрузка данных в установленные по времени таймер
            .repeat()                                                           //повторяет загрузку(но при ошибке не будет возобновлено скачивание)
            .retry()                                                            //повторно выполнит загрузку если все произошло не успешно
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d(TAG, "Success: $it")
            }, {
                Log.d(TAG, "Failure: ${it.message.toString()}")
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData)
            : List<CoinPriceInfo> {
        val listResult = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return listResult
        val coinKeySet = jsonObject.keySet()       //получаем список ключей
        for (coinKey in coinKeySet) {                           // список на биткоины
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeyJson =
                currencyJson.keySet() //получаем список ключей у следующего объекта
            for (key in currencyKeyJson) {                      //список на валюту данного биткоина
                val priceInfo =
                    Gson().fromJson(currencyJson.getAsJsonObject(key), CoinPriceInfo::class.java)
                listResult.add(priceInfo)
            }
        }
        return listResult
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}