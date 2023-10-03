package com.example.airnet

import android.app.Application
import androidx.lifecycle.*
import com.example.airnet.Model.House.House
import com.example.airnet.Model.Street.Street


class ViewModel(application: Application) :AndroidViewModel(application){
    val streetList= MutableLiveData<List<Street>>()//Сюда передается новая информация на один день(для все продукции - всего списка)

    val houseList= MutableLiveData<List<House>>()//Сюда передается новая информация на один день(для все продукции - всего списка)


}