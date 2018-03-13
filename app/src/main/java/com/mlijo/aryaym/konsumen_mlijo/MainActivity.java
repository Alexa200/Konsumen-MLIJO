package com.mlijo.aryaym.konsumen_mlijo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mlijo.aryaym.konsumen_mlijo.Autentifikasi.AutentifikasiTeleponActivity;
import com.mlijo.aryaym.konsumen_mlijo.Bantuan.BantuanFragment;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.DeviceToken;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.Dashboard.DashboardFragment;
import com.mlijo.aryaym.konsumen_mlijo.KelolaPembelian.KelolaPembelianFragment;
import com.mlijo.aryaym.konsumen_mlijo.LokasiPenjual.LokasiPenjualActivity;
import com.mlijo.aryaym.konsumen_mlijo.Obrolan.DaftarObrolanFragment;
import com.mlijo.aryaym.konsumen_mlijo.Pengaturan.PengaturanFragment;
import com.mlijo.aryaym.konsumen_mlijo.Pengaturan.ProfilFragment;
import com.mlijo.aryaym.konsumen_mlijo.Ulasan.DaftarUlasanFragment;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.thefinestartist.finestwebview.FinestWebView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imgAvatar;
    private TextView txtUsername, txtUserEmail;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private View headerView;
    private boolean exit = false;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();
        initData();

        if (findViewById(R.id.main_fragment_container) !=null){
            DashboardFragment dashboardfragment = new DashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, dashboardfragment).commit();
        }
    }

    private void initData(){
        if (InternetConnection.getInstance().isOnline(MainActivity.this)) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                String token = FirebaseInstanceId.getInstance().getToken();
                DeviceToken.getInstance().addDeviceToken(mDatabase, BaseActivity.getUid(),  token);
                dataUserDrawer();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(drawerLayout, getResources().getString(R.string.msg_noInternet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.msg_retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData();
                        }
                    });
            snackbar.show();
        }
    }

    private void dataUserDrawer(){
        mDatabase.child(Constants.KONSUMEN).child(BaseActivity.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
                if (konsumenModel != null) {
                    try {
                        txtUsername.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                        ImageLoader.getInstance().loadImageAvatar(MainActivity.this, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), imgAvatar);
                        txtUserEmail.setText(konsumenModel.getEmail());
                    }catch (Exception e){

                    }
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViews(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        imgAvatar = headerView.findViewById(R.id.img_avatar);
        txtUsername = headerView.findViewById(R.id.txt_header_name);
        txtUserEmail = headerView.findViewById(R.id.txt_header_email);
        progressBar = headerView.findViewById(R.id.progress_bar);
        linearLayout = headerView.findViewById(R.id.linear_contain);
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    ProfilFragment profilFragment = new ProfilFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, profilFragment).commit();
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (count == 0){
           // super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setMessage("Apa anda ingin keluar aplikasi?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            exit = true;
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }else {
            getFragmentManager().popBackStack();
        }
//        new AlertDialog.Builder(this)
//                .setMessage("Apa anda ingin keluar aplikasi?")
//                .setCancelable(false)
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        exit = true;
//                        finish();
//                    }
//                })
//                .setNegativeButton("Tidak", null)
//                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (id) {
            case R.id.dashboard:
                DashboardFragment dashboardFragment = new DashboardFragment();
                transaction.replace(R.id.main_fragment_container, dashboardFragment).commit();
                break;
            case R.id.kelola_pembelian:
                KelolaPembelianFragment kelolaPembelianFragment = new KelolaPembelianFragment();
                transaction.addToBackStack(KelolaPembelianFragment.class.getName());
                transaction.replace(R.id.main_fragment_container, kelolaPembelianFragment).commit();
                break;
            case R.id.info_harga:
                infoHarga();
                break;
            case R.id.lokasi_penjual:
                Intent intentLokasi = new Intent(this, LokasiPenjualActivity.class);
                startActivity(intentLokasi);
                break;
            case R.id.obrolan:
                DaftarObrolanFragment daftarObrolanFragment = new DaftarObrolanFragment();
                transaction.addToBackStack(DaftarObrolanFragment.class.getName());
                transaction.replace(R.id.main_fragment_container, daftarObrolanFragment).commit();
                break;
            case R.id.ulasan:
                DaftarUlasanFragment daftarUlasanFragment = new DaftarUlasanFragment();
                transaction.addToBackStack(DaftarUlasanFragment.class.getName());
                transaction.replace(R.id.main_fragment_container, daftarUlasanFragment).commit();
                break;
            case R.id.pengaturan:
                PengaturanFragment pengaturanFragment = new PengaturanFragment();
                transaction.addToBackStack(PengaturanFragment.class.getName());
                //transaction.replace(R.id.main_fragment_container, pengaturanFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, pengaturanFragment).commit();
                break;
            case R.id.bantuan:
                BantuanFragment bantuanFragment = new BantuanFragment();
                transaction.addToBackStack(BantuanFragment.class.getName());
                transaction.replace(R.id.main_fragment_container, bantuanFragment).commit();
                break;
            case R.id.signout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.msg_confirmLogOut)
                        .setCancelable(false)
                        .setPositiveButton(R.string.lbl_ya, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logOut();
                                // sessionManagerUser.logoutUser();
                                //initInfo();
                            }
                        })
                        .setNegativeButton(R.string.lbl_tidak, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create().show();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void infoHarga(){
        new FinestWebView.Builder(this).titleDefault("Informasi Harga")
                .updateTitleFromHtml(false)
                .statusBarColorRes(R.color.colorPrimaryDark)
                .toolbarColorRes(R.color.colorPrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.finestWhite)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .show("http://siskaperbapo.com/harga/tabel");
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AutentifikasiTeleponActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
