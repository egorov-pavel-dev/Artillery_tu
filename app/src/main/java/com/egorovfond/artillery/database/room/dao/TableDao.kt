package com.egorovfond.artillery.database.room.dao

import androidx.room.*
import com.egorovfond.artillery.database.room.entity.TableEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


@Dao
interface TableDao {
    @Insert(entity = TableEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTable(table: TableEntity): Completable

    @Update(entity = TableEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateTable(table: TableEntity): Completable

    @Delete(entity = TableEntity::class)
    fun deleteTable(table: TableEntity): Completable

    @Query("DELETE FROM TableEntity")
    fun delete(): Completable

    @Query("SELECT * FROM TableEntity WHERE weapon == :weapon ORDER BY weapon, bulet, mortir, D ASC")
    fun getTableByWeapon(weapon: String): Single<List<TableEntity>>

    @Query("SELECT * FROM TableEntity WHERE weapon == :weapon AND bulet == :bullet AND nameTable == :base ORDER BY weapon, nameTable, bulet, mortir, D ASC")
    fun getTableByWeaponAndBullet(weapon: String, bullet: String, base: String): Single<List<TableEntity>>

    @Query("SELECT * FROM TableEntity ORDER BY weapon, bulet, mortir, D ASC")
    fun getAllWeapon(): Single<List<TableEntity>>

    @Query("SELECT DISTINCT bulet FROM TableEntity WHERE weapon == :weapon AND nameTable == :base ORDER BY bulet ASC")
    fun getWeaponBullet(weapon: String, base: String): Single<List<String>>

    @Query("SELECT DISTINCT nameTable FROM TableEntity WHERE weapon == :weapon ORDER BY nameTable ASC")
    fun getWeaponBase(weapon: String): Single<List<String>>

}