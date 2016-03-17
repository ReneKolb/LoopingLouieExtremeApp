package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.BuildConfig;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PROFILE_NAME = "profileName";

    private TextView tvProfileName;
    private Button btnServer;
    private Button btnClient;
    private ImageButton btnSettings;

    private FullscreenActivity fa;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance() {
        //fragment.fa = fa;
        return new MainMenuFragment();
    }

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        tvProfileName = (TextView) view.findViewById(R.id.tv_main_menu_profile_name);
        tvProfileName.setText(fa.getCurrentPlayer().getPlayerName());

        btnServer = (Button) view.findViewById(R.id.btn_main_menu_server);
        btnServer.setEnabled(FullscreenActivity.BLE_SUPPORT);

        btnClient = (Button) view.findViewById(R.id.btn_main_menu_client);
        btnSettings = (ImageButton) view.findViewById(R.id.btn_main_menu_settings);

        ImageButton btnProfile = (ImageButton) view.findViewById(R.id.btn_main_menu_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.MAIN_MENU_PROFILE);
            }
        });

        ImageButton btnAchievements = (ImageButton) view.findViewById(R.id.btn_main_menu_achievements);
        btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.MAIN_MENU_ACHIEVEMENTS);
            }
        });


        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.buttons.MAIN_MENU_HOST_GAME);
            }
        });

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.buttons.MAIN_MENU_CONNECT);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Constants.buttons.MAIN_MENU_SETTINGS);
            }
        });


        TextView tv = (TextView) view.findViewById(R.id.tv_main_menu_version);
        tv.setText(String.format(getResources().getString(R.string.main_menu_version), BuildConfig.VERSION_NAME));


        return view;
    }

    @Override
    public void onResume() {
        //disconnect from board
        if (fa.btLEService != null) {
            fa.btLEService.stopScanning();
            fa.btLEService.disconnect();
        }

        if (fa.btServer != null) {
            fa.btServer.stop();
            fa.btServer.disconnectClients();
        }

        if (fa.btClient != null && !fa.btClient.isConnecting()) {
            fa.btClient.stop();
        }

        //Log.e("BLAA", "MainMenu Resume. Role = None");
        //fa.deviceRole = DeviceRole.NONE;
        super.onResume();
    }

    private void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    public void updateProfile() {
        this.tvProfileName.setText(fa.getCurrentPlayer().getPlayerName());
    }

    @Override
    public void onAttach(Activity activity) {
//    public void onAttach(Context context) {
        super.onAttach(activity);
//        super.onAttach(context);
        try {
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

}
