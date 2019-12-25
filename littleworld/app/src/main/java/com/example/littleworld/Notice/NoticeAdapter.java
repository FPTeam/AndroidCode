package com.example.littleworld.Notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littleworld.R;

import java.util.List;


public class NoticeAdapter extends ArrayAdapter<Notice> {

    private int resourceId;

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
            viewHolder.noticeMeg = (TextView) view.findViewById (R.id.notice_meg);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.noticeImage.setImageResource(notice.getImage());
        viewHolder.noticeMeg.setText(notice.getMeg());
        return view;
    }

    class ViewHolder {

        ImageView noticeImage;

        TextView noticeMeg;

    }
}
