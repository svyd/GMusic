package com.google.android.music.utils;

public class MusicTagUtils
{
  private static final String[] GENRE_DESCRIPTION = arrayOfString;
  private static final int GENRE_DESCRIPTION_LENGTH = GENRE_DESCRIPTION.length;

  static
  {
    String[] arrayOfString = new String[''];
    arrayOfString[0] = "Blues";
    arrayOfString[1] = "Classic Rock";
    arrayOfString[2] = "Country";
    arrayOfString[3] = "Dance";
    arrayOfString[4] = "Disco";
    arrayOfString[5] = "Funk";
    arrayOfString[6] = "Grunge";
    arrayOfString[7] = "Hip-Hop";
    arrayOfString[8] = "Jazz";
    arrayOfString[9] = "Metal";
    arrayOfString[10] = "New Age";
    arrayOfString[11] = "Oldies";
    arrayOfString[12] = "Other";
    arrayOfString[13] = "Pop";
    arrayOfString[14] = "R&B";
    arrayOfString[15] = "Rap";
    arrayOfString[16] = "Reggae";
    arrayOfString[17] = "Rock";
    arrayOfString[18] = "Techno";
    arrayOfString[19] = "Industrial";
    arrayOfString[20] = "Alternative";
    arrayOfString[21] = "Ska";
    arrayOfString[22] = "Death Metal";
    arrayOfString[23] = "Pranks";
    arrayOfString[24] = "Soundtrack";
    arrayOfString[25] = "Euro-Techno";
    arrayOfString[26] = "Ambient";
    arrayOfString[27] = "Trip-Hop";
    arrayOfString[28] = "Vocal";
    arrayOfString[29] = "Jazz+Funk";
    arrayOfString[30] = "Fusion";
    arrayOfString[31] = "Trance";
    arrayOfString[32] = "Classical";
    arrayOfString[33] = "Instrumental";
    arrayOfString[34] = "Acid";
    arrayOfString[35] = "House";
    arrayOfString[36] = "Game";
    arrayOfString[37] = "Sound Clip";
    arrayOfString[38] = "Gospel";
    arrayOfString[39] = "Noise";
    arrayOfString[40] = "AlternRock";
    arrayOfString[41] = "Bass";
    arrayOfString[42] = "Soul";
    arrayOfString[43] = "Punk";
    arrayOfString[44] = "Space";
    arrayOfString[45] = "Meditative";
    arrayOfString[46] = "Instrumental Pop";
    arrayOfString[47] = "Instrumental Rock";
    arrayOfString[48] = "Ethnic";
    arrayOfString[49] = "Gothic";
    arrayOfString[50] = "Darkwave";
    arrayOfString[51] = "Techno-Industrial";
    arrayOfString[52] = "Electronic";
    arrayOfString[53] = "Pop-Folk";
    arrayOfString[54] = "Eurodance";
    arrayOfString[55] = "Dream";
    arrayOfString[56] = "Southern Rock";
    arrayOfString[57] = "Comedy";
    arrayOfString[58] = "Cult";
    arrayOfString[59] = "Gangsta";
    arrayOfString[60] = "Top 40";
    arrayOfString[61] = "Christian Rap";
    arrayOfString[62] = "Pop/Funk";
    arrayOfString[63] = "Jungle";
    arrayOfString[64] = "Native American";
    arrayOfString[65] = "Cabaret";
    arrayOfString[66] = "New Wave";
    arrayOfString[67] = "Psychadelic";
    arrayOfString[68] = "Rave";
    arrayOfString[69] = "Showtunes";
    arrayOfString[70] = "Trailer";
    arrayOfString[71] = "Lo-Fi";
    arrayOfString[72] = "Tribal";
    arrayOfString[73] = "Acid Punk";
    arrayOfString[74] = "Acid Jazz";
    arrayOfString[75] = "Polka";
    arrayOfString[76] = "Retro";
    arrayOfString[77] = "Musical";
    arrayOfString[78] = "Rock & Roll";
    arrayOfString[79] = "Hard Rock";
    arrayOfString[80] = "Folk";
    arrayOfString[81] = "Folk-Rock";
    arrayOfString[82] = "National Folk";
    arrayOfString[83] = "Swing";
    arrayOfString[84] = "Fast Fusion";
    arrayOfString[85] = "Bebob";
    arrayOfString[86] = "Latin";
    arrayOfString[87] = "Revival";
    arrayOfString[88] = "Celtic";
    arrayOfString[89] = "Bluegrass";
    arrayOfString[90] = "Avantgarde";
    arrayOfString[91] = "Gothic Rock";
    arrayOfString[92] = "Progressive Rock";
    arrayOfString[93] = "Psychedelic Rock";
    arrayOfString[94] = "Symphonic Rock";
    arrayOfString[95] = "Slow Rock";
    arrayOfString[96] = "Big Band";
    arrayOfString[97] = "Chorus";
    arrayOfString[98] = "Easy Listening";
    arrayOfString[99] = "Acoustic";
    arrayOfString[100] = "Humour";
    arrayOfString[101] = "Speech";
    arrayOfString[102] = "Chanson";
    arrayOfString[103] = "Opera";
    arrayOfString[104] = "Chamber Music";
    arrayOfString[105] = "Sonata";
    arrayOfString[106] = "Symphony";
    arrayOfString[107] = "Booty Bass";
    arrayOfString[108] = "Primus";
    arrayOfString[109] = "Porn Groove";
    arrayOfString[110] = "Satire";
    arrayOfString[111] = "Slow Jam";
    arrayOfString[112] = "Club";
    arrayOfString[113] = "Tango";
    arrayOfString[114] = "Samba";
    arrayOfString[115] = "Folklore";
    arrayOfString[116] = "Ballad";
    arrayOfString[117] = "Power Ballad";
    arrayOfString[118] = "Rhythmic Soul";
    arrayOfString[119] = "Freestyle";
    arrayOfString[120] = "Duet";
    arrayOfString[121] = "Punk Rock";
    arrayOfString[122] = "Drum Solo";
    arrayOfString[123] = "A capella";
    arrayOfString[124] = "Euro-House";
    arrayOfString[125] = "Dance Hall";
    arrayOfString[126] = "Goa";
    arrayOfString[127] = "Drum & Bass";
    arrayOfString[''] = "Club-House";
    arrayOfString[''] = "Hardcore";
    arrayOfString[''] = "Terror";
    arrayOfString[''] = "Indie";
    arrayOfString[''] = "Britpop";
    arrayOfString[''] = "Negerpunk";
    arrayOfString[''] = "Polsk Punk";
    arrayOfString[''] = "Beat";
    arrayOfString[''] = "Christian Gangsta";
    arrayOfString[''] = "Heavy Metal";
    arrayOfString[''] = "Black Metal";
    arrayOfString[''] = "Crossover";
    arrayOfString[''] = "Contemporary Christian";
    arrayOfString[''] = "Christian Rock";
    arrayOfString[''] = "Merengue";
    arrayOfString[''] = "Salsa";
    arrayOfString[''] = "Thrash Metal";
    arrayOfString[''] = "Anime";
    arrayOfString[''] = "JPop";
    arrayOfString[''] = "Synthpop";
  }

  public static String getGenreName(String paramString)
  {
    if (paramString == null)
      paramString = null;
    while (true)
    {
      return paramString;
      int i = paramString.length();
      if ((i > 0) && (paramString.charAt(0) == '('))
      {
        StringBuffer localStringBuffer1 = new StringBuffer();
        int j = 1;
        while (true)
        {
          int k = i + -1;
          if (j >= k)
            break;
          char c = paramString.charAt(j);
          if (!Character.isDigit(c))
            break;
          StringBuffer localStringBuffer2 = localStringBuffer1.append(c);
          j += 1;
        }
        if (paramString.charAt(j) == ')')
          try
          {
            int m = Short.parseShort(localStringBuffer1.toString());
            if (m >= 0)
            {
              int n = GENRE_DESCRIPTION_LENGTH;
              if (m < n)
              {
                paramString = GENRE_DESCRIPTION[m];
              }
              else if ((m < 255) && (j + 1 < i))
              {
                int i1 = j + 1;
                String str = paramString.substring(i1);
                paramString = str;
              }
            }
          }
          catch (NumberFormatException localNumberFormatException)
          {
          }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.MusicTagUtils
 * JD-Core Version:    0.6.2
 */