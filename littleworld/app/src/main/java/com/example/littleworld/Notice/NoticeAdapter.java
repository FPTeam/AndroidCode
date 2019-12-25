package com.example.littleworld.Notice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littleworld.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class NoticeAdapter extends ArrayAdapter<Notice> {

    private int resourceId;
    private Bitmap bitmap;

    public NoticeAdapter(Context context, int textViewResourceId,
                        List<Notice> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notice notice = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.noticeImage = (ImageView) view.findViewById (R.id.notice_image);
            viewHolder.noticeName = (TextView) view.findViewById (R.id.notice_name);
            viewHolder.noticeMeg = (TextView) view.findViewById (R.id.notice_meg);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }

        Bitmap bit = returnBitMap(notice.getImage());
        viewHolder.noticeImage.setImageBitmap(bit);
        viewHolder.noticeName.setText(notice.getName());
        viewHolder.noticeMeg.setText(notice.getMeg());
        return view;
    }

    class ViewHolder {

        ImageView noticeImage;
        TextView noticeName;
        TextView noticeMeg;

    }

    public Bitmap returnBitMap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

}
