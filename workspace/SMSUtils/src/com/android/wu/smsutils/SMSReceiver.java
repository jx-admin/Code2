package com.android.wu.smsutils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.log.CLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

//�̳�BroadcastReceiver

public class SMSReceiver extends BroadcastReceiver {

    private String TAG="AutSMS";
    //广播消息类型
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    //覆盖onReceive方法
    @Override
    public void onReceive(Context context, Intent intent){
        // TODO Auto-generated method stub
        //StringBuilder body=new StringBuilder("");//短信内容
        //StringBuilder sender=new StringBuilder("");//发件人
        //先判断广播消息
        String action = intent.getAction();
        CLog.print(TAG, "receiver:"+action);
        if (SMS_RECEIVED_ACTION.equals(action)){
            //获取intent参数
            Bundle bundle=intent.getExtras();
            //判断bundle内容
            if (bundle!=null){
                //取pdus内容,转换为Object[]
                Object[] pdus=(Object[])bundle.get("pdus");
                //解析短信
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i=0;i<messages.length;i++){
                    byte[] pdu=(byte[])pdus[i];
                    messages[i]=SmsMessage.createFromPdu(pdu);
                }    
                //解析完内容后分析具体参数
                for(SmsMessage msg:messages){
                    //获取短信内容
                    String content=msg.getMessageBody();
                    String sender=msg.getOriginatingAddress();
                    Date date = new Date(msg.getTimestampMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    CLog.print(TAG, sendTime+" 收到: "+sender+" -- "+content);
                    //TODO:根据条件判断,然后进一般处理
                    if ("10060".equals(sender)){
                        // 屏蔽手机号为10060的短信，这里还可以时行一些处理，如把这个信息发送到第三人的手机等等。
                        //TODO:测试
                        Toast.makeText(context, "收到10060的短信"+"内容:"+content, Toast.LENGTH_LONG).show();
                        //对于特定的内容,取消广播
                        abortBroadcast();
                    }else{
                        Toast.makeText(context, "收到:"+sender+"内容:"+content+"时间:"+sendTime.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                
            }
        }//if 判断广播消息结束
    }

}