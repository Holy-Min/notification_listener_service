package notification.listener.service;

import static notification.listener.service.NotificationUtils.isPermissionGranted;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import java.util.List;
import com.google.gson.Gson;

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
            hasRemoved = call.argument("hasRemoved");

            LocalDateTime now = LocalDateTime.now();
            String formatedNow = now.format(DateTimeFormatter.ofPattern("a hh시 mm분"));
            String formatedNow2 = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            NotiData noti = new NotiData();
            noti.name = name;
            noti.text = message;
            noti.room = room;
            noti.date = formatedNow;
            noti.vsDate = formatedNow2;
            noti.send = 2;
            if(packageName.equals("com.kakao.talk")) {
                noti.app = 1;
            } else if(packageName.equals("com.whatsapp")) {
                noti.app = 2;
            }


//            final Action action = ActionCache.cachedNotifications.get(notificationId);
            final Action action = ActionCache.cachedNotifications.get(tag);
            if (action == null) {
                result.error("Notification", "Can't find this cached notification", null);
            }
            try {
                action.sendReply(context, message);
                if(hasRemoved == false) notiDb.NotiDao().insert(noti);
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

        } else if (call.method.equals("getRoom")) {

            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            List<RoomData> noti = notiDb.RoomDataDao().getAll();
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

        }else if (call.method.equals("dataInsert")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());

            String name = call.argument("name");
            String text = call.argument("text");
            String room = call.argument("room");
            String date = call.argument("date");
            String vsDate = call.argument("vsDate");
            String packageName = call.argument("packageName");
            
            if(!name.euals("카카오톡")) {
                NotiData noti = new NotiData();
                noti.name = name;
                noti.text = text;
                noti.room = room;
                noti.date = date;
                noti.vsDate = vsDate;
                noti.send = 1;
                if(packageName.equals("com.kakao.talk")) {
                    noti.app = 1;
                } else if(packageName.equals("com.whatsapp")) {
                    noti.app = 2;
                }

                notiDb.NotiDao().insert(noti);
                result.success(true);
            }

        }else if (call.method.equals("setFalse")) {
            NotificationListener nl = new NotificationListener();
            nl.setRunAppFalse();
            result.success(null);

        }else if (call.method.equals("setTrue")) {
            NotificationListener nl = new NotificationListener();
            nl.setRunAppTrue();
            result.success(true);

        }else if (call.method.equals("deleteChat")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            notiDb.NotiDao().delete();
            result.success(true);

        }else if (call.method.equals("roomDelete")) {
            notiDb = NotiDatabase.getInstance(context.getApplicationContext());
            String room = call.argument("room");
            notiDb.NotiDao().roomDelete(room);
            notiDb.RoomDataDao().delete(room);
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
