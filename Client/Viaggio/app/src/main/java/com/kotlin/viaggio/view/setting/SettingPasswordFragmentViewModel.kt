package com.kotlin.viaggio.view.setting

import android.text.TextUtils
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.kotlin.viaggio.data.obj.Error
import com.kotlin.viaggio.data.obj.SignError
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.model.UserModel
import com.kotlin.viaggio.view.common.BaseViewModel
import com.kotlin.viaggio.view.sign.common.Encryption
import timber.log.Timber
import javax.inject.Inject

class SettingPasswordFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var userModel: UserModel
    @Inject
    lateinit var encryption: Encryption
    val password = ObservableField<String>("")
        .apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    check()
                }
            })
        }
    val newPassword = ObservableField<String>("")
        .apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    check()
                }
            })
        }
    val newPasswordConfirm = ObservableField<String>("")
        .apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    check()
                }
            })
        }
    val isFormValid = ObservableBoolean(false)

    val completeLiveData = MutableLiveData<Event<Any>>()
    val error: MutableLiveData<Event<SignError>> = MutableLiveData()
    fun check() {
        when {
            TextUtils.isEmpty(password.get()) -> isFormValid.set(false)
            TextUtils.isEmpty(newPassword.get()) -> isFormValid.set(false)
            TextUtils.isEmpty(newPasswordConfirm.get()) -> isFormValid.set(false)
            else -> isFormValid.set(true)
        }
    }
    fun save() :Boolean{
        when{
            password.get()!!.length < 8 -> {
                error.value = Event(SignError.PW_NUM)
                return false
            }
            password.get() == newPassword.get() ->{
                error.value = Event(SignError.SAME_PW)
                return false
            }
            newPassword.get() != newPasswordConfirm.get() -> {
                error.value = Event(SignError.PW_MISMATCH)
                return false
            }
        }
        val old = encryption.encryptionValue(password.get()!!)
        val new = encryption.encryptionValue(newPassword.get()!!)
        val confirm = encryption.encryptionValue(newPasswordConfirm.get()!!)

        val disposable = userModel.updatePassword(old, new, confirm)
            .subscribe({
                if(it.isSuccessful){
                    completeLiveData.postValue(Event(Any()))
                }else{
                    val errorMsg: Error = gson.fromJson(it.errorBody()?.string(), Error::class.java)
                    when(errorMsg.message){
                        402 -> error.postValue(Event(SignError.WRONG_PW))
                        403 -> error.postValue(Event(SignError.PW_NUM))
                        else -> error.postValue(Event(SignError.WRONG_PW))
                    }
                }
            }){
                Timber.d(it)
            }
        addDisposable(disposable)

        return true
    }
}
