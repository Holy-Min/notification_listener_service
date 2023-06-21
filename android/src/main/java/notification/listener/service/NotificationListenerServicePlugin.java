package notification.listener.service;

import static notification.listener.service.NotificationUtils.isPermissionGranted;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import java.util.*;
import com.google.gson.Gson;
import androidx.sqlite.db.SimpleSQLiteQuery;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import notification.listener.service.models.Action;
import notification.listener.service.models.ActionCache;
//import notification.listener.service.kakao.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NotificationListenerServicePlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.ActivityResultListener, EventChannel.StreamHandler {

//     private static final String defaultSMS = Telephony.Sms.getDefaultSmsPackage(context.getApplicationContext());
//     private static final String defaultSMS = Telephony.Sms.getDefaultSmsPackage(this);

    private static final String CHANNEL_TAG = "x-slayer/notifications_channel";
    private static final String EVENT_TAG = "x-slayer/notifications_event";

    private MethodChannel channel;
    private EventChannel eventChannel;
    private NotificationReceiver notificationReceiver;
    private Context context;
    private Activity mActivity;

    private Result pendingResult;
    final int REQUEST_CODE_FOR_NOTIFICATIONS = 1199;

    NotiDatabase notiDb;
    private boolean hasRemoved = false;
//    private NotiData noti = new NotiData();
//    noti.name = "testName";
//    noti.text = "testText";
//    noti.room = "testRoom";
//    noti.date = "testTime;
//    noti.send = 2;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_TAG);
        channel.setMethodCallHandler(this);
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), EVENT_TAG);
        eventChannel.setStreamHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        pendingResult = result;
        if (call.method.equals("isPermissionGranted")) {
            result.success(isPermissionGranted(context));
        } else if (call.method.equals("requestPermission")) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            mActivity.startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATIONS);
        } else if (call.method.equals("sendReply")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());

            final String message = call.argument("message");
            final String name = call.argument("name");
            final String room = call.argument("room");
//            final int notificationId = call.argument("notificationId");
            final String tag = call.argument("tag");
            final String packageName = call.argument("packageName");
            final String str = "1";
            hasRemoved = call.argument("hasRemoved");
            final String url = "N";

            LocalDateTime now = LocalDateTime.now();
            String formatedNow = now.format(DateTimeFormatter.ofPattern("a hh:mm"));
            String formatedNow2 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//             String defaultSMS = Telephony.Sms.getDefaultSmsPackage(context.getApplicationContext());

            NotiData noti = new NotiData();
            KakaoData kakaonoti = new KakaoData();
            WhatsappData whatsappnoti = new WhatsappData();
            TelegramData telegramnoti = new TelegramData();
            LineData linenoti = new LineData();
            InstagramData instagramnoti = new InstagramData();
            FacebookData facebooknoti = new FacebookData();

//            final Action action = ActionCache.cachedNotifications.get(notificationId);
            final Action action = ActionCache.cachedNotifications.get(tag);
            if (action == null) {
                result.error("Notification", "Can't find this cached notification", null);
            }
            try {
                action.sendReply(context, message);
                if(packageName.equals("com.samsung.android.messaging") || packageName.equals("com.google.android.apps.messaging")) {
                    noti.name = name;
                    noti.text = message;
                    noti.room = room;
                    noti.date = formatedNow;
                    noti.vsDate = formatedNow2;
                    noti.send = 2;
                    noti.result = str;
                    noti.app = packageName;
                    noti.read = "1";
                    noti.url = url;

                    if(hasRemoved == false) notiDb.NotiDao().insert(noti);
//                    if(hasRemoved == false && noti.app.equals(packageName)) notiDb.NotiDao().insert(noti);
                } else if(packageName.equals("com.kakao.talk")) {
                    kakaonoti.name = name;
                    kakaonoti.text = message;
                    kakaonoti.room = room;
                    kakaonoti.date = formatedNow;
                    kakaonoti.vsDate = formatedNow2;
                    kakaonoti.send = 2;
                    kakaonoti.result = str;
                    kakaonoti.app = "2";
                    kakaonoti.read = "1";
                    kakaonoti.url = url;

                    if(hasRemoved == false) notiDb.KakaoDao().insert(kakaonoti);
                } else if(packageName.equals("com.whatsapp")) {
                    whatsappnoti.name = name;
                    whatsappnoti.text = message;
                    whatsappnoti.room = room;
                    whatsappnoti.date = formatedNow;
                    whatsappnoti.vsDate = formatedNow2;
                    whatsappnoti.send = 2;
                    whatsappnoti.result = str;
                    whatsappnoti.app = "3";
                    whatsappnoti.read = "1";
                    whatsappnoti.url = url;

                    if(hasRemoved == false) notiDb.WhatsappDao().insert(whatsappnoti);
                } else if(packageName.equals("org.telegram.messenger")) {
                    telegramnoti.name = name;
                    telegramnoti.text = message;
                    telegramnoti.room = room;
                    telegramnoti.date = formatedNow;
                    telegramnoti.vsDate = formatedNow2;
                    telegramnoti.send = 2;
                    telegramnoti.result = str;
                    telegramnoti.app = "4";
                    telegramnoti.read = "1";
                    telegramnoti.url = url;

                    if(hasRemoved == false) notiDb.TelegramDao().insert(telegramnoti);
                } else if(packageName.equals("jp.naver.line.android")) {
                    linenoti.name = name;
                    linenoti.text = message;
                    linenoti.room = room;
                    linenoti.date = formatedNow;
                    linenoti.vsDate = formatedNow2;
                    linenoti.send = 2;
                    linenoti.result = str;
                    linenoti.app = "4";
                    linenoti.read = "1";
                    linenoti.url = url;

                    if(hasRemoved == false) notiDb.LineDao().insert(linenoti);
                } else if(packageName.equals("com.instagram.android")) {
                    instagramnoti.name = name;
                    instagramnoti.text = message;
                    instagramnoti.room = room;
                    instagramnoti.date = formatedNow;
                    instagramnoti.vsDate = formatedNow2;
                    instagramnoti.send = 2;
                    instagramnoti.result = str;
                    instagramnoti.app = "4";
                    instagramnoti.read = "1";
                    instagramnoti.url = url;

                    if(hasRemoved == false) notiDb.InstagramDao().insert(instagramnoti);
                } else if(packageName.equals("com.facebook.orca")) {
                    facebooknoti.name = name;
                    facebooknoti.text = message;
                    facebooknoti.room = room;
                    facebooknoti.date = formatedNow;
                    facebooknoti.vsDate = formatedNow2;
                    facebooknoti.send = 2;
                    facebooknoti.result = str;
                    facebooknoti.app = "4";
                    facebooknoti.read = "1";
                    facebooknoti.url = url;

                    if(hasRemoved == false) notiDb.FacebookDao().insert(facebooknoti);
                }
//                 notiDb.NotiDao().insert(noti);
                result.success(true);
            } catch (PendingIntent.CanceledException e) {
                result.success(false);
                e.printStackTrace();
            }
        } else if (call.method.equals("getChat")) {
//            mContext = getApplicationContext();
//            Intent intent = new Intent(getApplicationContext(), NotiDatabase.class);

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramData> noti = notiDb.TelegramDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineData> noti = notiDb.LineDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramData> noti = notiDb.InstagramDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookData> noti = notiDb.FacebookDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<RoomData> noti = notiDb.RoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getKakaoRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoRoomData> noti = notiDb.KakaoRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getWhatsappRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappRoomData> noti = notiDb.WhatsappRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getTelegramRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramRoomData> noti = notiDb.TelegramRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getLineRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineRoomData> noti = notiDb.LineRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getInstagramRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramRoomData> noti = notiDb.InstagramRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getFacebookRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookRoomData> noti = notiDb.FacebookRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getChatInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getKakaoInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getWhatsappInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getTelegramInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramData> noti = notiDb.TelegramDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getLineInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineData> noti = notiDb.LineDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getInstagramInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramData> noti = notiDb.InstagramDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("getFacebookInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookData> noti = notiDb.FacebookDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("selectMessage")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        } else if (call.method.equals("selectKakao")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectWhatsapp")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectTelegram")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramData> noti = notiDb.TelegramDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectLine")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineData> noti = notiDb.LineDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectInstagram")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramData> noti = notiDb.InstagramDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectFacebook")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookData> noti = notiDb.FacebookDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramData> noti = notiDb.TelegramDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineData> noti = notiDb.LineDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramData> noti = notiDb.InstagramDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookData> noti = notiDb.FacebookDao().undetectedSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("updateMessage")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateKakao")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateWhatsapp")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateTelegram")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.TelegramDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateLine")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.LineDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateInstagram")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.InstagramDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("updateFacebook")) {
            String str = call.argument("result");
            String url = call.argument("url");
            int nid = call.argument("nid");
            String text = call.argument("text");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.FacebookDao().update(str, url, nid, text);
            result.success(true);

        }else if (call.method.equals("dataInsert")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
//             String defaultSMS = Telephony.Sms.getDefaultSmsPackage(context.getApplicationContext());

            String name = call.argument("name");
            String text = call.argument("text");
            String room = call.argument("room");
            String date = call.argument("date");
            String vsDate = call.argument("vsDate");
            String packageName = call.argument("packageName");
            String str = call.argument("result");
            String read = call.argument("read");
            String url = call.argument("url");


            NotiData noti = new NotiData();
            KakaoData kakaonoti = new KakaoData();
            WhatsappData whatsappnoti = new WhatsappData();
            TelegramData telegramnoti = new TelegramData();
            LineData linenoti = new LineData();
            InstagramData instagramnoti = new InstagramData();
            FacebookData facebooknoti = new FacebookData();
//             if(packageName.equals(defaultSMS)) {
//           if(packageName.contains("messaging")) {
            if(packageName.contains("com.samsung.android.messaging") || packageName.contains("com.google.android.apps.messaging")) {
                noti.name = name;
                noti.text = text;
                noti.room = room;
                noti.date = date;
                noti.vsDate = vsDate;
                noti.send = 1;
                noti.result = str;
                noti.app = packageName;
                noti.read = "2";
                noti.url = url;

                notiDb.NotiDao().insert(noti);
            } else if(packageName.equals("com.kakao.talk")) {
                kakaonoti.name = name;
                kakaonoti.text = text;
                kakaonoti.room = room;
                kakaonoti.date = date;
                kakaonoti.vsDate = vsDate;
                kakaonoti.send = 1;
                kakaonoti.result = str;
                kakaonoti.app = "2";
                kakaonoti.read = "2";
                kakaonoti.url = url;

                notiDb.KakaoDao().insert(kakaonoti);
            } else if(packageName.equals("com.whatsapp")) {
                whatsappnoti.name = name;
                whatsappnoti.text = text;
                whatsappnoti.room = room;
                whatsappnoti.date = date;
                whatsappnoti.vsDate = vsDate;
                whatsappnoti.send = 1;
                whatsappnoti.result = str;
                whatsappnoti.app = "3";
                whatsappnoti.read = "2";
                whatsappnoti.url = url;

                notiDb.WhatsappDao().insert(whatsappnoti);
            } else if(packageName.equals("org.telegram.messenger")) {
                telegramnoti.name = name;
                telegramnoti.text = text;
                telegramnoti.room = room;
                telegramnoti.date = date;
                telegramnoti.vsDate = vsDate;
                telegramnoti.send = 1;
                telegramnoti.result = str;
                telegramnoti.app = "4";
                telegramnoti.read = "2";
                telegramnoti.url = url;

                notiDb.TelegramDao().insert(telegramnoti);
            } else if(packageName.equals("jp.naver.line.android")) {
                linenoti.name = name;
                linenoti.text = text;
                linenoti.room = room;
                linenoti.date = date;
                linenoti.vsDate = vsDate;
                linenoti.send = 1;
                linenoti.result = str;
                linenoti.app = "5";
                linenoti.read = "2";
                linenoti.url = url;

                notiDb.TelegramDao().insert(linenoti);
            } else if(packageName.equals("com.instagram.android")) {
                instagramnoti.name = name;
                instagramnoti.text = text;
                instagramnoti.room = room;
                instagramnoti.date = date;
                instagramnoti.vsDate = vsDate;
                instagramnoti.send = 1;
                instagramnoti.result = str;
                instagramnoti.app = "6";
                instagramnoti.read = "2";
                instagramnoti.url = url;

                notiDb.TelegramDao().insert(instagramnoti);
            } else if(packageName.equals("com.facebook.orca")) {
                facebooknoti.name = name;
                facebooknoti.text = text;
                facebooknoti.room = room;
                facebooknoti.date = date;
                facebooknoti.vsDate = vsDate;
                facebooknoti.send = 1;
                facebooknoti.result = str;
                facebooknoti.app = "7";
                facebooknoti.read = "2";
                facebooknoti.url = url;

                notiDb.TelegramDao().insert(facebooknoti);
            }

//                 if(packageName.equals("com.samsung.android.messaging")) {

            result.success(true);


        }else if (call.method.equals("setFalse")) {
            NotificationListener nl = new NotificationListener();
            nl.setRunAppFalse();
//             System.out.println("변수 확인" + nl.runApp);
            result.success(true);

        }else if (call.method.equals("setTrue")) {
            NotificationListener nl = new NotificationListener();
//             System.out.println("변수 확인" + nl.runApp);
            nl.setRunAppTrue();
            result.success(true);

        }else if (call.method.equals("deleteMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().deleteAll();
            notiDb.RoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().deleteAll();
            notiDb.KakaoRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().deleteAll();
            notiDb.WhatsappRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.TelegramDao().deleteAll();
            notiDb.TelegramRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.LineDao().deleteAll();
            notiDb.LineRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.InstagramDao().deleteAll();
            notiDb.InstagramRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("deleteFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.FacebookDao().deleteAll();
            notiDb.FacebookRoomDataDao().deleteAll();
            result.success(true);

        }else if (call.method.equals("messageRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.NotiDao().roomDelete(room);
            notiDb.RoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("kakaoRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.KakaoDao().roomDelete(room);
            notiDb.KakaoRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("WhatsappRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.WhatsappDao().roomDelete(room);
            notiDb.WhatsappRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("TelegramRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.TelegramDao().roomDelete(room);
            notiDb.TelegramRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("LineRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.LineDao().roomDelete(room);
            notiDb.LineRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("InstagramRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.InstagramDao().roomDelete(room);
            notiDb.InstagramRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("FacebookRoomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.FacebookDao().roomDelete(room);
            notiDb.FacebookRoomDataDao().delete(room);
            result.success(true);

        }else if (call.method.equals("countMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.NotiDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.KakaoDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.WhatsappDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.TelegramDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.LineDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.InstagramDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("countFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            int count = notiDb.FacebookDao().roomCount(room);
            result.success(count);

        }else if (call.method.equals("readMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.NotiDao().read(room);
            result.success(true);

        }else if (call.method.equals("readKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.KakaoDao().read(room);
            result.success(true);

        }else if (call.method.equals("readWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.WhatsappDao().read(room);
            result.success(true);

        }else if (call.method.equals("readTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.TelegramDao().read(room);
            result.success(true);

        }else if (call.method.equals("readLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.LineDao().read(room);
            result.success(true);

        }else if (call.method.equals("readInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.InstagramDao().read(room);
            result.success(true);

        }else if (call.method.equals("readFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.FacebookDao().read(room);
            result.success(true);

        }else if (call.method.equals("lastMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.NotiDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.KakaoDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.WhatsappDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.TelegramDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.LineDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.InstagramDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.FacebookDao().lastText(room);
            result.success(text);

        }else if (call.method.equals("lastDateMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.NotiDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.KakaoDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.WhatsappDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.TelegramDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.LineDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.InstagramDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("lastDateFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            String text = notiDb.FacebookDao().lastDate(room);
            result.success(text);

        }else if (call.method.equals("totalMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.NotiDao().total();
            result.success(text);

        }else if (call.method.equals("totalKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.KakaoDao().total();
            result.success(text);

        }else if (call.method.equals("totalWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.WhatsappDao().total();
            result.success(text);

        }else if (call.method.equals("totalTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.TelegramDao().total();
            result.success(text);

        }else if (call.method.equals("totalLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.LineDao().total();
            result.success(text);

        }else if (call.method.equals("totalInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.InstagramDao().total();
            result.success(text);

        }else if (call.method.equals("totalFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.FacebookDao().total();
            result.success(text);

        }else if (call.method.equals("resultMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.NotiDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.KakaoDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.WhatsappDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.TelegramDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.LineDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.InstagramDao().resultCount();
            result.success(text);

        }else if (call.method.equals("resultFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int text = notiDb.FacebookDao().resultCount();
            result.success(text);

        }else if (call.method.equals("runVacuum")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().vacuum(new SimpleSQLiteQuery("VACUUM"));
            result.success(true);

        }else if (call.method.equals("allReadMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.TelegramDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.LineDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.InstagramDao().allRead();
            result.success(true);

        }else if (call.method.equals("allReadFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.FacebookDao().allRead();
            result.success(true);

        }else if (call.method.equals("dayOutMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.TelegramDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.LineDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.InstagramDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.FacebookDao().delete();
            result.success(true);

        }else if (call.method.equals("dayOutMessageRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.RoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.RoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutKakaoRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.KakaoRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.KakaoRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutWhatsappRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.WhatsappRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.WhatsappRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutTelegramRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.TelegramRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.TelegramRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutLineRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.LineRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.LineRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutInstagramRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.InstagramRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.InstagramRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("dayOutFacebookRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> roomList = notiDb.FacebookRoomDataDao().checkRoom();
            for(int i = 0; i < roomList.size(); i++) {
                notiDb.FacebookRoomDataDao().roomDelete(roomList.get(i));
            }
            result.success(true);

        }else if (call.method.equals("examRoomInsert")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            final String app = call.argument("app");
            RoomData roomData = new RoomData();
            roomData.room = "예시";
            roomData.app = app;
            roomData.vsDate = "2999-01-01";
            notiDb.RoomDataDao().insert(roomData);
            result.success(true);

        }else if (call.method.equals("updateMessageRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.RoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateKakaoRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.KakaoRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateWhatsappRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.WhatsappRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateTelegramRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.TelegramRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateLineRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.LineRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateInstagramRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.InstagramRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("updateFacebookRoom")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.FacebookRoomDataDao().update(room);
            result.success(true);

        }else if (call.method.equals("detectLastMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastTelegram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<TelegramData> noti = notiDb.TelegramDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastLine")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<LineData> noti = notiDb.LineDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastInstagram")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<InstagramData> noti = notiDb.InstagramDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("detectLastFacebook")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<FacebookData> noti = notiDb.FacebookDao().lastOneSelect();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if(call.method.equals("changeMessageList")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            ArrayList<String> message = call.argument("message");
//            System.out.println("변경된 리스트 확인 : " + message);
            List<String> m1 = notiDb.MessageListDao().getAll();
//            System.out.println("기존 리스트 확인 : " + m1);
            ArrayList<String> m2 = new ArrayList<String>(m1);
//            System.out.println("array 리스트 확인 : " + m2);
            
            MessageListData noti = new MessageListData();
            
            if(m1.size() == 0) {
                for(int i = 0; i < message.size(); i++) {
                    noti.name = message.get(i);
                    notiDb.MessageListDao().insert(noti);
                }
                result.success(true);
            } else {
                ArrayList<String> resultList = compageAndDel(message, m2);
                // DB에 없던, 새롭게 추가될 데이터
                for (int i = 0; i < resultList.size(); i++) {
                    System.out.println("Inst " + resultList.get(i));
                    if(resultList.get(i) != null) {
                        noti.name = resultList.get(i);
                        notiDb.MessageListDao().insert(noti);
                    }
                }

                //기존 DB에 있었지만 삭제된 데이터
                ArrayList<String> resultList2 = compageAndDel(m2, message);
                for (int i = 0; i < resultList2.size(); i++) {
                    System.out.println("Del " + resultList2.get(i));
                    if(resultList2.get(i) != null) {
                        notiDb.MessageListDao().delete(resultList2.get(i));
                    }
                }
                result.success(true);
            }
        }else if (call.method.equals("countMessageList")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            int count = notiDb.MessageListDao().countList();
            result.success(count);

        }else if (call.method.equals("getMessageList")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<String> m1 = notiDb.MessageListDao().getAll();
            result.success(m1);

        }else {
            result.notImplemented();
        }
    }

    private static ArrayList<String> compageAndDel(ArrayList<String> target, ArrayList<String> source) {
        ArrayList<String> tmpArr = new ArrayList<>();
        tmpArr.addAll(target);
        for (String item : source) {
            if (target.contains(item) == true) {
                //일치하는 아이템을 지움
                tmpArr.remove(item);
            }
        }
        return tmpArr;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        eventChannel.setStreamHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.mActivity = binding.getActivity();
        binding.addActivityResultListener(this);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        this.mActivity = null;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationConstants.INTENT);
        notificationReceiver = new NotificationReceiver(events);
        context.registerReceiver(notificationReceiver, intentFilter);
        Intent listenerIntent = new Intent(context, NotificationReceiver.class);
        context.startService(listenerIntent);
        Log.i("NotificationPlugin", "Started the notifications tracking service.");
    }

    @Override
    public void onCancel(Object arguments) {
        context.unregisterReceiver(notificationReceiver);
        notificationReceiver = null;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_NOTIFICATIONS) {
            if (resultCode == Activity.RESULT_OK) {
                pendingResult.success(true);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if(data != null) {
                    pendingResult.success(isPermissionGranted(context));
                }
            } else {
                pendingResult.success(false);
            }
            return true;
        }
        return false;
    }
}
