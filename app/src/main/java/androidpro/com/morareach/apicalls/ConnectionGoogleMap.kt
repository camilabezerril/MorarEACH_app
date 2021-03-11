package androidpro.com.morareach.apicalls

import android.util.Log
import androidpro.com.morareach.R
import androidpro.com.morareach.models.Moradia
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ConnectionGoogleMap {
    fun criarMarker(moradia: Moradia?, p0: GoogleMap?) {
        val iconType = moradia!!.tipoMoradia

        val icon = when {
            iconType == "Casa" -> R.drawable.icon_casa
            iconType == "Apartamento" -> R.drawable.icon_apart
            iconType == "Kitnet" -> R.drawable.icon_kitnet
            else -> return
        }

        Log.d(
            "MapaActivity",
            "As coordenadas de ${moradia.nomeMoradia} são: ${moradia.lat}, ${moradia.lng}"
        )

        p0?.apply {
            val location = LatLng(moradia.lat!!, moradia.lng!!)
            addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(icon))
                    .position(location)
                    .title(moradia.nomeMoradia)
            )
        }
    }
}
