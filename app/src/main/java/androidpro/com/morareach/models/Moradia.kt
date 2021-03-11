package androidpro.com.morareach.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Moradia (val key: String, val nomeMoradia: String, val tipoMoradia: String, val preferencia: String,
               val endereco: String, val valor: String, val desc: String, val usuario: String,
               val lat: Double?, val lng: Double?, val procura: Boolean) : Parcelable {
    constructor() : this("","", "", "","", "", "", "",0.0,0.0, true)
}