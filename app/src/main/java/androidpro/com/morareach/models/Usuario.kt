package androidpro.com.morareach.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Usuario (val uid: String, val nome: String, val contato: String?, val moradiaKey: String?) : Parcelable {
    constructor() : this("", "", "", "")
}