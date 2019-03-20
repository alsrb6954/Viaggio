package com.kotlin.viaggio.view.traveling.enroll

import android.graphics.Bitmap
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.model.TravelModel
import com.kotlin.viaggio.view.common.BaseViewModel
import com.naver.android.helloyako.imagecrop.model.CropInfo
import javax.inject.Inject

class TravelingOfDayImageEnrollFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var travelModel: TravelModel

    val imagePathList: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()

    var entireChooseCount:Int = 1
    val chooseCountList: MutableList<ObservableInt> = mutableListOf()
    val imageAllList: MutableList<String> = mutableListOf()
    val imageChooseList: MutableList<String> = mutableListOf()
    val imageBitmapChooseList: MutableList<Bitmap> = mutableListOf()

    override fun initialize() {
        super.initialize()
        for (s in travelModel.imageAllPath()) {
            imageAllList.add(s)
            chooseCountList.add(ObservableInt(0))
        }
        imageChooseList.add(imageAllList[0])
        chooseCountList[0].set(1)
        imagePathList.value = Event(imageAllList)
    }

    fun selectImage() {
        rxEventBus.travelOfDayImages.onNext(imageBitmapChooseList)
    }

}
