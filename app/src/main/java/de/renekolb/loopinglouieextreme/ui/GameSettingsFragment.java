package de.renekolb.loopinglouieextreme.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.FragmentUtils;
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
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.disableAnimations) {
            Animator a = AnimatorInflater.loadAnimator(fa, nextAnim);
            a.setDuration(0);
            return a;
        } else {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
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

        ImageButton btnUP = (ImageButton) view.findViewById(R.id.btn_game_settings_number_picker_button_up);
        ImageButton btnDOWN = (ImageButton) view.findViewById(R.id.btn_game_settings_number_picker_button_down);
        final TextView tvValue = (TextView) view.findViewById(R.id.tv_game_settings_number_picker_value);
        //TODO: maybe EditText??

        btnUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fa.getGame().getMaxRounds() < 10) {
                    fa.getGame().setMaxRounds(fa.getGame().getMaxRounds() + 1);
                    tvValue.setText(String.valueOf(fa.getGame().getMaxRounds()));
                }
            }
        });

        btnDOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fa.getGame().getMaxRounds() > 1) {
                    fa.getGame().setMaxRounds(fa.getGame().getMaxRounds() - 1);
                    tvValue.setText(String.valueOf(fa.getGame().getMaxRounds()));
                }
            }
        });

        tvValue.setText(String.valueOf(fa.getGame().getMaxRounds()));

      /*  NumberPicker np = (NumberPicker) view.findViewById(R.id.np_game_settings_rounds);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setValue(fa.getGame().getMaxRounds());
        np.setWrapSelectorWheel(false); //click plus when max value is reached, don't go to 1.
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                fa.getGame().setMaxRounds(newVal);
            }
        });*/

        final ScrollView svGameModes = (ScrollView) view.findViewById(R.id.sv_game_settings_game_modes);

        final Button btnClassic = (Button) view.findViewById(R.id.btn_game_settings_classic);
        final Button btnAction = (Button) view.findViewById(R.id.btn_game_settings_action);
        final Button btnCustom = (Button) view.findViewById(R.id.btn_game_settings_custom);
        final ImageButton btnCustomSettings = (ImageButton) view.findViewById(R.id.btn_game_settings_custom_settings);
        final Button btnPlayerSettings = (Button) view.findViewById(R.id.btn_game_settings_player_settings);

        final CheckBox cbEnableWheel = (CheckBox) view.findViewById(R.id.cb_game_settings_enable_wheel_of_fortune);
        final CheckBox cbEnableLoserWheel = (CheckBox) view.findViewById(R.id.cb_game_settings_enable_loser_wheel);
        final CheckBox cbEffects = (CheckBox) view.findViewById(R.id.cb_game_settings_effects);

        if (fa.getGame().getWheelOfFortuneEnabled()) {
            cbEnableWheel.setChecked(true);
            cbEnableLoserWheel.setVisibility(View.VISIBLE);
        } else {
            cbEnableWheel.setChecked(false);
            cbEnableLoserWheel.setVisibility(View.INVISIBLE);
        }

        cbEnableLoserWheel.setChecked(fa.getGame().getLoserWheelEnabled());

        cbEffects.setChecked(!fa.getGame().getGameSettings().getNoAnimations());

        cbEnableWheel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().setWheelOfFortuneEnabled(isChecked);
                if (isChecked) {
                    cbEnableLoserWheel.setVisibility(View.VISIBLE);
                } else {
                    cbEnableLoserWheel.setVisibility(View.INVISIBLE);
                }
            }
        });

        cbEnableLoserWheel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().setLoserWheelEnabled(isChecked);
            }
        });

        cbEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                fa.getGame().getGameSettings().setNoAnimations(!isChecked);
            }
        });


        btnCustomSettings.setVisibility(mGameMode == 2 ? View.VISIBLE : View.GONE);

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
                btnCustomSettings.setVisibility(View.GONE);
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
                btnCustomSettings.setVisibility(View.GONE);
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
                svGameModes.fullScroll(View.FOCUS_DOWN); //scroll to bottom (so the custom settings button is visible)
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
