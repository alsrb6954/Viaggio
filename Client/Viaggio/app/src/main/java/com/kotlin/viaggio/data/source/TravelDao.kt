package com.kotlin.viaggio.data.source

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kotlin.viaggio.data.`object`.Travel
import com.kotlin.viaggio.data.`object`.TravelCard
import com.kotlin.viaggio.data.`object`.TravelOfDay
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TravelDao {
    // Travel
    @Insert
    fun insertTravel(travel: Travel): Single<Long>

    @Insert
    fun insertAllTravel(vararg travel: Travel)

    @Query("SELECT * FROM travels Where endDate Is not null Order By startDate Desc")
    fun getTravels(): DataSource.Factory<Int, Travel>

    @Query("SELECT * FROM travels WHERE id IN(:id)")
    fun getTravel(id: Long): Single<Travel>

    @Update
    fun updateTravel(travel: Travel)

    // travelOfDay
    @Insert
    fun insertTravelOfDay(travelOfDay: TravelOfDay): Single<Long>

    @Insert
    fun insertAllTravelOfDay(vararg travelOfDay: TravelOfDay): Single<MutableList<Long>>

    @Query("SELECT * FROM travelOfDays WHERE travelId IN(:travelId) ORDER BY date DESC")
    fun getTravelOfDays(travelId: Long): Single<MutableList<TravelOfDay>>

    @Query("SELECT * FROM travelOfDays WHERE id = :id")
    fun getTravelOfDay(id:Long): Single<TravelOfDay>

    @Query("SELECT * FROM travelOfDays WHERE travelOfDay = :day And travelId = :travelId")
    fun getTravelOfDayCount(day: Int, travelId: Long): Single<TravelOfDay>

    @Update
    fun updateTravelOfDay(travelOfDay: TravelOfDay)

    // travelCard
    @Insert
    fun insertTravelCard(travelCard: TravelCard): Single<Long>

    @Query("SELECT * FROM travelCards WHERE travelOfDayId IN(:travelOfDayId) ORDER BY enrollOfTime DESC")
    fun getTravelCardsPaged(travelOfDayId: Long): DataSource.Factory<Int, TravelCard>

    @Query("SELECT * FROM travelCards WHERE travelOfDayId IN(:travelOfDayId) ORDER BY enrollOfTime DESC")
    fun getTravelCard(travelOfDayId: Long): Single<MutableList<TravelCard>>

}