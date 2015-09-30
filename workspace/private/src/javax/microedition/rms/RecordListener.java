package javax.microedition.rms; 

public interface RecordListener
{
    public abstract void recordAdded(RecordStore recordStore, int recordId);

    public abstract void recordChanged(RecordStore recordStore, int recordId);

    public abstract void recordDeleted(RecordStore recordStore, int recordId);

}
