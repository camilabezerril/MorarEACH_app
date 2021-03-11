package androidpro.com.morareach.cadastrologin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidpro.com.morareach.MapaActivity
import androidpro.com.morareach.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        if(FirebaseAuth.getInstance().uid != null) {
            // Persistência de Login mesmo com aplicativo fechando -> pular LoginActivity
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }

        cadastrar_cadastro.setOnClickListener {
            val email = email_login.text.toString()
            val senha = senha_login.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                val intent = Intent(this, MapaActivity::class.java)
                startActivity(intent)
            }
        }

        cadastrar_login.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        login_anonimo.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }
    }
}