# DiffUtil
工具包，现在功能包括（屏幕适配，带删除的输入框，log和toast优化，优化的popupwindow，分页，优化的对话框）

导入方式
------
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.125549234:DiffUtil:1.1'
	}

初始化
------
##### 由于有关界面的部分都使用的屏幕适配的关系，需要在打开的第一个activity或者application中初始化一下组件
ViewPlugBaseLayout.initFont(this);


各部分功能介绍(先介绍popupwindow，正在给公司一个很古老的项目更新换代，工作量有点大暂时没太多时间)

### 优化的popupwindow
这一部分主要的优化在于，那些什么杂七杂八的什么activity被finish后又不知道被什么地方调用了，或者是说在activity在没有完成前被调用报错，又或者是第一次打开了后跳转到其他界面后又回来调用popupwindow报activity错之类的问题。  
采用链式，已检查没有内存泄露，并且提供多种调用场景。  
\n这里没有layout，采用纯代码生成界面  


	 new PopWindow.Builder().activity(this)
			.title("标题啦")
			.content("好多内存")
			.confirmClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
				LogUtil.tToast(getApplicationContext(),"点击确认");

			    }
			})
			.build().popOneButton();

![666666](https://github.com/125549234/DiffUtil/blob/master/images/webwxgetmsgimg.jpg)  
这个算是最简单的一个弹出  

这个是有确认和取消的，当然所有的文字都可以编辑，不设置值的话会采用默认值

	 new PopWindow.Builder().activity(this)
                .title("标题啦")
                .content("好多内存")
                .confirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.tToast(getApplicationContext(),"点击确认");

                    }
                })
                .cancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.tToast(getApplicationContext(),"点击退出");
                    }
                })
                .build().popTwoButton();
		
		
还可以设置按钮的方向，.isVertical(true)  
![666666](https://github.com/125549234/DiffUtil/blob/master/images/webwxgetmsgimg2.jpg)  
![666666](https://github.com/125549234/DiffUtil/blob/master/images/webwxgetmsgimg3.jpg) 
![666666](https://github.com/125549234/DiffUtil/blob/master/images/SVID_20181016_111133_1.gif) 

还有更加特别的设置  
当你是想做一个登录超时，想弹个框给用户提示，并且让客户只能通过popupwindow的确认键来跳转，并不想用户点击阴影或者是返回键直接关闭popupwindow继续使用时，来尝试一下：

	 new PopWindow.Builder().activity(this)
                .title("标题啦")
                .content("登录超时")
                .confirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.tToast(getApplicationContext(),"点击确认");

                    }
                })
                .isMustDoClick(true)
                .build().popOneButton();

![666666](https://github.com/125549234/DiffUtil/blob/master/images/SVID_20181016_113744_1.gif) 

还有些特殊需求就是，我点击了确认键，但我还不想关闭popupwindow，想调用个验证接口之类的，来尝试一下：
首先：

	 private PopWindow.OnPopWindowListener onPopWindowListener;
	 
然后，由于链式的结果都是返回这个回调接口，这个回调接口现在有的功能是

	public interface OnPopWindowListener {
        //让窗口关闭
        void onClose();

        //获取输入框的内容
        String getEdittextContent();
    }
    
 当然，现在介绍的是控制popupwindow
 
 	
	onPopWindowListener = new PopWindow.Builder().activity(this)
                .title("标题啦")
                .content("登录超时")
                .confirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.tToast(getApplicationContext(),"点击确认");
                        Message message = new Message();
                        handler.sendMessageDelayed(message,1000);

                    }
                })
                .onDismissListener(new BackgroundDarkPopupWindow.OnDismissListener() {
                    @Override
                    public void Ondismiss() {
                        LogUtil.tToast(getApplicationContext(),"被关闭了");
                    }
                })
                .isClickOverDismiss(false)
                .isMustDoClick(true)
                .build().popOneButton();
		
那么模拟调用

	
	 @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                    if(onPopWindowListener!=null){
                        onPopWindowListener.onClose();
                    }
        }
    };
	

![666666](https://github.com/125549234/DiffUtil/blob/master/images/SVID_20181016_115124_1.gif) 

下一个popupwindow类型是带输入框的，针对与带输入的，可以自定义设置键盘类型，也提供了2种密码类型（纯数字和英文+数字）

    
            onPopWindowListener = new PopWindow.Builder().activity(this)
                    .title("标题啦")
                    .content("好多内存")
                    .confirmClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtil.tToast(getApplicationContext(), "确认:"+onPopWindowListener.getEdittextContent());
    
                        }
                    })
                    .isClickOverDismiss(false)
                    .isMustDoClick(false)
                    .onDismissListener(new BackgroundDarkPopupWindow.OnDismissListener() {
                        @Override
                        public void Ondismiss() {
                            LogUtil.tToast(getApplicationContext(),"关闭了");
                        }
                    })
                    .cancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtil.tToast(getApplicationContext(),"取消");
                        }
                    })
                    //.isVertical(true)
    
                    //.inputModelByModel(2)
                    .hintEdittextContent("请输入密码")
                    .setEdittextDisableClear(false)
                    .setEdittextBottomLineColor(Color.parseColor("#410000"))
                    .setEdittextBottomLineWidth(30f)
                    .showEdittextBottomLine(true)
                    .build().popEditTextTwoButton();
                    
 
 输入框的设置直接用默认的就可以了，其实不用怎么设置
 ![666666](https://github.com/125549234/DiffUtil/blob/master/images/webwxgetmsgimg4.jpg) 
 
 具体有什么参数可以设置和设置什么参数
 
    
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
        
        
 不设置则使用默认配置
 
 
 ### 优化的log和toast
 其实对于log的优化就直接控制和统一输出而已，发布自动直接屏蔽log输出，toast优化则解决了toast最头疼的事情，有些机型不显示，各种机型各种操蛋  
 在这里直接使用了国外的一个github的项目Toasty，还有处理了toast报错。
    
    Unable to add window -- token android.os.BinderProxy@ea87a1b is not valid; is your activity running?
    
 反正我现在用起来还没出什么毛病。
 
 
 ### 分页（随便加塞，网上一堆）
 
 ### 屏幕适配（前期产物，但一直用到现在，因为屏幕适配变得更容易控制了）
 
    
         ViewPlugBaseLayout.setSizeUtil(view, R.id.user_layout);
         
 这个是主要使用的方法  
 首先要初始化（popupwindow也一样，发现没有popupwindow只要阴影就是没有初始化导致的）  
 view就是你将要layout生成的View，后面的R.id就是id
    
    
     <RelativeLayout
                android:id="@+id/user_layout"
                android:layout_width="fill_parent"
                android:layout_height="40dp"
                android:layout_marginLeft="20dp"
                android:layout_marginRight="20dp"
                android:background="@drawable/edit_parent" >
    
 这样，就会把这个RelativeLayout自动根据里面设置的长宽里外边距等适配屏幕。（等比例的，这个比例值是根据获取的机型屏幕像素来决定，现在不提供修改）
 








	
	
	
	
	
开饭。。。





