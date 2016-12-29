package com.codeitnow.shopoffer;

/**
 * Created by Rahul Malhotra on 12/29/2016.
 */

public class Shop {
    public String id;
    public String desc;
    public String name;
    public String offer;

    public void setData(String id, String desc, String name, String offer)
    {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.offer = offer;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDesc()
    {
        return this.desc;
    }

    public String getOffer()
    {
        return this.offer;
    }
    public String getName()
    {
        return this.name;
    }

}
