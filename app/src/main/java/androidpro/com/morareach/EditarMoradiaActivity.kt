package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidpro.com.morareach.utils.UpdatedInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_editar_moradia.*
import kotlinx.android.synthetic.main.activity_editar_moradia.homens_radio
import kotlinx.android.synthetic.main.activity_editar_moradia.mulheres_radio
import kotlinx.android.synthetic.main.activity_editar_moradia.tanto_faz_radio
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view
import kotlinx.android.synthetic.main.activity_publicar.*

class EditarMoradiaActivity : AppCompatActivity() {

    val moradia = UpdatedInfo.moradiaAtual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_moradia)

        supportActionBar?.title="Editar ${moradia?.nomeMoradia}"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())

        nome_moradia_editar_moradia.setText(moradia?.nomeMoradia)
        desc_moradia_editar_moradia.setText(moradia?.desc)
        valor_moradia_editar_moradia.setText(moradia?.valor)

        when (moradia?.preferencia) {
            "Mulheres" -> mulheres_radio.isChecked = true
            "Homens" -> homens_radio.isChecked = true
            "Tanto faz" -> tanto_faz_radio.isChecked = true
        }

        botao_salvar_editar_moradia.setOnClickListener {
            moradia?.nomeMoradia = nome_moradia_editar_moradia.text.toString()
            moradia?.desc = desc_moradia_editar_moradia.text.toString()
            moradia?.valor = valor_moradia_editar_moradia.text.toString()

            val pref_id: RadioButton = findViewById(radio_preferencia_publicar.checkedRadioButtonId)
            val pref = pref_id.text

            moradia?.preferencia = pref.toString()

            val ref = FirebaseDatabase.getInstance().getReference("/moradias/")
            ref.child(moradia!!.key).setValue(moradia)
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