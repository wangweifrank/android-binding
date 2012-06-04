package gueei.binding.markupDemoICS.viewModels;

import android.os.Parcel;
import android.os.Parcelable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class RawEntry implements Parcelable {
	public final StringObservable Title = new StringObservable();

	public final IntegerObservable ResId = new IntegerObservable();

	public RawEntry(String title, int resId) {
		Title.set(title);
		ResId.set(resId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(Title.get());
		out.writeInt(ResId.get());
	}

	public static final Parcelable.Creator<RawEntry> CREATOR = new Parcelable.Creator<RawEntry>() {
		public RawEntry createFromParcel(Parcel in) {
			return new RawEntry(in.readString(), in.readInt());
		}

		public RawEntry[] newArray(int size) {
			return new RawEntry[size];
		}
	};
}
