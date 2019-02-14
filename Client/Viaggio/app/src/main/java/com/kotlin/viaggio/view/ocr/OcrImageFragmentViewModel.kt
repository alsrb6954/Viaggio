package com.kotlin.viaggio.view.ocr

import androidx.lifecycle.MutableLiveData
import com.kotlin.viaggio.model.TravelModel
import com.kotlin.viaggio.view.common.BaseViewModel
import io.fotoapparat.result.PhotoResult
import javax.inject.Inject

class OcrImageFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var travelModel: TravelModel

    val photoResult:MutableLiveData<PhotoResult> = MutableLiveData()
    override fun initialize() {
        super.initialize()
        val disposable = travelModel.cameraResult.subscribe {
            photoResult.value = it
        }
        addDisposable(disposable)
    }
}
