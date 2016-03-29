package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.UUID;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


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

    //private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID SERVICE_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    private static final UUID CHARACTERISTIC = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    private Handler h; //TODO: ??????

    public ProgressBar scanningBoardProgress;

    private Button btnGameSettings;

    private BluetoothAdapter mBluetoothAdapter = null;

    public ArrayAdapter<ConnectedPlayerListItem> connectedPlayerAdapter;

    public ArrayAdapter<ConnectedPlayerListItem> availableBoardAdapter; //TODO: Change Type in future

    //private OnFragmentInteractionListener mListener;

    private FullscreenActivity fa;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HostGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HostGameFragment newInstance(Handler h) {
        HostGameFragment fragment = new HostGameFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        fragment.h = h;
        return fragment;
    }

    public HostGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

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

        connectedPlayerAdapter = new ArrayAdapter<>(getActivity(), R.layout.listitem_player);

        ListView players = (ListView) view.findViewById(R.id.lv_host_game_clients);
        players.setAdapter(connectedPlayerAdapter);
        players.setOnItemClickListener(mConnectedPlayerClickListener);

        //loadConnectedPlayers();
        if (fa.btServer.getConnectedDevices() > 0) {
            connectedPlayerAdapter.addAll(fa.btServer.getConnectedPlayers());
        }else{
            connectedPlayerAdapter.add(new ConnectedPlayerListItem(null,"no connected players"));
        }

        availableBoardAdapter = new ArrayAdapter<>(getActivity(), R.layout.listitem_board_connection);

        ListView boards = (ListView) view.findViewById(R.id.lv_host_game_board_devices);
        boards.setAdapter(availableBoardAdapter);
        boards.setOnItemClickListener(mConnectBoardClickListener);

        //loadConnectedBoard();
        if (fa.btLEService.isConnected()) {
            availableBoardAdapter.add(fa.btLEService.getBoard());
        }else{
            availableBoardAdapter.add(new ConnectedPlayerListItem(null,"no connected board"));
        }

        Button btnLeScan = (Button) view.findViewById(R.id.btn_host_game_le_scan);
        btnLeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa.btLEService.startDiscoverDevices();
            }
        });

        scanningBoardProgress = (ProgressBar) view.findViewById(R.id.pb_host_game_scanning_board);

        btnGameSettings = (Button) view.findViewById(R.id.btn_host_game_game_settings);
        btnGameSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.HOST_GAME_GAME_SETTINGS);
            }
        });
        btnGameSettings.setEnabled(fa.appSettings.isDebugEnabled() || fa.btLEService.isConnected());

        ImageButton btnMakeVis = (ImageButton) view.findViewById(R.id.btn_host_game_make_visible);
        btnMakeVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa.btServer.ensureDiscoverable();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        /*//disconnect from board
        if(fa.btLEService!=null){
            fa.btLEService.stopScanning();
            fa.btLEService.disconnect();
        }

        if(fa.btServer != null){
            fa.btServer.stop();
            fa.btServer.disconnectClients();
        }*/

        super.onDestroyView();
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

    public void boardConnectionChanged(boolean connectedToBoard) {
        btnGameSettings.setEnabled(connectedToBoard);
        //TODO: update visual effect ??? ein haken oder so
    }

    private void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
            fa = (FullscreenActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fa = null;
    }

    private final AdapterView.OnItemClickListener mConnectedPlayerClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            fa.btServer.sendMessage(connectedPlayerAdapter.getItem(position).getAddress(), "Msg from Server");
        }
    };

    private final AdapterView.OnItemClickListener mConnectBoardClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            fa.connectToBoard(availableBoardAdapter.getItem(position).getAddress());
        }
    };

}
