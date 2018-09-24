package com.kunal.shopclaws.UploadImage;

public class ImageUpload {
    public String title;
    public String url;
    public String desc;
    public String price;
    public String stock_size;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc(){ return desc; }

    public String getPrice(){return price;}

    public String getStock_size() { return stock_size; }

    public ImageUpload(String title, String url, String desc, String price, String stock_size) {
        this.title = title;
        this.url = url;
        this.desc=desc;
        this.price=price;
        this.stock_size=stock_size;
    }
}
