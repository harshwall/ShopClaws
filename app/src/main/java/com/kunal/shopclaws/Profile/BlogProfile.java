package com.kunal.shopclaws.Profile;

public class BlogProfile {
    String name,img,mobile,verify,solditems;
    Long revenue;

    public BlogProfile( String name,String img, String mobile,String verify,Long revenue,String solditems) {

        this.name= name;
        this.img = img;
        this.mobile = mobile;
        this.verify=verify;
        this.revenue=revenue;
        this.solditems=solditems;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public String getSolditems() {
        return solditems;
    }

    public void setSolditems(String solditems) {
        this.solditems = solditems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlogProfile() {
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
