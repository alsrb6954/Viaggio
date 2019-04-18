package com.kotlin.viaggio.data.`object`

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey(autoGenerate = true) var id:Long = 0,
    var continent: String, var country: String,
    var url: String,
    @Suppress("ArrayInDataClass") var area: ArrayList<String>
)


