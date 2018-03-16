package com.mlijo.aryaym.konsumen_mlijo.KelolaPembelian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class KelolaPembelianFragment extends Fragment implements ValueEventListener {

    Unbinder unbinder;
    @BindView(R.id.pesanan_baru)
    LinearLayout pesananBaru;
    @BindView(R.id.status_penjualan)
    LinearLayout statusPenjualan;
    @BindView(R.id.riwayat_transaksi)
    LinearLayout riwayatTransaksi;
    @BindView(R.id.jml_pembelian_baru)
    TextView jmlPembelianBaru;
    @BindView(R.id.jml_status_pembelian)
    TextView jmlStatusPembelian;
    @BindView(R.id.jml_riwayat_transaksi)
    TextView jmlRiwayatTransaksi;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_kelola_pembelian);
        View view = inflater.inflate(R.layout.fragment_kelola_pembelian, container, false);
        unbinder = ButterKnife.bind(this, view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase.child(Constants.KONSUMEN).child(BaseActivity.getUid()).child(Constants.DAFTAR_TRANSAKSI).addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            try {
                long newOrder = dataSnapshot.child(Constants.PEMBELIAN_BARU).getChildrenCount();
                long statusOrder = dataSnapshot.child(Constants.STATUS_PEMBELIAN).getChildrenCount();
                long historyOrder = dataSnapshot.child(Constants.RIWAYAT_TRANSAKSI).getChildrenCount();
                jmlPembelianBaru.setText(Long.toString(newOrder));
                jmlStatusPembelian.setText(Long.toString(statusOrder));
                jmlRiwayatTransaksi.setText(Long.toString(historyOrder));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @OnClick(R.id.pesanan_baru)
    public void onPesananBaruClicked() {
        Intent intent = new Intent(KelolaPembelianFragment.this.getActivity(), DaftarTransaksiActivity.class);
        intent.putExtra(Constants.TITLE, "Pesanan Baru");
        intent.putExtra(Constants.TRANSAKSI, Constants.PEMBELIAN_BARU);
        startActivity(intent);
    }

    @OnClick(R.id.status_penjualan)
    public void onStatusPenjualanClicked() {
        Intent intent = new Intent(KelolaPembelianFragment.this.getActivity(), DaftarTransaksiActivity.class);
        intent.putExtra(Constants.TITLE, "Status Pembelian");
        intent.putExtra(Constants.TRANSAKSI, Constants.STATUS_PEMBELIAN);
        startActivity(intent);
    }

    @OnClick(R.id.riwayat_transaksi)
    public void onRiwayatTransaksiClicked() {
        Intent intent = new Intent(KelolaPembelianFragment.this.getActivity(), DaftarTransaksiActivity.class);
        intent.putExtra(Constants.TITLE, "Riwayat Transaksi");
        intent.putExtra(Constants.TRANSAKSI, Constants.RIWAYAT_TRANSAKSI);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}