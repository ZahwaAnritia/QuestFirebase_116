package com.example.myfirebase.repositori

import com.example.myfirebase.modeldata.Siswa


interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
}
