import 'dart:async';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:notification_listener_service/notification_event.dart';

const MethodChannel methodeChannel =
    MethodChannel('x-slayer/notifications_channel');
const EventChannel _eventChannel = EventChannel('x-slayer/notifications_event');
Stream<ServiceNotificationEvent>? _stream;

class NotificationListenerService {
  NotificationListenerService._();

  /// stream the incoming notifications events
  static Stream<ServiceNotificationEvent> get notificationsStream {
    if (Platform.isAndroid) {
      _stream ??=
          _eventChannel.receiveBroadcastStream().map<ServiceNotificationEvent>(
                (event) => ServiceNotificationEvent.fromMap(event),
              );
      return _stream!;
    }
    throw Exception("Notifications API exclusively available on Android!");
  }

  /// request notification permission
  /// it will open the notification settings page and return `true` once the permission granted.
  static Future<bool> requestPermission() async {
    try {
      return await methodeChannel.invokeMethod('requestPermission');
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  /// check if notification permission is enebaled
  static Future<bool> isPermissionGranted() async {
    try {
      return await methodeChannel.invokeMethod('isPermissionGranted');
    } on PlatformException catch (error) {
      log("$error");
      return false;
    }
  }

  static Future getKakaoChat() async {
    final content = await methodeChannel.invokeMethod('getKakaoChat');
    // final kakao = content.toString();
    return content;
  }

  static Future getKakaoRoom() async {
    final content = await methodeChannel.invokeMethod('getKakaoRoom');
    // final kakao = content.toString();
    return content;
  }

  static Future getChatInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getChatInfo', { "roomName" : roomName});
    // final kakao = content.toString();
    return content;
  }

  static Future dataInsert(String name, String text, String room, String date) async {
    final content = await methodeChannel.invokeMethod('dataInsert',
        {"name" : name,
          "text" : text,
          "room" : room,
          "date" : date,});
    // final kakao = content.toString();
    return content;
  }

  static Future setRunAppFalse() async {
    await methodeChannel.invokeMethod('setFalse');
    // final kakao = content.toString();
  }

  static Future setRunAppTrue() async {
    await methodeChannel.invokeMethod('setTrue');
    // final kakao = content.toString();
  }
}
