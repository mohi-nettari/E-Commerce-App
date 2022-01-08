package com.example.ecommerce.Model;

public class product {
   private String date,pcategory,pdesc,pid,pimage,pname,pprice,time;

   public product(String date, String pcategory, String pdesc, String pid, String pimage, String pname, String pprice, String time) {
      this.date = date;
      this.pcategory = pcategory;
      this.pdesc = pdesc;
      this.pid = pid;
      this.pimage = pimage;
      this.pname = pname;
      this.pprice = pprice;
      this.time = time;
   }

   public product() {

   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getPcategory() {
      return pcategory;
   }

   public void setPcategory(String pcategory) {
      this.pcategory = pcategory;
   }

   public String getPdesc() {
      return pdesc;
   }

   public void setPdesc(String pdesc) {
      this.pdesc = pdesc;
   }

   public String getPid() {
      return pid;
   }

   public void setPid(String pid) {
      this.pid = pid;
   }

   public String getPimage() {
      return pimage;
   }

   public void setPimage(String pimage) {
      this.pimage = pimage;
   }

   public String getPname() {
      return pname;
   }

   public void setPname(String pname) {
      this.pname = pname;
   }

   public String getPprice() {
      return pprice;
   }

   public void setPprice(String pprice) {
      this.pprice = pprice;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }
}
