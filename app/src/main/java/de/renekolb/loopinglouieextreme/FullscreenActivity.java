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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;
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
import de.renekolb.loopinglouieextreme.ui.SettingsFragment;
import de.renekolb.loopinglouieextreme.ui.WheelOfFortuneFragment;

public class FullscreenActivity extends Activity implements OnFragmentInteractionListener {
//vorher: AppCompatActivity. Aber es wird keine Action bar benötigt!!

    public static boolean BLE_SUPPORT;

    public BTServerService btServer;
    public BTClientService btClient;

    public BluetoothLEService btLEService;

    private MainMenuFragment mainMenuFragment;
    private SettingsFragment settingsFragment;
    private HostGameFragment hostGameFragment;
    private ConnectFragment connectFragment;
    private GameSettingsFragment gameSettingsFragment;
    private CustomGameSettingsFragment customGameSettingsFragment;
    private PlayerSettingsFragment playerSettingsFragment;
    private GameFragment gameFragment;
    private GameResultFragment gameResultsFragment;
    private WheelOfFortuneFragment wheelOfFortuneFragment;

    private Game game;

    //private Runnable actionBTon;

    public AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        appSettings = new AppSettings(this);

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
                    game = new Game(this);
                    startBTServer();
                    startBTLEService();
                }

                break;
            case Constants.buttons.MAIN_MENU_CONNECT:
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

            case Constants.buttons.MAIN_MENU_INFO:
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.toast_achievement, (ViewGroup) findViewById(R.id.rl_toast_achievement_root));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP, 0, 150);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);

                toast.show();
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
            case Constants.buttons.CONNECT_GAME_TEST_CLIENT_MESSAGE:
                if (btClient != null) {
                    btClient.sendMessage("test Msg from Client");
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
                break;

            case Constants.buttons.GAME_SETTINGS_TEST_WHEEL:
                ft = getFragmentManager().beginTransaction();
                if (wheelOfFortuneFragment == null) {
                    wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance();
                }
                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                ft.commit();
                break;
            case Constants.buttons.GAME_RESULTS_WHEEL_OF_FORTUNE:
                ft = getFragmentManager().beginTransaction();
                if (wheelOfFortuneFragment == null) {
                    wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance();
                }
                //TODO: only temporary
                wheelOfFortuneFragment.setPlayerSpin(game.first, game.second, game.third, game.fourth);

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                ft.commit();
                break;

            case Constants.buttons.WHEEL_OF_FORTUNE_NEXT_ROUND:
                //TODO: handle wheel correct (next Wheel or next Round)
                //Toast.makeText(FullscreenActivity.this, "Hier gehts noch nicht weiter", Toast.LENGTH_SHORT).show();
                if (game.getCurrentRound() >= game.getMaxRounds()) {
                    //TODO: show final stats Screen...
                    //but for now: go back to first screen (Main Menu)
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //TODO:
                    //alternative: nur bis zum GameSettingsFragment zurück (nochmal spielen ohne neu zu verbinden)
                    //        oder komplett zum anfang (wie jetzt) und neue rollen (host/client) verteilen
                } else {
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
                break;

            case Constants.buttons.SETTINGS_TEST_WHEEL:
                ft = getFragmentManager().beginTransaction();
                if (wheelOfFortuneFragment == null) {
                    wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance();
                }
                //TODO: only temporary
                wheelOfFortuneFragment.setPlayerSpin(-1, -1, -1, -1);

                ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                ft.addToBackStack(null);
                ft.replace(R.id.main_fragment, wheelOfFortuneFragment);
                ft.commit();
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
        this.btClient = new BTClientService(this, ServiceMessageHandler);
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
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(FullscreenActivity.this, "Read: " + readMessage, Toast.LENGTH_SHORT).show();
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.messages.BT_DEVICE_NAME:
                    // save the connected device's name
                    String devName = msg.getData().getString(Constants.KEY_DEVICE_NAME);
                    String devAddr = msg.getData().getString(Constants.KEY_DEVICE_ADDRESS);
                    //only relevant when hosting a game!
                    if (hostGameFragment != null) {
                        hostGameFragment.connectedPlayerAdapter.add(new ConnectedPlayerListItem(devAddr, devName));
                    }
                    //Toast.makeText(FullscreenActivity.this, "new Device: " + devName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.messages.BT_CONNECTION_FAILED:
                    Toast.makeText(FullscreenActivity.this, "Connection Failed!", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.messages.BT_CONNECTION_LOST:
                    String addr = msg.getData().getString(Constants.KEY_DEVICE_ADDRESS);
                    Toast.makeText(FullscreenActivity.this, "BT Connection Lost", Toast.LENGTH_SHORT).show();
                    //only relevant when hosting a game
                    if (btServer != null) {
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

                        } else {
                            //ERROR
                            Toast.makeText(FullscreenActivity.this, "ERROR unknown address", Toast.LENGTH_SHORT).show();
                        }
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

    private ReceiveCallback onBoardMessageRead = new ReceiveCallback() {
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
                        String[] scoreSplit = command.substring(1).split("\\:");
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
}
