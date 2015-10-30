package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSettingsFragment extends Fragment {

    private static final String ARG_GAME_MODE = "GAME MODE";
    private static final String ARG_ROUNDS = "ROUNDS";

    private int mGameMode;
    private int mRounds;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameSettingsFragment.
     */
    public static GameSettingsFragment newInstance(int gameMode, int rounds) {
        GameSettingsFragment fragment = new GameSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_MODE, gameMode);
        args.putInt(ARG_ROUNDS, rounds);
        fragment.setArguments(args);
        return fragment;
    }

    public GameSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameMode = getArguments().getInt(ARG_GAME_MODE);
            mRounds = getArguments().getInt(ARG_ROUNDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_settings, container, false);
        NumberPicker np = (NumberPicker) view.findViewById(R.id.np_game_settings_rounds);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setValue(mRounds);
        np.setWrapSelectorWheel(false); //click plus when max value is reached, don't go to 1.
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mRounds = newVal;
            }
        });

        final Button btnClassic = (Button) view.findViewById(R.id.btn_game_settings_classic);
        final Button btnAction = (Button) view.findViewById(R.id.btn_game_settings_action);
        final Button btnCustom = (Button) view.findViewById(R.id.btn_game_settings_custom);
        final ImageButton btnCustomSettings = (ImageButton) view.findViewById(R.id.btn_game_settings_custom_settings);
        final Button btnStartGame = (Button) view.findViewById(R.id.btn_game_settings_start_game);

        btnCustomSettings.setVisibility(mGameMode == 2 ? View.VISIBLE : View.INVISIBLE);

        switch(mGameMode){
            case 0:
                btnClassic.setEnabled(false);
                break;
            case 1:
                btnAction.setEnabled(false);
                break;
            case 2:
                btnCustom.setEnabled(false);
                break;
        }

        btnClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClassic.setEnabled(false);
                btnAction.setEnabled(true);
                btnCustom.setEnabled(true);
                btnCustomSettings.setVisibility(View.INVISIBLE);
                mGameMode = 0;
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClassic.setEnabled(true);
                btnAction.setEnabled(false);
                btnCustom.setEnabled(true);
                btnCustomSettings.setVisibility(View.INVISIBLE);
                mGameMode = 1;
            }
        });

        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClassic.setEnabled(true);
                btnAction.setEnabled(true);
                btnCustom.setEnabled(false);
                btnCustomSettings.setVisibility(View.VISIBLE);
                mGameMode = 2;
            }
        });

        btnCustomSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.BUTTON_GAME_SETTINGS_CUSTOM_SETTINGS);
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.BUTTON_GAME_SETTINGS_START_GAME);
            }
        });

        Button btnTestWheel = (Button) view.findViewById(R.id.btn_game_settings_test_wheel);
        btnTestWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.BUTTON_GAME_SETTINGS_TEST_WHEEL);
            }
        });



        return view;
    }

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

}