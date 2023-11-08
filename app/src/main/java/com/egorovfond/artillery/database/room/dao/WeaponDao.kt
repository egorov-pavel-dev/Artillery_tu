package com.egorovfond.artillery.database.room.dao

import androidx.room.*
import com.egorovfond.artillery.database.room.entity.WeaponEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


@Dao
interface  WeaponDao {
    @Insert(entity = WeaponEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertWeapon(weapon: WeaponEntity): Completable

    @Update(entity = WeaponEntity::class)
    fun updateWeapon(weapon: WeaponEntity): Completable

    @Delete(entity = WeaponEntity::class)
    fun deleteWeapon(weapon: WeaponEntity): Completable

    @Query("DELETE FROM WeaponEntity")
    fun delete(): Completable

    @Query("SELECT * FROM WeaponEntity WHERE name == :name ORDER BY name ASC")
    fun getWeaponByName(name: String): Single<WeaponEntity>

    @Query("SELECT mil FROM WeaponEntity WHERE name == :name")
    fun getMilByName(name: String): Single<Int>

    @Query("SELECT * FROM WeaponEntity")
    fun getAllWeapon(): Single<List<WeaponEntity>>
}