
package javax.microedition.rms; 


public interface RecordFilter
{
    public abstract boolean matches(byte[] candidate);
}
