package dev.shreyaspatil.celllocationfinder.view

import dev.shreyaspatil.celllocationfinder.model.response.CellLocation

sealed class State {
    object Loading : State()
    data class Success(val response: CellLocation) : State()
    data class Failed(val message: String) : State()
}
