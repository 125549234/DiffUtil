package com.nathan.diff.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.nathan.diff.util.dialog.MToast;
import com.nathan.diff.util.plug.ViewPlugBaseLayout;

import es.dmoral.toasty.Toasty;

import static com.nathan.diff.util.BuildConfig.DEBUG;

/**
 * Created by Administrator on 2017/1/3.
 */

public class LogUtil {
    private static String TAG  = "test111111";


    public static void tLogD(String temp){
        //if(LOG_DEBUG){
            if(temp!=null){
                //if(logFlag)
                if(ViewPlugBaseLayout.isDebug!=null){
                    boolean tem = ViewPlugBaseLayout.isDebug;
                    if(tem){
                        Log.e(TAG,""+temp);
                    }
                }



//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date curDate = new Date(System.currentTimeMillis());
//                String dataStr = formatter.format(curDate);
//                TlogDInfo tlogDInfo = new TlogDInfo();
//                tlogDInfo.setTime(dataStr);
//                tlogDInfo.setValue(temp);
//                IMApplication.getInstance().getTlogDInfoBox().put(tlogDInfo);

            }
        //}

    }
    public static void tLogE(Context context, String temp){
        if(temp!=null){
            //if(logFlag)
            if(DEBUG){
                Log.e("警告111："+context.getClass().getName(),""+temp);

            }

        }
    }

    public static void tToast(Context context, String str){
       // Toast.makeText(context,""+str, Toast.LENGTH_SHORT).show();
        tToast(context, str,Toast.LENGTH_LONG);
//        Looper mLooper = Looper.myLooper();
//        MToast.makeTextShort(context, str).show();
//        mLooper.quit();//停止循环，下次执行的时候重新进入Looper消息泵中
//        Looper.loop();

      // MToast.makeTextLong(context, str).show();
      //  Toast.makeText(context,""+str, Toast.LENGTH_SHORT).show();
//        if (Build.VERSION.SDK_INT >= 19) {
          //  ToastFactory.getInstance(context).makeTextShow(str,Toast.LENGTH_SHORT);
//        }else{
//            Toast.makeText(context,""+str, Toast.LENGTH_SHORT).show();
//        }
    }
    public static void tToast(Context context, String str,int time){

        if (Looper.myLooper() == null)
        {
            Looper.prepare();
            //MToast.makeTextShort(context, str).show();
            Toasty.normal(context, str, time).show();
            Looper.loop();
        }else{
            //MToast.makeTextShort(context, str).show();
            Toasty.normal(context, str, time).show();
        }

    }

}
