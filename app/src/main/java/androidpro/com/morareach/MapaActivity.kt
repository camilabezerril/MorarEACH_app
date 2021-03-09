package androidpro.com.morareach


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidpro.com.morareach.apicalls.*
import androidpro.com.morareach.models.MarkerMap
import androidpro.com.morareach.models.Moradia
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        supportActionBar?.title="Morar EACH"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        bottom_nav_view.itemIconTintList = null

        val mapFragment = mapa_fragment as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottom_nav_view.setOnNavigationItemSelectedListener(configureMenu())
    }

    private fun verificarLogin(): Boolean {
        val uid = FirebaseAuth.getInstance().uid ?: return false
        return true
    }

    private fun configureMenu(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_mapa -> {
                    return@OnNavigationItemSelectedListener false
                }
                R.id.menu_publicar -> {
                    if (verificarLogin()) {
                        val intent = Intent(this, PublicarActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Usuário não está logado!", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.menu_editar -> {
                    if (verificarLogin()) {
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

        p0?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.485744, -46.499057), 15f))

        val ref = FirebaseDatabase.getInstance().getReference("/moradias/")
        val list = listOf("Casa", "Apartamento", "Kitnet")
        val markers = ArrayList<MarkerMap?>()

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.forEach{
                    val tipo_moradia = snapshot.child(it).children

                    for (moradia in tipo_moradia) {
                        val iconType = it
                        val moradia = moradia.getValue(Moradia::class.java)

                        Log.d("MapaActivity", "Criando marker da moradia ${moradia?.nomeMoradia} do tipo $iconType")

                        markers.add(criarMarker(moradia, p0, iconType))
                    }
                }

                p0?.setOnMarkerClickListener { marker ->
                    for (markerMap in markers)
                        if (marker.position == LatLng(markerMap!!.lat, markerMap.lng)) {
                            Log.d("ConnectionGoogleMap", "Clicked on ${markerMap.title}")

                            val intent = Intent(this@MapaActivity, InfoMoradiaActivity::class.java)
                            intent.putExtra("moradiaNome", markerMap.title)
                            intent.putExtra("moradiaKey", markerMap.moradiaKey)
                            startActivity(intent)
                        }
                    true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}