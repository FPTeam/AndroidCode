package com.example.littleworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.littleworld.BottomMenu.NavigationActivity;
import com.example.littleworld.Entity.passage;
import com.example.littleworld.LoginActivity;
import com.example.littleworld.ProfileActivity;
import com.example.littleworld.R;

import java.util.List;

public class passageAdapter extends RecyclerView.Adapter<passageAdapter.passageAdapterHolder>  {

    private  Context context;
    private  List<passage> passageList;

    //绑定控件
    public class passageAdapterHolder extends RecyclerView.ViewHolder {


        public ImageView headImg;
        public TextView name;
        public TextView content;
        public TextView postTime;
        public ImageView note_img;
        public TextView starNum;
        public TextView likeNum;
        public ImageView img_like;
        public ImageView img_collect;

        public passageAdapterHolder(View view) {
            super(view);
            headImg = view.findViewById(R.id.sculpture);
            name = view.findViewById(R.id.name);
            content = view.findViewById(R.id.content);
            postTime = view.findViewById(R.id.PostTime);
            note_img = view.findViewById(R.id.note_img);
            starNum = view.findViewById(R.id.star_num);
            likeNum = view.findViewById(R.id.like_num);
            img_like= view.findViewById(R.id.img_like);//点赞图片
            img_collect = view.findViewById(R.id.img_collect);//收藏图片
        }
    }

    public passageAdapter(Context context,List<passage> passageList) {
        this.context = context;
        this.passageList = passageList;
    }


    @NonNull
    @Override
    public passageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        passageAdapterHolder holder = new passageAdapterHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final passageAdapterHolder holder, int position) {

        final passage passage =passageList.get(position);

        holder.name.setText(passage.getName());
        holder.content.setText(passage.getContent());
        holder.postTime.setText(passage.getPostTime());
        holder.starNum.setText(passage.getCollectNumber());
        holder.likeNum.setText(passage.getLikeNumber());
        holder.headImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                int userId = 190001;
                intent.putExtra("user_id",userId);
                intent.putExtra("is_owner",0);//访问别人主页
                context.startActivity(intent);
            }
        });

         Glide
                .with(this.context)
                .load(passage.getImgpath())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(holder.headImg);
       
        Glide
                .with(this.context)
                .load(passage.getImgpath())
                .into(holder.note_img);


        holder.img_like.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View arg0) {
                // 喜爱点击事件
                //变数
                int likeNumber =Integer.valueOf(passage.getLikeNumber());
               passage.setLikeNumber(++likeNumber);

                holder.likeNum.setText(passage.getLikeNumber());
                //变色
                holder. img_like.setColorFilter(Color.RED);

                /*
                * 这里放数据库插入的语言
                * */

            }
        });

        holder.img_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 收藏点击事件
                //变数
                int collectNumber =Integer.valueOf(passage.getCollectNumber());
                passage.setCollectNumber(++collectNumber);

                holder.starNum.setText(passage.getCollectNumber());
                //变色
                holder. img_collect.setColorFilter(Color.RED);

                /*
                * 这里和数据库插入有关
                * */

            }
        });



    }


    @Override
    public int getItemCount() {
        return passageList.size();
    }
}
