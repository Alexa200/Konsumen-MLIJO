package com.mlijo.aryaym.konsumen_mlijo.Autentifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mlijo.aryaym.konsumen_mlijo.Autentifikasi.Presenter.AutentifikasiTeleponPresenter;
import com.mlijo.aryaym.konsumen_mlijo.MainActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowAlertDialog;
import com.rilixtech.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutentifikasiTeleponActivity extends AppCompatActivity {

    @BindView(R.id.txt_countdown)
    TextView txtCountdown;
    @BindView(R.id.input_kode_verifikasi)
    EditText inputKodeVerifikasi;
    @BindView(R.id.txt_phone_number)
    TextView txtPhoneNumber;
    @BindView(R.id.btn_kirim_ulang_kode)
    Button btnKirimUlangKode;
    @BindView(R.id.input_nomor_telepon)
    EditText inputNomorTelepon;
    @BindView(R.id.layout_input_nomor)
    LinearLayout layoutInputNomor;
    @BindView(R.id.layout_input_kode)
    LinearLayout layoutInputKode;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    private AutentifikasiTeleponPresenter presenter;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikasi_telepon);
        ButterKnife.bind(this);

        presenter = new AutentifikasiTeleponPresenter(this);
        layoutInputKode.setVisibility(View.GONE);
    }

    public boolean cekNomor() {
        boolean statusNomor = true;
        phoneNumber = inputNomorTelepon.getText().toString();
        ccp.registerPhoneNumberTextView(inputNomorTelepon);
        if (TextUtils.isEmpty(inputNomorTelepon.getText()) || TextUtils.getTrimmedLength(inputNomorTelepon.getText()) < 10) {
            statusNomor = false;
            ShowAlertDialog.showAlert("Mohon perhatikan nomor telepon Anda dengan benar !", this);
        }
        return statusNomor;
    }

    public void loginSukses() {
        startActivity(new Intent(AutentifikasiTeleponActivity.this, MainActivity.class));
        finish();
    }

    public void lengkapiDataUserBaru() {
        Intent intent = new Intent(AutentifikasiTeleponActivity.this, DataUserBaruActivity.class);
        intent.putExtra(Constants.TELPON, phoneNumber);
        startActivity(intent);
        finish();
    }

    public void tampilHitungMundur(long millisUntilFinished) {
        txtCountdown.setText("kirim kode dalam :  " + millisUntilFinished / 1000);
        txtPhoneNumber.setText(ccp.getFullNumberWithPlus());
    }

    public void tampilTombolKirimUlang() {
        txtCountdown.setVisibility(View.GONE);
        btnKirimUlangKode.setVisibility(View.VISIBLE);
    }

    public void tampilToast(String pesan) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_selanjutnya)
    public void onBtnSelanjutnyaClicked(View view) {
        if (cekNomor()) {
            layoutInputKode.setVisibility(View.VISIBLE);
            layoutInputNomor.setVisibility(View.GONE);
            presenter.requestCode(phoneNumber);
        }
    }

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {
        String code = inputKodeVerifikasi.getText().toString();
        presenter.signIn(code);
    }

    @OnClick(R.id.btn_kirim_ulang_kode)
    public void onBtnKirimUlangKodeClicked() {
        presenter.requestCode(phoneNumber);
    }

    @OnClick(R.id.btn_edit_nomor)
    public void onViewClicked() {
        layoutInputKode.setVisibility(View.GONE);
        layoutInputNomor.setVisibility(View.VISIBLE);
    }
}