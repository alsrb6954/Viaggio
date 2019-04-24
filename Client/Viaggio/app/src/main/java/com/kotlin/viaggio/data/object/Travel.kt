package com.kotlin.viaggio.data.`object`

import androidx.databinding.ObservableInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "travels")
data class Travel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @Suppress("ArrayInDataClass") var area: MutableList<Area> = mutableListOf(),
    var userExist: Boolean = false,
    var title: String = "재미있는 여행",
    // 0 해외여행, 1 국내여행
    var travelKind:Int =0,
    var startDate: Date? = null,
    var endDate: Date? = null,
    @Suppress("ArrayInDataClass") var theme: MutableList<String> = mutableListOf(),
    var imageName: String = "",
    var imageUrl: String = "",
    var share: Boolean = false,
    var isDelete:Boolean = false
)

data class Traveled(
    var id: Long = 0,
    var theme:String = "",
    var period:String = "",
    var title:String ="",
    var countries:String = ""
)

@Entity(tableName = "travelOfDays")
data class TravelOfDay(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var travelId: Long = 0,
    @Suppress("ArrayInDataClass") var dayCountries: ArrayList<String> = arrayListOf(),
    @Suppress("ArrayInDataClass") var theme: ArrayList<String> = arrayListOf(),
    var date: Date = Date(),
    var travelOfDay: Int = 1,
    var themeImageName: String = ""
)

@Entity(tableName = "travelCards")
data class TravelCard(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var travelId: Long = 0,
    var travelOfDay: Int = 1,
    var country:String = "",
    @Suppress("ArrayInDataClass") var theme: ArrayList<String> = arrayListOf(),
    @Suppress("ArrayInDataClass") var imageNames: ArrayList<String> = arrayListOf(),
    @Suppress("ArrayInDataClass") var imageUrl: ArrayList<String> = arrayListOf(),
    var content: String = "",
    var date: Date = Date()
)

data class ImageData(
    var imageName: String = "",
    var chooseCountList:ObservableInt = ObservableInt(0)
)

data class TravelCardValue(
    var id: Long = 0,
    var travelId: Long = 0,
    var travelOfDay: Int = 1,
    var country:String = "",
    var theme: String = "",
    var imageName: String = "",
    var content: String = ""
)