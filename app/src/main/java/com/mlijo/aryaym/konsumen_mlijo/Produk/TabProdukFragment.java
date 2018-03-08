package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlijo.aryaym.konsumen_mlijo.DBModel.KategoriModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 03/06/2017.
 */

public class TabProdukFragment extends Fragment {

    @BindView(R.id.kategori_list)
    RecyclerView mRecycler;
    private DaftarKategoriAdapter daftarKategoriAdapter;
    private List<KategoriModel> kategoriList = new ArrayList<>();
    private ViewPager mViewPager;
    private int count = 0;
    private static final Integer[] produkImg = {R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,};
    private ArrayList<Integer> produkArray = new ArrayList<Integer>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_tab_produk,container,false);
        ButterKnife.bind(this, view);
        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(TabProdukFragment.this.getActivity(), PencarianActivity.class);
//                startActivity(intent);
//                Toast.makeText(getActivity(), "Masih dalam pembuatan", Toast.LENGTH_LONG).show();
//            }
//        });

        for (int i=0; i<produkImg.length; i++)
            produkArray.add(produkImg[i]);
        mViewPager = view.findViewById(R.id.imagePager);
        mViewPager.setAdapter(new AdapterImageSlider(this.getActivity(), produkArray));
        TabLayout tabLayout = view.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);
        //start pager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (count == produkImg.length){
                    count = 0;
                }
                mViewPager.setCurrentItem(count++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },2500,2500);

        kategoriData();
        daftarKategoriAdapter = new DaftarKategoriAdapter(getActivity(), kategoriList);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecycler.setAdapter(daftarKategoriAdapter);

        return view;
    }

    private void kategoriData() {
        KategoriModel kategoriSayuran = new KategoriModel(Constants.SAYURAN, R.drawable.sayuran, Constants.SAYURAN);
        KategoriModel kategoriBuah = new KategoriModel(Constants.BUAH, R.drawable.buah, Constants.BUAH);
        KategoriModel kategoriDaging = new KategoriModel(Constants.DAGING, R.drawable.daging, Constants.DAGING);
        KategoriModel kategoriIkan = new KategoriModel(Constants.IKAN, R.drawable.ikan, Constants.IKAN);
        KategoriModel kategoriPalawija = new KategoriModel(Constants.PALAWIJA, R.drawable.palawija, Constants.PALAWIJA);
        KategoriModel kategoriBumbu = new KategoriModel(Constants.BUMBUDAPUR, R.drawable.bumbu, Constants.BUMBUDAPUR);
        KategoriModel kategoriPeralatan = new KategoriModel(Constants.PERALATANDAPUR, R.drawable.lainlain, Constants.PERALATANDAPUR);
        KategoriModel kategoriLain = new KategoriModel(Constants.LAINLAIN, R.drawable.lainlain, Constants.LAINLAIN);

        kategoriList.add(kategoriSayuran);
        kategoriList.add(kategoriBuah);
        kategoriList.add(kategoriDaging);
        kategoriList.add(kategoriIkan);
        kategoriList.add(kategoriPalawija);
        kategoriList.add(kategoriBumbu);
        kategoriList.add(kategoriPeralatan);
        kategoriList.add(kategoriLain);
    }
}
