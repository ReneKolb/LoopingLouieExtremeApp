package de.renekolb.loopinglouieextreme;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.UUID;

@SuppressLint("NewApi")
public class BluetoothLEService {

    //private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID SERVICE_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    private static final UUID CHARACTERISTIC = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");

    public static final long SCAN_PERIODE = 10000;

    private FullscreenActivity fa;
    private Handler h;

    private BluetoothGatt btGatt;
    private BluetoothGattCharacteristic characteristic;

    private BluetoothAdapter mBluetoothAdapter = null;

    public BluetoothLEService(FullscreenActivity fa, Handler h){
        this.h = h;
        this.fa = fa;
        characteristic = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(fa, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            fa.finish();
        }
    }

    public void startDiscoverDevices(){
        //Stop scanning after 10 seconds
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        }, SCAN_PERIODE);
        h.sendMessage(h.obtainMessage(Constants.MESSAGE_START_DISCOVERING_BLE_DEVICES));
        mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
    }

    public void stopScanning(){
        h.sendMessage(h.obtainMessage(Constants.MESSAGE_STOP_DISCOVERING_BLE_DEVICES));
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    public void disconnect(){
        if(btGatt!=null) {
            btGatt.disconnect();
            btGatt = null;
        }
        characteristic = null;
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Message m = h.obtainMessage(Constants.MESSAGE_DISCOVERED_BLE_DEVICE);
            Bundle b = new Bundle();
            b.putString(Constants.DEVICE_NAME, result.getDevice().getName());
            b.putString(Constants.DEVICE_ADDRESS, result.getDevice().getAddress());
            m.setData(b);
            h.sendMessage(m);
        }
    };

    public void connect(String remoteAddress, final ReceiveCallback onReceiveCallback){
        stopScanning();

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(remoteAddress);
        if(device == null){
            Toast.makeText(fa,"Unkown Device",Toast.LENGTH_SHORT).show();
            return;
        }

        btGatt=device.connectGatt(fa, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    //Log.i(TAG, "disconnected");
                    Message m = h.obtainMessage(Constants.MESSAGE_TOAST);
                    Bundle b = new Bundle();
                    b.putString(Constants.TOAST,"disconnected");
                    m.setData(b);
                    h.sendMessage(m);
                    
                    characteristic = null;
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //Log.i(TAG, "Service Discovered");
                    BluetoothGattService service = gatt.getService(SERVICE_UUID);
                    if (service == null) {
                        //Log.e(TAG,"Service not found");
                        return;
                    }
                    characteristic = service.getCharacteristic(CHARACTERISTIC);
                    if (characteristic == null) {
                        //Log.e(TAG,"Characteristic not found");
                    }

                    gatt.setCharacteristicNotification(characteristic, true); // setup onRead Event


                    Message m = h.obtainMessage(Constants.MESSAGE_TOAST);
                    Bundle b = new Bundle();
                    b.putString(Constants.TOAST, "connected to Board");
                    m.setData(b);
                    h.sendMessage(m);

                    characteristic.setValue("BT Connected"); //max 30bytes
                    gatt.writeCharacteristic(characteristic); // Send message to client
//                mBluetoothGatt.writeDescriptor(descriptor);
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt,
                                                BluetoothGattCharacteristic characteristic) {
                //on MEssage Read
                String msg = new String(characteristic.getValue());
                onReceiveCallback.onReceiveMessage(msg);
                //

                //echo Message
                //characteristic.setValue(msg);
                //gatt.writeCharacteristic(characteristic);
            }
        });
    }

    public void sendMessage(String message){
        if(characteristic != null){
            characteristic.setValue(message);
            btGatt.writeCharacteristic(characteristic);
        }
    }
}
