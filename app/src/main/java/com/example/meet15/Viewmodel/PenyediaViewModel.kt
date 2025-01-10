package com.example.meet15.Viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.meet15.MahasiswaApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeMhsViewModel(MahasiswaApp().containerApp.repositoryMhs) }
        initializer {
            InsertViewModel( MahasiswaApp().containerApp.repositoryMhs
            )
        }
    }
}

fun CreationExtras. MahasiswaApp (): MahasiswaApp =
    ( this [ ViewModelProvider . AndroidViewModelFactory .APPLICATION_KEY]  as MahasiswaApp)