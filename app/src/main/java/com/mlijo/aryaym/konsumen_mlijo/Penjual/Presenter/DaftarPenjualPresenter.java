package com.mlijo.aryaym.konsumen_mlijo.Penjual.Presenter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.Penjual.DaftarPenjualAdapter;
import com.mlijo.aryaym.konsumen_mlijo.Penjual.TabPenjualFragment;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AryaYM on 08/02/2018.
 */

public class DaftarPenjualPresenter {

    private TabPenjualFragment view;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    private List<PenjualModel> penjualList = new ArrayList<>();
    private DaftarPenjualAdapter daftarPenjualAdapter;
   // private ArrayList<Integer> penjualArray = new ArrayList<Integer>();

    public DaftarPenjualPresenter(TabPenjualFragment view){
        this.view = view;
        daftarPenjualAdapter = new DaftarPenjualAdapter(view.getActivity(), penjualList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void loadDataPenjual(){
        final Query query = mDatabase.child(Constants.PENJUAL);
        try {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        view.hideItemData();
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                                        view.showItemData(penjualModel);
                                    }
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                try {
                                    if (dataSnapshot != null){
                                        view.penjualList.clear();
                                        view.daftarPenjualAdapter.notifyDataSetChanged();
                                    }
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else {
                       view.tidakAdaPenjual();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
    }
}
