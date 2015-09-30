package com.aphidmobile.flipview.demo.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flipview.demo.R;
import com.aphidmobile.flipview.demo.data.Travels;
import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.IO;
import com.aphidmobile.utils.UI;

public class TravelAdapter extends BaseAdapter {

  private LayoutInflater inflater;

  private int repeatCount = 1;

  private List<Travels.Data> travelData;

  private int type;

  public static final int COLUMN_ONE_HORIZONTAL = 1;
  public static final int COLUMN_TWO = 2;
  public static final int COLUMN_ONE_VERTICAL = 3;


  private Context mContext;

  public TravelAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    travelData = new ArrayList<Travels.Data>(Travels.IMG_DESCRIPTIONS);
    mContext = context;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public int getCount() {
    return travelData.size() * repeatCount;
  }

  public int getRepeatCount() {
    return repeatCount;
  }

  public void setRepeatCount(int repeatCount) {
    this.repeatCount = repeatCount;
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
    View layout = convertView;
    if (position % 3 == 0) {
      setType(COLUMN_ONE_HORIZONTAL);
    } else if (position % 3 == 1) {
      setType(COLUMN_TWO);
    } else if (position % 3 == 2) {
      setType(COLUMN_ONE_VERTICAL);
    }

    Toast.makeText(mContext,
        "position:" + position + ", type:" + type + "convertView:" + (convertView == null), 0)
        .show();
    Log.e("TravelAdapter", "position:" + position + ", type:" + type + "convertView:"
        + (convertView == null));

    if (convertView == null) {
      switch (type) {
        case COLUMN_ONE_HORIZONTAL:
          layout = inflater.inflate(R.layout.complex1, null);
          AphidLog.d("created new view from adapter: %d", position);

          break;
        case COLUMN_TWO:
          layout = inflater.inflate(R.layout.column, null);
          break;
        case COLUMN_ONE_VERTICAL:
          layout = inflater.inflate(R.layout.column_vertical, null);
          break;
      }
    }


    switch (type) {
      case COLUMN_ONE_HORIZONTAL:
        final Travels.Data data = travelData.get(position % travelData.size());

        UI.<TextView>findViewById(layout, R.id.title).setText(
            AphidLog.format("%d. %s", position, data.title));

        UI.<ImageView>findViewById(layout, R.id.photo).setImageBitmap(
            IO.readBitmap(inflater.getContext().getAssets(), data.imageFilename));

        UI.<TextView>findViewById(layout, R.id.description)
            .setText(Html.fromHtml(data.description));

        UI.<Button>findViewById(layout, R.id.wikipedia).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.link));
                inflater.getContext().startActivity(intent);
              }
            });

        break;
      case COLUMN_TWO:
        layout = inflater.inflate(R.layout.column, null);
        ColumnAdapter adapter = new ColumnAdapter(mContext);
        FlipViewController controller1 =
            UI.<FlipViewController>findViewById(layout, R.id.flip_controller1);
        controller1.setAdapter(adapter);
        FlipViewController controller2 =
            UI.<FlipViewController>findViewById(layout, R.id.flip_controller2);
        controller2.setAdapter(adapter);
        break;
      case COLUMN_ONE_VERTICAL:
        layout = inflater.inflate(R.layout.column_vertical, null);
        ColumnAdapter adapter2 = new ColumnAdapter(mContext);
        FlipViewController controller3 = UI.<FlipViewController>findViewById(layout, R.id.flip_controller3);
        controller3.setAdapter(adapter2);
        
        break;

    // new Handler().postAtTime(new Runnable() {
    //
    // @Override
    // public void run() {
    // controller1.onAutoEvent();
    // }
    // }, 1000,1000);

    // new Handler().postAtTime(new Runnable() {
    //
    // @Override
    // public void run() {
    // controller2.onAutoEvent();
    // }
    // }, 2000,1000);
    // break;
    }


    return layout;
  }

  private Timer timer1 = new Timer();
  private Timer timer2 = new Timer();

  private TimerTask task1 = new TimerTask() {

    @Override
    public void run() {

    }
  };

  private TimerTask task2 = new TimerTask() {

    @Override
    public void run() {

    }
  };

  private class HolderView {
    private FlipViewController flipViewController1;
    private FlipViewController flipViewController2;
  }

  public void removeData(int index) {
    if (travelData.size() > 1) {
      travelData.remove(index);
    }
  }
}
