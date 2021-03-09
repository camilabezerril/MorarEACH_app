package androidpro.com.morareach

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidpro.com.morareach.models.Usuario
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
    }

    private fun usuarioAtual(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarioAtual = snapshot.getValue(Usuario::class.java)
                nome_usuario_editar.setText(usuarioAtual?.nome)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}