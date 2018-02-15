package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.mlijo.aryaym.konsumen_mlijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 11/02/2018.
 */

public class SortingDialogFragment extends DialogFragment {

    @BindView(R.id.chk_produk_terbaru)
    CheckedTextView chkProdukTerbaru;
    @BindView(R.id.chk_nama_asc)
    CheckedTextView chkNamaAsc;
    @BindView(R.id.chk_nama_desc)
    CheckedTextView chkNamaDesc;
    @BindView(R.id.chk_harga_asc)
    CheckedTextView chkHargaAsc;
    @BindView(R.id.chk_harga_desc)
    CheckedTextView chkHargaDesc;
    @BindView(R.id.btn_sorting_ya)
    Button btnSortingYa;
    @BindView(R.id.btn_sorting_tidak)
    Button btnSortingTidak;
    Unbinder unbinder;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_dialog_sorting, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


//    private void showDialogSorting(){
//        final Dialog dialog = new Dialog(DaftarProdukActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_sorting);
//
//        final CheckedTextView produkTerbaru = (CheckedTextView) dialog.findViewById(R.id.chk_produk_terbaru);
//        final CheckedTextView namaASC = (CheckedTextView) dialog.findViewById(R.id.chk_nama_asc);
//        final CheckedTextView namaDESC = (CheckedTextView) dialog.findViewById(R.id.chk_nama_desc);
//        final CheckedTextView hargaASC = (CheckedTextView) dialog.findViewById(R.id.chk_harga_asc);
//        final CheckedTextView hargaDESC = (CheckedTextView) dialog.findViewById(R.id.chk_harga_desc);
//        Button btnYa = (Button) dialog.findViewById(R.id.btn_sorting_ya);
//        Button btnTidak = (Button) dialog.findViewById(R.id.btn_sorting_tidak);
//
//        produkTerbaru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (produkTerbaru.isChecked()){
//                    produkTerbaru.setChecked(false);
//                    produkTerbaru.setCheckMarkDrawable(null);
//                }else {
//                    queryData = "terbaru";
//                    produkTerbaru.setChecked(true);
//                    produkTerbaru.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    namaASC.setChecked(false);
//                    namaASC.setCheckMarkDrawable(null);
//                    namaDESC.setChecked(false);
//                    namaDESC.setCheckMarkDrawable(null);
//                    hargaASC.setChecked(false);
//                    hargaASC.setCheckMarkDrawable(null);
//                    hargaDESC.setChecked(false);
//                    hargaDESC.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        namaASC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (namaASC.isChecked()){
//                    namaASC.setChecked(false);
//                    namaASC.setCheckMarkDrawable(null);
//                }else {
//                    queryData = "namaProdukASC";
//                    namaASC.setChecked(true);
//                    namaASC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    produkTerbaru.setChecked(false);
//                    produkTerbaru.setCheckMarkDrawable(null);
//                    namaDESC.setChecked(false);
//                    namaDESC.setCheckMarkDrawable(null);
//                    hargaASC.setChecked(false);
//                    hargaASC.setCheckMarkDrawable(null);
//                    hargaDESC.setChecked(false);
//                    hargaDESC.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        namaDESC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (namaDESC.isChecked()){
//                    namaDESC.setChecked(false);
//                    namaDESC.setCheckMarkDrawable(null);
//                }else {
//                    queryData = "namaProdukDESC";
//                    namaDESC.setChecked(true);
//                    namaDESC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    produkTerbaru.setChecked(false);
//                    produkTerbaru.setCheckMarkDrawable(null);
//                    namaASC.setChecked(false);
//                    namaASC.setCheckMarkDrawable(null);
//                    hargaASC.setChecked(false);
//                    hargaASC.setCheckMarkDrawable(null);
//                    hargaDESC.setChecked(false);
//                    hargaDESC.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        hargaASC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (hargaASC.isChecked()){
//                    hargaASC.setChecked(false);
//                    hargaASC.setCheckMarkDrawable(null);
//                }else {
//                    queryData = "hargaProdukASC";
//                    hargaASC.setChecked(true);
//                    hargaASC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    produkTerbaru.setChecked(false);
//                    produkTerbaru.setCheckMarkDrawable(null);
//                    namaASC.setChecked(false);
//                    namaASC.setCheckMarkDrawable(null);
//                    namaDESC.setChecked(false);
//                    namaDESC.setCheckMarkDrawable(null);
//                    hargaDESC.setChecked(false);
//                    hargaDESC.setCheckMarkDrawable(null);
//                }
//            }
//        });
//        hargaDESC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (hargaDESC.isChecked()){
//                    hargaDESC.setChecked(false);
//                    hargaDESC.setCheckMarkDrawable(null);
//                }else {
//                    queryData = "hargaProdukDESC";
//                    hargaDESC.setChecked(true);
//                    hargaDESC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
//                    produkTerbaru.setChecked(false);
//                    produkTerbaru.setCheckMarkDrawable(null);
//                    namaASC.setChecked(false);
//                    namaASC.setCheckMarkDrawable(null);
//                    namaDESC.setChecked(false);
//                    namaDESC.setCheckMarkDrawable(null);
//                    hargaASC.setChecked(false);
//                    hargaASC.setCheckMarkDrawable(null);
//                }
//            }
//        });

//        lyProdukTerbaru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chkProdukTerbaru.setVisibility(View.VISIBLE);
//                querytext.setText("errro");
//            }
//        });
//        lyNamaASC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chkNamaASC.setVisibility(View.VISIBLE);
//            }
//        });
//        lyHargaASC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (chkHargaASC.getVisibility() == View.INVISIBLE){
//                    chkHargaASC.setVisibility(View.VISIBLE);
//                //    queryData.
//                }else if (chkHargaASC.getVisibility() != View.INVISIBLE){
//                    chkHargaASC.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

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
//                Toast.makeText(dialog.getContext(), queryData, Toast.LENGTH_SHORT).show();
//                //updateTampilanData(queryData);
//                dialog.dismiss();
//            }
//        });
//        //queryData.contains(querytext.getText());
//
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//        dialog.getWindow().setAttributes(layoutParams);
//
//        dialog.show();
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
