package com.islam.strawberryaccount.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.InfoObject;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Total;
import com.islam.strawberryaccount.pojo.Trader;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT * FROM trader")
    Observable<List<Trader>> getAllTraders();

    @Query("SELECT * FROM trader WHERE id = :traderId")
    Observable<Trader> getTrader(Long traderId);

    @Insert
    Completable insertTrader(Trader trader);

    @Update
    Completable updateTrader(Trader trader);

    @Delete
    Completable deleteTrader(Trader trader);

    //===========================================================

    @Query("SELECT * FROM package")
    Observable<List<Package>> getAllPackages();

    @Query("SELECT * FROM package WHERE trader_id = :traderId")
    Observable<List<Package>> getAllPackagesForTrader(Long traderId);

    @Query("SELECT * FROM package WHERE trader_id = :traderId AND date BETWEEN :startDate AND :endDate")
    Single<List<Package>> searchInPackagesForTrader(Long traderId , Long startDate , Long endDate);

    @Insert
    Completable insertPackage(Package aPackage);

    @Update
    Completable updatePackage(Package aPackage);

    @Delete
    Completable deletePackage(Package aPackage);

    //===========================================================

    @Query("SELECT * FROM cash")
    Observable<List<Cash>> getAllCashes();

    @Query("SELECT * FROM cash WHERE trader_id = :traderId")
    Observable<List<Cash>> getAllCashesForTrader(Long traderId);


    @Query("SELECT * FROM cash WHERE trader_id = :traderId AND date BETWEEN :startDate AND :endDate")
    Single<List<Cash>> searchInCashesForTrader(Long traderId , Long startDate , Long endDate);

    @Insert
    Completable insertCash(Cash cash);

    @Update
    Completable updateCash(Cash cash);

    @Delete
    Completable deleteCash(Cash cash);

}
