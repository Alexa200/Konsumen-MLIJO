package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.Produk.DaftarProdukPenjualActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 02/03/2018.
 */

public class DaftarKategoriPenjualAdapter extends RecyclerView.Adapter<DaftarKategoriPenjualAdapter.DaftarKategoriPenjualVH> {


    private Activity activity;
    private List<PenjualModel> kategoriList;

    public DaftarKategoriPenjualAdapter(Activity activity, List<PenjualModel> kategoriList) {
        this.activity = activity;
        this.kategoriList = kategoriList;
    }

    @Override
    public DaftarKategoriPenjualVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(activity, R.layout.item_kategori_penjual, null);
        return new DaftarKategoriPenjualVH(rootView);
    }

    @Override
    public void onBindViewHolder(DaftarKategoriPenjualVH holder, int position) {
        final PenjualModel penjualModel = kategoriList.get(position);
        if (penjualModel != null) {
            holder.txtItemKategori.setText(penjualModel.getNamaKategori().toString());
            if (penjualModel.getNilaiKategori() == 0){
                holder.checkboxKategori.setChecked(false);
            }else {
                holder.checkboxKategori.setChecked(true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, DaftarProdukPenjualActivity.class);
                        intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                        intent.putExtra(Constants.ID_KATEGORI, penjualModel.getNamaKategori().toString());
                        intent.putExtra(Constants.TITLE, penjualModel.getNamaKategori().toString());
                        activity.startActivity(intent);
                    }
                });
            }
            Log.d("nilai adapter", ""+penjualModel.getNilaiKategori());
            Log.d("nilai adapter", ""+penjualModel.getNamaKategori());
        }
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    static class DaftarKategoriPenjualVH extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox_kategori)
        CheckBox checkboxKategori;
        @BindView(R.id.txt_item_kategori)
        TextView txtItemKategori;

        public DaftarKategoriPenjualVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
