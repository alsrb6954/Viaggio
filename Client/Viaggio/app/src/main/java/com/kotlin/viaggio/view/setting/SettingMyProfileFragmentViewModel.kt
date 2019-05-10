package com.kotlin.viaggio.view.setting

import android.text.TextUtils
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.google.gson.Gson
import com.kotlin.viaggio.BuildConfig
import com.kotlin.viaggio.aws.DeveloperAuthenticationProvider
import com.kotlin.viaggio.data.`object`.Error
import com.kotlin.viaggio.data.`object`.SignError
import com.kotlin.viaggio.data.source.AndroidPrefUtilService
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.model.UserModel
import com.kotlin.viaggio.view.common.BaseViewModel
import com.kotlin.viaggio.view.sign.common.Encryption
import io.reactivex.Single
import timber.log.Timber
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class SettingMyProfileFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var userModel: UserModel
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var transferUtility: TransferUtility
    @Inject
    lateinit var config: DeveloperAuthenticationProvider

    val email = ObservableField<String>("")
    val name = ObservableField<String>("").apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                check()
            }
        })
    }
    val isFormValid = ObservableBoolean(false)

    val completeLiveData = MutableLiveData<Event<Any>>()
    val error: MutableLiveData<Event<SignError>> = MutableLiveData()

    var imageName = ""
    override fun initialize() {
        super.initialize()
        email.set(prefUtilService.getString(AndroidPrefUtilService.Key.USER_ID).blockingGet())
        name.set(prefUtilService.getString(AndroidPrefUtilService.Key.USER_NAME).blockingGet())
        imageName = prefUtilService.getString(AndroidPrefUtilService.Key.USER_IMAGE_PROFILE).blockingGet()

        val imageDisposable = rxEventBus.userImage
            .subscribe {
                imageName = it
            }
        addDisposable(imageDisposable)
    }
    fun check() {
        when {
            TextUtils.isEmpty(name.get()) -> isFormValid.set(false)
            else -> isFormValid.set(true)
        }
    }

    fun save() {
        val image = prefUtilService.getString(AndroidPrefUtilService.Key.USER_IMAGE_PROFILE).blockingGet()
        val disposable = if(imageName == image){
            userModel.updateUser(name.get()!!, imageName)
        }else{
            userModel.userProfile(imageName)
                .flatMap {
                    Single.create<String> { emitter ->
                        if (TextUtils.isEmpty(image).not()) {
                            File(image).delete()
                        }
                        prefUtilService.putString(AndroidPrefUtilService.Key.USER_IMAGE_PROFILE, it.first()).blockingAwait()

                        val awsId = prefUtilService.getString(AndroidPrefUtilService.Key.AWS_ID).blockingGet()
                        val awsToken = prefUtilService.getString(AndroidPrefUtilService.Key.AWS_TOKEN).blockingGet()
                        config.setInfo(awsId, awsToken)
                        val uploadObserver = transferUtility.upload(BuildConfig.S3_UPLOAD_BUCKET, "image/${it.first().split("/").last()}", File(it.first()))
                        uploadObserver.setTransferListener(object : TransferListener {
                            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
                            override fun onStateChanged(id: Int, state: TransferState?) {
                                if(state == TransferState.COMPLETED){
                                    emitter.onSuccess(uploadObserver.key)
                                }
                            }
                            override fun onError(id: Int, ex: Exception?) {
                                emitter.onError(ex as Throwable)
                            }
                        })
                    }
                }
                .flatMap {
                    userModel.updateUser(name.get()!!, it)
                }
        }
            .subscribe({
                if(it.isSuccessful){
                    prefUtilService.putString(AndroidPrefUtilService.Key.USER_NAME, name.get()!!).blockingAwait()
                    rxEventBus.userUpdate.onNext(Any())
                    completeLiveData.postValue(Event(Any()))
                }
            }) {
                Timber.d(it)
            }

        addDisposable(disposable)
    }
}