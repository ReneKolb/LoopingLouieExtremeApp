package de.renekolb.loopinglouieextreme.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.BTPackets.PacketClientChangeItem;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerUpdatePlayerSettings;
import de.renekolb.loopinglouieextreme.ConnectionState;
import de.renekolb.loopinglouieextreme.DeviceRole;
import de.renekolb.loopinglouieextreme.FragmentUtils;
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

    private static final long CHIP_UPDATE_DELAY = 3000; //3 sec

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private int mSelectedItem;

    private boolean mPlayerNameEdible = true;

    private PlayerSettingsListAdapter playerSettingsListAdapter;

    private Handler chipCountRefreshTimer;

    private boolean changeingPosition;
    private int changePositionIndex;

    private InputMethodManager inputMethodManager;

    public static PlayerSettingsFragment newInstance() {
        return new PlayerSettingsFragment();
    }

    public PlayerSettingsFragment() {
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

        chipCountRefreshTimer = new Handler();
        inputMethodManager = (InputMethodManager) fa.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (savedInstanceState == null) {
            this.playerSettingsListAdapter = new PlayerSettingsListAdapter(fa, fa.getGame());
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: show player -> #chips,
        // disable Player, oder error if no chips
        // set "local" player oder "connected"

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_settings, container, false);

        final LinearLayout llConState = (LinearLayout) view.findViewById(R.id.ll_player_settings_select_connection_state);
        final Button btnOpen = (Button) view.findViewById(R.id.btn_player_settings_open);
        final Button btnLocal = (Button) view.findViewById(R.id.btn_player_settings_local);
        final Button btnClose = (Button) view.findViewById(R.id.btn_player_settings_close);

        final LinearLayout llBooster = (LinearLayout) view.findViewById(R.id.ll_player_settings_booster);
        final Button btnTurbo = (Button) view.findViewById(R.id.btn_player_settings_turbo);
        final Button btnSlow = (Button) view.findViewById(R.id.btn_player_settings_slow);
        final Button btnReverse = (Button) view.findViewById(R.id.btn_player_settings_reverse);
        final Button btnBlackout = (Button) view.findViewById(R.id.btn_player_settings_blackout);

        final LinearLayout llPlayerName = (LinearLayout) view.findViewById(R.id.ll_player_settings_set_player_name);
        final TextView tvPlayerName = (TextView) view.findViewById(R.id.tv_player_settings_player_name_title);
        final EditText etPlayerName = (EditText) view.findViewById(R.id.et_player_settings_player_name_edit);

        if (fa.deviceRole == DeviceRole.SERVER) {
            llBooster.setVisibility(View.INVISIBLE);
            /*btnTurbo.setVisibility(View.INVISIBLE);
            btnSlow.setVisibility(View.INVISIBLE);
            btnReverse.setVisibility(View.INVISIBLE);
            btnBlackout.setVisibility(View.INVISIBLE);*/
        }

        llConState.setVisibility(View.GONE);
        /*btnOpen.setVisibility(View.GONE);
        btnLocal.setVisibility(View.GONE);
        btnClose.setVisibility(View.GONE);*/

        llPlayerName.setVisibility(View.GONE);
        /*tvPlayerName.setVisibility(View.GONE);
        etPlayerName.setVisibility(View.GONE);*/

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
                    fa.getGame().getGamePlayer(mSelectedItem).setGuestName(s.toString());
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));
                }
            }
        });

        etPlayerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    /*if (mSelectedItem != -1) {
                        fa.getGame().getGamePlayer(mSelectedItem).setGuestName(etPlayerName.getText().toString());
                        playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                        fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));
                    }*/

                    inputMethodManager.hideSoftInputFromWindow(etPlayerName.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);
                    if (player.getConnectionState() == ConnectionState.CONNECTED) {
                        //Disconnect current Player
                        fa.btServer.disconnectClient(player.getRemoteAddress(), true);
                    }
                    player.setConnectionState(ConnectionState.OPEN);
                    playerSettingsListAdapter.update(mSelectedItem, player);
                    //fa.sendPlayerSettingsUpdate(mSelectedItem);
                    fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));

                    llBooster.setVisibility(View.INVISIBLE);
                    /*btnTurbo.setVisibility(View.INVISIBLE);
                    btnSlow.setVisibility(View.INVISIBLE);
                    btnReverse.setVisibility(View.INVISIBLE);
                    btnBlackout.setVisibility(View.INVISIBLE);*/

                    llPlayerName.setVisibility(View.INVISIBLE);
                    /*tvPlayerName.setVisibility(View.INVISIBLE);
                    etPlayerName.setVisibility(View.INVISIBLE);*/

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
                    if (player.getConnectionState() == ConnectionState.CONNECTED) {
                        //Disconnect current Player
                        fa.btServer.disconnectClient(player.getRemoteAddress(), true);
                    }
                    player.setConnectionState(ConnectionState.LOCAL);
                    playerSettingsListAdapter.update(mSelectedItem, player);
                    //fa.sendPlayerSettingsUpdate(mSelectedItem);
                    fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));

                    llBooster.setVisibility(View.VISIBLE);
                    /*btnTurbo.setVisibility(View.VISIBLE);
                    btnSlow.setVisibility(View.VISIBLE);
                    btnReverse.setVisibility(View.VISIBLE);
                    btnBlackout.setVisibility(View.VISIBLE);*/

                    if (player.isGuest()) {
                        llPlayerName.setVisibility(View.VISIBLE);
                        /*tvPlayerName.setVisibility(View.VISIBLE);
                        etPlayerName.setVisibility(View.VISIBLE);*/

                        etPlayerName.setText(player.getDisplayName());
                    }

                    btnOpen.setEnabled(true);
                    btnLocal.setEnabled(false);
                    btnClose.setEnabled(true);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);
                    if (player.getConnectionState() == ConnectionState.CONNECTED) {
                        //Disconnect current Player
                        fa.btServer.disconnectClient(player.getRemoteAddress(), true);
                    }
                    player.setConnectionState(ConnectionState.CLOSED);
                    player.setGuestName(null);
                    playerSettingsListAdapter.update(mSelectedItem, player);
                    //fa.sendPlayerSettingsUpdate(mSelectedItem);
                    fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));

                    llBooster.setVisibility(View.INVISIBLE);
                    /*btnTurbo.setVisibility(View.INVISIBLE);
                    btnSlow.setVisibility(View.INVISIBLE);
                    btnReverse.setVisibility(View.INVISIBLE);
                    btnBlackout.setVisibility(View.INVISIBLE);*/

                    llPlayerName.setVisibility(View.INVISIBLE);
                    /*tvPlayerName.setVisibility(View.INVISIBLE);
                    etPlayerName.setVisibility(View.INVISIBLE);*/

                    btnOpen.setEnabled(true);
                    btnLocal.setEnabled(true);
                    btnClose.setEnabled(false);
                }
            }
        });


        btnTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTurbo.setEnabled(false);
                btnSlow.setEnabled(true);
                btnReverse.setEnabled(true);
                btnBlackout.setEnabled(true);
                if (fa.deviceRole == DeviceRole.SERVER) {
                    if (mSelectedItem != -1) {
                        fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.TURBO);
                        playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                        //fa.sendPlayerSettingsUpdate(mSelectedItem);
                        fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem,fa.getGame().getGamePlayer(mSelectedItem)));
                    }
                } else if (fa.deviceRole == DeviceRole.CLIENT) {
                    //fa.sendItemTypeToServer(ItemType.TURBO);
                    fa.btClient.sendPacket(new PacketClientChangeItem(ItemType.TURBO.getItemID()));
                }
            }
        });

        btnSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTurbo.setEnabled(true);
                btnSlow.setEnabled(false);
                btnReverse.setEnabled(true);
                btnBlackout.setEnabled(true);
                if (fa.deviceRole == DeviceRole.SERVER) {
                    if (mSelectedItem != -1) {
                        fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.SLOW);
                        playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                        //fa.sendPlayerSettingsUpdate(mSelectedItem);
                        fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));
                    }
                } else if (fa.deviceRole == DeviceRole.CLIENT) {
                    //fa.sendItemTypeToServer(ItemType.SLOW);
                    fa.btClient.sendPacket(new PacketClientChangeItem(ItemType.SLOW.getItemID()));
                }
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTurbo.setEnabled(true);
                btnSlow.setEnabled(true);
                btnReverse.setEnabled(false);
                btnBlackout.setEnabled(true);
                if (fa.deviceRole == DeviceRole.SERVER) {
                    if (mSelectedItem != -1) {
                        fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.REVERSE);
                        playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                        //fa.sendPlayerSettingsUpdate(mSelectedItem);
                        fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));
                    }
                } else if (fa.deviceRole == DeviceRole.CLIENT) {
                    //fa.sendItemTypeToServer(ItemType.REVERSE);
                    fa.btClient.sendPacket(new PacketClientChangeItem(ItemType.REVERSE.getItemID()));
                }
            }
        });

        btnBlackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTurbo.setEnabled(true);
                btnSlow.setEnabled(true);
                btnReverse.setEnabled(true);
                btnBlackout.setEnabled(false);
                if (fa.deviceRole == DeviceRole.SERVER) {
                    if (mSelectedItem != -1) {
                        fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.BLACKOUT);
                        playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                        //fa.sendPlayerSettingsUpdate(mSelectedItem);
                        fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem)));
                    }
                } else if (fa.deviceRole == DeviceRole.CLIENT) {
                    //fa.sendItemTypeToServer(ItemType.BLACKOUT);
                    fa.btClient.sendPacket(new PacketClientChangeItem(ItemType.BLACKOUT.getItemID()));
                }
            }
        });

        mSelectedItem = -1;

        final ListView lvPlayers = (ListView) view.findViewById(R.id.lv_player_settings_players);
        lvPlayers.setAdapter(this.playerSettingsListAdapter);

        if (fa.deviceRole == DeviceRole.SERVER) {
            lvPlayers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    changeingPosition = true;
                    changePositionIndex = position;
                    Log.i("Test", "long click:" + position);
                    return true;
                }
            });

            lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.i("Test", "click:" + position);
                    if (changeingPosition) {
                        Log.i("Test", "swap:" + changePositionIndex + " - " + position);
                        changeingPosition = false;
                        if (changePositionIndex != position) {
                            Log.i("Test", "swap!");
                            fa.getGame().switchGamePlayers(changePositionIndex, position);
                            playerSettingsListAdapter.update(changePositionIndex, fa.getGame().getGamePlayer(changePositionIndex));
                            playerSettingsListAdapter.update(position, fa.getGame().getGamePlayer(position));

                            //sync with clients
                            fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(changePositionIndex,fa.getGame().getGamePlayer(changePositionIndex)));
                            fa.btServer.sendMessageToAll(new PacketServerUpdatePlayerSettings(position,fa.getGame().getGamePlayer(position)));

                            //fa.sendPlayerSettingsUpdate(changePositionIndex);
                            //fa.sendPlayerSettingsUpdate(position);
                        }
                    } else {

                        if (mSelectedItem == -1 || mSelectedItem != position) {
                            mSelectedItem = position;

                            PlayerSettingsListAdapter p = (PlayerSettingsListAdapter) parent.getAdapter();

                            GamePlayer player = fa.getGame().getGamePlayer(mSelectedItem);

                            if (player.getConnectionState().equals(ConnectionState.LOCAL)) {
                                llBooster.setVisibility(View.VISIBLE);
                                /*btnTurbo.setVisibility(View.VISIBLE);
                                btnSlow.setVisibility(View.VISIBLE);
                                btnReverse.setVisibility(View.VISIBLE);
                                btnBlackout.setVisibility(View.VISIBLE);*/

                                if (player.isGuest()) {
                                    llPlayerName.setVisibility(View.VISIBLE);
                                    /*tvPlayerName.setVisibility(View.VISIBLE);
                                    etPlayerName.setVisibility(View.VISIBLE);*/
                                } else {
                                    llPlayerName.setVisibility(View.INVISIBLE);
                                    /*tvPlayerName.setVisibility(View.INVISIBLE);
                                    etPlayerName.setVisibility(View.INVISIBLE);*/
                                }
                            } else {
                                llBooster.setVisibility(View.INVISIBLE);
                                /*btnTurbo.setVisibility(View.INVISIBLE);
                                btnSlow.setVisibility(View.INVISIBLE);
                                btnReverse.setVisibility(View.INVISIBLE);
                                btnBlackout.setVisibility(View.INVISIBLE);*/

                                llPlayerName.setVisibility(View.INVISIBLE);
                                /*tvPlayerName.setVisibility(View.INVISIBLE);
                                etPlayerName.setVisibility(View.INVISIBLE);*/
                            }

                            if (mPlayerNameEdible) {
                                if (player.getConnectionState().equals(ConnectionState.LOCAL) && player.isGuest()) {
                                    llPlayerName.setVisibility(View.VISIBLE);
                                    /*tvPlayerName.setVisibility(View.VISIBLE);
                                    etPlayerName.setVisibility(View.VISIBLE);*/

                                    etPlayerName.setText(player.getDisplayName());
                                    etPlayerName.setSelection(etPlayerName.getText().length());
                                }

                                llConState.setVisibility(View.VISIBLE);
                                /*btnOpen.setVisibility(View.VISIBLE);
                                btnLocal.setVisibility(View.VISIBLE);
                                btnClose.setVisibility(View.VISIBLE);*/
                            } else {
                                llPlayerName.setVisibility(View.INVISIBLE);
                                /*tvPlayerName.setVisibility(View.INVISIBLE);
                                etPlayerName.setVisibility(View.INVISIBLE);*/
                            }

                            btnOpen.setEnabled(!ConnectionState.OPEN.equals(p.getItem(position).getConnectionState()));
                            btnLocal.setEnabled(!ConnectionState.LOCAL.equals(p.getItem(position).getConnectionState()));
                            btnClose.setEnabled(!ConnectionState.CLOSED.equals(p.getItem(position).getConnectionState()));

                            btnTurbo.setEnabled(!ItemType.TURBO.equals(p.getItem(position).getBooster()));
                            btnSlow.setEnabled(!ItemType.SLOW.equals(p.getItem(position).getBooster()));
                            btnReverse.setEnabled(!ItemType.REVERSE.equals(p.getItem(position).getBooster()));
                            btnBlackout.setEnabled(!ItemType.BLACKOUT.equals(p.getItem(position).getBooster()));
/*                    } else if (mSelectedItem == position) {
                    mSelectedItem = -1;

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
                }
            });
        }

        Button btnStart = (Button) view.findViewById(R.id.btn_player_settings_start);
        if (fa.deviceRole == DeviceRole.SERVER) {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(Constants.buttons.PLAYER_SETTINGS_START_GAME);
                }
            });
        } else {
            btnStart.setVisibility(View.GONE);
        }

        return view;
    }

    private void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    public void stopUpdatingChips() {
        chipCountRefreshTimer.removeCallbacks(updateChipsTask);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (fa.deviceRole == DeviceRole.SERVER) {
            updateChipsTask.run();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        chipCountRefreshTimer.removeCallbacks(updateChipsTask);
    }

    private final Runnable updateChipsTask = new Runnable() {
        @Override
        public void run() {
            Log.v("PlayerSettings", "Request Chip Count");
            fa.btLEService.sendRequestChipsCount();
            chipCountRefreshTimer.postDelayed(updateChipsTask, CHIP_UPDATE_DELAY);
        }
    };


    public void updatePlayerSettings() {
        //mainly used for updated chip amount
        updatePlayerSettings(0);
        updatePlayerSettings(1);
        updatePlayerSettings(2);
        updatePlayerSettings(3);
        //playerSettingsListAdapter.update(0, fa.getGame().getGamePlayer(0));
        //playerSettingsListAdapter.update(1, fa.getGame().getGamePlayer(1));
        //playerSettingsListAdapter.update(2, fa.getGame().getGamePlayer(2));
        //playerSettingsListAdapter.update(3, fa.getGame().getGamePlayer(3));

    }

    public void updatePlayerSettings(final int slot) {
        fa.runOnUiThread(new Runnable() {
            public void run() {
                playerSettingsListAdapter.update(slot, fa.getGame().getGamePlayer(slot));
        } });
    }

    public void setPlayerNameEdible(boolean edible) {
        this.mPlayerNameEdible = edible;
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

}
