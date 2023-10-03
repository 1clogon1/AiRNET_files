package com.example.airnet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
//import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.airnet.Api.Api
import com.example.airnet.Model.AddAdres.Adres
import com.example.airnet.Model.House.House
import com.example.airnet.Model.Street.Street
import com.example.airnet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    var houseNumberList = arrayListOf<String>("Выберите дом")
    private lateinit var binding: ActivityMainBinding
    var streetListModel = arrayListOf<Street>()
    var streetList = arrayListOf<String>()

    var houseListModel = arrayListOf<House>()
    var houseList = arrayListOf<String>()

    var Adres = Adres("","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchHouse()


        binding.btnSend.setOnClickListener{
            Adres.street = ""
            Adres.house = ""
            Adres.courpus = ""
            Adres.flat = ""
            if(binding.searchAdres.text.toString() !=""){
                val street = streetListModel.find { it.street == binding.searchAdres.text.toString() }
                if(street !=null){
                    val House = houseListModel.find { it.house == binding.spinner2.selectedItem.toString() }
                    if(House !=null){
                        if (binding.edFlat.text.toString()!="" && binding.edFlat.text.toString()!="0"){
                            Adres.street = street.street_id
                            Adres.house = House.house_id
                            Adres.flat = binding.edFlat.text.toString()
                            Toast.makeText(this,"ID улицы - ${Adres.street}, ID дома - ${Adres.house}, квартира - ${Adres.flat}",Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        if ((binding.edFlat.text.toString()!="" && binding.edFlat.text.toString()!="0" )&&binding.edCorpus.text.toString()!="" && binding.edCorpus.text.toString()!="0" ){
                            Adres.street = street.street_id
                            Adres.house =  binding.edHouse.text.toString()
                            Adres.courpus =  binding.edCorpus.text.toString()
                            Adres.flat = binding.edFlat.text.toString()
                            Toast.makeText(this,"ID улицы - ${Adres.street}, дом - ${Adres.house}, корпус - ${Adres.courpus} квартира - ${Adres.flat}",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Adres.street = binding.searchAdres.text.toString()
                    Adres.house =  binding.edHouse.text.toString()
                    Adres.courpus =  binding.edCorpus.text.toString()
                    Adres.flat = binding.edFlat.text.toString()
                    Toast.makeText(this,"улица - ${Adres.street}, дом - ${Adres.house}, корпус - ${Adres.courpus} квартира - ${Adres.flat}",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Инициализация первой строки
    private fun searchHouse() {
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getAllStreet())

        //languages += getAllStreet()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getAllStreet())
        binding.searchAdres.threshold = 3
        binding.searchAdres.setAdapter(adapter)


        binding.searchAdres.setOnItemClickListener { parent, arg1, pos, id ->
            binding.spinner2.visibility = View.VISIBLE
            binding.cvHouse.visibility = View.VISIBLE
            binding.cvHousing.visibility = View.VISIBLE
            val street = streetListModel.find { it.street == binding.searchAdres.text.toString() }

            houseNumberList.clear()
            houseListModel.clear()
            houseList.clear()
            if (street != null) {
                Adres.street = street.street_id
                getAllHouse(street.street_id)
            }
        }
    }


    //Добавление спинера
    fun spinner(streats: ArrayList<String>)=with(binding) {

        val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, streats)
        spinner2.adapter = arrayAdapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 != 0){
                    cvHouse.visibility = View.GONE
                    cvHousing.visibility = View.GONE
                    edHouse.text = null
                    edCorpus.text = null
                    Adres.house = ""
                    Adres.courpus = ""

                }
                else{
                    cvHouse.visibility = View.VISIBLE
                    cvHousing.visibility = View.VISIBLE
                    edHouse.text = null
                    edCorpus.text = null
                    Adres.house = ""
                    Adres.courpus = ""
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("2222", "2222")

            }
        }
    }


    //Получение домов
    private fun getAllHouse(street_id: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://stat-api.airnet.ru/v2/utils/get/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(Api::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val house = retrofitAPI.getHouse(street_id)

            Log.i("sdasd", house.count().toString())
            Log.i("sdasd", house.toString())
            houseList.add("Выберите дом")
            runOnUiThread {
                for (i in 0..house.count() - 1) {
                    houseListModel.add(House(house[i].house_id, house[i].house))
                    houseList.add(house[i].house)
                }
                spinner(houseList)
                Log.i("houseListModel", houseListModel.toString())
                Log.i("houseList", houseList.toString())
            }
        }
    }

    private fun getAllStreet():ArrayList<String> {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://stat-api.airnet.ru/v2/utils/get/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(Api::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val street = retrofitAPI.getStreet()

            runOnUiThread {
                for (i in 0..street.count() - 1) {
                    streetListModel.add(Street(street[i].street_id, street[i].street))
                    streetList.add(street[i].street)
                }
                Log.i("streetListModel", streetListModel.toString())
                Log.i("streetList", streetList.toString())
            }
        }

        return streetList
    }


}
