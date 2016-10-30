package com.lmad.proyectomovil.adapter;

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
import com.lmad.proyectomovil.model.Comentario;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Raul on 30/10/2016.
 */

public class ComentarioAdapter extends BaseAdapter {

    List<Comentario> mComentarioList;

    public ComentarioAdapter(List<Comentario> mComentarioList) {
        this.mComentarioList = mComentarioList;
    }

    @Override
    public int getCount() {
        return mComentarioList.size();
    }

    @Override
    public Object getItem(int i) {
        return mComentarioList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ComentarioAdapter.ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_lista_comentarios,null);

            holder = new ComentarioAdapter.ViewHolder();
            holder.imgComment_Profile = (ImageView) convertView.findViewById(R.id.imgComment_Profile);
            holder.tvComment_User = (TextView) convertView.findViewById(R.id.tvComment_User);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);


            convertView.setTag(holder);
        }

        else {
            holder = (ComentarioAdapter.ViewHolder) convertView.getTag();
        }

        Comentario Comentario = mComentarioList.get(i);
        holder.tvComment_User.setText(Comentario.getUsuario().getApodo());
        holder.tvComment.setText(Comentario.getComentario());
        Bitmap foto = decodeBase64(Comentario.getUsuario().getFoto());
        holder.imgComment_Profile.setImageBitmap(foto);

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
        ImageView imgComment_Profile;
        TextView tvComment_User;
        TextView tvComment;
    }
    
}
