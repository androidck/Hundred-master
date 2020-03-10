package com.community.hundred.modules.manager;

import com.community.hundred.common.base.MyApplication;
import com.community.hundred.common.greendao.BookEntryDao;
import com.community.hundred.modules.manager.entry.BookEntry;

import java.util.List;

// 书本数据管理
public class BookUtils {

    public static BookUtils instance;

    public BookEntryDao entryDao = MyApplication.getInstances().getDaoSession().getBookEntryDao();

    public static BookUtils getInstance() {
        if (instance == null) {
            synchronized (BookUtils.class) {
                if (instance == null) {
                    instance = new BookUtils();
                }
            }
        }
        return instance;
    }

    // 保存数据
    public void saveBook(BookEntry bookEntry) {
        entryDao.insert(bookEntry);
    }

    // 查询条数据
    public BookEntry getBook(String name) {
        BookEntry entry = entryDao.queryBuilder().where(BookEntryDao.Properties.Name.eq(name)).unique();
        if (entry == null) {
            return null;
        } else {
            return entry;
        }
    }
}
