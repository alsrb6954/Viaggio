package com.kotlin.viaggio.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.kotlin.viaggio.android.WorkerName
import com.kotlin.viaggio.data.`object`.Travel
import com.kotlin.viaggio.data.`object`.TravelCard
import com.kotlin.viaggio.model.TravelLocalModel
import com.kotlin.viaggio.model.TravelModel
import io.reactivex.Completable
import javax.inject.Inject

class UploadTravelWorker @Inject constructor(context: Context, params: WorkerParameters) : BaseWorker(context, params) {
    @Inject
    lateinit var travelLocalModel: TravelLocalModel
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var travelModel: TravelModel
    override fun doWork(): Result {
        super.doWork()
        val toJson = inputData.getString(WorkerName.TRAVEL.name) ?: ""
        val travel = gson.fromJson(toJson, Travel::class.java) ?: Travel()

        val toJson1 = inputData.getString(WorkerName.TRAVEL_CARD.name) ?: ""
        val travelCard = gson.fromJson(toJson1, TravelCard::class.java) ?: TravelCard()

        if(travel.id != 0L){
            travelModel.uploadTravel(travel)
                .flatMapCompletable {
                    if(it.isSuccessful){
                        travel.userExist = true
                        travelLocalModel.updateTravel(travel)
                    }else{
                        travelModel.uploadTravel(travel)
                            .flatMapCompletable {sec ->
                                if(sec.isSuccessful){
                                    travel.userExist = true
                                    travelLocalModel.updateTravel(travel)
                                }else{
                                    // 처리 부
                                    Completable.complete()
                                }
                            }
                    }
                }.blockingAwait()
        }

        if(travelCard.id != 0L){
            travelModel.uploadTravelCard(travelCard)
                .flatMapCompletable {
                    if(it.isSuccessful){
                        travelCard.userExist = true
                        travelLocalModel.updateTravelCard(travelCard)
                    }else{
                        Completable.complete()
                    }
                }.blockingAwait()
        }

        return Result.success()
    }
}