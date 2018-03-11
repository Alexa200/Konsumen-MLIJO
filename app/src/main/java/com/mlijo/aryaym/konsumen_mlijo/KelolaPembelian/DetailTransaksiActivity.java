package com.mlijo.aryaym.konsumen_mlijo.KelolaPembelian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.TransaksiModel;
import com.mlijo.aryaym.konsumen_mlijo.Obrolan.ObrolanActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Ulasan.BuatUlasanActivity;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AryaYM on 25/09/2017.
 */

public class DetailTransaksiActivity extends BaseActivity {


    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.txtNamaProduk)
    TextView txtNamaProduk;
    @BindView(R.id.jml_item_produk)
    TextView jmlItemProduk;
    @BindView(R.id.total_harga_produk)
    TextView totalHargaProduk;
    @BindView(R.id.biaya_kirim)
    TextView biayaKirim;
    @BindView(R.id.catatan_pembeli)
    TextView catatanPembeli;
    @BindView(R.id.nama_penerima)
    TextView namaPenerima;
    @BindView(R.id.alamat_lengkap)
    TextView alamatLengkap;
    @BindView(R.id.nomortelp_penerima)
    TextView telpPenerima;
    @BindView(R.id.status_transaksi)
    TextView statusTransaksi;
    @BindView(R.id.nama_penerima_pesanan)
    TextView namaPenerimaPesanan;
    @BindView(R.id.btn_batal_pesan)
    Button btnBatalPesan;
    @BindView(R.id.btn_terima_produk)
    Button btnTerimaProduk;
    @BindView(R.id.btn_ulasan)
    Button btnUlasan;
    @BindView(R.id.btn_kirim_obrolan)
    Button btnKirimObrolan;
    @BindView(R.id.penerima_layout)
    LinearLayout penerimaLayout;
    @BindView(R.id.txt_harga_produk)
    TextView txtHargaProduk;
    @BindView(R.id.txt_satuan_digit)
    TextView txtSatuanDigit;
    @BindView(R.id.txt_satuan)
    TextView txtSatuan;
    @BindView(R.id.txt_waktu_antar)
    TextView txtWaktuAntar;
    @BindView(R.id.txt_tanggal_antar)
    TextView txtTanggalAntar;
    private DatabaseReference mDatabase;
    private TransaksiModel transaksiModel;
    public String transaksiId, penjualId, jenisProduk, produkId;
    private DetailTransaksiPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_detail_transaksi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        transaksiModel = (TransaksiModel) getIntent().getSerializableExtra(Constants.TRANSAKSI);
        transaksiId = transaksiModel.getIdTransaksi();
        jenisProduk = transaksiModel.getJenisProduk();
        produkId = transaksiModel.getIdProduk();
        penjualId = transaksiModel.getIdPenjual();

        presenter = new DetailTransaksiPresenter(this);

        presenter.loadDataAlamat();
        presenter.loadDataProduk();

        initInfo();
        loadDataPesanan();
        loadDataPenjual();

    }

    private void initInfo() {
        if (transaksiModel.getStatusTransaksi() == 1) {
            statusTransaksi.setText(Constants.MENUNGGU);
            tombolBatalOrder();
            penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 2) {
            statusTransaksi.setText(Constants.DIPROSES);
            tombolKonfirmasiOrder();
            penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 3) {
            statusTransaksi.setText(Constants.DIKIRIM);
            tombolKonfirmasiOrder();
            penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 4) {
            statusTransaksi.setText(Constants.TERKIRIM);
            tombolKonfirmasiOrder();
            penerimaLayout.setVisibility(View.VISIBLE);
        } else if (transaksiModel.getStatusTransaksi() == 5) {
            statusTransaksi.setText(Constants.DITOLAK);
            penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 6) {
            statusTransaksi.setText(Constants.DIBATALKAN);
            penerimaLayout.setVisibility(View.GONE);
        } else if (transaksiModel.getStatusTransaksi() == 7) {
            statusTransaksi.setText(Constants.DITERIMA);
            penerimaLayout.setVisibility(View.VISIBLE);
            if (!transaksiModel.isUlasan()) {
                btnUlasan.setVisibility(View.VISIBLE);
            }
        }
    }

    private void tombolBatalOrder() {
        btnKirimObrolan.setVisibility(View.VISIBLE);
        btnBatalPesan.setVisibility(View.VISIBLE);
    }

    private void tombolKonfirmasiOrder() {
        btnKirimObrolan.setVisibility(View.VISIBLE);
        btnTerimaProduk.setVisibility(View.VISIBLE);
    }

    private void loadDataPesanan() {
        try {
            totalHargaProduk.setText("Rp." + rupiah().format(transaksiModel.getTotalHarga()));
            jmlItemProduk.setText(String.valueOf(transaksiModel.getJumlahProduk()));
            txtTanggalAntar.setText("Tanggal :" + transaksiModel.getTanggalKirim());
            txtWaktuAntar.setText("Jam :" + transaksiModel.getWaktuKirim());
            catatanPembeli.setText(transaksiModel.getCatatanKonsumen());
            biayaKirim.setText("Rp." + rupiah().format(transaksiModel.getBiayaKirim()));
            namaPenerimaPesanan.setText(transaksiModel.getPenerima());
        } catch (Exception e) {

        }
    }

    private void batalOrder() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.PENJUALAN_BARU).child(transaksiId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(6);
                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.PENJUALAN_BARU).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.PEMBELIAN_BARU).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(6);
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.PEMBELIAN_BARU).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void terimaKiriman() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.STATUS_PENGIRIMAN).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(7);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.NAMA_PENERIMA).setValue(Constants.NAMA_PENERIMA);
                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.PENJUALAN).child(Constants.STATUS_PENGIRIMAN).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.STATUS_PEMBELIAN).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(7);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.NAMA_PENERIMA).setValue(Constants.NAMA_PENERIMA);
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).child(Constants.STATUS_PEMBELIAN).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadDataPenjual(){
        mDatabase.child(Constants.PENJUAL).child(transaksiModel.getIdPenjual()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                if (penjualModel != null){
                    btnKirimObrolan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailTransaksiActivity.this, ObrolanActivity.class);
                            intent.putExtra(Constants.ID_PENJUAL, penjualId);
                            intent.putExtra(Constants.AVATAR, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString());
                            intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                            startActivity(intent);
                        }
                    });
                    btnUlasan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailTransaksiActivity.this, BuatUlasanActivity.class);
                            intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                            intent.putExtra(Constants.TRANSAKSI, transaksiModel);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_batal_pesan)
    public void onBtnBatalPesanClicked() {
        batalOrder();
        finish();
    }

    @OnClick(R.id.btn_terima_produk)
    public void onBtnTerimaProdukClicked() {
        terimaKiriman();
        finish();
    }
}