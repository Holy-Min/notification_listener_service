import 'dart:typed_data';

import 'notification_listener_service.dart';

class ServiceNotificationEvent {
  /// the notification id
  int? id;

  /// check if we can reply the Notification
  bool? canReply;

  /// if the notification has an extras image
  bool? hasExtrasPicture;

  /// if the notification has been removed
  bool? hasRemoved;

  /// notification extras image
  /// To display an image simply use the [Image.memory] widget.
  /// Example:
  ///
  /// ```
  /// Image.memory(notif.extrasPicture)
  /// ```
  Uint8List? extrasPicture;

  /// notification package name
  String? packageName;

  /// notification title
  String? title;

  /// the notification app icon
  /// To display an image simply use the [Image.memory] widget.
  /// Example:
  ///
  /// ```
  /// Image.memory(notif.notificationIcon)
  /// ```
  Uint8List? notificationIcon;

  /// the content of the notification
  String? content;
  
  String? subContent;

  ServiceNotificationEvent({
    this.id,
    this.canReply,
    this.hasExtrasPicture,
    this.hasRemoved,
    this.extrasPicture,
    this.packageName,
    this.title,
    this.notificationIcon,
    this.content,
    this.subContent,
  });

  ServiceNotificationEvent.fromMap(Map<dynamic, dynamic> map) {
    id = map['id'];
    canReply = map['canReply'];
    hasExtrasPicture = map['hasExtrasPicture'];
    hasRemoved = map['hasRemoved'];
    extrasPicture = map['notificationExtrasPicture'];
    packageName = map['packageName'];
    title = map['title'];
    notificationIcon = map['notificationIcon'];
    content = map['content'];
    subContent = map['subContent'];
  }

  /// send a direct message reply to the incoming notification
  Future<bool> sendReply(String message, String defaultName) async {
    // if (!canReply!) throw Exception("The notification is not replyable");
    if (!canReply!) throw Exception("이 알림은 답장을 보낼 수 없는 상태입니다.");
    try {
      return await methodeChannel.invokeMethod<bool>("sendReply", {
            'message': message,
            'notificationId': id,           
            'title' : "구병희사원님",
            // 'room' : subContent
          }) ??
          false;
    } catch (e) {
      rethrow;
    }
  }

  @override
  String toString() {
    return '''ServiceNotificationEvent(
      id: $id
      can reply: $canReply
      packageName: $packageName
      title: $title
      content: $content
      subContent: $subContent
      hasRemoved: $hasRemoved
      hasExtrasPicture: $hasExtrasPicture
      ''';
  }
}
