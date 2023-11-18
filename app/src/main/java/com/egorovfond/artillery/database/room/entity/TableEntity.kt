package com.egorovfond.artillery.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("bulet"), Index("weapon"), Index("D")],
    foreignKeys = [ForeignKey(entity = WeaponEntity::class,
        parentColumns = ["name"], childColumns = ["weapon"])])
data class TableEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val weapon: String,
    val mortir: Boolean,
    val bulet: String,
    val D: Int, /// Дальность
    val II: Int, // Прицел
    val X_tis: Float, // Изменение дальности на 1 тыс Хтыс
    val II_delt: Float, // Изменение прицела на 50м дальности
    val IIh_delt: Float, // Изменение прицела на 100м высоты
    val Z: Float, // Изменение угла. Деривация
    val Zw_delt: Float, // Изменение угла боковой ветер на 1м/с
    val Xw_delt: Float, // Изменение прицела, продольный ветер 1 м/с
    val Xt_delt: Float, // Изменение температуры воздуха на 10 градусов. норма 15.
    val Xh_delt: Float, // Изенение прицела, давление воздуха на 10hPa, норма 1013.25
    val nameTable: String = "0м/15С/1013.25гПа",
    val baseH: Float = 1013.25f,
    val baseTemp: Float = 15f,
    val baseHeigth: Int = 0,
    val time: Float = 0f
)