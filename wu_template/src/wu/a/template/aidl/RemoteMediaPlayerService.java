package wu.a.template.aidl;

import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author Administrator http://android.blog.51cto.com/268543/537684/
 */
public class RemoteMediaPlayerService extends Service {

	private LinkedList<Person> personList = new LinkedList<Person>();

	public RemoteMediaPlayerService() {
		mBinder = new IMediaPlayerAidlInterface.Stub() {

			@Override
			public void basicTypes(int anInt, long aLong, boolean aBoolean,
					float aFloat, double aDouble, String aString)
					throws RemoteException {
				// TODO Auto-generated method stub

			}

			@Override
			public void savePersonInfo(Person person) throws RemoteException {
				if (person != null) {
					personList.add(person);
				}
			}

			@Override
			public List<Person> getAllPerson() throws RemoteException {
				return personList;
			}

		};
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IMediaPlayerAidlInterface.Stub mBinder;
}
