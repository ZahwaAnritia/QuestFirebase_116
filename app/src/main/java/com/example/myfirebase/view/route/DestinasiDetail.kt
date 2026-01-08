package com.example.myfirebase.view.route

import com.example.myfirebase.R

object DestinasiDetail: DestinasiNavigasi {
    override val route = "detail_siswa"
    override val titleRes = R.string.detail_siswa
    const val siswaIdArg = "idSiswa"
    val routeWithArgs = "$route/{$siswaIdArg}"
}