package com.nathan.diff.util.plug;

/**用于更改导航栏的回调接口
 * Created by nathan on 2017/11/21.
 */

public interface OnNavigationModifyLister {

    /**
     * 更改中间标题
     * @param content
     */
    void modifyTitle(String content);

    /**
     * 更改右边标题
     * @param content
     */
    void modifyRightTitle(String content);
}
