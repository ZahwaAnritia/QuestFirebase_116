package com.example.myfirebase.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.repositori.RepositorySiswa
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface StatusUiSiswa {
    data class Success(val siswa: List<Siswa> = listOf()) : StatusUiSiswa
    object Error : StatusUiSiswa
    object Loading : StatusUiSiswa
}

class HomeViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var statusUiSiswa: StatusUiSiswa by mutableStateOf(StatusUiSiswa.Loading)
        private set

    init {
        Log.d("HomeViewModel", "ViewModel initialized")
        loadSiswa()
    }

    fun loadSiswa() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Loading siswa...")
            statusUiSiswa = StatusUiSiswa.Loading

            statusUiSiswa = try {
                val data = repositorySiswa.getDataSiswa()
                Log.d("HomeViewModel", "Data loaded: ${data.size} items")
                data.forEach {
                    Log.d("HomeViewModel", "Siswa: ID=${it.id}, Nama=${it.nama}")
                }
                StatusUiSiswa.Success(data)
            } catch (e: IOException) {
                Log.e("HomeViewModel", "IOException: ${e.message}", e)
                StatusUiSiswa.Error
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}", e)
                StatusUiSiswa.Error
            }
        }
    }
}