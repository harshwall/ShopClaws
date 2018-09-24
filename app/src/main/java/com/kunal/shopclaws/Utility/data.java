package com.kunal.shopclaws.Utility;

public class data {
    String name,img;
    Long revenue;

    public data()
    {

    }

    public data(String name, Long revenue,String img) {
        this.name = name;
        this.img=img;
        this.revenue = revenue;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }
}
