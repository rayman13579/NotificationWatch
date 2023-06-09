package at.rayman.notificationwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.rayman.common.Notification;

public class MainActivity extends AppCompatActivity {

	private NotificationAdapter notificationAdapter;

	private ExecutorService executorService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		requestNotificationAccess();
		executorService = Executors.newSingleThreadExecutor();

		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		notificationAdapter = new NotificationAdapter();
		recyclerView.setAdapter(notificationAdapter);

		createNotifButton();
		registerNotificationReceiver();
	}

	private void createNotifButton() {
		findViewById(R.id.button).setOnClickListener(v -> {
			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			notificationManager.notify((int) System.currentTimeMillis(), new NotificationCompat.Builder(this, "test")
				.setContentTitle("rip")
				.setContentText("message")
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.build());
		});
	}

	private void registerNotificationReceiver() {
		BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (Action.ADD_NOTIFICATION.getAction().equals(intent.getAction())) {
					Notification notification = (Notification) intent.getSerializableExtra("notification");
					notificationAdapter.addNotification(notification);
					executorService.execute(new RabbitMQTask(notification));
				} else if (Action.REMOVE_NOTIFICATION.getAction().equals(intent.getAction())) {
					notificationAdapter.removeNotification((Notification) intent.getSerializableExtra("notification"));
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Action.ADD_NOTIFICATION.getAction());
		intentFilter.addAction(Action.REMOVE_NOTIFICATION.getAction());
		registerReceiver(notificationReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		executorService.shutdown();
	}

	private void requestNotificationAccess() {
		if (!hasNotificationAccessPermission()) {
			startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
		}
	}

	private boolean hasNotificationAccessPermission() {
		Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
		return packageNames.contains(getPackageName());
	}
}