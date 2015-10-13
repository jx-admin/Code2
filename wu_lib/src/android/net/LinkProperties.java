package android.net;

import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Collection;

public abstract class LinkProperties implements Parcelable {
    public abstract Collection<InetAddress> getAllAddresses();
}
