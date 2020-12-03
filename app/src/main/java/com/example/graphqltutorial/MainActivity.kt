package com.example.graphqltutorial

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import example.CountriesQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apolloClient = ApolloClient.builder()
                .serverUrl("https://countries-274616.ew.r.appspot.com/")
                .build()

        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                apolloClient.query(CountriesQuery()).await()
            } catch (e: ApolloException) {
                // handle protocol errors
                Log.i("qwerty", e.toString())

                return@launch
            }

            val launch = response.data?.country
            Log.i("qwerty", launch.toString())
            if (launch == null || response.hasErrors()) {
                // handle application errors
                return@launch
            }
            withContext(Dispatchers.Main) {
                // launch now contains a type safe model of your data
                findViewById<TextView>(R.id.text).text = launch.size.toString()
            }
        }
    }
}