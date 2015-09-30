package com.google.ads;

import android.content.Context;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ac
  implements Runnable
{
  private final Context a;
  private final String b;

  public ac(String paramString, Context paramContext)
  {
    this.b = paramString;
    this.a = paramContext;
  }

  protected HttpURLConnection a(URL paramURL)
    throws IOException
  {
    return (HttpURLConnection)paramURL.openConnection();
  }

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: new 35	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 36	java/lang/StringBuilder:<init>	()V
    //   7: ldc 38
    //   9: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: aload_0
    //   13: getfield 17	com/google/ads/ac:b	Ljava/lang/String;
    //   16: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   22: invokestatic 51	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   25: aload_0
    //   26: new 24	java/net/URL
    //   29: dup
    //   30: aload_0
    //   31: getfield 17	com/google/ads/ac:b	Ljava/lang/String;
    //   34: invokespecial 53	java/net/URL:<init>	(Ljava/lang/String;)V
    //   37: invokevirtual 55	com/google/ads/ac:a	(Ljava/net/URL;)Ljava/net/HttpURLConnection;
    //   40: astore_2
    //   41: aload_2
    //   42: aload_0
    //   43: getfield 19	com/google/ads/ac:a	Landroid/content/Context;
    //   46: invokestatic 60	com/google/ads/util/AdUtil:a	(Ljava/net/HttpURLConnection;Landroid/content/Context;)V
    //   49: aload_2
    //   50: iconst_1
    //   51: invokevirtual 64	java/net/HttpURLConnection:setInstanceFollowRedirects	(Z)V
    //   54: aload_2
    //   55: invokevirtual 67	java/net/HttpURLConnection:connect	()V
    //   58: aload_2
    //   59: invokevirtual 71	java/net/HttpURLConnection:getResponseCode	()I
    //   62: istore 4
    //   64: iload 4
    //   66: sipush 200
    //   69: if_icmplt +11 -> 80
    //   72: iload 4
    //   74: sipush 300
    //   77: if_icmplt +38 -> 115
    //   80: new 35	java/lang/StringBuilder
    //   83: dup
    //   84: invokespecial 36	java/lang/StringBuilder:<init>	()V
    //   87: ldc 73
    //   89: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: iload 4
    //   94: invokevirtual 76	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   97: ldc 78
    //   99: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: aload_0
    //   103: getfield 17	com/google/ads/ac:b	Ljava/lang/String;
    //   106: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   112: invokestatic 81	com/google/ads/util/b:e	(Ljava/lang/String;)V
    //   115: aload_2
    //   116: invokevirtual 84	java/net/HttpURLConnection:disconnect	()V
    //   119: goto +37 -> 156
    //   122: astore_3
    //   123: aload_2
    //   124: invokevirtual 84	java/net/HttpURLConnection:disconnect	()V
    //   127: aload_3
    //   128: athrow
    //   129: astore_1
    //   130: new 35	java/lang/StringBuilder
    //   133: dup
    //   134: invokespecial 36	java/lang/StringBuilder:<init>	()V
    //   137: ldc 86
    //   139: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: aload_0
    //   143: getfield 17	com/google/ads/ac:b	Ljava/lang/String;
    //   146: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: invokevirtual 46	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   152: aload_1
    //   153: invokestatic 90	com/google/ads/util/b:d	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   156: return
    //
    // Exception table:
    //   from	to	target	type
    //   41	115	122	finally
    //   0	41	129	java/lang/Throwable
    //   115	129	129	java/lang/Throwable
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.ac
 * JD-Core Version:    0.5.4
 */