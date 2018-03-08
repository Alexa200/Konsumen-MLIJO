package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.ListenerRegistration;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.ProdukModel;
import com.mlijo.aryaym.konsumen_mlijo.Obrolan.ObrolanActivity;
import com.mlijo.aryaym.konsumen_mlijo.Penjual.DetailPenjualActivity;
import com.mlijo.aryaym.konsumen_mlijo.PesanProduk.PesanProdukActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailProdukActivity extends BaseActivity
        implements EventListener<DocumentSnapshot>,
        ValueEventListener {


    @BindView(R.id.imgProduk)
    ImageView imgProduk;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.nama_produk_view)
    TextView namaProdukView;
    @BindView(R.id.harga_produk_view)
    TextView hargaProdukView;
    @BindView(R.id.kategori_produk_view)
    TextView kategoriProdukView;
    @BindView(R.id.satuan_produk_view)
    TextView satuanProdukView;
    @BindView(R.id.detail_produk_view)
    TextView detailProdukView;
    @BindView(R.id.pesanProduk)
    Button pesanProduk;
    @BindView(R.id.imgPenjual)
    ImageView imgPenjual;
    @BindView(R.id.txt_nama_penjual)
    TextView txtNamaPenjual;
    @BindView(R.id.btn_kirim_obrolan)
    Button btnKirimObrolan;
    @BindView(R.id.txt_waktu_update)
    TextView txtWaktuUpdate;

    private FirebaseFirestore mFirestore;
    private DocumentReference mProdukRef;
    private ListenerRegistration mProdukReg;
    private ValueEventListener mPenjualReg;
    private DatabaseReference mDatabase;
    private AdapterImagePager mViewImagePager;
    //DecimalFormat df = new DecimalFormat("#0");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    String produkId, penjualId, kategoriId;
    long hargaProduk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        produkId = getIntent().getExtras().getString(Constants.ID_PRODUK);
        penjualId = getIntent().getExtras().getString(Constants.ID_PENJUAL);
        kategoriId = getIntent().getExtras().getString(Constants.ID_KATEGORI);
        hargaProduk = getIntent().getExtras().getLong(Constants.HARGA_PRODUK);
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProdukRef = mFirestore.collection("produk_reguler").document(produkId);
        Log.d("nilai harga produk", "" + hargaProduk);
        dfs.setDecimalSeparator('.');
    }

    @Override
    public void onStart() {
        super.onStart();
        mProdukReg = mProdukRef.addSnapshotListener(this);
        mPenjualReg = mDatabase.child(Constants.PENJUAL).child(penjualId).addValueEventListener(this);
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("detail", "restaurant:onEvent", e);
            return;
        }
        onProductLoaded(snapshot.toObject(ProdukModel.class));
    }

    private void onProductLoaded(ProdukModel produkModel) {
        Log.d("nilai model", "" + produkModel);
        try {
            setTitle(produkModel.getNamaProduk());
            namaProdukView.setText(produkModel.getNamaProduk());
            kategoriProdukView.setText(produkModel.getKategoriProduk());
            hargaProdukView.setText("Rp." + rupiah().format(produkModel.getHargaProduk()));
            satuanProdukView.setText(produkModel.getSatuanProduk() + " " + produkModel.getNamaSatuan());
            detailProdukView.setText(produkModel.getDeskripsiProduk());
            txtWaktuUpdate.setText("Update terakhir : " + DateFormat.getDateTimeInstance().format(produkModel.getWaktuDibuat()));

            if (produkModel.getGambarProduk().size() > 0) {
                imgProduk.setVisibility(View.GONE);
                mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
                viewPager.setAdapter(mViewImagePager);
            } else {
                viewPager.setVisibility(View.GONE);
                imgProduk.setImageResource(R.drawable.no_image);
            }
            mViewImagePager = new AdapterImagePager(this, produkModel.getGambarProduk());
            viewPager.setAdapter(mViewImagePager);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            final PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
            if (penjualModel != null) {
                try {
                    txtNamaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                    ImageLoader.getInstance().loadImageAvatar(DetailProdukActivity.this, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString(), imgPenjual);
                } catch (Exception e) {

                }
                        btnKirimObrolan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailProdukActivity.this, ObrolanActivity.class);
                                intent.putExtra(Constants.AVATAR, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString());
                                intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                                intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                startActivity(intent);
                            }
                        });
                        imgPenjual.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailProdukActivity.this, DetailPenjualActivity.class);
                                intent.putExtra(Constants.AVATAR, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString());
                                intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                                intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                                startActivity(intent);
                            }
                        });
                if (!penjualModel.isStatusBerjualan()) {
                    pesanProduk.setEnabled(false);
                    pesanProduk.setText("Libur");
                    pesanProduk.setBackgroundColor(Color.RED);

                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @OnClick(R.id.pesanProduk)
    public void onPesanProdukClicked() {
            Intent intent = new Intent(this, PesanProdukActivity.class);
            intent.putExtra(Constants.ID_PRODUK, produkId);
            intent.putExtra(Constants.ID_PENJUAL, penjualId);
            intent.putExtra(Constants.HARGA_PRODUK, hargaProduk);
            startActivity(intent);
    }
}
