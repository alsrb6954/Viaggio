package com.kotlin.viaggio.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transfermanager.internal.TransferManagerUtils
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.google.gson.Gson
import com.kotlin.viaggio.BuildConfig
import com.kotlin.viaggio.android.WorkerName
import com.kotlin.viaggio.aws.DeveloperAuthenticationProvider
import com.kotlin.viaggio.data.`object`.Travel
import com.kotlin.viaggio.data.`object`.TravelCard
import com.kotlin.viaggio.data.source.AndroidPrefUtilService
import com.kotlin.viaggio.model.TravelLocalModel
import com.kotlin.viaggio.model.TravelModel
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class UploadTravelWorker @Inject constructor(context: Context, params: WorkerParameters) : BaseWorker(context, params) {
    @Inject
    lateinit var travelLocalModel: TravelLocalModel
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var travelModel: TravelModel
    @Inject
    lateinit var transferUtility: TransferUtility
    @Inject
    lateinit var config: DeveloperAuthenticationProvider
    @Inject
    lateinit var prefUtilService: AndroidPrefUtilService

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
            if(travelCard.imageNames.isNotEmpty()){
                val awsId = prefUtilService.getString(AndroidPrefUtilService.Key.AWS_ID).blockingGet()
                val awsToken = prefUtilService.getString(AndroidPrefUtilService.Key.AWS_TOKEN).blockingGet()
                val list = travelCard.imageNames.map{
                    Single.create<String> { emitter ->
                        config.setInfo(awsId, awsToken)
                        val uploadObserver = transferUtility.upload(BuildConfig.S3_UPLOAD_BUCKET, "image/${it.split("/").last()}", File(it))
                        uploadObserver.setTransferListener(object : TransferListener {
                            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
                            override fun onStateChanged(id: Int, state: TransferState?) {
                                if(state == TransferState.COMPLETED){
                                    emitter.onSuccess(uploadObserver.key)
                                }
                            }
                            override fun onError(id: Int, ex: Exception?) {
                            }
                        })
                    }
                }
                val resultList = mutableListOf<String>()

                Single.merge(list)
                    .map {
                        resultList.add(it)
                    }.lastOrError()
                    .flatMapCompletable {
                        travelCard.imageUrl = resultList
                        travelModel.uploadTravelCard(travelCard)
                            .flatMapCompletable {
                                if(it.isSuccessful){
                                    travelCard.userExist = true
                                    travelLocalModel.updateTravelCard(travelCard)
                                }else{
                                    Completable.complete()
                                }
                            }
                    }
            }else{
                travelModel.uploadTravelCard(travelCard)
                    .flatMapCompletable {
                        if(it.isSuccessful){
                            travelCard.userExist = true
                            travelLocalModel.updateTravelCard(travelCard)
                        }else{
                            Completable.complete()
                        }
                    }
            }.blockingAwait()
        }

        return Result.success()
    }
}