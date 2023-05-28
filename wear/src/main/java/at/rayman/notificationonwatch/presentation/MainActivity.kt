package at.rayman.notificationonwatch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import at.rayman.common.Notification
import at.rayman.common.RabbitMQ
import at.rayman.notificationonwatch.presentation.theme.NotificationWatchTheme
import com.google.gson.Gson
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp(true)
        }
    }
}

@Composable
fun WearApp(connect: Boolean) {
    NotificationWatchTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            Notifications(connect)
        }
    }
}

@Composable
fun Notifications(connect: Boolean) {
    val notifications = remember {
        mutableStateListOf("Test1", "Test2", "Test3")
    }
    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        item {
            ListHeader {
                Text(text = "Notifications")
            }
        }
        items(notifications) { item ->
            Text(text = item)
        }
    }

    if (connect) {
        LaunchedEffect(listState) {
            withContext(Dispatchers.IO) {
                startRabbitMQ(notifications)
            }
        }
    }
}

fun startRabbitMQ(notifications: MutableList<String>) {
    val connection = RabbitMQ.getFactory().newConnection()
    val channel = connection.createChannel()

    val callback = DeliverCallback { _, delivery ->
        val message = String(delivery.body)
        val notification = Gson().fromJson(message, Notification::class.java)
        notifications.add(0, notification.id)
    }
    channel.basicConsume(RabbitMQ.QUEUE, true, callback) { _ -> }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(false)
}