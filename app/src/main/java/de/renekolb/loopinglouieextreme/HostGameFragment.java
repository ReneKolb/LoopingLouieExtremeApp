package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HostGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostGameFragment extends Fragment {

    private static final String TAG = "TEST BLAA";

    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID CHARACTERISTIC = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Handler h;

    private BluetoothAdapter mBluetoothAdapter = null;

    public ArrayAdapter<ConnectedPlayerListItem> connectedPlayerAdapter;

    private OnFragmentInteractionListener mListener;

    private BluetoothGatt mBluetoothGatt;

    private FullscreenActivity fa;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HostGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HostGameFragment newInstance(String param1, String param2) {
        HostGameFragment fragment = new HostGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HostGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Activity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_host_game, container, false);
        Button btnWakeLock = (Button) view.findViewById(R.id.btn_wake_lock);


        btnWakeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                //final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK/*PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK*/, "Your Tag");
                //wl.acquire();
                onButtonPressed(Constants.BUTTON_TEST_BLACK);
            }
        });

        Button btnTestMessage = (Button) view.findViewById(R.id.btn_test_server_msg);
        btnTestMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.BUTTON_TEST_SERVER_MESSAGE);
            }
        });

        connectedPlayerAdapter = new ArrayAdapter<ConnectedPlayerListItem>(getActivity(), R.layout.listitem_player);

        ListView players = (ListView) view.findViewById(R.id.lv_clients);
        players.setAdapter(connectedPlayerAdapter);
        players.setOnItemClickListener(mConnectedPlayerClickListener);

        Button btnLeScan =(Button)view.findViewById(R.id.btn_le_scan);
        btnLeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.getBluetoothLeScanner().startScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        mBluetoothGatt = result.getDevice().connectGatt(fa, false, bluetoothGattCallback);

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            bluetoothOn();
        }
    }

    private void bluetoothOn() {
        //asdasd
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int button) {
        if (mListener != null) {
            mListener.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            fa = (FullscreenActivity)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private AdapterView.OnItemClickListener mConnectedPlayerClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            fa.btServer.sendMessage(connectedPlayerAdapter.getItem(position).getAddress(),"Msg from Server");
        }
    };

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Log.i(TAG,"connected");
                mBluetoothGatt.discoverServices();
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.i(TAG, "disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                Log.i(TAG, "Service Discovered");
                ///BluetoothGattDescriptor descriptor =
                BluetoothGattService service = mBluetoothGatt.getService(SERVICE_UUID);
                if(service == null){
                    Log.e(TAG,"Service not found");
                    return;
                }
                BluetoothGattCharacteristic chara = service.getCharacteristic(CHARACTERISTIC);
                if(chara == null){
                    Log.e(TAG,"Characteristic not found");
                }

                mBluetoothGatt.setCharacteristicNotification(chara, true);


                chara.setValue("BT Connected"); //max 30bytes
                mBluetoothGatt.writeCharacteristic(chara);
//                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS){
                Log.i(TAG, "Char Read");
                final byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for(byte byteChar : data)
                        stringBuilder.append(String.format("%02X ", byteChar));

                    Log.i(TAG, new String(data) + "\n" + stringBuilder.toString());
                }

            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //on MEssage Read
            String msg = new String(characteristic.getValue());
            Log.i(TAG, "received: "+msg);

            //echo Message
            characteristic.setValue(msg);
            gatt.writeCharacteristic(characteristic);
        }
    };
}
