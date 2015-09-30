package cn.wangmeng.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class AsyncListImage extends Activity {
	private ListView list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        list=(ListView)findViewById(R.id.list);
        List<ImageAndText> dataArray=new ArrayList<ImageAndText>();
        ImageAndText test=new ImageAndText("http://www.wangmeng.cn/images/logo.gif", "test");
        ImageAndText test1=new ImageAndText("http://www.wangmeng.cn/images/ad/t1.gif", "test1");
        ImageAndText test2=new ImageAndText("http://www.wangmeng.cn/images/ad/t3.gif", "test2");
        dataArray.add(test);
        dataArray.add(test1);
        dataArray.add(test2);
        ImageAndTextListAdapter adapter=new ImageAndTextListAdapter(this, dataArray, list);
        list.setAdapter(adapter);
        
    }
}