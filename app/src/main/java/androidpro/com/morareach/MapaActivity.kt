package androidpro.com.morareach


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidpro.com.morareach.apicalls.*
import androidpro.com.morareach.models.Moradia
import androidpro.com.morareach.utils.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_editar.*
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlinx.android.synthetic.main.activity_mapa.bottom_nav_view

class MapaActivity : AppCompatActivity (), OnMapReadyCallback{

    private var verificado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        verificado = verificarLogin()

        supportActionBar?.title="Morar EACH"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        val mapFragment = mapa_fragment as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())
    }

    private fun verificarLogin(): Boolean {
        return if (FirebaseAuth.getInstance().uid == null)
            false
        else {
            UpdateInfo.setUsuarioAtual()
            UpdateInfo.setMoradiaAtual()
            true
        }
    }

    private fun configureMenu(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_mapa -> {
                    return@OnNavigationItemSelectedListener false
                }
                R.id.menu_publicar -> {
                    if (verificado) {
                        val intent = Intent(this, PublicarActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Usuário não está logado!", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.menu_editar -> {
                    if (verificado) {
                        val intent = Intent(this, EditarActivity::class.java)
                        startActivity(intent)
                    } else {
                     Toast.makeText(this, "Usuário não está logado!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMapReady(p0: GoogleMap?) {

        p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.485744, -46.499057), 14f))

        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (moradia in snapshot.children) {
                    val moradia = moradia.getValue(Moradia::class.java) ?: return

                    if (moradia.procura) {
                        Log.d(
                            "MapaActivity",
                            "Criando marker da moradia ${moradia.nomeMoradia} do tipo ${moradia.tipoMoradia}"
                        )

                        ConnectionGoogleMap.criarMarker(moradia, p0)

                        p0?.setOnMarkerClickListener { marker ->
                            if (marker.position == LatLng(moradia.lat!!, moradia.lng!!)) {
                                Log.d("ConnectionGoogleMap", "Clicked on ${marker.title}")

                                val intent =
                                    Intent(this@MapaActivity, InfoMoradiaActivity::class.java)
                                intent.putExtra("moradia", moradia)
                                startActivity(intent)
                            }
                            true
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}