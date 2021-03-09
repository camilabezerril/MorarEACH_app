package androidpro.com.morareach.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Moradia (val key: String, val nomeMoradia: String, val preferencia: String, val endereco: String, val valor: String?, val desc: String?) : Parcelable {
    constructor() : this("","", "","", "", "")
}