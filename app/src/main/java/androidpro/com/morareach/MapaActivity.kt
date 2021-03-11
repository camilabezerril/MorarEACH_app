package androidpro.com.morareach


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidpro.com.morareach.apicalls.*
import androidpro.com.morareach.cadastrologin.LoginActivity
import androidpro.com.morareach.models.MarkerMap
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filtrar -> {
                return false
            }
            R.id.menu_sair -> {
                FirebaseAuth.getInstance().signOut()
                UpdatedInfo.usuarioAtual = null
                UpdatedInfo.moradiaAtual = null

                val intent = Intent(this, LoginActivity:: class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap?) {

        p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.485744, -46.499057), 14f))

        trocar_zoom_mapa.setOnClickListener {
            trocarZoom(p0)
        }

        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")
        val markers = ArrayList<MarkerMap?>()

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (moradia in snapshot.children) {
                    val moradia = moradia.getValue(Moradia::class.java) ?: return

                    if (moradia.procura) {
                        Log.d(
                            "MapaActivity",
                            "Criando marker da moradia ${moradia.nomeMoradia} do tipo ${moradia.tipoMoradia}"
                        )

                        markers.add(ConnectionGoogleMap.criarMarker(moradia, p0))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        p0?.setOnMarkerClickListener { marker ->
            for (markerMap in markers)
                if (marker.position == markerMap?.latLng) {
                    Log.d("MapaActivity", "Clicked on ${markerMap?.title}")

                    val intent =
                        Intent(this@MapaActivity, InfoMoradiaActivity::class.java)
                    intent.putExtra("moradiaKey", markerMap?.moradiakey)
                    startActivity(intent)
                }
                true
        }
    }

    private fun trocarZoom(p0: GoogleMap?){
        if (trocar_zoom_mapa.text == "Tatuapé"){
            trocar_zoom_mapa.text = "Campus"
            p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.535048205065, -46.57617571598909), 14f))
        }else {
            trocar_zoom_mapa.text = "Tatuapé"
            p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.485744, -46.499057), 14f))
        }
    }
}