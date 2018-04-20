package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

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
    @BindView(R.id.chk_sayuran)
    CheckBox chkSayuran;
    @BindView(R.id.ly_chk_sayuran)
    LinearLayout lyChkSayuran;
    @BindView(R.id.chk_buah)
    CheckBox chkBuah;
    @BindView(R.id.ly_chk_buah)
    LinearLayout lyChkBuah;
    @BindView(R.id.chk_daging)
    CheckBox chkDaging;
    @BindView(R.id.ly_chk_daging)
    LinearLayout lyChkDaging;
    @BindView(R.id.chk_ikan)
    CheckBox chkIkan;
    @BindView(R.id.ly_chk_ikan)
    LinearLayout lyChkIkan;
    @BindView(R.id.chk_palawija)
    CheckBox chkPalawija;
    @BindView(R.id.ly_chk_palawija)
    LinearLayout lyChkPalawija;
    @BindView(R.id.chk_bumbu)
    CheckBox chkBumbu;
    @BindView(R.id.ly_chk_bumbu)
    LinearLayout lyChkBumbu;
    @BindView(R.id.chk_peralatan)
    CheckBox chkPeralatan;
    @BindView(R.id.ly_chk_peralatan)
    LinearLayout lyChkPeralatan;
    @BindView(R.id.chk_lain)
    CheckBox chkLain;
    @BindView(R.id.ly_chk_lain)
    LinearLayout lyChkLain;

    private String penjualId, avatarPenjual, namaPenjual;
    private DatabaseReference mDatabase, penjualRef;

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
        getRatingPenjual();
        getKategori();

    }

    private void getKategori() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            final PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                            try {
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_SAYURAN).toString())) {
                                    chkSayuran.setChecked(true);
                                    lyChkSayuran.setClickable(true);
                                    lyChkSayuran.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.SAYURAN);
                                            intent.putExtra(Constants.TITLE, Constants.SAYURAN);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkSayuran.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_BUAH).toString())) {
                                    chkBuah.setChecked(true);
                                    lyChkBuah.setClickable(true);
                                    lyChkBuah.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.BUAH);
                                            intent.putExtra(Constants.TITLE, Constants.BUAH);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkBuah.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_DAGING).toString())) {
                                    chkDaging.setChecked(true);
                                    lyChkDaging.setClickable(true);
                                    lyChkDaging.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.DAGING);
                                            intent.putExtra(Constants.TITLE, Constants.DAGING);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkDaging.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_IKAN).toString())) {
                                    chkIkan.setChecked(true);
                                    lyChkIkan.setClickable(true);
                                    lyChkIkan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.IKAN);
                                            intent.putExtra(Constants.TITLE, Constants.IKAN);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkIkan.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_PALAWIJA).toString())) {
                                    chkPalawija.setChecked(true);
                                    lyChkPalawija.setClickable(true);
                                    lyChkPalawija.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.PALAWIJA);
                                            intent.putExtra(Constants.TITLE, Constants.PALAWIJA);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkPalawija.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_BUMBU).toString())) {
                                    chkBumbu.setChecked(true);
                                    lyChkBumbu.setClickable(true);
                                    lyChkBumbu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.BUMBUDAPUR);
                                            intent.putExtra(Constants.TITLE, Constants.BUMBUDAPUR);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkBumbu.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_PERALATAN).toString())) {
                                    chkPeralatan.setChecked(true);
                                    lyChkPeralatan.setClickable(true);
                                    lyChkPeralatan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.PERALATANDAPUR);
                                            intent.putExtra(Constants.TITLE, Constants.PERALATANDAPUR);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkPeralatan.setChecked(false);
                                }
                                if (Boolean.parseBoolean(penjualModel.getInfoKategori().get(Constants.KATEGORI_LAIN).toString())) {
                                    chkLain.setChecked(true);
                                    lyChkLain.setClickable(true);
                                    lyChkLain.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPenjualActivity.this, DaftarProdukPenjualActivity.class);
                                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                            intent.putExtra(Constants.ID_KATEGORI, Constants.LAINLAIN);
                                            intent.putExtra(Constants.TITLE, Constants.LAINLAIN);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    chkLain.setChecked(false);
                                }
                            } catch (Exception e) {

                            }
                        }
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
