package com.nathan.diff.util.plug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.nathan.diff.util.BackgroundDarkPopupWindow;
import com.nathan.diff.util.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static com.nathan.diff.util.LogUtil.tLogD;

/**
 * Created by nathan on 2017/7/26.
 */

public class ViewPlugBaseLayout {
    public static String LinearLayout_Name = "LinearLayout";
    public static String RelativeLayout_Name = "RelativeLayout";
    //微调系数
    public static float transformflag = 1.0f;
    //需要在起始activity赋值，否则为0；
    public static int width = 0;
    public static int height = 0;

    public static int dpWidth = 0;
    public static int dpHeight = 0;
    //默认间隔，单位dp
    public static float NormalJianGe = 10;
    /**
     * 控制是否开始尺寸修改功能
     */
    public static boolean modifyDeminFLag = true;


    /**
     * 控制控件高宽
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static float transformDemin(Context context, float dpValue) {
        // transformflag=2;
        return dip2px(context, dpValue * transformflag);
    }

    /**
     * 控制字体大小
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static float transformFontDemin(Context context, float dpValue) {
        return dip2px(context, dpValue * transformflag);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 计算屏幕长宽
     * @param activity
     */
    public static void initFont(Activity activity) {

        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

        if (width < 540) {
            transformflag = 0.7f;
        } else if (width < 720) {
            transformflag = 0.9f;
        }


        dpWidth = px2dip(activity, width);
        dpHeight = px2dip(activity, height);
        syncIsDebug(activity);

    }

    public static Boolean isDebug = null;
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            try {
                String packageName = context.getPackageName();
                Class buildConfig = Class.forName(packageName + ".BuildConfig");
                Field DEBUG = buildConfig.getField("DEBUG");
                DEBUG.setAccessible(true);
                isDebug = DEBUG.getBoolean(null);
            } catch (Throwable t) {
                // Do nothing
            }
        }
    }
    
    /**
     * 获取LinearLayout的配置(简化版，MATCH_PARENT，WRAP_CONTENT，不设置高度)
     * @param context
     * @return
     */
    public static LinearLayout.LayoutParams getLLParamsSimpleMW(Context context){
        return getLLParams(context,-2,-1,0,0,0,0,0,-1);
    }

    /**
     * 获取LinearLayout的配置(简化版，MATCH_PARENT，WRAP_CONTENT，设置高度)
     * @param context
     * @return
     */
    public static LinearLayout.LayoutParams getLLParamsSimpleMWH(Context context, int height){
        return getLLParams(context,-2,height,0,0,0,0,0,-1);
    }


    /**
     * 获取LinearLayout的配置(简化版，MATCH_PARENT，WRAP_CONTENT，设置高度，有内边距)
     * @param context
     * @return
     */
    public static LinearLayout.LayoutParams getLLParamsSimpleMWHP(Context context, int height, int paddingLeft, int paddingRight){
        return getLLParams(context,-2,height,paddingLeft,paddingRight,0,0,0,-1);
    }

    /**
     * 获取LinearLayout的配置(简化版，WRAP_CONTENT，WRAP_CONTENT，不设置高度)
     * @param context
     * @return
     */
    public static LinearLayout.LayoutParams getLLParamsSimpleWW(Context context){
        return getLLParams(context,-1,-1,0,0,0,0,0,-1);
    }
    /**
     * 获取LinearLayout的配置(简化版，WRAP_CONTENT，WRAP_CONTENT，不设置高度,有内边距)
     * @param context
     * @return
     */
    public static LinearLayout.LayoutParams getLLParamsSimpleWWP(Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        return getLLParams(context,-1,-1,0,paddingLeft,paddingRight,paddingTop,paddingBottom,-1);
    }
    /**
     * 获取LinearLayout的配置
     * @param context
     * @param w 设置宽度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置宽度，大于等于0时则表示WRAP_CONTENT而且设置w的宽度
     * @param h 设置高度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置高度，大于等于0时则表示WRAP_CONTENT而且设置h的高度
     * @param weight 设置权重 大于0时表示设置，而且值为weight
     * @param marginLeft 设置左外边距 大于0时表示设置，而且值为marginLeft
     * @param marginRight 设置右外边距 大于0时表示设置，而且值为marginRight
     * @param marginTop 设置上外边距 大于0时表示设置，而且值为marginTop
     * @param marginButtom 设置下外边距 大于0时表示设置，而且值为marginButtom
     * @param gravity 设置Android:layout_gravity属性 -1为不设置
     * @return
     */
    public static LinearLayout.LayoutParams getLLParams(Context context, int w, int h, int weight, int marginLeft, int marginRight, int marginTop, int marginButtom, int gravity){
        int width;
        int height;
        //首先判断高宽
        if(w==-2){
            width= ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            width= ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if(h==-2){
            height= ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            height= ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);

        //当判断w的值大于等于0时，把params的宽度设置
        if(w>=0){
            params.width=(int) transformDemin(context, w);
        }
        //当判断h的值大于等于0时，把params的高度设置
        if(h>=0){
            params.height=(int)transformDemin(context, h);
        }

        //设置外边距
        if(marginLeft>0){
            params.leftMargin=(int)transformDemin(context, marginLeft);
        }
        if(marginRight>0){
            params.rightMargin=(int)transformDemin(context, marginRight);
        }
        if(marginTop>0){
            params.topMargin=(int)transformDemin(context, marginTop);
        }
        if(marginButtom>0){
            params.bottomMargin=(int)transformDemin(context, marginButtom);
        }

        //设置权重
        if(weight>0){
            params.weight=weight;
        }

        //设置Android:layout_gravity属性
        if(gravity!=-1){
            params.gravity =gravity;
        }



        return params;
    }




    /**
     * 获取LinearLayout (简化版，垂直方向，可见，默认背景)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleVertical(Context context){
        return getLinearLayout(context, LinearLayout.VERTICAL,-1,0,0,0,0,null, View.VISIBLE,null);
    }
    /**
     * 获取LinearLayout (简化版，水平方向，可见，默认背景)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHorizontal(Context context){
        return getLinearLayout(context, LinearLayout.HORIZONTAL,-1,0,0,0,0,null, View.VISIBLE,null);
    }
    /**
     * 获取LinearLayout (简化版，垂直方向，可见，默认背景，带左右默认内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHavePadding(Context context){

        return getLinearLayout(context, LinearLayout.VERTICAL,-1,(int)NormalJianGe,(int)NormalJianGe,0,0,null, View.VISIBLE,null);
    }
    /**
     * 获取LinearLayout (简化版，水平方向，可见，默认背景，带左右默认内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHavePaddingHorizontal(Context context){

        return getLinearLayout(context, LinearLayout.HORIZONTAL,-1,(int)NormalJianGe,(int)NormalJianGe,0,0,null, View.VISIBLE,null);
    }

    /**
     * 获取LinearLayout (简化版，水平方向，可见，默认背景，带上下左右默认内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHaveALLPaddingHorizontal(Context context){

        return getLinearLayout(context, LinearLayout.HORIZONTAL,-1,(int)NormalJianGe,(int)NormalJianGe,(int)NormalJianGe,(int)NormalJianGe,null, View.VISIBLE,null);
    }
    /**
     * 获取LinearLayout (简化版，垂直方向，可见，默认背景，带上下左右默认内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHaveALLPaddingVertical(Context context){

        return getLinearLayout(context, LinearLayout.VERTICAL,-1,(int)NormalJianGe,(int)NormalJianGe,(int)NormalJianGe,(int)NormalJianGe,null, View.VISIBLE,null);
    }

    /**
     * 获取LinearLayout (简化版，垂直方向，可见，默认背景，带左右默认内边距,设置上下内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleHaveALLPaddingVertical(Context context, int paddingTop, int paddingButtom){

        return getLinearLayout(context, LinearLayout.VERTICAL,-1,(int)NormalJianGe,(int)NormalJianGe,paddingTop,paddingButtom,null, View.VISIBLE,null);
    }

    /**
     * 获取LinearLayout (简化版，垂直方向，可见，默认背景，需要设置内边距)
     * @param context
     * @return
     */
    public static LinearLayout getLinearLayoutSimpleSettingPadding(Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){

        return getLinearLayout(context, LinearLayout.VERTICAL,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,null, View.VISIBLE,null);
    }
    /**
     * 获取LinearLayout
     * @param context
     * @param orientation  设置方向
     * @param gravity  设置Android:gravity属性
     * @param paddingLeft 设置左内边距
     * @param paddingRight 设置右内边距
     * @param paddingTop 设置上内边距
     * @param paddingBottom 设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认为白色背景。
     * @param visibility 设置是否可见
     * @param onClick 设置点击事件
     * @return
     */
    public static LinearLayout getLinearLayout(Context context, int orientation, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, final OnPlugLayoutClickOnLister onClick){
        LinearLayout linearLayout = new LinearLayout(context);

        //设置方向
        linearLayout.setOrientation(orientation);

        //设置Android:gravity属性
        if(gravity!=-1){
            linearLayout.setGravity(gravity);
        }

        //设置内边距
        linearLayout.setPadding((int)transformDemin(context, paddingLeft),(int)transformDemin(context, paddingTop),(int)transformDemin(context, paddingRight),(int)transformDemin(context, paddingBottom));

        //设置背景颜色，如果参数为空则默认为白色背景。
        if(checkIsNull(backgroundColor)){
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            linearLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        }

        //设置是否可见
        linearLayout.setVisibility(visibility);

        //设置点击事件
        if(onClick!=null){
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.OnClick();
                }
            });
        }

        return linearLayout;
    }

    /**
     * 检查字符串是否为空
     *
     * @param temp
     * @return
     */
    public static boolean checkIsNull(String temp) {
        if (temp == null) {
            return true;
        }
        if (temp.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 检查集合是否为空或者集合里数量为0
     *
     * @param temp
     * @return 空和0返回 true  ，有数据则返回false
     */
    public static boolean checkIsNullOrZeroSize(Collection temp) {
        if (temp == null) {
            return true;
        }

        if(temp.size()==0){
            return true;
        }
        return false;
    }




    /**
     * 获取RelativeLayout的配置(简化版，MATCH_PARENT，WRAP_CONTENT，不设置高度)
     * @param context
     * @return
     */
    public static RelativeLayout.LayoutParams getRLParams(Context context){
        return getRLParams(context,-2,-1,0,0,0,0,null);
    }
    /**
     * 获取RelativeLayout的配置(简化版，WRAP_CONTENT，WRAP_CONTENT，不设置宽高度)
     * @param context
     * @return
     */
    public static RelativeLayout.LayoutParams getRLParamsWW(Context context){
        return getRLParams(context,-1,-1,0,0,0,0,null);
    }
    /**
     * 获取RelativeLayout的配置(简化版，WRAP_CONTENT，WRAP_CONTENT，不设置宽高度，需设置rules)
     * @param context
     * @return
     */
    public static RelativeLayout.LayoutParams getRLParamsWWP(Context context, List<Integer> rules){
        return getRLParams(context,-1,-1,0,0,0,0,rules);
    }

    /**
     * 获取RelativeLayout的配置
     * @param context
     * @param w 设置宽度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置宽度，大于等于0时则表示WRAP_CONTENT而且设置w的宽度
     * @param h 设置高度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置高度，大于等于0时则表示WRAP_CONTENT而且设置h的高度
     * @param marginLeft 设置左外边距 大于0时表示设置，而且值为marginLeft
     * @param marginRight 设置右外边距 大于0时表示设置，而且值为marginRight
     * @param marginTop 设置上外边距 大于0时表示设置，而且值为marginTop
     * @param marginButtom 设置下外边距 大于0时表示设置，而且值为marginButtom
     * @param rule 设置rule属性 -1为不设置
     * @return
     */
    public static RelativeLayout.LayoutParams getRLParams(Context context, int w, int h, int marginLeft, int marginRight, int marginTop, int marginButtom, List<Integer> rule){
        int width;
        int height;
        //首先判断高宽
        if(w==-2){
            width= ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            width= ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if(h==-2){
            height= ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            height= ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);

        //当判断w的值大于等于0时，把params的宽度设置
        if(w>=0){
            params.width=(int)transformDemin(context, w);
        }
        //当判断h的值大于等于0时，把params的高度设置
        if(h>=0){
            params.height=(int)transformDemin(context, h);
        }

        //设置外边距
        if(marginLeft>0){
            params.leftMargin=(int)transformDemin(context, marginLeft);
        }
        if(marginRight>0){
            params.rightMargin=(int)transformDemin(context, marginRight);
        }
        if(marginTop>0){
            params.topMargin=(int)transformDemin(context, marginTop);
        }
        if(marginButtom>0){
            params.bottomMargin=(int)transformDemin(context, marginButtom);
        }

        //设置rule
        if(rule!=null){
            for(int i=0;i<rule.size();i++){
                params.addRule(rule.get(i));
            }
        }


        return params;
    }


    /**
     * 获取RelativeLayout (简化版，垂直方向，可见，默认背景)
     * @param context
     * @return
     */
    public static RelativeLayout getRelativeLayoutSimple(Context context){
        return getRelativeLayout(context,-1,0,0,0,0,null, View.VISIBLE,null);
    }
    /**
     * 获取RelativeLayout (简化版，可见，默认背景，带左右默认内边距)
     * @param context
     * @return
     */
    public static RelativeLayout getRelativeLayoutSimpleHavePadding(Context context){

        return getRelativeLayout(context,-1,(int)NormalJianGe,(int)NormalJianGe,0,0,null, View.VISIBLE,null);
    }
    /**
     * 获取RelativeLayout (简化版，可见，默认背景，带内边距)
     * @param context
     * @return
     */
    public static RelativeLayout getRelativeLayoutSimpleHavePadding(Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){

        return getRelativeLayout(context,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,null, View.VISIBLE,null);
    }
    /**
     * 获取RelativeLayout
     * @param context
     * @param gravity  设置Android:gravity属性
     * @param paddingLeft 设置左内边距
     * @param paddingRight 设置右内边距
     * @param paddingTop 设置上内边距
     * @param paddingBottom 设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认为白色背景。
     * @param visibility 设置是否可见
     * @param onClick 设置点击事件
     * @return
     */
    public static RelativeLayout getRelativeLayout(Context context, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, final OnPlugLayoutClickOnLister onClick){
        RelativeLayout linearLayout = new RelativeLayout(context);



        //设置Android:gravity属性
        if(gravity!=-1){
            linearLayout.setGravity(gravity);
        }

        //设置内边距
        linearLayout.setPadding((int)transformDemin(context, paddingLeft),(int)transformDemin(context, paddingTop),(int)transformDemin(context, paddingRight),(int)transformDemin(context, paddingBottom));

        //设置背景颜色，如果参数为空则默认为白色背景。
        if(checkIsNull(backgroundColor)){
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            linearLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        }

        //设置是否可见
        linearLayout.setVisibility(visibility);

        //设置点击事件
        if(onClick!=null){
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.OnClick();
                }
            });
        }

        return linearLayout;
    }


    /**
     *  获取textview(简化版，字体默认13)
     * @param context
     * @param title
     * @param TextColor
     * @return
     */
    public static TextView getTextViewSimpleFont2(Context context, String title, String TextColor){
        return getTextView(context,title,TextColor,13,-1,0,0,0,0,null, View.VISIBLE,null);
    }
    /**
     *  获取textview(简化版，字体默认13,有上下为5的内边距)
     * @param context
     * @param title
     * @param TextColor
     * @return
     */
    public static TextView getTextViewSimpleFont2HavaPaddingTM(Context context, String title, String TextColor){
        return getTextView(context,title,TextColor,13,-1,0,0,5,5,null, View.VISIBLE,null);
    }
    /**
     *  获取textview(简化版，字体默认15)
     * @param context
     * @param title
     * @param TextColor
     * @return
     */
    public static TextView getTextViewSimpleFont3(Context context, String title, String TextColor){
        return getTextView(context,title,TextColor,15,-1,0,0,0,0,null, View.VISIBLE,null);
    }


    /**
     * 获取textview
     * @param context
     * @param title 设置内容
     * @param TextColor  设置字体颜色
     * @param TextSize 设置字体大小
     * @param gravity 设置Android:gravity属性
     * @param paddingLeft 设置左内边距
     * @param paddingRight 设置右内边距
     * @param paddingTop 设置上内边距
     * @param paddingBottom 设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认不设置背景。
     * @param visibility 设置是否可见
     * @param onClick 设置点击事件
     * @return
     */
    public static TextView getTextView(Context context, String title, String TextColor, float TextSize, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, final OnPlugLayoutClickOnLister onClick){

        TextView textView = new TextView(context);
        //设置内容
        textView.setText(title);

        //设置字体颜色
        if(!checkIsNull(TextColor)){
            textView.setTextColor(Color.parseColor(TextColor));
        }

        //设置字体大小
        textView.setTextSize(COMPLEX_UNIT_PX,transformDemin(context, TextSize));

        //设置Android:gravity属性
        if(gravity!=-1){
            textView.setGravity(gravity);
        }

        //设置内边距
        textView.setPadding((int)transformDemin(context, paddingLeft),(int)transformDemin(context, paddingTop),(int)transformDemin(context, paddingRight),(int)transformDemin(context, paddingBottom));

        //设置背景颜色，如果参数为空则默认不设置背景。
        if(checkIsNull(backgroundColor)){
            //textView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            textView.setBackgroundColor(Color.parseColor(backgroundColor));
        }

        //设置是否可见
        textView.setVisibility(visibility);

        //设置点击事件
        if(onClick!=null){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.OnClick();
                }
            });
        }

        return textView;
    }


    public static View getUnderLine(Context context, int height, String backgroundColor, int paddingLeft, int paddingRight){
        View view = new View(context);
        view.setLayoutParams(getLLParamsSimpleMWHP(context,height,paddingLeft,paddingRight));
        view.setBackgroundColor(Color.parseColor(backgroundColor));
        return view;
    }



    /**
     * 获取params配置
     *
     * @param type         区分是那种父布局
     * @param context
     * @param w            设置宽度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置宽度，大于等于0时则表示WRAP_CONTENT而且设置w的宽度
     * @param h            设置高度 如果-2表示MATCH_PARENT，-1表示WRAP_CONTENT而且不设置高度，大于等于0时则表示WRAP_CONTENT而且设置h的高度
     * @param weight       设置权重 大于0时表示设置，而且值为weight
     * @param marginLeft   设置左外边距 大于0时表示设置，而且值为marginLeft
     * @param marginRight  设置右外边距 大于0时表示设置，而且值为marginRight
     * @param marginTop    设置上外边距 大于0时表示设置，而且值为marginTop
     * @param marginButtom 设置下外边距 大于0时表示设置，而且值为marginButtom
     * @param gravity      设置Android:layout_gravity属性 -1为不设置
     * @param rule         设置rule属性 null为不设置
     * @return
     */
    public static ViewGroup.LayoutParams getParams(String type, Context context, int w, int h, int weight, int marginLeft, int marginRight, int marginTop, int marginButtom, int gravity, List<Integer> rule) {
        ViewGroup.LayoutParams params = null;
        int width;
        int height;
        //首先判断高宽
        if (w == -2) {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (h == -2) {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        //区分生成那个类型的params
        if (type.equals(LinearLayout_Name)) {
            params = new LinearLayout.LayoutParams(width, height);
            //设置外边距
            params = setMarginLL(params, context, marginLeft, marginRight, marginTop, marginButtom);

            //设置Android:layout_gravity属性
            if (gravity != -1) {
                params = setLayout_GravityLL(params, gravity);
            }

        } else if (type.equals(RelativeLayout_Name)) {
            params = new RelativeLayout.LayoutParams(width, height);
            //设置外边距
            params = setMarginRL(params, context, marginLeft, marginRight, marginTop, marginButtom);
        }

        //设置权重
        if (weight > 0) {
            params = setWeightLL(params, weight);
        }


        //当判断w的值大于等于0时，把params的宽度设置
        if (w >= 0) {
            params.width = (int) transformDemin(context, w);
        }
        //当判断h的值大于等于0时，把params的高度设置
        if (h >= 0) {
            params.height = (int) transformDemin(context, h);
        }

        //设置rule
        if (rule != null) {
            params = setRuleRL(params, rule);
        }
        return params;
    }


    public static ViewGroup.LayoutParams setMarginLL(ViewGroup.LayoutParams param, Context context, int marginLeft, int marginRight, int marginTop, int marginButtom) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
        if (marginLeft > 0 || marginRight > 0 || marginTop > 0 || marginButtom > 0) {
            //设置外边距
            if (marginLeft > 0) {
                params.leftMargin = (int) transformDemin(context, marginLeft);
            }
            if (marginRight > 0) {
                params.rightMargin = (int) transformDemin(context, marginRight);
            }
            if (marginTop > 0) {
                params.topMargin = (int) transformDemin(context, marginTop);
            }
            if (marginButtom > 0) {
                params.bottomMargin = (int) transformDemin(context, marginButtom);
            }
            return params;
        } else {
            return param;
        }


    }

    public static ViewGroup.LayoutParams setMarginRL(ViewGroup.LayoutParams param, Context context, int marginLeft, int marginRight, int marginTop, int marginButtom) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) param;
        if (marginLeft > 0 || marginRight > 0 || marginTop > 0 || marginButtom > 0) {
            //设置外边距
            if (marginLeft > 0) {
                params.leftMargin = (int) transformDemin(context, marginLeft);
            }
            if (marginRight > 0) {
                params.rightMargin = (int) transformDemin(context, marginRight);
            }
            if (marginTop > 0) {
                params.topMargin = (int) transformDemin(context, marginTop);
            }
            if (marginButtom > 0) {
                params.bottomMargin = (int) transformDemin(context, marginButtom);
            }
            return params;
        } else {
            return param;
        }
    }

    public static ViewGroup.LayoutParams setRuleRL(ViewGroup.LayoutParams param, List<Integer> rule) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) param;
        //设置rule
        for (int i = 0; i < rule.size(); i++) {
            params.addRule(rule.get(i));
        }
        return params;
    }

    public static ViewGroup.LayoutParams setWeightLL(ViewGroup.LayoutParams param, int weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
        //设置权重
        if (weight > 0) {
            params.weight = weight;
            return params;
        } else {
            return param;
        }

    }

    public static ViewGroup.LayoutParams setLayout_GravityLL(ViewGroup.LayoutParams param, int gravity) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
        if (gravity != -1) {
            params.gravity = gravity;
            return params;
        } else {
            return param;
        }

    }


    /**
     * 为控件设置大众参数
     *
     * @param view
     * @param context
     * @param paddingLeft     设置左内边距
     * @param paddingRight    设置右内边距
     * @param paddingTop      设置上内边距
     * @param paddingBottom   设置下内边距
     * @param backgroundColor 设置背景色，参数空时不设置
     * @param visibility      设置是否可见
     * @param id              设置id,参数为-1时不设置
     * @param isEnabled       设置是否点击
     * @return
     */
    public static View setNormalParams(final View view, Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, int id, boolean isEnabled) {

        //设置Android:gravity属性
//        if(gravity!=-1){
//            view.setGravity(gravity);
//        }
        //设置内边距
        view.setPadding((int) transformDemin(context, paddingLeft), (int) transformDemin(context, paddingTop), (int) transformDemin(context, paddingRight), (int) transformDemin(context, paddingBottom));


        //设置背景色，参数空时不设置
        if (!checkIsNull(backgroundColor)) {
            view.setBackgroundColor(Color.parseColor(backgroundColor));
        }


        //设置是否可见
        view.setVisibility(visibility);
        //设置id,参数为-1时不设置
        if (id != -1) {
            view.setId(id);
        }

        //设置是否点击
        view.setEnabled(isEnabled);
        return view;
    }


    /**
     * 获取textview
     *
     * @param context
     * @param title           设置内容
     * @param TextColor       设置字体颜色
     * @param TextSize        设置字体大小
     * @param gravity         设置Android:gravity属性
     * @param paddingLeft     设置左内边距
     * @param paddingRight    设置右内边距
     * @param paddingTop      设置上内边距
     * @param paddingBottom   设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认不设置背景。
     * @param visibility      设置是否可见
     * @param textStyle       设置字体风格（加粗）
     * @param id              设置id,参数为-1时不设置
     * @param isEnabled       设置是否点击
     * @param singleLine      设置是单行
     * @return
     */
    public static TextView getTextView(Context context, String title, String TextColor, float TextSize, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, String textStyle, int id, boolean isEnabled, boolean singleLine) {

        TextView textView = new TextView(context);

        setNormalParams(textView, context, paddingLeft, paddingRight, paddingTop, paddingBottom, backgroundColor, visibility, id, isEnabled);

        //设置内容
        textView.setText(title);


        //设置字体颜色
        if (!checkIsNull(TextColor)) {
            textView.setTextColor(Color.parseColor(TextColor));
        }

        //设置字体大小
        textView.setTextSize(COMPLEX_UNIT_PX, transformFontDemin(context, TextSize));

        //设置Android:gravity属性
        if (gravity != -1) {
            textView.setGravity(gravity);
        }

        //设置字体风格
        if (!checkIsNull(textStyle)) {
            //斜体
            if (textStyle.equals("italic")) {
                textView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                //加粗
            } else if (textStyle.equals("bold")) {
                textView.getPaint().setFakeBoldText(true);
            } else {
                textView.getPaint().setFakeBoldText(false);
            }

        }

        //设置单行
        if (singleLine) {
            textView.setSingleLine();
            //不设置这里会不显示后面的点点点
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }


        return textView;
    }


    /**
     * 获取EditText
     *
     * @param context
     * @param title           设置内容
     * @param TextColor       设置字体颜色
     * @param TextSize        设置字体大小
     * @param gravity         设置Android:gravity属性
     * @param paddingLeft     设置左内边距
     * @param paddingRight    设置右内边距
     * @param paddingTop      设置上内边距
     * @param paddingBottom   设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认不设置背景。
     * @param visibility      设置是否可见
     * @param textStyle       设置字体风格（加粗）
     * @param id              设置id,参数为-1时不设置
     * @param isEnabled       设置是否点击
     * @param singleLine      设置是单行
     * @param textColorHint   设置提示字体颜色
     * @param hint            设置提示内容
     * @param inputType       设置限制输入格式
     * @return
     */
    public static EditText getEditText(Context context, String title, String TextColor, float TextSize, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, String textStyle, int id, boolean isEnabled, boolean singleLine, String textColorHint, String hint, int inputType) {

        EditText textView = new EditText(context);

        if(backgroundColor.equals("@null")||backgroundColor.equals("null")){
            setNormalParams(textView, context, paddingLeft, paddingRight, paddingTop, paddingBottom, "", visibility, id, isEnabled);
            textView.setBackgroundResource(0);
        }else{
            setNormalParams(textView, context, paddingLeft, paddingRight, paddingTop, paddingBottom, backgroundColor, visibility, id, isEnabled);
        }
        textView.setHintTextColor(Color.parseColor("#969696"));

        //设置内容
        textView.setText(title);

        //设置字体颜色
        if (!checkIsNull(TextColor)) {
            textView.setTextColor(Color.parseColor(TextColor));
        }

        //设置字体大小
        textView.setTextSize(COMPLEX_UNIT_PX, transformFontDemin(context, TextSize));

        //设置Android:gravity属性
        if (gravity != -1) {
            textView.setGravity(gravity);
        }

        //设置字体风格
        if (!checkIsNull(textStyle)) {
            //斜体
            if (textStyle.equals("italic")) {
                textView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                //加粗
            } else if (textStyle.equals("bold")) {
                textView.getPaint().setFakeBoldText(true);
            } else {
                textView.getPaint().setFakeBoldText(false);
            }

        }

        //设置单行
        if (singleLine) {
            textView.setSingleLine();
            //不设置这里会不显示后面的点点点
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        //设置提示字体颜色
        if (!checkIsNull(textColorHint)) {
            textView.setHintTextColor(Color.parseColor(textColorHint));
        }
        //设置提示内容
        if (!checkIsNull(hint)) {
            textView.setHint(hint);
        }
        //设置限制输入格式
        if (inputType != -1) {
            textView.setInputType(inputType);
            // textView.setInputType(InputType.TYPE_CLASS_TEXT);
        }



        return textView;
    }


    /**
     * 获取Button
     *
     * @param context
     * @param title           设置内容
     * @param TextColor       设置字体颜色
     * @param TextSize        设置字体大小
     * @param gravity         设置Android:gravity属性
     * @param paddingLeft     设置左内边距
     * @param paddingRight    设置右内边距
     * @param paddingTop      设置上内边距
     * @param paddingBottom   设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认不设置背景。
     * @param visibility      设置是否可见
     * @param textStyle       设置字体风格（加粗）
     * @param id              设置id,参数为-1时不设置
     * @param isEnabled       设置是否点击
     * @param singleLine      设置是单行
     * @return
     */
    public static Button getButton(Context context, String title, String TextColor, float TextSize, int gravity, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, String textStyle, int id, boolean isEnabled, boolean singleLine) {

        Button textView = new Button(context);

        setNormalParams(textView, context, paddingLeft, paddingRight, paddingTop, paddingBottom, backgroundColor, visibility, id, isEnabled);

        //设置内容
        textView.setText(title);

        //设置字体颜色
        if (!checkIsNull(TextColor)) {
            textView.setTextColor(Color.parseColor(TextColor));
        }

        //设置字体大小
        textView.setTextSize(COMPLEX_UNIT_PX, transformFontDemin(context, TextSize));

        //设置Android:gravity属性
        if (gravity != -1) {
            textView.setGravity(gravity);
        }

        //设置字体风格
        if (!checkIsNull(textStyle)) {
            //斜体
            if (textStyle.equals("italic")) {
                textView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                //加粗
            } else if (textStyle.equals("bold")) {
                textView.getPaint().setFakeBoldText(true);
            } else {
                textView.getPaint().setFakeBoldText(false);
            }

        }

        //设置单行
        if (singleLine) {
            textView.setSingleLine();
            //不设置这里会不显示后面的点点点
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }


        return textView;
    }

    /**
     * 获取View
     *
     * @param context
     * @param paddingLeft     设置左内边距
     * @param paddingRight    设置右内边距
     * @param paddingTop      设置上内边距
     * @param paddingBottom   设置下内边距
     * @param backgroundColor 设置背景颜色，如果参数为空则默认为白色背景。
     * @param visibility      设置是否可见
     * @return
     */
    public static View getView(Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility) {
        View linearLayout = new View(context);


        setNormalParams(linearLayout, context, paddingLeft, paddingRight, paddingTop, paddingBottom, backgroundColor, visibility, -1, false);




        return linearLayout;
    }


    public static ImageView getImageView(Context context, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, String backgroundColor, int visibility, int id, boolean isEnabled, int srcId, ImageView.ScaleType scaleType, boolean isAdjust) {
        ImageView imageView = new ImageView(context);
        setNormalParams(imageView, context, paddingLeft, paddingRight, paddingTop, paddingBottom, backgroundColor, visibility, id, isEnabled);


        //设置图片源
        if(srcId!=-1){
            imageView.setImageResource(srcId);
        }
        if(scaleType!=null){
            imageView.setScaleType(scaleType);
        }

        if(isAdjust){
            imageView.setAdjustViewBounds(isAdjust);
        }
        return imageView;
    }

    /**
     * @param view
     * @param ResId
     */
    public static void setSizeUtil(View view, int ResId){
        View baseV = view.findViewById(ResId);
        setSizeUtilBase(baseV);
    }
    public static void setSizeAutoUtil(Activity activity){

        List<View> viewList = getAllChildViews(activity.getWindow().getDecorView());
        for (int i = 0; i < viewList.size(); i++) {
            setSizeUtilBase(viewList.get(i));
        }
    }
    private static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    public static void setSizeAutoUtil(Activity activity, int layoutId){
        LinearLayout loginLayout = (LinearLayout) activity.getLayoutInflater().inflate(layoutId, null);
        for (int i = 0; i < loginLayout.getChildCount(); i++) {
            View v=loginLayout.getChildAt(i);
            //如何判断是Button或者是TextBox
            setSizeUtilBase(v);
        }
    }


    public static void setSizeUtilBase(View baseV){
        Context context = baseV.getContext();
        int paddingTop = baseV.getPaddingTop();
        int paddingBottom = baseV.getPaddingBottom();
        int paddingLeft = baseV.getPaddingLeft();
        int paddingRight = baseV.getPaddingRight();
        baseV.setPadding((int) transformDemin(context,px2dip(context,paddingLeft)),
                (int) transformDemin(context,px2dip(context,paddingTop)),
                (int) transformDemin(context,px2dip(context,paddingRight)),
                (int) transformDemin(context,px2dip(context,paddingBottom)));

        ViewGroup.LayoutParams params = baseV.getLayoutParams();
        if(params instanceof LinearLayout.LayoutParams){
            LinearLayout.LayoutParams tempP = (LinearLayout.LayoutParams) params;
            tempP.topMargin=(int) transformDemin(context,px2dip(context,tempP.topMargin));
            tempP.bottomMargin=(int) transformDemin(context,px2dip(context,tempP.bottomMargin));
            tempP.leftMargin=(int) transformDemin(context,px2dip(context,tempP.leftMargin));
            tempP.rightMargin=(int) transformDemin(context,px2dip(context,tempP.rightMargin));

            if(tempP.width>0)
                tempP.width=(int) transformDemin(context,px2dip(context,tempP.width));
            if(tempP.height>0)
                tempP.height=(int) transformDemin(context,px2dip(context,tempP.height));

            baseV.setLayoutParams(tempP);
        }else if(params instanceof RelativeLayout.LayoutParams){
            RelativeLayout.LayoutParams tempP = (RelativeLayout.LayoutParams) params;
            tempP.topMargin=(int) transformDemin(context,px2dip(context,tempP.topMargin));
            tempP.bottomMargin=(int) transformDemin(context,px2dip(context,tempP.bottomMargin));
            tempP.leftMargin=(int) transformDemin(context,px2dip(context,tempP.leftMargin));
            tempP.rightMargin=(int) transformDemin(context,px2dip(context,tempP.rightMargin));
            if(tempP.width>0)
                tempP.width=(int) transformDemin(context,px2dip(context,tempP.width));
            if(tempP.height>0)
                tempP.height=(int) transformDemin(context,px2dip(context,tempP.height));
            baseV.setLayoutParams(tempP);
        }else if(params instanceof FrameLayout.LayoutParams){
            FrameLayout.LayoutParams tempP = (FrameLayout.LayoutParams) params;
            tempP.topMargin=(int) transformDemin(context,px2dip(context,tempP.topMargin));
            tempP.bottomMargin=(int) transformDemin(context,px2dip(context,tempP.bottomMargin));
            tempP.leftMargin=(int) transformDemin(context,px2dip(context,tempP.leftMargin));
            tempP.rightMargin=(int) transformDemin(context,px2dip(context,tempP.rightMargin));
            if(tempP.width>0)
                tempP.width=(int) transformDemin(context,px2dip(context,tempP.width));
            if(tempP.height>0)
                tempP.height=(int) transformDemin(context,px2dip(context,tempP.height));
            baseV.setLayoutParams(tempP);
        } else if(params instanceof RecyclerView.LayoutParams){
            RecyclerView.LayoutParams tempP = (RecyclerView.LayoutParams) params;
            tempP.topMargin=(int) transformDemin(context,px2dip(context,tempP.topMargin));
            tempP.bottomMargin=(int) transformDemin(context,px2dip(context,tempP.bottomMargin));
            tempP.leftMargin=(int) transformDemin(context,px2dip(context,tempP.leftMargin));
            tempP.rightMargin=(int) transformDemin(context,px2dip(context,tempP.rightMargin));
            if(tempP.width>0)
                tempP.width=(int) transformDemin(context,px2dip(context,tempP.width));
            if(tempP.height>0)
                tempP.height=(int) transformDemin(context,px2dip(context,tempP.height));
            baseV.setLayoutParams(tempP);
        }
        else{
           // tLogE(context,"没有检查到父控件的类型："+baseV.getId());
        }

        //tLogE(context,""+baseV.getId());


        //当是TextView
        if(baseV instanceof TextView){
            TextView tempV = (TextView)baseV;
            float textSize = tempV.getTextSize();
            // tLogD("字体大小："+textSize);
            tempV.setTextSize(TypedValue.COMPLEX_UNIT_PX,transformFontDemin(context,px2dip(context,textSize)));
        }

        //当是EditText
        if(baseV instanceof EditText){
            EditText tempV = (EditText)baseV;
            float textSize = tempV.getTextSize();
            tempV.setTextSize(TypedValue.COMPLEX_UNIT_PX,transformFontDemin(context,px2dip(context,textSize)));
        }

        //当是Button
        if(baseV instanceof Button){
            Button tempV = (Button)baseV;
            float textSize = tempV.getTextSize();
            tempV.setTextSize(TypedValue.COMPLEX_UNIT_PX,transformFontDemin(context,px2dip(context,textSize)));
        }
        //当是RadioButton
        if(baseV instanceof RadioButton){
            RadioButton tempV = (RadioButton)baseV;
            float textSize = tempV.getTextSize();
            tempV.setTextSize(TypedValue.COMPLEX_UNIT_PX,transformFontDemin(context,px2dip(context,textSize)));
        }
    }



    public static void modifyParams(View view, Context context, int width, int height){
        if(view==null){
           // tLogD("view 是空的");
            return;
        }
        ViewGroup.LayoutParams params= view.getLayoutParams();

        if(width!=-1){
            params.width=(int)transformDemin(context,width);
        }
        if(height!=-1){
            params.height=(int)transformDemin(context,height);
        }

    }

    public static void modifyParams(View view, int resId, int width, int height){

        modifyParams(null,view,resId,width,height,0,0,0,0,0,0,0,0);
    }

    /**
     * 设置尺寸（简化版，控制宽高,四周内边距）
     * @param view
     * @param resId
     * @param width
     * @param height
     * @param paddingLeft
     * @param paddingRight
     * @param paddingTop
     * @param paddingBottom
     */
    public static void modifyParamsN(View view, int resId, int width, int height, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(null,view,resId,width,height,paddingLeft,paddingRight,paddingTop,paddingBottom,0,0,0,0);
    }
    /**
     * 设置尺寸（简化版，控制宽高,四周内边距）
     * @param view
     * @param resId
     * @param width
     * @param height
     * @param paddingLeft
     * @param paddingRight
     * @param paddingTop
     * @param paddingBottom
     */
    public static void modifyParamsN(Activity view, int resId, int width, int height, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(view,null,resId,width,height,paddingLeft,paddingRight,paddingTop,paddingBottom,0,0,0,0);
    }
    /**
     * 设置尺寸（简化版，控制宽高,四周内边距外边距）
     * @param view
     * @param resId
     * @param width
     * @param height
     */
    public static void modifyParamsW(Activity view, int resId, int width, int height, int marginLeft, int marginRight, int marginTop, int marginBottom){
        modifyParams(view,null,resId,width,height,0,0,0,0,marginLeft,marginRight,marginTop,marginBottom);
    }
    /**
     * 设置尺寸（简化版，控制宽高,四周内边距外边距）
     * @param view
     * @param resId
     * @param width
     * @param height
     */
    public static void modifyParamsW(View view, int resId, int width, int height, int marginLeft, int marginRight, int marginTop, int marginBottom){
        modifyParams(null,view,resId,width,height,0,0,0,0,marginLeft,marginRight,marginTop,marginBottom);
    }
    /**
     * 设置尺寸（简化版，只设置外边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyW(View view, int resId, int marginLeft, int marginRight, int marginTop, int marginBottom){
        modifyParams(null,view,resId,-1,-1,0,0,0,0,marginLeft,marginRight,marginTop,marginBottom);
    }
    /**
     * 设置尺寸（简化版，只设置外边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyW(Activity view, int resId, int marginLeft, int marginRight, int marginTop, int marginBottom){
        modifyParams(view,null,resId,-1,-1,0,0,0,0,marginLeft,marginRight,marginTop,marginBottom);
    }
    /**
     * 设置尺寸（简化版，只设置内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyN(View view, int resId, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(null,view,resId,-1,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,0,0,0,0);
    }
    /**
     * 设置尺寸（简化版，只设置外内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyWN(Activity view, int resId, int marginLeft, int marginRight, int marginTop, int marginBottom, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(view,null,resId,-1,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,marginLeft,marginRight,marginTop,marginBottom);
    }

    /**
     * 设置尺寸（简化版，只设置外内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyWN(View view, int resId, int marginLeft, int marginRight, int marginTop, int marginBottom, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(null,view,resId,-1,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,marginLeft,marginRight,marginTop,marginBottom);
    }

    /**
     * 设置尺寸（简化版，只设置外内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsWN(Activity view, int resId, int width, int height, int marginLeft, int marginRight, int marginTop, int marginBottom, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(view,null,resId,width,height,paddingLeft,paddingRight,paddingTop,paddingBottom,marginLeft,marginRight,marginTop,marginBottom);
    }

    /**
     * 设置尺寸（简化版，只设置外内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsWN(View view, int resId, int width, int height, int marginLeft, int marginRight, int marginTop, int marginBottom, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(null,view,resId,width,height,paddingLeft,paddingRight,paddingTop,paddingBottom,marginLeft,marginRight,marginTop,marginBottom);
    }
    /**
     * 设置尺寸（简化版，只设置内边距）
     * @param view
     * @param resId
     */
    public static void modifyParamsOnlyN(Activity view, int resId, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        modifyParams(view,null,resId,-1,-1,paddingLeft,paddingRight,paddingTop,paddingBottom,0,0,0,0);
    }

    /**
     * 设置尺寸（简化版，控制宽高）
     * @param view
     * @param resId
     * @param width
     * @param height
     */
    public static void modifyParams(Activity view, int resId, int width, int height){
        modifyParams(view,null,resId,width,height,0,0,0,0,0,0,0,0);
    }


    public static void modifyFontSize(Activity view, int resId, int textSize) {
        modifyFontSize(view,null,resId,textSize);

    }
    public static void modifyFontSize(View view, int resId, int textSize) {
        modifyFontSize(null,view,resId,textSize);

    }

    public static void modifyFontSize(Activity view, View view2, int resId, int textSize) {

        View v;
        Context context;
        if (view != null) {
            v = view.findViewById(resId);
            context = view.getApplicationContext();
        } else if (view2 != null) {
            v = view2.findViewById(resId);
            context = view2.getContext();
        } else {
            //tLogD("view 是空的");
            return;
        }


        TextView textView = (TextView) v.findViewById(resId);
        textView.setTextSize(COMPLEX_UNIT_PX,transformFontDemin(context,textSize));

    }



    public static void modifyEditTextFontSize(Activity view, int resId, int textSize) {
        modifyFontSize(view,null,resId,textSize);

    }
    public static void modifyEditTextFontSize(View view, int resId, int textSize) {
        modifyFontSize(null,view,resId,textSize);

    }

    public static void modifyEditTextFontSize(Activity view, View view2, int resId, int textSize) {

        View v;
        Context context;
        if (view != null) {
            v = view.findViewById(resId);
            context = view.getApplicationContext();
        } else if (view2 != null) {
            v = view2.findViewById(resId);
            context = view2.getContext();
        } else {
            //tLogD("view 是空的");
            return;
        }


        EditText textView = (EditText) v.findViewById(resId);
        textView.setTextSize(COMPLEX_UNIT_PX,transformFontDemin(context,textSize));

    }


    public static void modifyParams(Activity view, View view2, int resId, int width, int height, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, int marginLeft, int marginRight, int marginTop, int marginBottom){

        View v;
        Context context;
        if(view!=null){
            v = view.findViewById(resId);
            context = view.getApplicationContext();
        }else if(view2!=null){
            v = view2.findViewById(resId);
            context = view2.getContext();
        }else{
            //tLogD("view 是空的");
            return;
        }



        ViewGroup.LayoutParams params= v.getLayoutParams();

        if(width!=-1){
            params.width=(int)transformDemin(context,width);
        }
        if(height!=-1){
            params.height=(int)transformDemin(context,height);
        }

        v.setPadding((int)transformDemin(context,paddingLeft),(int)transformDemin(context,paddingTop),(int)transformDemin(context,paddingRight),(int)transformDemin(context,paddingBottom));


        if(marginLeft>0){
            if(params instanceof LinearLayout.LayoutParams){
                ((LinearLayout.LayoutParams) params).leftMargin=(int)transformDemin(context,marginLeft);
            }
            if(params instanceof RelativeLayout.LayoutParams){
                ((RelativeLayout.LayoutParams) params).leftMargin=(int)transformDemin(context,marginLeft);
            }
//            if(params instanceof RecyclerView.LayoutParams){
//                ((RecyclerView.LayoutParams) params).leftMargin=(int)transformDemin(context,marginLeft);
//            }
        }
        if(marginRight>0){
            if(params instanceof LinearLayout.LayoutParams){
                ((LinearLayout.LayoutParams) params).rightMargin=(int)transformDemin(context,marginRight);
            }
            if(params instanceof RelativeLayout.LayoutParams){
                ((RelativeLayout.LayoutParams) params).rightMargin=(int)transformDemin(context,marginRight);
            }
//            if(params instanceof RecyclerView.LayoutParams){
//                ((RecyclerView.LayoutParams) params).rightMargin=(int)transformDemin(context,marginRight);
//            }
        }
        if(marginTop>0){
            if(params instanceof LinearLayout.LayoutParams){
                ((LinearLayout.LayoutParams) params).topMargin=(int)transformDemin(context,marginTop);
            }
            if(params instanceof RelativeLayout.LayoutParams){
                ((RelativeLayout.LayoutParams) params).topMargin=(int)transformDemin(context,marginTop);
            }
//            if(params instanceof RecyclerView.LayoutParams){
//                ((RecyclerView.LayoutParams) params).topMargin=(int)transformDemin(context,marginTop);
//            }
        }
        if(marginBottom>0){
            if(params instanceof LinearLayout.LayoutParams){
                ((LinearLayout.LayoutParams) params).bottomMargin=(int)transformDemin(context,marginBottom);
            }
            if(params instanceof RelativeLayout.LayoutParams){
                ((RelativeLayout.LayoutParams) params).bottomMargin=(int)transformDemin(context,marginBottom);
            }
//            if(params instanceof RecyclerView.LayoutParams){
//                ((RecyclerView.LayoutParams) params).bottomMargin=(int)transformDemin(context,marginBottom);
//            }
        }
    }




    /**
     * 输入百分比，获取整个屏幕的宽度百分比
     *
     * @param number
     * @return
     */
    public static int getDPPercentW(float number) {
        return (int) (dpWidth * number / 100);
    }

    /**
     * 输入百分比，获取整个屏幕的高度百分比
     *
     * @param number
     * @return
     */
    public static int getDPPercentH(float number) {
        return (int) (dpHeight * number / 100);

    }
    /**
     * 简化版导航(中间标题)
     *
     * @param context
     * @param title
     * @return
     */
    public static View NavigationViewOnlyCenter(Context context, String title) {
        return NavigationView(context, null, title, null, 50, 50, 0, 0, 10, "#ffffff", "#75ACDC", 15, 13, 20, 20).getView();
    }

    /**
     * 简化版导航(有左侧图标，中间标题)
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static View NavigationView(Context context, final OnNavigationClickLister lister, String title) {
        return NavigationView(context, lister, title, null, 50, 50, R.drawable.chat_back, 0, 10, "#ffffff", "#00BFFF", 15, 13, 20, 20).getView();
    }

    /**
     * 简化版导航(有左侧图标，中间标题,右边图标)
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static View NavigationViewHaveRightI(Context context, final OnNavigationClickLister lister, String title) {
        return NavigationView(context, lister, title, null, 50, 50, R.drawable.chat_back, 0, 10, "#ffffff", "#75ACDC", 15, 13, 20, 20).getView();
    }
    /**
     * 简化版导航(有左侧图标，中间标题)
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static View NavigationViewHaveRight(Context context, final OnNavigationClickLister lister, String title, String rightTitle) {
        return NavigationView(context, lister, title, rightTitle, 50, 50, R.drawable.chat_back, 0, 10, "#ffffff", "#000000", 15, 13, 20, 20).getView();
    }
//    /**
//     * 简化版导航（有左侧图标，中间标题，右侧图标）
//     *
//     * @param context
//     * @param lister
//     * @param title
//     * @return
//     */
//    public static View NavigationView(Context context, final OnNavigationClickLister lister, String title, int rightPicuter) {
//        return NavigationView(context, lister, title, null, 50, 40, R.drawable.nav_back_left, rightPicuter, 10, "#000000", "#f5f5f5", 15, 13, 20, 20);
//    }

    /**
     * 简化版导航(有左侧图标，中间标题,右侧标题)
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static View NavigationViewHaveCR(Context context, final OnNavigationClickLister lister, String title, String righttitle) {
        return NavigationView(context, lister, title, righttitle, 50, 50, R.drawable.chat_back, 0, 10, "#ffffff", "#75ACDC", 15, 13, 20, 20).getView();
    }


    /**
     * 简化版导航(有左侧图标，中间标题,右侧标题)待回调接口
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static KeyViewListerModel NavigationViewHaveCRL(Context context, final OnNavigationClickLister lister, String title, String righttitle) {
        return NavigationView(context, lister, title, righttitle, 50, 50, R.drawable.chat_back, 0, 10, "#ffffff", "#75ACDC", 15, 13, 20, 20);
    }
    /**
     * 简化版导航(有左侧图标，中间标题,右侧标题)待回调接口
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static KeyViewListerModel NavigationViewNoLeftHaveCRL(Context context, final OnNavigationClickLister lister, String title, String righttitle) {
        return NavigationView(context, lister, title, righttitle, 50, 50, 0, 0, 10, "#ffffff", "#75ACDC", 15, 13, 20, 20);
    }
    /**
     * 简化版导航(有中间标题,右侧标题)
     *
     * @param context
     * @param lister
     * @param title
     * @return
     */
    public static View NavigationViewNoLeft(Context context, final OnNavigationClickLister lister, String title, String righttitle) {
        return NavigationView(context, lister, title, righttitle, 50, 50, 0, 0, 10, "#000000", "#f5f5f5", 15, 13, 20, 20).getView();
    }

//    /**
//     * 简化版导航(左侧是关闭图标，有中间标题,右侧图标)
//     *
//     * @param context
//     * @param lister
//     * @param title
//     * @return
//     */
//    public static View NavigationViewClose(Context context, final OnNavigationClickLister lister, String title) {
//        return NavigationView(context, lister, title, "", 50, 40, R.drawable.nav_close_left, R.drawable.nav_reset_right, 10, "#000000", "#f5f5f5", 15, 13, 20, 20);
//    }


    /**
     * 导航
     *
     * @param context
     * @param lister
     * @param title             中间标题
     * @param rightTitle        右侧标题
     * @param height            整个导航的高度
     * @param width             左右触发区的宽度
     * @param leftPicture       左侧触发区图标
     * @param rightPicture      右侧触发区图标
     * @param jiange            两边的间隔
     * @param TextColor         字体的颜色
     * @param backgroundColor   整体背景颜色
     * @param titleSize         中间标题字体大小
     * @param rightTitleSize    右边标题字体大小
     * @param pictureWidthSize  图标的宽度
     * @param pictureHeightSize 图标的高度
     * @return
     */
    public static KeyViewListerModel NavigationView(Context context, final OnNavigationClickLister lister, String title, String rightTitle, int height, int width, int leftPicture, int rightPicture, int jiange, String TextColor, String backgroundColor, int titleSize, int rightTitleSize, int pictureWidthSize, int pictureHeightSize) {
        //-------------------------最外层----------------------------------
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = (int) transformDemin(context, height);
        RelativeLayout ALLRL = new RelativeLayout(context);
        ALLRL.setLayoutParams(params);
        ALLRL.setBackgroundColor(Color.parseColor(backgroundColor));
        //---------------------------最外层--------------------------------

        if (leftPicture != 0) {
            //---------------------------左边按钮外壳--------------------------------

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params1.width = (int) transformDemin(context, width);
            RelativeLayout RL1 = new RelativeLayout(context);
            RL1.setLayoutParams(params1);
            RL1.setPadding((int) transformDemin(context, jiange), 0, 0, 0);
            RL1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lister.onLeftClick();
                }
            });
            //---------------------------左边按钮外壳--------------------------------
            //---------------------------左边图标--------------------------------
            if (leftPicture != 0) {
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params2.width = (int) transformDemin(context, pictureWidthSize);
                params2.height = (int) transformDemin(context, pictureHeightSize);
                params2.addRule(RelativeLayout.CENTER_VERTICAL);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(leftPicture);
                imageView.setLayoutParams(params2);
                RL1.addView(imageView);
            }
            //---------------------------左边图标--------------------------------

            ALLRL.addView(RL1);
        }


        //---------------------------中间标题--------------------------------
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.addRule(RelativeLayout.CENTER_IN_PARENT);
        final TextView textView = new TextView(context);
        textView.setLayoutParams(params3);
        textView.setTextSize(COMPLEX_UNIT_PX, transformDemin(context, titleSize));
        textView.setTextColor(Color.parseColor(TextColor));
        textView.setText(title);
        //---------------------------中间标题--------------------------------
        ALLRL.addView(textView);
        //---------------------------右边触发区外壳--------------------------------
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //params4.width = (int) transformDemin(context, width);
        params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout RL2 = new RelativeLayout(context);
        RL2.setPadding(0, 0, (int) transformDemin(context, jiange), 0);
        RL2.setLayoutParams(params4);
        RL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lister.onRightClick();
            }
        });
        //---------------------------右边触发区外壳--------------------------------
        //---------------------------右边触发区图标--------------------------------
        if (rightPicture != 0) {
            RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params5.width = (int) transformDemin(context, pictureWidthSize);
            params5.height = (int) transformDemin(context, pictureHeightSize);
            params5.addRule(RelativeLayout.CENTER_VERTICAL);
            params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ImageView imageView2 = new ImageView(context);
            imageView2.setImageResource(rightPicture);
            imageView2.setLayoutParams(params5);
            RL2.addView(imageView2);
        }

        //---------------------------右边触发区图标--------------------------------

        //---------------------------右边触发区文字--------------------------------
        RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params6.addRule(RelativeLayout.CENTER_VERTICAL);
        final TextView textView2 = new TextView(context);
        textView2.setLayoutParams(params6);
        textView2.setTextSize(COMPLEX_UNIT_PX, transformDemin(context, rightTitleSize));
        textView2.setTextColor(Color.parseColor(TextColor));
        textView2.setText(rightTitle);
        //---------------------------右边触发区文字--------------------------------
        RL2.addView(textView2);
        ALLRL.addView(RL2);


        KeyViewListerModel model = new KeyViewListerModel();
        model.setView(ALLRL);

        OnNavigationModifyLister modifyLister = new OnNavigationModifyLister() {
            @Override
            public void modifyTitle(String content) {
                textView.setText(content);
            }

            @Override
            public void modifyRightTitle(String content) {
                textView2.setText(content);
            }
        };
        model.setOnChoosingOnClick(modifyLister);


        return model;
    }


    /**
     * 生成一个scroll
     * @param context
     * @param addV
     * @return
     */
    public static View getScroll(Context context, View addV){
        ScrollView scrollView = new ScrollView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(params);

        LinearLayout plug1 = ViewPlugBaseLayout.getLinearLayout(context, LinearLayout.VERTICAL,-1,0,0,0,0,"#ffffff", View.VISIBLE,null);
        plug1.setLayoutParams(ViewPlugBaseLayout.getParams("LinearLayout",context,-2,-1,0,0,0,0,0,-1,null));



        plug1.addView(addV);
        scrollView.addView(plug1);

        return scrollView;
    }
    /**
     * 生成一个scroll
     * @param context
     * @param addV
     * @return
     */
    public static View getScroll(Context context, View addV, String background){
        ScrollView scrollView = new ScrollView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(params);
        scrollView.setBackgroundColor(Color.parseColor(background));
        LinearLayout plug1 = ViewPlugBaseLayout.getLinearLayout(context, LinearLayout.VERTICAL,-1,0,0,0,0,background, View.VISIBLE,null);
        plug1.setLayoutParams(ViewPlugBaseLayout.getParams("LinearLayout",context,-2,-1,0,0,0,0,0,-1,null));



        plug1.addView(addV);
        scrollView.addView(plug1);

        return scrollView;
    }
    /**
     * 生成shape背景(请填参数时以dp为单位)
     *
     * @param strokeWidth 背景线的宽度
     * @param roundRadius 四个角的弧度
     * @param lineColor   背景线的颜色
     * @param backgroud   背景的颜色
     * @return
     */
    public static Drawable makeBackgroundKuang(Context context, int strokeWidth, int roundRadius, String lineColor, String backgroud) {
        int strokeColor = Color.parseColor(lineColor);
        int fillColor = Color.parseColor(backgroud);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius((int) transformDemin(context, roundRadius));
        gd.setStroke((int) transformDemin(context, strokeWidth), strokeColor);

        return gd;
    }

//    public static BackgroundDarkPopupWindow makeBackgroundPopwindow(Context context, View addV, View dropV) {
//        BackgroundDarkPopupWindow mPopupWindow = new BackgroundDarkPopupWindow(addV, WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//
//
//        mPopupWindow.setDarkStyle(-1);
//        mPopupWindow.setDarkColor(Color.parseColor("#5e000000"));
//        mPopupWindow.resetDarkPosition();
//        //控制在什么地方弹出
//        if (dropV != null) {
//            mPopupWindow.darkBelow(dropV);
//            mPopupWindow.showAsDropDown(dropV, dropV.getRight() / 2, 0);
//        }
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
//
//        return mPopupWindow;
//    }
    /**
     * 检查3个EditText是否有值存在
     *
     * @param editText1
     * @param editText2
     * @return
     */
    public static boolean checkEidtext3(EditText editText1, EditText editText2, EditText editText3) {
        String temp1 = editText1.getText().toString().trim();
        String temp2 = editText2.getText().toString().trim();
        String temp3 = editText3.getText().toString().trim();
        boolean flag = true;
        if (temp1.equals("") || temp2.equals("") || temp3.equals("")) {
            flag = false;
        }


        return flag;
    }


    public static BackgroundDarkPopupWindow makeBackgroundPopwindow(Context context, View addV, View dropV) {
        BackgroundDarkPopupWindow mPopupWindow = new BackgroundDarkPopupWindow(addV, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);


        mPopupWindow.setDarkStyle(-1);
        mPopupWindow.setDarkColor(Color.parseColor("#000000"));
        mPopupWindow.resetDarkPosition();
        //控制在什么地方弹出
        if (dropV != null) {
            mPopupWindow.darkBelow(dropV);
            mPopupWindow.showAsDropDown(dropV, dropV.getRight() / 2, 0);
            //  mPopupWindow.showAtLocation(dropV, Gravity.CENTER_HORIZONTAL, 0, dropV.getTop());
            // mPopupWindow.showAsDropDown(dropV);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));

        return mPopupWindow;
    }


    /**
     * 检查5个EditText是否有值存在
     *
     * @param editText1
     * @param editText2
     * @return
     */
    public static boolean checkEidtext5(EditText editText1, EditText editText2, EditText editText3, EditText editText4, EditText editText5) {
        String temp1 = editText1.getText().toString().trim();
        String temp2 = editText2.getText().toString().trim();
        String temp3 = editText3.getText().toString().trim();
        String temp4 = editText4.getText().toString().trim();
        String temp5 = editText5.getText().toString().trim();
        boolean flag = true;
        if (temp1.equals("") || temp2.equals("") || temp3.equals("")|| temp4.equals("")|| temp5.equals("")) {
            flag = false;
        }


        return flag;
    }


}
