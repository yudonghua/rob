package com.example.pc.rob;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class QQHongbaoService extends AccessibilityService {
  private static final String WECHAT_OPEN_EN = "Open";    
  private static final String WECHAT_OPENED_EN = "You've opened";    
  private final static String QQ_DEFAULT_CLICK_OPEN = "点击拆开";   
  private final static String QQ_HONG_BAO_PASSWORD = "口令红包";    
  private final static String QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令";   
  private boolean mLuckyMoneyReceived;    
  private String lastFetchedHongbaoId = null;    
  private long lastFetchedTime = 0;    
  private static final int MAX_CACHE_TOLERANCE = 5000;    
  private AccessibilityNodeInfo rootNodeInfo;
  private List<AccessibilityNodeInfo> mReceiveNode;

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public void recycle(AccessibilityNodeInfo info) {
      if(info==null)return;
      //Log.d("--》",info.toString());
 //     if(info!=null)Log.d("info-->",info.toString());

//      if(info.getClassName()!=null)
//          if (info.getClassName().toString().equals("android.widget.TextView")) {
//              if(info.getText()!=null&&info.getParent()!=null)
//              if(info.getText().toString().charAt(0)>='1'&&info.getText().toString().charAt(0)<='9')
//                  info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//
//          }

      if (info.getChildCount() == 0) {
      /*这个if代码的作用是：匹配“点击输入口令的节点，并点击这个节点”*/
        if(info.getText()!=null&&info.getText().toString().length()>0)
      if(info.getText().toString().equals(QQ_CLICK_TO_PASTE_PASSWORD)) {
          if(info.getParent()!=null)info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
       }

      /*这个if代码的作用是：匹配文本编辑框后面的发送按钮，并点击发送口令*/
        if(info.getClassName()!=null&&info.getText()!=null)
      if (info.getClassName().toString().equals("android.widget.Button") && info.getText().toString().equals("发送")) {
        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
       }
    } else {            
        for (int i = 0; i < info.getChildCount(); i++) {             
           if (info.getChild(i) != null) {                      
              recycle(info.getChild(i));              
            }            
         }       
       }    
    }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)    
  @Override    
  public void onAccessibilityEvent(AccessibilityEvent event) {
    this.rootNodeInfo = event.getSource();
      recycle(rootNodeInfo);
//      if(rootNodeInfo!=null){
  //        recycle(rootNodeInfo);
//     //     Log.d("rootNodeInfo-->",rootNodeInfo.toString());
//          if(rootNodeInfo.getParent()!=null){
//        //      Log.d("parentrootNodeInfo-->",rootNodeInfo.getParent().toString());
//              recycle(rootNodeInfo.getParent());
//              if(rootNodeInfo.getParent().getParent()!=null){
//                  recycle(rootNodeInfo.getParent().getParent());
//         //         Log.d("childrootNodeInfo-->",rootNodeInfo.getParent().getParent().toString()+"");
//                  if(rootNodeInfo.getParent().getParent().getParent()!=null){
//                      recycle(rootNodeInfo.getParent().getParent().getParent());
//                      //         Log.d("childrootNodeInfo-->",rootNodeInfo.getParent().getParent().toString()+"");
//                  }
//              }
//
//          }
//
//      }


//      if(rootNodeInfo!=null){
//       //   Log.d("rootNodeInfo-->",rootNodeInfo.toString());
//          if(rootNodeInfo.getParent()!=null)Log.d("parentrootNodeInfo-->",rootNodeInfo.getParent().toString());
//      //    Log.d("childCount-->",rootNodeInfo.getChildCount()+"");
//          for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
//              if (rootNodeInfo.getChild(i) != null) {
//                  if(rootNodeInfo.getChild(i).getText() != null)
//          //        Log.d("childrootNodeInfo-->",rootNodeInfo.getChild(i).getText().toString()+"");
//              }
//          }
//      }
    if (rootNodeInfo == null) {            
      return;       
     }        
    mReceiveNode = null;       
    checkNodeInfo();        
    /* 如果已经接收到红包并且还没有戳开 */       
    if (mLuckyMoneyReceived && (mReceiveNode != null)) {           
       int size = mReceiveNode.size();           
       if (size > 0) {               
           String id = getHongbaoText(mReceiveNode.get(size - 1));                
           long now = System.currentTimeMillis();              
           if (this.shouldReturn(id, now - lastFetchedTime))                      
              return;               
           lastFetchedHongbaoId = id;                
           lastFetchedTime = now;                
           AccessibilityNodeInfo cellNode = mReceiveNode.get(size - 1);                  
           if (cellNode.getText().toString().equals("口令红包已拆开")) {                    
              return;                
           }                
           cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);            
           if (cellNode.getText().toString().equals(QQ_HONG_BAO_PASSWORD)) {
                 AccessibilityNodeInfo rowNode = getRootInActiveWindow();                   
           if (rowNode == null) {                      
              Log.e("ddd", "noteInfo is　null");
              return;                    
            } else {                     
                 recycle(rowNode);                  
            }                
         }
         mLuckyMoneyReceived = false;            
          }       
       }  
    }

  private void checkNodeInfo() {    
    if (rootNodeInfo == null) {      
        return;    
    }     
    /* 聊天会话窗口，遍历节点匹配“点击拆开”，“口令红包”，“点击输入口令” */   
    List<AccessibilityNodeInfo> nodes1 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{QQ_DEFAULT_CLICK_OPEN, QQ_HONG_BAO_PASSWORD, QQ_CLICK_TO_PASTE_PASSWORD, "发送"});    
    if (!nodes1.isEmpty()) {      
        String nodeId = Integer.toHexString(System.identityHashCode(this.rootNodeInfo));        
        if (!nodeId.equals(lastFetchedHongbaoId)) {    
        mLuckyMoneyReceived = true;           
        mReceiveNode = nodes1;       
         }        return;    
      }
    }

  private String getHongbaoText(AccessibilityNodeInfo node) {  
      /* 获取红包上的文本 */    
      String content;   
      try {        
          AccessibilityNodeInfo i = node.getParent().getChild(0);        
          content = i.getText().toString();
          } catch (NullPointerException npe) {   
                 return null;    
              }    
          return content;
    }

   private boolean shouldReturn(String id, long duration) {   
       // ID为空    
      if (id == null) return true;    
      // 名称和缓存不一致  
      if (duration < MAX_CACHE_TOLERANCE && id.equals(lastFetchedHongbaoId)) {      
          return true;   
       }   
      return false;
    }

   private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {  
        for (String text : texts) {     
           if (text == null) continue;      
           List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);     
           if (!nodes.isEmpty()) {         
               if (text.equals(WECHAT_OPEN_EN) && !nodeInfo.findAccessibilityNodeInfosByText(WECHAT_OPENED_EN).isEmpty()) {              
                    continue;         
                }            
                return nodes;      
            }  
          }    
        return new ArrayList<>();
    }

    @Override
    public void onInterrupt() {}
}