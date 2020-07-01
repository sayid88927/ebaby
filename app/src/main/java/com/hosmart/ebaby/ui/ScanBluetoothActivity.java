package com.hosmart.ebaby.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.ui.apadter.DeviceAdapter;
import com.hosmart.ebaby.utils.BluetoothUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanBluetoothActivity extends BaseActivity implements DeviceAdapter.onItemClick {


    @BindView(R.id.rl_scan)
    RelativeLayout rlScan;

    @BindView(R.id.rv_device)
    RecyclerView rvDevice;

    @BindView(R.id.ll_again)
    LinearLayout llAgain;

    @BindView(R.id.btu_again)
    Button btuAgain;

    private DeviceAdapter adapter;
    private List<SearchResult> beaconList = new ArrayList<>();
    private SearchRequest request;


    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_bluetooth;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        scanDevice();

    }


    @OnClick({R.id.btu_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btu_again:
                scanDevice();
                break;
        }
    }

    private void scanDevice() {
        if(llAgain.getVisibility() == View.VISIBLE){
            llAgain.setVisibility(View.GONE);
        }
        rlScan.setVisibility(View.VISIBLE);
        if (!BluetoothUtil.getBluetoothUtil().isBluetoothOpened()) {
            BluetoothUtil.getBluetoothUtil().openBluetooth();
        }
        BluetoothUtil.getBluetoothUtil().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
//                Logger.e("onSearchStarted");
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                Logger.e(String.format("beacon for %s\n%s \n %s", device.getAddress(), beacon.toString(),device.getName()));
                if (device.getName().equalsIgnoreCase("QCOM")) {
                    beaconList.add(device);
                }
            }

            @Override
            public void onSearchStopped() {
//                Logger.e("onSearchStopped");
                if (beaconList.size() <= 0) {
                    llAgain.setVisibility(View.VISIBLE);
                    rlScan.setVisibility(View.GONE);
                } else {
                    rlScan.setVisibility(View.GONE);
                    rvDevice.setVisibility(View.VISIBLE);
                    rvDevice.setLayoutManager(new LinearLayoutManager(ScanBluetoothActivity.this));
                    adapter = new DeviceAdapter(beaconList);
                    rvDevice.setAdapter(adapter);
                    adapter.onItemClick(ScanBluetoothActivity.this);

                }
            }

            @Override
            public void onSearchCanceled() {
//                Logger.e("onSearchCanceled");
                scanDevice();
            }
        });
    }

    @Override
    public void onItemClick(SearchResult item) {
        BluetoothUtil.getBluetoothUtil().connect(item.getAddress(), new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                Logger.e(String.valueOf(code));
            }
        });
    }
}
