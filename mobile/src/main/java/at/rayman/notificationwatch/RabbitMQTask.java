package at.rayman.notificationwatch;

import at.rayman.common.Notification;
import at.rayman.common.RabbitMQ;

public class RabbitMQTask implements Runnable {

	private final Notification notification;

	public RabbitMQTask(Notification notification) {
		this.notification = notification;
	}

	@Override
	public void run() {
		RabbitMQ.publish(notification);
	}

}
