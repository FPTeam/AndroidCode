package com.example.littleworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/* 创建一个自定义的适配器 */
public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;

    public UserAdapter(Context context, int textViewResourceId, List<User> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        User user = getItem(position);  // 获取当前的user实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView peopleImage = (ImageView) view.findViewById(R.id.user_image);
        TextView peopleName = (TextView) view.findViewById(R.id.user_name);
        peopleImage.setImageResource(user.getImageId());
        peopleName.setText(user.getUserId());
        return view;
    }
}
