package com.mlijo.aryaym.konsumen_mlijo.KelolaPembelian;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

/**
 * Created by AryaYM on 06/03/2018.
 */

public class DetailTransaksiPresenter {

    private DetailTransaksiActivity view;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private DocumentReference produkRef;

    DetailTransaksiPresenter(DetailTransaksiActivity view){
        this.view = view;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        produkRef = mFirestore.collection(view.tipeTransaksi).document(view.produkId);
    }

    public void loadDataAlamat(){
        mDatabase.child(Constants.KONSUMEN).child(BaseActivity.getUid()).child(Constants.DETAIL_KONSUMEN)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("nilai snap alamat", ""+dataSnapshot);
                KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                if (konsumenModel != null) {
                    view.alamatLengkap.setText(konsumenModel.getAlamat());
                    view.telpPenerima.setText(konsumenModel.getNoTelp());
                    view.namaPenerima.setText(konsumenModel.getNama());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadDataProduk(){
        try {
            produkRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                    if (snapshot != null){
                        ProdukModel produkModel = snapshot.toObject(ProdukModel.class);
                        try {
                            view.txtNamaProduk.setText(produkModel.getNamaProduk());
                            view.txtSatuanDigit.setText(produkModel.getSatuanProduk());
                            view.txtSatuan.setText(produkModel.getNamaSatuan());
                            view.txtHargaProduk.setText("Rp." + BaseActivity.rupiah().format(produkModel.getHargaProduk()));
                            ImageLoader.getInstance().loadImageOther(view, produkModel.getGambarProduk().get(0), view.imgProduk);
                        }catch (Exception er){

                        }
                    }
                }
            });
        }catch (Exception e){

        }
    }
}
