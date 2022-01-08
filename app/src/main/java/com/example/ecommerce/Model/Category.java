package com.example.ecommerce.Model;

public class Category {
    private String Categoryname;
    private String Categoryimage;
    private String CategoryId;


    public Category( String Categoryimage , String Categoryname) {
        this.Categoryname = Categoryname;
        this.Categoryimage = Categoryimage;
    }

    public Category( String categoryId , String categoryimage , String categoryname) {
        this.Categoryname = categoryname;
        this.Categoryimage = categoryimage;
        this.CategoryId = categoryId;
    }

    public String getCategoryId( ) {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.CategoryId = categoryId;
    }

    public String getCategoryname() {
        return Categoryname;
    }

    public void setCategoryname(String productname) {

        this.Categoryname = productname;
    }

    public String getCategoryimage() {
        return Categoryimage;
    }

    public void setCategoryimage(String productimage) {

        this.Categoryimage = productimage;
    }
}
