@file:Suppress("unused")

package com.kotlin.viaggio.event

import android.graphics.Bitmap
import com.kotlin.viaggio.data.obj.Area
import com.kotlin.viaggio.data.obj.Country
import com.kotlin.viaggio.data.obj.ImageData
import com.kotlin.viaggio.data.obj.ThemeData
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxEventBus @Inject constructor() {
    val travelOfGo:BehaviorSubject<Any> = BehaviorSubject.create()
    val travelOfFirstImage: BehaviorSubject<Bitmap> = BehaviorSubject.create()
    val travelOfTheme: BehaviorSubject<List<ThemeData>> = BehaviorSubject.create()
    val travelOfDayTheme: BehaviorSubject<List<String>> = BehaviorSubject.create()

    val travelCardTransportation: BehaviorSubject<String> = BehaviorSubject.create()
    val travelOfDayImage: BehaviorSubject<String> = BehaviorSubject.create()
    val travelOfCountryChange:BehaviorSubject<Any> = BehaviorSubject.create()


    val travelUpdate:PublishSubject<Any> = PublishSubject.create()
    val travelCardImages: BehaviorSubject<List<Bitmap>> = BehaviorSubject.create()
    val travelCacheImages: BehaviorSubject<List<ImageData>> = BehaviorSubject.create()
    val travelingStartOfDay:BehaviorSubject<List<Date>> = BehaviorSubject.create()
    val travelCountry:BehaviorSubject<Country> = BehaviorSubject.create()
    val travelCity:PublishSubject<List<Area>> = PublishSubject.create()
    val travelSelectedCity:BehaviorSubject<List<Area>> = BehaviorSubject.create()
    val travelingOption:PublishSubject<Any> = PublishSubject.create()
    val travelingCalendar:PublishSubject<Calendar> = PublishSubject.create()
    val travelCardUpdate: PublishSubject<Any> = PublishSubject.create()
    val userUpdate: PublishSubject<Any> = PublishSubject.create()
    val travelCardChange: PublishSubject<Int> = PublishSubject.create()
    val uploadCheck: PublishSubject<Boolean> = PublishSubject.create()
    val sync: PublishSubject<Boolean> = PublishSubject.create()
    val userImage: PublishSubject<String> = PublishSubject.create()
    val travelCardDelete: PublishSubject<Any> = PublishSubject.create()
    val travelDelete: PublishSubject<Any> = PublishSubject.create()

    val completeLock: PublishSubject<Any> = PublishSubject.create()
    val travelCardImageModifyCount: BehaviorSubject<Int> = BehaviorSubject.create()
}