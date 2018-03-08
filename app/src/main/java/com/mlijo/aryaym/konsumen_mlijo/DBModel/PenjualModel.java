package com.mlijo.aryaym.konsumen_mlijo.DBModel;

import java.util.HashMap;

/**
 * Created by AryaYM on 13/10/2017.
 */

public class PenjualModel {

    private String uid;
    private String deviceToken, namaKategori;
    private HashMap<String, Object> infoKategori;
    private HashMap<String, Object> infoLokasi;
    private HashMap<String, Object> detailPenjual;
    private HashMap<String, Object> kategori;
    private boolean statusBerjualan;
    private int nilaiKategori;

    public PenjualModel(){}

    public HashMap<String, Object> getDetailPenjual() {
        return detailPenjual;
    }

    public HashMap<String, Object> getInfoLokasi() {
        return infoLokasi;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public HashMap<String, Object> getInfoKategori() {
        return infoKategori;
    }

    public boolean isStatusBerjualan() {
        return statusBerjualan;
    }

    public HashMap<String, Object> getKategori() {
        return kategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public int getNilaiKategori() {

        return nilaiKategori;
    }
}
