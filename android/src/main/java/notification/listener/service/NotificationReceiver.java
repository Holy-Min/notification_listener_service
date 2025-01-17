package notification.listener.service;

import static notification.listener.service.NotificationConstants.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;

import androidx.annotation.RequiresApi;

import io.flutter.plugin.common.EventChannel.EventSink;

import java.util.HashMap;

public class NotificationReceiver extends BroadcastReceiver {

    private EventSink eventSink;

    public NotificationReceiver(EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra(PACKAGE_NAME);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);
        String subContent = intent.getStringExtra(NOTIFICATION_SUBCONTENT);
        String tag = intent.getStringExtra(TAG);
//        byte[] notificationIcon = intent.getByteArrayExtra(NOTIFICATIONS_ICON);
        byte[] notificationExtrasPicture = intent.getByteArrayExtra(EXTRAS_PICTURE);
        boolean hasExtrasPicture = intent.getBooleanExtra(HAS_EXTRAS_PICTURE, false);
        boolean hasRemoved = intent.getBooleanExtra(IS_REMOVED, false);
        boolean canReply = intent.getBooleanExtra(CAN_REPLY, false);
        int id = intent.getIntExtra(ID, -1);


        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("tag", tag);
        data.put("packageName", packageName);
        data.put("title", title);
        data.put("content", content);
        data.put("subContent", subContent);
//        data.put("notificationIcon", notificationIcon);
        data.put("notificationExtrasPicture", notificationExtrasPicture);
        data.put("hasExtrasPicture", hasExtrasPicture);
        data.put("hasRemoved", hasRemoved);
        data.put("canReply", canReply);

        eventSink.success(data);
    }
}
