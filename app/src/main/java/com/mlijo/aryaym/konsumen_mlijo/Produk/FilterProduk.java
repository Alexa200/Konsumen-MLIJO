package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.text.TextUtils;

/**
 * Created by AryaYM on 13/02/2018.
 */

public class FilterProduk {

    private String kategori = null;
    private String lokasi = null;
    private double harga_awal = 0;
    private double harga_akhir = 10000000;

    public FilterProduk(){}

    public static FilterProduk getDefault(String kategori){
        FilterProduk filterProduk = new FilterProduk();
        filterProduk.setKategori(kategori);

        return filterProduk;
    }

    public boolean hasKategori(){
        return !(TextUtils.isEmpty(kategori));
    }

    public boolean hasLokasi(){
        return !(TextUtils.isEmpty(lokasi));
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public double getHarga_awal() {
        return harga_awal;
    }

    public void setHarga_awal(double harga_awal) {
        this.harga_awal = harga_awal;
    }

    public double getHarga_akhir() {
        return harga_akhir;
    }

    public void setHarga_akhir(double harga_akhir) {
        this.harga_akhir = harga_akhir;
    }
}
