package com.arttt95.qrvtotal.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.qrvtotal.R
import com.arttt95.qrvtotal.databinding.ActivityCadastroBinding
import com.arttt95.qrvtotal.models.Usuario
import com.arttt95.qrvtotal.utils.exibirMensagem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCadastroBinding.inflate(layoutInflater) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private lateinit var textInputCadastroNome: TextInputLayout
    private lateinit var editTextCadastroNome: TextInputEditText
    private lateinit var textInputCadastroEmail: TextInputLayout
    private lateinit var editTextCadastroEmail: TextInputEditText
    private lateinit var textInputCadastroPassword: TextInputLayout
    private lateinit var editTextCadastroPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarToolbar()
        inicializarComponentes()
        inicializarEventosClick()

    }

    private fun inicializarEventosClick() {

        binding.btnCadastrar.setOnClickListener {
            if (validarCampos()) {
                val nome = editTextCadastroNome.text.toString().trim()
                val email = editTextCadastroEmail.text.toString().trim()
                val password = editTextCadastroPassword.text.toString().trim()
                cadastrarUsuario(nome, email, password)
            }
        }

    }

    private fun cadastrarUsuario(nome: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { resultado ->
                if(resultado.isSuccessful) {
                    val idUsuarioLogado = resultado.result.user?.uid
                    if(idUsuarioLogado != null) {
                        val usuario = Usuario(
                            idUsuarioLogado, nome, email
                        )
                        salvarUsuarioFirestore(usuario)
                    }
                }
            }
            .addOnFailureListener { e ->
                tratarErrosCadastro(e)
                exibirMensagem("Erro ao cadastrar usuário")
            }
    }


    private fun tratarErrosCadastro(e: Exception) {
        try {
            throw e
        } catch (eEmailExistente: FirebaseAuthUserCollisionException) {
            eEmailExistente.printStackTrace()
            exibirMensagem("E-mail já cadastrado")
        } catch (eWeakPassword: FirebaseAuthWeakPasswordException) {
            eWeakPassword.printStackTrace()
            exibirMensagem("A senha precisa conter letras, números e caracteres especiais.")
        } catch (eCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException) {
            eCredenciaisInvalidas.printStackTrace()
            exibirMensagem("O e-mail ou senha inseridos não são válidos")
        } catch (ex: Exception) {
            ex.printStackTrace()
            exibirMensagem("Erro desconhecido: ${ex.message}")
        }
    }

    private fun salvarUsuarioFirestore(usuario: Usuario) {
        firestore.collection("usuarios")
            .document(usuario.id)
            .set(usuario)
            .addOnSuccessListener {
                exibirMensagem("usuário cadastrado com sucesso")
                startActivity(Intent(applicationContext, VehicleListActivity::class.java))
            }.addOnFailureListener { e ->
                e.printStackTrace()
                exibirMensagem("Erro ao cadastrar usuário")
            }
    }

    private fun validarCampos(): Boolean {
        val nome = editTextCadastroNome.text.toString().trim()
        val emailPessoal = editTextCadastroEmail.text.toString().trim()
        val password = editTextCadastroPassword.text.toString().trim()

        var valid = true

        if (nome.isEmpty()) {
            textInputCadastroNome.error = "Preencha o nome"
            valid = false
        } else {
            textInputCadastroNome.error = null
        }

        if (emailPessoal.isEmpty()) {
            textInputCadastroEmail.error = "Preencha o e-mail"
            valid = false
        } else {
            textInputCadastroEmail.error = null
        }

        if (password.isEmpty()) {
            textInputCadastroPassword.error = "Insira a senha"
            valid = false
        } else {
            textInputCadastroPassword.error = null
        }

        return valid
    }

    private fun inicializarComponentes() {

        textInputCadastroNome = binding.textInputCadastroNome
        editTextCadastroNome = binding.editTextCadastroNome
        textInputCadastroEmail = binding.textInputCadastroEmail
        editTextCadastroEmail = binding.editTextCadastroEmail
        textInputCadastroPassword = binding.textInputCadastroPassword
        editTextCadastroPassword = binding.editTextCadstroPassword


    }

    private fun inicializarToolbar() {

        val toolbar = binding.tbPrincipal.tbPrincipal

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Cadastro"
            setDisplayHomeAsUpEnabled(true)
        }

    }
}