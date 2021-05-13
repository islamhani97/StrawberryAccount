package com.islam.strawberryaccount.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.islam.strawberryaccount.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "cash",
        foreignKeys = @ForeignKey(entity = Trader.class,
                parentColumns = "id",
                childColumns = "trader_id",
                onDelete = ForeignKey.CASCADE))
@TypeConverters(DateConverter.class)
public class Cash {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "trader_id")
    private Long traderId;

    @ColumnInfo(name = "value")
    private Double value;

    @ColumnInfo(name = "date")
    private Date date;


    public Cash() {
    }

    @Ignore
    public Cash(Long id, Long traderId, Double value, Date date) {
        this.id = id;
        this.traderId = traderId;
        this.value = value;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Ignore
    public Cash getInstance() {

        return new Cash(id, traderId, value, date);
    }
}
