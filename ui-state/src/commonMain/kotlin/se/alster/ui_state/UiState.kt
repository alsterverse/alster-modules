package se.alster.ui_state

/**
 * Represents the state of a UI component. It can be in one of the following states:
 * - [Idle] - The initial state of the UI component.
 * - [Loading] - The UI component is loading data.
 * - [Data] - The UI component has received data.
 * - [Error] - An error occurred while loading data.
 * @param T The type of the data.
 * @see Result
 * @see toUiState
 * @see toUiStateData
 * @see mapData
 * @see dataOrNull
 */
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Data<out T>(val value: T) : UiState<T>
    data class Error(val value: Throwable) : UiState<Nothing>
}

/**
 * Converts a [Result] to a [UiState].
 */
fun <T> Result<T>.toUiState(): UiState<T> {
    onSuccess {
        return UiState.Data(it)
    }
    onFailure {
        return UiState.Error(it)
    }
    return UiState.Loading
}

/**
 * Converts a value to a [UiState.Data].
 */
fun <T : Any> T.toUiStateData(): UiState.Data<T> {
    return UiState.Data(this)
}

/**
 * Maps the data of a [UiState] to another type.
 */
fun <T, R> UiState<T>.mapData(data: (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Data -> UiState.Data(data(value))
        is UiState.Error -> UiState.Error(value)
        is UiState.Loading -> UiState.Loading
        is UiState.Idle -> UiState.Idle
    }
}

/**
 * Returns the data of a [UiState] or `null` if the state is not [UiState.Data].
 */
fun <T> UiState<T>.dataOrNull(): T? {
    return when (this) {
        is UiState.Data -> value
        else -> null
    }
}
