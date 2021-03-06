package com.kotlin.viaggio.data.obj

import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "themes")
data class Theme(
     @PrimaryKey var theme: String = "",
    var authority:Boolean = false
)

data class ThemeData(
    var theme: String = "",
    var authority:Boolean = false,
    var select:ObservableBoolean = ObservableBoolean(false)
)

data class ThemeBody(
    var themes:List<Theme>
)