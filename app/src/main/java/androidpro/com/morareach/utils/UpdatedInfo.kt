package androidpro.com.morareach.utils

import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object UpdatedInfo {
    var usuarioAtual: Usuario? = null
    var moradiaAtual: Moradia? = null
}

object UpdateInfo {
    fun setUsuarioAtual() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                UpdatedInfo.usuarioAtual = snapshot.getValue(Usuario::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun setMoradiaAtual() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (moradia in snapshot.children) {
                    val rep = moradia.getValue(Moradia::class.java)

                    if (rep?.usuario == uid)
                        UpdatedInfo.moradiaAtual = rep
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}