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

        public ImageButton setting_bnt;
        public ImageView headImg;
        public TextView name;
        public TextView content;
        public TextView postTime;
        public ImageView note_img;
        public TextView starNum;
        public TextView likeNum;
        public ImageView img_like;
        public ImageView img_collect;
        public TextView tag;
        public TextView location_info;


        public passageAdapterHolder(View view) {
            super(view);
            setting_bnt = view.findViewById(R.id.setting_bnt);//设置按钮
            headImg = view.findViewById(R.id.sculpture);//头像图片
            name = view.findViewById(R.id.name);//姓名
            content = view.findViewById(R.id.content);//说说内容
            postTime = view.findViewById(R.id.PostTime);//发布时间
            note_img = view.findViewById(R.id.note_img);//说说图片
            starNum = view.findViewById(R.id.star_num);//收藏个数
            likeNum = view.findViewById(R.id.like_num);//点赞个数
            img_like= view.findViewById(R.id.img_like);//点赞图片
            img_collect = view.findViewById(R.id.img_collect);//收藏图片
            tag = view.findViewById(R.id.tag);//标签
            location_info = view.findViewById(R.id.location_info);//地点信息

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

        holder.name.setText(passage.getName());//取出姓名
        holder.postTime.setText(passage.getPostTime());//取出时间
        holder.content.setText(passage.getContent());//取出说说内容
        holder.starNum.setText(passage.getCollectNumber());//取出收藏个数
        holder.likeNum.setText(passage.getLikeNumber());//取出点赞个数
        holder.tag.setText(passage.getTag());//取出标签
        holder.location_info.setText(passage.getPostPlace());//取出地点信息

         Glide
                .with(this.context)
                .load(passage.getHeadpath())//加载头像路径
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(holder.headImg);
       
        Glide
                .with(this.context)
                .load(passage.getImgpath())//加载说说路径
                .into(holder.note_img);


        //点击头像事件
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

        //点击设置图片事件
        holder.setting_bnt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //弹出一个小菜单

                //showPopupMenu(imageVie);
            }
        });


        // 喜爱点击事件
        holder.img_like.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View arg0) {

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


        // 收藏点击事件
        holder.img_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

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
