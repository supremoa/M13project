package com.project.m13.m13project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alumne on 15/04/16.
 */
public class misLibrosAdapter extends BaseAdapter {
    Context cont;
    List<Material> material;
    LayoutInflater inflater;

    public misLibrosAdapter(Context cont, List<Material> material) {
        this.cont = cont;
        this.material = material;
    }

    @Override
    public int getCount() {
        return material.size();
    }

    @Override
    public Object getItem(int position) {
        return material.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        if(itemView ==null) {
            inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            itemView = inflater.inflate(R.layout.list, parent, false);
        }
        TextView nombre=(TextView)itemView.findViewById(R.id.myNombreProd);
        TextView descripcion=(TextView)itemView.findViewById(R.id.myDescripcion);
        TextView precio=(TextView) itemView.findViewById(R.id.myprecio);

        nombre.setText(material.get(position).getNombre());
        descripcion.setText("Descripción"+material.get(position).getTexto());

        precio.setText("Precio: " + material.get(position).getPrecio());
       // Drawable a= h.getDrawable(R.drawable.fondo);
       // itemView.setBackground(a);
        return itemView;
    }
}
