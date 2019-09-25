package nsn.mobile.apps.nsnnotifier.service;

import android.os.Parcel;
import android.os.Parcelable;

public final class Notifications implements Parcelable {
	
	public static final Creator<Notifications> CREATOR = new Creator<Notifications>() {
		@Override
		public Notifications createFromParcel(Parcel source) {
			return new Notifications(source);
		}

		@Override
		public Notifications[] newArray(int size) {
			return new Notifications[size];
		}
	};
	
	private int msgid;

	public Notifications(int msgid) {
		this.msgid = msgid;
	}
	
	private Notifications(Parcel source) {
		msgid = source.readInt();
	}

	public int getMsgId() {
		return msgid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(msgid);
	}
}
