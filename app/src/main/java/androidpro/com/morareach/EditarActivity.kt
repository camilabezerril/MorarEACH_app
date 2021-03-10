package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.models.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editar.*
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view

class EditarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        supportActionBar?.title="Editar dados"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        usuarioAtual()
        moradiaAtual()
        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())
    }

    private fun configureMenu(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_mapa -> {
                    val intent = Intent(this, MapaActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_publicar -> {
                    val intent = Intent(this, PublicarActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_editar -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
            false
        }
    }

    private fun usuarioAtual(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarioAtual = snapshot.getValue(Usuario::class.java)
                nome_usuario_editar.text = usuarioAtual?.nome
                nome_usuario_alterar_editar.setText(usuarioAtual?.nome)
                alterar_contato_editar.setText(usuarioAtual?.contato)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun moradiaAtual(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")
        val list = listOf("Casa", "Apartamento", "Kitnet")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var found = false

                list.forEach {
                    val tipo_moradia = snapshot.child(it).children

                    for (moradia in tipo_moradia) {
                        val rep = moradia.getValue(Moradia::class.java)

                        if (rep?.usuario == uid) {
                            nome_moradia_editar.text = rep?.nomeMoradia
                            found = true
                        }
                    }
                }

                if (!found) {
                    nome_moradia_editar.text = "Crie uma república!"
                    alterar_informacoes_editar.visibility = View.INVISIBLE
                    apagar_republica_editar.visibility = View.INVISIBLE
                    switch_procurando_editar.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}