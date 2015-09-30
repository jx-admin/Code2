
package com.accenture.mbank;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.LogManager;
import com.google.android.maps.GeoPoint;

public class DirectionDetailActivity extends BaseActivity {

    ListView directionListView;

    Button back;

    List<String> directors;

    private TextView title;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.direction_list_layout);

        // title = (TextView)findViewById(R.id.title_text);
        // title.setText("help");
        directors = new ArrayList<String>();
        directionListView = (ListView)findViewById(R.id.direction_list_view);
        directionListView.setAdapter(new DirectionAdapter());

        directionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        handler = new Handler();
        setData();

    }
    @Override
    protected void onBackClick() {
        // TODO Auto-generated method stub

        onBackPressed();
    }

    private void setData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int startLatitude = bundle.getInt("start-latitude");
        int startLongtitude = bundle.getInt("start-longtitude");

        final GeoPoint start = new GeoPoint(startLatitude, startLongtitude);

        int endLatitude = bundle.getInt("end-latitude");
        int endLongtitude = bundle.getInt("end-longtitude");
        final GeoPoint end = new GeoPoint(endLatitude, endLongtitude);

        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("loading", new OnProgressEvent() {

            @Override
            public void onProgress() {

                calculateRoutePoints(start, end);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        DirectionAdapter directionAdapter = (DirectionAdapter)directionListView
                                .getAdapter();
                        directionAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }

    public List<GeoPoint> calculateRoutePoints(GeoPoint aStartPoint, GeoPoint aEndPoint) {
        // TODO Auto-generated method stub

        // Get Points from Google direction API
        // "http://maps.google.com/maps/api/directions/xml?origin=39.928774,116.460801&destination=39.918774,116.458801&sensor=false&mode=walking";
        String url = "http://maps.google.com/maps/api/directions/json?origin="
                + aStartPoint.getLatitudeE6() / 1e6 + "," + aStartPoint.getLongitudeE6() / 1e6
                + "&destination=" + aEndPoint.getLatitudeE6() / 1e6 + ","
                + aEndPoint.getLongitudeE6() / 1e6 + "&sensor=true&mode=walking";
        Log.d("Route Overlay", "URL:" + url);

        HttpGet get = new HttpGet(url);
        String strResult = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(get);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            Log.d("Route Overlay", "Exception");
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(strResult);
            JSONArray jsonArray = jsonObject.getJSONArray("routes");

            JSONObject jsonObject2 = jsonArray.getJSONObject(0);

            JSONArray array = jsonObject2.getJSONArray("legs");
            JSONObject jsonObject3 = array.getJSONObject(0);

            // System.out.println(jsonArray);
            // LogManager.d(jsonArray.toString());

            JSONArray steps = jsonObject3.getJSONArray("steps");

            LogManager.d(steps.toString());
            directors.clear();
            for (int i = 0; i < steps.length(); i++) {
                JSONObject obje = steps.getJSONObject(i);

                String html = obje.optString("html_instructions");
                LogManager.d(html);
                directors.add(html);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private void offlineTest() {

    }

    class DirectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return directors.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return directors.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(DirectionDetailActivity.this);

                convertView = (View)inflater.inflate(R.layout.direction_item, null);
            }
            TextView text = (TextView)convertView.findViewById(R.id.direction_title_text);

            text.setText(Html.fromHtml(directors.get(position)));
            return convertView;
        }

    }
}
