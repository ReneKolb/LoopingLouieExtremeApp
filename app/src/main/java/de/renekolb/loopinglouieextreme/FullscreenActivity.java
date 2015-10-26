package de.renekolb.loopinglouieextreme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity implements OnFragmentInteractionListener {
//vorher: AppCompatActivity. Aber es wird keine Action bar benötigt!!

    private View mContentView;

    public BTServerService btServer;
    public BTClientService btClient;

    public BluetoothLEService btLEService;

    private HostGameFragment hostGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.fullscreen_content);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, MainMenuFragment.newInstance("", ""));
        ft.commit();

        if (getResources().getBoolean(R.bool.is_tablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
    }

    @Override
    public void onDestroy() {
        if (btServer != null) {
            btServer.stop();
            btServer = null;
        }
        if (btClient != null) {
            btClient.stop();
            btClient = null;
        }

        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CONNECT_DEVICE_SECURE:
                Toast.makeText(this, "connect...", Toast.LENGTH_SHORT).show();
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    //       connectDevice(data, true);
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BluetoothDevice object
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                    connect(device);
                }
                break;
        }
    }

    @Override
    public void onFragmentInteraction(int button) {
        FragmentTransaction ft;
        switch (button) {
            case Constants.BUTTON_HOST_GAME:
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, this.hostGameFragment = HostGameFragment.newInstance(this.ServiceMessageHandler));
                ft.commit();
                startBTServer();
                startBTLEService();
                break;
            case Constants.BUTTON_CONNECT:
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, ConnectFragment.newInstance());
                ft.commit();
                break;
            case Constants.BUTTON_SETTINGS:
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, SettingsFragment.newInstance("", ""));
                ft.commit();
                break;

            case Constants.BUTTON_TEST_BLACK:
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = 0;
                getWindow().setAttributes(params);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment, BlackFragment.newInstance());
                ft.addToBackStack("BLACK");
                ft.commit();

                //TEST:
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //      wl.release();
                        try {
                            getFragmentManager().popBackStack("BLACK", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } catch (Exception e) {
                            //fixes bug, when the game is closed and the timer is not executed
                            //so ignore this Exception
                        }
                        Toast.makeText(FullscreenActivity.this, "On", Toast.LENGTH_SHORT).show();
                        WindowManager.LayoutParams params2 = getWindow().getAttributes();
                        params2.screenBrightness = -1;
                        getWindow().setAttributes(params2);
                    }
                }, 5000);
                break;
            case Constants.BUTTON_TEST_SERVER_MESSAGE:
                if (btServer != null) {
                    btServer.sendMessageToAll("Test Msg from Server");
                }
                break;
            case Constants.BUTTON_TEST_CLIENT_MESSAGE:
                if (btClient != null) {
                    btClient.sendMessage("test Msg from Client");
                }
                break;
        }
    }


    private void startBTLEService(){
        this.btLEService = new BluetoothLEService(this,ServiceMessageHandler);
    }

    private void startBTServer() {
        this.btServer = new BTServerService(this, ServiceMessageHandler);
        btServer.start();
    }

    public void connect(BluetoothDevice remoteDevice) {
        this.btClient = new BTClientService(this, ServiceMessageHandler);
        btClient.connect(remoteDevice);
    }

  /*  private void hide() {
        // Hide UI first
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
//            actionBar.hide();
//        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, 200);
    }*/

  /*  private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };*/

    /*@SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, 200);
    }*/

    /*private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            //ActionBar actionBar = getSupportActionBar();
            //if (actionBar != null) {
//                actionBar.show();
//            }
        }
    };*/

    /*private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };*/


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler ServiceMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(FullscreenActivity.this, "Read: " + readMessage, Toast.LENGTH_SHORT).show();
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String devName = msg.getData().getString(Constants.DEVICE_NAME);
                    String devAddr = msg.getData().getString(Constants.DEVICE_ADDRESS);
                    //only relevant when hosting a game!
                    if(hostGameFragment!=null) {
                        hostGameFragment.connectedPlayerAdapter.add(new ConnectedPlayerListItem(devAddr, devName));
                    }
                    //Toast.makeText(FullscreenActivity.this, "new Device: " + devName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_CONNECTION_LOST:
                    String addr = msg.getData().getString(Constants.DEVICE_ADDRESS);
                    //only relevant when hosting a game
                    if(btServer!=null) {
                        btServer.disconnectClient(addr, true); //cleanup internal connectedThread List
                        ConnectedPlayerListItem item = null;
                        for (int i = 0; i < hostGameFragment.connectedPlayerAdapter.getCount(); i++) {
                            if (hostGameFragment.connectedPlayerAdapter.getItem(i).getAddress().equals(addr)) {
                                item = hostGameFragment.connectedPlayerAdapter.getItem(i);
                                break;
                            }
                        }
                        if (item != null) {
                            hostGameFragment.connectedPlayerAdapter.remove(item);

                        } else {
                            //ERROR
                            Toast.makeText(FullscreenActivity.this, "ERROR unknown address", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case Constants.MESSAGE_START_DISCOVERING_BLE_DEVICES:
                    hostGameFragment.scanningBoardProgress.setVisibility(View.VISIBLE);
                    break;
                case Constants.MESSAGE_STOP_DISCOVERING_BLE_DEVICES:
                    hostGameFragment.scanningBoardProgress.setVisibility(View.INVISIBLE);
                    break;
                case Constants.MESSAGE_DISCOVERED_BLE_DEVICE:
                    String bleAddr = msg.getData().getString(Constants.DEVICE_ADDRESS);
                    String bleName = msg.getData().getString(Constants.DEVICE_NAME);
                    if(hostGameFragment!=null){
                        boolean contains = false;
                        for(int i=0;i<hostGameFragment.availableBoardAdapter.getCount();i++) {
                            if(hostGameFragment.availableBoardAdapter.getItem(i).getAddress().equals(bleAddr)){
                                contains = true;
                                break;
                            }
                        }
                        if(!contains) {
                            hostGameFragment.availableBoardAdapter.add(new ConnectedPlayerListItem(bleAddr, bleName));
                        }
                    }

                    break;
                case Constants.MESSAGE_TOAST:
                        Toast.makeText(FullscreenActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    /*private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }*/

    public void connectToBoard(String remoteAddress){
        btLEService.connect(remoteAddress, onBoardMessageRead);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private ReceiveCallback onBoardMessageRead =  new ReceiveCallback() {
        @Override
        public void onReceiveMessage(String message) {
            Message msg = ServiceMessageHandler.obtainMessage(Constants.MESSAGE_TOAST);
            Bundle b = new Bundle();
            b.putString(Constants.TOAST,"Read: "+message);
            msg.setData(b);
            ServiceMessageHandler.sendMessage(msg);

            //DEBUG: echo to all clients
            if(btServer!=null) {
                //should always be true
                btServer.sendMessageToAll("board:"+message);
            }
        }
    };
}
