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

  static Future getTelegram() async {
    final content = await methodeChannel.invokeMethod('getTelegram');
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

  static Future getTelegramRoom() async {
    final content = await methodeChannel.invokeMethod('getTelegramRoom');
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

  static Future getTelegramInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getTelegramInfo', { "roomName" : roomName});
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

  static Future selectTelegram(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectTelegram', {"roomName" : roomName});
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

  static Future detectTelegram() async {
    final content = await methodeChannel.invokeMethod('detectTelegram');
    return content;
  }

  static Future updateMessage(String result, String url, int nid, String text) async {
    await methodeChannel.invokeMethod('updateMessage', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateKakao(String result, String url, int nid, String text) async {
    await methodeChannel.invokeMethod('updateKakao', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateWhatsapp(String result, String url, int nid, String text) async {
    await methodeChannel.invokeMethod('updateWhatsapp', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateTelegram(String result, String url, int nid, String text) async {
    await methodeChannel.invokeMethod('updateTelegram', {"result" : result, "url" : url, "nid" : nid, "text" : text});
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

  static Future countTelegram(String roomName) async {
    final content = await methodeChannel.invokeMethod('countTelegram', {"room" : roomName});
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

  static Future readTelegram(String roomName) async {
    await methodeChannel.invokeMethod('readTelegram', {"room" : roomName});
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

  static Future lastTelegram(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastTelegram', {"room" : roomName});
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

  static Future lastDateTelegram(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateTelegram', {"room" : roomName});
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

  static Future totalTelegram() async {
    final content = await methodeChannel.invokeMethod('totalTelegram');
    return content;
  }

  static Future resultMessage() async {
    final content = await methodeChannel.invokeMethod('resultMessage');
    return content;

  }

  static Future resultKakao() async {
    final content = await methodeChannel.invokeMethod('resultKakao');
    return content;

  }

  static Future resultWhatsapp() async {
    final content = await methodeChannel.invokeMethod('resultWhatsapp');
    return content;
  }

  static Future resultTelegram() async {
    final content = await methodeChannel.invokeMethod('resultTelegram');
    return content;
  }

  static Future dataInsert(String name, String text, String room, String date, String vsDate, String packageName, String result, String read, String url) async {
    final content = await methodeChannel.invokeMethod('dataInsert',
        {"name" : name,
          "text" : text,
          "room" : room,
          "date" : date,
          "vsDate" : vsDate,
          "packageName" : packageName,
          "result" : result,
          "read" : read,
          "url" : url,
        });
    return content;
  }

  static Future setRunAppFalse() async {
    await methodeChannel.invokeMethod('setFalse');
  }

  static Future setRunAppTrue() async {
    await methodeChannel.invokeMethod('setTrue');
  }

  static Future deleteMessage() async {
    await methodeChannel.invokeMethod('deleteMessage');
  }

  static Future deleteKakao() async {
    await methodeChannel.invokeMethod('deleteKakao');
  }

  static Future deleteWhatsapp() async {
    await methodeChannel.invokeMethod('deleteWhatsapp');
  }

  static Future deleteTelegram() async {
    await methodeChannel.invokeMethod('deleteTelegram');
  }

  static Future messageRoomDelete(String room) async {
    await methodeChannel.invokeMethod('messageRoomDelete', {"room" : room});
  }

  static Future kakaoRoomDelete(String room) async {
    await methodeChannel.invokeMethod('kakaoRoomDelete', {"room" : room});
  }

  static Future WhatsappRoomDelete(String room) async {
    await methodeChannel.invokeMethod('WhatsappRoomDelete', {"room" : room});
  }

  static Future TelegramRoomDelete(String room) async {
    await methodeChannel.invokeMethod('TelegramRoomDelete', {"room" : room});
  }

  static Future runVacuum() async {
    await methodeChannel.invokeMethod('runVacuum');
  }

  static Future allReadMessage() async {
    await methodeChannel.invokeMethod('allReadMessage');
  }

  static Future allReadKakao() async {
    await methodeChannel.invokeMethod('allReadKakao');
  }

  static Future allReadWhatsapp() async {
    await methodeChannel.invokeMethod('allReadWhatsapp');
  }

  static Future allReadTelegram() async {
    await methodeChannel.invokeMethod('allReadTelegram');
  }

  static Future dropKakao() async {
    await methodeChannel.invokeMethod('dropKakao');
  }

  static Future dayOutMessage() async {
    await methodeChannel.invokeMethod('dayOutMessage');
  }

  static Future dayOutKakao() async {
    await methodeChannel.invokeMethod('dayOutKakao');
  }

  static Future dayOutWhatsapp() async {
    await methodeChannel.invokeMethod('dayOutWhatsapp');
  }

  static Future dayOutTelegram() async {
    await methodeChannel.invokeMethod('dayOutTelegram');
  }

  static Future dayOutMessageRoom() async {
    await methodeChannel.invokeMethod('dayOutMessageRoom');
  }

  static Future dayOutKakaoRoom() async {
    await methodeChannel.invokeMethod('dayOutKakaoRoom');
  }

  static Future dayOutWhatsappRoom() async {
    await methodeChannel.invokeMethod('dayOutWhatsappRoom');
  }

  static Future dayOutTelegramRoom() async {
    await methodeChannel.invokeMethod('dayOutTelegramRoom');
  }

  static Future examRoomInsert(String app) async {
    await methodeChannel.invokeMethod('examRoomInsert', {"app" : app});
  }

  static Future updateMessageRoom(String room) async {
    await methodeChannel.invokeMethod('updateMessageRoom', {"room" : room});
  }

  static Future updateKakaoRoom(String room) async {
    await methodeChannel.invokeMethod('updateKakaoRoom', {"room" : room});
  }

  static Future updateWhatsappRoom(String room) async {
    await methodeChannel.invokeMethod('updateWhatsappRoom', {"room" : room});
  }

  static Future updateTelegramRoom(String room) async {
    await methodeChannel.invokeMethod('updateTelegramRoom', {"room" : room});
  }
  
  static Future detectLastMessage() async {
    final content = await methodeChannel.invokeMethod('detectLastMessage');
    return content;
  }
  
  static Future detectLastKakao() async {
    final content = await methodeChannel.invokeMethod('detectLastKakao');
    return content;
  }
  
  static Future detectLastWhatsapp() async {
    final content = await methodeChannel.invokeMethod('detectLastWhatsapp');
    return content;
  }

  static Future detectLastTelegram() async {
    final content = await methodeChannel.invokeMethod('detectLastTelegram');
    return content;
  }

  static Future changeMessageList() async {
    await methodeChannel.invokeMethod('changeMessageList');
  }

  static Future countMessageList() async {
    final count = await methodeChannel.invokeMethod('countMessageList');
    return count;
  }
}
