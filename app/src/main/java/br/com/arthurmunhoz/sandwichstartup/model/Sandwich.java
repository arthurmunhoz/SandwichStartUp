package br.com.arthurmunhoz.sandwichstartup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sandwich implements Parcelable
{
    private String image;
    private String name;
    private String description;
    private String ingredients;
    private String price;
    private int id;
    private int lettuce;
    private int egg;
    private int bread;
    private int cheese;
    private int burger;
    private int bacon;


    public Sandwich()
    {
        this.cheese = 0;
        this.lettuce = 0;
        this.egg = 0;
        this.bread = 0;
        this.bacon = 0;
        this.burger = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(String ingredients)
    {
        this.ingredients = ingredients;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public int getLettuce()
    {
        return lettuce;
    }

    public void setLettuce(int lettuce)
    {
        this.lettuce = lettuce;
    }

    public int getEgg()
    {
        return egg;
    }

    public void setEgg(int egg)
    {
        this.egg = egg;
    }

    public int getBread()
    {
        return bread;
    }

    public void setBread(int bread)
    {
        this.bread = bread;
    }

    public int getCheese()
    {
        return cheese;
    }

    public void setCheese(int cheese)
    {
        this.cheese = cheese;
    }

    public int getBurger()
    {
        return burger;
    }

    public void setBurger(int burger)
    {
        this.burger = burger;
    }

    public int getBacon()
    {
        return bacon;
    }

    public void setBacon(int bacon)
    {
        this.bacon = bacon;
    }

    //Parcelable implementation
    protected Sandwich(Parcel in)
    {
        image = in.readString();
        name = in.readString();
        description = in.readString();
        ingredients = in.readString();
        price = in.readString();
        id = in.readInt();
        lettuce = in.readInt();
        egg = in.readInt();
        bread = in.readInt();
        cheese = in.readInt();
        burger = in.readInt();
        bacon = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(ingredients);
        dest.writeString(price);
        dest.writeInt(id);
        dest.writeInt(lettuce);
        dest.writeInt(egg);
        dest.writeInt(bread);
        dest.writeInt(cheese);
        dest.writeInt(burger);
        dest.writeInt(bacon);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<Sandwich> CREATOR = new Creator<Sandwich>()
    {
        @Override
        public Sandwich createFromParcel(Parcel in)
        {
            return new Sandwich(in);
        }

        @Override
        public Sandwich[] newArray(int size)
        {
            return new Sandwich[size];
        }
    };

}
