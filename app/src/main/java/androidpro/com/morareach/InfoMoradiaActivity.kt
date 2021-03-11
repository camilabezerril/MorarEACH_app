package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.models.Usuario
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_info_moradia.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view

class InfoMoradiaActivity : AppCompatActivity() {
    companion object {
        var moradia: Moradia? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_moradia)

        val moradiaKey = intent.getStringExtra("moradiaKey")
        searchMoradia(moradiaKey)

        supportActionBar?.title= moradia?.nomeMoradia
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())

        setViewInfo()
    }

    private fun searchMoradia(moradiaKey: String){
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/$moradiaKey")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                moradia = snapshot.getValue(Moradia::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setViewInfo(){
        tipo_moradia_info.text = moradia?.tipoMoradia
        endereco_moradia_info.text = moradia?.endereco
        desc_moradia_info.text = moradia?.desc
        valor_moradia_info.text = moradia?.valor

        when (moradia?.preferencia) {
            "Mulheres" -> iv_woman.setBackgroundResource(R.drawable.tv_border_black)
            "Homens" -> iv_man.setBackgroundResource(R.drawable.tv_border_black)
            "Tanto faz" -> iv_tanto_faz.setBackgroundResource(R.drawable.tv_border_black)
        }

        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/${moradia?.usuario}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val usuario = snapshot.getValue(Usuario::class.java)

                contato_principal_info_moradia.text = usuario?.contato
                nome_principal_info_moradia.text = usuario?.nome
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
                    val intent = Intent(this, EditarActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }
    }
}