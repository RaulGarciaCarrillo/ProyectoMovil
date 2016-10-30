package com.lmad.proyectomovil.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.model.Puesto;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Raul on 29/10/2016.
 */

public class PuestoAdapter extends BaseAdapter {

    List<Puesto> mPuestoList;

    public PuestoAdapter(List<Puesto> mPuestoList) {
        this.mPuestoList = mPuestoList;
    }

    @Override
    public int getCount() {
        return mPuestoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPuestoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_lista_puesto,null);

            holder = new ViewHolder();
            holder.ivPicture = (ImageView) convertView.findViewById(R.id.imgStandPicture);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvStandName);
            holder.tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);


            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Puesto puesto = mPuestoList.get(i);
        holder.tvName.setText(puesto.getNombre());
        holder.tvDescripcion.setText(puesto.getDescripcion());
        Bitmap foto = decodeBase64(puesto.getFoto());
        holder.ivPicture.setImageBitmap(foto);

        return convertView;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static class ViewHolder {
        ImageView ivPicture;
        TextView tvName;
        TextView tvDescripcion;
    }
}
