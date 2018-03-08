package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.firebase.firestore.Query;
import com.mlijo.aryaym.konsumen_mlijo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 11/02/2018.
 */

public class SortingDialogFragment extends DialogFragment {

    public static final String TAG = "SortingDialog";
    interface SortingListener {
        void onSorting(FilterProduk sortingProduk);
    }

    Unbinder unbinder;
    //@BindView(R.id.listview_sorting)
    private SortingListener mSortingListener;
    ListView listviewSorting;
    private View rootView;
    private BaseAdapter adapter;
    String[] sortArray = {
            "John Cena", "Randy Orton", "Triple H", "Roman Reign", "Sheamus"};
    private int mLastCorrectPosition = -1;
    private int mButtonPosition = -1;
    private String query;
    private Query.Direction queryDirection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_dialog_sorting, container, false);
        // sortArray = getResources().getStringArray(R.array.arrKecamatan);
//        listviewSorting = rootView.findViewById(R.id.listview_sorting);
//        listviewSorting.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        adapter = new ArrayAdapter<String>(this.getActivity(),
//                android.R.layout.simple_list_item_checked, sortArray);
//        getData();
        showDialogSorting();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getData() {
        listviewSorting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == mButtonPosition) {
                    if (mLastCorrectPosition != -1) {
                        listviewSorting.setItemChecked(mLastCorrectPosition, true);
                    } else {
                        listviewSorting.setItemChecked(mButtonPosition, false);
                    }
                    // here show dialog
                } else {
                    mLastCorrectPosition = position;
                    // here refresh fragment
                    Log.d("nilai", "" + sortArray[position]);
                }
            }
        });
    }

    private void showDialogSorting() {

        final CheckedTextView produkTerbaru = rootView.findViewById(R.id.chk_produk_terbaru);
        final CheckedTextView namaASC = rootView.findViewById(R.id.chk_nama_asc);
        final CheckedTextView namaDESC = rootView.findViewById(R.id.chk_nama_desc);
        final CheckedTextView hargaASC = rootView.findViewById(R.id.chk_harga_asc);
        final CheckedTextView hargaDESC = rootView.findViewById(R.id.chk_harga_desc);

        produkTerbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produkTerbaru.isChecked()) {
                    produkTerbaru.setChecked(false);
                    produkTerbaru.setCheckMarkDrawable(null);
                } else {
                    query = "waktuDibuat";
                    queryDirection = Query.Direction.DESCENDING;
                    produkTerbaru.setChecked(true);
                    produkTerbaru.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    namaASC.setChecked(false);
                    namaASC.setCheckMarkDrawable(null);
                    namaDESC.setChecked(false);
                    namaDESC.setCheckMarkDrawable(null);
                    hargaASC.setChecked(false);
                    hargaASC.setCheckMarkDrawable(null);
                    hargaDESC.setChecked(false);
                    hargaDESC.setCheckMarkDrawable(null);
                }
            }
        });
        namaASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaASC.isChecked()) {
                    namaASC.setChecked(false);
                    namaASC.setCheckMarkDrawable(null);
                } else {
                    query = "namaProduk";
                    queryDirection = Query.Direction.ASCENDING;
                    namaASC.setChecked(true);
                    namaASC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    produkTerbaru.setChecked(false);
                    produkTerbaru.setCheckMarkDrawable(null);
                    namaDESC.setChecked(false);
                    namaDESC.setCheckMarkDrawable(null);
                    hargaASC.setChecked(false);
                    hargaASC.setCheckMarkDrawable(null);
                    hargaDESC.setChecked(false);
                    hargaDESC.setCheckMarkDrawable(null);
                }
            }
        });
        namaDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaDESC.isChecked()) {
                    namaDESC.setChecked(false);
                    namaDESC.setCheckMarkDrawable(null);
                } else {
                    query = "namaProduk";
                    queryDirection = Query.Direction.DESCENDING;
                    namaDESC.setChecked(true);
                    namaDESC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    produkTerbaru.setChecked(false);
                    produkTerbaru.setCheckMarkDrawable(null);
                    namaASC.setChecked(false);
                    namaASC.setCheckMarkDrawable(null);
                    hargaASC.setChecked(false);
                    hargaASC.setCheckMarkDrawable(null);
                    hargaDESC.setChecked(false);
                    hargaDESC.setCheckMarkDrawable(null);
                }
            }
        });
        hargaASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hargaASC.isChecked()) {
                    hargaASC.setChecked(false);
                    hargaASC.setCheckMarkDrawable(null);
                } else {
                    query = "hargaProduk";
                    queryDirection = Query.Direction.ASCENDING;
                    hargaASC.setChecked(true);
                    hargaASC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    produkTerbaru.setChecked(false);
                    produkTerbaru.setCheckMarkDrawable(null);
                    namaASC.setChecked(false);
                    namaASC.setCheckMarkDrawable(null);
                    namaDESC.setChecked(false);
                    namaDESC.setCheckMarkDrawable(null);
                    hargaDESC.setChecked(false);
                    hargaDESC.setCheckMarkDrawable(null);
                }
            }
        });
        hargaDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hargaDESC.isChecked()) {
                    hargaDESC.setChecked(false);
                    hargaDESC.setCheckMarkDrawable(null);
                } else {
                    query = "hargaProduk";
                    queryDirection = Query.Direction.DESCENDING;
                    hargaDESC.setChecked(true);
                    hargaDESC.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    produkTerbaru.setChecked(false);
                    produkTerbaru.setCheckMarkDrawable(null);
                    namaASC.setChecked(false);
                    namaASC.setCheckMarkDrawable(null);
                    namaDESC.setChecked(false);
                    namaDESC.setCheckMarkDrawable(null);
                    hargaASC.setChecked(false);
                    hargaASC.setCheckMarkDrawable(null);
                }
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SortingListener) {
            mSortingListener = (SortingDialogFragment.SortingListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R.id.btn_sorting_ya, R.id.btn_sorting_tidak})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sorting_ya:
                if (mSortingListener != null){
                    mSortingListener.onSorting(getSortingProduk());
                }
                dismiss();
                break;
            case R.id.btn_sorting_tidak:
                dismiss();
                break;
        }
    }

    public FilterProduk getSortingProduk() {
        FilterProduk sortingProduk = new FilterProduk();

        if (rootView != null) {
            sortingProduk.setKategori(DaftarProdukActivity.kategori());
            sortingProduk.setSortBy(query);
            sortingProduk.setSortDirection(queryDirection);
        }
        return sortingProduk;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
