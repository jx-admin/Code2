package com.anjoystudio.pandoradef;

import java.io.Serializable;

public class Sin
  implements Serializable
{
  int[] date;

  public Sin()
  {
    int[] arrayOfInt = new int[91];
    arrayOfInt[1] = 1745;
    arrayOfInt[2] = 3489;
    arrayOfInt[3] = 5233;
    arrayOfInt[4] = 6975;
    arrayOfInt[5] = 8715;
    arrayOfInt[6] = 10452;
    arrayOfInt[7] = 12186;
    arrayOfInt[8] = 13917;
    arrayOfInt[9] = 15643;
    arrayOfInt[10] = 17364;
    arrayOfInt[11] = 19080;
    arrayOfInt[12] = 20791;
    arrayOfInt[13] = 22495;
    arrayOfInt[14] = 24192;
    arrayOfInt[15] = 25881;
    arrayOfInt[16] = 27563;
    arrayOfInt[17] = 29237;
    arrayOfInt[18] = 30901;
    arrayOfInt[19] = 32556;
    arrayOfInt[20] = 34202;
    arrayOfInt[21] = 35836;
    arrayOfInt[22] = 37460;
    arrayOfInt[23] = 39073;
    arrayOfInt[24] = 40673;
    arrayOfInt[25] = 42261;
    arrayOfInt[26] = 43837;
    arrayOfInt[27] = 45399;
    arrayOfInt[28] = 46947;
    arrayOfInt[29] = 48480;
    arrayOfInt[30] = 49999;
    arrayOfInt[31] = 51503;
    arrayOfInt[32] = 52991;
    arrayOfInt[33] = 54463;
    arrayOfInt[34] = 55919;
    arrayOfInt[35] = 57357;
    arrayOfInt[36] = 58778;
    arrayOfInt[37] = 60181;
    arrayOfInt[38] = 61566;
    arrayOfInt[39] = 62932;
    arrayOfInt[40] = 64278;
    arrayOfInt[41] = 65605;
    arrayOfInt[42] = 66913;
    arrayOfInt[43] = 68199;
    arrayOfInt[44] = 69465;
    arrayOfInt[45] = 70710;
    arrayOfInt[46] = 71933;
    arrayOfInt[47] = 73135;
    arrayOfInt[48] = 74314;
    arrayOfInt[49] = 75470;
    arrayOfInt[50] = 76604;
    arrayOfInt[51] = 77714;
    arrayOfInt[52] = 78801;
    arrayOfInt[53] = 79863;
    arrayOfInt[54] = 80901;
    arrayOfInt[55] = 81915;
    arrayOfInt[56] = 82903;
    arrayOfInt[57] = 83867;
    arrayOfInt[58] = 84804;
    arrayOfInt[59] = 85716;
    arrayOfInt[60] = 86602;
    arrayOfInt[61] = 87461;
    arrayOfInt[62] = 88294;
    arrayOfInt[63] = 89100;
    arrayOfInt[64] = 89879;
    arrayOfInt[65] = 90630;
    arrayOfInt[66] = 91354;
    arrayOfInt[67] = 92050;
    arrayOfInt[68] = 92718;
    arrayOfInt[69] = 93358;
    arrayOfInt[70] = 93969;
    arrayOfInt[71] = 94551;
    arrayOfInt[72] = 95105;
    arrayOfInt[73] = 95630;
    arrayOfInt[74] = 96126;
    arrayOfInt[75] = 96592;
    arrayOfInt[76] = 97029;
    arrayOfInt[77] = 97437;
    arrayOfInt[78] = 97814;
    arrayOfInt[79] = 98162;
    arrayOfInt[80] = 98480;
    arrayOfInt[81] = 98768;
    arrayOfInt[82] = 99026;
    arrayOfInt[83] = 99254;
    arrayOfInt[84] = 99452;
    arrayOfInt[85] = 99619;
    arrayOfInt[86] = 99756;
    arrayOfInt[87] = 99862;
    arrayOfInt[88] = 99939;
    arrayOfInt[89] = 99984;
    arrayOfInt[90] = 100000;
    this.date = arrayOfInt;
  }

  public int getCos(int paramInt1, int paramInt2)
  {
    int l;
    if ((paramInt1 > 89) && (paramInt1 < 180))
      l = paramInt1 - 90;
    for (int i = -paramInt2 * this.date[l] / 100000; ; i = paramInt2 * this.date[(90 - paramInt1)] / 100000)
      while (true)
      {
        return i;
        if ((paramInt1 > 179) && (paramInt1 < 270))
        {
          int k = paramInt1 - 180;
          i = -paramInt2 * this.date[(90 - k)] / 100000;
        }
        if ((paramInt1 <= 269) || (paramInt1 >= 360))
          break;
        int j = paramInt1 - 270;
        i = paramInt2 * this.date[j] / 100000;
      }
  }

  public int getSin(int paramInt1, int paramInt2)
  {
    int l;
    if ((paramInt1 > 89) && (paramInt1 < 180))
      l = paramInt1 - 90;
    for (int i = paramInt2 * this.date[(90 - l)] / 100000; ; i = paramInt2 * this.date[paramInt1] / 100000)
      while (true)
      {
        return i;
        if ((paramInt1 > 179) && (paramInt1 < 270))
        {
          int k = paramInt1 - 180;
          i = -paramInt2 * this.date[k] / 100000;
        }
        if ((paramInt1 <= 269) || (paramInt1 >= 360))
          break;
        int j = paramInt1 - 270;
        i = -paramInt2 * this.date[(90 - j)] / 100000;
      }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Sin
 * JD-Core Version:    0.5.4
 */