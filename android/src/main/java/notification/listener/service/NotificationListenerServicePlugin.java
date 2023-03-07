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

//            final Action action = ActionCache.cachedNotifications.get(notificationId);
            final Action action = ActionCache.cachedNotifications.get(tag);
            if (action == null) {
                result.error("Notification", "Can't find this cached notification", null);
            }
            try {
                action.sendReply(context, message);
                if(packageName.contains("messaging") || packageName.contains("messenger")) {
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

                    if(hasRemoved == false && noti.app.equals(packageName)) notiDb.NotiDao().insert(noti);
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

                    if(hasRemoved == false && kakaonoti.app.equals("2")) notiDb.KakaoDao().insert(kakaonoti);
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

                    if(hasRemoved == false && whatsappnoti.app.equals("3")) notiDb.WhatsappDao().insert(whatsappnoti);
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

        }else if (call.method.equals("getWhatsappRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappRoomData> noti = notiDb.WhatsappRoomDataDao().getAll();
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("getChatInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("getKakaoInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<KakaoData> noti = notiDb.KakaoDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("getWhatsappInfo")) {
            String room = call.argument("roomName");

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<WhatsappData> noti = notiDb.WhatsappDao().getRoom(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectMessage")) {
            String room = call.argument("roomName");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<NotiData> noti = notiDb.NotiDao().select(room);
            Gson gson = new Gson();
            String jsonString = gson.toJson(noti);
            result.success(jsonString);

        }else if (call.method.equals("selectKakao")) {
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

        }else if (call.method.equals("updateMessage")) {
            String str = call.argument("result");
            String url = call.argument("url");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().update(str, url);
            result.success(true);

        }else if (call.method.equals("updateKakao")) {
            String str = call.argument("result");
            String url = call.argument("url");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().update(str, url);
            result.success(true);

        }else if (call.method.equals("updateWhatsapp")) {
            String str = call.argument("result");
            String url = call.argument("url");
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().update(str, url);
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
//             if(packageName.equals(defaultSMS)) {
//           if(packageName.contains("messaging")) {
            if(packageName.contains("messaging") || packageName.contains("messenger")) {
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
            }

//                 if(packageName.equals("com.samsung.android.messaging")) {

            result.success(true);


        }else if (call.method.equals("setFalse")) {
            NotificationListener nl = new NotificationListener();
            nl.setRunAppFalse();
            System.out.println("앱 실행 변수 확인" + nl.runApp);
            result.success(true);

        }else if (call.method.equals("setTrue")) {
            NotificationListener nl = new NotificationListener();
            nl.setRunAppTrue();
            System.out.println("앱 실행 변수 확인" + nl.runApp);
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

        }else if (call.method.equals("runVacuum")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().vacuum(new SimpleSQLiteQuery("VACUUM"));
//            notiDb.NotiDao().vacuum();
            result.success(true);

        }else if (call.method.equals("allReadMessage")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().allRead();
//            notiDb.NotiDao().vacuum();
            result.success(true);

        }else if (call.method.equals("allReadKakao")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.KakaoDao().allRead();
//            notiDb.NotiDao().vacuum();
            result.success(true);

        }else if (call.method.equals("allReadWhatsapp")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.WhatsappDao().allRead();
//            notiDb.NotiDao().vacuum();
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

        }else if (call.method.equals("examRoomInsert")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            final String app = call.argument("app");
            RoomData roomData = new RoomData();
            roomData.room = "예시";
            roomData.app = app;
            roomData.vsDate = "2999-01-01";
            notiDb.RoomDataDao().insert(roomData);
            result.success(true);

        }else {
            result.notImplemented();
        }
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
