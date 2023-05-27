package at.rayman.notificationwatch;

import java.io.Serializable;

public class Notification implements Serializable {

	private final String id;

	private final String title;

	private final String content;

	private Notification(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.content = builder.content;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String id;
		private String title;
		private String content;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Notification build() {
			return new Notification(this);
		}
	}


}
