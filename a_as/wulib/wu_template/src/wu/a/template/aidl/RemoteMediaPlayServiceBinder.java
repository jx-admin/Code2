package wu.a.template.aidl;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author Administrator http://android.blog.51cto.com/268543/537684/
 */
public class RemoteMediaPlayServiceBinder {

	private IMediaPlayerAidlInterface mRemoteService;
	private boolean mIsRemoteBound;

	private ServiceConnection mRemoteConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mRemoteService = IMediaPlayerAidlInterface.Stub
					.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mRemoteService = null;
		}
	};

	public void binder(Context context) {
		if (mIsRemoteBound) {
			context.unbindService(mRemoteConnection);
		} else {
			context.bindService(new Intent("com.demo.IMyService"),
					mRemoteConnection, Context.BIND_AUTO_CREATE);
		}
		mIsRemoteBound = !mIsRemoteBound;
	}

	private void testSet(int index) {
		Person person = new Person();
		person.setName("Person" + index);
		person.setSex(index % 2 == 0);
		try {
			mRemoteService.savePersonInfo(person);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	private String testGet() {
		List<Person> list = null;

		try {
			list = mRemoteService.getAllPerson();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (list != null) {
			StringBuilder text = new StringBuilder();

			for (Person person : list) {
				text.append("\nPerson name:");
				text.append(person.getName());
				text.append("\n             sex :");
				text.append(person.isSex());
			}
			return text.toString();
		}

		return null;

	}
}
