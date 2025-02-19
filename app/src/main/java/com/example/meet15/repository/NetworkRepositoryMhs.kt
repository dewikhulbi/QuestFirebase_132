package com.example.meet15.repository

import com.example.meet15.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkRepositoryMhs (
    private val firestore: FirebaseFirestore
) : RepositoryMhs {
    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try { firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (e: Exception) {
            throw Exception("Gagal menambahkan data mahasiswa:${e.message}")
        }
    }

    override fun getAllMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow {
        val mhscollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener {
                value, error ->
                if (value != null) {
                    val mhslist = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)!! //convert dari dokumen firestore ke dataclass
                    }
                    trySend(mhslist) //fungsi untuk mengirim colection ke dataclass
                }
            }
        awaitClose {
            mhscollection.remove()  //menutup collection dari firestore
        }
    }

    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsDocument = firestore.collection("Mahasiswa")
            .document(nim)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)
                }
            }
        awaitClose {
            mhsDocument.remove()
        }
    }

    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            mahasiswa.nim?.let {
                firestore.collection("Mahasiswa")
                    .whereEqualTo("nim", mahasiswa.nim)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()
                    ?.reference
                    ?.delete()
                    ?.await()
            }
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data mahasiswa: ${e.message}")
        }
    }


    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        try { firestore.collection("Mahasiswa")
            .document(mahasiswa.nim)
            .set(mahasiswa)
            .await()
        } catch (e: Exception) {
            throw Exception("Gagal mengupdate data mahasiswa:${e.message}")
        }
    }

}