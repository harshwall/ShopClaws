package com.kunal.shopclaws.fragments;

public class Blog {
    private String title,desc,url,price,seller_id;
    private String stock_size;


    public Blog(String title,String url, String desc,String price,String stock_size) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.price = price;
        this.stock_size=stock_size;
    }
    public Blog()
    {

    }


    public String getStock_size() {
        return stock_size;
    }

    public void setStock_size(String stock_size) {
        this.stock_size = stock_size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}