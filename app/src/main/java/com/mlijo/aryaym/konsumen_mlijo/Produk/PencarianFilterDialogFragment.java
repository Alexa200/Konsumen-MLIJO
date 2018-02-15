package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.mlijo.aryaym.konsumen_mlijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 11/02/2018.
 */

public class PencarianFilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialog";

    @BindView(R.id.spinner_category)
    Spinner spinnerCategory;
    @BindView(R.id.spinner_lokasi)
    Spinner spinnerLokasi;



    interface FilterListener {
        void onFilter(FilterProduk filterProduk);
    }

    private View rootView;
    private FilterListener mFilterListener;

    //    @BindView(R.id.txt_harga_awal)
//    TextView txtHargaAwal;
//    @BindView(R.id.txt_harga_akhir)
//    TextView txtHargaAkhir;
//    @BindView(R.id.rangeSeekbar1)
//    com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar rangeSeekbar1;
//    @BindView(R.id.chk_harga1)
//    CheckedTextView chkHarga1;
//    @BindView(R.id.chk_harga2)
//    CheckedTextView chkHarga2;
//    @BindView(R.id.chk_harga3)
//    CheckedTextView chkHarga3;
//    @BindView(R.id.chk_harga4)
//    CheckedTextView chkHarga4;
//    @BindView(R.id.chk_harga5)
//    CheckedTextView chkHarga5;
//    @BindView(R.id.btn_filter_ya)
//    Button btnFilterYa;
//    @BindView(R.id.btn_filter_tidak)
//    Button btnFilterTidak;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_dialog_filter, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener){
            mFilterListener = (FilterListener) context;
        }
    }

//    private void showDialogFilter(){
//        final Dialog dialog = new Dialog(DaftarProdukActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_filter);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//        dialog.getWindow().setAttributes(layoutParams);
//
//        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar1);
//        final TextView txthargaAwal = (TextView) dialog.findViewById(R.id.txt_harga_awal);
//        final TextView txthargaAkhir = (TextView) dialog.findViewById(R.id.txt_harga_akhir);
//        final CheckedTextView harga1 = (CheckedTextView) dialog.findViewById(R.id.chk_harga1);
//        final CheckedTextView harga2 = (CheckedTextView) dialog.findViewById(R.id.chk_harga2);
//        final CheckedTextView harga3 = (CheckedTextView) dialog.findViewById(R.id.chk_harga3);
//        final CheckedTextView harga4 = (CheckedTextView) dialog.findViewById(R.id.chk_harga4);
//        final CheckedTextView harga5 = (CheckedTextView) dialog.findViewById(R.id.chk_harga5);
//        Button btnYa = (Button) dialog.findViewById(R.id.btn_filter_ya);
//        Button btnTidak = (Button) dialog.findViewById(R.id.btn_filter_tidak);
//
//        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                txthargaAwal.setText("Rp." + rupiah().format(minValue));
//                txthargaAkhir.setText("Rp." + rupiah().format(maxValue));
//                hargaAwal = minValue.intValue();
//                hargaAkhir = maxValue.intValue();
//            }
//        });
//        harga1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (harga1.isChecked()){
//                    harga1.setChecked(false);
//                    harga1.setCheckMarkDrawable(null);
//                }else {
//                    hargaAwal = 0;
//                    hargaAkhir = 25000;
//                    harga1.setChecked(true);
//                    harga1.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    harga2.setChecked(false);
//                    harga2.setCheckMarkDrawable(null);
//                    harga3.setChecked(false);
//                    harga3.setCheckMarkDrawable(null);
//                    harga4.setChecked(false);
//                    harga4.setCheckMarkDrawable(null);
//                    harga5.setChecked(false);
//                    harga5.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        harga2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (harga2.isChecked()){
//                    harga2.setChecked(false);
//                    harga2.setCheckMarkDrawable(null);
//                }else {
//                    hargaAwal = 25000;
//                    hargaAkhir = 50000;
//                    harga2.setChecked(true);
//                    harga2.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    harga1.setChecked(false);
//                    harga1.setCheckMarkDrawable(null);
//                    harga3.setChecked(false);
//                    harga3.setCheckMarkDrawable(null);
//                    harga4.setChecked(false);
//                    harga4.setCheckMarkDrawable(null);
//                    harga5.setChecked(false);
//                    harga5.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        harga3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (harga3.isChecked()){
//                    harga3.setChecked(false);
//                    harga3.setCheckMarkDrawable(null);
//                }else {
//                    hargaAwal = 50000;
//                    hargaAkhir = 100000;
//                    harga3.setChecked(true);
//                    harga3.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    harga2.setChecked(false);
//                    harga2.setCheckMarkDrawable(null);
//                    harga1.setChecked(false);
//                    harga1.setCheckMarkDrawable(null);
//                    harga4.setChecked(false);
//                    harga4.setCheckMarkDrawable(null);
//                    harga5.setChecked(false);
//                    harga5.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        harga4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (harga4.isChecked()){
//                    harga4.setChecked(false);
//                    harga4.setCheckMarkDrawable(null);
//                }else {
//                    hargaAwal = 100000;
//                    hargaAkhir = 250000;
//                    harga4.setChecked(true);
//                    harga4.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    harga2.setChecked(false);
//                    harga2.setCheckMarkDrawable(null);
//                    harga3.setChecked(false);
//                    harga3.setCheckMarkDrawable(null);
//                    harga1.setChecked(false);
//                    harga1.setCheckMarkDrawable(null);
//                    harga5.setChecked(false);
//                    harga5.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        harga5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (harga5.isChecked()){
//                    harga5.setChecked(false);
//                    harga5.setCheckMarkDrawable(null);
//                }else {
//                    hargaAwal = 250000;
//                    //hargaAkhir = 25000;
//                    harga5.setChecked(true);
//                    harga5.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    harga2.setChecked(false);
//                    harga2.setCheckMarkDrawable(null);
//                    harga3.setChecked(false);
//                    harga3.setCheckMarkDrawable(null);
//                    harga4.setChecked(false);
//                    harga4.setCheckMarkDrawable(null);
//                    harga1.setChecked(false);
//                    harga1.setCheckMarkDrawable(null);
//                }
//            }
//        });
//
//        btnTidak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btnYa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //queryData.contains(querytext.getText());
//                Toast.makeText(dialog.getContext(), String.valueOf(hargaAwal)+" - "+ String.valueOf(hargaAkhir), Toast.LENGTH_SHORT).show();
//                //updateFilterData();
//                getDataUpdate();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    @OnClick({R.id.btn_filter_ya, R.id.btn_filter_tidak})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_filter_ya:
                if (mFilterListener != null){
                    mFilterListener.onFilter(getFilterProduk());
                }
                dismiss();
                break;
            case R.id.btn_filter_tidak:
                dismiss();
                break;
        }
    }

    @Nullable
    private String getSelectedKategori(){
        String selected = (String) spinnerCategory.getSelectedItem();
        if (getString(R.string.value_any_category).equals(selected)){
            return null;
        }else {
            return selected;
        }
    }

    @Nullable
    private String getSelectedLokasi(){
        String selected = (String) spinnerLokasi.getSelectedItem();
        if (getString(R.string.value_any_location).equals(selected)){
            return null;
        }else {
            return selected;
        }
    }

    public FilterProduk getFilterProduk(){
        FilterProduk filterProduk = new FilterProduk();

        if (rootView != null){
            filterProduk.setKategori(getSelectedKategori());
            filterProduk.setLokasi(getSelectedLokasi());
        }

        return filterProduk;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
