package com.kv.swiggyaddress;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.kv.swiggyaddress.util.Constant;
import com.kv.swiggyaddress.util.Toast;
import com.kv.swiggyaddress.util.UserData;
import com.kv.swiggyaddress.util.location.FusedLocationService;
import com.kv.swiggyaddress.util.location.GpsUtils;
import com.kv.swiggyaddress.util.location.LocationInstance;
import com.kv.swiggyaddress.util.progress.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.kv.swiggyaddress.PermissionHandling.checkRequiredPermissions;
import static com.kv.swiggyaddress.util.LocalStorage.getRecentAddress;
import static com.kv.swiggyaddress.util.Utils.isServiceRunning;

public class SelectLocationActivity extends AppCompatActivity {

    ImageView ivBack;
    TextInputEditText etSearch;
    LinearLayout llCurrentLocation;
    AppCompatActivity activity;
    RecyclerView recyclerView, rvRecentAdd;
    TextView tvSavedAdd, tvRecentAdd, btExpandRecent, btExpandSaved;
    AVLoadingIndicatorView progress;
    View devider;

    private String action;
    private Intent intent;
    private SelectLocationAdapter selectLocationAdapterSaved, selectLocationAdapterRecent;
    private UserData userData;
    private boolean selectCurrentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        init();
    }

    private void init() {
        activity = this;
        viewInit();
        setPlacePicker();

        action = getIntent().getStringExtra("action");

        if (action.equalsIgnoreCase("select")) {
            userData = new UserData();
            userData.setAddress((ArrayList<UserData.Address>) getIntent().getSerializableExtra("saved_address"));
            setAddressAdapter(userData);
            serRecentAddressAdapter(getRecentAddress(activity));

        } else if (action.equalsIgnoreCase("add")) {
            intent = getIntent();
            intent.setClass(activity, SelectLocationDetailsActivity.class);
            startActivityForResult(intent, 1000);
        } else if (action.equalsIgnoreCase("edit")) {
            intent = getIntent();
            intent.setClass(activity, SelectLocationDetailsActivity.class);
            startActivityForResult(intent, 1000);
        }
    }

    private void viewInit() {
        getSupportActionBar().hide();
        progress = findViewById(R.id.material_design_progress);
        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);
        llCurrentLocation = findViewById(R.id.ll_current_location);
        recyclerView = findViewById(R.id.recyclerView);
        tvSavedAdd = findViewById(R.id.tv_saved_add);
        tvRecentAdd = findViewById(R.id.tv_recent_add);
        rvRecentAdd = findViewById(R.id.rv_recent_add);
        btExpandRecent = findViewById(R.id.bt_expand_recent);
        btExpandSaved = findViewById(R.id.bt_expand_saved);
        devider = findViewById(R.id.devider);

        llCurrentLocation.setOnClickListener(v -> {
            setCurrentLocation();
        });
        btExpandSaved.setOnClickListener(v -> {
            selectLocationAdapterSaved.setExpand();
            btExpandSaved.setVisibility(View.GONE);
        });
        btExpandRecent.setOnClickListener(v -> {
            selectLocationAdapterRecent.setExpand();
            btExpandRecent.setVisibility(View.GONE);
        });
    }

    private void setCurrentLocation() {
        startLocationService();
        selectCurrentLocation = true;
        LocationInstance.getInstance().setListener(location -> {
            if (selectCurrentLocation) {
                intent = getIntent();
                intent.putExtra("lat", location.getLatitude());
                intent.putExtra("lng", location.getLongitude());
                intent.setClass(activity, SelectLocationDetailsActivity.class);
                startActivityForResult(intent, 1000);
                selectCurrentLocation = false;
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void setPlacePicker() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        autocompleteFragment.setFilter(filter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                intent = getIntent();
                intent.putExtra("replace", true);
                intent.setClass(activity, SelectLocationDetailsActivity.class);
                intent.putExtra("lat", place.getLatLng().latitude);
                intent.putExtra("lng", place.getLatLng().longitude);
                startActivityForResult(intent, 1000);
            }

            @Override
            public void onError(Status status) {
                Log.e("error", status.getStatus() + "" + status.getStatusCode());
            }
        });
    }

    private void setAddressAdapter(UserData userData) {

        if (userData.getAddress() == null || userData.getAddress().size() == 0) {
            tvSavedAdd.setVisibility(View.GONE);
        } else {
            tvSavedAdd.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            selectLocationAdapterSaved = new SelectLocationAdapter(activity, userData.getAddress(), false);
            recyclerView.setAdapter(selectLocationAdapterSaved);
            if (userData.getAddress().size() > 3) {
                btExpandSaved.setVisibility(View.VISIBLE);
            }
        }
    }

    private void serRecentAddressAdapter(UserData recentAddress) {
        if (recentAddress == null || recentAddress.getAddress().size() == 0) {
            tvRecentAdd.setVisibility(View.GONE);
        } else {
            tvRecentAdd.setVisibility(View.VISIBLE);
            rvRecentAdd.setHasFixedSize(true);
            rvRecentAdd.setLayoutManager(new LinearLayoutManager(this));
            selectLocationAdapterRecent = new SelectLocationAdapter(activity, recentAddress.getAddress(), true);
            rvRecentAdd.setAdapter(selectLocationAdapterRecent);
            if (recentAddress.getAddress().size() > 3) {
                btExpandRecent.setVisibility(View.VISIBLE);
            }

            if (userData.getAddress() != null && userData.getAddress().size() > 0) {
                devider.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_FIRST_USER) {
                setResult(RESULT_FIRST_USER, data);
                finish();
            } else if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        } else if (requestCode == Constant.GPS_REQUEST && resultCode == RESULT_OK) {
            setCurrentLocation();
        } else if (requestCode == Constant.GPS_REQUEST && resultCode == RESULT_CANCELED) {
            Toast.showToast(activity, "Location allow is necessary");
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        } else {
            setCurrentLocation();
        }
    }

    @Override
    public void onBackPressed() {
        if (action.equalsIgnoreCase("edit")) {
            intent = getIntent();
            intent.setClass(activity, SelectLocationDetailsActivity.class);
            startActivityForResult(intent, 1000);
        } else {
            super.onBackPressed();
        }
    }

    public void startLocationService() {
        new GpsUtils(activity).turnGPSOn(isGPSEnable -> {
            if (isGPSEnable && checkRequiredPermissions(activity) && !isServiceRunning(FusedLocationService.class, this)) {
                progress.setVisibility(View.VISIBLE);
                Intent intent = new Intent(this, FusedLocationService.class);
                startService(intent);
            }
        });
    }

    public void stopLocationService() {
        if (isServiceRunning(FusedLocationService.class, this))
            stopService(new Intent(this, FusedLocationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
    }
}
