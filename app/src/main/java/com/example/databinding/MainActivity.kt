package com.example.databinding

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.database.AppDatabase
import com.example.databinding.databinding.ActivityMainBinding
import com.example.databinding.models.Pokemon
import com.example.databinding.models.User
import com.example.databinding.repo.UserRepository
import com.example.databinding.utils.ApiClient
import com.example.databinding.utils.ListUserAdapter
import com.example.databinding.viewmodel.UserViewModel
import com.example.databinding.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var txtView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModel: UserViewModel by viewModels {
            UserViewModelFactory(((application as UserApplication).repository))
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = userViewModel

        val recyclerView = findViewById<RecyclerView>(R.id.listUsers)
        val adapter = ListUserAdapter({ (u) -> userViewModel.deleteById(u) },
            { (u) -> userViewModel.onSelectUser(u) })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        userViewModel.users.observe(this, Observer { users ->
            // userViewModel.onListChange(users, adapter)
            users?.let {
                adapter.submitList(it)
            }
        })

        userViewModel._userLastName.observe(this, Observer {
            val editTxt = findViewById<EditText>(R.id.editView)
            if (editTxt.text.toString() != it) {
                editTxt.setText(it)
            }
        })

        executePokemonCall()

        /*
        val container = findViewById<LinearLayout>(R.id.layoutContainer)
        val mySnackBar = Snackbar.make(container, R.string.click_btn, LENGTH_SHORT)

        userModel.userLikes.observe(this, Observer {

        })

        val userLastNameObserver = Observer<String> { newLastName ->
            userModel.userFirstName.value = newLastName
            userModel.incrementLike()
            mySnackBar.show()
        }

        userModel.userLastName.observe(this, userLastNameObserver)

         */
    }

    private fun executeCall() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getPosts()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    if (content != null) {
                        var counter = 0
                        for (p in content) {
                            if (counter <= 3) {
                                Toast.makeText(
                                    this@MainActivity, "" +
                                            "post: :${p.title} ${p.body}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                counter++
                            } else {
                                break
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity, "" +
                                "Error occured: ${response.message()}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity, "" +
                            "Error occured: ${e.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun executePokemonCall() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val createResponse = ApiClient.apiPokemonService.createPokemon(
                    Pokemon(99, 777, 2541, "CousiMon", "", listOf("Feuille", "Feu"), Date())
                )

                val response = ApiClient.apiPokemonService.getPokemonById(99)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    if (content != null) {
                        Toast.makeText(
                            this@MainActivity, "" +
                                    "Pokemon: Name->${content.name} HP-> ${content.hp} CP-> ${content.cp}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    val delResponse = ApiClient.apiPokemonService.deletePokemon(99)
                    if (delResponse.isSuccessful && delResponse.body() != null) {
                        val content = response.body()
                        if (content != null) {
                            Toast.makeText(
                                this@MainActivity, "" +
                                        "Pokemon effacer",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity, "" +
                                "Error occured: ${response.message()}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity, "" +
                            "Error occured: ${e.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}