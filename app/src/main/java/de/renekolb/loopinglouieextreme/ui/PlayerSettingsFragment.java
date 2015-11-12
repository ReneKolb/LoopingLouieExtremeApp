package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.ConnectionState;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.ItemType;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerSettingsFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private int mSelectedItem;

    private boolean mPlayerNameEdible = true;

    private PlayerSettingsListAdapter playerSettingsListAdapter;

    private Handler chipCountRefreshTimer;

    public static PlayerSettingsFragment newInstance() {
        PlayerSettingsFragment fragment = new PlayerSettingsFragment();
        return fragment;
    }

    public PlayerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        chipCountRefreshTimer = new Handler();

        if(savedInstanceState == null){
            this.playerSettingsListAdapter = new PlayerSettingsListAdapter(fa,fa.getGame());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: show player -> #chips,
        // disable Player, oder error if no chips
        // set "local" player oder "connected"

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_settings, container, false);

        final Button btnOpen = (Button) view.findViewById(R.id.btn_player_settings_open);
        final Button btnLocal = (Button) view.findViewById(R.id.btn_player_settings_local);
        final Button btnClose = (Button) view.findViewById(R.id.btn_player_settings_close);

        final Button btnTurbo = (Button)view.findViewById(R.id.btn_player_settings_turbo);
        final Button btnSlow = (Button)view.findViewById(R.id.btn_player_settings_slow);
        final Button btnReverse = (Button)view.findViewById(R.id.btn_player_settings_reverse);
        final Button btnBlackout = (Button)view.findViewById(R.id.btn_player_settings_blackout);

        final TextView tvPlayerName = (TextView)view.findViewById(R.id.tv_player_settings_player_name_title);
        final EditText etPlayerName = (EditText)view.findViewById(R.id.et_player_settings_player_name_edit);


        btnTurbo.setVisibility(View.INVISIBLE);
        btnSlow.setVisibility(View.INVISIBLE);
        btnReverse.setVisibility(View.INVISIBLE);
        btnBlackout.setVisibility(View.INVISIBLE);

        btnOpen.setVisibility(View.INVISIBLE);
        btnLocal.setVisibility(View.INVISIBLE);
        btnClose.setVisibility(View.INVISIBLE);

        tvPlayerName.setVisibility(View.INVISIBLE);
        etPlayerName.setVisibility(View.INVISIBLE);

        etPlayerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSelectedItem != -1) {
                    fa.getGame().getGamePlayer(mSelectedItem).setDisplayName(s.toString());
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                }
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);
                    player.setConnectionState(ConnectionState.OPEN);
                    playerSettingsListAdapter.update(mSelectedItem, player);

                    btnTurbo.setVisibility(View.INVISIBLE);
                    btnSlow.setVisibility(View.INVISIBLE);
                    btnReverse.setVisibility(View.INVISIBLE);
                    btnBlackout.setVisibility(View.INVISIBLE);

                    tvPlayerName.setVisibility(View.INVISIBLE);
                    etPlayerName.setVisibility(View.INVISIBLE);

                    btnOpen.setEnabled(false);
                    btnLocal.setEnabled(true);
                    btnClose.setEnabled(true);
                }
            }
        });

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);
                    player.setConnectionState(ConnectionState.LOCAL);
                    playerSettingsListAdapter.update(mSelectedItem, player);

                    btnTurbo.setVisibility(View.VISIBLE);
                    btnSlow.setVisibility(View.VISIBLE);
                    btnReverse.setVisibility(View.VISIBLE);
                    btnBlackout.setVisibility(View.VISIBLE);

                    tvPlayerName.setVisibility(View.VISIBLE);
                    etPlayerName.setVisibility(View.VISIBLE);

                    etPlayerName.setText(player.getDisplayName());

                    btnOpen.setEnabled(true);
                    btnLocal.setEnabled(false);
                    btnClose.setEnabled(true);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-1){
                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);
                    player.setConnectionState(ConnectionState.CLOSED);
                    playerSettingsListAdapter.update(mSelectedItem, player);

                    btnTurbo.setVisibility(View.INVISIBLE);
                    btnSlow.setVisibility(View.INVISIBLE);
                    btnReverse.setVisibility(View.INVISIBLE);
                    btnBlackout.setVisibility(View.INVISIBLE);

                    tvPlayerName.setVisibility(View.INVISIBLE);
                    etPlayerName.setVisibility(View.INVISIBLE);

                    btnOpen.setEnabled(true);
                    btnLocal.setEnabled(true);
                    btnClose.setEnabled(false);
                }
            }
        });

        btnTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    btnTurbo.setEnabled(false);
                    btnSlow.setEnabled(true);
                    btnReverse.setEnabled(true);
                    btnBlackout.setEnabled(true);
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.TURBO);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                }
            }
        });

        btnSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(false);
                    btnReverse.setEnabled(true);
                    btnBlackout.setEnabled(true);
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.SLOW);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Slow");
                }
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(true);
                    btnReverse.setEnabled(false);
                    btnBlackout.setEnabled(true);
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.REVERSE);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Reverse");
                }
            }
        });

        btnBlackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(true);
                    btnReverse.setEnabled(true);
                    btnBlackout.setEnabled(false);
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.BLACKOUT);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Blackout");
                }
            }
        });

        mSelectedItem = -1;

        final ListView lvPlayers = (ListView) view.findViewById(R.id.lv_player_settings_players);
        lvPlayers.setAdapter(this.playerSettingsListAdapter);
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedItem == -1 || mSelectedItem != position) {
                    mSelectedItem = position;

                    PlayerSettingsListAdapter p = (PlayerSettingsListAdapter) parent.getAdapter();

                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);

                    if(player.getConnectionState().equals(ConnectionState.LOCAL)) {
                        btnTurbo.setVisibility(View.VISIBLE);
                        btnSlow.setVisibility(View.VISIBLE);
                        btnReverse.setVisibility(View.VISIBLE);
                        btnBlackout.setVisibility(View.VISIBLE);

                        tvPlayerName.setVisibility(View.VISIBLE);
                        etPlayerName.setVisibility(View.VISIBLE);
                    }else {
                        btnTurbo.setVisibility(View.INVISIBLE);
                        btnSlow.setVisibility(View.INVISIBLE);
                        btnReverse.setVisibility(View.INVISIBLE);
                        btnBlackout.setVisibility(View.INVISIBLE);

                        tvPlayerName.setVisibility(View.INVISIBLE);
                        etPlayerName.setVisibility(View.INVISIBLE);
                    }

                    if(mPlayerNameEdible) {
                        if (player.getConnectionState().equals(ConnectionState.LOCAL)) {
                            tvPlayerName.setVisibility(View.VISIBLE);
                            etPlayerName.setVisibility(View.VISIBLE);

                            etPlayerName.setText(player.getDisplayName());
                            etPlayerName.setSelection(etPlayerName.getText().length());
                        }

                        btnOpen.setVisibility(View.VISIBLE);
                        btnLocal.setVisibility(View.VISIBLE);
                        btnClose.setVisibility(View.VISIBLE);
                    }

                    btnOpen.setEnabled(!ConnectionState.OPEN.equals(p.getItem(position).getConnectionState()));
                    btnLocal.setEnabled(!ConnectionState.LOCAL.equals(p.getItem(position).getConnectionState()));
                    btnClose.setEnabled(!ConnectionState.CLOSED.equals(p.getItem(position).getConnectionState()));

                    btnTurbo.setEnabled(!ItemType.TURBO.equals(p.getItem(position).getBooster()));
                    btnSlow.setEnabled(!ItemType.SLOW.equals(p.getItem(position).getBooster()));
                    btnReverse.setEnabled(!ItemType.REVERSE.equals(p.getItem(position).getBooster()));
                    btnBlackout.setEnabled(!ItemType.BLACKOUT.equals(p.getItem(position).getBooster()));
                }else if(mSelectedItem == position){
/*                    mSelectedItem = -1;

                    btnTurbo.setVisibility(View.INVISIBLE);
                    btnSlow.setVisibility(View.INVISIBLE);
                    btnReverse.setVisibility(View.INVISIBLE);
                    btnBlackout.setVisibility(View.INVISIBLE);

                    btnOpen.setVisibility(View.INVISIBLE);
                    btnLocal.setVisibility(View.INVISIBLE);
                    btnClose.setVisibility(View.INVISIBLE);

                    tvPlayerName.setVisibility(View.INVISIBLE);
                    etPlayerName.setVisibility(View.INVISIBLE);*/
                }
            }
        });

        Button btnStart = (Button)view.findViewById(R.id.btn_player_settings_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipCountRefreshTimer.removeCallbacks(updateChipsTask);
                onButtonPressed(Constants.buttons.PLAYER_SETTINGS_START_GAME);
            }
        });

        return view;
    }

    public void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateChipsTask.run();
    }

    @Override
    public void onPause(){
        super.onPause();
        chipCountRefreshTimer.removeCallbacks(updateChipsTask);
    }

    private Runnable updateChipsTask = new Runnable() {
        @Override
        public void run() {
            fa.btLEService.sendRequestChipsCount();
            chipCountRefreshTimer.postDelayed(updateChipsTask,3000);
        }
    };


    public void updatePlayerSettings(){
        //mainly used for updated chip amount
                playerSettingsListAdapter.update(0,fa.getGame().getGamePlayer(0));
                playerSettingsListAdapter.update(1,fa.getGame().getGamePlayer(1));
                playerSettingsListAdapter.update(2,fa.getGame().getGamePlayer(2));
                playerSettingsListAdapter.update(3,fa.getGame().getGamePlayer(3));

    }

    public void setPlayerNameEdible(boolean edible){
        this.mPlayerNameEdible = edible;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
            fa = (FullscreenActivity)activity;
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
