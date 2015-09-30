package javax.microedition.rms; 

public class RecordStoreNotOpenException
    extends RecordStoreException
{
    public RecordStoreNotOpenException(String message) {
    	super(message);
    } 
    
    public RecordStoreNotOpenException() {
    } 
}
