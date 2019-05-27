package com.leandrojimenez.tpagatest.model

import android.content.Context
import android.os.Build
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.leandrojimenez.tpagatest.R
import org.json.JSONObject
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

public class TpagaCurrencies {
    private lateinit var currencyDate: Date
    private lateinit var currencyNames: JSONObject

    constructor(){
        currencyDate = Date()
        currencyNames = JSONObject()
    }

    constructor(currencyDate: Date, currencies: JSONObject) {
        this.currencyDate = currencyDate
        this.currencyNames = currencies
    }

    public fun initValues(ctx:Context){

        val nowDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            Calendar.getInstance().getTime()
        }

        if (!currencyDate.equals(nowDate)){
            getCurrencyValues(ctx)
        }
    }

    private fun getCurrencyValues(ctx:Context){
        val queue = Volley.newRequestQueue(ctx)
        val url = ctx.getString(R.string.currency_url)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.

                var jsonCurrencies : JSONObject = JSONObject(response)
                Log.e("Json",response)
                currencyNames = jsonCurrencies.getJSONObject("rates")

            },
            Response.ErrorListener { Log.e("Petition Error","That didn't work!" )})

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    public fun getCurrencyNames() : ArrayList<String> {
        return currencyNames.keys().asSequence().toList() as ArrayList<String>
        //return ArrayList()
    }

    public fun getCurrencyValue(name:String,value: Double):Double
    {
        val currencyValue : Double = currencyNames.getDouble(name)
        return value*currencyValue
    }
}