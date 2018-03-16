package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowAlertDialog;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowSnackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PesanProdukKhususActivity extends BaseActivity
        implements View.OnClickListener, ValueEventListener,
        RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener {

    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.input_nama_produk)
    EditText inputNamaProduk;
    @BindView(R.id.notePenjual)
    EditText notePenjual;
    @BindView(R.id.nama_penerima)
    TextView namaPenerima;
    @BindView(R.id.alamat_lengkap)
    TextView alamatLengkap;
    @BindView(R.id.nomortelp_penerima)
    TextView nomortelpPenerima;
    @BindView(R.id.btn_layout)
    LinearLayout btnLayout;
    @BindView(R.id.pesanProduk)
    Button btnPesanProduk;
    @BindView(R.id.spn_kategori_produk)
    Spinner spnKategoriProduk;
    @BindView(R.id.spn_nama_satuan)
    Spinner spnNamaSatuan;
    @BindView(R.id.input_satuan_digit)
    EditText inputSatuanDigit;
    @BindView(R.id.input_tanggal_kirim)
    TextView inputTanggalKirim;
    @BindView(R.id.input_jam_kirim)
    TextView inputJamKirim;
    @BindView(R.id.activity)
    LinearLayout activity;

    private ArrayAdapter<String> spinnerKategoriAdapter, spinnerSatuanAdapter;
    private String kategoriProduk, namaSatuan, penjualId;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private ValueEventListener mKonsumenReg;
    //private PenjualModel penjualModel;
    DecimalFormat df = new DecimalFormat("#0");
    private long timeOpenInMinute = 0;
    private static final String FRAG_TAG_TIME_PICKER_OPEN = "timePickerDialogFragmentOpen";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_produk_khusus);
        setTitle(R.string.title_activity_pesan_produk_khusus);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        //penjualModel = (PenjualModel) getIntent().getSerializableExtra(Constants.PENJUAL_MODEL);
        penjualId = getIntent().getStringExtra(Constants.ID_PENJUAL);
        btnLayout.setVisibility(View.GONE);
        btnPesanProduk.setOnClickListener(this);
        inputTanggalKirim.setOnClickListener(this);
        inputJamKirim.setOnClickListener(this);
//        inputNamaProduk.setText("produk stub");
//        inputSatuanDigit.setText("1");
//        inputTanggalKirim.setText("1/1/2018");
//        inputJamKirim.setText("08:08");

        handleSpinner();
        //loadDataAlamat();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (penjualId != null) {
            mKonsumenReg = mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DETAIL_KONSUMEN).addValueEventListener(this);
            Log.d("nilai UID", "" + getUid());
        }
    }

    private void handleSpinner() {
        //spinnerKategori
        spinnerKategoriAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrKategori));
        spinnerKategoriAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnKategoriProduk.setAdapter(spinnerKategoriAdapter);
        spnKategoriProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String itemKategori = adapterView.getItemAtPosition(position).toString();
                kategoriProduk = itemKategori;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinnerSatuan
        spinnerSatuanAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.arrSatuan));
        spinnerSatuanAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnNamaSatuan.setAdapter(spinnerSatuanAdapter);
        spnNamaSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String itemSatuan = adapterView.getItemAtPosition(position).toString();
                namaSatuan = itemSatuan;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean cekKolomIsian() {
        boolean hasil;
        if (TextUtils.isEmpty(inputNamaProduk.getText()) || TextUtils.isEmpty(inputSatuanDigit.getText()) ||
                TextUtils.isEmpty(inputTanggalKirim.getText()) || TextUtils.isEmpty(inputJamKirim.getText())) {
            hasil = false;
        } else {
            hasil = true;
        }
        return hasil;
    }


    private void buatProdukKhusus() {
        String namaProduk = inputNamaProduk.getText().toString();
        int satuanProduk = Integer.parseInt(inputSatuanDigit.getText().toString());

        if (cekKolomIsian() == true) {
            //String statusCekKolomIsian = String.valueOf(cekKolomIsian());
            if (InternetConnection.getInstance().isOnline(PesanProdukKhususActivity.this)) {
                try {
                    showProgessDialog();
                    String pushId = mDatabase.child(Constants.PRODUK_KHUSUS).child(kategoriProduk).push().getKey();
                    final String produkId = pushId;
                    HashMap<String, Object> dataProduk = new HashMap<>();
                    dataProduk.put(Constants.NAMAPRODUK, namaProduk);
                    dataProduk.put(Constants.DIGITSATUAN, satuanProduk);
                    dataProduk.put(Constants.NAMASATUAN, namaSatuan);
                    dataProduk.put(Constants.ID_PRODUK, produkId);
                    //mDatabase.child(Constants.PRODUK_KHUSUS).child(kategoriProduk).child(produkId).setValue(dataProduk);
                    //mFirestore.collection(Constants.PRODUK_KHUSUS).document(produkId).set(dataProduk);
                    mFirestore.collection(Constants.PRODUK_KHUSUS).document(produkId).set(dataProduk)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pesanProdukKhusus(produkId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ShowSnackbar.showSnack(PesanProdukKhususActivity.this, "gagal membuat pesanan");
                        }
                    });
                   // String status = "cekKolomIsian true, setValue dataProduk berhasil";
                    //pesanProdukKhusus(produkId);
                    //driverPesanProdukKhusus(produkId);
                } catch (Exception e) {
                    ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                }
            } else {
                final Snackbar snackbar = Snackbar.make(activity, getResources().getString(R.string.msg_noInternet), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        } else {
            //String stubStatus = "Mengeksekusi stub yang mempunyai nilai false";
//            String statusCekKolomIsian = String.valueOf(cekKolomIsian());
//            String status = "cekKolomIsian false, setValue dataProduk tidak tereksekusi";
            ShowAlertDialog.showAlert("Anda harus mengisi semua Form yang tersedia !", this);
        }
    }

    private void pesanProdukKhusus(String produkId) {
        String idProduk = produkId;
        String pesanKonsumen = notePenjual.getText().toString();
        String tanggalKirim = inputTanggalKirim.getText().toString();
        String waktuKirim = inputJamKirim.getText().toString();
        long orderTime = new Date().getTime();

        if (InternetConnection.getInstance().isOnline(PesanProdukKhususActivity.this)) {
            try {
                String pushId = mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.PEMBELIAN).push().getKey();
                String transaksiId = pushId;
                Map<String, Object> pemesanan = new HashMap<>();
                pemesanan.put(Constants.BIAYA_KIRIM, 0);
                pemesanan.put(Constants.NOTE_KONSUMEN, pesanKonsumen);
                pemesanan.put(Constants.ID_KONSUMEN, getUid());
                pemesanan.put(Constants.ID_TRANSAKSI, transaksiId);
                pemesanan.put(Constants.ID_PENJUAL, penjualId);
                pemesanan.put(Constants.ID_PRODUK, idProduk);
                pemesanan.put(Constants.JUMLAH_ORDER_PRODUK, 1);
                pemesanan.put(Constants.NAMA_PENERIMA, " ");
                pemesanan.put(Constants.STATUS_TRANSAKSI, 1);
                pemesanan.put(Constants.JENIS_PRODUK, Constants.PRODUK_KHUSUS);
                pemesanan.put(Constants.JUMLAH_HARGA_PRODUK, 0);
                pemesanan.put(Constants.TANGGAL, orderTime);
                pemesanan.put(Constants.TANGGAL_KIRIM, tanggalKirim);
                pemesanan.put(Constants.WAKTU_KIRIM, waktuKirim);

                mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.PEMBELIAN_BARU).child(pushId).setValue(pemesanan);
                mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.PENJUALAN_BARU).child(pushId).setValue(pemesanan);
                //String statusPemesanan = "pemesanan produk berhasil";
                buatNotifikasiOrder();
                finish();
                Toast.makeText(getApplicationContext(), "Anda telah berhasil melakukan pemesanan produk", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
            }
        } else {
            final Snackbar snackbar = Snackbar.make(activity, getResources().getString(R.string.msg_noInternet), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnPesanProduk) {
            buatProdukKhusus();
        } else if (v == inputTanggalKirim) {
            DateTime now = DateTime.now();
            MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 1);
            MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 7);

            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setDateRange(minDate, maxDate)
                    .setPreselectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 1)
                    .setOnDateSetListener(PesanProdukKhususActivity.this);

            cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
        } else if (v == inputJamKirim) {
            RadialTimePickerDialogFragment time = new RadialTimePickerDialogFragment()
                    .setOnTimeSetListener(PesanProdukKhususActivity.this)
                    .setStartTime(12, 00)
                    .setForced24hFormat()
                    .setDoneText("OK")
                    .setCancelText("Batal");
            time.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_OPEN);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialogFragment, int jam, int menit) {
        if (dialogFragment.getTag().equals(FRAG_TAG_TIME_PICKER_OPEN)) {
            inputJamKirim.setText(String.format("%02d : %02d", jam, menit));
            timeOpenInMinute = jam * 60 + menit;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        inputTanggalKirim.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
    }

    private void buatNotifikasiOrder() {
        String key = mDatabase.child(Constants.NOTIFIKASI).child(penjualId).push().getKey();
        Map<String, Object> notifikasi = new HashMap<>();
        notifikasi.put(Constants.TITLE, "Pesanan Baru");
        notifikasi.put(Constants.TRANSAKSI, Constants.PENJUALAN_BARU);
        notifikasi.put("konsumenId", getUid());
        mDatabase.child(Constants.NOTIFIKASI).child("penjual").child(Constants.ORDER).child(penjualId).child(key).setValue(notifikasi);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
            Log.d("nilai model konsumen", "" + konsumenModel);
            if (konsumenModel != null) {
                namaPenerima.setText(konsumenModel.getNama());
                alamatLengkap.setText(konsumenModel.getAlamat());
                nomortelpPenerima.setText(konsumenModel.getNoTelp());
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
