//package com.example.pc.rob;
//import android.accessibilityservice.AccessibilityService;
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.os.Build;
//import android.os.Handler;
//import android.util.Log;
//import android.view.accessibility.AccessibilityEvent;
//import android.view.accessibility.AccessibilityManager;
//import android.view.accessibility.AccessibilityNodeInfo;
//import android.widget.Toast;
//
//import java.util.List;
//
///**
// * <p>Created by Administrator</p>
// * <p/>
// * 抢红包外挂服务
// */
//public class RobMoney extends AccessibilityService {
//
//    static final String TAG = "robmoney";
//
//    /**
//     * 微信的包名
//     */
//    static final String WECHAT_PACKAGENAME = "com.tencent.moblieqq";
//    /**
//     * 红包消息的关键字
//     */
//    static final String ENVELOPE_TEXT_KEY = "点击拆开";
//
//    Handler handler = new Handler();
//
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//        final int eventType = event.getEventType();
//        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
//  //      AccessibilityEvent event2 = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
////        final int eventType2 = event2.getEventType();
////        //    Log.d(TAG, "事件---->" + event);
////        Log.d("manager", "内容---->" + manager);
////        Log.d(eventType+" ???", "内容---->" + event.getText());
////    //    Log.d(eventType2+" 222", "内容---->" + event2.getText());
//
//        //通知栏事件
//        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
//            List<CharSequence> texts = event.getText();
//            Log.d(TAG, "内容---->" + event.getText());
//            if (!texts.isEmpty()) {
//                for (CharSequence t : texts) {
//                    String text = String.valueOf(t);
//                    if (text.contains(ENVELOPE_TEXT_KEY)) {
//                        openNotification(event);
//                        break;
//                    }
//                }
//            }
//        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            openEnvelope(event);
//        }
//    }
//
//    /*@Override
//    protected boolean onKeyEvent(KeyEvent event) {
//        //return super.onKeyEvent(event);
//        return true;
//    }*/
//
//    @Override
//    public void onInterrupt() {
//        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        Toast.makeText(this, "连接抢红包服务", Toast.LENGTH_SHORT).show();
//    }
//
//    private void sendNotificationEvent() {
//        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
//        if (!manager.isEnabled()) {
//            return;
//        }
//        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
//        event.setPackageName(WECHAT_PACKAGENAME);
//        event.setClassName(Notification.class.getName());
//        CharSequence tickerText = ENVELOPE_TEXT_KEY;
//        event.getText().add(tickerText);
//        manager.sendAccessibilityEvent(event);
//    }
//
//    /**
//     * 打开通知栏消息
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void openNotification(AccessibilityEvent event) {
//        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
//            return;
//        }
//        //以下是精华，将微信的通知栏消息打开
//        Notification notification = (Notification) event.getParcelableData();
//        PendingIntent pendingIntent = notification.contentIntent;
//        try {
//            pendingIntent.send();
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void openEnvelope(AccessibilityEvent event) {
//        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
//            //点中了红包，下一步就是去拆红包
//            checkKey1();
//        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
//            //拆完红包后看详细的纪录界面
//            //nonething
//        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
//            //在聊天界面,去点中红包
//            checkKey2();
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void checkKey1() {
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//        if (nodeInfo == null) {
//            Log.w(TAG, "rootWindow为空");
//            return;
//        }
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
//        for (AccessibilityNodeInfo n : list) {
//            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void checkKey2() {
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//        if (nodeInfo == null) {
//            Log.w(TAG, "rootWindow为空");
//            return;
//        }
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
//        if (list.isEmpty()) {
//            list = nodeInfo.findAccessibilityNodeInfosByText(ENVELOPE_TEXT_KEY);
//            for (AccessibilityNodeInfo n : list) {
//                Log.i(TAG, "-->红包:" + n);
//                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                break;
//            }
//        } else {
//            //最新的红包领起
//            for (int i = list.size() - 1; i >= 0; i--) {
//                AccessibilityNodeInfo parent = list.get(i).getParent();
//                Log.i(TAG, "-->领取红包:" + parent);
//                if (parent != null) {
//                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    break;
//                }
//            }
//        }
//    }
//}