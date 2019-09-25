package nsn.mobile.apps.nsnnotifier.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public final class NotificationSearchResult implements Parcelable {

	public static final Creator<NotificationSearchResult> CREATOR = new Creator<NotificationSearchResult>() {
		@Override
		public NotificationSearchResult[] newArray(int size) {
			return new NotificationSearchResult[size];
		}
		
		@Override
		public NotificationSearchResult createFromParcel(Parcel source) {
			return new NotificationSearchResult(source);
		}
	};
	
	private List<Notifications> Notifications;
	
	public NotificationSearchResult() {
		Notifications = new ArrayList<Notifications>();
	}
	
	@SuppressWarnings("unchecked")
	private NotificationSearchResult(Parcel source) {
		Notifications = source.readArrayList(Notifications.class.getClassLoader());
	}
	
	public void addNotification(Notifications Notification) {
		Notifications.add(Notification);
	}
	
	public void removeNotification(Notifications Notification) {
		Notifications.remove(Notification);
	}
	
	public List<Notifications> getNotifications() {
		return Notifications;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(Notifications);
	}

}
