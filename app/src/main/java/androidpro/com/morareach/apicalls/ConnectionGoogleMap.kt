package androidpro.com.morareach.apicalls

import android.util.Log
import androidpro.com.morareach.R
import androidpro.com.morareach.models.MarkerMap
import androidpro.com.morareach.models.Moradia
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

fun criarMarker(moradia: Moradia?, p0: GoogleMap?, iconType: String): MarkerMap?{
    val icon = when {
        iconType == "Casa" -> R.drawable.icon_casa
        iconType == "Apartamento" -> R.drawable.icon_apart
        iconType == "Kitnet" -> R.drawable.icon_kitnet
        else -> return null
    }

    val coord = getCoordenadas(moradia!!.endereco) ?: return null

    p0?.apply {
        val location = LatLng(coord[0], coord[1])
        addMarker(
                MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(icon))
                        .position(location)
                        .title(moradia.nomeMoradia)
        )
    }

    return MarkerMap(moradia.key, moradia.nomeMoradia, coord[0], coord[1])
}
