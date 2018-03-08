package com.mlijo.aryaym.konsumen_mlijo.Produk.Presenter;

import com.mlijo.aryaym.konsumen_mlijo.Produk.DaftarProdukPenjualActivity;
import com.mlijo.aryaym.konsumen_mlijo.Produk.FilterProduk;

/**
 * Created by AryaYM on 11/02/2018.
 */

public class DaftarProdukPenjualPresenter {

    private DaftarProdukPenjualActivity view;
    private FilterProduk mFilters;

    public DaftarProdukPenjualPresenter(DaftarProdukPenjualActivity view){
        this.view = view;
    }

    public void setFilters(FilterProduk filters){
        this.mFilters = filters;
    }
}
