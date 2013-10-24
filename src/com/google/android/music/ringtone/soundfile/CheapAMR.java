package com.google.android.music.ringtone.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class CheapAMR extends CheapSoundFile
{
  private static int[] BLOCK_SIZES = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
  private static int[] GAIN_FAC_MR475 = { 812, 128, 542, 140, 2873, 1135, 2266, 3402, 2067, 563, 12677, 647, 4132, 1798, 5601, 5285, 7689, 374, 3735, 441, 10912, 2638, 11807, 2494, 20490, 797, 5218, 675, 6724, 8354, 5282, 1696, 1488, 428, 5882, 452, 5332, 4072, 3583, 1268, 2469, 901, 15894, 1005, 14982, 3271, 10331, 4858, 3635, 2021, 2596, 835, 12360, 4892, 12206, 1704, 13432, 1604, 9118, 2341, 3968, 1538, 5479, 9936, 3795, 417, 1359, 414, 3640, 1569, 7995, 3541, 11405, 645, 8552, 635, 4056, 1377, 16608, 6124, 11420, 700, 2007, 607, 12415, 1578, 11119, 4654, 13680, 1708, 11990, 1229, 7996, 7297, 13231, 5715, 2428, 1159, 2073, 1941, 6218, 6121, 3546, 1804, 8925, 1802, 8679, 1580, 13935, 3576, 13313, 6237, 6142, 1130, 5994, 1734, 14141, 4662, 11271, 3321, 12226, 1551, 13931, 3015, 5081, 10464, 9444, 6706, 1689, 683, 1436, 1306, 7212, 3933, 4082, 2713, 7793, 704, 15070, 802, 6299, 5212, 4337, 5357, 6676, 541, 6062, 626, 13651, 3700, 11498, 2408, 16156, 716, 12177, 751, 8065, 11489, 6314, 2256, 4466, 496, 7293, 523, 10213, 3833, 8394, 3037, 8403, 966, 14228, 1880, 8703, 5409, 16395, 4863, 7420, 1979, 6089, 1230, 9371, 4398, 14558, 3363, 13559, 2873, 13163, 1465, 5534, 1678, 13138, 14771, 7338, 600, 1318, 548, 4252, 3539, 10044, 2364, 10587, 622, 13088, 669, 14126, 3526, 5039, 9784, 15338, 619, 3115, 590, 16442, 3013, 15542, 4168, 15537, 1611, 15405, 1228, 16023, 9299, 7534, 4976, 1990, 1213, 11447, 1157, 12512, 5519, 9475, 2644, 7716, 2034, 13280, 2239, 16011, 5093, 8066, 6761, 10083, 1413, 5002, 2347, 12523, 5975, 15126, 2899, 18264, 2289, 15827, 2527, 16265, 10254, 14651, 11319, 1797, 337, 3115, 397, 3510, 2928, 4592, 2670, 7519, 628, 11415, 656, 5946, 2435, 6544, 7367, 8238, 829, 4000, 863, 10032, 2492, 16057, 3551, 18204, 1054, 6103, 1454, 5884, 7900, 18752, 3468, 1864, 544, 9198, 683, 11623, 4160, 4594, 1644, 3158, 1157, 15953, 2560, 12349, 3733, 17420, 5260, 6106, 2004, 2917, 1742, 16467, 5257, 16787, 1680, 17205, 1759, 4773, 3231, 7386, 6035, 14342, 10012, 4035, 442, 4194, 458, 9214, 2242, 7427, 4217, 12860, 801, 11186, 825, 12648, 2084, 12956, 6554, 9505, 996, 6629, 985, 10537, 2502, 15289, 5006, 12602, 2055, 15484, 1653, 16194, 6921, 14231, 5790, 2626, 828, 5615, 1686, 13663, 5778, 3668, 1554, 11313, 2633, 9770, 1459, 14003, 4733, 15897, 6291, 6278, 1870, 7910, 2285, 16978, 4571, 16576, 3849, 15248, 2311, 16023, 3244, 14459, 17808, 11847, 2763, 1981, 1407, 1400, 876, 4335, 3547, 4391, 4210, 5405, 680, 17461, 781, 6501, 5118, 8091, 7677, 7355, 794, 8333, 1182, 15041, 3160, 14928, 3039, 20421, 880, 14545, 852, 12337, 14708, 6904, 1920, 4225, 933, 8218, 1087, 10659, 4084, 10082, 4533, 2735, 840, 20657, 1081, 16711, 5966, 15873, 4578, 10871, 2574, 3773, 1166, 14519, 4044, 20699, 2627, 15219, 2734, 15274, 2186, 6257, 3226, 13125, 19480, 7196, 930, 2462, 1618, 4515, 3092, 13852, 4277, 10460, 833, 17339, 810, 16891, 2289, 15546, 8217, 13603, 1684, 3197, 1834, 15948, 2820, 15812, 5327, 17006, 2438, 16788, 1326, 15671, 8156, 11726, 8556, 3762, 2053, 9563, 1317, 13561, 6790, 12227, 1936, 8180, 3550, 13287, 1778, 16299, 6599, 16291, 7758, 8521, 2551, 7225, 2645, 18269, 7489, 16885, 2248, 17882, 2884, 17265, 3328, 9417, 20162, 11042, 8320, 1286, 620, 1431, 583, 5993, 2289, 3978, 3626, 5144, 752, 13409, 830, 5553, 2860, 11764, 5908, 10737, 560, 5446, 564, 13321, 3008, 11946, 3683, 19887, 798, 9825, 728, 13663, 8748, 7391, 3053, 2515, 778, 6050, 833, 6469, 5074, 8305, 2463, 6141, 1865, 15308, 1262, 14408, 4547, 13663, 4515, 3137, 2983, 2479, 1259, 15088, 4647, 15382, 2607, 14492, 2392, 12462, 2537, 7539, 2949, 12909, 12060, 5468, 684, 3141, 722, 5081, 1274, 12732, 4200, 15302, 681, 7819, 592, 6534, 2021, 16478, 8737, 13364, 882, 5397, 899, 14656, 2178, 14741, 4227, 14270, 1298, 13929, 2029, 15477, 7482, 15815, 4572, 2521, 2013, 5062, 1804, 5159, 6582, 7130, 3597, 10920, 1611, 11729, 1708, 16903, 3455, 16268, 6640, 9306, 1007, 9369, 2106, 19182, 5037, 12441, 4269, 15919, 1332, 15357, 3512, 11898, 14141, 16101, 6854, 2010, 737, 3779, 861, 11454, 2880, 3564, 3540, 9057, 1241, 12391, 896, 8546, 4629, 11561, 5776, 8129, 589, 8218, 588, 18728, 3755, 12973, 3149, 15729, 758, 16634, 754, 15222, 11138, 15871, 2208, 4673, 610, 10218, 678, 15257, 4146, 5729, 3327, 8377, 1670, 19862, 2321, 15450, 5511, 14054, 5481, 5728, 2888, 7580, 1346, 14384, 5325, 16236, 3950, 15118, 3744, 15306, 1435, 14597, 4070, 12301, 15696, 7617, 1699, 2170, 884, 4459, 4567, 18094, 3306, 12742, 815, 14926, 907, 15016, 4281, 15518, 8368, 17994, 1087, 2358, 865, 16281, 3787, 15679, 4596, 16356, 1534, 16584, 2210, 16833, 9697, 15929, 4513, 3277, 1085, 9643, 2187, 11973, 6068, 9199, 4462, 8955, 1629, 10289, 3062, 16481, 5155, 15466, 7066, 13678, 2543, 5273, 2277, 16746, 6213, 16655, 3408, 20304, 3363, 18688, 1985, 14172, 12867, 15154, 15703, 4473, 1020, 1681, 886, 4311, 4301, 8952, 3657, 5893, 1147, 11647, 1452, 15886, 2227, 4582, 6644, 6929, 1205, 6220, 799, 12415, 3409, 15968, 3877, 19859, 2109, 9689, 2141, 14742, 8830, 14480, 2599, 1817, 1238, 7771, 813, 19079, 4410, 5554, 2064, 3687, 2844, 17435, 2256, 16697, 4486, 16199, 5388, 8028, 2763, 3405, 2119, 17426, 5477, 13698, 2786, 19879, 2720, 9098, 3880, 18172, 4833, 17336, 12207, 5116, 996, 4935, 988, 9888, 3081, 6014, 5371, 15881, 1667, 8405, 1183, 15087, 2366, 19777, 7002, 11963, 1562, 7279, 1128, 16859, 1532, 15762, 5381, 14708, 2065, 20105, 2155, 17158, 8245, 17911, 6318, 5467, 1504, 4100, 2574, 17421, 6810, 5673, 2888, 16636, 3382, 8975, 1831, 20159, 4737, 19550, 7294, 6658, 2781, 11472, 3321, 19397, 5054, 18878, 4722, 16439, 2373, 20430, 4386, 11353, 26526, 11593, 3068, 2866, 1566, 5108, 1070, 9614, 4915, 4939, 3536, 7541, 878, 20717, 851, 6938, 4395, 16799, 7733, 10137, 1019, 9845, 964, 15494, 3955, 15459, 3430, 18863, 982, 20120, 963, 16876, 12887, 14334, 4200, 6599, 1220, 9222, 814, 16942, 5134, 5661, 4898, 5488, 1798, 20258, 3962, 17005, 6178, 17929, 5929, 9365, 3420, 7474, 1971, 19537, 5177, 19003, 3006, 16454, 3788, 16070, 2367, 8664, 2743, 9445, 26358, 10856, 1287, 3555, 1009, 5606, 3622, 19453, 5512, 12453, 797, 20634, 911, 15427, 3066, 17037, 10275, 18883, 2633, 3913, 1268, 19519, 3371, 18052, 5230, 19291, 1678, 19508, 3172, 18072, 10754, 16625, 6845, 3134, 2298, 10869, 2437, 15580, 6913, 12597, 3381, 11116, 3297, 16762, 2424, 18853, 6715, 17171, 9887, 12743, 2605, 8937, 3140, 19033, 7764, 18347, 3880, 20475, 3682, 19602, 3380, 13044, 19373, 10526, 23124 };
  private static int[] GAIN_FAC_MR515 = { 28753, 2785, 6594, 7413, 10444, 1269, 4423, 1556, 12820, 2498, 4833, 2498, 7864, 1884, 3153, 1802, 20193, 3031, 5857, 4014, 8970, 1392, 4096, 655, 13926, 3112, 4669, 2703, 6553, 901, 2662, 655, 23511, 2457, 5079, 4096, 8560, 737, 4259, 2088, 12288, 1474, 4628, 1433, 7004, 737, 2252, 1228, 17326, 2334, 5816, 3686, 8601, 778, 3809, 614, 9256, 1761, 3522, 1966, 5529, 737, 3194, 778 };
  private static int[] GRAY = { 0, 1, 3, 2, 5, 6, 4, 7 };
  private static int[] QUA_ENER_MR515 = { 17333, -3431, 4235, 5276, 8325, -10422, 683, -8609, 10148, -4398, 1472, -4398, 5802, -6907, -2327, -7303, 14189, -2678, 3181, -180, 6972, -9599, 0, -16305, 10884, -2444, 1165, -3697, 4180, -13468, -3833, -16305, 15543, -4546, 1913, 0, 6556, -15255, 347, -5993, 9771, -9090, 1086, -9341, 4772, -15255, -5321, -10714, 12827, -5002, 3118, -938, 6598, -14774, -646, -16879, 7251, -7508, -1343, -6529, 2668, -15255, -2212, -2454, -14774 };
  private static int[] QUA_GAIN_CODE = { 159, -3776, -22731, 206, -3394, -20428, 268, -3005, -18088, 349, -2615, -15739, 419, -2345, -14113, 482, -2138, -12867, 554, -1932, -11629, 637, -1726, -10387, 733, -1518, -9139, 842, -1314, -7906, 969, -1106, -6656, 1114, -900, -5416, 1281, -694, -4173, 1473, -487, -2931, 1694, -281, -1688, 1948, -75, -445, 2241, 133, 801, 2577, 339, 2044, 2963, 545, 3285, 3408, 752, 4530, 3919, 958, 5772, 4507, 1165, 7016, 5183, 1371, 8259, 5960, 1577, 9501, 6855, 1784, 10745, 7883, 1991, 11988, 9065, 2197, 13231, 10425, 2404, 14474, 12510, 2673, 16096, 16263, 3060, 18429, 21142, 3448, 20763, 27485, 3836, 23097 };
  private static int[] QUA_GAIN_PITCH = { 0, 3277, 6556, 8192, 9830, 11469, 12288, 13107, 13926, 14746, 15565, 16384, 17203, 18022, 18842, 19661 };
  private int mBitRate;
  private int mFileSize;
  private int[] mFrameGains;
  private int[] mFrameLens;
  private int[] mFrameOffsets;
  private int mMaxFrames;
  private int mMaxGain;
  private int mMinGain;
  private int mNumFrames;
  private int mOffset;

  public static CheapSoundFile.Factory getFactory()
  {
    return new CheapSoundFile.Factory()
    {
      public CheapSoundFile create()
      {
        return new CheapAMR();
      }

      public String[] getSupportedExtensions()
      {
        String[] arrayOfString = new String[3];
        arrayOfString[0] = "3gpp";
        arrayOfString[1] = "3gp";
        arrayOfString[2] = "amr";
        return arrayOfString;
      }
    };
  }

  private void parse3gpp(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    if (paramInt < 8)
      return;
    byte[] arrayOfByte = new byte[8];
    int i = paramInputStream.read(arrayOfByte, 0, 8);
    int j = this.mOffset + 8;
    this.mOffset = j;
    int k = (arrayOfByte[0] & 0xFF) << 24;
    int m = (arrayOfByte[1] & 0xFF) << 16;
    int n = k | m;
    int i1 = (arrayOfByte[2] & 0xFF) << 8;
    int i2 = n | i1;
    int i3 = arrayOfByte[3] & 0xFF;
    int i4 = i2 | i3;
    if (i4 > paramInt)
      return;
    if (i4 <= 0)
      return;
    if ((arrayOfByte[4] == 109) && (arrayOfByte[5] == 100) && (arrayOfByte[6] == 97) && (arrayOfByte[7] == 116))
    {
      parseAMR(paramInputStream, i4);
      return;
    }
    long l1 = i4 + -8;
    long l2 = paramInputStream.skip(l1);
    int i5 = this.mOffset;
    int i6 = i4 + -8;
    int i7 = i5 + i6;
    this.mOffset = i7;
    int i8 = paramInt - i4;
    parse3gpp(paramInputStream, i8);
  }

  public void ReadFile(File paramFile)
    throws FileNotFoundException, IOException
  {
    super.ReadFile(paramFile);
    this.mNumFrames = 0;
    this.mMaxFrames = 64;
    int[] arrayOfInt1 = new int[this.mMaxFrames];
    this.mFrameOffsets = arrayOfInt1;
    int[] arrayOfInt2 = new int[this.mMaxFrames];
    this.mFrameLens = arrayOfInt2;
    int[] arrayOfInt3 = new int[this.mMaxFrames];
    this.mFrameGains = arrayOfInt3;
    this.mMinGain = 1000000000;
    this.mMaxGain = 0;
    this.mBitRate = 10;
    this.mOffset = 0;
    int i = (int)this.mInputFile.length();
    this.mFileSize = i;
    if (this.mFileSize < 128)
      throw new IOException("File too small to parse");
    File localFile = this.mInputFile;
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    byte[] arrayOfByte = new byte[12];
    int j = localFileInputStream.read(arrayOfByte, 0, 6);
    int k = this.mOffset + 6;
    this.mOffset = k;
    if ((arrayOfByte[0] == 35) && (arrayOfByte[1] == 33) && (arrayOfByte[2] == 65) && (arrayOfByte[3] == 77) && (arrayOfByte[4] == 82) && (arrayOfByte[5] == 10))
    {
      int m = this.mFileSize + -6;
      parseAMR(localFileInputStream, m);
    }
    int n = localFileInputStream.read(arrayOfByte, 6, 6);
    int i1 = this.mOffset + 6;
    this.mOffset = i1;
    if (arrayOfByte[4] != 102)
      return;
    if (arrayOfByte[5] != 116)
      return;
    if (arrayOfByte[6] != 121)
      return;
    if (arrayOfByte[7] != 112)
      return;
    if (arrayOfByte[8] != 51)
      return;
    if (arrayOfByte[9] != 103)
      return;
    if (arrayOfByte[10] != 112)
      return;
    if (arrayOfByte[11] != 52)
      return;
    int i2 = (arrayOfByte[0] & 0xFF) << 24;
    int i3 = (arrayOfByte[1] & 0xFF) << 16;
    int i4 = i2 | i3;
    int i5 = (arrayOfByte[2] & 0xFF) << 8;
    int i6 = i4 | i5;
    int i7 = arrayOfByte[3] & 0xFF;
    int i8 = i6 | i7;
    if (i8 >= 4)
    {
      int i9 = this.mFileSize + -8;
      if (i8 <= i9)
      {
        long l1 = i8 + -12;
        long l2 = localFileInputStream.skip(l1);
        int i10 = this.mOffset;
        int i11 = i8 + -12;
        int i12 = i10 + i11;
        this.mOffset = i12;
      }
    }
    int i13 = this.mFileSize - i8;
    parse3gpp(localFileInputStream, i13);
  }

  public void WriteFile(File paramFile, int paramInt1, int paramInt2)
    throws IOException
  {
    boolean bool = paramFile.createNewFile();
    File localFile = this.mInputFile;
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    byte[] arrayOfByte1 = new byte[6];
    arrayOfByte1[0] = 35;
    arrayOfByte1[1] = 33;
    arrayOfByte1[2] = 65;
    arrayOfByte1[3] = 77;
    arrayOfByte1[4] = 82;
    arrayOfByte1[5] = 10;
    localFileOutputStream.write(arrayOfByte1, 0, 6);
    int i = 0;
    int j = 0;
    while (j < paramInt2)
    {
      int[] arrayOfInt1 = this.mFrameLens;
      int k = paramInt1 + j;
      if (arrayOfInt1[k] > i)
      {
        int[] arrayOfInt2 = this.mFrameLens;
        int m = paramInt1 + j;
        i = arrayOfInt2[m];
      }
      j += 1;
    }
    byte[] arrayOfByte2 = new byte[i];
    int n = 0;
    int i1 = 0;
    if (i1 < paramInt2)
    {
      int[] arrayOfInt3 = this.mFrameOffsets;
      int i2 = paramInt1 + i1;
      int i3 = arrayOfInt3[i2] - n;
      int[] arrayOfInt4 = this.mFrameLens;
      int i4 = paramInt1 + i1;
      int i5 = arrayOfInt4[i4];
      if (i3 < 0);
      while (true)
      {
        i1 += 1;
        break;
        if (i3 > 0)
        {
          long l1 = i3;
          long l2 = localFileInputStream.skip(l1);
          n += i3;
        }
        int i6 = localFileInputStream.read(arrayOfByte2, 0, i5);
        localFileOutputStream.write(arrayOfByte2, 0, i5);
        n += i5;
      }
    }
    localFileInputStream.close();
    localFileOutputStream.close();
  }

  void addFrame(int paramInt1, int paramInt2, int paramInt3)
  {
    int[] arrayOfInt1 = this.mFrameOffsets;
    int i = this.mNumFrames;
    arrayOfInt1[i] = paramInt1;
    int[] arrayOfInt2 = this.mFrameLens;
    int j = this.mNumFrames;
    arrayOfInt2[j] = paramInt2;
    int[] arrayOfInt3 = this.mFrameGains;
    int k = this.mNumFrames;
    arrayOfInt3[k] = paramInt3;
    int m = this.mMinGain;
    if (paramInt3 < m)
      this.mMinGain = paramInt3;
    int n = this.mMaxGain;
    if (paramInt3 > n)
      this.mMaxGain = paramInt3;
    int i1 = this.mNumFrames + 1;
    this.mNumFrames = i1;
    int i2 = this.mNumFrames;
    int i3 = this.mMaxFrames;
    if (i2 != i3)
      return;
    int i4 = this.mMaxFrames * 2;
    int[] arrayOfInt4 = new int[i4];
    int[] arrayOfInt5 = new int[i4];
    int[] arrayOfInt6 = new int[i4];
    int i5 = 0;
    while (true)
    {
      int i6 = this.mNumFrames;
      if (i5 >= i6)
        break;
      int i7 = this.mFrameOffsets[i5];
      arrayOfInt4[i5] = i7;
      int i8 = this.mFrameLens[i5];
      arrayOfInt5[i5] = i8;
      int i9 = this.mFrameGains[i5];
      arrayOfInt6[i5] = i9;
      i5 += 1;
    }
    this.mFrameOffsets = arrayOfInt4;
    this.mFrameLens = arrayOfInt5;
    this.mFrameGains = arrayOfInt6;
    this.mMaxFrames = i4;
  }

  public int getAvgBitrateKbps()
  {
    return this.mBitRate;
  }

  public String getFiletype()
  {
    return "AMR";
  }

  public int[] getFrameGains()
  {
    return this.mFrameGains;
  }

  // ERROR //
  void getMR122Params(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[][] paramArrayOfInt)
  {
    // Byte code:
    //   0: aload_1
    //   1: bipush 45
    //   3: iaload
    //   4: iconst_1
    //   5: imul
    //   6: istore 6
    //   8: aload_1
    //   9: bipush 43
    //   11: iaload
    //   12: iconst_2
    //   13: imul
    //   14: istore 7
    //   16: iload 6
    //   18: iload 7
    //   20: iadd
    //   21: istore 8
    //   23: aload_1
    //   24: bipush 41
    //   26: iaload
    //   27: iconst_4
    //   28: imul
    //   29: istore 9
    //   31: iload 8
    //   33: iload 9
    //   35: iadd
    //   36: istore 10
    //   38: aload_1
    //   39: bipush 39
    //   41: iaload
    //   42: bipush 8
    //   44: imul
    //   45: istore 11
    //   47: iload 10
    //   49: iload 11
    //   51: iadd
    //   52: istore 12
    //   54: aload_1
    //   55: bipush 37
    //   57: iaload
    //   58: bipush 16
    //   60: imul
    //   61: istore 13
    //   63: iload 12
    //   65: iload 13
    //   67: iadd
    //   68: istore 14
    //   70: aload_1
    //   71: bipush 35
    //   73: iaload
    //   74: bipush 32
    //   76: imul
    //   77: istore 15
    //   79: iload 14
    //   81: iload 15
    //   83: iadd
    //   84: istore 16
    //   86: aload_1
    //   87: bipush 33
    //   89: iaload
    //   90: bipush 64
    //   92: imul
    //   93: istore 17
    //   95: iload 16
    //   97: iload 17
    //   99: iadd
    //   100: istore 18
    //   102: aload_1
    //   103: bipush 31
    //   105: iaload
    //   106: sipush 128
    //   109: imul
    //   110: istore 19
    //   112: iload 18
    //   114: iload 19
    //   116: iadd
    //   117: istore 20
    //   119: aload_1
    //   120: bipush 29
    //   122: iaload
    //   123: sipush 256
    //   126: imul
    //   127: istore 21
    //   129: iload 20
    //   131: iload 21
    //   133: iadd
    //   134: istore 22
    //   136: aload_2
    //   137: iconst_0
    //   138: iload 22
    //   140: iastore
    //   141: aload_1
    //   142: sipush 242
    //   145: iaload
    //   146: iconst_1
    //   147: imul
    //   148: istore 23
    //   150: aload_1
    //   151: bipush 79
    //   153: iaload
    //   154: iconst_2
    //   155: imul
    //   156: istore 24
    //   158: iload 23
    //   160: iload 24
    //   162: iadd
    //   163: istore 25
    //   165: aload_1
    //   166: bipush 77
    //   168: iaload
    //   169: iconst_4
    //   170: imul
    //   171: istore 26
    //   173: iload 25
    //   175: iload 26
    //   177: iadd
    //   178: istore 27
    //   180: aload_1
    //   181: bipush 75
    //   183: iaload
    //   184: bipush 8
    //   186: imul
    //   187: istore 28
    //   189: iload 27
    //   191: iload 28
    //   193: iadd
    //   194: istore 29
    //   196: aload_1
    //   197: bipush 73
    //   199: iaload
    //   200: bipush 16
    //   202: imul
    //   203: istore 30
    //   205: iload 29
    //   207: iload 30
    //   209: iadd
    //   210: istore 31
    //   212: aload_1
    //   213: bipush 71
    //   215: iaload
    //   216: bipush 32
    //   218: imul
    //   219: istore 32
    //   221: iload 31
    //   223: iload 32
    //   225: iadd
    //   226: istore 33
    //   228: aload_2
    //   229: iconst_1
    //   230: iload 33
    //   232: iastore
    //   233: aload_1
    //   234: bipush 46
    //   236: iaload
    //   237: iconst_1
    //   238: imul
    //   239: istore 34
    //   241: aload_1
    //   242: bipush 44
    //   244: iaload
    //   245: iconst_2
    //   246: imul
    //   247: istore 35
    //   249: iload 34
    //   251: iload 35
    //   253: iadd
    //   254: istore 36
    //   256: aload_1
    //   257: bipush 42
    //   259: iaload
    //   260: iconst_4
    //   261: imul
    //   262: istore 37
    //   264: iload 36
    //   266: iload 37
    //   268: iadd
    //   269: istore 38
    //   271: aload_1
    //   272: bipush 40
    //   274: iaload
    //   275: bipush 8
    //   277: imul
    //   278: istore 39
    //   280: iload 38
    //   282: iload 39
    //   284: iadd
    //   285: istore 40
    //   287: aload_1
    //   288: bipush 38
    //   290: iaload
    //   291: bipush 16
    //   293: imul
    //   294: istore 41
    //   296: iload 40
    //   298: iload 41
    //   300: iadd
    //   301: istore 42
    //   303: aload_1
    //   304: bipush 36
    //   306: iaload
    //   307: bipush 32
    //   309: imul
    //   310: istore 43
    //   312: iload 42
    //   314: iload 43
    //   316: iadd
    //   317: istore 44
    //   319: aload_1
    //   320: bipush 34
    //   322: iaload
    //   323: bipush 64
    //   325: imul
    //   326: istore 45
    //   328: iload 44
    //   330: iload 45
    //   332: iadd
    //   333: istore 46
    //   335: aload_1
    //   336: bipush 32
    //   338: iaload
    //   339: sipush 128
    //   342: imul
    //   343: istore 47
    //   345: iload 46
    //   347: iload 47
    //   349: iadd
    //   350: istore 48
    //   352: aload_1
    //   353: bipush 30
    //   355: iaload
    //   356: sipush 256
    //   359: imul
    //   360: istore 49
    //   362: iload 48
    //   364: iload 49
    //   366: iadd
    //   367: istore 50
    //   369: aload_2
    //   370: iconst_2
    //   371: iload 50
    //   373: iastore
    //   374: aload_1
    //   375: sipush 243
    //   378: iaload
    //   379: iconst_1
    //   380: imul
    //   381: istore 51
    //   383: aload_1
    //   384: bipush 80
    //   386: iaload
    //   387: iconst_2
    //   388: imul
    //   389: istore 52
    //   391: iload 51
    //   393: iload 52
    //   395: iadd
    //   396: istore 53
    //   398: aload_1
    //   399: bipush 78
    //   401: iaload
    //   402: iconst_4
    //   403: imul
    //   404: istore 54
    //   406: iload 53
    //   408: iload 54
    //   410: iadd
    //   411: istore 55
    //   413: aload_1
    //   414: bipush 76
    //   416: iaload
    //   417: bipush 8
    //   419: imul
    //   420: istore 56
    //   422: iload 55
    //   424: iload 56
    //   426: iadd
    //   427: istore 57
    //   429: aload_1
    //   430: bipush 74
    //   432: iaload
    //   433: bipush 16
    //   435: imul
    //   436: istore 58
    //   438: iload 57
    //   440: iload 58
    //   442: iadd
    //   443: istore 59
    //   445: aload_1
    //   446: bipush 72
    //   448: iaload
    //   449: bipush 32
    //   451: imul
    //   452: istore 60
    //   454: iload 59
    //   456: iload 60
    //   458: iadd
    //   459: istore 61
    //   461: aload_2
    //   462: iconst_3
    //   463: iload 61
    //   465: iastore
    //   466: aload_1
    //   467: bipush 88
    //   469: iaload
    //   470: iconst_1
    //   471: imul
    //   472: istore 62
    //   474: aload_1
    //   475: bipush 55
    //   477: iaload
    //   478: iconst_2
    //   479: imul
    //   480: istore 63
    //   482: iload 62
    //   484: iload 63
    //   486: iadd
    //   487: istore 64
    //   489: aload_1
    //   490: bipush 51
    //   492: iaload
    //   493: iconst_4
    //   494: imul
    //   495: istore 65
    //   497: iload 64
    //   499: iload 65
    //   501: iadd
    //   502: istore 66
    //   504: aload_1
    //   505: bipush 47
    //   507: iaload
    //   508: bipush 8
    //   510: imul
    //   511: istore 67
    //   513: iload 66
    //   515: iload 67
    //   517: iadd
    //   518: istore 68
    //   520: aload_3
    //   521: iconst_0
    //   522: iload 68
    //   524: iastore
    //   525: aload_1
    //   526: bipush 89
    //   528: iaload
    //   529: iconst_1
    //   530: imul
    //   531: istore 69
    //   533: aload_1
    //   534: bipush 56
    //   536: iaload
    //   537: iconst_2
    //   538: imul
    //   539: istore 70
    //   541: iload 69
    //   543: iload 70
    //   545: iadd
    //   546: istore 71
    //   548: aload_1
    //   549: bipush 52
    //   551: iaload
    //   552: iconst_4
    //   553: imul
    //   554: istore 72
    //   556: iload 71
    //   558: iload 72
    //   560: iadd
    //   561: istore 73
    //   563: aload_1
    //   564: bipush 48
    //   566: iaload
    //   567: bipush 8
    //   569: imul
    //   570: istore 74
    //   572: iload 73
    //   574: iload 74
    //   576: iadd
    //   577: istore 75
    //   579: aload_3
    //   580: iconst_1
    //   581: iload 75
    //   583: iastore
    //   584: aload_1
    //   585: bipush 90
    //   587: iaload
    //   588: iconst_1
    //   589: imul
    //   590: istore 76
    //   592: aload_1
    //   593: bipush 57
    //   595: iaload
    //   596: iconst_2
    //   597: imul
    //   598: istore 77
    //   600: iload 76
    //   602: iload 77
    //   604: iadd
    //   605: istore 78
    //   607: aload_1
    //   608: bipush 53
    //   610: iaload
    //   611: iconst_4
    //   612: imul
    //   613: istore 79
    //   615: iload 78
    //   617: iload 79
    //   619: iadd
    //   620: istore 80
    //   622: aload_1
    //   623: bipush 49
    //   625: iaload
    //   626: bipush 8
    //   628: imul
    //   629: istore 81
    //   631: iload 80
    //   633: iload 81
    //   635: iadd
    //   636: istore 82
    //   638: aload_3
    //   639: iconst_2
    //   640: iload 82
    //   642: iastore
    //   643: aload_1
    //   644: bipush 91
    //   646: iaload
    //   647: iconst_1
    //   648: imul
    //   649: istore 83
    //   651: aload_1
    //   652: bipush 58
    //   654: iaload
    //   655: iconst_2
    //   656: imul
    //   657: istore 84
    //   659: iload 83
    //   661: iload 84
    //   663: iadd
    //   664: istore 85
    //   666: aload_1
    //   667: bipush 54
    //   669: iaload
    //   670: iconst_4
    //   671: imul
    //   672: istore 86
    //   674: iload 85
    //   676: iload 86
    //   678: iadd
    //   679: istore 87
    //   681: aload_1
    //   682: bipush 50
    //   684: iaload
    //   685: bipush 8
    //   687: imul
    //   688: istore 88
    //   690: iload 87
    //   692: iload 88
    //   694: iadd
    //   695: istore 89
    //   697: aload_3
    //   698: iconst_3
    //   699: iload 89
    //   701: iastore
    //   702: aload_1
    //   703: bipush 104
    //   705: iaload
    //   706: iconst_1
    //   707: imul
    //   708: istore 90
    //   710: aload_1
    //   711: bipush 92
    //   713: iaload
    //   714: iconst_2
    //   715: imul
    //   716: istore 91
    //   718: iload 90
    //   720: iload 91
    //   722: iadd
    //   723: istore 92
    //   725: aload_1
    //   726: bipush 67
    //   728: iaload
    //   729: iconst_4
    //   730: imul
    //   731: istore 93
    //   733: iload 92
    //   735: iload 93
    //   737: iadd
    //   738: istore 94
    //   740: aload_1
    //   741: bipush 63
    //   743: iaload
    //   744: bipush 8
    //   746: imul
    //   747: istore 95
    //   749: iload 94
    //   751: iload 95
    //   753: iadd
    //   754: istore 96
    //   756: aload_1
    //   757: bipush 59
    //   759: iaload
    //   760: bipush 16
    //   762: imul
    //   763: istore 97
    //   765: iload 96
    //   767: iload 97
    //   769: iadd
    //   770: istore 98
    //   772: aload 4
    //   774: iconst_0
    //   775: iload 98
    //   777: iastore
    //   778: aload_1
    //   779: bipush 105
    //   781: iaload
    //   782: iconst_1
    //   783: imul
    //   784: istore 99
    //   786: aload_1
    //   787: bipush 93
    //   789: iaload
    //   790: iconst_2
    //   791: imul
    //   792: istore 100
    //   794: iload 99
    //   796: iload 100
    //   798: iadd
    //   799: istore 101
    //   801: aload_1
    //   802: bipush 68
    //   804: iaload
    //   805: iconst_4
    //   806: imul
    //   807: istore 102
    //   809: iload 101
    //   811: iload 102
    //   813: iadd
    //   814: istore 103
    //   816: aload_1
    //   817: bipush 64
    //   819: iaload
    //   820: bipush 8
    //   822: imul
    //   823: istore 104
    //   825: iload 103
    //   827: iload 104
    //   829: iadd
    //   830: istore 105
    //   832: aload_1
    //   833: bipush 60
    //   835: iaload
    //   836: bipush 16
    //   838: imul
    //   839: istore 106
    //   841: iload 105
    //   843: iload 106
    //   845: iadd
    //   846: istore 107
    //   848: aload 4
    //   850: iconst_1
    //   851: iload 107
    //   853: iastore
    //   854: aload_1
    //   855: bipush 106
    //   857: iaload
    //   858: iconst_1
    //   859: imul
    //   860: istore 108
    //   862: aload_1
    //   863: bipush 94
    //   865: iaload
    //   866: iconst_2
    //   867: imul
    //   868: istore 109
    //   870: iload 108
    //   872: iload 109
    //   874: iadd
    //   875: istore 110
    //   877: aload_1
    //   878: bipush 69
    //   880: iaload
    //   881: iconst_4
    //   882: imul
    //   883: istore 111
    //   885: iload 110
    //   887: iload 111
    //   889: iadd
    //   890: istore 112
    //   892: aload_1
    //   893: bipush 65
    //   895: iaload
    //   896: bipush 8
    //   898: imul
    //   899: istore 113
    //   901: iload 112
    //   903: iload 113
    //   905: iadd
    //   906: istore 114
    //   908: aload_1
    //   909: bipush 61
    //   911: iaload
    //   912: bipush 16
    //   914: imul
    //   915: istore 115
    //   917: iload 114
    //   919: iload 115
    //   921: iadd
    //   922: istore 116
    //   924: aload 4
    //   926: iconst_2
    //   927: iload 116
    //   929: iastore
    //   930: aload_1
    //   931: bipush 107
    //   933: iaload
    //   934: iconst_1
    //   935: imul
    //   936: istore 117
    //   938: aload_1
    //   939: bipush 95
    //   941: iaload
    //   942: iconst_2
    //   943: imul
    //   944: istore 118
    //   946: iload 117
    //   948: iload 118
    //   950: iadd
    //   951: istore 119
    //   953: aload_1
    //   954: bipush 70
    //   956: iaload
    //   957: iconst_4
    //   958: imul
    //   959: istore 120
    //   961: iload 119
    //   963: iload 120
    //   965: iadd
    //   966: istore 121
    //   968: aload_1
    //   969: bipush 66
    //   971: iaload
    //   972: bipush 8
    //   974: imul
    //   975: istore 122
    //   977: iload 121
    //   979: iload 122
    //   981: iadd
    //   982: istore 123
    //   984: aload_1
    //   985: bipush 62
    //   987: iaload
    //   988: bipush 16
    //   990: imul
    //   991: istore 124
    //   993: iload 123
    //   995: iload 124
    //   997: iadd
    //   998: istore 125
    //   1000: aload 4
    //   1002: iconst_3
    //   1003: iload 125
    //   1005: iastore
    //   1006: aload 5
    //   1008: iconst_0
    //   1009: aaload
    //   1010: astore 126
    //   1012: aload_1
    //   1013: bipush 122
    //   1015: iaload
    //   1016: iconst_1
    //   1017: imul
    //   1018: istore 127
    //   1020: aload_1
    //   1021: bipush 123
    //   1023: iaload
    //   1024: iconst_2
    //   1025: imul
    //   1026: istore 128
    //   1028: iload 127
    //   1030: iload 128
    //   1032: iadd
    //   1033: istore 129
    //   1035: aload_1
    //   1036: bipush 124
    //   1038: iaload
    //   1039: iconst_4
    //   1040: imul
    //   1041: istore 130
    //   1043: iload 129
    //   1045: iload 130
    //   1047: iadd
    //   1048: istore 131
    //   1050: aload_1
    //   1051: bipush 96
    //   1053: iaload
    //   1054: bipush 8
    //   1056: imul
    //   1057: istore 132
    //   1059: iload 131
    //   1061: iload 132
    //   1063: iadd
    //   1064: istore 133
    //   1066: aload 126
    //   1068: iconst_0
    //   1069: iload 133
    //   1071: iastore
    //   1072: aload 5
    //   1074: iconst_0
    //   1075: aaload
    //   1076: astore 134
    //   1078: aload_1
    //   1079: bipush 125
    //   1081: iaload
    //   1082: iconst_1
    //   1083: imul
    //   1084: istore 135
    //   1086: aload_1
    //   1087: bipush 126
    //   1089: iaload
    //   1090: iconst_2
    //   1091: imul
    //   1092: istore 136
    //   1094: iload 135
    //   1096: iload 136
    //   1098: iadd
    //   1099: istore 137
    //   1101: aload_1
    //   1102: bipush 127
    //   1104: iaload
    //   1105: iconst_4
    //   1106: imul
    //   1107: istore 138
    //   1109: iload 137
    //   1111: iload 138
    //   1113: iadd
    //   1114: istore 139
    //   1116: aload_1
    //   1117: bipush 100
    //   1119: iaload
    //   1120: bipush 8
    //   1122: imul
    //   1123: istore 140
    //   1125: iload 139
    //   1127: iload 140
    //   1129: iadd
    //   1130: istore 141
    //   1132: aload 134
    //   1134: iconst_1
    //   1135: iload 141
    //   1137: iastore
    //   1138: aload 5
    //   1140: iconst_0
    //   1141: aaload
    //   1142: astore 142
    //   1144: aload_1
    //   1145: sipush 128
    //   1148: iaload
    //   1149: iconst_1
    //   1150: imul
    //   1151: istore 143
    //   1153: aload_1
    //   1154: sipush 129
    //   1157: iaload
    //   1158: iconst_2
    //   1159: imul
    //   1160: istore 144
    //   1162: iload 143
    //   1164: iload 144
    //   1166: iadd
    //   1167: istore 145
    //   1169: aload_1
    //   1170: sipush 130
    //   1173: iaload
    //   1174: iconst_4
    //   1175: imul
    //   1176: istore 146
    //   1178: iload 145
    //   1180: iload 146
    //   1182: iadd
    //   1183: istore 147
    //   1185: aload_1
    //   1186: bipush 108
    //   1188: iaload
    //   1189: bipush 8
    //   1191: imul
    //   1192: istore 148
    //   1194: iload 147
    //   1196: iload 148
    //   1198: iadd
    //   1199: istore 149
    //   1201: aload 142
    //   1203: iconst_2
    //   1204: iload 149
    //   1206: iastore
    //   1207: aload 5
    //   1209: iconst_0
    //   1210: aaload
    //   1211: astore 150
    //   1213: aload_1
    //   1214: sipush 131
    //   1217: iaload
    //   1218: iconst_1
    //   1219: imul
    //   1220: istore 151
    //   1222: aload_1
    //   1223: sipush 132
    //   1226: iaload
    //   1227: iconst_2
    //   1228: imul
    //   1229: istore 152
    //   1231: iload 151
    //   1233: iload 152
    //   1235: iadd
    //   1236: istore 153
    //   1238: aload_1
    //   1239: sipush 133
    //   1242: iaload
    //   1243: iconst_4
    //   1244: imul
    //   1245: istore 154
    //   1247: iload 153
    //   1249: iload 154
    //   1251: iadd
    //   1252: istore 155
    //   1254: aload_1
    //   1255: bipush 112
    //   1257: iaload
    //   1258: bipush 8
    //   1260: imul
    //   1261: istore 156
    //   1263: iload 155
    //   1265: iload 156
    //   1267: iadd
    //   1268: istore 157
    //   1270: aload 150
    //   1272: iconst_3
    //   1273: iload 157
    //   1275: iastore
    //   1276: aload 5
    //   1278: iconst_0
    //   1279: aaload
    //   1280: astore 158
    //   1282: aload_1
    //   1283: sipush 134
    //   1286: iaload
    //   1287: iconst_1
    //   1288: imul
    //   1289: istore 159
    //   1291: aload_1
    //   1292: sipush 135
    //   1295: iaload
    //   1296: iconst_2
    //   1297: imul
    //   1298: istore 160
    //   1300: iload 159
    //   1302: iload 160
    //   1304: iadd
    //   1305: istore 161
    //   1307: aload_1
    //   1308: sipush 136
    //   1311: iaload
    //   1312: iconst_4
    //   1313: imul
    //   1314: istore 162
    //   1316: iload 161
    //   1318: iload 162
    //   1320: iadd
    //   1321: istore 163
    //   1323: aload_1
    //   1324: bipush 116
    //   1326: iaload
    //   1327: bipush 8
    //   1329: imul
    //   1330: istore 164
    //   1332: iload 163
    //   1334: iload 164
    //   1336: iadd
    //   1337: istore 165
    //   1339: aload 158
    //   1341: iconst_4
    //   1342: iload 165
    //   1344: iastore
    //   1345: aload 5
    //   1347: iconst_0
    //   1348: aaload
    //   1349: astore 166
    //   1351: aload_1
    //   1352: sipush 182
    //   1355: iaload
    //   1356: iconst_1
    //   1357: imul
    //   1358: istore 167
    //   1360: aload_1
    //   1361: sipush 183
    //   1364: iaload
    //   1365: iconst_2
    //   1366: imul
    //   1367: istore 168
    //   1369: iload 167
    //   1371: iload 168
    //   1373: iadd
    //   1374: istore 169
    //   1376: aload_1
    //   1377: sipush 184
    //   1380: iaload
    //   1381: iconst_4
    //   1382: imul
    //   1383: istore 170
    //   1385: iload 169
    //   1387: iload 170
    //   1389: iadd
    //   1390: istore 171
    //   1392: aload 166
    //   1394: iconst_5
    //   1395: iload 171
    //   1397: iastore
    //   1398: aload 5
    //   1400: iconst_0
    //   1401: aaload
    //   1402: astore 172
    //   1404: aload_1
    //   1405: sipush 185
    //   1408: iaload
    //   1409: iconst_1
    //   1410: imul
    //   1411: istore 173
    //   1413: aload_1
    //   1414: sipush 186
    //   1417: iaload
    //   1418: iconst_2
    //   1419: imul
    //   1420: istore 174
    //   1422: iload 173
    //   1424: iload 174
    //   1426: iadd
    //   1427: istore 175
    //   1429: aload_1
    //   1430: sipush 187
    //   1433: iaload
    //   1434: iconst_4
    //   1435: imul
    //   1436: istore 176
    //   1438: iload 175
    //   1440: iload 176
    //   1442: iadd
    //   1443: istore 177
    //   1445: aload 172
    //   1447: bipush 6
    //   1449: iload 177
    //   1451: iastore
    //   1452: aload 5
    //   1454: iconst_0
    //   1455: aaload
    //   1456: astore 178
    //   1458: aload_1
    //   1459: sipush 188
    //   1462: iaload
    //   1463: iconst_1
    //   1464: imul
    //   1465: istore 179
    //   1467: aload_1
    //   1468: sipush 189
    //   1471: iaload
    //   1472: iconst_2
    //   1473: imul
    //   1474: istore 180
    //   1476: iload 179
    //   1478: iload 180
    //   1480: iadd
    //   1481: istore 181
    //   1483: aload_1
    //   1484: sipush 190
    //   1487: iaload
    //   1488: iconst_4
    //   1489: imul
    //   1490: istore 182
    //   1492: iload 181
    //   1494: iload 182
    //   1496: iadd
    //   1497: istore 183
    //   1499: aload 178
    //   1501: bipush 7
    //   1503: iload 183
    //   1505: iastore
    //   1506: aload 5
    //   1508: iconst_0
    //   1509: aaload
    //   1510: astore 184
    //   1512: aload_1
    //   1513: sipush 191
    //   1516: iaload
    //   1517: iconst_1
    //   1518: imul
    //   1519: istore 185
    //   1521: aload_1
    //   1522: sipush 192
    //   1525: iaload
    //   1526: iconst_2
    //   1527: imul
    //   1528: istore 186
    //   1530: iload 185
    //   1532: iload 186
    //   1534: iadd
    //   1535: istore 187
    //   1537: aload_1
    //   1538: sipush 193
    //   1541: iaload
    //   1542: iconst_4
    //   1543: imul
    //   1544: istore 188
    //   1546: iload 187
    //   1548: iload 188
    //   1550: iadd
    //   1551: istore 189
    //   1553: aload 184
    //   1555: bipush 8
    //   1557: iload 189
    //   1559: iastore
    //   1560: aload 5
    //   1562: iconst_0
    //   1563: aaload
    //   1564: astore 190
    //   1566: aload_1
    //   1567: sipush 194
    //   1570: iaload
    //   1571: iconst_1
    //   1572: imul
    //   1573: istore 191
    //   1575: aload_1
    //   1576: sipush 195
    //   1579: iaload
    //   1580: iconst_2
    //   1581: imul
    //   1582: istore 192
    //   1584: iload 191
    //   1586: iload 192
    //   1588: iadd
    //   1589: istore 193
    //   1591: aload_1
    //   1592: sipush 196
    //   1595: iaload
    //   1596: iconst_4
    //   1597: imul
    //   1598: istore 194
    //   1600: iload 193
    //   1602: iload 194
    //   1604: iadd
    //   1605: istore 195
    //   1607: aload 190
    //   1609: bipush 9
    //   1611: iload 195
    //   1613: iastore
    //   1614: aload 5
    //   1616: iconst_1
    //   1617: aaload
    //   1618: astore 196
    //   1620: aload_1
    //   1621: sipush 137
    //   1624: iaload
    //   1625: iconst_1
    //   1626: imul
    //   1627: istore 197
    //   1629: aload_1
    //   1630: sipush 138
    //   1633: iaload
    //   1634: iconst_2
    //   1635: imul
    //   1636: istore 198
    //   1638: iload 197
    //   1640: iload 198
    //   1642: iadd
    //   1643: istore 199
    //   1645: aload_1
    //   1646: sipush 139
    //   1649: iaload
    //   1650: iconst_4
    //   1651: imul
    //   1652: istore 200
    //   1654: iload 199
    //   1656: iload 200
    //   1658: iadd
    //   1659: istore 201
    //   1661: aload_1
    //   1662: bipush 97
    //   1664: iaload
    //   1665: bipush 8
    //   1667: imul
    //   1668: istore 202
    //   1670: iload 201
    //   1672: iload 202
    //   1674: iadd
    //   1675: istore 203
    //   1677: aload 196
    //   1679: iconst_0
    //   1680: iload 203
    //   1682: iastore
    //   1683: aload 5
    //   1685: iconst_1
    //   1686: aaload
    //   1687: astore 204
    //   1689: aload_1
    //   1690: sipush 140
    //   1693: iaload
    //   1694: iconst_1
    //   1695: imul
    //   1696: istore 205
    //   1698: aload_1
    //   1699: sipush 141
    //   1702: iaload
    //   1703: iconst_2
    //   1704: imul
    //   1705: istore 206
    //   1707: iload 205
    //   1709: iload 206
    //   1711: iadd
    //   1712: istore 207
    //   1714: aload_1
    //   1715: sipush 142
    //   1718: iaload
    //   1719: iconst_4
    //   1720: imul
    //   1721: istore 208
    //   1723: iload 207
    //   1725: iload 208
    //   1727: iadd
    //   1728: istore 209
    //   1730: aload_1
    //   1731: bipush 101
    //   1733: iaload
    //   1734: bipush 8
    //   1736: imul
    //   1737: istore 210
    //   1739: iload 209
    //   1741: iload 210
    //   1743: iadd
    //   1744: istore 211
    //   1746: aload 204
    //   1748: iconst_1
    //   1749: iload 211
    //   1751: iastore
    //   1752: aload 5
    //   1754: iconst_1
    //   1755: aaload
    //   1756: astore 212
    //   1758: aload_1
    //   1759: sipush 143
    //   1762: iaload
    //   1763: iconst_1
    //   1764: imul
    //   1765: istore 213
    //   1767: aload_1
    //   1768: sipush 144
    //   1771: iaload
    //   1772: iconst_2
    //   1773: imul
    //   1774: istore 214
    //   1776: iload 213
    //   1778: iload 214
    //   1780: iadd
    //   1781: istore 215
    //   1783: aload_1
    //   1784: sipush 145
    //   1787: iaload
    //   1788: iconst_4
    //   1789: imul
    //   1790: istore 216
    //   1792: iload 215
    //   1794: iload 216
    //   1796: iadd
    //   1797: istore 217
    //   1799: aload_1
    //   1800: bipush 109
    //   1802: iaload
    //   1803: bipush 8
    //   1805: imul
    //   1806: istore 218
    //   1808: iload 217
    //   1810: iload 218
    //   1812: iadd
    //   1813: istore 219
    //   1815: aload 212
    //   1817: iconst_2
    //   1818: iload 219
    //   1820: iastore
    //   1821: aload 5
    //   1823: iconst_1
    //   1824: aaload
    //   1825: astore 220
    //   1827: aload_1
    //   1828: sipush 146
    //   1831: iaload
    //   1832: iconst_1
    //   1833: imul
    //   1834: istore 221
    //   1836: aload_1
    //   1837: sipush 147
    //   1840: iaload
    //   1841: iconst_2
    //   1842: imul
    //   1843: istore 222
    //   1845: iload 221
    //   1847: iload 222
    //   1849: iadd
    //   1850: istore 223
    //   1852: aload_1
    //   1853: sipush 148
    //   1856: iaload
    //   1857: iconst_4
    //   1858: imul
    //   1859: istore 224
    //   1861: iload 223
    //   1863: iload 224
    //   1865: iadd
    //   1866: istore 225
    //   1868: aload_1
    //   1869: bipush 113
    //   1871: iaload
    //   1872: bipush 8
    //   1874: imul
    //   1875: istore 226
    //   1877: iload 225
    //   1879: iload 226
    //   1881: iadd
    //   1882: istore 227
    //   1884: aload 220
    //   1886: iconst_3
    //   1887: iload 227
    //   1889: iastore
    //   1890: aload 5
    //   1892: iconst_1
    //   1893: aaload
    //   1894: astore 228
    //   1896: aload_1
    //   1897: sipush 149
    //   1900: iaload
    //   1901: iconst_1
    //   1902: imul
    //   1903: istore 229
    //   1905: aload_1
    //   1906: sipush 150
    //   1909: iaload
    //   1910: iconst_2
    //   1911: imul
    //   1912: istore 230
    //   1914: iload 229
    //   1916: iload 230
    //   1918: iadd
    //   1919: istore 231
    //   1921: aload_1
    //   1922: sipush 151
    //   1925: iaload
    //   1926: iconst_4
    //   1927: imul
    //   1928: istore 232
    //   1930: iload 231
    //   1932: iload 232
    //   1934: iadd
    //   1935: istore 233
    //   1937: aload_1
    //   1938: bipush 117
    //   1940: iaload
    //   1941: bipush 8
    //   1943: imul
    //   1944: istore 234
    //   1946: iload 233
    //   1948: iload 234
    //   1950: iadd
    //   1951: istore 235
    //   1953: aload 228
    //   1955: iconst_4
    //   1956: iload 235
    //   1958: iastore
    //   1959: aload 5
    //   1961: iconst_1
    //   1962: aaload
    //   1963: astore 236
    //   1965: aload_1
    //   1966: sipush 197
    //   1969: iaload
    //   1970: iconst_1
    //   1971: imul
    //   1972: istore 237
    //   1974: aload_1
    //   1975: sipush 198
    //   1978: iaload
    //   1979: iconst_2
    //   1980: imul
    //   1981: istore 238
    //   1983: iload 237
    //   1985: iload 238
    //   1987: iadd
    //   1988: istore 239
    //   1990: aload_1
    //   1991: sipush 199
    //   1994: iaload
    //   1995: iconst_4
    //   1996: imul
    //   1997: istore 240
    //   1999: iload 239
    //   2001: iload 240
    //   2003: iadd
    //   2004: istore 241
    //   2006: aload 236
    //   2008: iconst_5
    //   2009: iload 241
    //   2011: iastore
    //   2012: aload 5
    //   2014: iconst_1
    //   2015: aaload
    //   2016: astore 242
    //   2018: aload_1
    //   2019: sipush 200
    //   2022: iaload
    //   2023: iconst_1
    //   2024: imul
    //   2025: istore 243
    //   2027: aload_1
    //   2028: sipush 201
    //   2031: iaload
    //   2032: iconst_2
    //   2033: imul
    //   2034: istore 244
    //   2036: iload 243
    //   2038: iload 244
    //   2040: iadd
    //   2041: istore 245
    //   2043: aload_1
    //   2044: sipush 202
    //   2047: iaload
    //   2048: iconst_4
    //   2049: imul
    //   2050: istore 246
    //   2052: iload 245
    //   2054: iload 246
    //   2056: iadd
    //   2057: istore 247
    //   2059: aload 242
    //   2061: bipush 6
    //   2063: iload 247
    //   2065: iastore
    //   2066: aload 5
    //   2068: iconst_1
    //   2069: aaload
    //   2070: astore 248
    //   2072: aload_1
    //   2073: sipush 203
    //   2076: iaload
    //   2077: iconst_1
    //   2078: imul
    //   2079: istore 249
    //   2081: aload_1
    //   2082: sipush 204
    //   2085: iaload
    //   2086: iconst_2
    //   2087: imul
    //   2088: istore 250
    //   2090: iload 249
    //   2092: iload 250
    //   2094: iadd
    //   2095: istore 251
    //   2097: aload_1
    //   2098: sipush 205
    //   2101: iaload
    //   2102: iconst_4
    //   2103: imul
    //   2104: istore 252
    //   2106: iload 251
    //   2108: iload 252
    //   2110: iadd
    //   2111: istore 253
    //   2113: aload 248
    //   2115: bipush 7
    //   2117: iload 253
    //   2119: iastore
    //   2120: aload 5
    //   2122: iconst_1
    //   2123: aaload
    //   2124: astore 254
    //   2126: aload_1
    //   2127: sipush 206
    //   2130: iaload
    //   2131: iconst_1
    //   2132: imul
    //   2133: istore 255
    //   2135: aload_1
    //   2136: sipush 207
    //   2139: iaload
    //   2140: iconst_2
    //   2141: imul
    //   2142: wide
    //   2146: iload 255
    //   2148: wide
    //   2152: iadd
    //   2153: wide
    //   2157: aload_1
    //   2158: sipush 208
    //   2161: iaload
    //   2162: iconst_4
    //   2163: imul
    //   2164: wide
    //   2168: wide
    //   2172: wide
    //   2176: iadd
    //   2177: wide
    //   2181: aload 254
    //   2183: bipush 8
    //   2185: wide
    //   2189: iastore
    //   2190: aload 5
    //   2192: iconst_1
    //   2193: aaload
    //   2194: wide
    //   2198: aload_1
    //   2199: sipush 209
    //   2202: iaload
    //   2203: iconst_1
    //   2204: imul
    //   2205: wide
    //   2209: aload_1
    //   2210: sipush 210
    //   2213: iaload
    //   2214: iconst_2
    //   2215: imul
    //   2216: wide
    //   2220: wide
    //   2224: wide
    //   2228: iadd
    //   2229: wide
    //   2233: aload_1
    //   2234: sipush 211
    //   2237: iaload
    //   2238: iconst_4
    //   2239: imul
    //   2240: wide
    //   2244: wide
    //   2248: wide
    //   2252: iadd
    //   2253: wide
    //   2257: wide
    //   2261: bipush 9
    //   2263: wide
    //   2267: iastore
    //   2268: aload 5
    //   2270: iconst_2
    //   2271: aaload
    //   2272: wide
    //   2276: aload_1
    //   2277: sipush 152
    //   2280: iaload
    //   2281: iconst_1
    //   2282: imul
    //   2283: wide
    //   2287: aload_1
    //   2288: sipush 153
    //   2291: iaload
    //   2292: iconst_2
    //   2293: imul
    //   2294: wide
    //   2298: wide
    //   2302: wide
    //   2306: iadd
    //   2307: wide
    //   2311: aload_1
    //   2312: sipush 154
    //   2315: iaload
    //   2316: iconst_4
    //   2317: imul
    //   2318: wide
    //   2322: wide
    //   2326: wide
    //   2330: iadd
    //   2331: wide
    //   2335: aload_1
    //   2336: bipush 98
    //   2338: iaload
    //   2339: bipush 8
    //   2341: imul
    //   2342: wide
    //   2346: wide
    //   2350: wide
    //   2354: iadd
    //   2355: wide
    //   2359: wide
    //   2363: iconst_0
    //   2364: wide
    //   2368: iastore
    //   2369: aload 5
    //   2371: iconst_2
    //   2372: aaload
    //   2373: wide
    //   2377: aload_1
    //   2378: sipush 155
    //   2381: iaload
    //   2382: iconst_1
    //   2383: imul
    //   2384: wide
    //   2388: aload_1
    //   2389: sipush 156
    //   2392: iaload
    //   2393: iconst_2
    //   2394: imul
    //   2395: wide
    //   2399: wide
    //   2403: wide
    //   2407: iadd
    //   2408: wide
    //   2412: aload_1
    //   2413: sipush 157
    //   2416: iaload
    //   2417: iconst_4
    //   2418: imul
    //   2419: wide
    //   2423: wide
    //   2427: wide
    //   2431: iadd
    //   2432: wide
    //   2436: aload_1
    //   2437: bipush 102
    //   2439: iaload
    //   2440: bipush 8
    //   2442: imul
    //   2443: wide
    //   2447: wide
    //   2451: wide
    //   2455: iadd
    //   2456: wide
    //   2460: wide
    //   2464: iconst_1
    //   2465: wide
    //   2469: iastore
    //   2470: aload 5
    //   2472: iconst_2
    //   2473: aaload
    //   2474: wide
    //   2478: aload_1
    //   2479: sipush 158
    //   2482: iaload
    //   2483: iconst_1
    //   2484: imul
    //   2485: wide
    //   2489: aload_1
    //   2490: sipush 159
    //   2493: iaload
    //   2494: iconst_2
    //   2495: imul
    //   2496: wide
    //   2500: wide
    //   2504: wide
    //   2508: iadd
    //   2509: wide
    //   2513: aload_1
    //   2514: sipush 160
    //   2517: iaload
    //   2518: iconst_4
    //   2519: imul
    //   2520: wide
    //   2524: wide
    //   2528: wide
    //   2532: iadd
    //   2533: wide
    //   2537: aload_1
    //   2538: bipush 110
    //   2540: iaload
    //   2541: bipush 8
    //   2543: imul
    //   2544: wide
    //   2548: wide
    //   2552: wide
    //   2556: iadd
    //   2557: wide
    //   2561: wide
    //   2565: iconst_2
    //   2566: wide
    //   2570: iastore
    //   2571: aload 5
    //   2573: iconst_2
    //   2574: aaload
    //   2575: wide
    //   2579: aload_1
    //   2580: sipush 161
    //   2583: iaload
    //   2584: iconst_1
    //   2585: imul
    //   2586: wide
    //   2590: aload_1
    //   2591: sipush 162
    //   2594: iaload
    //   2595: iconst_2
    //   2596: imul
    //   2597: wide
    //   2601: wide
    //   2605: wide
    //   2609: iadd
    //   2610: wide
    //   2614: aload_1
    //   2615: sipush 163
    //   2618: iaload
    //   2619: iconst_4
    //   2620: imul
    //   2621: wide
    //   2625: wide
    //   2629: wide
    //   2633: iadd
    //   2634: wide
    //   2638: aload_1
    //   2639: bipush 114
    //   2641: iaload
    //   2642: bipush 8
    //   2644: imul
    //   2645: wide
    //   2649: wide
    //   2653: wide
    //   2657: iadd
    //   2658: wide
    //   2662: wide
    //   2666: iconst_3
    //   2667: wide
    //   2671: iastore
    //   2672: aload 5
    //   2674: iconst_2
    //   2675: aaload
    //   2676: wide
    //   2680: aload_1
    //   2681: sipush 164
    //   2684: iaload
    //   2685: iconst_1
    //   2686: imul
    //   2687: wide
    //   2691: aload_1
    //   2692: sipush 165
    //   2695: iaload
    //   2696: iconst_2
    //   2697: imul
    //   2698: wide
    //   2702: wide
    //   2706: wide
    //   2710: iadd
    //   2711: wide
    //   2715: aload_1
    //   2716: sipush 166
    //   2719: iaload
    //   2720: iconst_4
    //   2721: imul
    //   2722: wide
    //   2726: wide
    //   2730: wide
    //   2734: iadd
    //   2735: wide
    //   2739: aload_1
    //   2740: bipush 118
    //   2742: iaload
    //   2743: bipush 8
    //   2745: imul
    //   2746: wide
    //   2750: wide
    //   2754: wide
    //   2758: iadd
    //   2759: wide
    //   2763: wide
    //   2767: iconst_4
    //   2768: wide
    //   2772: iastore
    //   2773: aload 5
    //   2775: iconst_2
    //   2776: aaload
    //   2777: wide
    //   2781: aload_1
    //   2782: sipush 212
    //   2785: iaload
    //   2786: iconst_1
    //   2787: imul
    //   2788: wide
    //   2792: aload_1
    //   2793: sipush 213
    //   2796: iaload
    //   2797: iconst_2
    //   2798: imul
    //   2799: wide
    //   2803: wide
    //   2807: wide
    //   2811: iadd
    //   2812: wide
    //   2816: aload_1
    //   2817: sipush 214
    //   2820: iaload
    //   2821: iconst_4
    //   2822: imul
    //   2823: wide
    //   2827: wide
    //   2831: wide
    //   2835: iadd
    //   2836: wide
    //   2840: wide
    //   2844: iconst_5
    //   2845: wide
    //   2849: iastore
    //   2850: aload 5
    //   2852: iconst_2
    //   2853: aaload
    //   2854: wide
    //   2858: aload_1
    //   2859: sipush 215
    //   2862: iaload
    //   2863: iconst_1
    //   2864: imul
    //   2865: wide
    //   2869: aload_1
    //   2870: sipush 216
    //   2873: iaload
    //   2874: iconst_2
    //   2875: imul
    //   2876: wide
    //   2880: wide
    //   2884: wide
    //   2888: iadd
    //   2889: wide
    //   2893: aload_1
    //   2894: sipush 217
    //   2897: iaload
    //   2898: iconst_4
    //   2899: imul
    //   2900: wide
    //   2904: wide
    //   2908: wide
    //   2912: iadd
    //   2913: wide
    //   2917: wide
    //   2921: bipush 6
    //   2923: wide
    //   2927: iastore
    //   2928: aload 5
    //   2930: iconst_2
    //   2931: aaload
    //   2932: wide
    //   2936: aload_1
    //   2937: sipush 218
    //   2940: iaload
    //   2941: iconst_1
    //   2942: imul
    //   2943: wide
    //   2947: aload_1
    //   2948: sipush 219
    //   2951: iaload
    //   2952: iconst_2
    //   2953: imul
    //   2954: wide
    //   2958: wide
    //   2962: wide
    //   2966: iadd
    //   2967: wide
    //   2971: aload_1
    //   2972: sipush 220
    //   2975: iaload
    //   2976: iconst_4
    //   2977: imul
    //   2978: wide
    //   2982: wide
    //   2986: wide
    //   2990: iadd
    //   2991: wide
    //   2995: wide
    //   2999: bipush 7
    //   3001: wide
    //   3005: iastore
    //   3006: aload 5
    //   3008: iconst_2
    //   3009: aaload
    //   3010: wide
    //   3014: aload_1
    //   3015: sipush 221
    //   3018: iaload
    //   3019: iconst_1
    //   3020: imul
    //   3021: wide
    //   3025: aload_1
    //   3026: sipush 222
    //   3029: iaload
    //   3030: iconst_2
    //   3031: imul
    //   3032: wide
    //   3036: wide
    //   3040: wide
    //   3044: iadd
    //   3045: wide
    //   3049: aload_1
    //   3050: sipush 223
    //   3053: iaload
    //   3054: iconst_4
    //   3055: imul
    //   3056: wide
    //   3060: wide
    //   3064: wide
    //   3068: iadd
    //   3069: wide
    //   3073: wide
    //   3077: bipush 8
    //   3079: wide
    //   3083: iastore
    //   3084: aload 5
    //   3086: iconst_2
    //   3087: aaload
    //   3088: wide
    //   3092: aload_1
    //   3093: sipush 224
    //   3096: iaload
    //   3097: iconst_1
    //   3098: imul
    //   3099: wide
    //   3103: aload_1
    //   3104: sipush 225
    //   3107: iaload
    //   3108: iconst_2
    //   3109: imul
    //   3110: wide
    //   3114: wide
    //   3118: wide
    //   3122: iadd
    //   3123: wide
    //   3127: aload_1
    //   3128: sipush 226
    //   3131: iaload
    //   3132: iconst_4
    //   3133: imul
    //   3134: wide
    //   3138: wide
    //   3142: wide
    //   3146: iadd
    //   3147: wide
    //   3151: wide
    //   3155: bipush 9
    //   3157: wide
    //   3161: iastore
    //   3162: aload 5
    //   3164: iconst_3
    //   3165: aaload
    //   3166: wide
    //   3170: aload_1
    //   3171: sipush 167
    //   3174: iaload
    //   3175: iconst_1
    //   3176: imul
    //   3177: wide
    //   3181: aload_1
    //   3182: sipush 168
    //   3185: iaload
    //   3186: iconst_2
    //   3187: imul
    //   3188: wide
    //   3192: wide
    //   3196: wide
    //   3200: iadd
    //   3201: wide
    //   3205: aload_1
    //   3206: sipush 169
    //   3209: iaload
    //   3210: iconst_4
    //   3211: imul
    //   3212: wide
    //   3216: wide
    //   3220: wide
    //   3224: iadd
    //   3225: wide
    //   3229: aload_1
    //   3230: bipush 99
    //   3232: iaload
    //   3233: bipush 8
    //   3235: imul
    //   3236: wide
    //   3240: wide
    //   3244: wide
    //   3248: iadd
    //   3249: wide
    //   3253: wide
    //   3257: iconst_0
    //   3258: wide
    //   3262: iastore
    //   3263: aload 5
    //   3265: iconst_3
    //   3266: aaload
    //   3267: wide
    //   3271: aload_1
    //   3272: sipush 170
    //   3275: iaload
    //   3276: iconst_1
    //   3277: imul
    //   3278: wide
    //   3282: aload_1
    //   3283: sipush 171
    //   3286: iaload
    //   3287: iconst_2
    //   3288: imul
    //   3289: wide
    //   3293: wide
    //   3297: wide
    //   3301: iadd
    //   3302: wide
    //   3306: aload_1
    //   3307: sipush 172
    //   3310: iaload
    //   3311: iconst_4
    //   3312: imul
    //   3313: wide
    //   3317: wide
    //   3321: wide
    //   3325: iadd
    //   3326: wide
    //   3330: aload_1
    //   3331: bipush 103
    //   3333: iaload
    //   3334: bipush 8
    //   3336: imul
    //   3337: wide
    //   3341: wide
    //   3345: wide
    //   3349: iadd
    //   3350: wide
    //   3354: wide
    //   3358: iconst_1
    //   3359: wide
    //   3363: iastore
    //   3364: aload 5
    //   3366: iconst_3
    //   3367: aaload
    //   3368: wide
    //   3372: aload_1
    //   3373: sipush 173
    //   3376: iaload
    //   3377: iconst_1
    //   3378: imul
    //   3379: wide
    //   3383: aload_1
    //   3384: sipush 174
    //   3387: iaload
    //   3388: iconst_2
    //   3389: imul
    //   3390: wide
    //   3394: wide
    //   3398: wide
    //   3402: iadd
    //   3403: wide
    //   3407: aload_1
    //   3408: sipush 175
    //   3411: iaload
    //   3412: iconst_4
    //   3413: imul
    //   3414: wide
    //   3418: wide
    //   3422: wide
    //   3426: iadd
    //   3427: wide
    //   3431: aload_1
    //   3432: bipush 111
    //   3434: iaload
    //   3435: bipush 8
    //   3437: imul
    //   3438: wide
    //   3442: wide
    //   3446: wide
    //   3450: iadd
    //   3451: wide
    //   3455: wide
    //   3459: iconst_2
    //   3460: wide
    //   3464: iastore
    //   3465: aload 5
    //   3467: iconst_3
    //   3468: aaload
    //   3469: wide
    //   3473: aload_1
    //   3474: sipush 176
    //   3477: iaload
    //   3478: iconst_1
    //   3479: imul
    //   3480: wide
    //   3484: aload_1
    //   3485: sipush 177
    //   3488: iaload
    //   3489: iconst_2
    //   3490: imul
    //   3491: wide
    //   3495: wide
    //   3499: wide
    //   3503: iadd
    //   3504: wide
    //   3508: aload_1
    //   3509: sipush 178
    //   3512: iaload
    //   3513: iconst_4
    //   3514: imul
    //   3515: wide
    //   3519: wide
    //   3523: wide
    //   3527: iadd
    //   3528: wide
    //   3532: aload_1
    //   3533: bipush 115
    //   3535: iaload
    //   3536: bipush 8
    //   3538: imul
    //   3539: wide
    //   3543: wide
    //   3547: wide
    //   3551: iadd
    //   3552: wide
    //   3556: wide
    //   3560: iconst_3
    //   3561: wide
    //   3565: iastore
    //   3566: aload 5
    //   3568: iconst_3
    //   3569: aaload
    //   3570: wide
    //   3574: aload_1
    //   3575: sipush 179
    //   3578: iaload
    //   3579: iconst_1
    //   3580: imul
    //   3581: wide
    //   3585: aload_1
    //   3586: sipush 180
    //   3589: iaload
    //   3590: iconst_2
    //   3591: imul
    //   3592: wide
    //   3596: wide
    //   3600: wide
    //   3604: iadd
    //   3605: wide
    //   3609: aload_1
    //   3610: sipush 181
    //   3613: iaload
    //   3614: iconst_4
    //   3615: imul
    //   3616: wide
    //   3620: wide
    //   3624: wide
    //   3628: iadd
    //   3629: wide
    //   3633: aload_1
    //   3634: bipush 119
    //   3636: iaload
    //   3637: bipush 8
    //   3639: imul
    //   3640: wide
    //   3644: wide
    //   3648: wide
    //   3652: iadd
    //   3653: wide
    //   3657: wide
    //   3661: iconst_4
    //   3662: wide
    //   3666: iastore
    //   3667: aload 5
    //   3669: iconst_3
    //   3670: aaload
    //   3671: wide
    //   3675: aload_1
    //   3676: sipush 227
    //   3679: iaload
    //   3680: iconst_1
    //   3681: imul
    //   3682: wide
    //   3686: aload_1
    //   3687: sipush 228
    //   3690: iaload
    //   3691: iconst_2
    //   3692: imul
    //   3693: wide
    //   3697: wide
    //   3701: wide
    //   3705: iadd
    //   3706: wide
    //   3710: aload_1
    //   3711: sipush 229
    //   3714: iaload
    //   3715: iconst_4
    //   3716: imul
    //   3717: wide
    //   3721: wide
    //   3725: wide
    //   3729: iadd
    //   3730: wide
    //   3734: wide
    //   3738: iconst_5
    //   3739: wide
    //   3743: iastore
    //   3744: aload 5
    //   3746: iconst_3
    //   3747: aaload
    //   3748: wide
    //   3752: aload_1
    //   3753: sipush 230
    //   3756: iaload
    //   3757: iconst_1
    //   3758: imul
    //   3759: wide
    //   3763: aload_1
    //   3764: sipush 231
    //   3767: iaload
    //   3768: iconst_2
    //   3769: imul
    //   3770: wide
    //   3774: wide
    //   3778: wide
    //   3782: iadd
    //   3783: wide
    //   3787: aload_1
    //   3788: sipush 232
    //   3791: iaload
    //   3792: iconst_4
    //   3793: imul
    //   3794: wide
    //   3798: wide
    //   3802: wide
    //   3806: iadd
    //   3807: wide
    //   3811: wide
    //   3815: bipush 6
    //   3817: wide
    //   3821: iastore
    //   3822: aload 5
    //   3824: iconst_3
    //   3825: aaload
    //   3826: wide
    //   3830: aload_1
    //   3831: sipush 233
    //   3834: iaload
    //   3835: iconst_1
    //   3836: imul
    //   3837: wide
    //   3841: aload_1
    //   3842: sipush 234
    //   3845: iaload
    //   3846: iconst_2
    //   3847: imul
    //   3848: wide
    //   3852: wide
    //   3856: wide
    //   3860: iadd
    //   3861: wide
    //   3865: aload_1
    //   3866: sipush 235
    //   3869: iaload
    //   3870: iconst_4
    //   3871: imul
    //   3872: wide
    //   3876: wide
    //   3880: wide
    //   3884: iadd
    //   3885: wide
    //   3889: wide
    //   3893: bipush 7
    //   3895: wide
    //   3899: iastore
    //   3900: aload 5
    //   3902: iconst_3
    //   3903: aaload
    //   3904: wide
    //   3908: aload_1
    //   3909: sipush 236
    //   3912: iaload
    //   3913: iconst_1
    //   3914: imul
    //   3915: wide
    //   3919: aload_1
    //   3920: sipush 237
    //   3923: iaload
    //   3924: iconst_2
    //   3925: imul
    //   3926: wide
    //   3930: wide
    //   3934: wide
    //   3938: iadd
    //   3939: wide
    //   3943: aload_1
    //   3944: sipush 238
    //   3947: iaload
    //   3948: iconst_4
    //   3949: imul
    //   3950: wide
    //   3954: wide
    //   3958: wide
    //   3962: iadd
    //   3963: wide
    //   3967: wide
    //   3971: bipush 8
    //   3973: wide
    //   3977: iastore
    //   3978: aload 5
    //   3980: iconst_3
    //   3981: aaload
    //   3982: wide
    //   3986: aload_1
    //   3987: sipush 239
    //   3990: iaload
    //   3991: iconst_1
    //   3992: imul
    //   3993: wide
    //   3997: aload_1
    //   3998: sipush 240
    //   4001: iaload
    //   4002: iconst_2
    //   4003: imul
    //   4004: wide
    //   4008: wide
    //   4012: wide
    //   4016: iadd
    //   4017: wide
    //   4021: aload_1
    //   4022: sipush 241
    //   4025: iaload
    //   4026: iconst_4
    //   4027: imul
    //   4028: wide
    //   4032: wide
    //   4036: wide
    //   4040: iadd
    //   4041: wide
    //   4045: wide
    //   4049: bipush 9
    //   4051: wide
    //   4055: iastore
    //   4056: return
  }

  public int getNumFrames()
  {
    return this.mNumFrames;
  }

  public int getSampleRate()
  {
    return 8000;
  }

  public int getSamplesPerFrame()
  {
    return 40;
  }

  void parseAMR(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    int[] arrayOfInt1 = new int[4];
    int i = 0;
    while (i < 4)
    {
      arrayOfInt1[i] = 0;
      i += 1;
    }
    int[] arrayOfInt2 = new int[4];
    int j = 0;
    while (j < 4)
    {
      arrayOfInt2[j] = 63155;
      j += 1;
    }
    int k = paramInt;
    int m = 0;
    CheapSoundFile.ProgressListener localProgressListener;
    double d3;
    do
    {
      do
      {
        if (paramInt <= 0)
          return;
        int n = parseAMRFrame(paramInputStream, paramInt, arrayOfInt1);
        m += n;
        paramInt -= n;
      }
      while (this.mProgressListener == null);
      localProgressListener = this.mProgressListener;
      double d1 = m * 1.0D;
      double d2 = k;
      d3 = d1 / d2;
    }
    while (localProgressListener.reportProgress(d3));
  }

  int parseAMRFrame(InputStream paramInputStream, int paramInt, int[] paramArrayOfInt)
    throws IOException
  {
    int i = this.mOffset;
    byte[] arrayOfByte1 = new byte[1];
    InputStream localInputStream1 = paramInputStream;
    byte[] arrayOfByte2 = arrayOfByte1;
    int j = 1;
    int k = localInputStream1.read(arrayOfByte2, 0, j);
    int m = this.mOffset + 1;
    this.mOffset = m;
    int n = ((arrayOfByte1[0] & 0xFF) >> 3) % 15;
    int i1 = (arrayOfByte1[0] & 0xFF) >> 2 & 0x1;
    int i2 = BLOCK_SIZES[n];
    int i3 = i2 + 1;
    int i4 = paramInt;
    if (i3 > i4);
    while (true)
    {
      return paramInt;
      if (i2 != 0)
        break;
      paramInt = 1;
    }
    byte[] arrayOfByte3 = new byte[i2];
    InputStream localInputStream2 = paramInputStream;
    byte[] arrayOfByte4 = arrayOfByte3;
    int i5 = localInputStream2.read(arrayOfByte4, 0, i2);
    int i6 = this.mOffset + i2;
    this.mOffset = i6;
    int[] arrayOfInt1 = new int[i2 * 8];
    int i7 = 0;
    int i8 = arrayOfByte3[i7] & 0xFF;
    int i9 = 0;
    while (true)
    {
      int i10 = i2 * 8;
      if (i9 >= i10)
        break;
      int i11 = (i8 & 0x80) >> 7;
      arrayOfInt1[i9] = i11;
      i8 <<= 1;
      int i12 = i9 & 0x7;
      int i13 = 7;
      if (i12 == i13)
      {
        int i14 = i2 * 8 + -1;
        if (i9 < i14)
        {
          int i15 = i7 + 1;
          int i16 = arrayOfByte3[i15] & 0xFF;
        }
      }
      i9 += 1;
    }
    switch (n)
    {
    default:
      PrintStream localPrintStream = System.out;
      StringBuilder localStringBuilder = new StringBuilder().append("Unsupported frame type: ");
      int i17 = n;
      String str = i17;
      localPrintStream.println(str);
      int i18 = i2 + 1;
      CheapAMR localCheapAMR1 = this;
      int i19 = i;
      int i20 = 1;
      localCheapAMR1.addFrame(i19, i18, i20);
    case 0:
    case 1:
    case 7:
    }
    int[] arrayOfInt5;
    int[] arrayOfInt6;
    int[] arrayOfInt7;
    int[] arrayOfInt8;
    int i141;
    int i142;
    do
    {
      paramInt = i2 + 1;
      break;
      this.mBitRate = 5;
      int[] arrayOfInt2 = new int[4];
      int i21 = arrayOfInt1[28] * 1;
      int i22 = arrayOfInt1[29] * 2;
      int i23 = i21 + i22;
      int i24 = arrayOfInt1[30] * 4;
      int i25 = i23 + i24;
      int i26 = arrayOfInt1[31] * 8;
      int i27 = i25 + i26;
      int i28 = arrayOfInt1[46] * 16;
      int i29 = i27 + i28;
      int i30 = arrayOfInt1[47] * 32;
      int i31 = i29 + i30;
      int i32 = arrayOfInt1[48] * 64;
      int i33 = i31 + i32;
      int i34 = arrayOfInt1[49] * 128;
      int i35 = i33 + i34;
      arrayOfInt2[0] = i35;
      int i36 = arrayOfInt2[0];
      arrayOfInt2[1] = i36;
      int i37 = arrayOfInt1[32] * 1;
      int i38 = arrayOfInt1[33] * 2;
      int i39 = i37 + i38;
      int i40 = arrayOfInt1[34] * 4;
      int i41 = i39 + i40;
      int i42 = arrayOfInt1[35] * 8;
      int i43 = i41 + i42;
      int i44 = arrayOfInt1[40] * 16;
      int i45 = i43 + i44;
      int i46 = arrayOfInt1[41] * 32;
      int i47 = i45 + i46;
      int i48 = arrayOfInt1[42] * 64;
      int i49 = i47 + i48;
      int i50 = arrayOfInt1[43] * 128;
      int i51 = i49 + i50;
      arrayOfInt2[2] = i51;
      int i52 = arrayOfInt2[2];
      arrayOfInt2[3] = i52;
      i9 = 0;
      while (i9 < 4)
      {
        int i53 = arrayOfInt2[i9] * 4;
        int i54 = (i9 & 0x1) * 2;
        int i55 = i53 + i54 + 1;
        int i56 = GAIN_FAC_MR475[i55];
        double d1 = Math.log(i56);
        double d2 = Math.log(2.0D);
        double d3 = d1 / d2;
        int i57 = (int)d3;
        double d4 = i57;
        int i58 = (int)((d3 - d4) * 0.0F);
        int i59 = (i57 + -12) * 49320;
        int i60 = (i58 * 24660 >> 15) * 2;
        int i61 = (i59 + i60) * 8192 + 32768 >> 16;
        int i62 = paramArrayOfInt[0] * 5571;
        int i63 = 385963008 + i62;
        int i64 = paramArrayOfInt[1] * 4751;
        int i65 = i63 + i64;
        int i66 = paramArrayOfInt[2] * 2785;
        int i67 = i65 + i66;
        int i68 = paramArrayOfInt[3] * 1556;
        int i69 = i67 + i68 >> 15;
        int i70 = paramArrayOfInt[2];
        paramArrayOfInt[3] = i70;
        int i71 = paramArrayOfInt[1];
        paramArrayOfInt[2] = i71;
        int i72 = paramArrayOfInt[0];
        paramArrayOfInt[1] = i72;
        paramArrayOfInt[0] = i61;
        int i73 = i69 * i56 >> 24;
        int i74 = i2 + 1;
        CheapAMR localCheapAMR2 = this;
        int i75 = i;
        int i76 = i73;
        localCheapAMR2.addFrame(i75, i74, i76);
        i9 += 1;
      }
      this.mBitRate = 5;
      arrayOfInt2 = new int[4];
      int i77 = arrayOfInt1[24] * 1;
      int i78 = arrayOfInt1[25] * 2;
      int i79 = i77 + i78;
      int i80 = arrayOfInt1[26] * 4;
      int i81 = i79 + i80;
      int i82 = arrayOfInt1[36] * 8;
      int i83 = i81 + i82;
      int i84 = arrayOfInt1[45] * 16;
      int i85 = i83 + i84;
      int i86 = arrayOfInt1[55] * 32;
      int i87 = i85 + i86;
      arrayOfInt2[0] = i87;
      int i88 = arrayOfInt1[27] * 1;
      int i89 = arrayOfInt1[28] * 2;
      int i90 = i88 + i89;
      int i91 = arrayOfInt1[29] * 4;
      int i92 = i90 + i91;
      int i93 = arrayOfInt1[37] * 8;
      int i94 = i92 + i93;
      int i95 = arrayOfInt1[46] * 16;
      int i96 = i94 + i95;
      int i97 = arrayOfInt1[56] * 32;
      int i98 = i96 + i97;
      arrayOfInt2[1] = i98;
      int i99 = arrayOfInt1[30] * 1;
      int i100 = arrayOfInt1[31] * 2;
      int i101 = i99 + i100;
      int i102 = arrayOfInt1[32] * 4;
      int i103 = i101 + i102;
      int i104 = arrayOfInt1[38] * 8;
      int i105 = i103 + i104;
      int i106 = arrayOfInt1[47] * 16;
      int i107 = i105 + i106;
      int i108 = arrayOfInt1[57] * 32;
      int i109 = i107 + i108;
      arrayOfInt2[2] = i109;
      int i110 = arrayOfInt1[33] * 1;
      int i111 = arrayOfInt1[34] * 2;
      int i112 = i110 + i111;
      int i113 = arrayOfInt1[35] * 4;
      int i114 = i112 + i113;
      int i115 = arrayOfInt1[39] * 8;
      int i116 = i114 + i115;
      int i117 = arrayOfInt1[48] * 16;
      int i118 = i116 + i117;
      int i119 = arrayOfInt1[58] * 32;
      int i120 = i118 + i119;
      arrayOfInt2[3] = i120;
      i9 = 0;
      while (i9 < 4)
      {
        int i121 = paramArrayOfInt[0] * 5571;
        int i122 = 385963008 + i121;
        int i123 = paramArrayOfInt[1] * 4751;
        int i124 = i122 + i123;
        int i125 = paramArrayOfInt[2] * 2785;
        int i126 = i124 + i125;
        int i127 = paramArrayOfInt[3] * 1556;
        int i128 = i126 + i127 >> 15;
        int[] arrayOfInt3 = QUA_ENER_MR515;
        int i129 = arrayOfInt2[i9];
        int i130 = arrayOfInt3[i129];
        int[] arrayOfInt4 = GAIN_FAC_MR515;
        int i131 = arrayOfInt2[i9];
        int i132 = arrayOfInt4[i131];
        int i133 = paramArrayOfInt[2];
        paramArrayOfInt[3] = i133;
        int i134 = paramArrayOfInt[1];
        paramArrayOfInt[2] = i134;
        int i135 = paramArrayOfInt[0];
        paramArrayOfInt[1] = i135;
        paramArrayOfInt[0] = i130;
        int i136 = i128 * i132 >> 24;
        int i137 = i2 + 1;
        CheapAMR localCheapAMR3 = this;
        int i138 = i;
        int i139 = i136;
        localCheapAMR3.addFrame(i138, i137, i139);
        i9 += 1;
      }
      this.mBitRate = 12;
      arrayOfInt5 = new int[4];
      arrayOfInt6 = new int[4];
      arrayOfInt7 = new int[4];
      arrayOfInt8 = new int[4];
      i9 = 0;
      while (i9 < 4)
      {
        int[] arrayOfInt9 = new int[10];
        arrayOfInt8[i9] = arrayOfInt9;
        int i140 = i9 + 1;
      }
      getMR122Params(arrayOfInt1, arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfInt8);
      i141 = 0;
      i142 = 0;
    }
    while (i142 >= 4);
    int[] arrayOfInt10 = new int[40];
    i9 = 0;
    while (i9 < 40)
    {
      arrayOfInt10[i9] = 0;
      int i143 = i9 + 1;
    }
    int i144 = 0;
    if (i144 < 5)
    {
      if ((arrayOfInt8[i142][i144] >> 3 & 0x1) == 0);
      for (int i145 = 4096; ; i145 = 61440)
      {
        int[] arrayOfInt11 = GRAY;
        int i146 = arrayOfInt8[i142][i144] & 0x7;
        int i147 = arrayOfInt11[i146] * 5;
        int i148 = i144 + i147;
        int[] arrayOfInt12 = GRAY;
        int i149 = arrayOfInt8[i142];
        int i150 = i144 + 5;
        int i151 = i149[i150] & 0x7;
        int i152 = arrayOfInt12[i151] * 5;
        int i153 = i144 + i152;
        arrayOfInt10[i148] = i145;
        int i154 = i153;
        int i155 = i148;
        if (i154 < i155)
          i145 = -i145;
        int i156 = arrayOfInt10[i153] + i145;
        arrayOfInt10[i153] = i156;
        i144 += 1;
        break;
      }
    }
    int i157 = arrayOfInt5[i142];
    int i159;
    if ((i142 == 0) || (i142 == 2))
      if (i157 < 463)
      {
        i141 = (i157 + 5) / 6 + 17;
        int[] arrayOfInt13 = QUA_GAIN_PITCH;
        int i158 = arrayOfInt6[i142];
        i159 = arrayOfInt13[i158] >> 2 << 2;
        if (i159 <= 16383)
          break label2064;
      }
    label2064: for (int i160 = 32767; ; i160 = i159 * 2)
    {
      int i161 = i141;
      while (i161 < 40)
      {
        int i162 = arrayOfInt10[i161];
        int i163 = i161 - i141;
        int i164 = arrayOfInt10[i163] * i160 >> 15;
        int i165 = i162 + i164;
        arrayOfInt10[i161] = i165;
        i161 += 1;
      }
      i141 = i157 + -368;
      break;
      int i166 = 18;
      int i167 = 143;
      int i168 = i141 + -5;
      int i169 = i166;
      if (i168 < i169)
        i168 = i166;
      int i170 = i168 + 9;
      int i171 = i167;
      if (i170 > i171)
        i168 = i167 + -9;
      i141 = (i157 + 5) / 6 + i168 + -1;
      break;
    }
    int i172 = 0;
    int i173 = 0;
    while (i173 < 40)
    {
      int i174 = arrayOfInt10[i173];
      int i175 = arrayOfInt10[i173];
      int i176 = i174 * i175;
      i172 += i176;
      i173 += 1;
    }
    int i177 = 2147483647;
    double d5 = Math.log((32768 + i177 >> 16) * 52428);
    double d6 = Math.log(2.0D);
    double d7 = d5 / d6;
    int i178 = (int)d7;
    double d8 = i178;
    int i179 = (int)((d7 - d8) * 0.0F);
    int i180 = i178 + -30 << 16;
    int i181 = i179 * 2;
    int i182 = i180 + i181;
    int i183 = paramArrayOfInt[0] * 44;
    int i184 = paramArrayOfInt[1] * 37;
    int i185 = i183 + i184;
    int i186 = paramArrayOfInt[2] * 22;
    int i187 = i185 + i186;
    int i188 = paramArrayOfInt[3] * 12;
    int i189 = ((i187 + i188) * 2 + 783741 - i182) / 2;
    int i190 = i189 >> 16;
    int i191 = i189 >> 1;
    int i192 = i190 << 15;
    int i193 = i191 - i192;
    double d9 = i190;
    double d10 = i193 / 32768.0D;
    double d11 = d9 + d10;
    int i194 = (int)(Math.pow(2.0D, d11) + 0.5D);
    int i195 = i194 << 4;
    int i204;
    int i207;
    while (true)
    {
      int i196 = arrayOfInt7[i142];
      int[] arrayOfInt14 = QUA_GAIN_CODE;
      int i197 = i196 * 3;
      int i198 = arrayOfInt14[i197] * i195 >> 15 << 1;
      if ((i198 & 0xFFFF8000) != 0)
        i198 = 32767;
      int i199 = i198;
      int i200 = i2 + 1;
      CheapAMR localCheapAMR4 = this;
      int i201 = i;
      int i202 = i199;
      -1.addFrame(i200, i202, 0);
      int[] arrayOfInt15 = QUA_GAIN_CODE;
      int i203 = i196 * 3 + 1;
      i204 = i203[2];
      int i205 = paramArrayOfInt[2];
      3[i205] = 4;
      int i206 = paramArrayOfInt[1];
      2[i206] = 5;
      i207 = paramArrayOfInt[0];
      1[i207] = 0L;
      0[i204] = 3;
      i142 += 1;
      i177 = i172 * 2;
      i195 = 32767;
      tmpTernaryOp = 0L;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.soundfile.CheapAMR
 * JD-Core Version:    0.6.2
 */