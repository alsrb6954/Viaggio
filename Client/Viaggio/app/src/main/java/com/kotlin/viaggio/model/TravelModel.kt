package com.kotlin.viaggio.model

import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import androidx.paging.DataSource
import com.kotlin.viaggio.data.`object`.Travel
import com.kotlin.viaggio.data.`object`.TravelCard
import com.kotlin.viaggio.data.`object`.TravelOfDay
import com.kotlin.viaggio.data.source.AndroidPrefUtilService
import com.kotlin.viaggio.event.RxEventBus
import io.fotoapparat.result.PhotoResult
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelModel @Inject constructor() : BaseModel() {
    @Inject
    lateinit var rxEventBus: RxEventBus
    @Inject
    lateinit var prefUtilService: AndroidPrefUtilService

    fun savePicture(photoResult: PhotoResult) =
        localDataSource.savePhotoResult(photoResult)

    fun imageAllPath() =
        localDataSource.imageAllPath()

    fun createTravel(travel: Travel): Single<Long> {
        return Single.create(SingleOnSubscribe<Bitmap> {
            rxEventBus.travelOfFirstImage
                .subscribe { t ->
                    it.onSuccess(t)
                }
        }).flatMap {
            localDataSource.cacheFile(it)
                .flatMap { uri ->
                    travel.themeImageName = Uri.parse(uri[0]).lastPathSegment!!
                    db.get().travelDao().insertTravel(travel)
                }
        }
    }

    fun createTravelCard(travelCard: TravelCard):Single<Long>{
        return db.get().travelDao().insertTravelCard(travelCard)
    }

    fun imagePathList(imageChooseList: MutableList<String>): Single<List<String>> {
        return localDataSource.recordImage(imageChooseList.toTypedArray())
    }

    fun createTravelOfDay(travelOfDay: TravelOfDay): Single<Long> {
        return db.get().travelDao().insertTravelOfDay(travelOfDay)
    }
    fun getTravelOfDays(): DataSource.Factory<Int, TravelOfDay> {
        return db.get().travelDao().getTravelOfDaysPaged(prefUtilService.getLong(AndroidPrefUtilService.Key.TRAVELING_ID).blockingGet())
    }

    fun getTravelOfDay(): Single<TravelOfDay> {
        return db.get().travelDao().getTravelOfDay(prefUtilService.getLong(AndroidPrefUtilService.Key.SELECTED_TRAVELING_OF_DAY_ID).blockingGet())
    }
    fun getTravelCardsPager(): DataSource.Factory<Int, TravelCard> {
        return db.get().travelDao().getTravelCardsPaged(prefUtilService.getLong(AndroidPrefUtilService.Key.SELECTED_TRAVELING_OF_DAY_ID).blockingGet())
    }

    fun updateTravelOfDay(travelOfDay: TravelOfDay) {
        db.get().travelDao().updateTravelOfDay(travelOfDay)
    }

    fun getTravelCards():Single<MutableList<TravelCard>> {
        return db.get().travelDao().getTravelCard(prefUtilService.getLong(AndroidPrefUtilService.Key.SELECTED_TRAVELING_OF_DAY_ID).blockingGet())
    }

}