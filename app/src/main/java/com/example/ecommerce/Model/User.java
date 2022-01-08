package com.example.ecommerce.Model;

public class User {


        String userName,email,password,userId , image , adress ;

//    public User( String userName, String email, String password, String userId) {
//        this.userName = userName;
//        // this.ProfilePic = profilePic;
//        this.email = email;
//        this.password = password;
//        this.userId = userId;
//
//    }

        public User(String userName, String email, String password) {
            this.userName = userName;
            this.email = email;
            this.password = password;
        }

    public User(String userName, String email, String password,String image) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.image = image;
    }
        public User( ) {

        }

    public User(String userName, String email, String password, String userId, String image, String adress) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.image = image;
        this.adress = adress;
    }

////
//        public String getProfilePic() {
//            return ProfilePic;
//        }
//
//        public void setProfilePic(String profilePic) {
//            ProfilePic = profilePic;
//        }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


}
