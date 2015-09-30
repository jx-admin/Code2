package com.aess.aemm.update.net;

public interface NetFlag {
	public final static int FLAG_DOWNLOAD_SUCCESS = 0;
	public final static int FLAG_DOWNLOAD_EXCEPTION = -1;
	public final static int FLAG_URL_EXCEPTION = -2;
	public final static int FLAG_IO_EXCEPTION = -3;
	public final static int FLAG_OPENFILE_FAILED = -4;
	public final static int FLAG_NETWORK_ERRO = -5;
	public final static int FLAG_DOWNLOAD_ING=-6;
	/** ContentLength is 0 */
	public static final int FLAG_DOWNLOAD_ZERO=-7;
	public static final int READ_WHAT=101;
}
