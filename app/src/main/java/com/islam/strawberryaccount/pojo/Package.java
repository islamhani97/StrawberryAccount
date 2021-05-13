package com.islam.strawberryaccount.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.islam.strawberryaccount.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "package",
        foreignKeys = @ForeignKey(entity = Trader.class,
                parentColumns = "id",
                childColumns = "trader_id",
                onDelete = ForeignKey.CASCADE))
@TypeConverters(DateConverter.class)
public class Package {




    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "trader_id")
    private Long traderId;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "amount")
    private Double amount;

    @ColumnInfo(name = "date")
    private Date date;


    public Package() {
    }

    @Ignore
    public Package(Long id, Long traderId, Double price, Double amount, Date date) {
        this.id = id;
        this.traderId = traderId;
        this.price = price;
        this.amount = amount;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    @Ignore
    public Package getInstance() {

        return new Package(id, traderId, price, amount, date);
    }


}
