package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Model;

public class User {

    private String nama, email, password;

    public User(){}

    public User (String nama, String email, String password){

        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
}
