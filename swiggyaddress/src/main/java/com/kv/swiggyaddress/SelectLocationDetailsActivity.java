package com.kv.swiggyaddress;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.gson.Gson;
import com.kv.swiggyaddress.util.location.GooglePlacePicker;
import com.kv.swiggyaddress.util.KeyboardHandler;
import com.kv.swiggyaddress.util.Log;
import com.kv.swiggyaddress.util.SelectedLinearLayout;
import com.kv.swiggyaddress.util.Toast;
import com.kv.swiggyaddress.util.UserData;
import com.kv.swiggyaddress.util.drawable_edittext_click.CustomEditText;
import com.kv.swiggyaddress.util.drawable_edittext_click.DrawableClickListener;
import com.kv.swiggyaddress.util.progress.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.kv.swiggyaddress.PermissionHandling.checkRequiredPermissions;
import static com.kv.swiggyaddress.util.Constant.RESULT_REPLACE;
import static com.kv.swiggyaddress.util.LocalStorage.getLatLng;
import static com.kv.swiggyaddress.util.LocalStorage.getRecentAddress;
import static com.kv.swiggyaddress.util.LocalStorage.setRecentAddress;
import static com.kv.swiggyaddress.util.Toast.showSnackBar;
import static com.kv.swiggyaddress.util.Utils.isETEmpty;


public class SelectLocationDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback, View.OnClickListener {

    AVLoadingIndicatorView progress;
    ImageView ivMarker;
    LinearLayout llBottomSheet;
    TextView tvSkip;
    LinearLayout llButton;
    LinearLayout llBottom;
    RelativeLayout rlMapview;
    TextInputEditText tietAddress;
    TextInputEditText tietHouseNo;
    CustomEditText tietTitle;
    CoordinatorLayout cl;
    TextInputEditText tietLandmark;
    TextView tbHome;
    TextView tbWork;
    SelectedLinearLayout llSelected;
    TextView tvSubmit;
    RelativeLayout rlIndicator;
    TextView tvAddMore;
    TextView tvDone;
    private BottomSheetBehavior bottomSheetBehavior;
    private GoogleMap mMap;
    AppCompatActivity activity;
    Location mlocation = new Location("");
    String title = "Other";
    double latitude, longitude;
    List<Address> addresses;
    private boolean sheetmove, dialogshowing, home_added, work_added;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_details);

        setBodyUI();
    }

    private void setBodyUI() {
        initViews();
        activity = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getStringExtra("action").equalsIgnoreCase("edit")) {
            tietTitle.setText(getIntent().getStringExtra("type"));
            tietAddress.setText(getIntent().getStringExtra("address"));
            tietLandmark.setText(getIntent().getStringExtra("landmark"));
            tietHouseNo.setText(getIntent().getStringExtra("house_no"));
            latitude = getIntent().getDoubleExtra("lat", 0.0);
            longitude = getIntent().getDoubleExtra("lng", 0.0);
            mlocation.setLatitude(latitude);
            mlocation.setLongitude(longitude);
        } else if (getIntent().getStringExtra("action").equalsIgnoreCase("add")) {
            if (getIntent().getBooleanExtra("replace", false)) {
                latitude = getIntent().getDoubleExtra("lat", 0.0);
                longitude = getIntent().getDoubleExtra("lng", 0.0);
            } else {
                Location location = getLatLng(activity);
                if (location == null) {
                    location = new Location("");
                    location.setLatitude(20.5937);
                    location.setLongitude(78.9629);
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            mlocation.setLatitude(latitude);
            mlocation.setLongitude(longitude);
        } else {
            latitude = getIntent().getDoubleExtra("lat", 0.0);
            longitude = getIntent().getDoubleExtra("lng", 0.0);
            mlocation.setLatitude(latitude);
            mlocation.setLongitude(longitude);
        }
        previousAddress();

        setBottomSheet();
        tietTitle.setDrawableClickListener(target -> {
            switch (target) {
                case RIGHT:
                    llSelected.setVisibility(View.VISIBLE);
                    tietTitle.setVisibility(View.GONE);
                    tietTitle.setText("");
                    title = "Other";
                    break;
            }
        });

        tietHouseNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setupSubmitButton();
            }
        });

        tietTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                title = tietTitle.getText().toString().trim();
                setupSubmitButton();
            }
        });

        tietLandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                setupSubmitButton();
            }
        });

        setupSubmitButton();
    }

    private void initViews() {
        getSupportActionBar().hide();
        progress = findViewById(R.id.material_design_progress);
        ivMarker = findViewById(R.id.iv_marker);
        llBottomSheet = findViewById(R.id.ll_bottom_sheet);
        llBottom = findViewById(R.id.ll_bottom);
        tvSkip = findViewById(R.id.tv_skip);
        llButton = findViewById(R.id.ll_button);
        rlMapview = findViewById(R.id.rl_mapview);
        tietAddress = findViewById(R.id.tiet_address);
        tietHouseNo = findViewById(R.id.tiet_house_no);
        tietTitle = findViewById(R.id.tiet_title);
        cl = findViewById(R.id.cl);
        tietLandmark = findViewById(R.id.tiet_landmark);
        tbHome = findViewById(R.id.tb_home);
        tbWork = findViewById(R.id.tb_work);
        llSelected = findViewById(R.id.ll_selected);
        tvSubmit = findViewById(R.id.tv_submit);
        rlIndicator = findViewById(R.id.rl_indicator);
        tvAddMore = findViewById(R.id.tv_add_more);
        tvDone = findViewById(R.id.tv_done);
        tvSkip.setOnClickListener(this);
        tietAddress.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvAddMore.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tbWork.setOnClickListener(this);
        tbHome.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tb_title).setOnClickListener(this);
    }

    private void previousAddress() {
        ArrayList<UserData.Address> saved_address = (ArrayList<UserData.Address>) getIntent().getSerializableExtra("saved_address");
        if (saved_address != null) {
            for (UserData.Address address : saved_address) {
                if (address.getType().equalsIgnoreCase("HOME")) {
                    home_added = true;
                } else if (address.getType().equalsIgnoreCase("WORK")) {
                    work_added = true;
                }
            }
        } else {
            Log.loge("saved_address missing ");
        }
    }

    private void setupSubmitButton() {
        if (!isETEmpty(tietLandmark) && !isETEmpty(tietHouseNo) && !title.isEmpty()) {
            tvSubmit.setAlpha(1);
            tvSubmit.setEnabled(true);
        } else {
            tvSubmit.setAlpha(0.5f);
            tvSubmit.setEnabled(false);
        }
    }

    private void setupDoneAddMoreButton() {
        if (isETEmpty(tietAddress)) {
            tvAddMore.setAlpha(0.5f);
            tvAddMore.setEnabled(false);
            tvDone.setAlpha(0.5f);
            tvDone.setEnabled(false);
        } else {
            tvAddMore.setAlpha(1);
            tvAddMore.setEnabled(true);
            tvDone.setAlpha(1);
            tvDone.setEnabled(true);
        }
    }

    private void toggleClick(int toggle) {
        if (toggle == 1) {
            if (!home_added) {
                title = "HOME";
                llSelected.setSelected(0);
                setupSubmitButton();
            } else {
                showSnackBar(activity, "You already have a Home address");
            }
        } else if (toggle == 2) {
            if (!work_added) {
                title = "WORK";
                llSelected.setSelected(1);
                setupSubmitButton();
            } else {
                showSnackBar(activity, "You already have a Work address");
            }
        } else {
            llSelected.setVisibility(View.GONE);
            tietTitle.setVisibility(View.VISIBLE);
            tietTitle.requestFocus();
        }
    }

    private void saveAddress() {
        if (title.length() == 0) {
            Toast.showToast(activity, "Enter Title");
            tietTitle.requestFocus();
            KeyboardHandler.showKeyboard(activity);
        } else {
            setResultData(true);
        }
    }

    private void setResultData(boolean submit) {
        UserData.Address userDataAddress = new UserData().new Address();
        userDataAddress.setId(getIntent().getStringExtra("address_id"));
        userDataAddress.setAddress(tietAddress.getText().toString().trim());
        userDataAddress.setHouse_no(tietHouseNo.getText().toString().trim());
        userDataAddress.setLandmark(tietLandmark.getText().toString().trim());
        userDataAddress.setLat(latitude);
        userDataAddress.setLng(longitude);
        userDataAddress.setType(submit ? title : addresses.get(0).getFeatureName());
        if (!submit) {
            UserData addRecent = getRecentAddress(activity);

            if (addRecent == null) {
                addRecent = new UserData();
                ArrayList<UserData.Address> ar = new ArrayList<>();
                ar.add(userDataAddress);
                addRecent.setAddress(ar);

            } else if (!isRecentAddress(userDataAddress.getAddress(), addRecent)) {
                addRecent.getAddress().add(userDataAddress);
            }
            setRecentAddress(activity, new Gson().toJson(addRecent));
        }

        Intent i = new Intent();
        i.putExtra("address", userDataAddress);
        setResult(submit ? RESULT_FIRST_USER : RESULT_OK, i);
        finish();
    }

    private boolean isRecentAddress(String address, UserData addRecent) {
        for (UserData.Address o : addRecent.getAddress()) {
            if (o.getAddress().equalsIgnoreCase(address)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkRequiredPermissions(activity)) {
            setMapUI();
        }

        Log.loge("onMapReady" + mlocation);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(mlocation.getLatitude(), mlocation.getLongitude()), 14);
        mMap.moveCamera(update);
    }

    private void setMapUI() {

        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map));
        mMap.setOnCameraMoveStartedListener(i -> {
            tietAddress.setText("");//Log.loge("CameraMoveStarted " + i);//1- touch/ gesure, 2 - key press, 3- autozoom /camerafocus
            setupDoneAddMoreButton();
            if (i == 1) {
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else if (i == 2) {
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else if (i == 3) {
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });

        mMap.setOnCameraIdleListener(() -> {
            if (!sheetmove) {
                Log.loge("CameraIdleListener " + mMap.getCameraPosition().target);

                mlocation.setLatitude(mMap.getCameraPosition().target.latitude);
                mlocation.setLongitude(mMap.getCameraPosition().target.longitude);
                latlngToAddress(mlocation);
            }
        });

    }

    private void latlngToAddress(Location location) {

        addresses = GooglePlacePicker.getGeocoder(activity, location);

        Log.loge(" " + location);
        if (addresses != null && addresses.size() > 0) {
            tietAddress.setText(addresses.get(0).getAddressLine(0));
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        } else {
            tietAddress.setText("");
            showDialogCustom();
        }
        dialogshowing = false;
        setupDoneAddMoreButton();
    }

    private void showDialogCustom() {
        if (!dialogshowing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Something went wrong")
                    .setCancelable(true)
                    .setPositiveButton("Retry", (dialog, which) -> {
                        dialogshowing = true;

                        dialog.dismiss();
                        latlngToAddress(mlocation);
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        setCameraPositionBySlide(mlocation);
                        sheetmove = false;
                        Log.logw("tBehavior.STATE_EXPANDED: state");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        setCameraPositionBySlide(mlocation);
                        sheetmove = false;
                        Log.logw("Behavior.STATE_COLLAPSED: state");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.logw("tBehavior.STATE_DRAGGING: state");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.logw("tBehavior.STATE_SETTLING: state");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetmove = true;
                        Log.logw("Behavior.STATE_DRAGGING: onSlide");
                        setMapPaddingBotttom(slideOffset);
                        setCameraPositionBySlide(mlocation);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.logw("Behavior.STATE_SETTLING: onSlide");
                        sheetmove = true;
                        setMapPaddingBotttom(slideOffset);
                        setCameraPositionBySlide(mlocation);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.logw("Behavior.STATE_EXPANDED: onSlide");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.logw("Behavior.STATE_COLLAPSED: onSlide");
                        break;
                }
            }

        });
        if (getIntent().getStringExtra("action").equalsIgnoreCase("edit") || getIntent().getStringExtra("action").equalsIgnoreCase("add")) {
            expandView();
        }
    }

    private void setMapPaddingBotttom(Float offset) {
        //From 0.0 (min) - 1.0 (max)
        float bsExpanded = 570;
        float bsCollapsed = 240;
        Float maxMapPaddingBottom = bsExpanded - bsCollapsed;

        mMap.setPadding(0, 0, 0, Math.round(offset * maxMapPaddingBottom));
        rlIndicator.setPadding(0, 0, 0, Math.round(offset * maxMapPaddingBottom));
        ivMarker.setPadding(0, 0, 0, Math.round(offset * maxMapPaddingBottom) + 24);
    }

    private void expandView() {
        tvSkip.setVisibility(View.VISIBLE);
        llButton.setVisibility(View.GONE);
        llBottom.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(3);
    }

    private void setCameraPositionBySlide(Location location) {
        if (location != null) {
            Log.logw("setCameraPositionBySlide" + location);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14);
            mMap.moveCamera(update);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tb_home) {
            toggleClick(1);
        } else if (i == R.id.tb_work) {
            toggleClick(2);
        } else if (i == R.id.tv_skip) {
            finish();
        } else if (i == R.id.tiet_address) {
            Intent intent = getIntent();
            setResult(RESULT_REPLACE, intent);
            finish();
        } else if (i == R.id.iv_back) {
            onBackPressed();
        } else if (i == R.id.tv_done) {
            setResultData(false);
        } else if (i == R.id.tv_add_more) {
            expandView();
        } else if (i == R.id.tv_submit) {
            saveAddress();
        } else if (i == R.id.tb_title) {
            toggleClick(3);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permission = 1;
        for (int status : grantResults) {
            if (status == -1) {
                permission = 0;
            }
        }
        if (permission == 0) {
            Toast.showToast(this, "Permission are necessary");
            finish();
        } else {
            setMapUI();
        }
    }
}