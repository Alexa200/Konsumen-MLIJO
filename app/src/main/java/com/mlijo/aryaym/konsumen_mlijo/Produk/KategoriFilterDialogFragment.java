package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.mlijo.aryaym.konsumen_mlijo.R;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import faranjit.currency.edittext.CurrencyEditText;

/**
 * Created by AryaYM on 13/02/2018.
 */

public class KategoriFilterDialogFragment extends DialogFragment {

    public static final String TAG = "KategoriFilterDialog";

    @BindView(R.id.spinner_lokasi)
    Spinner spinnerLokasi;
    Unbinder unbinder;
    interface KategoriFilterListener {
        void onFilter(FilterProduk filterProduk);
    }

    private View rootView;
    private KategoriFilterListener mKategoriFilterListener;
    private double hargaMin, hargaMax;
    CurrencyEditText hargaMinimum, hargaMaksimum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_dialog_filter_kategori, container, false);
        // get range seekbar container
        CrystalRangeSeekbar rangeSeekbarHarga = rootView.findViewById(R.id.rangeSeekbar1);
        hargaMinimum = rootView.findViewById(R.id.harga_awal);
        hargaMaksimum = rootView.findViewById(R.id.harga_akhir);

        //hargaMinimum.addTextChangedListener(new CurrencyTextWatcher());

        rangeSeekbarHarga.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                hargaMinimum.setText(String.valueOf(minValue));
                hargaMaksimum.setText(String.valueOf(maxValue));
                try {
                    hargaMin = hargaMinimum.getCurrencyDouble();
                    hargaMax = hargaMaksimum.getCurrencyDouble();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    class CurrencyTextWatcher implements TextWatcher{

        //boolean mEditt //pending dulu

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof KategoriFilterListener) {
            mKategoriFilterListener = (KategoriFilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R.id.btn_filter_ya, R.id.btn_filter_tidak})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_filter_ya:
                if (mKategoriFilterListener != null) {
                    mKategoriFilterListener.onFilter(getFilterProduk());
                }
                dismiss();
                break;
            case R.id.btn_filter_tidak:
                dismiss();
                break;
        }
    }

    @Nullable
    private String getSelectedLokasi() {
        String selected = (String) spinnerLokasi.getSelectedItem();
        if (getString(R.string.value_any_location).equals(selected)) {
            return null;
        } else {
            return selected;
        }
    }

    public FilterProduk getFilterProduk() {
        FilterProduk filterProduk = new FilterProduk();

        if (rootView != null) {
            filterProduk.setKategori(DaftarProdukActivity.kategori());
            filterProduk.setLokasi(getSelectedLokasi());
            filterProduk.setHarga_awal(hargaMin);
            filterProduk.setHarga_akhir(hargaMax);
        }

        return filterProduk;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
