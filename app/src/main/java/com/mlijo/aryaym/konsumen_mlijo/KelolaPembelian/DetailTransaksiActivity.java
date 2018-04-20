package com.mlijo.aryaym.konsumen_mlijo.KelolaPembelian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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
    public String transaksiId, penjualId, jenisProduk, produkId, nama_produk;
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
        String status_transaksi_title = "dibatalkan";
        String status_transaksi = Constants.RIWAYAT_TRANSAKSI;
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(6);
                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(6);
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buatNotifikasiStatusTransaksi(status_transaksi, status_transaksi_title);
    }

    private void terimaKiriman() {
        mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(7);
                                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.NAMA_PENERIMA).setValue(Constants.NAMA_PENERIMA);
                            }
                        });
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_PENGIRIMAN).child(transaksiId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_TRANSAKSI).child(transaksiId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //  ShowAlertDialog.showAlert("sukses", DetailTransaksiActivity.this);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.STATUS_TRANSAKSI).setValue(7);
                                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.RIWAYAT_TRANSAKSI).child(transaksiId)
                                        .child(Constants.NAMA_PENERIMA).setValue(Constants.NAMA_PENERIMA);
                            }
                        });
                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.STATUS_PEMBELIAN).child(transaksiId).removeValue();
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

    private void buatNotifikasiStatusTransaksi(String status, String title){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String penjualId = transaksiModel.getIdPenjual();

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic MmE5NTkyYWYtODM3OS00MTkzLTllZGEtNjA5MDM1MDRlYWE4");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"eed14716-bf93-456a-9833-203325aad307\","
                    +   "\"included_segments\": [\"All\"],"
                    +   "\"filters\": [{\"field\": \"tag\", \"key\": \"uid\", \"relation\": \"=\", \"value\": \"" + penjualId + "\"}],"
                    +   "\"data\": {\"title\": \"Status Pembelian\",\"click_action\": \"2\",\"transaksi\": \""+ status +"\"},"
                    //+   "\"data\": {\"uid\": \"SyykXrusoxTSP8nWg2u4kYFQIdq2\",\"click_action\": \"1\"},"
                    +   "\"contents\": {\"en\": \"Pesanan produk " + nama_produk + " " + title + "oleh pembeli\"}"
                    + "}";


            //System.out.println("strJsonBody:\n" + strJsonBody);
            Log.d("nilai strJsonBody:", "" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            //System.out.println("httpResponse: " + httpResponse);
            Log.d("nilai httpRespone:", "" + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            //System.out.println("jsonResponse:\n" + jsonResponse);
            Log.d("nilai jsonRespone:", "" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @OnClick(R.id.btn_batal_pesan)
    public void onBtnBatalPesanClicked() {
        final AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getApplicationContext());
        cancelBuilder.setMessage(R.string.batalOrder).setCancelable(false)
                .setPositiveButton(R.string.lbl_ya, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        batalOrder();
                    }
                })
                .setNegativeButton(R.string.lbl_tidak, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        cancelBuilder.create().show();
        finish();
    }

    @OnClick(R.id.btn_terima_produk)
    public void onBtnTerimaProdukClicked() {
        terimaKiriman();
        finish();
    }
}