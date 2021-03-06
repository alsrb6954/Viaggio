package com.kotlin.viaggio.view.setting

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.github.ajalt.reprint.core.Reprint
import com.kotlin.viaggio.data.source.AndroidPrefUtilService
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.view.common.BaseViewModel
import javax.inject.Inject

class SettingLockFragmentViewModel @Inject constructor() : BaseViewModel() {

    val completeLiveData = MutableLiveData<Event<Any>>()

    val lockApp = ObservableBoolean(false)
    val fingerPrintLockApp = ObservableBoolean(false)
    val isExistFingerPrint = ObservableBoolean(false)
    val isRegisterFingerPrint = ObservableBoolean(false)

    val completableLiveData = MutableLiveData<Event<Any>>()
    override fun initialize() {
        super.initialize()
        Reprint.initialize(appCtx.get())

        lockApp.set(prefUtilService.getBool(AndroidPrefUtilService.Key.LOCK_APP).blockingGet())
        fingerPrintLockApp.set(prefUtilService.getBool(AndroidPrefUtilService.Key.FINGER_PRINT_LOCK_APP).blockingGet())

        val disposable = rxEventBus.completeLock.subscribe {
            lockApp.set(prefUtilService.getBool(AndroidPrefUtilService.Key.LOCK_APP
            ).blockingGet())
            completableLiveData.value = Event(Any())
        }
        addDisposable(disposable)

        isExistFingerPrint.set(Reprint.isHardwarePresent())
        isRegisterFingerPrint.set(Reprint.hasFingerprintRegistered())
    }

    fun initPassword() {
        prefUtilService.putBool(AndroidPrefUtilService.Key.LOCK_APP, false).blockingAwait()
        prefUtilService.putBool(AndroidPrefUtilService.Key.FINGER_PRINT_LOCK_APP, false).blockingAwait()
        prefUtilService.putString(AndroidPrefUtilService.Key.LOCK_PW, "").blockingAwait()
        rxEventBus.completeLock.onNext(Any())
    }

    fun fingerPrint() {
        prefUtilService.putBool(AndroidPrefUtilService.Key.FINGER_PRINT_LOCK_APP, fingerPrintLockApp.get()).blockingAwait()
    }

}
