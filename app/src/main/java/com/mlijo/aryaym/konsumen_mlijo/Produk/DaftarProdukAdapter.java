package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.app.Activity;
import android.content.res.Resources;
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
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.FirestoreAdapter;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AryaYM on 03/09/2017.
 */

public class DaftarProdukAdapter extends FirestoreAdapter<DaftarProdukAdapter.DaftarProdukVH> {

    public interface OnProdukListener{
        void onProdukSelected(DocumentSnapshot produk);
    }

    //private OnProdukListener mListener;
    private Activity activity;
    private List<ProdukModel> produkList;
    private DatabaseReference mDatabase;

    private OnProdukListener mListener;

    public DaftarProdukAdapter(Query query, OnProdukListener listener){
        super(query);
        mListener = listener;
    }

//    public DaftarProdukAdapter(Activity activity, List<ProdukModel> produkList) {
//        this.activity = activity;
//        this.produkList = produkList;
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//    }

    @Override
    public DaftarProdukVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View rootView = View.inflate(activity, R.layout.item_produk, null);
//        return new DaftarProdukVH(rootView);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DaftarProdukVH(inflater.inflate(R.layout.item_produk, parent, false));
    }

    @Override
    public void onBindViewHolder(final DaftarProdukVH holder, int position) {
      //  final ProdukModel produkModel = produkList.get(position);
       // if (produkModel != null) {
//            try {
//                holder.txtNamaProduk.setText(produkModel.getNamaProduk());
//                holder.txtNamaKategori.setText(produkModel.getKategoriProduk());
//                holder.txtHargaProduk.setText("Rp." + BaseActivity.rupiah().format(produkModel.getHargaProduk()));
////                if (produkModel.getGambarProduk() != null){
////                    ImageLoader.getInstance().loadImageOther(activity, produkModel.getGambarProduk().get(0), holder.iconProduk);
////
////                }
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (InternetConnection.getInstance().isOnline(activity)) {
////                            final Intent intent = new Intent(activity, DetailProdukActivity.class);
////                            intent.putExtra(Constants.PRODUK_REGULER, produkModel);
////                            activity.startActivity(intent);
//                        }
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//            mDatabase.child(Constants.PENJUAL).child(produkModel.getIdPenjual()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
//                    holder.txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
     //  }

        holder.bindData(getSnapshot(position), mListener);

    }

//    @Override
//    public int getItemCount() {
//        return produkList.size();
//    }


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

            mDatabase = FirebaseDatabase.getInstance().getReference();
            ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
            Resources resources = itemView.getResources();

            txtNamaProduk.setText(produkModel.getNamaProduk());
            txtNamaKategori.setText(produkModel.getKategoriProduk());
            txtHargaProduk.setText("Rp." + BaseActivity.rupiah().format(produkModel.getHargaProduk()));

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

                }
            });
        }
    }
}
