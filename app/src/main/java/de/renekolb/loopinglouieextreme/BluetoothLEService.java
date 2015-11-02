package de.renekolb.loopinglouieextreme;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.UUID;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;

@SuppressLint("NewApi")
public class BluetoothLEService {

    //private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID SERVICE_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    private static final UUID CHARACTERISTIC = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");

    public static final long SCAN_PERIODE = 10000;

    private FullscreenActivity fa;
    private Handler h;

    private  boolean connected;

    private BluetoothGatt btGatt;
    private BluetoothGattCharacteristic characteristic;

    private BluetoothAdapter mBluetoothAdapter = null;

    LinkedList<byte[]> sendingQueue;

    public BluetoothLEService(FullscreenActivity fa, Handler h){
        this.connected = false;
        this.h = h;
        this.fa = fa;
        characteristic = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sendingQueue = new LinkedList<>();
        if (mBluetoothAdapter == null) {
            Toast.makeText(fa, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            fa.finish();
        }
    }

    @SuppressWarnings("Deprecation")
    public void startDiscoverDevices(){
        //Stop scanning after 10 seconds
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        }, SCAN_PERIODE);
        h.sendMessage(h.obtainMessage(Constants.MESSAGE_START_DISCOVERING_BLE_DEVICES));
        //only available in API 21 or higher!!
        //mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);

        //used in older APIs
        mBluetoothAdapter.startLeScan(scanCallback);
    }

    @SuppressWarnings("Deprecation")
    public void stopScanning(){
        h.sendMessage(h.obtainMessage(Constants.MESSAGE_STOP_DISCOVERING_BLE_DEVICES));
        //mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        mBluetoothAdapter.stopLeScan(scanCallback);
    }

    public void disconnect(){
        Log.i("", "disconnect BLE!");
        if(btGatt!=null) {
            btGatt.disconnect();
            btGatt = null;
        }
        characteristic = null;
        connected = false;
    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
    //private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device,
                        final int rssi, final byte[] scanRecord){
            Message m = h.obtainMessage(Constants.MESSAGE_DISCOVERED_BLE_DEVICE);
            Bundle b = new Bundle();
            b.putString(Constants.DEVICE_NAME, device.getName());
            b.putString(Constants.DEVICE_ADDRESS, device.getAddress());
            m.setData(b);
            h.sendMessage(m);
        }
        /*@Override
        public void onScanResult(int callbackType, ScanResult result) {
            Message m = h.obtainMessage(Constants.MESSAGE_DISCOVERED_BLE_DEVICE);
            Bundle b = new Bundle();
            b.putString(Constants.DEVICE_NAME, result.getDevice().getName());
            b.putString(Constants.DEVICE_ADDRESS, result.getDevice().getAddress());
            m.setData(b);
            h.sendMessage(m);
        }*/
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
                    Log.i("", "BLE disconnected");
                    Message m = h.obtainMessage(Constants.MESSAGE_BLE_CONNECTION_STATE_CHANGED);
                    Bundle b = new Bundle();
                    b.putBoolean(Constants.CONNECTED_TO_BOARD, false);
                    m.setData(b);
                    h.sendMessage(m);
                    connected = false;
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


                    Message m = h.obtainMessage(Constants.MESSAGE_BLE_CONNECTION_STATE_CHANGED);
                    Bundle b = new Bundle();
                    b.putBoolean(Constants.CONNECTED_TO_BOARD, true);
                    m.setData(b);
                    h.sendMessage(m);

                    //characteristic.setValue("BT Connected"); //max 30bytes
                    //gatt.writeCharacteristic(characteristic); // Send message to client
                    connected = true;
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

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
                if(!sendingQueue.isEmpty()) {
                    sendingQueue.removeFirst();
                    if (sendingQueue.size() > 0) {
                        //still messages to send, so send next one
                        sendMessage(sendingQueue.getFirst());
                    }
                }
            }
        });
    }

    public void addSendMessage(byte[]message){
        if(message.length>20){
         Log.e("TAG TAG", "you cannot send messaged larger than 20 bytes");
        }else {
            sendingQueue.add(message);
            if (sendingQueue.size() == 1) {
                sendMessage(message);
            }
        }
    }

    private void sendMessage(byte[] message){
        Log.i("MESSAGE TAG", "Send: " + new String(message));
        if(characteristic != null){
            characteristic.setValue(message);
            btGatt.writeCharacteristic(characteristic);
        }
    }

    public boolean isConnected(){
        return this.connected;
    }

    public ConnectedPlayerListItem getBoard(){
        if(!connected)
            return null;

        return new ConnectedPlayerListItem(btGatt.getDevice().getAddress(),btGatt.getDevice().getName());
    }

    private String boolToString(boolean b){
        return b?"1":"0";
    }

 //   private void sendCommand(/*Command, value*/String  cmd){
//    }

    public void sendGameSettings(CustomGameSettings settings){
        //addSendMessage(settings.getSendArray());

        addSendMessage((BTCommands.SET_RANDOM_SPEED + boolToString(settings.getRandomSpeed()) + ".").getBytes());
        addSendMessage((BTCommands.SET_START_SPEED + String.valueOf(settings.getStartSpeed()) + ".").getBytes());
        addSendMessage((BTCommands.SET_SPEED_MIN_DELAY + String.valueOf(settings.getSpeedMinDelay()) + ".").getBytes());
        addSendMessage((BTCommands.SET_SPEED_MAX_DELAY + String.valueOf(settings.getSpeedMaxDelay()) + ".").getBytes());
        addSendMessage((BTCommands.SET_SPEED_MIN_STEP_SIZE + String.valueOf(settings.getSpeedMinStepSize()) + ".").getBytes());
        addSendMessage((BTCommands.SET_SPEED_MAX_STEP_SIZE + String.valueOf(settings.getSpeedMaxStepSize()) + ".").getBytes());
        addSendMessage((BTCommands.SET_ENABLE_REVERSE + boolToString(settings.getEnableReverse()) + ".").getBytes());
        addSendMessage((BTCommands.SET_CHEF_MODE + boolToString(settings.getChefMode()) + ".").getBytes());
        addSendMessage((BTCommands.SET_CHEF_ROULETTE + boolToString(settings.getChefRoulette()) + ".").getBytes());
        addSendMessage((BTCommands.SET_CHEF_CHANGE_DELAY + String.valueOf(settings.getChefChangeDelay()) + ".").getBytes());
        addSendMessage((BTCommands.SET_CHEF_HAS_SHORTER_COOLDOWN + boolToString(settings.getChefHasShorterCooldown()) + ".").getBytes());
        addSendMessage((BTCommands.SET_ENABLE_ITEMS + boolToString(settings.getEnableItems()) + ".").getBytes());
        addSendMessage((BTCommands.SET_ENABLE_EVENTS + boolToString(settings.getEnableEvents()) + ".").getBytes());
        
    }

    public void sendGameStart(){
        addSendMessage((BTCommands.START_GAME + ".").getBytes());
    }
}
