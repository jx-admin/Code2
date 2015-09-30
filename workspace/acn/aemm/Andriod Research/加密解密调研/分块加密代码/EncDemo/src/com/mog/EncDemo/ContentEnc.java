package com.mog.EncDemo;

import com.mog.EncDemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mog.EncDemo.FileService;
import com.mog.EncDemo.SetPassword;
/*
 * this file is used to encrypt the input data , 
 * this page contains a inputtextarea ,
 *  which allows user to input data .
 *  a button is prepared as : go and encrypt data .  
 */
public class ContentEnc extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	 public static final String KEY_ENCTYPE =
	      "com.mog.EncDemo.fileenc";
	 
	private FileService fileService = new FileService();
    //定义视图中的filename输入框对象
    private EditText fileNameText;
    //定义视图中的contentText输入框对象
    private EditText contentText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contentenc);

		Button buttonEnc = (Button) findViewById(R.id.contentencButton);
		buttonEnc.setOnClickListener(this);
		buttonEnc = (Button)findViewById(R.id.contomainButton) ; 
		buttonEnc.setOnClickListener(this) ; 
		contentText = (EditText)findViewById(R.id.edit_text) ; 
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.contentencButton:
			encInputText() ; 
			break;
		case R.id.contomainButton:
			finish();
			break;
		}
	}
	
	public void encInputText()
	{
		String fileName = "wokao" ; // fileNameText.getText().toString();
        String content = contentText.getText().toString();
/*   
        //当文件名为空的时候，提示用户文件名为空，并记录日志。
        if(isEmpty(fileName)) 
        {
        	showMessage("错误","请输入要保存的文件名","返回");
            return;
        }
*/
        //当文件内容为空的时候，提示用户文件内容为空，并记录日志。
        if(EncUtil.isEmpty(content)) 
        {
        	EncUtil.showMessage(this , "错误","请输入要加密的内容","返回") ; 
            return;
        }
        Intent iStart = new Intent(ContentEnc.this , SetPassword.class ) ; 
    	startActivity(iStart);
/*    	
        //当文件名和内容都不为空的时候，调用fileService的save方法
        //当成功执行的时候，提示用户保存成功，并记录日志
        //当出现异常的时候，提示用户保存失败，并记录日志
        try 
        {
            fileService.save(fileName, content);
            Log.i(fileService.TAG, "The file save successful");
        } 
        catch (Exception e) 
        {
        	showMessage("错误","文件保存失败","返回");
            Log.e(fileService.TAG, "The file save failed");
        }
*/
	}
}