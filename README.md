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
	        implementation 'com.github.125549234:DiffUtil:1.0'
	}

初始化
------
##### 由于有关界面的部分都使用的屏幕适配的关系，需要在打开的第一个activity或者application中初始化一下组件
ViewPlugBaseLayout.initFont(this);


各部分功能介绍(先介绍popupwindow，准备当爸，又正在给公司一个很古老的项目更新换代，工作量有点大暂时没太多时间)

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
	
开饭。。。





