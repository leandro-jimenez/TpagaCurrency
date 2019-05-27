package com.leandrojimenez.tpagatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.leandrojimenez.tpagatest.model.TpagaCurrencies
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var currencies: TpagaCurrencies
    lateinit var sp_curr_names: Spinner
    lateinit var et_base_value: EditText
    lateinit var tv_result_value: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initInterface()
        currencies = TpagaCurrencies()
        initCurrencies()
        //currencies.initValues(this)
    }

    private fun initInterface(){
        sp_curr_names = this.sp_currency_name
        et_base_value = this.et_base_currency
        tv_result_value = this.tv_final_result
        sp_curr_names!!.setOnItemSelectedListener(this)
    }
    private fun initCurrencies(){
        val queue = Volley.newRequestQueue(this)
        val url = getString(R.string.currency_url)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.

                var jsonCurrencies : JSONObject = JSONObject(response)
                Log.e("Json",response)
                val nowDate = Calendar.getInstance().getTime()/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.now()
                } else {
                    Calendar.getInstance().getTime()
                }*/
                currencies = TpagaCurrencies( nowDate ,jsonCurrencies.getJSONObject("rates"))
                val spinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,currencies.getCurrencyNames())
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_curr_names!!.setAdapter(spinnerAdapter)
            },
            Response.ErrorListener { Log.e("Petition Error","That didn't work!" )})

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val name: String = currencies.getCurrencyNames().get(position)
        val selectedCurrency: Double = et_base_value.toString().toDouble()
        Log.d("selected",currencies.getCurrencyValue(name,selectedCurrency).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
