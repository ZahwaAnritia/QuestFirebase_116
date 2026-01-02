package com.example.myfirebase.repositori

import android.util.Log
import com.example.myfirebase.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    //suspend fun deleteSiswa(id: Long)
    //suspend fun getSatuSiswa(id: Long): Siswa
    //suspend fun updateSiswa(siswa: Siswa)
}

class FirebaseRepositorySiswa : RepositorySiswa {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            Log.d("Repository", "Fetching data...")
            val docs = collection.get().await().documents
            Log.d("Repository", "Found ${docs.size} documents")

            docs.map { doc ->
                Log.d("Repository", "Doc ID: ${doc.id}, Data: ${doc.data}")
                Siswa(
                    id = doc.getLong("id") ?: 0L,
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef =
            if (siswa.id == 0L) collection.document()
            else collection.document(siswa.id.toString())

        val data = hashMapOf(
            "id" to (siswa.id.takeIf { it != 0L } ?: docRef.id.hashCode()),
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )

        Log.d("Repository", "Posting data: $data")
        docRef.set(data).await()
        Log.d("Repository", "Data saved with doc ID: ${docRef.id}")
    }


}


