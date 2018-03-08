package com.mlijo.aryaym.konsumen_mlijo.Pengaturan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mlijo.aryaym.konsumen_mlijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PengaturanFragment extends Fragment {

    @BindView(R.id.set_profil_layout)
    LinearLayout profilLayout;
    @BindView(R.id.set_alamat_layout)
    LinearLayout alamatLayout;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_pengaturan);
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        unbinder = ButterKnife.bind(this, view);

        profilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PengaturanFragment.this.getActivity(), PengaturanProfilActivity.class);
                startActivity(intent);
            }
        });
        alamatLayout.setVisibility(View.GONE);
//        alamatLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PengaturanFragment.this.getActivity(), KelolaAlamatActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;

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
}
