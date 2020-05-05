package com.nadu.foodsearch;

public class UserInfo {
    private String nickname;
    private String age;
    private String gender;

    public UserInfo(String nickname,  String age, String gender) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}