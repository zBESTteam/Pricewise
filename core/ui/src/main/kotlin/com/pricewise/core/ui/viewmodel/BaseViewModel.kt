package com.pricewise.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.concurrent.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val ViewModel.viewModelScopeSafe: CoroutineScope
        get() = viewModelScope + defaultCEH

    private val defaultCEH: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                onDefaultCEH(throwable)
            }
        }

    protected open fun onDefaultCEH(throwable: Throwable) {
        Timber.e(throwable, "Unhandled exception in ${this.javaClass.simpleName}")
    }
}
