package com.example.ecommerce.Model;


public class AdminOrders {

    private String name,phone,address,city,date,time,total,status,userid;

//    public AdminOrders(String name, String phone, String address, String city, String state, String date, String time, String totalamount,String status) {
//        this.name = name;
//        this.phone = phone;
//        this.address = address;
//        this.city = city;
//        this.state = state;
//        this.date = date;
//        this.time = time;
//        this.totalamount = totalamount;
//
//    }
//
//public AdminOrders(String name, String phone, String address, String city, String date, String time, String totalamount) {
//    this.name = name;
//    this.phone = phone;
//    this.address = address;
//    this.city = city;
//    this.state = state;
//    this.date = date;
//    this.time = time;
//    this.totalamount = totalamount;
//    this.status = status;
//
//}
    public AdminOrders(String address, String city, String date, String name, String phone, String status, String time, String total,String userid) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.time = time;
        this.total = total;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public AdminOrders(){


}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
