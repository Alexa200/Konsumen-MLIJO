package com.mlijo.aryaym.konsumen_mlijo.Ulasan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.TransaksiModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuatUlasanActivity extends BaseActivity
        implements EventListener<DocumentSnapshot> {

    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.txtNamaProduk)
    TextView txtNamaProduk;
    @BindView(R.id.txt_nama_penjual)
    TextView txtNamaPenjual;
    @BindView(R.id.rb_kualitas_produk)
    RatingBar rbKualitasProduk;
    @BindView(R.id.txt_nilai_produk)
    TextView txtNilaiProduk;
    @BindView(R.id.rb_kualitas_pelayanan)
    RatingBar rbKualitasPelayanan;
    @BindView(R.id.txt_nilai_pelayanan)
    TextView txtNilaiPelayanan;
    @BindView(R.id.input_ulasan)
    EditText inputUlasan;
    @BindView(R.id.btn_submit_ulasan)
    Button btnEditProfil;

    private TransaksiModel transaksiModel;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private DocumentReference produkRef;
    private String penjualId, jenisProduk, produkId, transaksiId, namaPenjual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_ulasan);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_tambah_ulasan);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transaksiModel = (TransaksiModel) getIntent().getSerializableExtra(Constants.TRANSAKSI);
        namaPenjual = getIntent().getStringExtra(Constants.NAMA);
        penjualId = transaksiModel.getIdPenjual();
        jenisProduk = transaksiModel.getJenisProduk();
        produkId = transaksiModel.getIdProduk();
        transaksiId = transaksiModel.getIdTransaksi();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        produkRef = mFirestore.collection(jenisProduk).document(produkId);

        setRatingBar();
        txtNamaPenjual.setText(namaPenjual);
        produkRef.addSnapshotListener(this);

    }

    private void setRatingBar() {
        rbKualitasProduk.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating) {
                    case 5: {
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_bagus));
                        break;
                    }
                    case 4: {
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_bagus));
                        break;
                    }
                    case 3: {
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_biasa));
                        break;
                    }
                    case 2: {
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_buruk));
                        break;
                    }
                    case 1: {
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                    default: {
                        ratingBar.setRating(1);
                        txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                }
            }
        });

        rbKualitasPelayanan.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating) {
                    case 5: {
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_bagus));
                        break;
                    }
                    case 4: {
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_bagus));
                        break;
                    }
                    case 3: {
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_biasa));
                        break;
                    }
                    case 2: {
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_buruk));
                        break;
                    }
                    case 1: {
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                    default: {
                        ratingBar.setRating(1);
                        txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                }
            }
        });
    }

    private void buatUlasan() {
        String ulasanText = inputUlasan.getText().toString();
        long waktuUlasan = new Date().getTime();
        String pushId = mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.ULASAN).push().getKey();
        String ulasanId = pushId;
        Map<String, Object> dataUlasan = new HashMap<>();
        dataUlasan.put(Constants.WAKTU_ULASAN, waktuUlasan);
        dataUlasan.put(Constants.TEXT_ULASAN, ulasanText);
        dataUlasan.put(Constants.RATING_PRODUK, rbKualitasProduk.getRating());
        dataUlasan.put(Constants.RATING_PELAYANAN, rbKualitasPelayanan.getRating());
        dataUlasan.put(Constants.ID_ULASAN, ulasanId);
        dataUlasan.put(Constants.ID_KONSUMEN, getUid());
        dataUlasan.put(Constants.ID_PENJUAL, penjualId);
        dataUlasan.put(Constants.ID_PRODUK, produkId);
        dataUlasan.put(Constants.JENIS_PRODUK, jenisProduk);

        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.ULASAN).child(ulasanId).setValue(dataUlasan);
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.ULASAN).child(ulasanId).setValue(dataUlasan);
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI)
                .child(transaksiId).child(Constants.ULASAN_STATUS).setValue(true);
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (snapshot != null) {
            ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
            try {
                txtNamaProduk.setText(produkModel.getNamaProduk());
                ImageLoader.getInstance().loadImageOther(BuatUlasanActivity.this, produkModel.getGambarProduk().get(0), imgProduk);
            } catch (Exception error) {

            }
        }
    }

    @OnClick(R.id.btn_submit_ulasan)
    public void onViewClicked() {
        buatUlasan();
        finish();
    }
}