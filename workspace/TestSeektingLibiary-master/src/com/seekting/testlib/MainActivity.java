
package com.seekting.testlib;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends TestActivity implements OnItemClickListener {

    List<TestActivity> list;

    ListView listView;

    
    
    ArrayAdapter<TestActivity> adapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView)findViewById(R.id.list);

        adapter = new ArrayAdapter<TestActivity>(this, android.R.layout.simple_list_item_1);

        TestPaoPaoDrawable testPaoPaoDrawable = new TestPaoPaoDrawable();
        TestRecordActivity testRecordActivity = new TestRecordActivity();
        adapter.add(testPaoPaoDrawable);
        adapter.add(testRecordActivity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TestActivity t = adapter.getItem(position);
        openActivity(t.getClass());

    }
}
