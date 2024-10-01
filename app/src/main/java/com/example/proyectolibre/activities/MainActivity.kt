package com.example.proyectolibre.activities

import MarvelApiService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectolibre.R
import com.example.proyectolibre.adapter.CharacterAdapter
import com.example.proyectolibre.adapter.MarvelApiUtil
import com.example.proyectolibre.comunication.MarvelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: MarvelApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextSearch: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        editTextSearch = findViewById(R.id.editTextSearch)
        searchButton = findViewById(R.id.button)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://developer.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(MarvelApiService::class.java)

        searchButton.setOnClickListener {
            val characterName = editTextSearch.text.toString().trim()
            if (characterName.isNotEmpty()) {
                searchCharacters(characterName)
            } else {
                Toast.makeText(this, "Por favor, ingresa un nombre de personaje", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchCharacters(characterName: String) {
        val apiKey = "ba41a2c4a5898d7d6de2057044f1f30c" // Coloca tu API key aquí
        val timestamp = (Date().time / 1000).toString()
        val hash = MarvelApiUtil.generateHash(timestamp)

        val call = apiService.searchCharacters(characterName, apiKey, timestamp, hash)
        call.enqueue(object : Callback<MarvelResponse> {
            override fun onResponse(call: Call<MarvelResponse>, response: Response<MarvelResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val characters = response.body()!!.data.results
                    if (characters.isNotEmpty()) {
                        recyclerView.adapter = CharacterAdapter(characters)
                    } else {
                        Toast.makeText(this@MainActivity, "No se encontraron personajes", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<MarvelResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}