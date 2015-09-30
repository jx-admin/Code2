package com.ccssoft.gpscamera;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aess.aemm.R;
import com.ccssoft.framework.base.BaseActivity;
import com.ccssoft.framework.base.GlobalInfo;
import com.ccssoft.framework.base.Version;
import com.ccssoft.framework.iface.BaseWsResponse;
import com.ccssoft.framework.iface.HttpCaller;
import com.ccssoft.framework.log.Logger;
import com.ccssoft.framework.system.Session;
import com.ccssoft.framework.user.vo.UserVO;
import com.ccssoft.framework.util.DateUtils;
import com.ccssoft.framework.util.DialogUtil;
import com.ccssoft.framework.util.FileUtils;
import com.ccssoft.framework.view.LoadingDialog;

public class GpsCameraActivity extends BaseActivity implements OnClickListener
{

   public static final int PHOTORESOULT = 2;

   private ImageView previewImage = null;

   private ImageButton photoBtn , delPhotoBtn , uploadphotoBtn = null;
   private Button cancle = null;
   private TextView latitude_text , longitude_text = null;

   String upLoadAdd = "http://192.168.1.100:9091/servlet/MopUploadServerlet";

   FileUtils fileUtils;

   String photoRealPath = null;
   String path = "photo";
   String photoName = "pic";

   @Override
   protected void onCreate(Bundle bundle)
   {
      super.onCreate(bundle);
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.bill_getphoto);
      initClass();
      UIComponent();
      setViewSize();
      setListener();
      fileUtils = new FileUtils();
      CreatDir();
      startGPS();

   }

   private void initClass()
   {
      UserVO userVO = new UserVO();
      Map<String, Version> versionMap = new HashMap<String, Version>();
      Version version = new Version();
      version.version = 1;
      versionMap.put("com.ccssoft", version);
      userVO.userId = "1";
      userVO.unitName = "hyc";
      userVO.versionMap = versionMap;
      Session.currUserVO = userVO;
      new Util(this).readDatabase(R.raw.ccssoft);
   }

   private void UIComponent()
   {
      //      Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/lcdNum.ttf");
      latitude_text = (TextView) findViewById(R.id.latitude_text);
      longitude_text = (TextView) findViewById(R.id.longitude_text);
      //      latitude_text.setTypeface(typeface);
      //      longitude_text.setTypeface(typeface);
      previewImage = (ImageView) findViewById(R.id.photo_previewimage2);

      cancle = (Button) findViewById(R.id.bill$photo_bn_cancle2);

      delPhotoBtn = (ImageButton) findViewById(R.id.delphotoBtn);
      photoBtn = (ImageButton) findViewById(R.id.photoBtn);
      uploadphotoBtn = (ImageButton) findViewById(R.id.uploadphotoBtn);

      Bitmap bmp = getLastPhoto(photoName);
      if (null != bmp)
      {
         previewImage.setImageBitmap(bmp);
      }
      String latitude = GlobalInfo.getStringSharedPre("latitude", "");
      String longitude = GlobalInfo.getStringSharedPre("longitude", "");
      if (!TextUtils.isEmpty(latitude))
      {
         latitude_text.setText(latitude);
         longitude_text.setText(longitude);
      }

   }

   private void setViewSize()
   {
      WindowManager m = getWindowManager();
      Display d = m.getDefaultDisplay();
      ViewGroup.LayoutParams params = previewImage.getLayoutParams();
      params.height = (int) (d.getHeight() * 0.3);
      previewImage.setLayoutParams(params);
   }

   private void setListener()
   {
      uploadphotoBtn.setOnClickListener(this);
      cancle.setOnClickListener(this);
      photoBtn.setOnClickListener(this);
      delPhotoBtn.setOnClickListener(this);
      previewImage.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            AlertDialog.Builder buide = new AlertDialog.Builder(GpsCameraActivity.this).setPositiveButton(R.string.close, null);
            try
            {

               Bitmap bitmap = getLastPhoto(photoName);
               LayoutInflater layout = LayoutInflater.from(GpsCameraActivity.this);
               View diaLoagView = layout.inflate(R.layout.loadphoto_image, null);
               ImageView image1 = (ImageView) diaLoagView.findViewById(R.id.Load_bigimage);
               image1.setImageBitmap(bitmap);
               LayoutParams layoutParams = image1.getLayoutParams();
               WindowManager manager = getWindowManager();
               layoutParams.height = (int) (manager.getDefaultDisplay().getHeight() * 0.5);
               layoutParams.width = (int) (manager.getDefaultDisplay().getWidth() * 0.7);
               image1.setLayoutParams(layoutParams);
               buide.setView(diaLoagView);
               buide.show();
            }
            catch (Exception e)
            {
               Logger.debug("出现异常：" + e.getMessage());
            }
         }
      });
   }

   /**
    * 根据当前系统中SDcard的状态是否可用，将自动创建一个目录<br/>
    * 如SDcard可用，将在SDcard的跟目录下，创建一个目录<br/>
    * 如不可用，将在程序的安装目录下，创建目录
    */
   private void CreatDir()
   {
      try
      {
         fileUtils.createDirByState(path);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   @Override
   public void onClick(View v)
   {
      if (uploadphotoBtn.equals(v))
      {

         if (null == getLastPhoto(photoName))
         {

            DialogUtil.displayWarning(GpsCameraActivity.this, getString(R.string.sys_msg),getString(R.string.no_photo), false, new OnClickListener()
            {
               @Override
               public void onClick(View v)
               {
               }
            });
         }
         else
         {
            try
            {
               String photoPath = GlobalInfo.getStringSharedPre(photoName, "");
               File file = new File(photoPath);
               new UploadPhoto(GpsCameraActivity.this, file, upLoadAdd, null).execute();

            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      }
      else if (cancle.equals(v))
      {
         finish();

      }
      else if (photoBtn.equals(v))
      {
         startCamera("", false, "", "", "");
      }
      else if (delPhotoBtn.equals(v))
      {
         previewImage.setImageBitmap(null);
         deleteOldPhotos(photoRealPath);
         savePhoto(photoName, "");
      }
   }

   public void onPhotoed(String absolutePath, String fileName)
   {
      super.onPhotoed(absolutePath, fileName);
      this.photoRealPath = absolutePath;
      Bitmap bmp = BitmapFactory.decodeFile(absolutePath);
      previewImage.setImageBitmap(bmp);
      delPhotoBtn.setVisibility(View.VISIBLE);
      savePhoto(photoName, absolutePath);
   }

   @Override
   public void onLocationChanged(String latitude, String longitude)
   {
      super.onLocationChanged(latitude, longitude);

      latitude_text.setText(latitude);
      longitude_text.setText(longitude);
      keepLocation(latitude, longitude);
      saveSinglePoint(latitude, longitude);
   }

   private void saveSinglePoint(String latitude, String longitude)
   {
      GlobalInfo.setStringSharedPre("latitude", latitude);
      GlobalInfo.setStringSharedPre("longitude", longitude);
   }

   private Bitmap getLastPhoto(String photoName)

   {
      Bitmap bmp = null;
      try
      {
         String photoPath = GlobalInfo.getStringSharedPre(photoName, "");
         if (!TextUtils.isEmpty(photoPath))
         {
            bmp = BitmapFactory.decodeFile(photoPath);
         }
      }
      catch (Exception e)
      {
         e.getMessage();
      }
      return bmp;
   }

   private void savePhoto(String photoName, String addr)
   {
      GlobalInfo.setStringSharedPre(photoName, addr);
   }

   /**
    * 获取相片系统路径
    */
   private String getRealPath(Uri photoUri)
   {
      String tempPath = null;
      try
      {
         String[] proj = { MediaStore.Images.Media.DATA };
         //好像是android多媒体数据库的封装接口，具体的看Android文档
         Cursor cursor = managedQuery(photoUri, proj, null, null, null);
         int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
         cursor.moveToFirst();
         tempPath = cursor.getString(column_index);
      }
      catch (Exception e)
      {
         Logger.debug("获取系统相片地址出现异常：" + e.getMessage());
      }
      return tempPath;
   }

   /**
    * 删除旧相片
    */
   private void deleteOldPhotos(String path)
   {
      if (null != path && null != getFile(path))
         getFile(path).delete();

   }

   private File getFile(String path)
   {
      if (null != path)
         return new File(path);
      else
         return new File("");

   }

   /**
    * 相片的完整目录路径
    * @return
    */
   private String getDirPath()
   {
      String PhotoDIR = FileUtils.getFilePath(path);
      return PhotoDIR;
   }

   /**
    * 保存经纬度
    * @return
    */
   private void keepLocation(String latitude, String longitude)
   {
      BufferedWriter locationWriteer = null;
      File locationFile = null;
      try
      {
         File dir = new File(FileUtils.getMemoryDirPath() + File.separator + FileUtils.ATTACHMANEPATH);
         if (!dir.exists())
         {
            dir.mkdirs();
         }
         locationFile = new File(dir, "location.txt");
         locationWriteer = new BufferedWriter(new BufferedWriter(new FileWriter(locationFile, true)));

         StringBuilder locationInfo = new StringBuilder();

         locationInfo.append(latitude);
         locationInfo.append(",");
         locationInfo.append(longitude);
         locationInfo.append(",");
         locationInfo.append(DateUtils.getCurrentDateTimeStr());
         locationWriteer.write(locationInfo.toString());
         locationWriteer.newLine();
      }
      catch (Exception e)
      {
         Logger.error(Logger.LOG_SYSTEM_ERROR, e, "收集经纬度信息出错");
         if (null != locationFile)
            locationFile.delete();
      }
      finally
      {
         try
         {
            locationWriteer.close();
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   private class UploadPhoto extends AsyncTask<String, Void, BaseWsResponse>
   {

      LoadingDialog proDialog;

      private File file;
      private String uploadAddr;
      private Map<String, Object> params;
      private Activity activity;

      public UploadPhoto(Activity activity, File file, String uploadAddr, Map<String, Object> params)
      {
         this.activity = activity;
         this.file = file;
         this.uploadAddr = uploadAddr;
         this.params = params;
      }

      @Override
      protected BaseWsResponse doInBackground(String... arg0)
      {
         BaseWsResponse response = new BaseWsResponse();

         int responseCode;
         try
         {
            //            responseCode = new HttpCaller(uploadAddr).upload(params, filePath, null);
            responseCode = new HttpCaller(null).uploadSingleFile(file, params, uploadAddr);
            if (responseCode == 200)
               response.getHashMap().put("result", "ok");
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }

         return response;
      }

      @Override
      protected void onPreExecute()
      {
         super.onPreExecute();
         proDialog = new LoadingDialog(activity);

         proDialog.setCancelable(false);
         proDialog.show();
         proDialog.setLoadingName(getString(R.string.photo_uploading));

      }

      @Override
      protected void onPostExecute(BaseWsResponse result)
      {
         super.onPostExecute(result);
         proDialog.cancel();
         String resultCode = (String) result.getHashMap().get("result");

         if (null != resultCode && resultCode.equalsIgnoreCase("ok"))
         {
            DialogUtil.displayWarning(activity, getString(R.string.sys_msg), getString(R.string.photo_upload_successful), false, null);
            previewImage.setImageBitmap(null);
            deleteOldPhotos(photoRealPath);
            saveSinglePoint("", "");
            savePhoto(photoName, "");
         }
         else
         {
            DialogUtil.displayWarning(activity, getString(R.string.sys_msg), getString(R.string.photo_upload_fail), false, new OnClickListener()
            {
               @Override
               public void onClick(View v)
               {

               }
            });
         }
      }
   }

   @Override
   public boolean isLoginFilte()
   {

      return false;
   }
}
