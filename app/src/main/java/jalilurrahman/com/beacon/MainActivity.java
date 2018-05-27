package jalilurrahman.com.beacon;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BeaconManager mBeaconManager;
    private Region mRegion;
    private boolean isReadyForScan;
    private boolean isScanning;
    private BeaconsInfo beaconsInfo;
    private int totalBeaconsInRange = 0;
    int cnt = 0;


    String TAG = "BeaconManager";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.test);
        textView.setText("ok");

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        initBeaconManager();
        createScanner();


        checkPermissions();
        verifyBluetooth();
    }

    private void initBeaconManager() {
        mBeaconManager.setBackgroundMode(true);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        //konkakt?
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconManager.setBackgroundBetweenScanPeriod(3000L);   //Every 15 seconds scan nearest beacons

        mBeaconManager.setBackgroundScanPeriod(1000L);          // default is 10000L
        mBeaconManager.setForegroundBetweenScanPeriod(0L);      // default is 0L
        mBeaconManager.setForegroundScanPeriod(1100L);          // Default is 1100L
        mBeaconManager.setDebug(true);
        //mBeaconManager.setMaxTrackingAge(10000);
        //mBeaconManager.setRegionExitPeriod(12000L);
    }


    private void createScanner() {
        BluetoothManager btManager =
                (BluetoothManager)getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Log.e("MainActivity", "Can't enable Bluetooth");
            Toast.makeText(getApplicationContext(), "Can't enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        mRegion = new Region("default", null, null, null);
        mBeaconManager.bind(this);
        mBeaconManager.addRangeNotifier(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG,"onBeaconServiceConnect");
        isReadyForScan = true;
        isScanning = false;
        startScan();
    }


    private void updateText(boolean flag) {
        textView.setText("ok ok");
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.d(TAG,"didRangeBeaconsInRegion adfadsf");
        cnt++;
        if (beacons != null) {
            noGpsAlertHandler.sendEmptyMessage(1);
            totalBeaconsInRange = beacons.size();
            if (beacons.size() > 0 && region != null && region.equals(mRegion)) {
                noGpsAlertHandler.sendEmptyMessage(2);
                List<BeaconsInfo.BeaconData> beacondata = new ArrayList<BeaconsInfo.BeaconData>();
                Iterator<Beacon> iterator = beacons.iterator();
                while (iterator.hasNext()) {

                    Beacon b = iterator.next();

                    beacondata.add(new BeaconsInfo().new BeaconData(
                            b.getId1().toString(),
                            String.valueOf(b.getDistance()),
                            String.valueOf(b.getRssi()),
                            String.valueOf(b.getTxPower()),
                            "iBeacons",
                            b.getBluetoothName(),
                            b.getId2().toString(),
                            b.getId3().toString(),
                            getProximity(b.getDistance())
                    ));

                    Log.i("iBeacons", "Name : " + b.getBluetoothName());
                    Log.i("iBeacons", b.getBluetoothName() + "UUID : " + String.valueOf(b.getServiceUuid()));
                    Log.i("iBeacons", b.getBluetoothName() + "UUID id : " + b.getId1().toString());
                    Log.i("iBeacons", b.getBluetoothName() + "Rssi : " + String.valueOf(b.getRssi()));
                    Log.i("iBeacons", b.getBluetoothName() + "Tx : " + String.valueOf(b.getTxPower()));
                    Log.i("iBeacons", b.getBluetoothName() + "Major : " + b.getId2().toString());
                    Log.i("iBeacons", b.getBluetoothName() + "Minor : " + b.getId3().toString());
                    Log.i("iBeacons", b.getBluetoothName() + "Distance : " + String.valueOf(b.getDistance()));
                    Log.i("iBeacons", b.getBluetoothName() + "Type : " + String.valueOf(b.getBeaconTypeCode()));
                    Log.i("iBeacons", b.getBluetoothName() + "Range : " + getProximity(b.getDistance()));

                }

                //beaconsInfo =  new BeaconsInfo(resourceType,resourceName, resourceId, beacondata);
                beaconsInfo =  new BeaconsInfo();
                beaconsInfo.setResourcetype("resourceType");
                beaconsInfo.setResourcename("resourceName");
                beaconsInfo.setResourceid("resourceId");
                beaconsInfo.setResultset(beacondata);

				/*sendBeaconsDataAsyncTask = new SendBeaconsDataAsyncTask();
				sendBeaconsDataAsyncTask.execute();*/


            }
        } else {
            noGpsAlertHandler.sendEmptyMessage(0);
        }
    }


    public String getProximity(double paramDouble) {
        if (paramDouble <= 0.5D) {
            return "IMMEDIATE";
        }
        if ((paramDouble > 0.5D) && (paramDouble <= 3.0D)) {
            return "NEAR";
        }
        return "FAR";
    }

    public void startScan() {
        Log.d(TAG,"startScan");
        try {
            if (isReadyForScan & mBeaconManager.isBound(this)) {
                mBeaconManager.startRangingBeaconsInRegion(mRegion);
                /*isScanning = true;
                if (getActivity() instanceof MainNavigationActivity) {
                    ((MainNavigationActivity) getActivity()).swappingFloatingScanIcon(isScanning);
                }*/
            }
        } catch (RemoteException e) {
            Log.d("MainActivity", "Start scan beacon problem", e);
        }
    }


    private Handler noGpsAlertHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0) {
                textView.setText("no found and total fetch "+cnt);
            } else if(msg.what == 1) {
                textView.setText("found 50% and total fetch "+cnt);
            }else if(msg.what == 2) {
                textView.setText("found 100% and total fetch "+cnt);
            }
        }
    };


    @TargetApi(23)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final Dialog permDialog = DialogBuilder.createSimpleOkErrorDialog(
                        this,
                        getString(R.string.dialog_error_need_location_access),
                        getString(R.string.error_message_location_access_need_tobe_granted)
                );
                permDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                permDialog.show();
            }
        }
    }

    @TargetApi(18)
    private void verifyBluetooth() {

        try {
            if (!mBeaconManager.checkAvailability()) {

                final Dialog bleDialog = DialogBuilder.createSimpleOkErrorDialog(
                        this,
                        getString(R.string.dialog_error_ble_not_enabled),
                        getString(R.string.error_message_please_enable_bluetooth)
                );
                bleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                bleDialog.show();

            }
        } catch (RuntimeException e) {

            final Dialog bleDialog = DialogBuilder.createSimpleOkErrorDialog(
                    this,
                    getString(R.string.dialog_error_ble_not_supported),
                    getString(R.string.error_message_bluetooth_le_not_supported)
            );
            bleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }
            });
            bleDialog.show();
        }
    }


}
