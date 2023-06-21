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
    return content;
  }

  static Future getKakao() async {
    final content = await methodeChannel.invokeMethod('getKakao');
    return content;
  }

  static Future getWhatsapp() async {
    final content = await methodeChannel.invokeMethod('getWhatsapp');
    return content;
  }

  static Future getTelegram() async {
    final content = await methodeChannel.invokeMethod('getTelegram');
    return content;
  }

  static Future getLine() async {
    final content = await methodeChannel.invokeMethod('getLine');
    return content;
  }

  static Future getInstagram() async {
    final content = await methodeChannel.invokeMethod('getInstagram');
    return content;
  }

  static Future getFacebook() async {
    final content = await methodeChannel.invokeMethod('getFacebook');
    return content;
  }

  static Future getRoom() async {
    final content = await methodeChannel.invokeMethod('getRoom');
    return content;
  }

  static Future getKakaoRoom() async {
    final content = await methodeChannel.invokeMethod('getKakaoRoom');
    return content;
  }

  static Future getWhatsappRoom() async {
    final content = await methodeChannel.invokeMethod('getWhatsappRoom');
    return content;
  }

  static Future getTelegramRoom() async {
    final content = await methodeChannel.invokeMethod('getTelegramRoom');
    return content;
  }

  static Future getLineRoom() async {
    final content = await methodeChannel.invokeMethod('getLineRoom');
    return content;
  }

  static Future getInstagramRoom() async {
    final content = await methodeChannel.invokeMethod('getInstagramRoom');
    return content;
  }

  static Future getFacebookRoom() async {
    final content = await methodeChannel.invokeMethod('getFacebookRoom');
    return content;
  }

  static Future getChatInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getChatInfo', { "roomName" : roomName});
    return content;
  }

  static Future getKakaoInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getKakaoInfo', { "roomName" : roomName});
    return content;
  }

  static Future getWhatsappInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getWhatsappInfo', { "roomName" : roomName});
    return content;
  }

  static Future getTelegramInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getTelegramInfo', { "roomName" : roomName});
    return content;
  }

  static Future getLineInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getLineInfo', { "roomName" : roomName});
    return content;
  }

  static Future getInstagramInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getInstagramInfo', { "roomName" : roomName});
    return content;
  }

  static Future getFacebookInfo(String roomName) async {
    final content = await methodeChannel.invokeMethod('getFacebookInfo', { "roomName" : roomName});
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

  static Future selectLine(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectLine', {"roomName" : roomName});
    return content;
  }

  static Future selectInstagram(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectInstagram', {"roomName" : roomName});
    return content;
  }

  static Future selectFacebook(String roomName) async {
    final content = await methodeChannel.invokeMethod('selectFacebook', {"roomName" : roomName});
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

  static Future detectLine() async {
    final content = await methodeChannel.invokeMethod('detectLine');
    return content;
  }

  static Future detectInstagram() async {
    final content = await methodeChannel.invokeMethod('detectInstagram');
    return content;
  }

  static Future detectFacebook() async {
    final content = await methodeChannel.invokeMethod('detectFacebook');
    return content;
  }

  static Future updateMessage(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateMessage', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateKakao(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateKakao', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateWhatsapp(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateWhatsapp', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateTelegram(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateTelegram', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateLine(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateLine', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateInstagram(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateIntagram', {"result" : result, "url" : url, "nid" : nid, "text" : text});
  }

  static Future updateFacebook(String result, String url, int nid, String text) async {
    return await methodeChannel.invokeMethod('updateFacebook', {"result" : result, "url" : url, "nid" : nid, "text" : text});
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

  static Future countLine(String roomName) async {
    final content = await methodeChannel.invokeMethod('countLine', {"room" : roomName});
    return content;
  }

  static Future countInstagram(String roomName) async {
    final content = await methodeChannel.invokeMethod('countInstagram', {"room" : roomName});
    return content;
  }

  static Future countFacebook(String roomName) async {
    final content = await methodeChannel.invokeMethod('countFacebook', {"room" : roomName});
    return content;
  }

  static Future readMessage(String roomName) async {
    return await methodeChannel.invokeMethod('readMessage', {"room" : roomName});
  }

  static Future readKakao(String roomName) async {
    return await methodeChannel.invokeMethod('readKakao', {"room" : roomName});
  }

  static Future readWhatsapp(String roomName) async {
    return await methodeChannel.invokeMethod('readWhatsapp', {"room" : roomName});
  }

  static Future readTelegram(String roomName) async {
    return await methodeChannel.invokeMethod('readTelegram', {"room" : roomName});
  }

  static Future readLine(String roomName) async {
    return await methodeChannel.invokeMethod('readLine', {"room" : roomName});
  }

  static Future readInstagram(String roomName) async {
    return await methodeChannel.invokeMethod('readInstagram', {"room" : roomName});
  }

  static Future readFacebook(String roomName) async {
    return await methodeChannel.invokeMethod('readFacebook', {"room" : roomName});
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

  static Future lastLine(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastLine', {"room" : roomName});
    return content;
  }

  static Future lastInstagram(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastInstagram', {"room" : roomName});
    return content;
  }

  static Future lastFacebook(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastFacebook', {"room" : roomName});
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

  static Future lastDateLine(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateLine', {"room" : roomName});
    return content;
  }

  static Future lastDateInstagram(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateInstagram', {"room" : roomName});
    return content;
  }

  static Future lastDateFacebook(String roomName) async {
    final content = await methodeChannel.invokeMethod('lastDateFacebook', {"room" : roomName});
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

  static Future totalLine() async {
    final content = await methodeChannel.invokeMethod('totalLine');
    return content;
  }

  static Future totalInstagram() async {
    final content = await methodeChannel.invokeMethod('totalInstagram');
    return content;
  }

  static Future totalFacebook() async {
    final content = await methodeChannel.invokeMethod('totalFacebook');
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

  static Future resultLine() async {
    final content = await methodeChannel.invokeMethod('resultLine');
    return content;
  }

  static Future resultInstagram() async {
    final content = await methodeChannel.invokeMethod('resultInstagram');
    return content;
  }

  static Future resultFacebook() async {
    final content = await methodeChannel.invokeMethod('resultFacebook');
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

  // static Future setRunAppFalse() async {
  //   await methodeChannel.invokeMethod('setFalse');
  // }
  //
  // static Future setRunAppTrue() async {
  //   await methodeChannel.invokeMethod('setTrue');
  // }

  static Future deleteMessage() async {
    return await methodeChannel.invokeMethod('deleteMessage');
  }

  static Future deleteKakao() async {
    return await methodeChannel.invokeMethod('deleteKakao');
  }

  static Future deleteWhatsapp() async {
    return await methodeChannel.invokeMethod('deleteWhatsapp');
  }

  static Future deleteTelegram() async {
    return await methodeChannel.invokeMethod('deleteTelegram');
  }

  static Future deleteLine() async {
    return await methodeChannel.invokeMethod('deleteLine');
  }

  static Future deleteInstagram() async {
    return await methodeChannel.invokeMethod('deleteInstagram');
  }

  static Future deleteFacebook() async {
    return await methodeChannel.invokeMethod('deleteFacebook');
  }

  static Future messageRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('messageRoomDelete', {"room" : room});
  }

  static Future kakaoRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('kakaoRoomDelete', {"room" : room});
  }

  static Future WhatsappRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('WhatsappRoomDelete', {"room" : room});
  }

  static Future TelegramRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('TelegramRoomDelete', {"room" : room});
  }

  static Future LineRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('LineRoomDelete', {"room" : room});
  }

  static Future InstagramRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('InstagramRoomDelete', {"room" : room});
  }

  static Future FacebookRoomDelete(String room) async {
    return await methodeChannel.invokeMethod('FacebookRoomDelete', {"room" : room});
  }

  static Future runVacuum() async {
    return await methodeChannel.invokeMethod('runVacuum');
  }

  static Future allReadMessage() async {
    return await methodeChannel.invokeMethod('allReadMessage');
  }

  static Future allReadKakao() async {
    return await methodeChannel.invokeMethod('allReadKakao');
  }

  static Future allReadWhatsapp() async {
    return await methodeChannel.invokeMethod('allReadWhatsapp');
  }

  static Future allReadTelegram() async {
    return await methodeChannel.invokeMethod('allReadTelegram');
  }

  static Future allReadLine() async {
    return await methodeChannel.invokeMethod('allReadLine');
  }

  static Future allReadInstagram() async {
    return await methodeChannel.invokeMethod('allReadInstagram');
  }

  static Future allReadFacebook() async {
    return await methodeChannel.invokeMethod('allReadFacebook');
  }

  // static Future dropKakao() async {
  //   await methodeChannel.invokeMethod('dropKakao');
  // }

  static Future dayOutMessage() async {
    return await methodeChannel.invokeMethod('dayOutMessage');
  }

  static Future dayOutKakao() async {
    return await methodeChannel.invokeMethod('dayOutKakao');
  }

  static Future dayOutWhatsapp() async {
    return await methodeChannel.invokeMethod('dayOutWhatsapp');
  }

  static Future dayOutTelegram() async {
    return await methodeChannel.invokeMethod('dayOutTelegram');
  }

  static Future dayOutLine() async {
    return await methodeChannel.invokeMethod('dayOutLine');
  }

  static Future dayOutInstagram() async {
    return await methodeChannel.invokeMethod('dayOutInstagram');
  }

  static Future dayOutFacebook() async {
    return await methodeChannel.invokeMethod('dayOutFacebook');
  }

  static Future dayOutMessageRoom() async {
    return await methodeChannel.invokeMethod('dayOutMessageRoom');
  }

  static Future dayOutKakaoRoom() async {
    return await methodeChannel.invokeMethod('dayOutKakaoRoom');
  }

  static Future dayOutWhatsappRoom() async {
    return await methodeChannel.invokeMethod('dayOutWhatsappRoom');
  }

  static Future dayOutTelegramRoom() async {
    return await methodeChannel.invokeMethod('dayOutTelegramRoom');
  }

  static Future dayOutLineRoom() async {
    return await methodeChannel.invokeMethod('dayOutLineRoom');
  }

  static Future dayOutInstagramRoom() async {
    return await methodeChannel.invokeMethod('dayOutInstagramRoom');
  }

  static Future dayOutFacebookRoom() async {
    return await methodeChannel.invokeMethod('dayOutFacebookRoom');
  }

  static Future examRoomInsert(String app) async {
    return await methodeChannel.invokeMethod('examRoomInsert', {"app" : app});
  }

  static Future updateMessageRoom(String room) async {
    return await methodeChannel.invokeMethod('updateMessageRoom', {"room" : room});
  }

  static Future updateKakaoRoom(String room) async {
    return await methodeChannel.invokeMethod('updateKakaoRoom', {"room" : room});
  }

  static Future updateWhatsappRoom(String room) async {
    return await methodeChannel.invokeMethod('updateWhatsappRoom', {"room" : room});
  }

  static Future updateTelegramRoom(String room) async {
    return await methodeChannel.invokeMethod('updateTelegramRoom', {"room" : room});
  }

  static Future updateLineRoom(String room) async {
    return await methodeChannel.invokeMethod('updateLineRoom', {"room" : room});
  }

  static Future updateInstagramRoom(String room) async {
    return await methodeChannel.invokeMethod('updateInstagramRoom', {"room" : room});
  }

  static Future updateFacebookRoom(String room) async {
    return await methodeChannel.invokeMethod('updateFacebookRoom', {"room" : room});
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

  static Future detectLastLine() async {
    final content = await methodeChannel.invokeMethod('detectLastLine');
    return content;
  }

  static Future detectLastInstagram() async {
    final content = await methodeChannel.invokeMethod('detectLastInstagram');
    return content;
  }

  static Future detectLastFacebook() async {
    final content = await methodeChannel.invokeMethod('detectLastFacebook');
    return content;
  }

  static Future changeMessageList(List message) async {
    return await methodeChannel.invokeMethod('changeMessageList', {"message" : message});
  }

  static Future countMessageList() async {
    final count = await methodeChannel.invokeMethod('countMessageList');
    return count;
  }
  
  static Future getMessageList() async {
    final content = await methodeChannel.invokeMethod('getMessageList');
    return content;
  }
}
