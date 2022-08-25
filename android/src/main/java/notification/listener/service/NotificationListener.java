package notification.listener.service;

import static notification.listener.service.NotificationUtils.getBitmapFromDrawable;
import static notification.listener.service.models.ActionCache.cachedNotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;

import notification.listener.service.models.Action;


@SuppressLint("OverrideAbstract")
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    NotiDatabase notiDb;

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

        if(packageName == "com.kakao.talk") {
            Bundle extras = notification.getNotification().extras;
            byte[] drawable = getSmallIcon(packageName);

            Action action = NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName);


            Intent intent = new Intent(NotificationConstants.INTENT);
            intent.putExtra(NotificationConstants.PACKAGE_NAME, packageName);
            intent.putExtra(NotificationConstants.ID, notification.getId());
            intent.putExtra(NotificationConstants.CAN_REPLY, action != null);

            if (NotificationUtils.getQuickReplyAction(notification.getNotification(), packageName) != null) {
                cachedNotifications.put(notification.getId(), action);
            }

            intent.putExtra(NotificationConstants.NOTIFICATIONS_ICON, drawable);

            if (extras != null) {
                CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
                CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
                CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

                intent.putExtra(NotificationConstants.NOTIFICATION_TITLE, title == null ? null : title.toString());
                intent.putExtra(NotificationConstants.NOTIFICATION_CONTENT, text == null ? null : text.toString());
                intent.putExtra(NotificationConstants.NOTIFICATION_SUBCONTENT, subText == null ? title.toString() : subText.toString());
                intent.putExtra(NotificationConstants.IS_REMOVED, isRemoved);
                intent.putExtra(NotificationConstants.HAS_EXTRAS_PICTURE, extras.containsKey(Notification.EXTRA_PICTURE));

                String room = "";
                if(subText != null) {
                    room = subText.toString();;
                }
                else {
                    room = title.toString();
                }
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a hh시 mm분");
                String formatedNow = now.format(formatter);

                if(title != null && text != null) {
//                    notiDb = NotiDatabase.getInstance(this);
                    notiDb = NotiDatabase.getInstance(getApplicationContext());

                    int roomnid = notiDb.RoomDataDao().checkId(room);
//                     if(roomnid == 0) {
                        RoomData music = new RoomData();
                        music.room = room;
                        notiDb.RoomDataDao().insert(music);
//                     }
                    NotiData noti = new NotiData();
                    noti.name = title.toString();
                    noti.text = text.toString();
                    noti.room = room;
                    noti.date = formatedNow;
                    noti.send = 1;

                    notiDb.NotiDao().insert(noti);
                }

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
