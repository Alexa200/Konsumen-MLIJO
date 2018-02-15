package com.mlijo.aryaym.konsumen_mlijo.Autentifikasi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.MainActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowAlertDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataUserBaruActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.input_nik)
    EditText inputNik;
    @BindView(R.id.input_nama_lengkap)
    EditText inputNamaLengkap;
    @BindView(R.id.input_alamat)
    EditText inputAlamat;
    @BindView(R.id.input_lokasi_alamat)
    TextView inputLokasiAlamat;
    @BindView(R.id.input_telepon)
    EditText inputTelepon;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    //private Character phoneNumber;
    private String phoneNumber;
    private double latitude, longitude;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user_baru);
        ButterKnife.bind(this);

        phoneNumber = getIntent().getStringExtra(Constants.TELPON);
        inputTelepon.setText(phoneNumber);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        inputLokasiAlamat.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void getDataLokasiAlamat() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            Place place = PlacePicker.getPlace(DataUserBaruActivity.this, data);
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;


            this.inputLokasiAlamat.setText("" + place.getAddress());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == inputLokasiAlamat) {
            inputLokasiAlamat.setEnabled(false);
            getDataLokasiAlamat();
        }else if (v == btnSubmit){
            if (cekFillData()){
                showProgessDialog();
                submitData();
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_LONG).show();
                startActivity(new Intent(DataUserBaruActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private boolean cekFillData(){
        boolean result = true;
        if (TextUtils.isEmpty(inputNik.getText()) || TextUtils.getTrimmedLength(inputNik.getText()) != 16 ||
                TextUtils.isEmpty(inputNamaLengkap.getText()) || TextUtils.isEmpty(inputAlamat.getText()) ||
                TextUtils.isEmpty(inputTelepon.getText()) || TextUtils.getTrimmedLength(inputTelepon.getText()) < 10) {
            result = false;
            ShowAlertDialog.showAlert("Wajib diisi, mohon perhatikan input data Anda dengan benar !", this);
        }
        return result;
    }

    private void submitData(){
        Double dataNIK = Double.valueOf(inputNik.getText().toString());
        String dataNama = inputNamaLengkap.getText().toString();
        String dataAlamat = inputAlamat.getText().toString();
        Double dataTelpon = Double.valueOf(inputTelepon.getText().toString());

        buatDataUser(dataNIK, dataNama, dataAlamat, dataTelpon);
    }

    private void buatDataUser(double NIK, String Nama, String Alamat, double Telpon){
        //DocumentReference documentReference = mDatabase.collection("User").document("Konsumen");
        Map<String, Object> detailKonsumenData = new HashMap<>();
        detailKonsumenData.put(Constants.NIK, NIK);
        detailKonsumenData.put(Constants.NAMA, Nama);
        detailKonsumenData.put(Constants.AVATAR, "");
        detailKonsumenData.put(Constants.ALAMAT, Alamat);
        detailKonsumenData.put(Constants.TELPON, Telpon);
        detailKonsumenData.put(Constants.LONGITUDE, longitude);
        detailKonsumenData.put(Constants.LATITUDE, latitude);
        detailKonsumenData.put(Constants.UID, getUid());

        //documentReference.collection(getUid()).document(Constants.DETAIL_KONSUMEN).set(detailKonsumenData);
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.UID).setValue(getUid());
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DETAIL_KONSUMEN).setValue(detailKonsumenData);
    }
}
