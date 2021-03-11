package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.utils.UpdatedInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_info_moradia.*
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view
import kotlinx.android.synthetic.main.activity_publicar.*

class InfoMoradiaActivity : AppCompatActivity() {
    lateinit var moradia: Moradia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_moradia)

        moradia = intent.getParcelableExtra("moradia")

        supportActionBar?.title= moradia.nomeMoradia
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())

        setViewInfo(moradia)
    }

    private fun setViewInfo(rep: Moradia){
        tipo_moradia_info.text = rep.tipoMoradia
        endereco_moradia_info.text = rep.endereco
        desc_moradia_info.text = rep.desc
        valor_moradia_info.text = rep.valor

        when (rep.preferencia) {
            "Mulheres" -> iv_woman.setBackgroundResource(R.drawable.tv_border_black)
            "Homens" -> iv_man.setBackgroundResource(R.drawable.tv_border_black)
            "Tanto faz" -> iv_tanto_faz.setBackgroundResource(R.drawable.tv_border_black)
        }

        contato_principal_info_moradia.text = UpdatedInfo.usuarioAtual?.contato
        nome_principal_info_moradia.text = UpdatedInfo.usuarioAtual?.nome
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