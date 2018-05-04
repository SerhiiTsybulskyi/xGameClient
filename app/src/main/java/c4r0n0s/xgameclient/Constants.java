package c4r0n0s.xgameclient;

public class Constants {
	public interface ACTION {
		public static String MAIN_ACTION = "c4r0n0s.action.main";
		public static String PREV_ACTION = "c4r0n0s.action.prev";
		public static String PLAY_ACTION = "c4r0n0s.action.play";
		public static String NEXT_ACTION = "c4r0n0s.action.next";
		public static String STARTFOREGROUND_ACTION = "c4r0n0s.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "c4r0n0s.action.stopforeground";
		public static String UPDATE_MAIN_NOTIFICATION = "c4r0n0s.action.update_main_notification";

		public static final String CHANNEL_ID_MAIN = "xGameChanelMain";
		public static final String CHANNEL_ID_NOTIFICATION = "xGameChanelNotification";
		public static final String CHANNEL_NAME_MAIN = "XGAME chanel Main";
		public static final String CHANNEL_NAME_NOTIFICATION = "XGAME chanel Notification";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}
}
