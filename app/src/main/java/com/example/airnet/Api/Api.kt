package com.example.airnet.Api



import com.example.airnet.Model.House.House
import com.example.airnet.Model.Street.Street
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("allStreets")
    suspend fun getStreet(): List<Street>


    @GET("houses")
    suspend fun getHouse(@Query("street_id") street_id:String): List<House>


}
