package com.example.littleworld.Notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.littleworld.Adapter.passageAdapter;
import com.example.littleworld.DbHelper;
import com.example.littleworld.Entity.passage;
import com.example.littleworld.ProfileActivity;
import com.example.littleworld.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeAdapterHolder> {

    private  Context context;
    private  List<Notice> noticeList;

    //绑定控件
    public class NoticeAdapterHolder extends RecyclerView.ViewHolder {

        public ImageButton noticeImage;
        public TextView noticeName;
        public TextView noticeMsg;
        public TextView noticeTime;

        public NoticeAdapterHolder(View view) {
            super(view);
            noticeImage = view.findViewById(R.id.notice_image); //头像
            noticeName = view.findViewById(R.id.notice_name);   //昵称
            noticeMsg = view.findViewById(R.id.notice_msg);
            noticeTime = view.findViewById(R.id.notice_time);
        }
    }

    public NoticeAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public NoticeAdapter.NoticeAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notice_item,parent,false);
        NoticeAdapter.NoticeAdapterHolder holder = new NoticeAdapter.NoticeAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeAdapter.NoticeAdapterHolder holder, int position) {

        final Notice notice =noticeList.get(position);

        holder.noticeName.setText(notice.getName());   //取出姓名
        holder.noticeMsg.setText(notice.getMsg());     //取出消息
        holder.noticeTime.setText(notice.getTime());
        /**Bitmap bit = returnBitMap(notice.getImage());
        viewHolder.noticeImage.setImageBitmap(bit);**/

        Glide
                .with(this.context)
                .load(notice.getImage())//加载头像路径
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(holder.noticeImage);

        //点击头像事件
        holder.noticeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                int userId =notice.getSendUserId();
                intent.putExtra("user_id",userId);
                intent.putExtra("is_owner",0);//访问别人主页
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

/**     本方法是将URL转换为Bitmap  未使用
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
    }**/

}
