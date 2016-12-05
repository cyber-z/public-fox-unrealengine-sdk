package co.cyberz.fox.support.ue;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import co.cyberz.fox.FoxConfig;
import co.cyberz.fox.FoxTrackOption;
import co.cyberz.fox.service.FoxEvent;
import co.cyberz.util.string.StringUtil;

/**
 * Created by Garhira on 2016/10/22.
 */

public class Fox {

  private static long mDelegate;

  public static native void onCompleted(long delegate);

  public static void init(Context c, int appId, String appKey, String appSalt, String fUrl, String aUrl, boolean isDebug) {
    FoxConfig config = new FoxConfig(c.getApplicationContext(),
        appId,
        appKey,
        appSalt);
    if (!StringUtil.isEmpty(fUrl)) config.addFoxServerUrlOption(fUrl);
    if (!StringUtil.isEmpty(aUrl)) config.addAnalyticsServerUrlOption(aUrl);
    config.addDebugOption(isDebug);
    config.activate();
  }

  /**
   * インストール
   */
  public static void trackInstall() {
    co.cyberz.fox.Fox.trackInstall();
  }

  /**
   *
   * @param redirectUrl
   * @param buid
   * @param optout
   * @param delegate
   */
  public static void trackInstall(String redirectUrl, String buid, boolean optout, long delegate) {
    FoxTrackOption option = new FoxTrackOption();
    if (!StringUtil.isEmpty(redirectUrl)) option.addRedirectUrl(redirectUrl);
    if (!StringUtil.isEmpty(buid)) option.addBuid(buid);
    option.addOptOut(optout);
    mDelegate = delegate;

    option.setTrackingStateListener(new FoxTrackOption.TrackingStateListerner() {
      @Override
      public void onComplete() {
        // 成功
        onCompleted(mDelegate);
      }
    });
    co.cyberz.fox.Fox.trackInstall(option);
  }

  public static void trackSession() {
    co.cyberz.fox.Fox.trackSession();
  }

  public static void trackEvent(FoxEvent e) {
    if (e == null) return;
    co.cyberz.fox.Fox.trackEvent(e);
  }

  public static void trackEvent(int ltvId, String buid, double price, String sku, String currency, String eventName, int value, String orderId, String itemName, int quantity, String eventInfo, String extraInfo) {
    FoxEvent e = null;
    if (0 < ltvId && !StringUtil.isEmpty(eventName)) {
      e = new FoxEvent(eventName, ltvId);
    } else if(!StringUtil.isEmpty(eventName)) {
      e = new FoxEvent(eventName);
    }
    if (e == null) {
      Log.e("Fox_UE_SDK", "Event name must be not null.");
      return;
    }
    e.buid = buid;
    e.price = price;
    e.sku = sku;
    e.currency = currency;
    e.value = value;
    e.orderId = orderId;
    e.itemName = itemName;
    e.quantity = quantity;
    try {
      if (!StringUtil.isEmpty(eventInfo)) {
        e.eventInfo = new JSONObject(eventInfo);
      }
    } catch (JSONException e1) {
      Log.e("Fox_UE_SDK", "FoxTrackEvent jsonException", e1);
    } catch (Exception e2) {
      Log.e("Fox_UE_SDK", "FoxTrackEvent anotherException", e2);
    }
    if (extraInfo != null) {
      try {
        String extraInfos[] = extraInfo.split("&");
        for(String info : extraInfos) {
          String param[] = info.split("=");
          if (!StringUtil.isEmpty(param[0]) && !StringUtil.isEmpty(param[1])) {
            e.addExtraInfo(param[0], param[1]);
            e.eventInfo.put(param[0], param[1]);
          }
        }
      } catch(Exception e1) {
        Log.e("Fox_UE_SDK", e1.getMessage());
      }
    }
    trackEvent(e);
  }

  public static void trackEventByBrowser(String url) {
    co.cyberz.fox.Fox.trackEventByBrowser(url);
  }

  public static void setUserInfo(String userInfo) {
    try {
      JSONObject jUserInfo = new JSONObject(userInfo);
      co.cyberz.fox.Fox.setUserInfo(jUserInfo);
    } catch(Exception e) {
      Log.e("Fox_UE_SDK", "setUserInfo", e);
    }
  }

  public static String getUserInfo() {
    try {
      JSONObject jUserInfo = co.cyberz.fox.Fox.getUserInfo();
      String str = jUserInfo.toString();
      if (!StringUtil.isEmpty(str)) {
        return str;
      }
    } catch(Exception e) {
      Log.e("Fox_UE_SDK", "getUserInfo", e);
    }
    return null;
  }

  public static boolean isConversionCompleted() {
    return co.cyberz.fox.Fox.isConversionCompleted();
  }

}
