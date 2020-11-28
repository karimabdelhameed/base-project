package com.bluecrunch.base


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.custom.Resource
import com.custom.Status
import com.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {

    private var mNavigator: WeakReference<N>? = null
    val compositeDisposable: CompositeDisposable
    val isLoading = ObservableBoolean(false)
    val progressLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var showLoadingLayout = ObservableBoolean(false)
    var isLastPage = false
    var isPagingLoading = ObservableBoolean(false)
    var showWhiteLoadingLayout = ObservableBoolean(false)
    var errorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var networkErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var action: Action? = null


    var navigator: N?
        get() = mNavigator!!.get()
        set(navigator) {
            this.mNavigator = WeakReference<N>(navigator)
        }

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        if (viewModelScope.isActive) {
            viewModelScope.cancel()
        }
        super.onCleared()
    }

    fun handleApiErrors(result: Resource<Any>, mAction: Action) {
        when (result.status) {
            Status.SUCCESS -> {
                this.action = null
            }
            Status.ERROR -> {
                errorMessage.value = result.message
            }
            Status.NETWORK_ERROR -> {
                networkErrorMessage.value = result.message
                this.action = mAction
            }
        }
    }

    fun setIsLoading(isLoading: Boolean) {
//        this.isLoading.set(isLoading)
        progressLoading.value = isLoading
    }

    fun setIsPagingLoading(isLoading: Boolean) {
        this.isPagingLoading.set(isLoading)
    }

    fun setShowLoadingLayout(isLoading: Boolean) {
        this.showLoadingLayout.set(isLoading)
    }

    fun setShowWhiteLoadingLayout(isLoading: Boolean) {
        this.showWhiteLoadingLayout.set(isLoading)
    }
}
