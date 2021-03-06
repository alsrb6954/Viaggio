package com.kotlin.viaggio.view.camera

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.kotlin.viaggio.data.obj.PermissionError
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.model.TravelLocalModel
import com.kotlin.viaggio.view.common.BaseViewModel
import io.fotoapparat.result.PhotoResult
import io.reactivex.Observable
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var travelLocalModel: TravelLocalModel
    @Inject
    lateinit var firebaseVision: FirebaseVisionTextRecognizer

    val photoUri: MutableLiveData<Event<Uri>> = MutableLiveData()
    val permissionRequestMsg: MutableLiveData<Event<PermissionError>> = MutableLiveData()
    val imageViewShow: MutableLiveData<Event<Any>> = MutableLiveData()
    val complete: MutableLiveData<Event<Any>> = MutableLiveData()

    val isImageMake: ObservableBoolean = ObservableBoolean(false)
    val isVisionTextRecognizer: ObservableBoolean = ObservableBoolean(false)
    lateinit var imagePathList: MutableList<String>

    override fun initialize() {
        super.initialize()
        imagePathList = travelLocalModel.imageAllPath()
        isVisionTextRecognizer.set(false)
    }

    fun savePicture(photoResult: PhotoResult) {
        isImageMake.set(true)
        val disposable = travelLocalModel.savePicture(photoResult).subscribe { t1 ->
            photoUri.postValue(Event(t1))
            visionTextRecognizer(t1)
        }
        addDisposable(disposable)
    }

    fun permissionCheck(request: Observable<Boolean>?) {
        val disposable = request?.subscribe { t ->
            if (t) {
                imageViewShow.value = Event(Any())
            } else {
                permissionRequestMsg.value = Event(PermissionError.STORAGE_PERMISSION)
            }
        }
        disposable?.let { addDisposable(it) }
    }

    fun visionTextRecognizer(any: Any) {
        when (any) {
            is Bitmap -> {
                firebaseVision.processImage(FirebaseVisionImage.fromBitmap(any))
            }
            is Uri -> {
                firebaseVision.processImage(FirebaseVisionImage.fromFilePath(appCtx.get(), any))
            }
            else -> {
                firebaseVision.processImage(FirebaseVisionImage.fromBitmap(any as Bitmap))
            }
        }.addOnCompleteListener {
            complete.value = Event(Any())
//            rxEventBus.travelSelectedCity.onNext("미국")
            isVisionTextRecognizer.set(true)
        }.addOnFailureListener {}
    }

    fun imageBitmapConfirm(bitmap: Bitmap) {
        rxEventBus.travelOfFirstImage.onNext(bitmap)
    }
}
