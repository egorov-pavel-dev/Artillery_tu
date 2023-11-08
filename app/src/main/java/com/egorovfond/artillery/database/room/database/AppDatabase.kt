package com.egorovfond.artillery.database.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.egorovfond.artillery.database.room.dao.TableDao
import com.egorovfond.artillery.database.room.dao.WeaponDao
import com.egorovfond.artillery.database.room.entity.TableEntity
import com.egorovfond.artillery.database.room.entity.WeaponEntity

const val db_version = 1
const val db_name = "artilery"

@Database(entities = [WeaponEntity::class, TableEntity::class], version = db_version)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weaponDao(): WeaponDao
    abstract fun tableDao(): TableDao


    companion object {
        var INSTANCE: AppDatabase? = null
        private val TAG = "AppDatabase"

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, db_name)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}