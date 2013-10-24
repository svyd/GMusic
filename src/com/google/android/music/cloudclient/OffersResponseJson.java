package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class OffersResponseJson extends GenericJson
{
  public static final int ACCOUNT_STATUS_LOCKER = 2;
  public static final int ACCOUNT_STATUS_NONE = 1;
  public static final int COUPON_STATUS_ALREADY_REDEEMED = 3;
  public static final int COUPON_STATUS_EXPIRED = 4;
  public static final int COUPON_STATUS_INVALID = 2;
  public static final int COUPON_STATUS_OK = 1;
  public static final int NAUTILUS_IN_TRIAL = 3;
  public static final int NAUTILUS_PAID = 4;

  @Key("accountStatus")
  public int mAccountStatus = 1;

  @Key("couponStatus")
  public int mCouponStatus;

  @Key("lockerAvail")
  public boolean mIsLockerAvailable;

  @Key("eTimeSecs")
  public long mNautilusExpirationTimeInSeconds;

  @Key("offers")
  public List<OfferJson> mOffers;

  public boolean canSignupForNautilus()
  {
    if ((isNautilusAvailable()) && (!isSignedUpForNautilus()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public OfferJson getDefaultOffer()
  {
    if (isNautilusAvailable());
    for (OfferJson localOfferJson = (OfferJson)this.mOffers.get(0); ; localOfferJson = null)
      return localOfferJson;
  }

  public boolean isLockerAvailable()
  {
    return this.mIsLockerAvailable;
  }

  public boolean isNautilusAvailable()
  {
    if ((this.mOffers != null) && (!this.mOffers.isEmpty()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isSignedUpForNautilus()
  {
    if ((this.mAccountStatus == 3) || (this.mAccountStatus == 4));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isValidMusicAccount()
  {
    boolean bool = true;
    if ((this.mIsLockerAvailable) || (this.mAccountStatus > 1));
    while (true)
    {
      return bool;
      bool = false;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.OffersResponseJson
 * JD-Core Version:    0.6.2
 */