package com.example.littleworld;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.example.littleworld.BottomMenu.NavigationActivity;
import com.example.littleworld.util.ToastUtil;

public class ReleaseDynamicsActivity extends Fragment {
    private static final int TAKE_PHOTO = 1;

    private static final int CHOOSE_PHOTO = 2;

    private int userId;

    private ImageView picture;

    private Uri imageUri;
    //图片
    private Bitmap bitmap;
    //保存的文件路径
    private File fileDir;

    private View layout;

    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;
    // 声明平移动画
    private TranslateAnimation animation;


    private String location = "未添加";

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String address = new String(amapLocation.getAddress());//解析地址获取详细地址
                    String province = new String(amapLocation.getProvince());//省份信息
                    String city = new String(amapLocation.getCity());//城市信息
                    String block = new String(amapLocation.getDistrict());//城区信息
                    String street = new String(amapLocation.getStreet());//街道信息
                    String number = new String(amapLocation.getStreetNum());//街道门牌号信息

                    TextView text1=(TextView)getActivity().findViewById(R.id.add_place);
                    text1.setText(address);
                    location = address;
                    deactivate();//定位成功就停止定位
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                    TextView text=(TextView)getActivity().findViewById(R.id.add_place);
                    text.setText("定位错误");
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    ToastUtil.show(getActivity(),  errText);

                    deactivate();//停止定位
                }
            }
        }
    };

    public ReleaseDynamicsActivity(int userId){
        super();
        this.userId = userId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        layout = inflater.inflate(R.layout.activity_new_notes, container, false);

        if(location.equals("未添加"))
        {
            //初始化定位
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化定位
            initLocation();
        }else{
            TextView text1=(TextView)layout.findViewById(R.id.add_place);
            text1.setText(location);
        }

        //多个单选按钮
        ImageButton add_pictures;
        add_pictures=layout.findViewById(R.id.add_pictures);
        //要显示的图片
        //这里改了一下，直接将加号替换成要显示的图片
        picture = layout.findViewById(R.id.add_pictures);

        //点击加号选择照相机或者相册
        add_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(v);
                lightoff();
            }
        });

/*
        // 返回上一个界面
        ImageButton backBtn=layout.findViewById(R.id.new_note_ret);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReleaseDynamicsActivity.this.finish();
            }
        });
*/

        //创建存放图片的文件
        String DATABASE_PATH=getActivity().getApplicationContext().getFilesDir().toString()+"/IMAGE";
        fileDir = new File(DATABASE_PATH);//和LoginActivity里的数据库一个路径
        // 如果目录不存在，创建这个目录
        if (!fileDir.exists())
            fileDir.mkdir();


        //图片文件存SD卡里
       /* File sdDir = Environment.getExternalStorageDirectory();
        fileDir = new File(sdDir.getPath() + "/IMAGE");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
*/

        final Button noteSend = layout.findViewById(R.id.note_send);
        noteSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPath;
                if(bitmap != null)
                    imgPath = SaveFile(fileDir);
                else
                    imgPath = null;
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }

                final EditText input_notes = layout.findViewById(R.id.input_notes);
                final String inputNotes = input_notes.getText().toString();

                //***添加时间
                SimpleDateFormat nowtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式
                nowtime.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));// 中国北京时间，东八区
                Date date = new Date(System.currentTimeMillis());//当前设备的时间
                String timestr = nowtime.format(date);//转换为字符串

                int passageId = DbHelper.getInstance().insertPassage( DbHelper.getInstance().getUserId(),inputNotes,imgPath,timestr,null,location);
                input_notes.setText("");
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                startActivity(intent);
//                ReleaseDynamicsActivity.this.finish();
            }
        });
        return layout;
    }

    private void changeIcon(View view) {
        if (popupWindow == null) {
            popupView = View.inflate(getActivity(), R.layout.bottompopup_camera_album, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });

            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);

            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);

            popupView.findViewById(R.id.takePhotobottom).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "相机", Toast.LENGTH_SHORT).show();
                    openCamera();
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.chooseFromAlbumbottom).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "相册", Toast.LENGTH_SHORT).show();
                    openAlbum();
                    popupWindow.dismiss();
                    lighton();
                }
            });
        }

        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(getActivity().findViewById(R.id.add_pictures), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }
    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.3f;
        getActivity().getWindow().setAttributes(lp);
    }
    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
    }


    //初始化定位
    public void initLocation(){

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }
    public void openCamera(){
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.littleworld.BottomMenu.NavigationActivity.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public String SaveFile(File fileDir){
        //mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = time.format(System.currentTimeMillis());
        //使用当前时间为文件(图片)命名
        File currentFile = new File(fileDir, "IMG_"+fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"添加失败",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"添加失败",Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity().getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
            return fileDir + "/IMG_"+ fileName + ".jpg";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 停止定位
     */
    public void deactivate() {
        mLocationListener=null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
}
