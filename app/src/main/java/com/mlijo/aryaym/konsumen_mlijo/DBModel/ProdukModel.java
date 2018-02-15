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

    public ProdukModel(String idProduk, String idKategori){
        this.idProduk = idProduk;
        this.idKategori = idKategori;
    }

    public ProdukModel(String idPenjual, String namaProduk, String idKategori, ArrayList<String> gambarProduk, Double hargaProduk, String digitSatuan, String namaSatuan, String deskripsiProduk, String idProduk) {
        this.idPenjual = idPenjual;
        this.namaProduk = namaProduk;
        this.idKategori = idKategori;
        this.gambarProduk = gambarProduk;
        this.hargaProduk = hargaProduk;
        this.digitSatuan = digitSatuan;
        this.namaSatuan = namaSatuan;
        this.deskripsiProduk = deskripsiProduk;
        this.idProduk = idProduk;
    }

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
