package at.rayman.notificationwatch;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import at.rayman.common.Notification;

public class RabbitMQTask implements Runnable {

	private static final String QUEUE = "NotificationWatch";

	private final Notification notification;

	private final Gson gson = new Gson();

	private ConnectionFactory factory;

	public RabbitMQTask(Notification notification) {
		this.notification = notification;
		factory = new ConnectionFactory();
		factory.setHost("host");
		factory.setUsername("user");
		factory.setPassword("pw");
		factory.setConnectionTimeout(3000);
	}

	@Override
	public void run() {
		try (Connection connection = factory.newConnection();
			 Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE, true, false, false, null);
			channel.basicPublish("", QUEUE, null, gson.toJson(notification).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
