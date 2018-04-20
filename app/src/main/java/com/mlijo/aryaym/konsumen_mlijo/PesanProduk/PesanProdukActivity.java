package com.mlijo.aryaym.konsumen_mlijo.PesanProduk;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.gildaswise.horizontalcounter.HorizontalCounter;
import com.gildaswise.horizontalcounter.RepeatListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowAlertDialog;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowSnackbar;

import org.joda.time.DateTime;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PesanProdukActivity extends BaseActivity
        implements View.OnClickListener, RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener, EventListener<DocumentSnapshot>,
        ValueEventListener {

    @BindView(R.id.txtNamaProduk)
    TextView txtNamaProduk;
    @BindView(R.id.notePenjual)
    EditText notePenjual;
    @BindView(R.id.harga_produk_view)
    TextView hargaProdukView;
    @BindView(R.id.pesanProduk)
    Button btnPesanProduk;
    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.nama_penerima)
    TextView namaPenerima;
    @BindView(R.id.alamat_lengkap)
    TextView alamatLengkap;
    @BindView(R.id.nomortelp_penerima)
    TextView telpPenerima;
    @BindView(R.id.txt_harga_produk)
    TextView txtHargaProduk;
    @BindView(R.id.txt_satuan_digit)
    TextView txtSatuanDigit;
    @BindView(R.id.txt_satuan)
    TextView txtSatuan;
    @BindView(R.id.input_tanggal_kirim)
    TextView inputTanggalKirim;
    @BindView(R.id.input_jam_kirim)
    TextView inputJamKirim;
    @BindView(R.id.activity)
    LinearLayout activity;

    private FirebaseFirestore mFirestore;
    private DatabaseReference mDatabase;
    private DocumentReference mProdukRef;
    private ListenerRegistration mProdukReg;
    private ValueEventListener mKonsumenReg;
   // private ProdukModel produkModel;
    private HorizontalCounter numberPicker;
    String produkId, penjualId, kategoriId, namaPembeli;
    DecimalFormat df = new DecimalFormat("#0");
    double a, b, hargaProduk;
    private long timeOpenInMinute = 0;
    private static final String FRAG_TAG_TIME_PICKER_OPEN = "timePickerDialogFragmentOpen";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_produk);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_pesan_produk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        produkId = getIntent().getExtras().getString(Constants.ID_PRODUK);
        penjualId = getIntent().getExtras().getString(Constants.ID_PENJUAL);
        hargaProduk = getIntent().getExtras().getDouble(Constants.HARGA_PRODUK);
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProdukRef = mFirestore.collection("produk_reguler").document(produkId);
        numberPicker = findViewById(R.id.horizontal_counter);
        numberPicker.setDisplayingInteger(true);

        inputTanggalKirim.setOnClickListener(this);
        inputJamKirim.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (produkId != null) {
            mProdukReg = mProdukRef.addSnapshotListener(this);
            mKonsumenReg = mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DETAIL_KONSUMEN).addValueEventListener(this);
            //Log.d("nilai harga intent", "" + hargaProduk);
        }
    }

    //load data produk
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("detail", "restaurant:onEvent", e);
            return;
        }
        onProductLoaded(snapshot.toObject(ProdukModel.class));
    }

    private void onProductLoaded(final ProdukModel produkModel) {
        //Log.d("nilai model", "" + produkModel);
        try {
            txtNamaProduk.setText(produkModel.getNamaProduk());
            txtHargaProduk.setText("Rp." + rupiah().format(Double.valueOf(hargaProduk)));
            txtSatuanDigit.setText(produkModel.getSatuanProduk() + "");
            txtSatuan.setText(produkModel.getNamaSatuan());
            ImageLoader.getInstance().loadImageOther(this, produkModel.getGambarProduk().get(0), imgProduk);
            hargaProdukView.setText(rupiah().format(totalHarga(produkModel.getHargaProduk())));
            numberPicker.setOnReleaseListener(new RepeatListener.ReleaseCallback() {
                @Override
                public void onRelease() {
                    hargaProdukView.setText(rupiah().format(totalHarga(Double.valueOf(hargaProduk))));
                }
            });
        } catch (Exception e) {

        }
    }

    private double totalHarga(double harga) {
        a = numberPicker.getCurrentValue();
        b = harga;
        double hargaTotal = a * b;
        if (a == 1) {
            return b;
        } else {
            return hargaTotal;
        }
    }

    // load data penjual
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
            //Log.d("nilai model konsumen", "" + konsumenModel);
            if (konsumenModel != null) {
                namaPenerima.setText(konsumenModel.getNama());
                alamatLengkap.setText(konsumenModel.getAlamat());
                telpPenerima.setText(konsumenModel.getNoTelp());
                namaPembeli = konsumenModel.getNama().toString();
            }
        }
    }

    private boolean cekKolomIsian() {
        boolean hasil;
        if (TextUtils.isEmpty(inputTanggalKirim.getText()) || TextUtils.isEmpty(inputJamKirim.getText())) {
            hasil = false;
        } else {
            hasil = true;
        }
        return hasil;
    }

    private void pesanProduk() {
        String pesanKonsumen = notePenjual.getText().toString();
        String tanggalKirim = inputTanggalKirim.getText().toString();
        String waktuKirim = inputJamKirim.getText().toString();
        int jumlahOrderProduk = Integer.parseInt(df.format(numberPicker.getCurrentValue()));
        double totalHargaProduk = totalHarga(Double.valueOf(hargaProduk));
        Log.d("nilai harga", ""+totalHarga(Double.valueOf(hargaProduk)));

        long orderTime = new Date().getTime();

        if (cekKolomIsian() == true) {
            if (InternetConnection.getInstance().isOnline(PesanProdukActivity.this)) {
                Log.d("test pesan", "koneksi sukses");
                try {
                    showProgessDialog();
                    String pushId = mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).push().getKey();
                    String transaksiId = pushId;
                    //Log.d("test pesan", "" + pemesananId);
                    Map<String, Object> pemesanan = new HashMap<>();
                    pemesanan.put(Constants.BIAYA_KIRIM, 0);
                    pemesanan.put(Constants.NOTE_KONSUMEN, pesanKonsumen);
                    pemesanan.put(Constants.ID_KONSUMEN, getUid());
                    pemesanan.put(Constants.ID_TRANSAKSI, transaksiId);
                    pemesanan.put(Constants.ID_PENJUAL, penjualId);
                    pemesanan.put(Constants.ID_PRODUK, produkId);
                    pemesanan.put(Constants.JUMLAH_ORDER_PRODUK, jumlahOrderProduk);
                    pemesanan.put(Constants.NAMA_PENERIMA, " ");
                    pemesanan.put(Constants.STATUS_TRANSAKSI, 1);
                    pemesanan.put(Constants.JENIS_PRODUK, Constants.PRODUK_REGULER);
                    pemesanan.put(Constants.JUMLAH_HARGA_PRODUK, totalHargaProduk);
                    pemesanan.put(Constants.TANGGAL, orderTime);
                    pemesanan.put(Constants.TANGGAL_KIRIM, tanggalKirim);
                    pemesanan.put(Constants.WAKTU_KIRIM, waktuKirim);

                    mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(pushId).setValue(pemesanan);
                    mDatabase.child(Constants.PENJUAL).child(penjualId).child(Constants.DAFTAR_TRANSAKSI).child(Constants.TRANSAKSI_BARU).child(pushId).setValue(pemesanan)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            buatNotifikasiOrder();
                            hideProgressDialog();
                            finish();
                            Toast.makeText(getApplicationContext(), "Anda telah berhasil melakukan pemesanan produk", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            ShowSnackbar.showSnack(PesanProdukActivity.this, "gagal membuat pesanan");
                        }
                    });
                    //Log.d("test pesan", "sukses");


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
            ShowAlertDialog.showAlert("Anda harus mengisi semua Form yang tersedia !", this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == inputTanggalKirim) {
            DateTime now = DateTime.now();
            MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 1);
            MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 7);

            int hourNow = now.getHourOfDay();
            Log.d("jam", ""+ hourNow);
            if (hourNow <= 12) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setDateRange(minDate, maxDate)
                        .setPreselectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 0)
                        .setOnDateSetListener(PesanProdukActivity.this);

                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }else {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setDateRange(minDate, maxDate)
                        .setPreselectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 1)
                        .setOnDateSetListener(PesanProdukActivity.this);

                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        } else if (v == inputJamKirim) {
            RadialTimePickerDialogFragment time = new RadialTimePickerDialogFragment()
                    .setOnTimeSetListener(PesanProdukActivity.this)
                    .setStartTime(4, 00)
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

    // @SuppressLint("StringFormatInvalid")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        inputTanggalKirim.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
    }

    private void buatNotifikasiOrder() {
//        String key = mDatabase.child(Constants.NOTIFIKASI).child(penjualId).push().getKey();
//        Map<String, Object> notifikasi = new HashMap<>();
//        notifikasi.put(Constants.TITLE, "Pesanan Baru");
//        notifikasi.put(Constants.TRANSAKSI, Constants.PENJUALAN_BARU);
//        notifikasi.put("konsumenId", getUid());
//        mDatabase.child(Constants.NOTIFIKASI).child("penjual").child(Constants.ORDER).child(penjualId).child(key).setValue(notifikasi);

//        mDatabase.child(Constants.NOTIFIKASI).child(Constants.ORDER).child(produkModel.getIdPenjual()).child(key).child("konsumenId").setValue(getUid());
//        mDatabase.child(Constants.NOTIFIKASI).child(Constants.ORDER).child(produkModel.getIdPenjual()).child(key).child(Constants.TITLE).setValue("Pesanan Baru");
//        mDatabase.child(Constants.NOTIFIKASI).child(Constants.ORDER).child(produkModel.getIdPenjual()).child(key).child(Constants.TRANSAKSI).setValue(Constants.PENJUALAN_BARU);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                    +   "\"data\": {\"title\": \"Pesanan Baru\",\"click_action\": \"2\",\"transaksi\": \"transaksiBaru\"},"
                    //+   "\"data\": {\"uid\": \"SyykXrusoxTSP8nWg2u4kYFQIdq2\",\"click_action\": \"1\"},"
                    +   "\"contents\": {\"en\": \"Pesanan baru dari " + namaPembeli + "\"}"
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




    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @OnClick(R.id.pesanProduk)
    public void onViewClicked() {
        pesanProduk();
    }
}