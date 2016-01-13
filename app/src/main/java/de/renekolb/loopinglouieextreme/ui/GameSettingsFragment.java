package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


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

    private int mGameMode;

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameSettingsFragment.
     */
    public static GameSettingsFragment newInstance(int gameMode) {
        GameSettingsFragment fragment = new GameSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_MODE, gameMode);
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
        np.setValue(fa.getGame().getMaxRounds());
        np.setWrapSelectorWheel(false); //click plus when max value is reached, don't go to 1.
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                fa.getGame().setMaxRounds(newVal);
            }
        });

        final Button btnClassic = (Button) view.findViewById(R.id.btn_game_settings_classic);
        final Button btnAction = (Button) view.findViewById(R.id.btn_game_settings_action);
        final Button btnCustom = (Button) view.findViewById(R.id.btn_game_settings_custom);
        final ImageButton btnCustomSettings = (ImageButton) view.findViewById(R.id.btn_game_settings_custom_settings);
        final Button btnPlayerSettings = (Button) view.findViewById(R.id.btn_game_settings_player_settings);

        btnCustomSettings.setVisibility(mGameMode == 2 ? View.VISIBLE : View.INVISIBLE);

        switch (mGameMode) {
            case 0:
                btnClassic.setEnabled(false);
                fa.getGame().getGameSettings().loadClassic();
                break;
            case 1:
                btnAction.setEnabled(false);
                fa.getGame().getGameSettings().loadAction();
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
                fa.getGame().getGameSettings().loadClassic();
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
                fa.getGame().getGameSettings().loadAction();
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
                onButtonPressed(Constants.buttons.GAME_SETTINGS_CUSTOM_SETTINGS);
            }
        });

        btnPlayerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.GAME_SETTINGS_PLAYER_SETTINGS);
            }
        });

        return view;
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
