package de.renekolb.loopinglouieextreme.ui;

public final class Constants {

    public static final class messages {
        // Message types sent from the BluetoothChatService Handler
        public static final int BT_STATE_CHANGE = 1;
        public static final int BT_READ = 2;
        public static final int BT_WRITE = 3;
        public static final int BT_DEVICE_NAME = 4;
        //public static final int MESSAGE_TOAST = 5;
        public static final int BT_CONNECTION_FAILED = 5;
        public static final int BT_CONNECTION_LOST = 6;

        public static final int BLE_START_DISCOVERING = 7;
        public static final int BLE_STOP_DISCOVERING = 8;
        public static final int BLE_DISCOVERED_DEVICE = 9;

        public static final int BLE_CONNECTION_STATE_CHANGED = 10;
        public static final String KEY_CONNECTED_TO_BOARD = "connected_to_board";

        public static final int GAME_TOO_FEW_PLAYERS_CHIPS = 11;
        public static final String KEY_PLAYER_AMOUNT = "player_amount";
    }

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_ENABLE_BT = 3;


    public static final class buttons {

        public static final int MAIN_MENU_HOST_GAME = 1;
        public static final int MAIN_MENU_CONNECT = 2;
        public static final int MAIN_MENU_SETTINGS = 3;

        public static final int HOST_GAME_TEST_BLACK = 4;
        public static final int HOST_GAME_TEST_SERVER_MESSAGE = 5;
        public static final int CONNECT_GAME_TEST_CLIENT_MESSAGE = 6;
        public static final int HOST_GAME_GAME_SETTINGS = 7;

        public static final int GAME_SETTINGS_CUSTOM_SETTINGS = 8;
        public static final int GAME_SETTINGS_START_GAME = 9;
        public static final int GAME_SETTINGS_TEST_WHEEL = 10;

        public static final int HOST_GAME_PLAYER_SETTINGS = 11;
    }


    // Key names received from the BluetoothChatService Handler
    public static final String KEY_DEVICE_NAME = "device_name";
    //public static final String TOAST = "toast";
    public static final String KEY_DEVICE_ADDRESS = "address";
}
