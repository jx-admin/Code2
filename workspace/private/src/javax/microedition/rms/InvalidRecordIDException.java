package javax.microedition.rms; 

import java.lang.Exception;

public class InvalidRecordIDException extends RecordStoreException
{
    public InvalidRecordIDException(String message) {
    	super(message);
    } 
    
    public InvalidRecordIDException() {
    } 
} 
