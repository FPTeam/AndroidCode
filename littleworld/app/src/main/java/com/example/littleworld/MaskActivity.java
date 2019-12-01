package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.ImageView;

public class MaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        只是测试遮罩功能的，并没有使用！！!
         */
        super.onCreate(savedInstanceState);
        ImageView iv=new ImageView(this);

        Bitmap source= BitmapFactory.decodeResource(getResources(),R.drawable.default_sculp);
        Bitmap mask=BitmapFactory.decodeResource(getResources(),R.drawable.sculp_shape_c);

        Bitmap result = Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(mask, 0 , 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);
        paint.setXfermode(null);

        iv.setImageBitmap(result);
        setContentView(iv);
    }
}
