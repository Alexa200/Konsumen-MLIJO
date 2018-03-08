package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.Obrolan.ObrolanActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailPenjualActivity extends BaseActivity implements ValueEventListener {

    @BindView(R.id.imgPenjual)
    ImageView imgPenjual;
    @BindView(R.id.txt_nama_penjual)
    TextView txtNamaPenjual;
    @BindView(R.id.txt_alamat_lengkap)
    TextView txtAlamatLengkap;
    @BindView(R.id.txt_nomortelp_penjual)
    TextView txtNomortelpPenjual;
    @BindView(R.id.rb_kualitas_produk)
    RatingBar rbKualitasProduk;
    @BindView(R.id.rb_kualitas_pelayanan)
    RatingBar rbKualitasPelayanan;
    @BindView(R.id.txt_hari_operasional)
    TextView txtHariOperasional;
    @BindView(R.id.txt_jam_operasional)
    TextView txtJamOperasional;
    @BindView(R.id.txt_kecamatan)
    TextView txtKecamatan;
    @BindView(R.id.btn_pesan_produk)
    Button btnPesanProduk;
    @BindView(R.id.btn_kirim_obrolan)
    Button btnKirimObrolan;
    @BindView(R.id.kategori_penjual_list)
    RecyclerView mRecycler;

    private String penjualId, avatarPenjual, namaPenjual;
    private DatabaseReference mDatabase, penjualRef;
    private DaftarKategoriPenjualAdapter daftarKategoriPenjualAdapter;
    private List<PenjualModel> kategoriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penjual);
        setTitle(R.string.title_activity_detail_penjual);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


        namaPenjual = getIntent().getStringExtra(Constants.NAMA);
        penjualId = getIntent().getExtras().getString(Constants.ID_PENJUAL);
        avatarPenjual = getIntent().getStringExtra(Constants.AVATAR);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        penjualRef = mDatabase.child(Constants.PENJUAL).child(penjualId);
        penjualRef.addValueEventListener(this);

        daftarKategoriPenjualAdapter = new DaftarKategoriPenjualAdapter(this, kategoriList);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setAdapter(daftarKategoriPenjualAdapter);
        getRatingPenjual();
        getKategori();

    }

    private void getKategori() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).child("kategori")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                        kategoriList.add(penjualModel);
                        daftarKategoriPenjualAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    //load data penjual
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
        try {
            txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
            txtNomortelpPenjual.setText(penjualModel.getDetailPenjual().get(Constants.TELPON).toString());
            ImageLoader.getInstance().loadImageAvatar(DetailPenjualActivity.this, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), imgPenjual);
            txtAlamatLengkap.setText(penjualModel.getDetailPenjual().get(Constants.ALAMAT).toString());

            txtHariOperasional.setText(penjualModel.getInfoLokasi().get(Constants.HARI_MULAI).toString() + " - " + penjualModel.getInfoLokasi().get(Constants.HARI_SELESAI).toString());
            txtJamOperasional.setText(penjualModel.getInfoLokasi().get(Constants.JAM_MULAI).toString() + " - " + penjualModel.getInfoLokasi().get(Constants.JAM_SELESAI).toString());
            txtKecamatan.setText(penjualModel.getInfoLokasi().get(Constants.KECAMATAN).toString());
            mRecycler.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void getRatingPenjual() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.ULASAN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float totalRatingProduk = 0, totalRatingPelayanan = 0;
                int count = 0;
                if (dataSnapshot != null) {
                    try {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            float ratingProduk = data.child(Constants.RATING_PRODUK).getValue(float.class);
                            totalRatingProduk = totalRatingProduk + ratingProduk;

                            float ratingPelayanan = data.child(Constants.RATING_PELAYANAN).getValue(float.class);
                            totalRatingPelayanan = totalRatingPelayanan + ratingPelayanan;
                            count = count + 1;
                        }
                        rbKualitasProduk.setRating(totalRatingProduk / count);
                        rbKualitasPelayanan.setRating(totalRatingPelayanan / count);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_pesan_produk)
    public void onBtnPesanProdukClicked() {
        Intent intent = new Intent(this, PesanProdukKhususActivity.class);
        intent.putExtra(Constants.ID_PENJUAL, penjualId);
        startActivity(intent);
    }

    @OnClick(R.id.btn_kirim_obrolan)
    public void onBtnKirimObrolanClicked() {
        Intent intent = new Intent(DetailPenjualActivity.this, ObrolanActivity.class);
        intent.putExtra(Constants.ID_PENJUAL, penjualId);
        intent.putExtra(Constants.AVATAR, avatarPenjual);
        intent.putExtra(Constants.NAMA, namaPenjual);
        startActivity(intent);
    }
}
