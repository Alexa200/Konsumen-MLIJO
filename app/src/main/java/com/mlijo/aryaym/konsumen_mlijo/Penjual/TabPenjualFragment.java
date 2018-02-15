package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.Penjual.Presenter.DaftarPenjualPresenter;
import com.mlijo.aryaym.konsumen_mlijo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AryaYM on 03/06/2017.
 */

public class TabPenjualFragment extends Fragment {

    @BindView(R.id.recycler_list_penjual)
    RecyclerView mRecycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_result)
    ImageView imgNoResult;
    Unbinder unbinder;

    private static final String TAG = "TabPenjualFragment";

    private List<PenjualModel> penjualList = new ArrayList<>();
    private DaftarPenjualAdapter daftarPenjualAdapter;
    private ArrayList<Integer> penjualArray = new ArrayList<Integer>();
    private DaftarPenjualPresenter presenter;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_penjual, container, false);

        unbinder = ButterKnife.bind(this, view);
        presenter = new DaftarPenjualPresenter(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //loadDataPenjual();
        loadData();
        daftarPenjualAdapter = new DaftarPenjualAdapter(this.getActivity(), penjualList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecycler.setAdapter(daftarPenjualAdapter);
        return view;
    }

    private void loadData() {
        presenter.loadDataPenjual();
        //presenter.loadPenjual();
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//
//        if (daftarPenjualAdapter != null){
//            daftarPenjualAdapter.loadDataPenjual();
//        }
//    }

    public void showItemData(PenjualModel penjualModel) {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        imgNoResult.setVisibility(View.GONE);
        penjualList.add(penjualModel);
        daftarPenjualAdapter.notifyDataSetChanged();
    }

    public void hideItemData() {
        progressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        imgNoResult.setVisibility(View.GONE);
    }
    public void tidakAdaPenjual() {
        progressBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
        imgNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
