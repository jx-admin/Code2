package com.example.sensordemo;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

/**
 * @author junxu.wang
 * <pre>
 *Android中常见的八种传感器：
       加速度传感器(accelerometer)
       陀螺仪传感器(gyroscope)
       环境光照传感器(light)
       磁力传感器(magnetic field)
       方向传感器(orientation)
       压力传感器(pressure)
       距离传感器(proximity)
       温度传感器(temperature)

Android的大部分手机中都有传感器，传感器类型有方向、加速度(重力)、光线、磁场、距离(临近性)、温度等。

  方向传感器：   Sensor.TYPE_ORIENTATION

  加速度(重力)传感器：Sensor.TYPE_ACCELEROMETER

  光线传感器:    Sensor.TYPE_LIGHT

  磁场传感器：   Sensor.TYPE_MAGNETIC_FIELD

  距离(临近性)传感器：Sensor.TYPE_PROXIMITY

  温度传感器：   Sensor.TYPE_TEMPERATURE

//获取某种类型的感应器

Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//注册监听，获取传感器变化值

sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);

上面第三个参数为采样率：最快、游戏、普通、用户界面。当应用程序请求特定的采样率时，其实只是对传感器子系统的一个建议，不保证特定的采样率可用。

最快： SensorManager.SENSOR_DELAY_FASTEST

最低延迟，一般不是特别敏感的处理不推荐使用，该种模式可能造成手机电力大量消耗，由于传递的为原始数据，算法不处理好将会影响游戏逻辑和UI的性能。

游戏： SensorManager.SENSOR_DELAY_GAME

游戏延迟，一般绝大多数的实时性较高的游戏都使用该级别。

普通： SensorManager.SENSOR_DELAY_NORMAL 

标准延迟，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象。

用户界面： SensorManager.SENSOR_DELAY_UI

 

//获取感应器管理器     

SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);    
<p>//获取方向传感器               
Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);              
sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);</p><p>//获取加速度传感器               
Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);              
sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL); </p>   

 */
public class SensorUtils {
	Context context;
	Sensor sensor ;       
	Handler handler;
    private float x, y, z;
    
    public SensorUtils(Context context){
    	this.context=context;
    	//从系统服务中获得传感器管理器       
        SensorManager sm = (SensorManager)context. getSystemService(Context.SENSOR_SERVICE);       
        // 注册listener，第三个参数是检测的精确度       
        
        //在title上显示重力传感器的变化       
        sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }
    public StringBuilder getAllSensor(Context context){

    	StringBuilder sb=new StringBuilder();
        //从系统服务中获得传感器管理器       
        SensorManager sm = (SensorManager)context. getSystemService(Context.SENSOR_SERVICE);       
       
        //从传感器管理器中获得全部的传感器列表       
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);       
       
        //显示有多少个传感器       
        sb.append("经检测该手机有" + allSensors.size() + "个传感器，他们分别是：\n");       
       
        //显示每个传感器的具体信息       
        for (Sensor s : allSensors) {       
       
        	String tempString=( "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："       
                    + s.getVendor() + "\n");       
       
            switch (s.getType()) {       
            case Sensor.TYPE_ACCELEROMETER:       
            	sb.append( s.getType() + " 加速度传感器accelerometer" + tempString);       
                break;       
            case Sensor.TYPE_GRAVITY:       
            	sb.append( s.getType() + " 重力传感器gravity API 9" + tempString);       
                break;       
            case Sensor.TYPE_GYROSCOPE:       
            	sb.append( s.getType() + " 陀螺仪传感器gyroscope" + tempString);       
                break;       
            case Sensor.TYPE_LIGHT:       
            	sb.append( s.getType() + " 环境光线传感器light" + tempString);       
                break;       
            case Sensor.TYPE_LINEAR_ACCELERATION:       
            	sb.append( s.getType() + " 线性加速器LINEAR_ACCELERATION API 9" + tempString);       
                break;       
            case Sensor.TYPE_MAGNETIC_FIELD:       
            	sb.append( s.getType() + " 电磁场传感器magnetic field" + tempString);       
                break;       
            case Sensor.TYPE_ORIENTATION:       
            	sb.append( s.getType() + " 方向传感器orientation" + tempString);       
                break;       
            case Sensor.TYPE_PRESSURE:       
            	sb.append( s.getType() + " 压力传感器pressure" + tempString);       
                break;       
            case Sensor.TYPE_PROXIMITY:       
            	sb.append( s.getType() + " 距离传感器proximity" + tempString);       
                break;       
            case Sensor.TYPE_ROTATION_VECTOR:       
            	sb.append( s.getType() + " 旋转向量ROTATION" + tempString);       
                break;       
            case Sensor.TYPE_TEMPERATURE:       
            	sb.append( s.getType() + " 温度传感器temperature" + tempString);       
                break;       
            default:       
            	sb.append( s.getType() + " 未知传感器" + tempString);       
                break;       
            }       
        }  
        return sb;
    }
    
    public void startListener(Context context, Handler handler) {
    	this.handler=handler;
    	//从系统服务中获得传感器管理器       
        SensorManager sm = (SensorManager)context. getSystemService(Context.SENSOR_SERVICE);       
        // 注册listener，第三个参数是检测的精确度       
        
    	sm.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);       
       
    } 
    
    public void startListener(Context context,SensorEventListener lsn) {
    	//从系统服务中获得传感器管理器       
        SensorManager sm = (SensorManager)context. getSystemService(Context.SENSOR_SERVICE);       
        // 注册listener，第三个参数是检测的精确度       
        
    	sm.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);       
       
    } 
    public void stopListener(Context context){

    	//从系统服务中获得传感器管理器       
        SensorManager sm = (SensorManager)context. getSystemService(Context.SENSOR_SERVICE); 
        sm.unregisterListener(lsn);
    }
    
    
    SensorEventListener lsn = new SensorEventListener() {       
       public void onSensorChanged(SensorEvent e) {  
           x = e.values[SensorManager.DATA_X];       
           y = e.values[SensorManager.DATA_Y];       
           z = e.values[SensorManager.DATA_Z];
           Message msg=handler.obtainMessage(0, e.sensor.getName()+"  "+"x=" + x + "," + "y=" +  y + "," + "z="+  z);
           handler.sendMessage(msg);
       }       
  
       public void onAccuracyChanged(Sensor s, int accuracy) {
           Message msg=handler.obtainMessage(0, "onAccuracyChanged " + s.getName()+ "," + "getPower=" + s.getPower() + "," + "accuracy="+ accuracy);
           handler.sendMessage(msg);
       }
   };
}
