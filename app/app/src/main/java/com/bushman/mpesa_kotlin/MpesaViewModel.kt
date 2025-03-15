package com.bushman.mpesa_kotlin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


data class MpesaState(
    val data: ApiResponse? = null,
    val isLoading:Boolean =false,
    val error:String? = null
)
sealed class MpesaEvents{
    data class InitializePaymentEvent(val phoneNumber:String,val amount:String): MpesaEvents()
//    data object LoadingEvent: MpesaEvents()


}
@HiltViewModel
class MpesaViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private var _state: MutableState<MpesaState> = mutableStateOf(MpesaState())
    val state: State<MpesaState> = _state

    fun onMpesaEvent(event: MpesaEvents){
        when(event){
            is MpesaEvents.InitializePaymentEvent -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    try {
                        val response = repository.initializePayment(
                            phoneNumber = event.phoneNumber,
                            amount = event.amount
                        )
                        _state.value = _state.value.copy(data = response, isLoading = false)



                } catch (e: HttpException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Network error: ${e.message}"
                    )
                } catch (e: IllegalStateException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Invalid data: ${e.message}"
                    )
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "An unexpected error occurred: ${e.message}"
                    )
                }
            }
            }
        }
    }


}