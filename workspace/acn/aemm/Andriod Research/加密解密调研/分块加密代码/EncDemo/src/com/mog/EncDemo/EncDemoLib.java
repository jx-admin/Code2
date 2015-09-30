package com.mog.EncDemo;

/*
 * this file is the .so lib base file 
 */
public class EncDemoLib {
	
	
    public native int EncryptFile(byte[] filepathname , byte[] password);
    public native int DecryptFile(byte[] filepathname , byte[] password);
  /*  
	static {
        System.loadLibrary("libdm-core");
    }
    public static void main(String[] args) {
    	EncDemoLib edl = new EncDemoLib();
    	edl.EncryptFile(null,null) ; 
    	edl.DecryptFile(null, null) ; 
    }
    */
}