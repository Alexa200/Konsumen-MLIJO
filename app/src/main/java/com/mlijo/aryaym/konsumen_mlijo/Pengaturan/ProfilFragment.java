package com.mlijo.aryaym.konsumen_mlijo.Pengaturan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AryaYM on 10/09/2017.
 */

public class ProfilFragment extends Fragment implements ValueEventListener {

    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.txt_header_name)
    TextView txtHeaderName;
    @BindView(R.id.btn_pengaturan)
    Button btnPengaturan;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_nomor_telpon)
    TextView txtNomorTelp;
    @BindView(R.id.txt_alamat_lengkap)
    TextView txtAlamat;
    Unbinder unbinder;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_profil);
        final View view = inflater.inflate(R.layout.fragment_profil, container, false);
        unbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.KONSUMEN).child(BaseActivity.getUid()).addValueEventListener(this);

        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
        if (konsumenModel != null) {
            try {
                txtHeaderName.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                txtEmail.setText(konsumenModel.getEmail());
                txtNomorTelp.setText(konsumenModel.getDetailKonsumen().get(Constants.TELPON).toString());
                txtAlamat.setText(konsumenModel.getDetailKonsumen().get(Constants.ALAMAT).toString());
                ImageLoader.getInstance().loadImageAvatar(ProfilFragment.this.getActivity(), konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), imgAvatar);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_pengaturan)
    public void onViewClicked() {
        PengaturanFragment pengaturanFragment = new PengaturanFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, pengaturanFragment).commit();
    }
}
