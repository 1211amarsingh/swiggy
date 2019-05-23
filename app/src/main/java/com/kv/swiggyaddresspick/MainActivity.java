package com.kv.swiggyaddresspick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kv.swiggyaddress.SelectLocationActivity;
import com.kv.swiggyaddress.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
