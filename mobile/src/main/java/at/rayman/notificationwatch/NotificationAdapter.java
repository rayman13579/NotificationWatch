package at.rayman.notificationwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import at.rayman.common.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

	private final List<Notification> notifications = new ArrayList<>();

	public void addNotification(Notification notification) {
		notifications.add(0, notification);
		notifyItemInserted(0);
	}

	public void removeNotification(Notification notification) {
		notifications.stream()
			.filter(n -> n.getId().equals(notification.getId()))
			.findFirst()
			.ifPresent(n -> {
				int index = notifications.indexOf(n);
				notifications.remove(n);
				notifyItemRemoved(index);
			});
	}

	@NonNull
	@Override
	public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.item_notification, parent, false);
		return new NotificationViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
		Notification notification = notifications.get(position);
		holder.titleView.setText(notification.getTitle());
		holder.contentView.setText(notification.getContent());
	}

	@Override
	public int getItemCount() {
		return notifications.size();
	}

	static class NotificationViewHolder extends RecyclerView.ViewHolder {
		TextView titleView;
		TextView contentView;

		public NotificationViewHolder(@NonNull View parent) {
			super(parent);
			titleView = parent.findViewById(R.id.notificationTitle);
			contentView = parent.findViewById(R.id.notificationContent);
		}
	}

}
