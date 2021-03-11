package androidpro.com.morareach.cadastrologin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidpro.com.morareach.MapaActivity
import androidpro.com.morareach.R
import androidpro.com.morareach.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_login.cadastrar_cadastro

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        voltar_login_cadastro.setOnClickListener {
            finish()
        }

        cadastrar_cadastro.setOnClickListener {
            cadastrarUsuario()
        }
    }

    private fun cadastrarUsuario() {
        val email = email_cadastro.text.toString()
        val senha = senha_login.text.toString()

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Há campos vazios!", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener {
                    salvarUsuarioFirebaseDatabase()
                }
    }

    private fun salvarUsuarioFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        val usuario = Usuario(uid, email_login.text.toString(), null, null)

        ref.setValue(usuario)
            .addOnSuccessListener {
                val intent = Intent(this, MapaActivity:: class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}