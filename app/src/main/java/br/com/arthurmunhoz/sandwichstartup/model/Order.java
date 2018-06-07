package br.com.arthurmunhoz.sandwichstartup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable
{
    //Attributes
    private List<Sandwich> list = new ArrayList<>();

    //Constructor
    public Order()
    {}


    protected Order(Parcel in)
    {
        list = in.createTypedArrayList(Sandwich.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>()
    {
        @Override
        public Order createFromParcel(Parcel in)
        {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size)
        {
            return new Order[size];
        }
    };

    //Getters and Setters
    public List<Sandwich> getList()
    {
        return list;
    }

    public void setList(List<Sandwich> list)
    {
        this.list = list;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeTypedList(list);
    }

    public void add(Sandwich sandwich)
    {
        this.list.add(sandwich);
    }

    public void remove(Sandwich sandwich)
    {
        list.remove(sandwich);
    }
}
