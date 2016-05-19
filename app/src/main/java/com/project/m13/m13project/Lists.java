package com.project.m13.m13project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by alumne on 15/04/16.
 */
public class Lists extends BaseAdapter {
    Context h;
    final List<Material> material;
    LayoutInflater inflater;

    public Lists(Context h, List<Material> material) {
        this.h = h;
        this.material = material;
        this.inflater = (LayoutInflater) h.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return material.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View s;

        if (convertView == null) {
            s = inflater.inflate(R.layout.list, null);
            s.setTag(new ViewHolder(s));
        } else {
            s = convertView;
        }

        final ViewHolder holder = (ViewHolder) s.getTag();
        holder.title.setText(material.get(position).getNombre());
        holder.precio.setText("precio: " + material.get(position).getPrecio());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(material.get(position).getFoto(), 0, material.get(position).getFoto().length));

        return s;
    }

    static class ViewHolder {
        final TextView title;
        final ImageView image;
        final TextView precio;

        ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.textl);
            precio = (TextView) root.findViewById(R.id.precio);
            image = (ImageView) root.findViewById(R.id.imagen);
        }
    }
}
