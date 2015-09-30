package com.google.ads.util;

import java.io.UnsupportedEncodingException;

public class c
{
  static
  {
    if (!c.class.desiredAssertionStatus());
    for (int i = 1; ; i = 0)
    {
      a = i;
      return;
    }
  }

  public static byte[] a(String paramString)
  {
    return a(paramString.getBytes(), 0);
  }

  public static byte[] a(byte[] paramArrayOfByte, int paramInt)
  {
    return a(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }

  public static byte[] a(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    b localb = new b(paramInt3, new byte[paramInt2 * 3 / 4]);
    if (!localb.a(paramArrayOfByte, paramInt1, paramInt2, true))
      throw new IllegalArgumentException("bad base-64");
    byte[] arrayOfByte;
    if (localb.b == localb.a.length)
      arrayOfByte = localb.a;
    while (true)
    {
      return arrayOfByte;
      arrayOfByte = new byte[localb.b];
      System.arraycopy(localb.a, 0, arrayOfByte, 0, localb.b);
    }
  }

  public static String b(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      String str = new String(c(paramArrayOfByte, paramInt), "US-ASCII");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
  }

  public static byte[] b(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    c localc = new c(paramInt3, null);
    int i = 4 * (paramInt2 / 3);
    label35: int j;
    if (localc.d)
    {
      if (paramInt2 % 3 > 0)
        i += 4;
      if ((localc.e) && (paramInt2 > 0))
      {
        j = 1 + (paramInt2 - 1) / 57;
        if (!localc.f)
          break label167;
      }
    }
    for (int k = 2; ; k = 1)
    {
      i += k * j;
      localc.a = new byte[i];
      localc.a(paramArrayOfByte, paramInt1, paramInt2, true);
      if ((a) || (localc.b == i))
        break;
      throw new AssertionError();
      switch (paramInt2 % 3)
      {
      case 0:
      default:
        break;
      case 1:
        i += 2;
        break;
      case 2:
      }
      i += 3;
      label167: break label35:
    }
    return localc.a;
  }

  public static byte[] c(byte[] paramArrayOfByte, int paramInt)
  {
    return b(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }

  public static abstract class a
  {
    public byte[] a;
    public int b;
  }

  public static class b extends c.a
  {
    private static final int[] c;
    private static final int[] d;
    private int e;
    private int f;
    private final int[] g;

    static
    {
      int[] arrayOfInt1 = new int[256];
      arrayOfInt1[0] = -1;
      arrayOfInt1[1] = -1;
      arrayOfInt1[2] = -1;
      arrayOfInt1[3] = -1;
      arrayOfInt1[4] = -1;
      arrayOfInt1[5] = -1;
      arrayOfInt1[6] = -1;
      arrayOfInt1[7] = -1;
      arrayOfInt1[8] = -1;
      arrayOfInt1[9] = -1;
      arrayOfInt1[10] = -1;
      arrayOfInt1[11] = -1;
      arrayOfInt1[12] = -1;
      arrayOfInt1[13] = -1;
      arrayOfInt1[14] = -1;
      arrayOfInt1[15] = -1;
      arrayOfInt1[16] = -1;
      arrayOfInt1[17] = -1;
      arrayOfInt1[18] = -1;
      arrayOfInt1[19] = -1;
      arrayOfInt1[20] = -1;
      arrayOfInt1[21] = -1;
      arrayOfInt1[22] = -1;
      arrayOfInt1[23] = -1;
      arrayOfInt1[24] = -1;
      arrayOfInt1[25] = -1;
      arrayOfInt1[26] = -1;
      arrayOfInt1[27] = -1;
      arrayOfInt1[28] = -1;
      arrayOfInt1[29] = -1;
      arrayOfInt1[30] = -1;
      arrayOfInt1[31] = -1;
      arrayOfInt1[32] = -1;
      arrayOfInt1[33] = -1;
      arrayOfInt1[34] = -1;
      arrayOfInt1[35] = -1;
      arrayOfInt1[36] = -1;
      arrayOfInt1[37] = -1;
      arrayOfInt1[38] = -1;
      arrayOfInt1[39] = -1;
      arrayOfInt1[40] = -1;
      arrayOfInt1[41] = -1;
      arrayOfInt1[42] = -1;
      arrayOfInt1[43] = 62;
      arrayOfInt1[44] = -1;
      arrayOfInt1[45] = -1;
      arrayOfInt1[46] = -1;
      arrayOfInt1[47] = 63;
      arrayOfInt1[48] = 52;
      arrayOfInt1[49] = 53;
      arrayOfInt1[50] = 54;
      arrayOfInt1[51] = 55;
      arrayOfInt1[52] = 56;
      arrayOfInt1[53] = 57;
      arrayOfInt1[54] = 58;
      arrayOfInt1[55] = 59;
      arrayOfInt1[56] = 60;
      arrayOfInt1[57] = 61;
      arrayOfInt1[58] = -1;
      arrayOfInt1[59] = -1;
      arrayOfInt1[60] = -1;
      arrayOfInt1[61] = -2;
      arrayOfInt1[62] = -1;
      arrayOfInt1[63] = -1;
      arrayOfInt1[64] = -1;
      arrayOfInt1[65] = 0;
      arrayOfInt1[66] = 1;
      arrayOfInt1[67] = 2;
      arrayOfInt1[68] = 3;
      arrayOfInt1[69] = 4;
      arrayOfInt1[70] = 5;
      arrayOfInt1[71] = 6;
      arrayOfInt1[72] = 7;
      arrayOfInt1[73] = 8;
      arrayOfInt1[74] = 9;
      arrayOfInt1[75] = 10;
      arrayOfInt1[76] = 11;
      arrayOfInt1[77] = 12;
      arrayOfInt1[78] = 13;
      arrayOfInt1[79] = 14;
      arrayOfInt1[80] = 15;
      arrayOfInt1[81] = 16;
      arrayOfInt1[82] = 17;
      arrayOfInt1[83] = 18;
      arrayOfInt1[84] = 19;
      arrayOfInt1[85] = 20;
      arrayOfInt1[86] = 21;
      arrayOfInt1[87] = 22;
      arrayOfInt1[88] = 23;
      arrayOfInt1[89] = 24;
      arrayOfInt1[90] = 25;
      arrayOfInt1[91] = -1;
      arrayOfInt1[92] = -1;
      arrayOfInt1[93] = -1;
      arrayOfInt1[94] = -1;
      arrayOfInt1[95] = -1;
      arrayOfInt1[96] = -1;
      arrayOfInt1[97] = 26;
      arrayOfInt1[98] = 27;
      arrayOfInt1[99] = 28;
      arrayOfInt1[100] = 29;
      arrayOfInt1[101] = 30;
      arrayOfInt1[102] = 31;
      arrayOfInt1[103] = 32;
      arrayOfInt1[104] = 33;
      arrayOfInt1[105] = 34;
      arrayOfInt1[106] = 35;
      arrayOfInt1[107] = 36;
      arrayOfInt1[108] = 37;
      arrayOfInt1[109] = 38;
      arrayOfInt1[110] = 39;
      arrayOfInt1[111] = 40;
      arrayOfInt1[112] = 41;
      arrayOfInt1[113] = 42;
      arrayOfInt1[114] = 43;
      arrayOfInt1[115] = 44;
      arrayOfInt1[116] = 45;
      arrayOfInt1[117] = 46;
      arrayOfInt1[118] = 47;
      arrayOfInt1[119] = 48;
      arrayOfInt1[120] = 49;
      arrayOfInt1[121] = 50;
      arrayOfInt1[122] = 51;
      arrayOfInt1[123] = -1;
      arrayOfInt1[124] = -1;
      arrayOfInt1[125] = -1;
      arrayOfInt1[126] = -1;
      arrayOfInt1[127] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[''] = -1;
      arrayOfInt1[' '] = -1;
      arrayOfInt1['¡'] = -1;
      arrayOfInt1['¢'] = -1;
      arrayOfInt1['£'] = -1;
      arrayOfInt1['¤'] = -1;
      arrayOfInt1['¥'] = -1;
      arrayOfInt1['¦'] = -1;
      arrayOfInt1['§'] = -1;
      arrayOfInt1['¨'] = -1;
      arrayOfInt1['©'] = -1;
      arrayOfInt1['ª'] = -1;
      arrayOfInt1['«'] = -1;
      arrayOfInt1['¬'] = -1;
      arrayOfInt1['­'] = -1;
      arrayOfInt1['®'] = -1;
      arrayOfInt1['¯'] = -1;
      arrayOfInt1['°'] = -1;
      arrayOfInt1['±'] = -1;
      arrayOfInt1['²'] = -1;
      arrayOfInt1['³'] = -1;
      arrayOfInt1['´'] = -1;
      arrayOfInt1['µ'] = -1;
      arrayOfInt1['¶'] = -1;
      arrayOfInt1['·'] = -1;
      arrayOfInt1['¸'] = -1;
      arrayOfInt1['¹'] = -1;
      arrayOfInt1['º'] = -1;
      arrayOfInt1['»'] = -1;
      arrayOfInt1['¼'] = -1;
      arrayOfInt1['½'] = -1;
      arrayOfInt1['¾'] = -1;
      arrayOfInt1['¿'] = -1;
      arrayOfInt1['À'] = -1;
      arrayOfInt1['Á'] = -1;
      arrayOfInt1['Â'] = -1;
      arrayOfInt1['Ã'] = -1;
      arrayOfInt1['Ä'] = -1;
      arrayOfInt1['Å'] = -1;
      arrayOfInt1['Æ'] = -1;
      arrayOfInt1['Ç'] = -1;
      arrayOfInt1['È'] = -1;
      arrayOfInt1['É'] = -1;
      arrayOfInt1['Ê'] = -1;
      arrayOfInt1['Ë'] = -1;
      arrayOfInt1['Ì'] = -1;
      arrayOfInt1['Í'] = -1;
      arrayOfInt1['Î'] = -1;
      arrayOfInt1['Ï'] = -1;
      arrayOfInt1['Ð'] = -1;
      arrayOfInt1['Ñ'] = -1;
      arrayOfInt1['Ò'] = -1;
      arrayOfInt1['Ó'] = -1;
      arrayOfInt1['Ô'] = -1;
      arrayOfInt1['Õ'] = -1;
      arrayOfInt1['Ö'] = -1;
      arrayOfInt1['×'] = -1;
      arrayOfInt1['Ø'] = -1;
      arrayOfInt1['Ù'] = -1;
      arrayOfInt1['Ú'] = -1;
      arrayOfInt1['Û'] = -1;
      arrayOfInt1['Ü'] = -1;
      arrayOfInt1['Ý'] = -1;
      arrayOfInt1['Þ'] = -1;
      arrayOfInt1['ß'] = -1;
      arrayOfInt1['à'] = -1;
      arrayOfInt1['á'] = -1;
      arrayOfInt1['â'] = -1;
      arrayOfInt1['ã'] = -1;
      arrayOfInt1['ä'] = -1;
      arrayOfInt1['å'] = -1;
      arrayOfInt1['æ'] = -1;
      arrayOfInt1['ç'] = -1;
      arrayOfInt1['è'] = -1;
      arrayOfInt1['é'] = -1;
      arrayOfInt1['ê'] = -1;
      arrayOfInt1['ë'] = -1;
      arrayOfInt1['ì'] = -1;
      arrayOfInt1['í'] = -1;
      arrayOfInt1['î'] = -1;
      arrayOfInt1['ï'] = -1;
      arrayOfInt1['ð'] = -1;
      arrayOfInt1['ñ'] = -1;
      arrayOfInt1['ò'] = -1;
      arrayOfInt1['ó'] = -1;
      arrayOfInt1['ô'] = -1;
      arrayOfInt1['õ'] = -1;
      arrayOfInt1['ö'] = -1;
      arrayOfInt1['÷'] = -1;
      arrayOfInt1['ø'] = -1;
      arrayOfInt1['ù'] = -1;
      arrayOfInt1['ú'] = -1;
      arrayOfInt1['û'] = -1;
      arrayOfInt1['ü'] = -1;
      arrayOfInt1['ý'] = -1;
      arrayOfInt1['þ'] = -1;
      arrayOfInt1['ÿ'] = -1;
      c = arrayOfInt1;
      int[] arrayOfInt2 = new int[256];
      arrayOfInt2[0] = -1;
      arrayOfInt2[1] = -1;
      arrayOfInt2[2] = -1;
      arrayOfInt2[3] = -1;
      arrayOfInt2[4] = -1;
      arrayOfInt2[5] = -1;
      arrayOfInt2[6] = -1;
      arrayOfInt2[7] = -1;
      arrayOfInt2[8] = -1;
      arrayOfInt2[9] = -1;
      arrayOfInt2[10] = -1;
      arrayOfInt2[11] = -1;
      arrayOfInt2[12] = -1;
      arrayOfInt2[13] = -1;
      arrayOfInt2[14] = -1;
      arrayOfInt2[15] = -1;
      arrayOfInt2[16] = -1;
      arrayOfInt2[17] = -1;
      arrayOfInt2[18] = -1;
      arrayOfInt2[19] = -1;
      arrayOfInt2[20] = -1;
      arrayOfInt2[21] = -1;
      arrayOfInt2[22] = -1;
      arrayOfInt2[23] = -1;
      arrayOfInt2[24] = -1;
      arrayOfInt2[25] = -1;
      arrayOfInt2[26] = -1;
      arrayOfInt2[27] = -1;
      arrayOfInt2[28] = -1;
      arrayOfInt2[29] = -1;
      arrayOfInt2[30] = -1;
      arrayOfInt2[31] = -1;
      arrayOfInt2[32] = -1;
      arrayOfInt2[33] = -1;
      arrayOfInt2[34] = -1;
      arrayOfInt2[35] = -1;
      arrayOfInt2[36] = -1;
      arrayOfInt2[37] = -1;
      arrayOfInt2[38] = -1;
      arrayOfInt2[39] = -1;
      arrayOfInt2[40] = -1;
      arrayOfInt2[41] = -1;
      arrayOfInt2[42] = -1;
      arrayOfInt2[43] = -1;
      arrayOfInt2[44] = -1;
      arrayOfInt2[45] = 62;
      arrayOfInt2[46] = -1;
      arrayOfInt2[47] = -1;
      arrayOfInt2[48] = 52;
      arrayOfInt2[49] = 53;
      arrayOfInt2[50] = 54;
      arrayOfInt2[51] = 55;
      arrayOfInt2[52] = 56;
      arrayOfInt2[53] = 57;
      arrayOfInt2[54] = 58;
      arrayOfInt2[55] = 59;
      arrayOfInt2[56] = 60;
      arrayOfInt2[57] = 61;
      arrayOfInt2[58] = -1;
      arrayOfInt2[59] = -1;
      arrayOfInt2[60] = -1;
      arrayOfInt2[61] = -2;
      arrayOfInt2[62] = -1;
      arrayOfInt2[63] = -1;
      arrayOfInt2[64] = -1;
      arrayOfInt2[65] = 0;
      arrayOfInt2[66] = 1;
      arrayOfInt2[67] = 2;
      arrayOfInt2[68] = 3;
      arrayOfInt2[69] = 4;
      arrayOfInt2[70] = 5;
      arrayOfInt2[71] = 6;
      arrayOfInt2[72] = 7;
      arrayOfInt2[73] = 8;
      arrayOfInt2[74] = 9;
      arrayOfInt2[75] = 10;
      arrayOfInt2[76] = 11;
      arrayOfInt2[77] = 12;
      arrayOfInt2[78] = 13;
      arrayOfInt2[79] = 14;
      arrayOfInt2[80] = 15;
      arrayOfInt2[81] = 16;
      arrayOfInt2[82] = 17;
      arrayOfInt2[83] = 18;
      arrayOfInt2[84] = 19;
      arrayOfInt2[85] = 20;
      arrayOfInt2[86] = 21;
      arrayOfInt2[87] = 22;
      arrayOfInt2[88] = 23;
      arrayOfInt2[89] = 24;
      arrayOfInt2[90] = 25;
      arrayOfInt2[91] = -1;
      arrayOfInt2[92] = -1;
      arrayOfInt2[93] = -1;
      arrayOfInt2[94] = -1;
      arrayOfInt2[95] = 63;
      arrayOfInt2[96] = -1;
      arrayOfInt2[97] = 26;
      arrayOfInt2[98] = 27;
      arrayOfInt2[99] = 28;
      arrayOfInt2[100] = 29;
      arrayOfInt2[101] = 30;
      arrayOfInt2[102] = 31;
      arrayOfInt2[103] = 32;
      arrayOfInt2[104] = 33;
      arrayOfInt2[105] = 34;
      arrayOfInt2[106] = 35;
      arrayOfInt2[107] = 36;
      arrayOfInt2[108] = 37;
      arrayOfInt2[109] = 38;
      arrayOfInt2[110] = 39;
      arrayOfInt2[111] = 40;
      arrayOfInt2[112] = 41;
      arrayOfInt2[113] = 42;
      arrayOfInt2[114] = 43;
      arrayOfInt2[115] = 44;
      arrayOfInt2[116] = 45;
      arrayOfInt2[117] = 46;
      arrayOfInt2[118] = 47;
      arrayOfInt2[119] = 48;
      arrayOfInt2[120] = 49;
      arrayOfInt2[121] = 50;
      arrayOfInt2[122] = 51;
      arrayOfInt2[123] = -1;
      arrayOfInt2[124] = -1;
      arrayOfInt2[125] = -1;
      arrayOfInt2[126] = -1;
      arrayOfInt2[127] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[''] = -1;
      arrayOfInt2[' '] = -1;
      arrayOfInt2['¡'] = -1;
      arrayOfInt2['¢'] = -1;
      arrayOfInt2['£'] = -1;
      arrayOfInt2['¤'] = -1;
      arrayOfInt2['¥'] = -1;
      arrayOfInt2['¦'] = -1;
      arrayOfInt2['§'] = -1;
      arrayOfInt2['¨'] = -1;
      arrayOfInt2['©'] = -1;
      arrayOfInt2['ª'] = -1;
      arrayOfInt2['«'] = -1;
      arrayOfInt2['¬'] = -1;
      arrayOfInt2['­'] = -1;
      arrayOfInt2['®'] = -1;
      arrayOfInt2['¯'] = -1;
      arrayOfInt2['°'] = -1;
      arrayOfInt2['±'] = -1;
      arrayOfInt2['²'] = -1;
      arrayOfInt2['³'] = -1;
      arrayOfInt2['´'] = -1;
      arrayOfInt2['µ'] = -1;
      arrayOfInt2['¶'] = -1;
      arrayOfInt2['·'] = -1;
      arrayOfInt2['¸'] = -1;
      arrayOfInt2['¹'] = -1;
      arrayOfInt2['º'] = -1;
      arrayOfInt2['»'] = -1;
      arrayOfInt2['¼'] = -1;
      arrayOfInt2['½'] = -1;
      arrayOfInt2['¾'] = -1;
      arrayOfInt2['¿'] = -1;
      arrayOfInt2['À'] = -1;
      arrayOfInt2['Á'] = -1;
      arrayOfInt2['Â'] = -1;
      arrayOfInt2['Ã'] = -1;
      arrayOfInt2['Ä'] = -1;
      arrayOfInt2['Å'] = -1;
      arrayOfInt2['Æ'] = -1;
      arrayOfInt2['Ç'] = -1;
      arrayOfInt2['È'] = -1;
      arrayOfInt2['É'] = -1;
      arrayOfInt2['Ê'] = -1;
      arrayOfInt2['Ë'] = -1;
      arrayOfInt2['Ì'] = -1;
      arrayOfInt2['Í'] = -1;
      arrayOfInt2['Î'] = -1;
      arrayOfInt2['Ï'] = -1;
      arrayOfInt2['Ð'] = -1;
      arrayOfInt2['Ñ'] = -1;
      arrayOfInt2['Ò'] = -1;
      arrayOfInt2['Ó'] = -1;
      arrayOfInt2['Ô'] = -1;
      arrayOfInt2['Õ'] = -1;
      arrayOfInt2['Ö'] = -1;
      arrayOfInt2['×'] = -1;
      arrayOfInt2['Ø'] = -1;
      arrayOfInt2['Ù'] = -1;
      arrayOfInt2['Ú'] = -1;
      arrayOfInt2['Û'] = -1;
      arrayOfInt2['Ü'] = -1;
      arrayOfInt2['Ý'] = -1;
      arrayOfInt2['Þ'] = -1;
      arrayOfInt2['ß'] = -1;
      arrayOfInt2['à'] = -1;
      arrayOfInt2['á'] = -1;
      arrayOfInt2['â'] = -1;
      arrayOfInt2['ã'] = -1;
      arrayOfInt2['ä'] = -1;
      arrayOfInt2['å'] = -1;
      arrayOfInt2['æ'] = -1;
      arrayOfInt2['ç'] = -1;
      arrayOfInt2['è'] = -1;
      arrayOfInt2['é'] = -1;
      arrayOfInt2['ê'] = -1;
      arrayOfInt2['ë'] = -1;
      arrayOfInt2['ì'] = -1;
      arrayOfInt2['í'] = -1;
      arrayOfInt2['î'] = -1;
      arrayOfInt2['ï'] = -1;
      arrayOfInt2['ð'] = -1;
      arrayOfInt2['ñ'] = -1;
      arrayOfInt2['ò'] = -1;
      arrayOfInt2['ó'] = -1;
      arrayOfInt2['ô'] = -1;
      arrayOfInt2['õ'] = -1;
      arrayOfInt2['ö'] = -1;
      arrayOfInt2['÷'] = -1;
      arrayOfInt2['ø'] = -1;
      arrayOfInt2['ù'] = -1;
      arrayOfInt2['ú'] = -1;
      arrayOfInt2['û'] = -1;
      arrayOfInt2['ü'] = -1;
      arrayOfInt2['ý'] = -1;
      arrayOfInt2['þ'] = -1;
      arrayOfInt2['ÿ'] = -1;
      d = arrayOfInt2;
    }

    public b(int paramInt, byte[] paramArrayOfByte)
    {
      this.a = paramArrayOfByte;
      if ((paramInt & 0x8) == 0);
      for (int[] arrayOfInt = c; ; arrayOfInt = d)
      {
        this.g = arrayOfInt;
        this.e = 0;
        this.f = 0;
        return;
      }
    }

    public boolean a(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int i3;
      if (this.e == 6)
      {
        i3 = 0;
        label12: return i3;
      }
      int i = paramInt2 + paramInt1;
      int j = this.e;
      int k = this.f;
      int l = 0;
      byte[] arrayOfByte = this.a;
      int[] arrayOfInt = this.g;
      int i1 = paramInt1;
      if (i1 < i)
        if (j == 0)
        {
          while (i1 + 4 <= i)
          {
            label50: k = arrayOfInt[(0xFF & paramArrayOfByte[i1])] << 18 | arrayOfInt[(0xFF & paramArrayOfByte[(i1 + 1)])] << 12 | arrayOfInt[(0xFF & paramArrayOfByte[(i1 + 2)])] << 6 | arrayOfInt[(0xFF & paramArrayOfByte[(i1 + 3)])];
            if (k < 0)
              break;
            arrayOfByte[(l + 2)] = (byte)k;
            arrayOfByte[(l + 1)] = (byte)(k >> 8);
            arrayOfByte[l] = (byte)(k >> 16);
            l += 3;
            i1 += 4;
          }
          if (i1 < i);
        }
      for (int i2 = k; ; i2 = k)
      {
        if (!paramBoolean)
        {
          this.e = j;
          this.f = i2;
          this.b = l;
          i3 = 1;
          break label12:
          int i6 = i1 + 1;
          int i7 = arrayOfInt[(0xFF & paramArrayOfByte[i1])];
          switch (j)
          {
          default:
          case 0:
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          }
          do
          {
            do
              for (int i8 = j; ; i8 = j + 1)
              {
                while (true)
                {
                  j = i8;
                  i1 = i6;
                  break label50:
                  if (i7 >= 0)
                  {
                    i8 = j + 1;
                    k = i7;
                  }
                  if (i7 != -1);
                  this.e = 6;
                  i3 = 0;
                  break label12:
                  if (i7 >= 0)
                  {
                    k = i7 | k << 6;
                    i8 = j + 1;
                  }
                  if (i7 != -1);
                  this.e = 6;
                  i3 = 0;
                  break label12:
                  if (i7 >= 0)
                  {
                    k = i7 | k << 6;
                    i8 = j + 1;
                  }
                  if (i7 == -2)
                  {
                    int i9 = l + 1;
                    arrayOfByte[l] = (byte)(k >> 4);
                    i8 = 4;
                    l = i9;
                  }
                  if (i7 != -1);
                  this.e = 6;
                  i3 = 0;
                  break label12:
                  if (i7 >= 0)
                  {
                    k = i7 | k << 6;
                    arrayOfByte[(l + 2)] = (byte)k;
                    arrayOfByte[(l + 1)] = (byte)(k >> 8);
                    arrayOfByte[l] = (byte)(k >> 16);
                    l += 3;
                    i8 = 0;
                  }
                  if (i7 != -2)
                    break;
                  arrayOfByte[(l + 1)] = (byte)(k >> 2);
                  arrayOfByte[l] = (byte)(k >> 10);
                  l += 2;
                  i8 = 5;
                }
                if (i7 != -1);
                this.e = 6;
                i3 = 0;
                break label12:
                if (i7 != -2)
                  break;
              }
            while (i7 == -1);
            this.e = 6;
            i3 = 0;
            break label12:
          }
          while (i7 == -1);
          this.e = 6;
          i3 = 0;
        }
        switch (j)
        {
        case 0:
        default:
        case 1:
        case 2:
        case 3:
          while (true)
          {
            this.e = j;
            this.b = l;
            i3 = 1;
            break label12:
            this.e = 6;
            i3 = 0;
            break label12:
            int i5 = l + 1;
            arrayOfByte[l] = (byte)(i2 >> 4);
            l = i5;
            continue;
            int i4 = l + 1;
            arrayOfByte[l] = (byte)(i2 >> 10);
            l = i4 + 1;
            arrayOfByte[i4] = (byte)(i2 >> 2);
          }
        case 4:
        }
        this.e = 6;
        i3 = 0;
        break label12:
      }
    }
  }

  public static class c extends c.a
  {
    private static final byte[] h;
    private static final byte[] i;
    public int c;
    public final boolean d;
    public final boolean e;
    public final boolean f;
    private final byte[] j;
    private int k;
    private final byte[] l;

    static
    {
      if (!c.class.desiredAssertionStatus());
      for (int i1 = 1; ; i1 = 0)
      {
        g = i1;
        byte[] arrayOfByte1 = new byte[64];
        arrayOfByte1[0] = 65;
        arrayOfByte1[1] = 66;
        arrayOfByte1[2] = 67;
        arrayOfByte1[3] = 68;
        arrayOfByte1[4] = 69;
        arrayOfByte1[5] = 70;
        arrayOfByte1[6] = 71;
        arrayOfByte1[7] = 72;
        arrayOfByte1[8] = 73;
        arrayOfByte1[9] = 74;
        arrayOfByte1[10] = 75;
        arrayOfByte1[11] = 76;
        arrayOfByte1[12] = 77;
        arrayOfByte1[13] = 78;
        arrayOfByte1[14] = 79;
        arrayOfByte1[15] = 80;
        arrayOfByte1[16] = 81;
        arrayOfByte1[17] = 82;
        arrayOfByte1[18] = 83;
        arrayOfByte1[19] = 84;
        arrayOfByte1[20] = 85;
        arrayOfByte1[21] = 86;
        arrayOfByte1[22] = 87;
        arrayOfByte1[23] = 88;
        arrayOfByte1[24] = 89;
        arrayOfByte1[25] = 90;
        arrayOfByte1[26] = 97;
        arrayOfByte1[27] = 98;
        arrayOfByte1[28] = 99;
        arrayOfByte1[29] = 100;
        arrayOfByte1[30] = 101;
        arrayOfByte1[31] = 102;
        arrayOfByte1[32] = 103;
        arrayOfByte1[33] = 104;
        arrayOfByte1[34] = 105;
        arrayOfByte1[35] = 106;
        arrayOfByte1[36] = 107;
        arrayOfByte1[37] = 108;
        arrayOfByte1[38] = 109;
        arrayOfByte1[39] = 110;
        arrayOfByte1[40] = 111;
        arrayOfByte1[41] = 112;
        arrayOfByte1[42] = 113;
        arrayOfByte1[43] = 114;
        arrayOfByte1[44] = 115;
        arrayOfByte1[45] = 116;
        arrayOfByte1[46] = 117;
        arrayOfByte1[47] = 118;
        arrayOfByte1[48] = 119;
        arrayOfByte1[49] = 120;
        arrayOfByte1[50] = 121;
        arrayOfByte1[51] = 122;
        arrayOfByte1[52] = 48;
        arrayOfByte1[53] = 49;
        arrayOfByte1[54] = 50;
        arrayOfByte1[55] = 51;
        arrayOfByte1[56] = 52;
        arrayOfByte1[57] = 53;
        arrayOfByte1[58] = 54;
        arrayOfByte1[59] = 55;
        arrayOfByte1[60] = 56;
        arrayOfByte1[61] = 57;
        arrayOfByte1[62] = 43;
        arrayOfByte1[63] = 47;
        h = arrayOfByte1;
        byte[] arrayOfByte2 = new byte[64];
        arrayOfByte2[0] = 65;
        arrayOfByte2[1] = 66;
        arrayOfByte2[2] = 67;
        arrayOfByte2[3] = 68;
        arrayOfByte2[4] = 69;
        arrayOfByte2[5] = 70;
        arrayOfByte2[6] = 71;
        arrayOfByte2[7] = 72;
        arrayOfByte2[8] = 73;
        arrayOfByte2[9] = 74;
        arrayOfByte2[10] = 75;
        arrayOfByte2[11] = 76;
        arrayOfByte2[12] = 77;
        arrayOfByte2[13] = 78;
        arrayOfByte2[14] = 79;
        arrayOfByte2[15] = 80;
        arrayOfByte2[16] = 81;
        arrayOfByte2[17] = 82;
        arrayOfByte2[18] = 83;
        arrayOfByte2[19] = 84;
        arrayOfByte2[20] = 85;
        arrayOfByte2[21] = 86;
        arrayOfByte2[22] = 87;
        arrayOfByte2[23] = 88;
        arrayOfByte2[24] = 89;
        arrayOfByte2[25] = 90;
        arrayOfByte2[26] = 97;
        arrayOfByte2[27] = 98;
        arrayOfByte2[28] = 99;
        arrayOfByte2[29] = 100;
        arrayOfByte2[30] = 101;
        arrayOfByte2[31] = 102;
        arrayOfByte2[32] = 103;
        arrayOfByte2[33] = 104;
        arrayOfByte2[34] = 105;
        arrayOfByte2[35] = 106;
        arrayOfByte2[36] = 107;
        arrayOfByte2[37] = 108;
        arrayOfByte2[38] = 109;
        arrayOfByte2[39] = 110;
        arrayOfByte2[40] = 111;
        arrayOfByte2[41] = 112;
        arrayOfByte2[42] = 113;
        arrayOfByte2[43] = 114;
        arrayOfByte2[44] = 115;
        arrayOfByte2[45] = 116;
        arrayOfByte2[46] = 117;
        arrayOfByte2[47] = 118;
        arrayOfByte2[48] = 119;
        arrayOfByte2[49] = 120;
        arrayOfByte2[50] = 121;
        arrayOfByte2[51] = 122;
        arrayOfByte2[52] = 48;
        arrayOfByte2[53] = 49;
        arrayOfByte2[54] = 50;
        arrayOfByte2[55] = 51;
        arrayOfByte2[56] = 52;
        arrayOfByte2[57] = 53;
        arrayOfByte2[58] = 54;
        arrayOfByte2[59] = 55;
        arrayOfByte2[60] = 56;
        arrayOfByte2[61] = 57;
        arrayOfByte2[62] = 45;
        arrayOfByte2[63] = 95;
        i = arrayOfByte2;
        return;
      }
    }

    public c(int paramInt, byte[] paramArrayOfByte)
    {
      this.a = paramArrayOfByte;
      int i2;
      label20: int i3;
      label35: label47: byte[] arrayOfByte;
      if ((paramInt & 0x1) == 0)
      {
        i2 = i1;
        this.d = i2;
        if ((paramInt & 0x2) != 0)
          break label106;
        i3 = i1;
        this.e = i3;
        if ((paramInt & 0x4) == 0)
          break label112;
        this.f = i1;
        if ((paramInt & 0x8) != 0)
          break label117;
        arrayOfByte = h;
        label64: this.l = arrayOfByte;
        this.j = new byte[2];
        this.c = 0;
        if (!this.e)
          break label125;
      }
      for (int i4 = 19; ; i4 = -1)
      {
        this.k = i4;
        return;
        i2 = 0;
        break label20:
        label106: i3 = 0;
        break label35:
        label112: i1 = 0;
        break label47:
        label117: arrayOfByte = i;
        label125: break label64:
      }
    }

    public boolean a(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      byte[] arrayOfByte1 = this.l;
      byte[] arrayOfByte2 = this.a;
      int i1 = 0;
      int i2 = this.k;
      int i3 = paramInt2 + paramInt1;
      int i4 = -1;
      int i6;
      label63: int i40;
      switch (this.c)
      {
      default:
        i6 = paramInt1;
        if (i4 == -1)
          break label1238;
        arrayOfByte2[i1] = arrayOfByte1[(0x3F & i4 >> 18)];
        arrayOfByte2[1] = arrayOfByte1[(0x3F & i4 >> 12)];
        arrayOfByte2[2] = arrayOfByte1[(0x3F & i4 >> 6)];
        i1 = 4;
        arrayOfByte2[3] = arrayOfByte1[(i4 & 0x3F)];
        if (--i2 != 0)
          break label1238;
        if (!this.f)
          break label1249;
        i40 = 5;
        arrayOfByte2[i1] = 13;
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        int i41 = i40 + 1;
        arrayOfByte2[i40] = 10;
        int i7 = 19;
        for (int i8 = i41; ; i8 = i1)
        {
          label177: int i38;
          if (i6 + 3 <= i3)
          {
            int i37 = (0xFF & paramArrayOfByte[i6]) << 16 | (0xFF & paramArrayOfByte[(i6 + 1)]) << 8 | 0xFF & paramArrayOfByte[(i6 + 2)];
            arrayOfByte2[i8] = arrayOfByte1[(0x3F & i37 >> 18)];
            arrayOfByte2[(i8 + 1)] = arrayOfByte1[(0x3F & i37 >> 12)];
            arrayOfByte2[(i8 + 2)] = arrayOfByte1[(0x3F & i37 >> 6)];
            arrayOfByte2[(i8 + 3)] = arrayOfByte1[(i37 & 0x3F)];
            i6 += 3;
            i1 = i8 + 4;
            i2 = i7 - 1;
            if (i2 != 0)
              break label1238;
            if (!this.f)
              break label1231;
            i38 = i1 + 1;
            arrayOfByte2[i1] = 13;
          }
          while (true)
          {
            int i39 = i38 + 1;
            arrayOfByte2[i38] = 10;
            i7 = 19;
            i8 = i39;
            break label177:
            i6 = paramInt1;
            break label63:
            if (paramInt1 + 2 <= i3);
            int i42 = (0xFF & this.j[0]) << 16;
            int i43 = paramInt1 + 1;
            int i44 = i42 | (0xFF & paramArrayOfByte[paramInt1]) << 8;
            int i45 = i43 + 1;
            i4 = i44 | 0xFF & paramArrayOfByte[i43];
            this.c = 0;
            i6 = i45;
            break label63:
            if (paramInt1 + 1 <= i3);
            int i5 = (0xFF & this.j[0]) << 16 | (0xFF & this.j[1]) << 8;
            i6 = paramInt1 + 1;
            i4 = i5 | 0xFF & paramArrayOfByte[paramInt1];
            this.c = 0;
            break label63:
            label693: int i15;
            int i14;
            label773: int i18;
            label814: int i22;
            int i23;
            if (paramBoolean)
            {
              if (i6 - this.c == i3 - 1)
              {
                int i30;
                int i28;
                int i29;
                if (this.c > 0)
                {
                  byte[] arrayOfByte8 = this.j;
                  i30 = 1;
                  i28 = arrayOfByte8[0];
                  i29 = i6;
                }
                while (true)
                {
                  int i31 = (i28 & 0xFF) << 4;
                  this.c -= i30;
                  int i32 = i8 + 1;
                  arrayOfByte2[i8] = arrayOfByte1[(0x3F & i31 >> 6)];
                  int i33 = i32 + 1;
                  arrayOfByte2[i32] = arrayOfByte1[(i31 & 0x3F)];
                  if (this.d)
                  {
                    int i36 = i33 + 1;
                    arrayOfByte2[i33] = 61;
                    i33 = i36 + 1;
                    arrayOfByte2[i36] = 61;
                  }
                  if (this.e)
                  {
                    if (this.f)
                    {
                      int i35 = i33 + 1;
                      arrayOfByte2[i33] = 13;
                      i33 = i35;
                    }
                    int i34 = i33 + 1;
                    arrayOfByte2[i33] = 10;
                    i33 = i34;
                  }
                  i6 = i29;
                  i8 = i33;
                  if ((g) || (this.c == 0))
                    break label1072;
                  throw new AssertionError();
                  int i27 = i6 + 1;
                  i28 = paramArrayOfByte[i6];
                  i29 = i27;
                  i30 = 0;
                }
              }
              if (i6 - this.c == i3 - 2)
                if (this.c > 1)
                {
                  byte[] arrayOfByte7 = this.j;
                  i15 = 1;
                  i14 = arrayOfByte7[0];
                  int i16 = (i14 & 0xFF) << 10;
                  if (this.c <= 0)
                    break label998;
                  byte[] arrayOfByte6 = this.j;
                  int i26 = i15 + 1;
                  i18 = arrayOfByte6[i15];
                  i15 = i26;
                  int i19 = i16 | (i18 & 0xFF) << 2;
                  this.c -= i15;
                  int i20 = i8 + 1;
                  arrayOfByte2[i8] = arrayOfByte1[(0x3F & i19 >> 12)];
                  int i21 = i20 + 1;
                  arrayOfByte2[i20] = arrayOfByte1[(0x3F & i19 >> 6)];
                  i22 = i21 + 1;
                  arrayOfByte2[i21] = arrayOfByte1[(i19 & 0x3F)];
                  if (!this.d)
                    break label1224;
                  i23 = i22 + 1;
                  arrayOfByte2[i22] = 61;
                }
            }
            while (true)
            {
              if (this.e)
              {
                if (this.f)
                {
                  int i25 = i23 + 1;
                  arrayOfByte2[i23] = 13;
                  i23 = i25;
                }
                int i24 = i23 + 1;
                arrayOfByte2[i23] = 10;
                i23 = i24;
              }
              i8 = i23;
              break label693:
              int i13 = i6 + 1;
              i14 = paramArrayOfByte[i6];
              i6 = i13;
              i15 = 0;
              break label773:
              label998: int i17 = i6 + 1;
              i18 = paramArrayOfByte[i6];
              i6 = i17;
              break label814:
              if ((this.e) && (i8 > 0) && (i7 != 19));
              int i12;
              if (this.f)
              {
                i12 = i8 + 1;
                arrayOfByte2[i8] = 13;
              }
              while (true)
              {
                i8 = i12 + 1;
                arrayOfByte2[i12] = 10;
                break label693:
                if ((!g) && (i6 != i3))
                {
                  label1072: throw new AssertionError();
                  if (i6 != i3 - 1)
                    break label1145;
                  byte[] arrayOfByte5 = this.j;
                  int i11 = this.c;
                  this.c = (i11 + 1);
                  arrayOfByte5[i11] = paramArrayOfByte[i6];
                }
                while (true)
                {
                  this.b = i8;
                  this.k = i7;
                  return true;
                  label1145: if (i6 != i3 - 2)
                    continue;
                  byte[] arrayOfByte3 = this.j;
                  int i9 = this.c;
                  this.c = (i9 + 1);
                  arrayOfByte3[i9] = paramArrayOfByte[i6];
                  byte[] arrayOfByte4 = this.j;
                  int i10 = this.c;
                  this.c = (i10 + 1);
                  arrayOfByte4[i10] = paramArrayOfByte[(i6 + 1)];
                }
                i12 = i8;
              }
              label1224: i23 = i22;
            }
            label1231: i38 = i1;
          }
          label1238: i7 = i2;
        }
        label1249: i40 = i1;
      }
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.util.c
 * JD-Core Version:    0.5.4
 */