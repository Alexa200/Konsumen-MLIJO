package com.mlijo.aryaym.konsumen_mlijo.Produk.Presenter;

import com.mlijo.aryaym.konsumen_mlijo.Produk.DaftarProdukActivity;
import com.mlijo.aryaym.konsumen_mlijo.Produk.FilterProduk;

/**
 * Created by AryaYM on 11/02/2018.
 */

public class DaftarProdukPresenter {

    private DaftarProdukActivity view;
    private FilterProduk mFilters;

    public DaftarProdukPresenter(DaftarProdukActivity view){
        this.view = view;
    }


    public FilterProduk getFilters() {
        return mFilters;
    }

    public void setFilters(FilterProduk filters){
        this.mFilters = filters;
    }
}
