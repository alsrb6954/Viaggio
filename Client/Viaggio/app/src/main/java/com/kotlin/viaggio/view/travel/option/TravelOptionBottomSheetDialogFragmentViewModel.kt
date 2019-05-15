package com.kotlin.viaggio.view.travel.option

import androidx.databinding.ObservableBoolean
import com.kotlin.viaggio.data.obj.Travel
import com.kotlin.viaggio.data.source.AndroidPrefUtilService
import com.kotlin.viaggio.model.TravelLocalModel
import com.kotlin.viaggio.view.common.BaseViewModel
import timber.log.Timber
import javax.inject.Inject


class TravelOptionBottomSheetDialogFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var travelLocalModel: TravelLocalModel
    val traveling = ObservableBoolean(false)
    var travel = Travel()
    override fun initialize() {
        super.initialize()

        val travelIngId = prefUtilService.getLong(AndroidPrefUtilService.Key.TRAVELING_ID).blockingGet()
        val selectedTravelId = prefUtilService.getLong(AndroidPrefUtilService.Key.SELECT_TRAVEL_ID).blockingGet()
        if(travelIngId == selectedTravelId){
            traveling.set(true)
        }

        val disposable = travelLocalModel.getTravel()
            .subscribe({
                travel = it
            }){
                Timber.d(it)
            }
        addDisposable(disposable)
    }
}