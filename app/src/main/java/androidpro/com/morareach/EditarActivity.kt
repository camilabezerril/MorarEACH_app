package androidpro.com.morareach

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidpro.com.morareach.utils.UpdatedInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        setUserViewInfo()
        setMoradiaViewInfo()
        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())

        apagar_republica_editar.setOnClickListener {
            apagarRepublica()
        }

        switch_procurando_editar.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                setMoradiaProcuraTrue()
            else
                setMoradiaProcuraFalse()
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
                    return@OnNavigationItemSelectedListener false
                }
            }
            false
        }
    }

    private fun setUserViewInfo(){
        val usuario = UpdatedInfo.usuarioAtual

        nome_usuario_editar.text = usuario?.nome
        nome_usuario_alterar_editar.setText(usuario?.nome)
        alterar_contato_editar.setText(usuario?.contato)
    }

    private fun setMoradiaViewInfo(){
        val moradia = UpdatedInfo.moradiaAtual

        if(moradia != null) {
            nome_moradia_editar.text = moradia.nomeMoradia
            switch_procurando_editar.isChecked = moradia.procura
        } else {
            nome_moradia_editar.text = "Crie uma república!"
            alterar_informacoes_editar.visibility = View.INVISIBLE
            apagar_republica_editar.visibility = View.INVISIBLE
            switch_procurando_editar.visibility = View.INVISIBLE
        }
    }

    private fun apagarRepublica(){
        val moradia = UpdatedInfo.moradiaAtual
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/${moradia?.key}")
        ref.removeValue().addOnSuccessListener {
            UpdatedInfo.moradiaAtual = null

            Toast.makeText(this, "República apagada!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditarActivity::class.java)
            overridePendingTransition(0, 0)
            startActivity(intent)
        }
    }

    private fun setMoradiaProcuraFalse(){
        val moradia = UpdatedInfo.moradiaAtual
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/${moradia?.key}/procura")
        ref.setValue(false).addOnSuccessListener {
            Toast.makeText(this, "Procura desativada!\nA república não aparecerá mais no mapa.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMoradiaProcuraTrue(){
        val moradia = UpdatedInfo.moradiaAtual
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/${moradia?.key}/procura")
        ref.setValue(true).addOnSuccessListener {
            Toast.makeText(this, "Procura ativada!\nA república aparecerá no mapa.", Toast.LENGTH_SHORT).show()
        }
    }
}