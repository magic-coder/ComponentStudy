package com.richfit.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.StringBuilderPrinter;

import java.util.List;

/**
 * 从本次缓存的构架来说，可以让BaseDao实现IRepository接口，
 * 但是这样不同业务的Dao层相应的方法将多余很多。权衡利弊这里
 * 不实现IRepository接口。
 * Created by monday on 2016/11/8.
 */

public class BaseDao {

    protected final static String PAuthOrgKey = "P_AUTH_ORG";
    protected final static String PAuthOrg2Key = "P_AUTH_ORG2";

    private BCSSQLiteHelper mSQLiteHelper;
    protected StringBuffer sb;


    public BaseDao(Context context) {
        this.mSQLiteHelper = BCSSQLiteHelper.getInstance(context);
        this.sb = new StringBuffer();
    }

    protected SQLiteDatabase getReadableDB() {
        return mSQLiteHelper.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDB() {
        return mSQLiteHelper.getWritableDatabase();
    }

    /**
     * 情况StringBuffer的字符串
     */
    protected void clearStringBuffer() {
        if (sb != null) {
            sb.setLength(0);
        }
    }

    protected String addSqlField(List<String> fieldList) {
        StringBuffer sqlField = new StringBuffer();
        int size = fieldList.size();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                sqlField.append(fieldList.get(0));
            } else {
                sqlField.append(fieldList.get(0)).append(",");
            }
        }
        return sqlField.toString();
    }

    protected String createPlaceHodler(List<String> fieldList) {
        StringBuffer sqlPlaceHolder = new StringBuffer();
        int size = fieldList.size();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                sqlPlaceHolder.append("?");
            } else {
                sqlPlaceHolder.append("?").append(",");
            }
        }
        return sqlPlaceHolder.toString();
    }

    protected String createPlaceHolder(String str) {
        StringBuffer sqlPlaceHolder = new StringBuffer();
        int size = str.split(",").length;
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                sqlPlaceHolder.append("?");
            } else {
                sqlPlaceHolder.append("?").append(",");
            }
        }
        return sqlPlaceHolder.toString();
    }

}