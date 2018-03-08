package com.mlijo.aryaym.konsumen_mlijo.DBModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AryaYM on 08/07/2017.
 */

public class ProdukModel implements Serializable {

    String idPenjual, namaProduk, idKategori,  digitSatuan, namaSatuan, deskripsiProduk;
    Double hargaProduk;
    String idProduk;
    ArrayList<String> gambarProduk = new ArrayList<>();
    Long waktuDibuat;


    public ProdukModel(){}

    public Long getWaktuDibuat() {
        return waktuDibuat;
    }

    public String getIdPenjual() {
        return idPenjual;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public String getKategoriProduk() {
        return idKategori;
    }

    public Double getHargaProduk() {
        return hargaProduk;
    }

    public String getSatuanProduk() {
        return digitSatuan;
    }

    public String getNamaSatuan() {
        return namaSatuan;
    }

    public String getDeskripsiProduk() {
        return deskripsiProduk;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public ArrayList<String> getGambarProduk() {
        return gambarProduk;
    }
}
