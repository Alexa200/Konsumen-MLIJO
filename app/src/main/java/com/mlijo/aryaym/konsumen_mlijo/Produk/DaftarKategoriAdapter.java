package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KategoriModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 02/09/2017.
 */

public class DaftarKategoriAdapter extends RecyclerView.Adapter<DaftarKategoriAdapter.DaftarKategoriVH> {


    private List<KategoriModel> kategoriList;
    private Activity activity;

    public DaftarKategoriAdapter(Activity activity, List<KategoriModel> kategoriList) {
        this.activity = activity;
        this.kategoriList = kategoriList;
    }

    @Override
    public DaftarKategoriVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(activity, R.layout.item_kategori, null);
        return new DaftarKategoriVH(rootView);
    }

    @Override
    public void onBindViewHolder(DaftarKategoriVH holder, int position) {
        final KategoriModel kategori = kategoriList.get(position);
        Glide.with(activity)
                .load(kategori.getGambarKategori())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_image)
                .into(holder.imgItemKategori);
        holder.txtItemKategori.setText(kategori.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String kategoriProduk = "";
                //kategoriProduk = kategori.getKategoriProduk();
                Intent intent = new Intent(activity, DaftarProdukActivity.class);
                intent.putExtra(Constants.ID_KATEGORI, kategori.getIdKategori());
                intent.putExtra(Constants.TITLE, kategori.getTitle());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    static class DaftarKategoriVH extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item_kategori)
        ImageView imgItemKategori;
        @BindView(R.id.txt_item_kategori)
        TextView txtItemKategori;

        public DaftarKategoriVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
