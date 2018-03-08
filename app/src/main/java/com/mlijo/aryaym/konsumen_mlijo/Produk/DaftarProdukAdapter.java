package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.FirestoreAdapter;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 03/09/2017.
 */

public class DaftarProdukAdapter extends FirestoreAdapter<DaftarProdukAdapter.DaftarProdukVH> {

    public interface OnProdukListener{
        void onProdukSelected(DocumentSnapshot produk);
    }

    private OnProdukListener mListener;

    public DaftarProdukAdapter(Query query, OnProdukListener listener){
        super(query);
        mListener = listener;
    }

    @Override
    public DaftarProdukVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DaftarProdukVH(inflater.inflate(R.layout.item_produk, parent, false));
    }

    @Override
    public void onBindViewHolder(final DaftarProdukVH holder, int position) {
        holder.bindData(getSnapshot(position), mListener);
    }

    static class DaftarProdukVH extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_produk)
        ImageView iconProduk;
        @BindView(R.id.txt_nama_produk)
        TextView txtNamaProduk;
        @BindView(R.id.txt_nama_kategori)
        TextView txtNamaKategori;
        @BindView(R.id.txt_harga_produk)
        TextView txtHargaProduk;
        @BindView(R.id.txt_nama_penjual)
        TextView txtNamaPenjual;
        private DatabaseReference mDatabase;

        public DaftarProdukVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final DocumentSnapshot snapshot,
                             final OnProdukListener listener){

            ProdukModel produkModel = snapshot.toObject(ProdukModel.class);

            txtNamaProduk.setText(produkModel.getNamaProduk());
            txtNamaKategori.setText(produkModel.getKategoriProduk());
            txtHargaProduk.setText("Rp." + BaseActivity.rupiah().format(produkModel.getHargaProduk()));
            if (produkModel.getGambarProduk() != null){
            ImageLoader.getInstance().loadImageProduk(iconProduk.getContext(), produkModel.getGambarProduk().get(0), iconProduk);
            }

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(Constants.PENJUAL).child(produkModel.getIdPenjual()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                    txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onProdukSelected(snapshot);
                    }
                }
            });
        }
    }
}
