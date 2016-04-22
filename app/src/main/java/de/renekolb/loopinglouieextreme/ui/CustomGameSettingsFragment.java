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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.renekolb.loopinglouieextreme.CustomGameSettings;
import de.renekolb.loopinglouieextreme.FragmentUtils;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomGameSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomGameSettingsFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private DecimalFormat format;

    private TextView tvStartSpeedCnt;
    private SeekBar sbStartSpeed;
    private TableRow trMinSpeed;
    private TableRow trMaxSpeed;
    private TableRow trSpeedMinDelay;
    private TableRow trSpeedMaxDelay;
    private TableRow trSpeedMinStepSize;
    private TableRow trSpeedMaxStepSize;
    private TableRow trEnableReverse;
    private Switch swRandomSpeed;
    private SeekBar sbMinSpeed;
    private TextView tvMinSpeed;
    private SeekBar sbMaxSpeed;
    private TextView tvMaxSpeed;
    private TextView tvSpeedMinDelayCnt;
    private SeekBar sbSpeedMinDelay;
    private TextView tvSpeedMaxDelay;
    private SeekBar sbSpeedMaxDelay;
    private TextView tvSpeedMinStepSizeCnt;
    private SeekBar sbSpeedMinStepSize;
    private TextView tvSpeedMaxStepSizeCnt;
    private SeekBar sbSpeedMaxStepSize;
    private Switch swEnableReverse;
    private TableRow trChefRoulette;
    private TableRow trChefDelay;
    private TableRow trChefCooldown;
    private Switch swChefMode;
    private Switch swChefRoulette;
    private TextView tvChefChangeDelayCnt;
    private SeekBar sbChefChangeDelay;
    private Switch swChefCooldown;
    private Switch swEnableItems;
    private Switch swEnableEvents;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomGameSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomGameSettingsFragment newInstance() {
        return new CustomGameSettingsFragment();
    }

    public CustomGameSettingsFragment() {
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
        format = new DecimalFormat("#0.0 sec");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_custom_game_settings, container, false);

        tvStartSpeedCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_start_speed_cnt);
        tvStartSpeedCnt.setText(String.valueOf(fa.getGame().getGameSettings().getStartSpeed() - 50));

        sbStartSpeed = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_start_speed);
        sbStartSpeed.setMax(70);
        sbStartSpeed.setProgress(fa.getGame().getGameSettings().getStartSpeed() - 50);
        sbStartSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStartSpeedCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setStartSpeed((byte) (50 + progress));//50 .. 120
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        trMinSpeed = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_min);
        trMaxSpeed = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_max);
        trSpeedMinDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_min_delay);
        trSpeedMaxDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_max_delay);
        trSpeedMinStepSize = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_min_step_size);
        trSpeedMaxStepSize = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_max_step_size);
        trEnableReverse = (TableRow) view.findViewById(R.id.tr_custom_game_settings_enable_reverse);

        swRandomSpeed = (Switch) view.findViewById(R.id.sw_custom_game_settings_random_speed);
        swRandomSpeed.setChecked(fa.getGame().getGameSettings().getRandomSpeed());
        if (swRandomSpeed.isChecked()) {
            trMinSpeed.setVisibility(View.VISIBLE);
            trMaxSpeed.setVisibility(View.VISIBLE);
            trSpeedMinDelay.setVisibility(View.VISIBLE);
            trSpeedMaxDelay.setVisibility(View.VISIBLE);
            trSpeedMinStepSize.setVisibility(View.VISIBLE);
            trSpeedMaxStepSize.setVisibility(View.VISIBLE);
            trEnableReverse.setVisibility(View.VISIBLE);
        } else {
            trMinSpeed.setVisibility(View.GONE);
            trMaxSpeed.setVisibility(View.GONE);
            trSpeedMinDelay.setVisibility(View.GONE);
            trSpeedMaxDelay.setVisibility(View.GONE);
            trSpeedMinStepSize.setVisibility(View.GONE);
            trSpeedMaxStepSize.setVisibility(View.GONE);
            trEnableReverse.setVisibility(View.GONE);
        }
        swRandomSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setRandomSpeed(isChecked);
                if (isChecked) {
                    trMinSpeed.setVisibility(View.VISIBLE);
                    trMaxSpeed.setVisibility(View.VISIBLE);
                    trSpeedMinDelay.setVisibility(View.VISIBLE);
                    trSpeedMaxDelay.setVisibility(View.VISIBLE);
                    trSpeedMinStepSize.setVisibility(View.VISIBLE);
                    trSpeedMaxStepSize.setVisibility(View.VISIBLE);
                    trEnableReverse.setVisibility(View.VISIBLE);
                } else {
                    trMinSpeed.setVisibility(View.GONE);
                    trMaxSpeed.setVisibility(View.GONE);
                    trSpeedMinDelay.setVisibility(View.GONE);
                    trSpeedMaxDelay.setVisibility(View.GONE);
                    trSpeedMinStepSize.setVisibility(View.GONE);
                    trSpeedMaxStepSize.setVisibility(View.GONE);
                    trEnableReverse.setVisibility(View.GONE);
                }
            }
        });

        tvMinSpeed = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_cnt);
        tvMinSpeed.setText(String.valueOf(fa.getGame().getGameSettings().getMinSpeed() - CustomGameSettings.MIN_MOTOR_SPEED));

        sbMinSpeed = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min);
        sbMinSpeed.setMax(CustomGameSettings.MAX_MOTOR_SPEED - CustomGameSettings.MIN_MOTOR_SPEED + 1);
        sbMinSpeed.setProgress(fa.getGame().getGameSettings().getMinSpeed() - CustomGameSettings.MIN_MOTOR_SPEED);
        sbMinSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fa.getGame().getGameSettings().setMinSpeed((byte)(progress + CustomGameSettings.MIN_MOTOR_SPEED));
                tvMinSpeed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        tvMaxSpeed = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_max_cnt);
        tvMaxSpeed.setText(String.valueOf(fa.getGame().getGameSettings().getMaxSpeed() - CustomGameSettings.MIN_MOTOR_SPEED));

        sbMaxSpeed = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_max);
        sbMaxSpeed.setMax(CustomGameSettings.MAX_MOTOR_SPEED - CustomGameSettings.MIN_MOTOR_SPEED + 1);
        sbMaxSpeed.setProgress(fa.getGame().getGameSettings().getMaxSpeed() - CustomGameSettings.MIN_MOTOR_SPEED);
        sbMaxSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fa.getGame().getGameSettings().setMaxSpeed((byte)(progress + CustomGameSettings.MIN_MOTOR_SPEED));
                tvMaxSpeed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        tvSpeedMinDelayCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_delay_cnt);
        tvSpeedMinDelayCnt.setText(format.format(fa.getGame().getGameSettings().getSpeedMinDelay() / 1000f));

        sbSpeedMinDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min_delay);
        sbSpeedMinDelay.setMax(150);
        sbSpeedMinDelay.setProgress(fa.getGame().getGameSettings().getSpeedMinDelay() / 100);
        sbSpeedMinDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinDelayCnt.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setSpeedMinDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tvSpeedMaxDelay = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_max_delay_cnt);
        tvSpeedMaxDelay.setText(format.format(fa.getGame().getGameSettings().getSpeedMaxDelay() / 1000f));

        sbSpeedMaxDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_max_delay);
        sbSpeedMaxDelay.setMax(150);
        sbSpeedMaxDelay.setProgress(fa.getGame().getGameSettings().getSpeedMaxDelay() / 100);
        sbSpeedMaxDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMaxDelay.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setSpeedMaxDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSpeedMinStepSizeCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_step_size_cnt);
        tvSpeedMinStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMinStepSize()));

        sbSpeedMinStepSize = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min_step_size);
        sbSpeedMinStepSize.setMax(30);
        sbSpeedMinStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMinStepSize());
        sbSpeedMinStepSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinStepSizeCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setSpeedMinStepSize((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tvSpeedMaxStepSizeCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_max_step_size_cnt);
        tvSpeedMaxStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMaxStepSize()));

        sbSpeedMaxStepSize = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_max_step_size);
        sbSpeedMaxStepSize.setMax(30);
        sbSpeedMaxStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMaxStepSize());
        sbSpeedMaxStepSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMaxStepSizeCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setSpeedMaxStepSize((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        swEnableReverse = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_reverse);
        swEnableReverse.setChecked(fa.getGame().getGameSettings().getEnableReverse());
        swEnableReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableReverse(isChecked);
            }
        });


        trChefRoulette = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_roulette);
        trChefDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_change_chef_delay);
        trChefCooldown = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_cooldown);

        swChefMode = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_mode);
        swChefMode.setChecked(fa.getGame().getGameSettings().getChefMode());
        if (swChefMode.isChecked()) {
            trChefRoulette.setVisibility(View.VISIBLE);
            trChefDelay.setVisibility(View.VISIBLE);
            trChefCooldown.setVisibility(View.VISIBLE);
        } else {
            trChefRoulette.setVisibility(View.GONE);
            trChefDelay.setVisibility(View.GONE);
            trChefCooldown.setVisibility(View.GONE);
        }
        swChefMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefMode(isChecked);
                if (isChecked) {
                    trChefRoulette.setVisibility(View.VISIBLE);
                    trChefDelay.setVisibility(View.VISIBLE);
                    trChefCooldown.setVisibility(View.VISIBLE);
                } else {
                    trChefRoulette.setVisibility(View.GONE);
                    trChefDelay.setVisibility(View.GONE);
                    trChefCooldown.setVisibility(View.GONE);
                }
            }
        });

        swChefRoulette = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_roulette);
        swChefRoulette.setChecked(fa.getGame().getGameSettings().getChefRoulette());
        swChefRoulette.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefRoulette(isChecked);
            }
        });

        tvChefChangeDelayCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_change_chef_delay_cnt);
        tvChefChangeDelayCnt.setText(format.format(fa.getGame().getGameSettings().getChefChangeDelay() / 1000f));

        sbChefChangeDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_change_chef_delay);
        sbChefChangeDelay.setMax(150);
        sbChefChangeDelay.setProgress(fa.getGame().getGameSettings().getChefChangeDelay() / 100);
        sbChefChangeDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvChefChangeDelayCnt.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setChefChangeDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        swChefCooldown = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_cooldown);
        swChefCooldown.setChecked(fa.getGame().getGameSettings().getChefHasShorterCooldown());
        swChefCooldown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefHasShorterCooldown(isChecked);
            }
        });

        swEnableItems = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_items);
        swEnableItems.setChecked(fa.getGame().getGameSettings().getEnableItems());
        swEnableItems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableItems(isChecked);
            }
        });

        swEnableEvents = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_events);
        swEnableEvents.setChecked(fa.getGame().getGameSettings().getEnableEvents());
        swEnableEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableEvents(isChecked);
            }
        });


        Button btnOK = (Button) view.findViewById(R.id.btn_custom_game_settings_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }

    public void updateUI() {
        tvStartSpeedCnt.setText(String.valueOf(fa.getGame().getGameSettings().getStartSpeed() - 50));
        sbStartSpeed.setProgress(fa.getGame().getGameSettings().getStartSpeed() - 50);
        if (fa.getGame().getGameSettings().getRandomSpeed()) {
            trSpeedMinDelay.setVisibility(View.VISIBLE);
            trSpeedMaxDelay.setVisibility(View.VISIBLE);
            trSpeedMinStepSize.setVisibility(View.VISIBLE);
            trSpeedMaxStepSize.setVisibility(View.VISIBLE);
            trEnableReverse.setVisibility(View.VISIBLE);
        } else {
            trSpeedMinDelay.setVisibility(View.GONE);
            trSpeedMaxDelay.setVisibility(View.GONE);
            trSpeedMinStepSize.setVisibility(View.GONE);
            trSpeedMaxStepSize.setVisibility(View.GONE);
            trEnableReverse.setVisibility(View.GONE);
        }
        swRandomSpeed.setChecked(fa.getGame().getGameSettings().getRandomSpeed());
        tvSpeedMinDelayCnt.setText(format.format(fa.getGame().getGameSettings().getSpeedMinDelay() / 1000f));
        sbSpeedMinDelay.setProgress(fa.getGame().getGameSettings().getSpeedMinDelay() / 100);
        tvSpeedMaxDelay.setText(format.format(fa.getGame().getGameSettings().getSpeedMaxDelay() / 1000f));
        sbSpeedMaxDelay.setProgress(fa.getGame().getGameSettings().getSpeedMaxDelay() / 100);
        tvSpeedMinStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMinStepSize()));
        sbSpeedMinStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMinStepSize());
        tvSpeedMaxStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMaxStepSize()));
        sbSpeedMaxStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMaxStepSize());
        swEnableReverse.setChecked(fa.getGame().getGameSettings().getEnableReverse());
        if (fa.getGame().getGameSettings().getChefMode()) {
            trChefRoulette.setVisibility(View.VISIBLE);
            trChefDelay.setVisibility(View.VISIBLE);
            trChefCooldown.setVisibility(View.VISIBLE);
        } else {
            trChefRoulette.setVisibility(View.GONE);
            trChefDelay.setVisibility(View.GONE);
            trChefCooldown.setVisibility(View.GONE);
        }
        swChefMode.setChecked(fa.getGame().getGameSettings().getChefMode());
        swChefRoulette.setChecked(fa.getGame().getGameSettings().getChefRoulette());
        tvChefChangeDelayCnt.setText(format.format(fa.getGame().getGameSettings().getChefChangeDelay() / 1000f));
        sbChefChangeDelay.setProgress(fa.getGame().getGameSettings().getChefChangeDelay() / 100);
        swChefCooldown.setChecked(fa.getGame().getGameSettings().getChefHasShorterCooldown());
        swEnableItems.setChecked(fa.getGame().getGameSettings().getEnableItems());
        swEnableEvents.setChecked(fa.getGame().getGameSettings().getEnableEvents());
    }


    public void onButtonPressed(int button) {
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
