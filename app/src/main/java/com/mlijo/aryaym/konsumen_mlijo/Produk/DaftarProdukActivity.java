package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.Produk.Presenter.DaftarProdukPresenter;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 03/09/2017.
 */

public class DaftarProdukActivity extends BaseActivity implements
        View.OnClickListener,
        DaftarProdukAdapter.OnProdukListener,
        KategoriFilterDialogFragment.KategoriFilterListener,
        SortingDialogFragment.SortingListener{

    @BindView(R.id.recycler_my_produk)
    RecyclerView mRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;
    @BindView(R.id.btn_sort)
    Button btnSort;
    @BindView(R.id.btn_filter)
    Button btnFilter;
    @BindView(R.id.btn_layout)
    LinearLayout btnLayout;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    public static String kategoriProduk;
    public String title;
    private List<ProdukModel> produkList = new ArrayList<>();
    private DaftarProdukAdapter daftarProdukAdapter;
    private DaftarProdukPresenter presenter;
    private KategoriFilterDialogFragment mFilterDialog;
    private SortingDialogFragment mSortingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_produk);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra(Constants.TITLE);
        kategoriProduk = getIntent().getStringExtra(Constants.ID_KATEGORI);
        presenter = new DaftarProdukPresenter(this);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection(Constants.PRODUK_REGULER)
                .orderBy("waktuDibuat", Query.Direction.ASCENDING) // sek error
                .limit(10);

        Log.d("nilaiKategori", "Current data: " + kategoriProduk);

        daftarProdukAdapter = new DaftarProdukAdapter(mQuery, this){
            @Override
            protected void onDataChanged(){
                if (getItemCount() == 0){
                    noItemData();
                    Log.d("nilai Data", "Current item data: " + getItemCount());
                }else {
                    showItem();
                    Log.d("nilai Data", "Current item data: " + getItemCount());
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e){

            }
        };

        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setAdapter(daftarProdukAdapter);
        mFilterDialog = new KategoriFilterDialogFragment();
        mSortingDialog = new SortingDialogFragment();
        defaultDataFilter();

        btnSort.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    public void showItem() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        imgNoResult.setVisibility(View.GONE);
        btnLayout.setVisibility(View.VISIBLE);
    }

    public void showItemData(ProdukModel produkModel) {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        imgNoResult.setVisibility(View.GONE);
        btnLayout.setVisibility(View.VISIBLE);
        produkList.add(produkModel);
        daftarProdukAdapter.notifyDataSetChanged();
    }

    public void noItemData(){
        imgNoResult.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (daftarProdukAdapter != null){
            daftarProdukAdapter.startListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v== btnSort){
            //showDialogSorting();
            mSortingDialog.show(getSupportFragmentManager(), SortingDialogFragment.TAG);
            //Toast.makeText(DaftarProdukActivity.this, "Masih dalam pembuatan", Toast.LENGTH_LONG).show();
        }else if (v == btnFilter){
            mFilterDialog.show(getSupportFragmentManager(), KategoriFilterDialogFragment.TAG);
        }
    }

    @Override
    public void onProdukSelected(DocumentSnapshot produk) {
        Intent intent = new Intent(this, DetailProdukActivity.class);
        intent.putExtra(Constants.ID_PRODUK, produk.getId());
        intent.putExtra(Constants.ID_PENJUAL, (String) produk.getData().get(Constants.ID_PENJUAL));
        intent.putExtra(Constants.ID_KATEGORI, (String) produk.getData().get(Constants.ID_KATEGORI));
        intent.putExtra(Constants.HARGA_PRODUK, (Long) produk.getData().get(Constants.HARGA_PRODUK));

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private void defaultDataFilter(){
        onFilter(FilterProduk.getDefault(kategoriProduk));
    }

    public static String kategori(){
        String selectKategori = kategoriProduk;
        return selectKategori;
    }

    @Override
    public void onFilter(FilterProduk filterProduk) {

        Query query = mFirestore.collection(Constants.PRODUK_REGULER);

        if (filterProduk.hasKategori()){
            query = query.whereEqualTo(Constants.ID_KATEGORI, filterProduk.getKategori());
        }
        //Log.d("nilai Harga Awal", "Current value: " + filterProduk.getKategori());
        if (filterProduk.hasLokasi()){
            query = query.whereEqualTo(Constants.ID_LOKASI, filterProduk.getLokasi());
        }
        query = query.whereGreaterThan(Constants.HARGA_PRODUK, filterProduk.getHarga_awal());
        Log.d("nilai Harga Awal", "Current value: " + filterProduk.getHarga_awal());
        query = query.whereLessThan(Constants.HARGA_PRODUK, filterProduk.getHarga_akhir());
        Log.d("nilai Harga Akhir", "Current value: " + filterProduk.getHarga_akhir());

        query = query.limit(10);

        daftarProdukAdapter.setQuery(query);

        presenter.setFilters(filterProduk);
    }

    @Override
    public void onSorting(FilterProduk sortingProduk) {

        Query query = mFirestore.collection(Constants.PRODUK_REGULER);

        if (sortingProduk.hasKategori()){
            query = query.whereEqualTo(Constants.ID_KATEGORI, sortingProduk.getKategori());
        }
        Log.d("nilai Harga Awal", "Current value: " + sortingProduk.getKategori());

        query = query.orderBy(sortingProduk.getSortBy(), sortingProduk.getSortDirection());

        query = query.limit(10);

        daftarProdukAdapter.setQuery(query);
    }
}
