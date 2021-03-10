package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.models.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editar.*
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view
import kotlinx.android.synthetic.main.activity_publicar.*

class PublicarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicar)

        supportActionBar?.title="Publicar República"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        checarPossuiRepublica()

        botao_publicar.setOnClickListener {
            criarMoradia()
        }

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
                    return@OnNavigationItemSelectedListener false
                }
                R.id.menu_editar -> {
                    val intent = Intent(this, EditarActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }
    }

    private fun checarPossuiRepublica(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarioAtual = snapshot.getValue(Usuario::class.java)

                if(usuarioAtual?.moradiaKey != null) {
                    Toast.makeText(this@PublicarActivity, "Usuário já possui república!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun criarMoradia(){
        val nomeMoradia = nome_moradia_publicar.text.toString()
        val endereco = endereco_moradia_publicar.text.toString()
        val valor = valor_moradia_publicar.text.toString()
        val desc = desc_moradia_publicar.text.toString()

        val pref_id: RadioButton = findViewById(radio_preferencia_publicar.checkedRadioButtonId)
        val tipo_id: RadioButton = findViewById(radio_tipo_moradia_publicar.checkedRadioButtonId)
        val pref = pref_id.text
        val tipo = tipo_id.text

        Log.d("PublicarActivity", "O checked radio é: $pref")

        val ref = FirebaseDatabase.getInstance().getReference("/moradias/$tipo")
        val key = ref.push().key
        val uid = FirebaseAuth.getInstance().uid

        val novaMoradia = Moradia(key!!, nomeMoradia, pref.toString(), endereco, valor, desc, uid!!)
        ref.child(key).setValue(novaMoradia)
                .addOnSuccessListener {
                    finish()
                }

        val refUser = FirebaseDatabase.getInstance().getReference("/usuarios/$uid/moradiaKey")
        refUser.setValue(key)
    }
}