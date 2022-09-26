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

  static Future getChat() async {
    final content = await methodeChannel.invokeMethod('getChat');
    // final kakao = content.toString();
    return content;
  }

  static Future getKakao() async {
    final content = await methodeChannel.invokeMethod('getKakao');
    // final kakao = content.toString();
    return content;
  }

  static Future getWhatsapp() async {
    final content = await methodeChannel.invokeMethod('getWhatsapp');
    // final kakao = content.toString();
    return content;
  }

  static Future getRoom() async {
    final content = await methodeChannel.invokeMethod('getRoom');
    // final kakao = content.toString();
    return content;
  }

  static Future getKakaoRoom() async {
    final content = await methodeChannel.invokeMethod('getKakaoRoom');
    // final kakao = content.toString();
    return content;
  }

  static Future getWhatsappRoom() async {
    final content = await methodeChannel.invokeMethod('getWhatsappRoom');
    // final kakao = content.toString();
    return content;
  }

  static Future getChatInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getChatInfo', { "roomName" : roomName});
    // final kakao = content.toString();
    return content;
  }

  static Future getKakaoInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getKakaoInfo', { "roomName" : roomName});
    // final kakao = content.toString();
    return content;
  }

  static Future getWhatsappInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getWhatsappInfo', { "roomName" : roomName});
    // final kakao = content.toString();
    return content;
  }

  static Future selectMessage(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectMessage', {"roomName" : roomName});
    return content;
  }

  static Future selectKakao(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectKakao', {"roomName" : roomName});
    return content;
  }

  static Future selectWhatsapp(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectWhatsapp', {"roomName" : roomName});
    return content;
  }

  static Future detectMessage() async {
    final content = await methodeChannel.invokeMethod('detectMessage');
    return content;
  }

  static Future detectKakao() async {
    final content = await methodeChannel.invokeMethod('detectKakao');
    return content;
  }

  static Future detectWhatsapp() async {
    final content = await methodeChannel.invokeMethod('detectWhatsapp');
    return content;
  }

  static Future updateMessage(String result) async {
    await methodeChannel.invokeMethod('updateMessage', {"result" : result});
  }

  static Future updateKakao(String result) async {
    await methodeChannel.invokeMethod('updateKakao', {"result" : result});
  }

  static Future updateWhatsapp(String result) async {
    await methodeChannel.invokeMethod('updateWhatsapp', {"result" : result});
  }

  static Future countMessage(String roomName) async {
    final content = await methodeChannel.invokeMethod('countMessage', {"room" : roomName});
    return content;
  }

  static Future countKakao(String roomName) async {
    final content = await methodeChannel.invokeMethod('countKakao', {"room" : roomName});
    return content;
  }

  static Future countWhatsapp(String roomName) async {
    final content = await methodeChannel.invokeMethod('countWhatsapp', {"room" : roomName});
    return content;
  }

  static Future readMessage(String roomName) async {
    await methodeChannel.invokeMethod('readMessage', {"room" : roomName});
  }

  static Future readKakao(String roomName) async {
    await methodeChannel.invokeMethod('readKakao', {"room" : roomName});
  }

  static Future readWhatsapp(String roomName) async {
    await methodeChannel.invokeMethod('readWhatsapp', {"room" : roomName});
  }

  static Future lastMessage(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastMessage', {"room" : roomName});
    return content;
  }

  static Future lastKakao(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastKakao', {"room" : roomName});
    return content;
  }

  static Future lastWhatsapp(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastWhatsapp', {"room" : roomName});
    return content;
  }

  static Future lastDateMessage(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateMessage', {"room" : roomName});
    return content;
  }

  static Future lastDateKakao(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateKakao', {"room" : roomName});
    return content;
  }

  static Future lastDateWhatsapp(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateWhatsapp', {"room" : roomName});
    return content;
  }

  static Future totalMessage() async {
    final content = await methodeChannel.invokeMethod('totalMessage');
    return content;
  }

  static Future totalKakao() async {
    final content = await methodeChannel.invokeMethod('totalKakao');
    return content;
  }

  static Future totalWhatsapp() async {
    final content = await methodeChannel.invokeMethod('totalWhatsapp');
    return content;
  }

  static Future resultMessage() async {
    final content = await methodeChannel.invokeMethod('resultMessage');
    return content;

  }static Future resultKakao() async {
    final content = await methodeChannel.invokeMethod('resultKakao');
    return content;

  }static Future resultWhatsapp() async {
    final content = await methodeChannel.invokeMethod('resultWhatsapp');
    return content;
  }

  static Future dataInsert(String name, String text, String room, String date, String vsDate, String packageName, String result, String read) async {
    final content = await methodeChannel.invokeMethod('dataInsert',
        {"name" : name,
          "text" : text,
          "room" : room,
          "date" : date,
          "vsDate" : vsDate,
          "packageName" : packageName,
          "result" : result,
          "read" : read
        });
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

  static Future deleteChat() async {
    await methodeChannel.invokeMethod('deleteChat');
  }

  static Future roomDelete(String room) async {
    await methodeChannel.invokeMethod('roomDelete', {"room" : room});
  }
}
