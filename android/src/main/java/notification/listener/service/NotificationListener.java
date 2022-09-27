package notification.listener.service;

import static notification.listener.service.NotificationUtils.getBitmapFromDrawable;
import static notification.listener.service.models.ActionCache.cachedNotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import android.provider.Telephony;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import android.app.NotificationManager;

import notification.listener.service.models.Action;


@SuppressLint("OverrideAbstract")
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    NotiDatabase notiDb;
    private Context context;

    public static boolean runApp = false;
    private final NotiData noti = new NotiData();
    private final KakaoData kakaonoti = new KakaoData();
    private final WhatsappData whatsappnoti = new WhatsappData();

    public void setRunAppFalse() {
        runApp = false;
    }
    public void setRunAppTrue() {
        runApp = true;
    }

    @RequiresApi(api = VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        handleNotification(notification, false);
    }

    @RequiresApi(api = VERSION_CODES.KITKAT)
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        handleNotification(sbn, true);
    }

    @RequiresApi(api = VERSION_CODES.KITKAT)
    private void handleNotification(StatusBarNotification notification, boolean isRemoved) {
        String packageName = notification.getPackageName();
        String tag = notification.getTag();
        System.out.println("앱 확인" + packageName);
        String defaultSMS = Telephony.Sms.getDefaultSmsPackage(context);
        System.out.println("기본 메시지앱 확인" + defaultSMS);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       StatusBarNotification[] barNotifications = notificationManager.getActiveNotifications();
//        System.out.println("노티피케이션 확인 :" + barNotifications);
//         packageName.equals("com.samsung.android.messaging")

//          if(packageName.equals("com.kakao.talk") || packageName.equals("com.whatsapp") || packageName.equals(defaultSMS)) {
        if(packageName.equals("com.kakao.talk") || packageName.equals("com.whatsapp") || packageName.contains("messaging") || packageName.contains("messenger")) {
//            System.out.println("앱 실행 여부 : " + runApp);
            Bundle extras = notification.getNotification().extras;
            byte[] drawable = getSmallIcon(packageName);
//             System.out.println("번들 확인 :" + extras);

            Action action = NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName);

            Intent intent = new Intent(NotificationConstants.INTENT);
            intent.putExtra(NotificationConstants.PACKAGE_NAME, packageName);
            intent.putExtra(NotificationConstants.ID, notification.getId());
            intent.putExtra(NotificationConstants.TAG, tag);
            intent.putExtra(NotificationConstants.CAN_REPLY, action != null);

//            if (NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName) != null) {
//                cachedNotifications.put(notification.getId(), action);
//            }
            if (NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName) != null) {
                cachedNotifications.put(notification.getTag(), action);
            }

            intent.putExtra(NotificationConstants.NOTIFICATIONS_ICON, drawable);

            if (extras != null) {
                CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
                CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
                CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT );

                intent.putExtra(NotificationConstants.NOTIFICATION_TITLE, title == null ? null : title.toString());
                intent.putExtra(NotificationConstants.NOTIFICATION_CONTENT, text == null ? null : text.toString());
                intent.putExtra(NotificationConstants.NOTIFICATION_SUBCONTENT, subText == null ? title.toString() : subText.toString());
                intent.putExtra(NotificationConstants.IS_REMOVED, isRemoved);
                intent.putExtra(NotificationConstants.HAS_EXTRAS_PICTURE, extras.containsKey(Notification.EXTRA_PICTURE));

                System.out.println("제거됨 확인 :" + isRemoved);

                String room = "";
                if(subText != null) {
                    room = subText.toString();
                }
                else {
                    room = title.toString();
                }
                LocalDateTime now = LocalDateTime.now();
                String formatedNow = now.format(DateTimeFormatter.ofPattern("a hh시 mm분"));
                String formatedNow2 = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                if(title != null && text != null) {
                    notiDb = NotiDatabase.getInstance(getApplicationContext());
                    int roomnid = notiDb.RoomDataDao().checkId(room);
                    int kakaoroomnid = notiDb.KakaoRoomDataDao().checkId(room);
                    int whatsapproomnid = notiDb.WhatsappRoomDataDao().checkId(room);

//                     if(packageName.equals(defaultSMS)) {
                   if(packageName.contains("messaging")) {
//                    if(packageName.contains("messaging") || packageName.contains("messenger")) {
                        if(roomnid == 0) {
                            RoomData roomData = new RoomData();
                            roomData.room = room;
                            if(isRemoved == false) notiDb.RoomDataDao().insert(roomData);
                        }
                    } else if(packageName.equals("com.kakao.talk") && (!title.equals("카카오톡") || !title.equals("KakaoTalk"))) {
                        if(kakaoroomnid == 0) {
                            KakaoRoomData kakaoroomData = new KakaoRoomData();
                            kakaoroomData.room = room;
                            if(isRemoved == false) notiDb.KakaoRoomDataDao().insert(kakaoroomData);
                        }
                    } else if(packageName.equals("com.whatsapp")) {
                        if(whatsapproomnid == 0) {
                            WhatsappRoomData whatsapproomData = new WhatsappRoomData();
                            whatsapproomData.room = room;
                            if(isRemoved == false) notiDb.WhatsappRoomDataDao().insert(whatsapproomData);
                        }
                    }
//                    NotiData noti = new NotiData();

//                     if(packageName.equals("com.samsung.android.messaging")) {
//                     if(packageName.equals(defaultSMS)) {
                   if(packageName.contains("messaging")) {
//                    if(packageName.contains("messaging") || packageName.contains("messenger")) {
                        noti.name = title.toString();
                        noti.text = text.toString();
                        noti.room = room;
                        noti.date = formatedNow;
                        noti.vsDate = formatedNow2;
                        noti.send = 1;
                        noti.result = "yet";
                        noti.app = 1;
                        noti.read = "2";
                        if(runApp == false && noti.app != 0 && isRemoved == false) notiDb.NotiDao().insert(noti);
                    } else if(packageName.equals("com.kakao.talk") && (!title.equals("카카오톡") || !title.equals("KakaoTalk"))) {
                        kakaonoti.name = title.toString();
                        kakaonoti.text = text.toString();
                        kakaonoti.room = room;
                        kakaonoti.date = formatedNow;
                        kakaonoti.vsDate = formatedNow2;
                        kakaonoti.send = 1;
                        kakaonoti.result = "yet";
                        kakaonoti.app = 2;
                        kakaonoti.read = "2";
                        if(runApp == false && kakaonoti.app != 0 && isRemoved == false)  notiDb.KakaoDao().insert(kakaonoti);
                    } else if(packageName.equals("com.whatsapp")) {
                        whatsappnoti.name = title.toString();
                        whatsappnoti.text = text.toString();
                        whatsappnoti.room = room;
                        whatsappnoti.date = formatedNow;
                        whatsappnoti.vsDate = formatedNow2;
                        whatsappnoti.send = 1;
                        whatsappnoti.result = "yet";
                        whatsappnoti.app = 3;
                        whatsappnoti.read = "2";
                        if(runApp == false && whatsappnoti.app != 0 && isRemoved == false) notiDb.WhatsappDao().insert(whatsappnoti);
                    }

//                     System.out.println("노티값 확인 : " + noti.text);

//                    if(runApp == false && noti.app != 0) notiDb.NotiDao().insert(noti);
//                      if(runApp == false) notiDb.NotiDao().insert(noti);
//                     notiDb.NotiDao().insert(noti);
//                 }

                if (extras.containsKey(Notification.EXTRA_PICTURE)) {
                    Bitmap bmp = (Bitmap) extras.get(Notification.EXTRA_PICTURE);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    intent.putExtra(NotificationConstants.EXTRAS_PICTURE, stream.toByteArray());
                }
            }
            sendBroadcast(intent);
        }
         }

    }


    public byte[] getSmallIcon(String packageName) {
        try {
            PackageManager manager = getBaseContext().getPackageManager();
            Drawable icon = manager.getApplicationIcon(packageName);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            getBitmapFromDrawable(icon).compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
