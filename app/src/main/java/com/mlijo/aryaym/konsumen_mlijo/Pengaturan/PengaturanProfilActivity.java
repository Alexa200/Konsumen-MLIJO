package com.mlijo.aryaym.konsumen_mlijo.Pengaturan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mlijo.aryaym.konsumen_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.konsumen_mlijo.Base.ImageLoader;
import com.mlijo.aryaym.konsumen_mlijo.Base.InternetConnection;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.KonsumenModel;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;
import com.mlijo.aryaym.konsumen_mlijo.Utils.EncodeImage;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowAlertDialog;
import com.mlijo.aryaym.konsumen_mlijo.Utils.ShowSnackbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mlijo.aryaym.konsumen_mlijo.Utils.Constants.PERMISSIONS_CAMERA;
import static com.mlijo.aryaym.konsumen_mlijo.Utils.Constants.PERMISSIONS_STORAGE;
import static com.mlijo.aryaym.konsumen_mlijo.Utils.Constants.REQUEST_CODE_CAMERA;
import static com.mlijo.aryaym.konsumen_mlijo.Utils.Constants.REQUEST_CODE_READ_STORAGE;

public class PengaturanProfilActivity extends BaseActivity implements ValueEventListener {

    @BindView(R.id.imgKonsumen)
    ImageView imgKonsumen;
    @BindView(R.id.input_nama_lengkap)
    EditText inputNamaLengkap;
    @BindView(R.id.input_alamat)
    EditText inputAlamat;
    @BindView(R.id.input_lokasi_alamat)
    TextView inputLokasiAlamat;
    @BindView(R.id.input_telepon_konsumen)
    EditText inputTeleponKonsumen;
    @BindView(R.id.btn_edit_profil)
    Button btnEditProfil;

    private Uri mUri;
    private DatabaseReference mDatabase, konsumenRef;
    private StorageReference mStorage;
    private byte[] bitmapDataUser = null;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_profil);
        ButterKnife.bind(this);
        setTitle(R.string.title_atur_profil);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        konsumenRef = mDatabase.child(Constants.KONSUMEN).child(getUid());
        konsumenRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            KonsumenModel konsumenModel = dataSnapshot.getValue(KonsumenModel.class);
            if (konsumenModel != null) {
                try {
                    inputNamaLengkap.setText(konsumenModel.getDetailKonsumen().get(Constants.NAMA).toString());
                    inputAlamat.setText(konsumenModel.getDetailKonsumen().get(Constants.ALAMAT).toString());
                    inputTeleponKonsumen.setText(konsumenModel.getDetailKonsumen().get(Constants.TELPON).toString());
                    ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, konsumenModel.getDetailKonsumen().get(Constants.AVATAR).toString(), imgKonsumen);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void getDataLokasiAlamat() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private boolean cekFillData() {
        boolean sukses = true;
        if (inputNamaLengkap.getText() == null || inputAlamat.getText() == null ||
                inputTeleponKonsumen.getText() == null && (inputTeleponKonsumen.getText().length() > 12 ||
                        inputTeleponKonsumen.getText().length() < 10)) {
            sukses = false;
            ShowAlertDialog.showAlert("Mohon diisi", this);
        }
        return sukses;
    }

    private void buatDataUser() {
        String dataNama = inputNamaLengkap.getText().toString();
        String dataAlamat = inputAlamat.getText().toString();
        String dataTelpon = inputTeleponKonsumen.getText().toString();
        Map<String, Object> detailKonsumenData = new HashMap<>();
        detailKonsumenData.put(Constants.NAMA, dataNama);
        detailKonsumenData.put(Constants.ALAMAT, dataAlamat);
        detailKonsumenData.put(Constants.TELPON, dataTelpon);
        detailKonsumenData.put(Constants.LONGITUDE, longitude);
        detailKonsumenData.put(Constants.LATITUDE, latitude);
        mDatabase.child(Constants.KONSUMEN).child(getUid()).child(Constants.DETAIL_KONSUMEN).updateChildren(detailKonsumenData);
    }

    //Update Foto
    private void showAlertForCamera() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.custom_dialog_up_image, null);
        builder.setView(v);
        //components in custom view
        TextView txtGallery = (TextView) v.findViewById(R.id.txt_open_gallery);
        TextView txtCamera = (TextView) v.findViewById(R.id.txt_open_camera);
        //show dialog
        final AlertDialog alertDialog = builder.show();
        //event click
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyStoragePermissions()) {
                    showGallery();
                }
                alertDialog.dismiss();
            }
        });
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyOpenCamera()) {
                    openCamera();
                }
                alertDialog.dismiss();
            }
        });

    }

    //open gallery to choosing image
    private void showGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.GALLERY_INTENT);
    }

    //open gallery to taking a picture
    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image File Name");
        mUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(cameraIntent, Constants.CAMERA_INTENT);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showGallery();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
            case Constants.MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    //confirm request persmission
    private boolean verifyOpenCamera() {
        int camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA
            );

            return false;
        }
        return true;
    }

    //confirm request persmission
    private boolean verifyStoragePermissions() {
        // Check if we have read or write permission
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE, REQUEST_CODE_READ_STORAGE
            );

            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY_INTENT && resultCode == RESULT_OK) {
            if (InternetConnection.getInstance().isOnline(PengaturanProfilActivity.this)) {
                try {
                    //load image into imageview
                    ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, data.getData().toString(), imgKonsumen);
                    Constants.USER_FILE_PATH = getRealPathFromURI(data.getData());
                    addImageUser(getUid(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            editUserPhotoURL(getUid(), taskSnapshot.getDownloadUrl().toString());
                        }
                    });
                } catch (Exception e) {
                    ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                }
            } else {
                ShowSnackbar.showSnack(PengaturanProfilActivity.this, getResources().getString(R.string.msg_noInternet));
            }
        } else if (requestCode == Constants.CAMERA_INTENT && resultCode == RESULT_OK) {
            if (InternetConnection.getInstance().isOnline(PengaturanProfilActivity.this)) {
                try {
                    if (mUri != null) {
                        //load image into imageview
                        ImageLoader.getInstance().loadImageAvatar(PengaturanProfilActivity.this, mUri.toString(), imgKonsumen);
                        Constants.USER_FILE_PATH = getRealPathFromURI(mUri);
                        addImageUser(getUid(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                editUserPhotoURL(getUid(), taskSnapshot.getDownloadUrl().toString());
                            }
                        });
                    } else {
                        ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                    }
                } catch (Exception e) {
                    ShowSnackbar.showSnack(this, getResources().getString(R.string.msg_error));
                }
            } else {
                ShowSnackbar.showSnack(PengaturanProfilActivity.this, getResources().getString(R.string.msg_noInternet));
            }
        } else if (requestCode == Constants.PLACE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            Place place = PlacePicker.getPlace(PengaturanProfilActivity.this, data);
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
            this.inputLokasiAlamat.setText("" + place.getAddress());
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void editUserPhotoURL(String uid, String photoURL) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put(Constants.AVATAR, photoURL);
        mDatabase.child(Constants.KONSUMEN).child(uid).child(Constants.DETAIL_KONSUMEN).updateChildren(myMap);
    }

    public void addImageUser(String uid, OnSuccessListener<UploadTask.TaskSnapshot> listener) {
        if (Constants.USER_FILE_PATH != null) {
            bitmapDataUser = EncodeImage.encodeImage(Constants.USER_FILE_PATH);
        }
        if (bitmapDataUser != null) {
            StorageReference filePathAvatar = mStorage.child(Constants.USER_AVATAR).child(Constants.KONSUMEN).child(uid).child(Constants.AVATAR);
            UploadTask uploadTask = filePathAvatar.putBytes(bitmapDataUser);
            uploadTask.addOnSuccessListener(listener);

            //restart bitmap
            Constants.USER_FILE_PATH = null;
        }
    }

    @OnClick(R.id.imgKonsumen)
    public void onImgKonsumenClicked() {
        showAlertForCamera();
    }

    @OnClick(R.id.input_lokasi_alamat)
    public void onInputLokasiAlamatClicked() {
        getDataLokasiAlamat();
    }

    @OnClick(R.id.btn_edit_profil)
    public void onBtnEditProfilClicked() {
        if (cekFillData()) {
            buatDataUser();
            finish();
        }
    }
}
