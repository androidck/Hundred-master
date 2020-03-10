package com.community.hundred.modules.manager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.base.MyApplication;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.greendao.UserEntryDao;
import com.community.hundred.common.util.HawkUtil;
import com.community.hundred.modules.manager.entry.UserEntry;

import java.util.List;

public class LoginUtils {

    public static LoginUtils instance;

    private MyActivity myActivity;

    public UserEntryDao userEntryDao = MyApplication.getInstances().getDaoSession().getUserEntryDao();

    public static LoginUtils getInstance() {
        if (instance == null) {
            synchronized (LoginUtils.class) {
                if (instance == null) {
                    instance = new LoginUtils();
                }
            }
        }
        return instance;
    }

    // 存储数据
    public void saveUserInfo(UserEntry entry) {
        List<UserEntry> list = userEntryDao.loadAll();
        if (list.size() == 0) {
            userEntryDao.insert(entry);
        } else {
            entry.setId(list.get(0).getId());
            userEntryDao.update(entry);
        }
    }

    // 查询uid
    public String getUid() {
        List<UserEntry> list = userEntryDao.loadAll();
        if (list.size() != 0) {
            return list.get(0).getUserId();
        } else {
            return null;
        }

    }

    // 是否登录
    public boolean isLogin() {
        List<UserEntry> list = userEntryDao.loadAll();
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    // 删除数据
    public void removeAll() {
        userEntryDao.deleteAll();
    }


}
