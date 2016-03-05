package de.renekolb.loopinglouieextreme.ui;

public final class Constants {

    public static final class messages {
        // Message types sent from the BluetoothChatService Handler
        public static final int BT_STATE_CHANGE = 1;
        public static final int BT_READ = 2;
        public static final int BT_WRITE = 3;
        public static final int BT_DEVICE_CONNECTED = 4;
        //public static final int MESSAGE_TOAST = 5;
        public static final int BT_CONNECTION_FAILED = 5;
        public static final int BT_CONNECTION_LOST = 6;

        public static final String KEY_BT_MESSAGE = "bt_message";

        public static final int BLE_START_DISCOVERING = 7;
        public static final int BLE_STOP_DISCOVERING = 8;
        public static final int BLE_DISCOVERED_DEVICE = 9;

        public static final int BLE_CONNECTION_STATE_CHANGED = 10;
        public static final String KEY_CONNECTED_TO_BOARD = "connected_to_board";

        public static final int GAME_TOO_FEW_PLAYERS_CHIPS = 11;
        public static final String KEY_PLAYER_AMOUNT = "player_amount";

        public static final int BLE_GAME_RESULTS = 12;
        public static final String KEY_GAME_RESULTS_FIRST = "game_results_first";
        public static final String KEY_GAME_RESULTS_SECOND = "game_results_second";
        public static final String KEY_GAME_RESULTS_THIRD = "game_results_third";
        public static final String KEY_GAME_RESULTS_FOURTH = "game_results_fourth";

        public static final int UNLOCKED_ACHIEVEMENT = 13;
        public static final String KEY_ACHIEVEMENT_ID = "achievement_ID";

    }

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_ENABLE_BT = 3;


    public static final class buttons {

        public static final int MAIN_MENU_HOST_GAME = 1;
        public static final int MAIN_MENU_CONNECT = 2;
        public static final int MAIN_MENU_SETTINGS = 3;
        public static final int MAIN_MENU_PROFILE = 4;
        public static final int MAIN_MENU_ACHIEVEMENTS = 5;

        public static final int HOST_GAME_TEST_BLACK = 6;
        public static final int HOST_GAME_TEST_SERVER_MESSAGE = 7;
        public static final int CONNECT_GAME_TEST_CLIENT_MESSAGE = 8;
        public static final int HOST_GAME_GAME_SETTINGS = 9;

        public static final int GAME_SETTINGS_CUSTOM_SETTINGS = 10;
        public static final int GAME_SETTINGS_PLAYER_SETTINGS = 11;
        public static final int GAME_SETTINGS_TEST_WHEEL = 12;

        public static final int PLAYER_SETTINGS_START_GAME = 13;

        public static final int GAME_RESULTS_WHEEL_OF_FORTUNE = 14;

        public static final int WHEEL_OF_FORTUNE_NEXT_ROUND = 15;

        public static final int SETTINGS_TEST_WHEEL = 16;
        public static final int SETTINGS_WIPE_FILES = 17;
    }


    // Key names received from the BluetoothChatService Handler
    public static final String KEY_DEVICE_NAME = "device_name";
    //public static final String TOAST = "toast";
    public static final String KEY_DEVICE_ADDRESS = "address";
}
