package ir.tapsell.sample

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

open class BaseViewModel : ViewModel() {

    private val _logMessage = MutableStateFlow("")
    val logMessage get() = _logMessage


    fun log(tag: String, message: String, error: Int = Log.DEBUG) {
        if (error == Log.ERROR) Log.e(tag, message)
        else Log.d(tag, message)
        val newMessage = buildString {
            appendLine(message)
            append(logMessage.value)
        }
        _logMessage.update { newMessage }
    }

    override fun onCleared() {
        super.onCleared()
        _logMessage.update { "" }
    }
}
