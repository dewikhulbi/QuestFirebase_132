package com.example.meet15.di

import android.content.Context
import com.example.meet15.repository.NetworkRepositoryMhs
import com.example.meet15.repository.RepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore

interface InterfaceContainerApp {
    val repositoryMhs: RepositoryMhs
}

class MahasiswaContainer(private val context: Context) : InterfaceContainerApp {
    private val firestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    override val repositoryMhs: RepositoryMhs by lazy {
        NetworkRepositoryMhs(firestore)
    }
}