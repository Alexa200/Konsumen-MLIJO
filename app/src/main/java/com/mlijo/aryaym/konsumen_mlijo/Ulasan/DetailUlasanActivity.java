package com.mlijo.aryaym.konsumen_mlijo.Ulasan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailUlasanActivity extends AppCompatActivity {

    @BindView(R.id.imgPenjual)
    ImageView imgPenjual;
    @BindView(R.id.txt_nama_penjual)
    TextView txtNamaPenjual;
    @BindView(R.id.img_produk)
    ImageView imgProduk;
    @BindView(R.id.txt_nama_produk)
    TextView txtNamaProduk;
    @BindView(R.id.txt_waktu_ulasan)
    TextView txtWaktuUlasan;
    @BindView(R.id.rb_kualitas_produk)
    RatingBar rbKualitasProduk;
    @BindView(R.id.rb_kualitas_pelayanan)
    RatingBar rbKualitasPelayanan;
    @BindView(R.id.txt_ulasan)
    TextView txtUlasan;

    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private UlasanModel ulasanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ulasan);
        setTitle("Detail Ulasan");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        ulasanModel = (UlasanModel) getIntent().getSerializableExtra(Constants.ULASAN);
        loadDataUlasan();
    }

    private void loadDataUlasan() {
        txtWaktuUlasan.setText(DateFormatter.formatDateByYMD(ulasanModel.getWaktuUlasan()));
        txtUlasan.setText(ulasanModel.getTextUlasan());
        rbKualitasProduk.setRating(ulasanModel.getRatingProduk());
        rbKualitasPelayanan.setRating(ulasanModel.getRatingPelayanan());

        mDatabase.child(Constants.PENJUAL).child(ulasanModel.getIdPenjual()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                    txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                    if (penjualModel.getDetailPenjual().get(Constants.AVATAR) != null) {
                        ImageLoader.getInstance().loadImageAvatar(DetailUlasanActivity.this, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), imgPenjual);
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
                            txtNamaProduk.setText(produkModel.getNamaProduk());
                            if (produkModel.getGambarProduk() != null){
                                ImageLoader.getInstance().loadImageOther(DetailUlasanActivity.this, produkModel.getGambarProduk().get(0), imgProduk);
                            }
                        }
                    }
                });


//        mDatabase.child(ulasanModel.getTipeTransaksi()).child(ulasanModel.getIdKategori()).child(ulasanModel.getIdProduk()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null) {
//                    ProdukModel produkModel = dataSnapshot.getValue(ProdukModel.class);
//                    txtNamaProduk.setText(produkModel.getNamaProduk());
//                    if (produkModel.getGambarProduk() != null){
//                        ImageLoader.getInstance().loadImageOther(DetailUlasanActivity.this, produkModel.getGambarProduk().get(0), imgProduk);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
