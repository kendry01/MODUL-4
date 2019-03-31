package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Model;

public class MenuProduk {

    private String namaMenu, harga, deskripsi,image;

    public MenuProduk(){}

    public MenuProduk(String namaMenu, String harga, String deksripsi, String image){

        this.namaMenu = namaMenu;
        this.harga = harga;
        this.deskripsi = deksripsi;
        this.image = image;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
