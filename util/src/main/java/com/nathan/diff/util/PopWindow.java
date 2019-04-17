package com.nathan.diff.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nathan.diff.util.edit.MClearEditText;
import com.nathan.diff.util.plug.ViewPlugBaseLayout;

import android.os.Handler;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


public class PopWindow {
    private Activity activity;
    private BackgroundDarkPopupWindow popupWindow;
    /**
     * 确认按钮事件
     */
    private View.OnClickListener confirmClickListener;
    /**
     * 取消按钮事件
     */
    private View.OnClickListener cancelClickListener;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 确认按钮文字
     */
    private String confirmButtonText;
    /**
     * 取消按钮文字
     */
    private String cancelButtonText;

    /**
     * 是否强制点击按钮，来完成事件
     * 默认为否
     */
    private boolean isMustDoClick;


    /**
     * 在什么位置显示（默认是正中）
     */
    private Integer showLocationStatus;

    /**
     * 点击按钮后是否自动消失（默认是，如果选择否的话，请build的时候获取事件监听，以便控制界面）
     */
    private boolean isClickOverDismiss;

    /**
     * 两个键时的方向是不是垂直的（默认为水平的）
     */
    private boolean isVertical;

    /**
     * 输入框hint的内容
     */
    private String hintEdittextContent;
    /**
     * 输入框的内容
     */
    private String edittextContent;

    /**
     * 输入框输入类型（自定义,设置类自定义后不设置固定模式！）
     */
    private Integer inputModelByMySelf;
    /**
     * 输入框输入类型（已设定固定模式，默认为不设置，1为密码【文本类】，2为密码【纯数字】）
     */
    private Integer inputModelByModel;
    /**
     * 监听画面消失事件
     */
    private BackgroundDarkPopupWindow.OnDismissListener onDismissListener;
    /**
     * 输入框是否显示下划线，默认不显示
     */
    private boolean showEdittextBottomLine;
    /**
     * 设置下划线的颜色
     */
    private Integer setEdittextBottomLineColor;
    /**
     * 设置下划线的宽度
     */
    private Float setEdittextBottomLineWidth;
    /**
     * 设置删除按钮是否显示，默认显示
     */
    private Boolean setEdittextDisableClear;


    /**
     * 是否直接弹出软键盘（默认为否）
     */
    private Boolean isFocusEdittextFlag;

    /**
     * 填充到对话框中间的view
     */
    private View contentView;


    private Integer RLayoutId;

    public interface OnPopWindowListener {
        //让窗口关闭
        void onClose();

        //获取输入框的内容
        String getEdittextContent();
    }

    private OnPopWindowListener onPopWindowListener = new OnPopWindowListener() {
        @Override
        public void onClose() {
            //让窗口关闭
            if (popupHandler != null) {
                Message message = new Message();
                message.what = 1;
                popupHandler.sendMessage(message);

            }


        }

        @Override
        public String getEdittextContent() {

            if (onGetEdittextContentLister != null) {
                return onGetEdittextContentLister.getContext();
            }
            return null;
        }
    };

    /**
     * 用于关闭自定义的view
     */
    public interface OnPopWindowCloseViewListener {
        //让窗口关闭
        void onClose();
    }

    private OnPopWindowCloseViewListener onPopWindowCloseViewListener;

    private OnGetEdittextContentLister onGetEdittextContentLister;

    private interface OnGetEdittextContentLister {
        String getContext();
    }


    private BackgroundDarkPopupWindow.OnDissLister onDissLister = new BackgroundDarkPopupWindow.OnDissLister() {
        @Override
        public void onGoingDiss() {
            if (popupHandler != null) {
                Message message = new Message();
                message.what = 1;
                popupHandler.sendMessage(message);
            }
        }
    };


    private RelativeLayout createBackgroudRL() {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout ALLR = new RelativeLayout(activity);
        ALLR.setLayoutParams(param);
        if (!isMustDoClick) {
            //点击最外层
            ALLR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupHandler != null) {
                        Message message = new Message();
                        message.what = 1;
                        popupHandler.sendMessage(message);
                    }
                }
            });
        }
        return ALLR;
    }
    private RelativeLayout createBackgroudRL22() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = ViewPlugBaseLayout.dip2px(activity, ViewPlugBaseLayout.getDPPercentW(80));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout ALLRL = new RelativeLayout(activity);
        ALLRL.setLayoutParams(params);
        if (!isMustDoClick) {
            //点击最外层
            ALLRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupHandler != null) {
                        Message message = new Message();
                        message.what = 1;
                        popupHandler.sendMessage(message);
                    }
                }
            });
        }
        return ALLRL;
    }
    private RelativeLayout createBackgroudRL2() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = ViewPlugBaseLayout.dip2px(activity, ViewPlugBaseLayout.getDPPercentW(80));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout ALLRL = new RelativeLayout(activity);
        //ALLRL.setBackgroundColor(Color.parseColor("#00f5f5f5"));
        ALLRL.setLayoutParams(params);
        ALLRL.setBackgroundResource(R.drawable.mn_rectangle_while_background);
        ALLRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return ALLRL;
    }

    private LinearLayout createBackgroundLL3() {
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.height = (int) ViewPlugBaseLayout.transformDemin(context, height);

        params2.width = width;
        params2.leftMargin = margin;
        params2.rightMargin = margin;
        params2.topMargin = margin;
        params2.bottomMargin = margin;
        LinearLayout LL1 = new LinearLayout(activity);
        LL1.setOrientation(LinearLayout.VERTICAL);
        LL1.setLayoutParams(params2);

        return LL1;
    }

    private TextView createTitle() {
        //标题
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params4.gravity = Gravity.CENTER;
        params4.bottomMargin = fontMargin;
        TextView textView1 = new TextView(activity);
        if (isNullOrEmpty(title)) {
            textView1.setText("提示");
        } else {
            textView1.setText(title);
        }

        textView1.setTextColor(Color.parseColor("#000000"));
        textView1.setTextSize(COMPLEX_UNIT_PX, ViewPlugBaseLayout.transformDemin(activity, 16));
        textView1.setLayoutParams(params4);

        return textView1;
    }


    private View createLine() {
        //分割线
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.height = ViewPlugBaseLayout.dip2px(activity, 1);
        View backgroundLine = new View(activity);
        backgroundLine.setBackgroundColor(Color.parseColor("#f5f5f5"));
        backgroundLine.setLayoutParams(params3);
        return backgroundLine;
    }


    private TextView createContent() {
        //内容
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params5.topMargin = fontMargin;
        params5.bottomMargin = fontMargin;
        TextView textView2 = new TextView(activity);
        if (isNullOrEmpty(content)) {
            textView2.setText("内容...");
        } else {
            textView2.setText(content);
        }
        textView2.setTextColor(Color.parseColor("#929292"));
        textView2.setTextSize(COMPLEX_UNIT_PX, ViewPlugBaseLayout.transformDemin(activity, 14));
        textView2.setLayoutParams(params5);
        return textView2;
    }


    private EditText createEdittext() {
        //内容
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params5.topMargin = fontMargin;
        params5.bottomMargin = fontMargin;
        final MClearEditText textView2 = new MClearEditText(activity);
        //设置是否有下划线
        textView2.setShowBottomLine(showEdittextBottomLine);

        //设置下划线的颜色
        if (setEdittextBottomLineColor != null) {
            textView2.setDefaultBottomLineColor(setEdittextBottomLineColor);
        }
        //设置下划线的宽度
        if (setEdittextBottomLineWidth != null) {
            textView2.setBottomLineWidth(setEdittextBottomLineWidth);
        }
        //设置删除按钮是否显示
        if (setEdittextDisableClear != null) {
            textView2.setDisableClean(setEdittextDisableClear);
        }

        if (!isNullOrEmpty(hintEdittextContent)) {
            textView2.setHint(hintEdittextContent);
            textView2.setHintTextColor(Color.parseColor("#929292"));
        }
        if (!isNullOrEmpty(edittextContent)) {
            textView2.setText(edittextContent);
        }
        if (inputModelByMySelf != null) {
            textView2.setInputType(inputModelByMySelf);
        } else {
            if (inputModelByModel != null) {
                //密码状态
                if (inputModelByModel == 1) {
                    textView2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else if (inputModelByModel == 2) {
                    textView2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }
            }
        }

        textView2.setTextColor(Color.parseColor("#000000"));
        textView2.setTextSize(COMPLEX_UNIT_PX, ViewPlugBaseLayout.transformDemin(activity, 14));
        textView2.setLayoutParams(params5);
        if (isFocusEdittextFlag) {
            textView2.setFocusable(true);
            textView2.setFocusableInTouchMode(true);
            textView2.requestFocus();
        } else {
//            textView2.setFocusable(false);
//            textView2.setFocusableInTouchMode(false);
//            textView2.clearFocus();
        }

        onGetEdittextContentLister = new OnGetEdittextContentLister() {
            @Override
            public String getContext() {
                return textView2.getText().toString();
            }
        };

        return textView2;
    }

    private LinearLayout createButtonLL() {
        //按钮行
        LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  params6.height = ViewPlugBaseLayout.dip2px(activity, 60);
        LinearLayout LL2 = new LinearLayout(activity);
        if (isVertical) {
            LL2.setOrientation(LinearLayout.VERTICAL);
        } else {
            LL2.setOrientation(LinearLayout.HORIZONTAL);
        }
        LL2.setLayoutParams(params6);
        return LL2;
    }

    private Button createConfirmButton() {
        //确认按钮
        LinearLayout.LayoutParams params7 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params7.setMargins(10,0,0,0);
        params7.height = ViewPlugBaseLayout.dip2px(activity, 40);
        params7.weight = 1;

        Button button = new Button(activity);
        button.setBackgroundResource(R.drawable.mn_updata_button_item_black_background_bg);
        if (isNullOrEmpty(confirmButtonText)) {
            button.setText("确认");
        } else {
            button.setText(confirmButtonText);
        }
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setTextSize(COMPLEX_UNIT_PX, ViewPlugBaseLayout.transformDemin(activity, 14));
        button.setLayoutParams(params7);
        button.setGravity(Gravity.CENTER);
        if (confirmClickListener != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmClickListener.onClick(v);
                    if (isClickOverDismiss) {
                        if (popupHandler != null) {
                            Message message = new Message();
                            message.what = 1;
                            popupHandler.sendMessage(message);
                        }
                    }

                }
            });
        }

        return button;
    }

    private Button createCancelButton() {
        //取消按钮
        LinearLayout.LayoutParams params8 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params7.setMargins(10,0,0,0);
        params8.weight = 1;

        if (isVertical) {
            params8.topMargin = margin;
        } else {
            params8.rightMargin = margin;
        }

        params8.height = ViewPlugBaseLayout.dip2px(activity, 40);

        Button button2 = new Button(activity);
        button2.setBackgroundResource(R.drawable.mn_user_detail_button_item_while_background_bg);
        if (isNullOrEmpty(cancelButtonText)) {
            button2.setText("取消");
        } else {
            button2.setText(cancelButtonText);
        }
        button2.setTextColor(Color.parseColor("#525353"));
        button2.setTextSize(COMPLEX_UNIT_PX, ViewPlugBaseLayout.transformDemin(activity, 14));
        button2.setLayoutParams(params8);
        button2.setGravity(Gravity.CENTER);
        if (cancelClickListener != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelClickListener.onClick(v);
                    if (isClickOverDismiss) {
                        if (popupHandler != null) {
                            Message message = new Message();
                            message.what = 1;
                            popupHandler.sendMessage(message);
                        }
                    }

                }
            });
        } else {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupHandler != null) {
                        Message message = new Message();
                        message.what = 1;
                        popupHandler.sendMessage(message);
                    }

                }
            });
        }
        return button2;
    }

    private void createPopupWindow(View view) {
        popupWindow = ViewPlugBaseLayout.makeBackgroundPopwindow(activity, view, null);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isMustDoClick) {
            popupWindow.setCloseDarkClick(false);
        }
        if (onDismissListener != null) {
            popupWindow.setDismissListener(onDismissListener);
        }


        //开始显示
        if (popupHandler != null) {
            Message message = new Message();
            message.what = 0;
            popupHandler.sendMessage(message);
        }

        popupWindow.setOnDissLister(onDissLister);

    }

    int width;
    int margin;
    int fontMargin;

    private void initParams() {
        width = ViewPlugBaseLayout.dip2px(activity, ViewPlugBaseLayout.getDPPercentW(80));
        margin = ViewPlugBaseLayout.dip2px(activity, 12);
        fontMargin = ViewPlugBaseLayout.dip2px(activity, 8);
    }

    /**
     * 有1个按钮
     */
    public OnPopWindowListener popOneButton() {
        if (activity.isFinishing()) {
            return null;
        }
        initParams();
        RelativeLayout ALLR = createBackgroudRL();
        RelativeLayout ALLRL2 = createBackgroudRL22();
        RelativeLayout ALLRL = createBackgroudRL2();
        LinearLayout LL1 = createBackgroundLL3();

        TextView textView1 = createTitle();
        safeAddView(LL1,textView1);


        View backgroundLine = createLine();
        safeAddView(LL1,backgroundLine);

        TextView textView2 = createContent();
        safeAddView(LL1,textView2);

        LinearLayout LL2 = createButtonLL();

        Button button = createConfirmButton();
        safeAddView(LL2,button);



        safeAddView(LL1,LL2);
        safeAddView(ALLRL,LL1);
        safeAddView(ALLRL2,ALLRL);
        safeAddView(ALLR,ALLRL2);


        createPopupWindow(ALLR);
        return onPopWindowListener;
    }


    /**
     * 加载一个view有1个按钮
     */
    public OnPopWindowListener popViewOneButton() {
        if (activity.isFinishing()) {
            return null;
        }
        initParams();
       final RelativeLayout ALLR = createBackgroudRL();
        RelativeLayout ALLRL2 = createBackgroudRL22();
        RelativeLayout ALLRL = createBackgroudRL2();

        final LinearLayout LL1 = createBackgroundLL3();

        if (!isNullOrEmpty(title)) {
            TextView textView1 = createTitle();
            safeAddView(LL1,textView1);
            View backgroundLine = createLine();
            safeAddView(LL1,backgroundLine);
        }


        if (contentView != null) {
           // LL1.addView(contentView);
            safeAddView(LL1,contentView);
            onPopWindowCloseViewListener = new OnPopWindowCloseViewListener() {
                @Override
                public void onClose() {
                    if (contentView == null) {
                        return;
                    }
                    LL1.removeView(contentView);
                }
            };
        }


        if (RLayoutId != null) {
            contentView = LayoutInflater.from(activity).inflate(RLayoutId, null);
          //  LL1.addView(contentView);
            safeAddView(LL1,contentView);
        }


        LinearLayout LL2 = createButtonLL();

        Button button = createConfirmButton();

        safeAddView(LL2,button);


        safeAddView(LL1,LL2);
        safeAddView(ALLRL,LL1);
        safeAddView(ALLRL2,ALLRL);
        safeAddView(ALLR,ALLRL2);




        createPopupWindow(ALLR);
//        ALLR.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//               int  mWidth = ALLR.getWidth();
//                int measuredHeight = ALLR.getMeasuredHeight();
//                int  mHeight = ALLR.getHeight();
//                Toast.makeText(activity," "+mWidth+" "+mHeight+" "+measuredHeight,Toast.LENGTH_LONG).show();
//            }
//        });

        return onPopWindowListener;
    }
    /**
     * 加载一个view
     */
    public OnPopWindowListener popViewNoButton() {
        if (activity.isFinishing()) {
            return null;
        }
        initParams();
        RelativeLayout ALLR = createBackgroudRL();
        RelativeLayout ALLRL2 = createBackgroudRL22();
        RelativeLayout ALLRL = createBackgroudRL2();
        final LinearLayout LL1 = createBackgroundLL3();

        if (!isNullOrEmpty(title)) {
            TextView textView1 = createTitle();
            safeAddView(LL1,textView1);

            View backgroundLine = createLine();
            safeAddView(LL1,backgroundLine);
        }


        if (contentView != null) {
            safeAddView(LL1,contentView);
            onPopWindowCloseViewListener = new OnPopWindowCloseViewListener() {
                @Override
                public void onClose() {
                    if (contentView == null) {
                        return;
                    }
                    LL1.removeView(contentView);
                }
            };
        }


        if (RLayoutId != null) {
            contentView = LayoutInflater.from(activity).inflate(RLayoutId, null);
            safeAddView(LL1,contentView);
        }


        LinearLayout LL2 = createButtonLL();


        safeAddView(LL1,LL2);
        safeAddView(ALLRL,LL1);
        safeAddView(ALLRL2,ALLRL);
        safeAddView(ALLR,ALLRL2);




        createPopupWindow(ALLR);
        return onPopWindowListener;
    }
    /**
     * 有2个按钮,1个输入框
     */
    public OnPopWindowListener popEditTextTwoButton() {
        if (activity.isFinishing()) {
            return null;
        }

        initParams();
        RelativeLayout ALLR = createBackgroudRL();
        RelativeLayout ALLRL2 = createBackgroudRL22();
        RelativeLayout ALLRL = createBackgroudRL2();
        LinearLayout LL1 = createBackgroundLL3();

        TextView textView1 = createTitle();
        safeAddView(LL1,textView1);


        View backgroundLine = createLine();
        safeAddView(LL1,backgroundLine);


        TextView textView2 = createContent();
        safeAddView(LL1,textView2);

        if (isFocusEdittextFlag) {
            LL1.setFocusable(true);
            LL1.setFocusableInTouchMode(true);
        }
        final EditText editText = createEdittext();
        safeAddView(LL1,editText);

        LinearLayout LL2 = createButtonLL();
        Button button2 = createCancelButton();
        Button button = createConfirmButton();

        if (isVertical) {
            safeAddView(LL2,button);
            safeAddView(LL2,button2);

        } else {
            safeAddView(LL2,button2);
            safeAddView(LL2,button);
        }

        safeAddView(LL1,LL2);
        safeAddView(ALLRL,LL1);
        safeAddView(ALLRL2,ALLRL);
        safeAddView(ALLR,ALLRL2);

        createPopupWindow(ALLR);

        if (isFocusEdittextFlag) {
            //如果是已经入某个界面就要立刻弹出输入键盘,可能会由于界面未加载完成而无法弹出,需要适当延迟,比如延迟500毫秒:
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                }
            }, 500);
        }
        return onPopWindowListener;
    }

    /**
     * 有2个按钮（确认和取消）
     */
    public OnPopWindowListener popTwoButton() {
        if (activity.isFinishing()) {
            return null;
        }
        initParams();
        RelativeLayout ALLR = createBackgroudRL();
        RelativeLayout ALLRL2 = createBackgroudRL22();
        RelativeLayout ALLRL = createBackgroudRL2();
        LinearLayout LL1 = createBackgroundLL3();

        TextView textView1 = createTitle();
        safeAddView(LL1,textView1);


        View backgroundLine = createLine();
        safeAddView(LL1,backgroundLine);

        TextView textView2 = createContent();
        safeAddView(LL1,textView2);

        LinearLayout LL2 = createButtonLL();

        Button button2 = createCancelButton();
        Button button = createConfirmButton();

        if (isVertical) {
            safeAddView(LL2,button);
            safeAddView(LL2,button2);

        } else {
            safeAddView(LL2,button2);
            safeAddView(LL2,button);
        }


        safeAddView(LL1,LL2);
        safeAddView(ALLRL,LL1);
        safeAddView(ALLRL2,ALLRL);
        safeAddView(ALLR,ALLRL2);


        createPopupWindow(ALLR);
        return onPopWindowListener;
    }

    private void safeAddView(ViewGroup father,View item){


        father.removeView(item);
        father.addView(item);
    }

    private void showPopupProgress() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (activity.getWindow().getDecorView().getWindowVisibility() == View.GONE) {
                    showPopupProgress();
                    return;
                }
                if (popupWindow != null) {
                    if (showLocationStatus != null) {
                        popupWindow.showAtLocation(activity.getWindow().getDecorView(), showLocationStatus, 0, 0);
                    } else {
                        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    }
                    popupWindow.update();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!activity.isFinishing()) {
                        if (activity.getWindow().getDecorView() != null) {
                            showPopupProgress();
                        } else {
                            if (activity != null) {
                                LogUtil.tToast(activity, "获取view为null");
                            }
                        }
                    }

                    break;
                case 1:
                    //退出
                    //if(!activity.isFinishing()){
                    if (isMustDoClick) {
                        if (popupWindow != null) {
                            popupWindow.handDismiss();
                            popupWindow = null;
                        }
                    } else {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                    if (onPopWindowCloseViewListener != null) {
                        onPopWindowCloseViewListener.onClose();
                    }
                    activity = null;
                    // contentView = null;
                    onDismissListener = null;
                    popupWindow = null;
                    confirmClickListener = null;
                    cancelClickListener = null;
                    //}
                    break;
            }
        }

    };


    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return
     */
    private boolean isNullOrEmpty(String string) {
        if (string == null || "".equals(string)) {
            return true;
        } else {
            return false;
        }
    }


    private PopWindow(Builder builder) {
        activity = builder.activity;
        confirmClickListener = builder.confirmClickListener;
        cancelClickListener = builder.cancelClickListener;
        title = builder.title;
        content = builder.content;
        confirmButtonText = builder.confirmButtonText;
        cancelButtonText = builder.cancelButtonText;
        isMustDoClick = builder.isMustDoClick;
        showLocationStatus = builder.showLocationStatus;
        isClickOverDismiss = builder.isClickOverDismiss;
        isVertical = builder.isVertical;
        hintEdittextContent = builder.hintEdittextContent;
        edittextContent = builder.edittextContent;
        inputModelByMySelf = builder.inputModelByMySelf;
        inputModelByModel = builder.inputModelByModel;
        onDismissListener = builder.onDismissListener;
        showEdittextBottomLine = builder.showEdittextBottomLine;
        setEdittextBottomLineColor = builder.setEdittextBottomLineColor;
        setEdittextBottomLineWidth = builder.setEdittextBottomLineWidth;
        setEdittextDisableClear = builder.setEdittextDisableClear;
        isFocusEdittextFlag = builder.isFocusEdittextFlag;
        contentView = builder.contentView;
        RLayoutId = builder.RLayoutId;
    }


    public static final class Builder {
        private Activity activity;
        private BackgroundDarkPopupWindow popupWindow;
        private View.OnClickListener confirmClickListener;
        private View.OnClickListener cancelClickListener;
        private String title;
        private String content;
        private String confirmButtonText;
        private String cancelButtonText;
        private boolean isMustDoClick;
        private Integer showLocationStatus;
        private boolean isClickOverDismiss = true;
        private boolean isVertical;
        private String hintEdittextContent;
        private String edittextContent;
        private Integer inputModelByMySelf;
        private Integer inputModelByModel;
        private BackgroundDarkPopupWindow.OnDismissListener onDismissListener;
        private boolean showEdittextBottomLine;
        private Integer setEdittextBottomLineColor;
        private Float setEdittextBottomLineWidth;
        private Boolean setEdittextDisableClear;
        private Boolean isFocusEdittextFlag = false;
        private View contentView;
        private Integer RLayoutId;

        public Builder() {
        }

        public Builder activity(Activity val) {
            activity = val;
            return this;
        }

        public Builder popupWindow(BackgroundDarkPopupWindow val) {
            popupWindow = val;
            return this;
        }

        public Builder confirmClickListener(View.OnClickListener val) {
            confirmClickListener = val;
            return this;
        }

        public Builder cancelClickListener(View.OnClickListener val) {
            cancelClickListener = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder confirmButtonText(String val) {
            confirmButtonText = val;
            return this;
        }

        public Builder cancelButtonText(String val) {
            cancelButtonText = val;
            return this;
        }

        public Builder isMustDoClick(boolean val) {
            isMustDoClick = val;
            return this;
        }


        public Builder showLocationStatus(Integer val) {
            showLocationStatus = val;
            return this;
        }


        public Builder isClickOverDismiss(boolean val) {
            isClickOverDismiss = val;
            return this;
        }

        public Builder isVertical(boolean val) {
            isVertical = val;
            return this;
        }

        public Builder hintEdittextContent(String val) {
            hintEdittextContent = val;
            return this;
        }

        public Builder edittextContent(String val) {
            edittextContent = val;
            return this;
        }

        public Builder inputModelByMySelf(Integer val) {
            inputModelByMySelf = val;
            return this;
        }

        public Builder inputModelByModel(Integer val) {
            inputModelByModel = val;
            return this;
        }

        public Builder onDismissListener(BackgroundDarkPopupWindow.OnDismissListener val) {
            onDismissListener = val;
            return this;
        }

        public Builder showEdittextBottomLine(boolean val) {
            showEdittextBottomLine = val;
            return this;
        }

        public Builder setEdittextBottomLineColor(Integer val) {
            setEdittextBottomLineColor = val;
            return this;
        }

        public Builder setEdittextBottomLineWidth(Float val) {
            setEdittextBottomLineWidth = val;
            return this;
        }

        public Builder setEdittextDisableClear(Boolean val) {
            setEdittextDisableClear = val;
            return this;
        }

        public Builder setIsFocusEdittextFlag(Boolean val) {
            isFocusEdittextFlag = val;
            return this;
        }

        public Builder setContentView(View val) {
            contentView = val;
            return this;
        }

        public Builder setRLayoutId(Integer val) {
            RLayoutId = val;
            return this;
        }


        public PopWindow build() {
            return new PopWindow(this);
        }
    }
}
