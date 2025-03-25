package com.arttt95.qrvtotal.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.qrvtotal.R
import com.arttt95.qrvtotal.databinding.ActivityLoginBinding
import com.arttt95.qrvtotal.utils.exibirMensagem
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inicializarToolbar()

        binding.textCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    exibirMensagem("Login realizado com sucesso!")
                    goToVehicleList()
                }
                .addOnFailureListener { e ->
                    exibirMensagem("Falha no login: ${e.message}")
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, VehicleListActivity::class.java))
            finish()
        }
    }

    private fun goToVehicleList() {
        startActivity(Intent(this, VehicleListActivity::class.java))
        finish()
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToolbar.tbPrincipal

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Login"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
