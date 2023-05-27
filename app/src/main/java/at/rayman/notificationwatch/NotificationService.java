package at.rayman.notificationwatch;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Notification notification = Notification.builder()
			.id(sbn.getPackageName() + ":" + sbn.getPostTime())
			.title(sbn.getNotification().extras.getString("android.title"))
			.content(sbn.getNotification().extras.getString("android.text"))
			.build();
		System.out.println("Notification posted: " + notification.getTitle() + " - " + notification.getContent());
		sendBroadcast(Action.ADD_NOTIFICATION, notification);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Notification notification = Notification.builder()
			.id(sbn.getPackageName() + ":" + sbn.getPostTime())
			.title(sbn.getNotification().extras.getString("android.title"))
			.content(sbn.getNotification().extras.getString("android.text"))
			.build();
		System.out.println("Notification posted: " + notification.getTitle() + " - " + notification.getContent());
		sendBroadcast(Action.REMOVE_NOTIFICATION, notification);
	}

	private void sendBroadcast(Action action, Notification notification) {
		Intent intent = new Intent(action.getAction());
		intent.putExtra("notification", notification);
		sendBroadcast(intent);
	}

}
