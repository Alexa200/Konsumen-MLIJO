package com.mlijo.aryaym.konsumen_mlijo.Produk;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mlijo.aryaym.konsumen_mlijo.R;

import java.util.ArrayList;

/**
 * Created by AryaYM on 31/08/2017.
 */

public class AdapterImageSlider extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Integer> list;

    public AdapterImageSlider(Context ctx, ArrayList<Integer> list){
        this.context = ctx;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        View itemView = layoutInflater.inflate(R.layout.dashboard_image_slider, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_slider);
        imageView.setImageResource(list.get(position));
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
