package com.example.myfirebase.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.DetailSiswa
import com.example.myfirebase.modeldata.UIStateSiswa
import com.example.myfirebase.modeldata.toDataSiswa
import com.example.myfirebase.modeldata.toUiStateSiswa
import com.example.myfirebase.repositori.RepositorySiswa
import com.example.myfirebase.view.route.DestinasiEdit
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private val siswaId: Int = checkNotNull(savedStateHandle[DestinasiEdit.siswaIdArg])

    init {
        Log.d("EditViewModel", "=== EDIT VIEWMODEL INITIALIZED ===")
        Log.d("EditViewModel", "SiswaId: $siswaId")
        viewModelScope.launch {
            loadSiswa()
        }
    }

    private suspend fun loadSiswa() {
        try {
            Log.d("EditViewModel", "Loading siswa with ID: $siswaId")
            val siswa = repositorySiswa.getSatuSiswa(siswaId.toLong())
            Log.d("EditViewModel", "Loaded siswa: ${siswa.nama}")

            uiStateSiswa = siswa.toUiStateSiswa(true)
        } catch (e: Exception) {
            Log.e("EditViewModel", "Error loading siswa: ${e.message}", e)
        }
    }

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa): Boolean {
        return with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa = UIStateSiswa(
            detailSiswa = detailSiswa,
            isEntryValid = validasiInput(detailSiswa)
        )
    }

    suspend fun updateSiswa() {
        if (validasiInput()) {
            try {
                Log.d("EditViewModel", "Updating siswa: ${uiStateSiswa.detailSiswa}")
                repositorySiswa.updateSiswa(uiStateSiswa.detailSiswa.toDataSiswa())
                Log.d("EditViewModel", "Update successful")
            } catch (e: Exception) {
                Log.e("EditViewModel", "Error updating: ${e.message}", e)
                throw e
            }
        }
    }
}