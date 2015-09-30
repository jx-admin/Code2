package com.aphidmobile.flipview.demo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flipview.demo.adapter.TravelAdapter;
import com.aphidmobile.flipview.demo.views.NumberTextView;

public class FlipTextViewActivity extends Activity {

  protected FlipViewController flipView1;
  protected FlipViewController flipView2;
  protected FlipViewController flipView3;

  TravelAdapter adapter;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTitle(R.string.activity_title);

    // flipView = new FlipViewController(this);
    setContentView(R.layout.main);
    flipView1 = (FlipViewController) findViewById(R.id.flip_controller1);
    flipView2 = (FlipViewController) findViewById(R.id.flip_controller2);
    flipView3 = (FlipViewController) findViewById(R.id.flip_controller3);

    flipView1.setAdapter(new BaseAdapter() {
      @Override
      public int getCount() {
        return 6;
      }

      @Override
      public Object getItem(int position) {
        return position;
      }

      @Override
      public long getItemId(int position) {
        return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        NumberTextView view;
        if (convertView == null) {
          final Context context = parent.getContext();
          view = new NumberTextView(context, position);
          view.setTextSize(context.getResources().getDimension(R.dimen.textSize));
        } else {
          view = (NumberTextView) convertView;
          view.setNumber(position);
        }

        return view;
      }
    });
    adapter = new TravelAdapter(this);

//    flipView2.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
//      @Override
//      public void onViewFlipped(View view, int position) {
//        if (position == adapter.getCount() - 1) {// expand the data size on the last page
//          adapter.setRepeatCount(adapter.getRepeatCount() + 1);
//          adapter.notifyDataSetChanged();
//        }
//      }
//    });

    flipView2.setAdapter(adapter);
    flipView3.setAdapter(new TravelAdapter(this));
    mHandler.sendEmptyMessage(4);

    // ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
    // // pool.execute(thread1);
    // pool.scheduleAtFixedRate(thread1, 2000,2000, TimeUnit.MILLISECONDS);
    // pool.scheduleAtFixedRate(thread2, 3000, 2000, TimeUnit.MILLISECONDS);


  }


  /**
   * auto flip
   */
  private Handler mHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          flipView1.onAutoEvent();
          break;
        case 2:
          flipView2.onAutoEvent();
          break;
        case 3:
          flipView3.onAutoEvent();
          // flipView3.setVisibility(View.GONE);
          timer3.cancel();
          break;
        case 4:
          // timer1.scheduleAtFixedRate(task1, 2000, 3000);
          // timer2.scheduleAtFixedRate(task2, 4000, 3000);
//          timer3.schedule(task3, 2000,8000);
          timer1.scheduleAtFixedRate(task1, 2000, 2000);
          timer2.scheduleAtFixedRate(task2, 2000, 2000);
//           timer1.schedule(task1, 2000);
//           timer2.schedule(task2, 2000);
           
          break;
        case 5:
          flipView3.setVisibility(View.GONE);
          break;
      }

    }

  };

  private Timer timer1 = new Timer();
  private Timer timer2 = new Timer();
  private Timer timer3 = new Timer();

  private TimerTask task1 = new TimerTask() {

    @Override
    public void run() {
      Message msg = new Message();
      msg.what = 1;
      mHandler.sendMessage(msg);

    }
  };

  private TimerTask task2 = new TimerTask() {

    @Override
    public void run() {
      Message msg = new Message();
      msg.what = 2;
      mHandler.sendMessage(msg);

    }
  };

  private TimerTask task3 = new TimerTask() {

    @Override
    public void run() {
      Message msg = new Message();
      msg.what = 3;
      mHandler.sendMessage(msg);
      if (this.cancel()) {
        mHandler.sendEmptyMessage(5);
      }

    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    flipView1.onResume();
    flipView2.onResume();
    flipView3.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
//    flipView1.onPause();
//    flipView2.onPause();
//    flipView3.onPause();
  }
}
