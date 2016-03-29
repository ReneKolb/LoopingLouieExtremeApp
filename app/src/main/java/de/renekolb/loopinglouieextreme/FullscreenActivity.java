package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;
import de.renekolb.loopinglouieextreme.PlayerProfiles.Achievement;
import de.renekolb.loopinglouieextreme.PlayerProfiles.Achievements;
import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;
import de.renekolb.loopinglouieextreme.PlayerProfiles.ProfileManager;
import de.renekolb.loopinglouieextreme.ui.AchievementsFragment;
import de.renekolb.loopinglouieextreme.ui.BlackFragment;
import de.renekolb.loopinglouieextreme.ui.ConnectFragment;
import de.renekolb.loopinglouieextreme.ui.Constants;
import de.renekolb.loopinglouieextreme.ui.CustomGameSettingsFragment;
import de.renekolb.loopinglouieextreme.ui.GameFragment;
import de.renekolb.loopinglouieextreme.ui.GameResultFragment;
import de.renekolb.loopinglouieextreme.ui.GameSettingsFragment;
import de.renekolb.loopinglouieextreme.ui.HostGameFragment;
import de.renekolb.loopinglouieextreme.ui.MainMenuFragment;
import de.renekolb.loopinglouieextreme.ui.OnFragmentInteractionListener;
import de.renekolb.loopinglouieextreme.ui.PlayerSettingsFragment;
import de.renekolb.loopinglouieextreme.ui.ProfilesFragment;
import de.renekolb.loopinglouieextreme.ui.SettingsFragment;
import de.renekolb.loopinglouieextreme.ui.StatisticsFragment;
import de.renekolb.loopinglouieextreme.ui.WheelOfFortuneFragment;

public class FullscreenActivity extends Activity implements OnFragmentInteractionListener {
//vorher: AppCompatActivity. Aber es wird keine Action bar benötigt!!

    public static boolean BLE_SUPPORT;

    public BTServerService btServer;
    public BTClientService btClient;

    public BluetoothLEService btLEService;

    private MainMenuFragment mainMenuFragment;
    private SettingsFragment settingsFragment;
    private ProfilesFragment profilesFragment;
    private StatisticsFragment statisticsFragment;
    private AchievementsFragment achievementsFragment;
    private HostGameFragment hostGameFragment;
    private ConnectFragment connectFragment;
    private GameSettingsFragment gameSettingsFragment;
    private CustomGameSettingsFragment customGameSettingsFragment;
    private PlayerSettingsFragment playerSettingsFragment;
    private GameFragment gameFragment;
    private GameResultFragment gameResultsFragment;
    private WheelOfFortuneFragment wheelOfFortuneFragment;

    private PlayerProfile currentPlayerProfile;

    private WheelOfFortuneHandler wheelOfFortuneHandler;

    private Game game;

    public DeviceRole deviceRole;

    public ProfileManager profileManager;

    //private Runnable actionBTon;

    public AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.profileManager = new ProfileManager(this, ServiceMessageHandler);
        this.wheelOfFortuneHandler = new WheelOfFortuneHandler(this);

        //Load default PlayerProfile
        int defaultProfileID = profileManager.getDefaultProfileID();
        if (defaultProfileID == -1) {
            Log.i("Loading", "No default Player: Creating new Player");
            this.currentPlayerProfile = profileManager.createNewPlayerProfile("Default Player");
            profileManager.setDefaultProfileID(this.currentPlayerProfile.getProfileID());
            Log.w("Loading", "No default PlayerProfile found. Creating one");
        } else {
            Log.i("Loading", "Found default Player");
            this.currentPlayerProfile = profileManager.getProfile(defaultProfileID);
        }

        appSettings = new AppSettings(this);

        setContentView(R.layout.activity_fullscreen);

        deviceRole = DeviceRole.NONE;


//TODO: load default!
        //this.currentPlayerProfile = new PlayerProfile(/*"Local Player Name"*/ Build.DEVICE,this.ServiceMessageHandler, false);

        //mContentView = findViewById(R.id.fullscreen_content);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mainMenuFragment == null) {
            mainMenuFragment = MainMenuFragment.newInstance();
        }
        ft.replace(R.id.main_fragment, mainMenuFragment);
        ft.commit();

        if (getResources().getBoolean(R.bool.is_tablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        //registerReceiver(mReceiver, filter);
    }

    public GameFragment getGameFragment() {
        return this.gameFragment;
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

        //unregisterReceiver(mReceiver);

        if (appSettings.getDisableBTonExit()) {
            BluetoothAdapter.getDefaultAdapter().disable();
        }
    }

    @Override
    public void onFragmentInteraction(int button) {
        FragmentTransaction ft;
        switch (button) {
            case Constants.buttons.MAIN_MENU_HOST_GAME:
                if (currentPlayerProfile == null) {
                    Toast.makeText(FullscreenActivity.this, "You don't have selected a profile", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
                } else {
                    ft = getFragmentManager().beginTransaction();
                    if (hostGameFragment == null) {
                        hostGameFragment = HostGameFragment.newInstance(ServiceMessageHandler);
                    }
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, hostGameFragment);
                    ft.commit();
                    //customGameSettings = new CustomGameSettings(); // initialize & set defaults
                    deviceRole = DeviceRole.SERVER;
                    game = new Game(this);
                    startBTServer();
                    startBTLEService();

                    this.game.getGamePlayer(0).setPlayerProfile(this.currentPlayerProfile);
                }

                break;
            case Constants.buttons.MAIN_MENU_CONNECT:
                if (currentPlayerProfile == null) {
                    Toast.makeText(FullscreenActivity.this, "You don't have selected a profile", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
                } else {
                    ft = getFragmentManager().beginTransaction();
                    if (connectFragment == null) {
                        connectFragment = ConnectFragment.newInstance();
                    }
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, connectFragment);
                    ft.commit();
                    deviceRole = DeviceRole.CLIENT;
                    game = new Game(this);
                }

                break;
            case Constants.buttons.MAIN_MENU_SETTINGS:
                ft = getFragmentManager().beginTransaction();
                if (settingsFragment == null) {
                    settingsFragment = SettingsFragment.newInstance();
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, settingsFragment);
                ft.commit();
                break;

            case Constants.buttons.MAIN_MENU_PROFILE:
                ft = getFragmentManager().beginTransaction();
                if (profilesFragment == null) {
                    profilesFragment = ProfilesFragment.newInstance();
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, profilesFragment);
                ft.commit();
                break;

            case Constants.buttons.MAIN_MENU_ACHIEVEMENTS:
                ft = getFragmentManager().beginTransaction();
                if (achievementsFragment == null) {
                    achievementsFragment = AchievementsFragment.newInstance();
                }

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, achievementsFragment);
                ft.commit();

                break;

            case Constants.buttons.ACHIEVEMENTS_STATISTICS:
                ft = getFragmentManager().beginTransaction();
                if (statisticsFragment == null) {
                    statisticsFragment = StatisticsFragment.newInstance();
                }

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, statisticsFragment);
                ft.commit();
                break;

            case Constants.buttons.HOST_GAME_TEST_BLACK:
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
            case Constants.buttons.HOST_GAME_TEST_SERVER_MESSAGE:
                if (btServer != null) {
                    btServer.sendMessageToAll("Test Msg from Server");
                }
                break;
            case Constants.buttons.HOST_GAME_GAME_SETTINGS:
                //the host must be connected to the board and at least one player has to be connected
                /*if(true || btLEService.isConnected() && btServer.getConnectedDevices()>=1) {
                    ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, CustomGameSettingsFragment.newInstance());
                    ft.commit();
                }*/

                ft = getFragmentManager().beginTransaction();
                if (gameSettingsFragment == null) {
                    gameSettingsFragment = GameSettingsFragment.newInstance(1); // DEFAULT VALUE
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, gameSettingsFragment);
                ft.commit();

                break;
            case Constants.buttons.GAME_SETTINGS_CUSTOM_SETTINGS:
                ft = getFragmentManager().beginTransaction();
                if (customGameSettingsFragment == null) {
                    customGameSettingsFragment = CustomGameSettingsFragment.newInstance();
                }

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, customGameSettingsFragment);
                ft.commit();

                break;
            case Constants.buttons.GAME_SETTINGS_PLAYER_SETTINGS:
                sendGameSettingsToClients();

                ft = getFragmentManager().beginTransaction();
                if (playerSettingsFragment == null) {
                    playerSettingsFragment = PlayerSettingsFragment.newInstance();
                }
                playerSettingsFragment.setPlayerNameEdible(true);
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, playerSettingsFragment);
                ft.commit();
                break;
            case Constants.buttons.PLAYER_SETTINGS_START_GAME:
                //TODO: Check if all (relevant) players have 3 chips plugged in
                boolean canStart = true;
                int playerAmount = 4;
                for (GamePlayer p : game.getPlayers()) {
                    if (p.getConnectionState() == ConnectionState.CLOSED) {
                        playerAmount--;
                    }

                    if (p.getConnectionState() == ConnectionState.OPEN) {
                        canStart = false;
                        break;
                    }

                    if (p.getConnectionState() == ConnectionState.CONNECTED || p.getConnectionState() == ConnectionState.LOCAL) {
                        if (p.getCurrentChips() < 3) {
                            canStart = false;
                            break;
                        }
                    }
                }

                if (playerAmount < 2) {
                    Toast.makeText(this, "Zu wenige Spieler!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!canStart) {
                    Toast.makeText(this, "Nicht alle Spieler haben 3 Chips eingesteckt!", Toast.LENGTH_SHORT).show();
                    break;
                }

                //if (canStart) {
                sendGameStart();

                playerSettingsFragment.stopUpdatingChips();
                game.nextRound();
                ft = getFragmentManager().beginTransaction();
                if (gameFragment == null) {
                    gameFragment = GameFragment.newInstance();
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, gameFragment);
                ft.commit();

                btLEService.sendGameSettings(game);
                btLEService.sendGameStart();
                game.setRunning(true);

                // } else {

                // }
                break;

            case Constants.buttons.GAME_SETTINGS_TEST_WHEEL:
                ft = getFragmentManager().beginTransaction();
                if (wheelOfFortuneFragment == null) {
                    wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance(wheelOfFortuneHandler);
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                ft.commit();
                break;
            case Constants.buttons.GAME_RESULTS_WHEEL_OF_FORTUNE:
                ft = getFragmentManager().beginTransaction();

                if (getGame().getWheelOfFortuneEnabled()) {
                    if (wheelOfFortuneFragment == null) {
                        wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance(wheelOfFortuneHandler);
                    }
                    //TODO: only temporary
                    //wheelOfFortuneFragment.setPlayerSpin(game.first, game.second, game.third, game.fourth);
                    wheelOfFortuneHandler.setPlayers(game.first, game.getLoser(), -1, -1);
                    //wheelOfFortuneFragment.setPlayerSpin(game.first, game.getLoser(), -1, -1);

                    sendSwitchToWheelOfFortune();
                    //sendWheelOfFortuneSpinner(game.first, true);

                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                    ft.commit();
                    break;
                    //} else {
                    //don't break; skip WOF -> directly execute WheelOfFortune NextRound button
                }

                //break;

            case Constants.buttons.WHEEL_OF_FORTUNE_NEXT_ROUND:
                //TODO: handle wheel correct (next Wheel or next Round)
                //Toast.makeText(FullscreenActivity.this, "Hier gehts noch nicht weiter", Toast.LENGTH_SHORT).show();
                currentPlayerProfile.updateTotalRoundsPlayed(1);
                if (game.getGamePlayer(game.first).getDisplayName().equals(currentPlayerProfile.getPlayerName())) {
                    currentPlayerProfile.updateTotalRoundsWon(1);
                }

                if (game.getCurrentRound() >= game.getMaxRounds()) {
                    currentPlayerProfile.updateTotalGamesPlayed(1);
                    if (game.getGameWinnerPlayer().getDisplayName().equals(currentPlayerProfile.getPlayerName())) {
                        currentPlayerProfile.updateTotalGamesWon(1);
                    }
                    sendEndgameToClients();
                    //TODO: show final stats Screen...
                    //but for now: go back to first screen (Main Menu)
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //TODO:
                    //alternative: nur bis zum GameSettingsFragment zurück (nochmal spielen ohne neu zu verbinden)
                    //        oder komplett zum anfang (wie jetzt) und neue rollen (host/client) verteilen
                } else {
                    sendNextRoundToClients();
                    ft = getFragmentManager().beginTransaction();
                    if (playerSettingsFragment == null) {
                        playerSettingsFragment = PlayerSettingsFragment.newInstance();
                    }
                    playerSettingsFragment.setPlayerNameEdible(false);
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, playerSettingsFragment);
                    ft.commit();
                }
                profileManager.saveProfile(currentPlayerProfile.getProfileID());
                break;

            case Constants.buttons.SETTINGS_TEST_WHEEL:
                ft = getFragmentManager().beginTransaction();
                if (wheelOfFortuneFragment == null) {
                    wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance(wheelOfFortuneHandler);
                }
                //TODO: only temporary
                wheelOfFortuneHandler.setPlayers(-1, -1, -1, -1);
                //wheelOfFortuneFragment.setPlayerSpin(-1, -1, -1, -1);

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                ft.commit();
                break;

            case Constants.buttons.SETTINGS_WIPE_FILES:
                Log.i("FullscreenActivity", "Wipe files");
                this.profileManager.wipeFiles();
                break;
        }
    }


    private void startBTLEService() {
        this.btLEService = new BluetoothLEService(this, ServiceMessageHandler);
    }

    private void startBTServer() {
        this.btServer = new BTServerService(this, ServiceMessageHandler);
        btServer.start();
    }

    public void connect(BluetoothDevice remoteDevice) {
        Log.i("FA connect", "remote Device: " + remoteDevice);
        this.btClient = new BTClientService(ServiceMessageHandler);
        btClient.connect(remoteDevice);
    }

    /*
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state =  intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_ON:
                        if(actionBTon != null){
                            //actionBTon.run(); ERROR
                            actionBTon = null;
                        }
                        break;
                }
            }
        }
    };*/

    private final Handler ServiceMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.messages.BT_READ:
                    //byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);

                    /*TODO: Bug! wenn erst Hosten und dann zurück zu Hauptmenü und versuchen zu Connecten btServer!=null -> NullPointerException*/
                    if (btServer != null) {
                        serverReceiveMessageFromClient(msg.getData().getString(Constants.KEY_DEVICE_ADDRESS), msg.getData().getString(Constants.messages.KEY_BT_MESSAGE));
                    } else if (btClient != null) {
                        clientReceiveMessageFromServer(/*msg.getData().getString(Constants.KEY_DEVICE_ADDRESS),*/ msg.getData().getString(Constants.messages.KEY_BT_MESSAGE));
                    }
                    //Toast.makeText(FullscreenActivity.this, "Read: " + readMessage, Toast.LENGTH_SHORT).show();
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.messages.BT_DEVICE_CONNECTED:
                    // save the connected device's name
                    String devName = msg.getData().getString(Constants.KEY_DEVICE_NAME);
                    String devAddr = msg.getData().getString(Constants.KEY_DEVICE_ADDRESS);

                    Log.i("BLAAAAAAAA", "BT Connected: Role" + deviceRole);

                    if (devName == null) {
                        Log.e("BLAAAAAAAA", "BT_DEVICE_CONNECTED but device name is null");
                        break;
                    }

                    if (devAddr == null) {
                        Log.e("BLAAAAAAAA", "BT_DEVICE_CONNECTED but device Address is null");
                        break;
                    }

                    if (deviceRole == DeviceRole.SERVER) {
                        Log.i("BLAAAAAAAA", "Role = Server");
                        if (hostGameFragment != null && hostGameFragment.isVisible()) {

                            if (hostGameFragment.connectedPlayerAdapter.getItem(0).getAddress() == null) {
                                hostGameFragment.connectedPlayerAdapter.remove(hostGameFragment.connectedPlayerAdapter.getItem(0));
                            }

                            hostGameFragment.connectedPlayerAdapter.add(new ConnectedPlayerListItem(devAddr, devName));
                        }

                        bindNewPlayer(devAddr, devName.replaceAll("\\.", "_").replaceAll(":", "_"));
                        sendGameSettingsToClient(devAddr);
                    } else if (deviceRole == DeviceRole.CLIENT) {
                        Log.i("BLAAAAAAAA", "Role = Client");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (playerSettingsFragment == null) {
                            playerSettingsFragment = PlayerSettingsFragment.newInstance();
                        }
                        playerSettingsFragment.setPlayerNameEdible(true);
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, playerSettingsFragment);
                        ft.commit();

                        //
                        sendPlayerNameToServer(currentPlayerProfile.getPlayerName());
                    }
                    break;
                case Constants.messages.BT_CONNECTION_FAILED:
                    Toast.makeText(FullscreenActivity.this, "Connection Failed!", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.messages.BT_CONNECTION_LOST:
                    String addr = msg.getData().getString(Constants.KEY_DEVICE_ADDRESS);
                    Toast.makeText(FullscreenActivity.this, "BT Connection Lost", Toast.LENGTH_SHORT).show();
                    //only relevant when hosting a game
                    if (deviceRole == DeviceRole.SERVER) {
                        if (btServer != null) {

                            int pindex = game.getGamePlayerIndex(addr);
                            if (pindex != -1) {
                                game.getGamePlayer(pindex).setConnectionState(ConnectionState.OPEN);
                                if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
                                    playerSettingsFragment.updatePlayerSettings(pindex);
                                }
                            }

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
                                if (hostGameFragment.connectedPlayerAdapter.getCount() == 0) {
                                    hostGameFragment.connectedPlayerAdapter.add(new ConnectedPlayerListItem(null, "no connected players"));
                                }
                            } else {
                                //ERROR
                                Toast.makeText(FullscreenActivity.this, "ERROR unknown address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (deviceRole == DeviceRole.CLIENT) {
                        //TODO: Go back to connect Fragment
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    break;

                case Constants.messages.BLE_START_DISCOVERING:
                    hostGameFragment.scanningBoardProgress.setVisibility(View.VISIBLE);
                    break;
                case Constants.messages.BLE_STOP_DISCOVERING:
                    hostGameFragment.scanningBoardProgress.setVisibility(View.INVISIBLE);
                    break;
                case Constants.messages.BLE_DISCOVERED_DEVICE:
                    String bleAddr = msg.getData().getString(Constants.KEY_DEVICE_ADDRESS);
                    String bleName = msg.getData().getString(Constants.KEY_DEVICE_NAME);
                    if (hostGameFragment != null) {
                        //remove dummy-entry
                        if (hostGameFragment.availableBoardAdapter.getItem(0).getAddress() == null) {
                            hostGameFragment.availableBoardAdapter.remove(hostGameFragment.availableBoardAdapter.getItem(0));
                        }

                        boolean contains = false;
                        for (int i = 0; i < hostGameFragment.availableBoardAdapter.getCount(); i++) {
                            if (hostGameFragment.availableBoardAdapter.getItem(i).getAddress().equals(bleAddr)) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            hostGameFragment.availableBoardAdapter.add(new ConnectedPlayerListItem(bleAddr, bleName));
                        }
                    }

                    break;
                case Constants.messages.BLE_CONNECTION_STATE_CHANGED:
                    boolean connected = msg.getData().getBoolean(Constants.messages.KEY_CONNECTED_TO_BOARD);
                    hostGameFragment.boardConnectionChanged(connected);
                    if (connected) {
                        Toast.makeText(FullscreenActivity.this, "BLE connected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FullscreenActivity.this, "BLE disconnected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.messages.GAME_TOO_FEW_PLAYERS_CHIPS:
                    int amount = msg.getData().getInt(Constants.messages.KEY_PLAYER_AMOUNT);
                    Toast.makeText(FullscreenActivity.this, "Too few players with chips (" + amount + ")", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.messages.BLE_GAME_RESULTS:
                    game.setRunning(false);
                    int first = msg.getData().getInt(Constants.messages.KEY_GAME_RESULTS_FIRST);
                    int second = msg.getData().getInt(Constants.messages.KEY_GAME_RESULTS_SECOND);
                    int third = msg.getData().getInt(Constants.messages.KEY_GAME_RESULTS_THIRD);
                    int fourth = msg.getData().getInt(Constants.messages.KEY_GAME_RESULTS_FOURTH);

                    sendGameResults(first, second, third, fourth);

                    //TODO: only temporary
                    game.first = first;
                    game.second = second;
                    game.third = third;
                    game.fourth = fourth;

                    //int playerAmount = 2+(third == -1?0:1)+(fourth==-1?0:1);

                    game.getGamePlayer(first).addPoints(4);
                    game.getGamePlayer(second).addPoints(3);
                    if (third != -1)
                        game.getGamePlayer(third).addPoints(2);
                    if (fourth != -1)
                        game.getGamePlayer(first).addPoints(1);


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (gameResultsFragment == null) {
                        gameResultsFragment = GameResultFragment.newInstance(first, second, third, fourth);
                    } else {
                        gameResultsFragment.setPlayers(first, second, third, fourth);
                    }
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, gameResultsFragment); // DEFAULT VALUES
                    ft.commit();
                    break;

                case Constants.messages.UNLOCKED_ACHIEVEMENT:
                    int achievementID = msg.getData().getInt(Constants.messages.KEY_ACHIEVEMENT_ID);
                    Achievement ach = Achievements.getAchievement(achievementID);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.toast_achievement, (ViewGroup) findViewById(R.id.rl_toast_achievement_root));
                    TextView tvTitle = (TextView) view.findViewById(R.id.tv_toast_achievement_title);
                    TextView tvDescription = (TextView) view.findViewById(R.id.tv_toast_achievement_description);
                    ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_toast_achievement);

                    tvTitle.setText(ach.getTitle()); //TODO: replace with StringID
                    tvDescription.setText(ach.getUnlockedDescription());
                    ivIcon.setImageResource(ach.getDrawableID());

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.TOP, 0, 150);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(view);

                    toast.show();
                    break;
//                case Constants.MESSAGE_TOAST:
//                        Toast.makeText(FullscreenActivity.this, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    };

    public void connectToBoard(String remoteAddress) {
        btLEService.connect(remoteAddress, onBoardMessageRead);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            if (game == null || !game.isRunning()) {
                getFragmentManager().popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }

    public Game getGame() {
        return this.game;
    }

    private final ReceiveCallback onBoardMessageRead = new ReceiveCallback() {
        @Override
        public void onReceiveMessage(String message) {
            Log.i("ReceivedMSG", "received: " + message);
            String[] split = message.split("\\.");
            char cmdType;
            for (String command : split) {
                if (command.length() == 0) {
                    continue;
                }
                cmdType = command.charAt(0);
                Message msg;
                Bundle b;

                switch (cmdType) {
                    case 'a':
                        //Game End Results
                        //Form: a[1st]:[2nd]:[3rd]:[4th]. 1st=winner (liste ist von hinten gefüllt!! d.h. bei 3 Spielern ist [1st] = 255)
                        String[] scoreSplit = command.substring(1).split(":");
                        if (scoreSplit.length != 4) {
                            //ERROR wrong length
                            Log.e("ReceivedMSG", "Wrong Split length in Game End Results");
                            return;
                        }

                        int[] ranking = new int[4];
                        int tmp;
                        int startIndex = -1;
                        for (int i = 0; i < 4; i++) {
                            tmp = Integer.parseInt(scoreSplit[i]);
                            if (tmp <= 3) {
                                if (startIndex == -1) {
                                    startIndex = i;
                                }
                                ranking[i - startIndex] = tmp;
                            }
                        }
                        for (int i = 4 - startIndex; i < 4; i++) {
                            ranking[i] = -1;
                        }

                        msg = ServiceMessageHandler.obtainMessage(Constants.messages.BLE_GAME_RESULTS);
                        b = new Bundle();
                        b.putInt(Constants.messages.KEY_GAME_RESULTS_FIRST, ranking[0]);
                        b.putInt(Constants.messages.KEY_GAME_RESULTS_SECOND, ranking[1]);
                        b.putInt(Constants.messages.KEY_GAME_RESULTS_THIRD, ranking[2]);
                        b.putInt(Constants.messages.KEY_GAME_RESULTS_FOURTH, ranking[3]);
                        msg.setData(b);
                        ServiceMessageHandler.sendMessage(msg);


                    /*    FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if(gameResultsFragment == null){
                            gameResultsFragment = GameResultFragment.newInstance(ranking[0], ranking[1], ranking[2], ranking[3]);
                        }else {
                            gameResultsFragment.setPlayers(ranking[0], ranking[1], ranking[2], ranking[3]);
                        }
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, gameResultsFragment); // DEFAULT VALUES
                        ft.commit();*/
                        break;

                    case 'b':
                        //Cannot start the game: too few Players have plugged chips in
                        //Form: b[anzahl].
                        int amount = Integer.parseInt(command.substring(1));
                        msg = ServiceMessageHandler.obtainMessage(Constants.messages.GAME_TOO_FEW_PLAYERS_CHIPS);
                        b = new Bundle();
                        b.putInt(Constants.messages.KEY_PLAYER_AMOUNT, amount);
                        msg.setData(b);
                        ServiceMessageHandler.sendMessage(msg);
                        break;
                    case 'c':
                        //Countdown started
                        break;
                    case 'd':
                        //players chip amount
                        int firstAmount = Integer.parseInt(command.substring(1, 2));
                        int secondAmount = Integer.parseInt(command.substring(2, 3));
                        int thirdAmount = Integer.parseInt(command.substring(3, 4));
                        int fourthAmount = Integer.parseInt(command.substring(4, 5));

                        if (game != null) {
                            game.getGamePlayer(0).setCurrentChips(firstAmount);
                            game.getGamePlayer(1).setCurrentChips(secondAmount);
                            game.getGamePlayer(2).setCurrentChips(thirdAmount);
                            game.getGamePlayer(3).setCurrentChips(fourthAmount);
                        }

                        if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    playerSettingsFragment.updatePlayerSettings();
                                }
                            });
                        }

                        //send update chips
                        sendPlayerSettingsUpdate(0);
                        sendPlayerSettingsUpdate(1);
                        sendPlayerSettingsUpdate(2);
                        sendPlayerSettingsUpdate(3);

                        break;
                    default:
                        Log.w("BLE READ", "Unkown Command. Read: " + message);
                        /*msg = ServiceMessageHandler.obtainMessage(Constants.MESSAGE_TOAST);
                        b = new Bundle();
                        b.putString(Constants.TOAST,"Read: "+message);
                        msg.setData(b);
                        ServiceMessageHandler.sendMessage(msg);*/
                }
            }
        }
    };

    private void serverReceiveMessageFromClient(String senderAddress, String msg) {
        String[] split = msg.split("\\.");
        char cmdType;
        String data;
        int slot;
        GamePlayer player;
        for (String command : split) {
            if (command.length() == 0) {
                continue;
            }
            cmdType = command.charAt(0);
            data = command.substring(1);

            switch (cmdType) {
                case 'a':
                    // player Name
                    //FORM: a[PlayerName].
                    slot = game.getGamePlayerIndex(senderAddress);
                    if (slot == -1) {
                        slot = bindNewPlayer(senderAddress, "Test");
                    }
                    player = game.getGamePlayer(slot);
                    Log.i("Client Sent", "New Name: " + data);
                    player.setGuestName(data);
                    if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
                        playerSettingsFragment.updatePlayerSettings(slot);
                    }
                    sendPlayerSettingsUpdate(slot); //send the change to all clients
                    break;
                case 'b':
                    //player change item
                    //Form: b[ItemID].
                    slot = game.getGamePlayerIndex(senderAddress);
                    player = game.getGamePlayer(slot);
                    ItemType it = ItemType.fromInt(Integer.parseInt(data));
                    player.setDefaultItemType(it);
                    if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
                        playerSettingsFragment.updatePlayerSettings(slot);
                    }
                    sendPlayerSettingsUpdate(slot); //send the change to all clients
                    break;

                case 'c':
                    //client has spin the Wheel and sent the result
                    //Form: c[ResultIndex].
                    int resultIndex = Integer.parseInt(data);

                    //wheelOfFortuneFragment.setResult(resultIndex);
                    //send result to all other clients
                    btServer.sendMessageToAllBut("f" + resultIndex + ".", senderAddress);

                    break;

                case 'd':
                    //a client spin the wheel
                    String[] split2 = data.split(":");
                    if (split2.length != 2) {
                        Log.e("Error", "Cannot spin Wheel of Fortune. Wrong split length");
                        break;
                    }

                    float viewRot = Float.parseFloat(split2[0].replaceAll(",", "."));
                    float animRot = Float.parseFloat(split2[1].replaceAll(",", "."));

                    wheelOfFortuneHandler.startSpinning(viewRot, animRot, false);
                    sendWheelOfFortuneSpinToClients(viewRot, animRot, senderAddress);
                    break;
                case 'e':
                    //a client pressed the nextRound button (he finished drinking)
                    wheelOfFortuneHandler.updateCurrentPlayer(wheelOfFortuneHandler.getCurrentPosition());
                    sendWheelOfFortuneSpinnerToClients(wheelOfFortuneHandler.getCurrentPosition());
                    break;
            }
        }
    }

    private void clientReceiveMessageFromServer(/*String senderAddress,*/ String msg) {
        String[] split = msg.split("\\.");
        char cmdType;
        String data;
        for (String command : split) {
            if (command.length() == 0) {
                continue;
            }
            cmdType = command.charAt(0);
            data = command.substring(1);

            switch (cmdType) {
                case 'a':
                    Log.v("Client Receive", "Update a client's player settings");
                    //update player settings (complete)
                    //FORM: a[PlayerSlot]:[PlayerName]:[ConnectionStateID]:[ItemID]:[Chips].
                    String[] split2 = data.split(":");
                    if (split2.length != 5) {
                        Log.e("Client Received Message", "Update Player Settings: Wrong split length");
                        continue;
                    }
                    int slot = Integer.parseInt(split2[0]);
                    String name = split2[1];
                    ConnectionState state = ConnectionState.fromInt(Integer.parseInt(split2[2]));
                    ItemType itemType = ItemType.fromInt(Integer.parseInt(split2[3]));
                    int chips = Integer.parseInt(split2[4]);
                    Log.v("Client Receive", "Slot:" + slot + " Name:" + name + " State: " + state + " item: " + itemType + " chips: " + chips);

                    GamePlayer gp = game.getGamePlayer(slot);

                    gp.setConnectionState(state); // first update connection State, because it overrides displayName with null!!
                    gp.setGuestName(name);
                    gp.setDefaultItemType(itemType);
                    gp.setCurrentChips(chips);

                    if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
                        playerSettingsFragment.updatePlayerSettings(slot);
                    }

                    break;
                case 'b':
                    Log.i("Client Recieve MSG", "Game Start");
                    //receive game Start
                    game.nextRound();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (gameFragment == null) {
                        gameFragment = GameFragment.newInstance();
                    }
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, gameFragment);
                    ft.commit();

                    game.setRunning(true);
                    break;
                case 'c':
                    Log.i("Client Recieve MSG", "Game Results");
                    //receive game Results
                    game.setRunning(false);
                    split2 = data.split(":");
                    if (split2.length != 4) {
                        Log.e("Client Received Message", "Game Results: Wrong split length");
                        continue;
                    }

                    int first = Integer.parseInt(split2[0]);
                    int second = Integer.parseInt(split2[1]);
                    int third = Integer.parseInt(split2[2]);
                    int fourth = Integer.parseInt(split2[3]);

                    game.first = first;
                    game.second = second;
                    game.third = third;
                    game.fourth = fourth;

                    //int playerAmount = 2+(third == -1?0:1)+(fourth==-1?0:1);

                    game.getGamePlayer(first).addPoints(4);
                    game.getGamePlayer(second).addPoints(3);
                    if (third != -1)
                        game.getGamePlayer(third).addPoints(2);
                    if (fourth != -1)
                        game.getGamePlayer(first).addPoints(1);


                    ft = getFragmentManager().beginTransaction();
                    if (gameResultsFragment == null) {
                        gameResultsFragment = GameResultFragment.newInstance(first, second, third, fourth);
                    } else {
                        gameResultsFragment.setPlayers(first, second, third, fourth);
                    }
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, gameResultsFragment); // DEFAULT VALUES
                    ft.commit();

                    break;
                case 'd':
                    Log.i("Client Recieve MSG", "GoTo WheelOfFortune");
                    //GOTO WheelOfFortune
                    ft = getFragmentManager().beginTransaction();
                    if (wheelOfFortuneFragment == null) {
                        wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance(wheelOfFortuneHandler);
                    }

                    //TODO: only temporary
                    //wheelOfFortuneFragment.setPlayerSpin(game.first, game.second, game.third, game.fourth);
                    wheelOfFortuneHandler.setPlayers(game.first, game.getLoser(), -1, -1);
                    //wheelOfFortuneFragment.setPlayerSpin(game.first, game.getLoser(), -1, -1);

                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                    ft.commit();
                    break;
                case 'e':
                    Log.i("Client Recieve MSG", "Update Wheel of Fortune Spinner");
                    if (wheelOfFortuneFragment == null || wheelOfFortuneFragment.isHidden()) {
                        Log.e("Error", "Cannot update Wheel of Fortune Spinner. Fragment is not created");
                        break;
                    }
                    final int position = Integer.parseInt(data);
                    wheelOfFortuneHandler.updateCurrentPlayer(position);
                    break;
                case 'f':
                    Log.i("Client Recieve MSG", "Wheel of Fortune Spin Result");
                    break;
                case 'g':
                    Log.i("Client Recieve MSG", "Wheel of Fortune Spin");
                    Log.i("RECIEVED", "received data: " + data);
                    split2 = data.split(":");
                    Log.i("RECIEVED", "received splitLen: " + split2.length);
                    if (split2.length != 2) {
                        Log.e("Error", "Cannot spin Wheel of Fortune. Wrong split length");
                        break;
                    }
                    wheelOfFortuneHandler.startSpinning(Float.parseFloat(split2[0].replaceAll(",", ".")), Float.parseFloat(split2[1].replaceAll(",", ".")), false);
                    break;

                case 'h':
                    Log.i("Client Receive MSG", "Next Round");
                    currentPlayerProfile.updateTotalRoundsPlayed(1);

                    if (game.getGamePlayer(game.first).getDisplayName().equals(currentPlayerProfile.getPlayerName())) {
                        currentPlayerProfile.updateTotalRoundsWon(1);
                    }

                    profileManager.saveProfile(currentPlayerProfile.getProfileID());

                    ft = getFragmentManager().beginTransaction();
                    if (playerSettingsFragment == null) {
                        playerSettingsFragment = PlayerSettingsFragment.newInstance();
                    }
                    playerSettingsFragment.setPlayerNameEdible(true);
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_fragment, playerSettingsFragment);
                    ft.commit();
                    break;
                case 'i':
                    Log.i("Client Receive MSG", "End Game");
                    currentPlayerProfile.updateTotalRoundsPlayed(1);
                    currentPlayerProfile.updateTotalGamesPlayed(1);

                    if (game.getGamePlayer(game.first).getDisplayName().equals(currentPlayerProfile.getPlayerName())) {
                        currentPlayerProfile.updateTotalRoundsWon(1);
                    }
                    if (game.getGameWinnerPlayer().getDisplayName().equals(currentPlayerProfile.getPlayerName())) {
                        currentPlayerProfile.updateTotalGamesWon(1);
                    }
                    profileManager.saveProfile(currentPlayerProfile.getProfileID());
                    btClient.stop();
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    break;
                case 'j':
                    Log.i("Client Receive MSG", "Game Settings");
                    //Form: j[#Rounds]:[WOF enabled]:[LoserWheel enabled].
                    split2 = data.split(":");
                    if (split2.length != 3) {
                        Log.e("Error", "Cannot update game settings. Wrong split length");
                        break;
                    }
                    int rounds = Integer.parseInt(split2[0]);
                    boolean enableWOF = "1".equals(split2[1]);
                    boolean enableLoserWheel = "1".equals(split2[2]);
                    this.game.setMaxRounds(rounds);
                    this.game.setWheelOfFortuneEnabled(enableWOF);
                    this.game.setLoserWheelEnabled(enableLoserWheel);
                    break;
       /*         case 'b':
                    //update player name
                    break;
                case 'c':
                    //update connection state
                    break;
                case 'd':
                    //update player item
                    break;
                case 'e':
                    //update player chip count
                    break;*/
            }
        }
    }

    private void sendPlayerSettingsToClient(String client) {
        sendPlayerSettingsToClient(client, 0);
        sendPlayerSettingsToClient(client, 1);
        sendPlayerSettingsToClient(client, 2);
        sendPlayerSettingsToClient(client, 3);
    }

    private void sendPlayerSettingsToClient(String client, int slot) {
        GamePlayer gp = game.getGamePlayer(slot);
        String msg = "a" + slot + ":" + gp.getDisplayName() + ":" + gp.getConnectionState().getId() + ":" + gp.getDefaultItemType().getItemID() + ":" + gp.getCurrentChips() + ".";
        btServer.sendMessage(client, msg);
    }

    public void sendPlayerSettingsUpdate(int slot) {
        GamePlayer gp = game.getGamePlayer(slot);
        String msg = "a" + slot + ":" + gp.getDisplayName() + ":" + gp.getConnectionState().getId() + ":" + gp.getDefaultItemType().getItemID() + ":" + gp.getCurrentChips() + ".";
        btServer.sendMessageToAll(msg);
        Log.i("Send to Client", "Msg: " + msg);
    }

    private void sendPlayerNameToServer(String name) {
        btClient.sendMessage("a" + name + ".");
    }

    public void sendItemTypeToServer(ItemType itemType) {
        btClient.sendMessage("b" + itemType.getItemID() + ".");

    }

    private void sendGameStart() {
        btServer.sendMessageToAll("b.");
    }

    private void sendGameResults(int first, int second, int third, int fourth) {
        btServer.sendMessageToAll("c" + first + ":" + second + ":" + third + ":" + fourth + ".");
    }

    public void sendWheelOfFortuneSpinToServer(float startViewRot, float animRot) {
        btClient.sendMessage("d" + String.valueOf(startViewRot).replaceAll("\\.", ",") + ":" + String.valueOf(animRot).replaceAll("\\.", ",") + ".");
    }

    public void sendWheelOfFortuneSpinToClients(float startViewRot, float animRot, @Nullable String receivedFromClient) {
        btServer.sendMessageToAllBut("g" + String.valueOf(startViewRot).replaceAll("\\.", ",") + ":" + String.valueOf(animRot).replaceAll("\\.", ",") + ".", receivedFromClient);
    }

    private void sendSwitchToWheelOfFortune() {
        btServer.sendMessageToAll("d.");
    }

    private void sendNextRoundToClients() {
        btServer.sendMessageToAll("h.");
    }

    private void sendEndgameToClients() {
        btServer.sendMessageToAll("i.");
    }

    public void sendWheelOfFortuneSpinnerToClients(int playerPosition) {
        btServer.sendMessageToAll("e" + playerPosition + ".");
    }

    public void sendWheelOfFortuneNextSpinnerToServer() {
        btClient.sendMessage("e.");
    }

    private void sendWheelOfFortuneResultToClients(int resultIndex) {
        btServer.sendMessageToAll("f" + resultIndex + ".");
    }

    private void sendWheelOfFortuneResultToServer(int resultIndex) {
        btClient.sendMessage("c" + resultIndex + ".");
        //btServer.sendMessageToAll("f"+resultIndex+".");
    }

    private void sendGameSettingsToClients() {
        btServer.sendMessageToAll("j" + game.getMaxRounds() + ":" + (game.getWheelOfFortuneEnabled() ? "1" : "0") + ":" + (game.getLoserWheelEnabled() ? "1" : "0") + ".");
    }

    private void sendGameSettingsToClient(String address) {
        btServer.sendMessage(address, "j" + game.getMaxRounds() + ":" + (game.getWheelOfFortuneEnabled() ? "1" : "0") + ":" + (game.getLoserWheelEnabled() ? "1" : "0") + ".");
    }

/*    public void onWheelSpinFinish(int resultIndex){
        if(deviceRole == DeviceRole.CLIENT){
            sendWheelOfFortuneResultToServer(resultIndex);
        }else{
            sendWheelOfFortuneResultToClients(resultIndex);
        }
    }*/

    private int bindNewPlayer(String address, String displayName) {
        if (game.getGamePlayerIndex(address) != -1) {
            //already bound
            return -1;
        }

        int index = game.getNextOpenSlot();
        if (index == -1) {
            Log.e("SDDAFG", "Too many players connected");
            return -1;
        }

        game.bindRemotePlayer(index, address);
        game.getGamePlayer(index).setGuestName(displayName); // Dummy Name
        if (playerSettingsFragment != null && playerSettingsFragment.isVisible()) {
            playerSettingsFragment.updatePlayerSettings(index);
        }

        sendPlayerSettingsToClient(address); // send player list to new client
        sendPlayerSettingsUpdate(index); // send new player to all clients
        return index;
    }

    public PlayerProfile getCurrentPlayer() {
        return this.currentPlayerProfile;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public void setCurrentPlayerProfile(PlayerProfile profile) {
        this.currentPlayerProfile = profile;
        this.mainMenuFragment.updateProfile();
    }

    public WheelOfFortuneFragment getWheelOfFortuneFragment() {
        return this.wheelOfFortuneFragment;
    }

}
