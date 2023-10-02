package com.example.airnet.Model.Street

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Street(
    val street_id:String,
    val street:String
) 
