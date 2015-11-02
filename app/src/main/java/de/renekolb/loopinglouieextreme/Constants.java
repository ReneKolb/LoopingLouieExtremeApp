package de.renekolb.loopinglouieextreme;

/**
 * Created by Admi on 16.10.2015.
 */
public class Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_DISCOVERED_BLE_DEVICE = 7;
    public static final int MESSAGE_START_DISCOVERING_BLE_DEVICES = 8;
    public static final int MESSAGE_STOP_DISCOVERING_BLE_DEVICES = 9;

    public static final int MESSAGE_BLE_CONNECTION_STATE_CHANGED = 10;
    public static final String CONNECTED_TO_BOARD = "connected_to_board";

    public static final int MESSAGE_TOO_FEW_PLAYERS_CHIPS = 11;
    public static final String PLAYER_AMOUNT = "player_amount";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_ENABLE_BT = 3;

    //Buttons that have been pressed on Fragments
    //Main Menu Fragment
    public static final int BUTTON_HOST_GAME = 1;
    public static final int BUTTON_CONNECT = 2;
    public static final int BUTTON_SETTINGS = 3;
    //Host Game Fragment
    public static final int BUTTON_TEST_BLACK = 4;
    public static final int BUTTON_TEST_SERVER_MESSAGE = 5;
    public static final int BUTTON_TEST_CLIENT_MESSAGE = 6;
    public static final int BUTTON_GAME_SETTINGS = 7;

    public static final int BUTTON_GAME_SETTINGS_CUSTOM_SETTINGS = 8;
    public static final int BUTTON_GAME_SETTINGS_START_GAME = 9;
    public static final int BUTTON_GAME_SETTINGS_TEST_WHEEL = 10;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final String DEVICE_ADDRESS = "address";
}
