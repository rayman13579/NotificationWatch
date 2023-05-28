package at.rayman.common;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

	public static final String QUEUE = "NotificationWatchAgain";

	private static final Gson gson = new Gson();

	private static volatile ConnectionFactory factory;

	private RabbitMQ() {
	}

	public static void publish(Notification notification) {
		try (Connection connection = getFactory().newConnection();
			 Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE, true, false, false, null);
			channel.basicPublish("", QUEUE, null, gson.toJson(notification).getBytes());
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void consume(DeliverCallback callback) {
		try (Connection connection = getFactory().newConnection();
			 Channel channel = connection.createChannel()) {
			channel.basicConsume(RabbitMQ.QUEUE, true, callback, consumerTag -> {
			});
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionFactory getFactory() {
		if (factory == null) {
			synchronized (RabbitMQ.class) {
				if (factory == null) {
					connect();
				}
			}
		}
		return factory;
	}

	private static synchronized void connect() {
		factory = new ConnectionFactory();
		factory.setHost("host");
		factory.setUsername("user");
		factory.setPassword("pw");
		factory.setConnectionTimeout(3000);
	}
}
