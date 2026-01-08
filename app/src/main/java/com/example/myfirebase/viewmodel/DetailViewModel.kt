package com.example.myfirebase.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.repositori.RepositorySiswa
import com.example.myfirebase.view.route.DestinasiDetail
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface StatusUIDetail {
    data class Success(val siswa: Siswa) : StatusUIDetail
    object Error : StatusUIDetail
    object Loading : StatusUIDetail
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    private val idSiswa: Int = checkNotNull(savedStateHandle[DestinasiDetail.siswaIdArg])

    var statusUIDetail: StatusUIDetail by mutableStateOf(StatusUIDetail.Loading)
        private set

    init {
        Log.d("DetailViewModel", "Init with siswaId: $idSiswa")
        loadSiswa()
    }

    fun loadSiswa() {
        viewModelScope.launch {
            Log.d("DetailViewModel", "Loading siswa with ID: $idSiswa")
            statusUIDetail = StatusUIDetail.Loading

            statusUIDetail = try {
                val siswaList = repositorySiswa.getDataSiswa()
                Log.d("DetailViewModel", "Got ${siswaList.size} siswa from repository")

                siswaList.forEach {
                    Log.d("DetailViewModel", "Siswa in list - ID: ${it.id} (${it.id.toInt()}), Nama: ${it.nama}")
                }

                val siswa = siswaList.find { it.id.toInt() == idSiswa }

                if (siswa != null) {
                    Log.d("DetailViewModel", "Found siswa: ${siswa.nama}")
                    StatusUIDetail.Success(siswa)
                } else {
                    Log.e("DetailViewModel", "Siswa with ID $idSiswa not found!")
                    StatusUIDetail.Error
                }
            } catch (e: IOException) {
                Log.e("DetailViewModel", "IOException: ${e.message}", e)
                StatusUIDetail.Error
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}", e)
                StatusUIDetail.Error
            }
        }
    }

    suspend fun hapusSatuSiswa() {
        viewModelScope.launch {
            try {
                Log.d("DetailViewModel", "Deleting siswa with ID: $idSiswa")
                repositorySiswa.hapusSatuSiswa(idSiswa.toLong())
                Log.d("DetailViewModel", "Delete successful")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Delete error: ${e.message}", e)
                e.printStackTrace()
            }
        }.join()
    }
}