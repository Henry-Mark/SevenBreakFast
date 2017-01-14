package com.haishu.SevenBreakFast.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by henry on 2016/3/10.
 */
public class FileOperate {
    /**
     * 将Bitmap数据存到SD卡
     * @param bitName
     * @param mBitmap
     */
    public void saveBitmap(String bitName,Bitmap mBitmap){
        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/7zao";
        File file=new File(path);
        if(!file.exists())
            file.mkdir();

        File f = new File("/sdcard/7zao/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
//            Toast.makeText(context,"在保存图片时出错" , Toast.LENGTH_SHORT).show();
            Log.d("...........","在保存图片时出错");

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片重命名
     * @param oldFile
     * @param newFile
     */
    public void reName(String oldFile,String newFile){
        File sd= Environment.getExternalStorageDirectory();
        String path="/sdcard/7zao/"+oldFile+ ".png";
        String pathNew = "/sdcard/7zao/"+newFile+ ".png";
        File file=new File(path);
        File newfile = new File(pathNew);
        Log.d("pathNew", pathNew);
        if(file.exists())
            file.renameTo(newfile);
    }

}
