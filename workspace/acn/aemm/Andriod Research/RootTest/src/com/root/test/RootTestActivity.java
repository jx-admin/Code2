package com.root.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RootTestActivity extends Activity implements OnClickListener {
	private Button btn_root = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn_root = (Button)findViewById(R.id.btn_root);
        btn_root.setOnClickListener((OnClickListener) this);
        
    }
    
	public void onClick(View v) {
		if(v.getId()==R.id.btn_root){
			if(isRooted()){  
			    Toast.makeText(getApplicationContext(), "my phone is rooted", Toast.LENGTH_SHORT).show();
            } else{
			    Toast.makeText(getApplicationContext(), "my phone is not rooted", Toast.LENGTH_SHORT).show();
			}

		}
	}
      
      public DataInputStream Terminal(String command)  
          {  
    	      InputStream instream  = null;
    	      DataInputStream DIPS  = null;
    	      try{
		              Process process = Runtime.getRuntime().exec("su");  
		              if(process == null){
		            	  System.out.println("there is no su file");  
		            	  return null;
		              }
		              //执行到这，Superuser会跳出来，选择是否允许获取最高权限  
		              OutputStream outstream = process.getOutputStream();  
		              DataOutputStream DOPS = new DataOutputStream(outstream);  
		              instream = process.getInputStream();  
		              DIPS = new DataInputStream(instream);  
		              String temp = command + "\n";  
		              //加回车  
		              DOPS.writeBytes(temp);  
		              //执行  
		              DOPS.flush();  
		              //刷新，确保都发送到outputstream  
		              DOPS.writeBytes("exit\n");  
		             //退出  
		              DOPS.flush();  
		              try {
						process.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
		   } catch (IOException e1) {  
               // TODO Auto-generated catch block  
               e1.printStackTrace();  
      
           } catch (SecurityException e){
         	  e.printStackTrace();  
           }
           return DIPS;  
         }  
      
          public boolean isRooted() {  
             //检测是否ROOT过  
              DataInputStream stream = null;  
              boolean flags = false;  
              String str = null;
              String stb = "-rwsr-xr-x";
              try {  
				  stream = Terminal("ls -l /system/bin/su");
                  if(stream == null){
                	  return flags;
                  }
                  str = stream.readLine();
                  System.out.println(str);  
                  if(str.startsWith(stb)){
                	  flags=true;  
                  }
                  //根据是否有返回来判断是否有root权限  
              } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
               
         
              return flags;  
          }  

     }  
