package androidpro.com.morareach.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Moradia (val key: String, var nomeMoradia: String, val tipoMoradia: String, var preferencia: String,
               val endereco: String, var valor: String, var desc: String, val usuario: String,
               val lat: Double?, val lng: Double?, var procura: Boolean) : Parcelable {
    constructor() : this("","", "", "","", "", "", "",0.0,0.0, true)
}