package dev.shreyaspatil.celllocationfinder.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shreyaspatil.celllocationfinder.model.request.CellInfo
import dev.shreyaspatil.celllocationfinder.repository.OpenCellRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CellLocationViewModel(
    private val repository: OpenCellRepository
) : ViewModel() {

    private var job: Job? = null

    private val _locationLiveData = MutableLiveData<State>()

    val locationLiveData: LiveData<State> = _locationLiveData

    fun fetchLocation(cellInfo: CellInfo) {
        job?.cancel()

        job = viewModelScope.launch {
            repository.getLocationByCellInfo(cellInfo)
                .onStart {
                    _locationLiveData.value = State.Loading
                }
                .catch {
                    it.printStackTrace()
                    _locationLiveData.value = State.Failed(it.message.toString())
                }
                .collect { cellLocation ->
                    cellLocation?.let {
                        if (cellLocation.isSuccess()) {
                            _locationLiveData.value = State.Success(cellLocation)
                        } else {
                            _locationLiveData.value = State.Failed(cellLocation.message.toString())
                        }
                    }
                }
        }
    }
}
