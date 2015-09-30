
package com.seekting.testlib;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.seekting.view.PaoPaoDrawable;

public class TestPaoPaoDrawable extends TestActivity implements OnClickListener {

    public TestPaoPaoDrawable() {
        name = "PaoPaoDrawable";
    }

    ImageView paopao, paopao_1, paopao_2, paopao_3;

    Button add, sub;

    PaoPaoDrawable paoDrawable, paoDrawable1, paoDrawable2, paoDrawable3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_paopaodrawable);
        paopao = (ImageView)findViewById(R.id.paopao);

        paopao_1 = (ImageView)findViewById(R.id.paopao_1);
        paopao_2 = (ImageView)findViewById(R.id.paopao_2);
        paopao_3 = (ImageView)findViewById(R.id.paopao_3);

        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(
                R.drawable.ic_launcher);
        paoDrawable = new PaoPaoDrawable(bitmapDrawable, 20, Color.GRAY, Color.CYAN, 9);

        Drawable selector = getResources().getDrawable(R.drawable.btn_dialog_selector);
        paoDrawable1 = new PaoPaoDrawable(selector, 20, Color.WHITE, Color.GREEN, 20);
        Drawable newbg = getResources().getDrawable(R.drawable.new_msg_bg);
        ColorStateList colorStateList = getResources().getColorStateList(
                R.drawable.text_color_selector);
        paoDrawable2 = new PaoPaoDrawable(selector, 20, colorStateList, newbg, 120);
        Drawable paopaoSelector = getResources().getDrawable(R.drawable.paopao_selector);
        paoDrawable3 = new PaoPaoDrawable(selector, 30, Color.WHITE, paopaoSelector, 100);
        add = (Button)findViewById(R.id.add);
        sub = (Button)findViewById(R.id.sub);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        paopao.setImageDrawable(paoDrawable);
        paopao_1.setImageDrawable(paoDrawable1);

        paopao_2.setImageDrawable(paoDrawable2);
        paopao_3.setImageDrawable(paoDrawable3);

        paopao_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        paopao_3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        paopao_1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        System.out.println(colorStateList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == add) {

            paoDrawable.num++;
            paoDrawable1.num++;
            paoDrawable2.num++;
            paoDrawable3.num++;

        } else if (v == sub) {

            paoDrawable.num--;
            paoDrawable1.num--;
            paoDrawable2.num--;
            paoDrawable3.num--;
        }
        paoDrawable.invalidateSelf();
    }

    @Override
    public String toString() {
        return name;
    }
}
