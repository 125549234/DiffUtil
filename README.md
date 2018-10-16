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
#####由于有关界面的部分都使用的屏幕适配的关系，需要在打开的第一个activity或者application中初始化一下组件
ViewPlugBaseLayout.initFont(this);


各部分功能介绍(先介绍对话框，准备当爸，暂时没太多时间)

###优化的popupwindow
这一部分主要的优化在于，那些什么杂七杂八的什么activity被finish后又不知道被什么地方调用了，或者是说在activity在没有完成前被调用报错，又或者是第一次打开了后跳转到其他界面后又回来调用popupwindow报activity错之类的问题。
采用链式，已检查没有内存泄露，并且提供多种调用场景。
这里没有layout，采用纯代码生成界面

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





