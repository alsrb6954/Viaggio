package com.kotlin.viaggio.view.traveling.enroll

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.kotlin.viaggio.R
import com.kotlin.viaggio.data.obj.ImageData
import com.kotlin.viaggio.event.Event
import com.kotlin.viaggio.model.TravelLocalModel
import com.kotlin.viaggio.view.common.BaseViewModel
import javax.inject.Inject

class TravelingCardImageEnrollFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var travelLocalModel: TravelLocalModel

    val imagePathList: MutableLiveData<Event<List<ImageData>>> = MutableLiveData()
    val folderNameListLiveData: MutableLiveData<Event<List<String>>> = MutableLiveData()

    var entireChooseCount: Int = 0

    var imageAllList: List<ImageData> = listOf()
    val imageChooseList: MutableList<String> = mutableListOf()
    var imageBitmapChooseList: MutableList<Bitmap> = mutableListOf()
    val emptyImageNotice = ObservableField<String>()

    var imageLimitCount = 10

    override fun initialize() {
        super.initialize()
        val disposable = rxEventBus.travelCacheImages
            .subscribe {
                if(it.isNotEmpty()){
                    imageAllList = it
//                    imagePathList.value = Event(imageAllList)
                    val result = imageAllList.filter { imageDataVal ->
                        imageDataVal.chooseCountList.get() != 0
                    }.sortedBy { imageDataVal ->
                        imageDataVal.chooseCountList.get()
                    }.map { imageDataVal ->
                        imageDataVal.imageName
                    }
                    imageChooseList.clear()
                    imageChooseList.addAll(result)
                    if(imageAllList.isNotEmpty()) {
                        entireChooseCount = if (imageChooseList.isEmpty()) {
                            0
                        } else {
                            imageChooseList.size
                        }
                    }
                } else {
                    folderName()
                }
            }
        addDisposable(disposable)

        val bitmapDisposable = rxEventBus.travelCardImages
            .subscribe {
                if(it.isNotEmpty()){
                    imageBitmapChooseList = it.toMutableList()
                    imageBitmapChooseList.remove(imageBitmapChooseList.last())
                }
            }
        addDisposable(bitmapDisposable)

        val imageCountDisposable = rxEventBus.travelCardImageModifyCount
            .subscribe{
                imageLimitCount -= it
        }
        addDisposable(imageCountDisposable)
        folderName()
        imageAllFetch()
    }



    var folderName = listOf<String>()
    private fun folderName() {
        folderName = travelLocalModel.folderName()
        folderNameListLiveData.value = Event(folderName)
    }
    private fun imageAllFetch() {
        if (imageAllList.isEmpty()) {
            val result = travelLocalModel.imageAllPath()
                .map {
                    ImageData(imageName = it)
                }
            imageAllList = result
            if(imageAllList.isNotEmpty()) {
//                imageChooseList.add(imageAllList[0].imageName)
//                imageAllList[0].chooseCountList.set(1)
//                imagePathList.value = Event(imageAllList)
                fetchImage(folderName.first())
                emptyImageNotice.set("")
            } else {
                emptyImageNotice.set(appCtx.get().resources.getString(R.string.empty_image))
            }
        }
    }

    fun selectImage() {
        rxEventBus.travelCardImageModifyCount.onNext(0)
        rxEventBus.travelCardImages.onNext(imageBitmapChooseList)
        rxEventBus.travelCacheImages.onNext(imageAllList)
    }

    fun backImage() {
        rxEventBus.travelCardImages.onNext(rxEventBus.travelCardImages.value?: listOf())
    }

    fun fetchImage(folder: String) {
        emptyImageNotice.set("")
        val list = imageAllList.filter {
            it.imageName.contains(folder)
        }

        if(list.isNullOrEmpty()) {
            emptyImageNotice.set(appCtx.get().resources.getString(R.string.empty_image))
        } else {
            emptyImageNotice.set("")
        }
        imagePathList.value = Event(list)
    }
}
