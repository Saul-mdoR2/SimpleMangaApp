package com.example.simplemangaapp.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Network(private var activity: AppCompatActivity) {

    private fun hayRed(): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(networkInfo)

        return if (capabilities != null) {
            networkInfo != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ))
        } else false
    }

    fun httpRequest(url: String, context: Context, httpResponse: HttpResponse) {
        if (hayRed()) {
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    httpResponse.httpRespuestaExitosa(response)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        activity,
                        "Error al realizar la solicitud HTTP",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            queue.add(request)
        }
    }
}