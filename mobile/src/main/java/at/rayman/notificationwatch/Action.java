package at.rayman.notificationwatch;

public enum Action {

	ADD_NOTIFICATION("at.rayman.notificationwatch.ACTION_NEW_NOTIFICATION"),
	REMOVE_NOTIFICATION("at.rayman.notificationwatch.ACTION_REMOVED_NOTIFICATION");

	private final String action;

	Action(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

}
