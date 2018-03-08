package com.mlijo.aryaym.konsumen_mlijo.Penjual;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 14/10/2017.
 */

public class DaftarPenjualAdapter extends RecyclerView.Adapter<DaftarPenjualAdapter.DaftarPenjualVH> {

    private Activity activity;
    private List<PenjualModel> penjualList;

    public DaftarPenjualAdapter(Activity activity, List<PenjualModel> penjualList) {
        this.activity = activity;
        this.penjualList = penjualList;
    }

    @Override
    public DaftarPenjualVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootview = View.inflate(activity, R.layout.item_penjual, null);
        return new DaftarPenjualVH(rootview);
    }

    @Override
    public void onBindViewHolder(DaftarPenjualVH holder, int position) {
        final PenjualModel penjualModel = penjualList.get(position);
        if (penjualModel != null) {
            //Log.d("nilai adapter", "Current data: " + penjualModel);
            try {
                holder.txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                //Log.d("nilai adapter", "Current data: " + penjualModel.getDetailPenjual());
                if (penjualModel.getDetailPenjual().get(Constants.AVATAR) != null) {
                    ImageLoader.getInstance().loadImageAvatar(activity, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), holder.iconPenjual);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, DetailPenjualActivity.class);
                        intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                        intent.putExtra(Constants.AVATAR, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString());
                        intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                        activity.startActivity(intent);
                    }
                });
                holder.txtHariOperasional.setText(penjualModel.getInfoLokasi()
                        .get(Constants.HARI_MULAI).toString() + " - " + penjualModel.getInfoLokasi()
                        .get(Constants.HARI_SELESAI).toString());
                holder.txtJamOperasional.setText(penjualModel.getInfoLokasi()
                        .get(Constants.JAM_MULAI).toString() + " - " + penjualModel.getInfoLokasi()
                        .get(Constants.JAM_SELESAI).toString());
                holder.txtAreaJualan.setText(penjualModel.getUid());
            } catch (Exception e) {

            }
        }
    }

    @Override
    public int getItemCount() {
        return penjualList.size();
    }


    static class DaftarPenjualVH extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_penjual)
        ImageView iconPenjual;
        @BindView(R.id.txt_nama_penjual)
        TextView txtNamaPenjual;
        @BindView(R.id.txt_hari_operasional)
        TextView txtHariOperasional;
        @BindView(R.id.txt_jam_operasional)
        TextView txtJamOperasional;
        @BindView(R.id.txt_area_jualan)
        TextView txtAreaJualan;

        public DaftarPenjualVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
