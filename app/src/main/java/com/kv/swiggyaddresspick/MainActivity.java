package com.kv.swiggyaddresspick;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.kv.swiggyaddress.SelectLocationActivity;
import com.kv.swiggyaddress.util.Toast;
import com.kv.swiggyaddress.util.UserData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kv.swiggyaddress.util.Constant.REQUEST_ADD;
import static com.kv.swiggyaddress.util.Constant.REQUEST_EDIT;
import static com.kv.swiggyaddress.util.Constant.REQUEST_SELECT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_select)
    Button btSelect;
    @BindView(R.id.bt_edit)
    Button btEdit;
    @BindView(R.id.bt_addnew)
    Button btAddnew;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_landmark)
    TextView tvLandmark;
    @BindView(R.id.tv_house_no)
    TextView tvHouseNo;
    @BindView(R.id.tv_lat)
    TextView tvLat;
    @BindView(R.id.tv_lng)
    TextView tvLng;

    private RewardedVideoAd rewardedVideoAd;

    @BindView(R.id.ad_view_container)
    FrameLayout adViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adViewContainer.post(this::loadBanner);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        int widthPixels = outMetrics.widthPixels;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {
        MobileAds.initialize(this, this.getResources().getString(R.string.app_id));

        AdView mAdView = new AdView(this);

        mAdView.setAdUnitId(this.getResources().getString(R.string.ad_unit_id_banner1));
        adViewContainer.removeAllViews();
        adViewContainer.addView(mAdView);

        AdSize adSize = getAdSize();
        mAdView.setAdSize(adSize);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    private void loadRewardedVideoAd() {
        if (rewardedVideoAd == null) {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        }
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(this.getResources().getString(R.string.ad_unit_id_reward1), new AdRequest.Builder().build());
        }
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT) {
            if (resultCode == RESULT_OK) {
                //address selected
                setAddressText((UserData.Address) data.getSerializableExtra("address"));
            } else if (resultCode == RESULT_FIRST_USER) {
                //new address submited
                setAddressText((UserData.Address) data.getSerializableExtra("address"));
            } else {
                Toast.showToast(this, "RESULT_CANCELED");
            }
        } else if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_CANCELED) {
                Toast.showToast(this, "RESULT_CANCELED");
            } else {
                //new address submited
                setAddressText((UserData.Address) data.getSerializableExtra("address"));
            }

        } else if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_CANCELED) {
                Toast.showToast(this, "RESULT_CANCELED");
            } else {
                //address edited
                setAddressText((UserData.Address) data.getSerializableExtra("address"));
            }
        }
    }

    @OnClick({R.id.bt_select, R.id.bt_edit, R.id.bt_addnew})
    public void onViewClicked(View view) {
        loadRewardedVideoAd();

        switch (view.getId()) {
            case R.id.bt_select:
                selectaddress();
                break;
            case R.id.bt_edit:
                editAddress();
                break;
            case R.id.bt_addnew:
                addNewAddress();
                break;
        }
    }

    private ArrayList<UserData.Address> getSavedAddress() {
        ArrayList<UserData.Address> arr_saved_address = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            UserData.Address address = new UserData().new Address();
            address.setAddress("add" + i);
            address.setHouse_no("houseno" + i);
            address.setLat(28.9909900 + i);
            address.setLng(77.9909900 + i);
            address.setLandmark("land" + i);
            address.setType("home" + i);
            arr_saved_address.add(address);
        }
        return arr_saved_address;
    }

    public void selectaddress() {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putExtra("action", "select");
        intent.putExtra("saved_address", getSavedAddress());
        startActivityForResult(intent, REQUEST_SELECT);
    }

    public void editAddress() {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("saved_address", getSavedAddress());

        intent.putExtra("type", tvType.getText().toString());
        intent.putExtra("address", tvAddress.getText().toString());
        intent.putExtra("landmark", tvLandmark.getText().toString());
        intent.putExtra("house_no", tvHouseNo.getText().toString());
        intent.putExtra("lat", Double.valueOf(tvLat.getText().toString()));
        intent.putExtra("lng", Double.valueOf(tvLng.getText().toString()));
        startActivityForResult(intent, REQUEST_EDIT);
    }

    public void addNewAddress() {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putExtra("action", "add");
        intent.putExtra("saved_address", getSavedAddress());
        startActivityForResult(intent, REQUEST_ADD);
    }

    private void setAddressText(UserData.Address address) {
        tvType.setText(address.getType());
        tvHouseNo.setText(address.getHouse_no());
        tvAddress.setText(address.getAddress());
        tvLandmark.setText(address.getLandmark());
        tvLat.setText("" + address.getLat());
        tvLng.setText("" + address.getLng());
        btEdit.setVisibility(View.VISIBLE);
    }
}