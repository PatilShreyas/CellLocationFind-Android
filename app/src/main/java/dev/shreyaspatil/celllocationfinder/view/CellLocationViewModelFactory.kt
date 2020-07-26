package dev.shreyaspatil.celllocationfinder.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.shreyaspatil.celllocationfinder.repository.OpenCellRepository
import dev.shreyaspatil.celllocationfinder.service.buildUnwiredLabsService
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CellLocationViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CellLocationViewModel::class.java)) {
            return CellLocationViewModel(
                OpenCellRepository(buildUnwiredLabsService())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
