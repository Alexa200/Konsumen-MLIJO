package com.mlijo.aryaym.konsumen_mlijo.DBModel;

/**
 * Created by AryaYM on 02/09/2017.
 */

public class KategoriModel {
    private String idKategori;
    private int gambarKategori;
    private String title;

    public KategoriModel(){}

    public KategoriModel(String idKategori, int gambarKategori, String title){
        this.idKategori = idKategori;
        this.gambarKategori = gambarKategori;
        this.title = title;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public int getGambarKategori() {
        return gambarKategori;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
