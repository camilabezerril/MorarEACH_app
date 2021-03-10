package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidpro.com.morareach.models.Moradia
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_info_moradia.*
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view
import kotlinx.android.synthetic.main.activity_publicar.*

class InfoMoradiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_moradia)

        supportActionBar?.title=intent.getStringExtra("moradiaNome")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())

        val moradiaKey = intent.getStringExtra("moradiaKey")
        getMoradiaFromDatabase(moradiaKey)
    }

    private fun getMoradiaFromDatabase(moradiaKey: String){
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")
        val list = listOf("Casa", "Apartamento", "Kitnet")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.forEach {
                    val tipo_moradia = snapshot.child(it).children

                    for (moradia in tipo_moradia) {
                        val rep = moradia.getValue(Moradia::class.java)

                        if (rep?.key == moradiaKey) {
                            tipo_moradia_info.text = it
                            setViewInfo(rep)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setViewInfo(rep: Moradia){
        endereco_moradia_info.text = rep.endereco
        desc_moradia_info.text = rep.desc
        valor_moradia_info.text = rep.valor

        when (rep.preferencia) {
            "Mulheres" -> iv_woman.setBackgroundResource(R.drawable.tv_border_black)
            "Homens" -> iv_man.setBackgroundResource(R.drawable.tv_border_black)
            "Tanto faz" -> iv_tanto_faz.setBackgroundResource(R.drawable.tv_border_black)
        }
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