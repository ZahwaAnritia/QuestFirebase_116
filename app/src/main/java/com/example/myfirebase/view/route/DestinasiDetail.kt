package com.example.myfirebase.view.route

import com.example.myfirebase.R

object DestinasiDetail: DestinasiNavigasi {
    override val route = "item_detail"
    override val titleRes = R.string.detail_siswa
    const val siswaIdArg = "siswaId"
    val routeWithArgs = "$route/{$siswaIdArg}"
}