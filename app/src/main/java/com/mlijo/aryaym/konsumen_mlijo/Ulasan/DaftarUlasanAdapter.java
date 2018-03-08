package com.mlijo.aryaym.konsumen_mlijo.Ulasan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.UlasanModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.DateFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AryaYM on 30/10/2017.
 */

public class DaftarUlasanAdapter extends RecyclerView.Adapter<DaftarUlasanAdapter.DaftarUlasanViewHolder> {

    public Activity activity;
    public List<UlasanModel> ulasanList;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;

    public DaftarUlasanAdapter(Activity activity, List<UlasanModel> ulasanList) {
        this.activity = activity;
        this.ulasanList = ulasanList;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public DaftarUlasanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(activity, R.layout.item_ulasan, null);
        return new DaftarUlasanViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final DaftarUlasanViewHolder holder, int position) {
        final UlasanModel ulasanModel = ulasanList.get(position);

        holder.txtWaktuUlasan.setText(DateFormatter.formatDateByYMD(ulasanModel.getWaktuUlasan()));
        mDatabase.child(Constants.PENJUAL).child(ulasanModel.getIdPenjual()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                    holder.txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                    if (penjualModel.getDetailPenjual().get(Constants.AVATAR) != null) {
                        ImageLoader.getInstance().loadImageAvatar(activity, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), holder.iconPenjual);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFirestore.collection(ulasanModel.getTipeTransaksi()).document(ulasanModel.getIdProduk())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (snapshot != null){
                    ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
                    holder.txtNamaProduk.setText(produkModel.getNamaProduk());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailUlasanActivity.class);
                intent.putExtra(Constants.ULASAN, ulasanModel);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ulasanList.size();
    }

    public class DaftarUlasanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_penjual)
        CircleImageView iconPenjual;
        @BindView(R.id.txt_nama_penjual)
        TextView txtNamaPenjual;
        @BindView(R.id.txt_nama_produk)
        TextView txtNamaProduk;
        @BindView(R.id.txt_waktu_ulasan)
        TextView txtWaktuUlasan;


        public DaftarUlasanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
