package com.android.answermonitor.database;

import android.content.Context;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    public static void insert(Context context,AnswerBean bean) throws SQLException {
        DatabaseHelper helper = DatabaseHelper.getHelper(context);
        helper.getAnswerDao().create(bean);
    }

    public static String findByKey(Context context,String key) throws SQLException{
        String sql = "select num,question,answer from tb_answer where question like '%" + key + "%'";
        DatabaseHelper helper = DatabaseHelper.getHelper(context);
        GenericRawResults<AnswerBean> results = helper.getAnswerDao().queryRaw(sql, new RawRowMapper<AnswerBean>() {
            @Override
            public AnswerBean mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                return new AnswerBean(Integer.parseInt(resultColumns[0]),resultColumns[1],resultColumns[2]);
            }
        });
        for(AnswerBean bean : results){
            return bean.getQuestion() + "\n" + bean.getAnswer();
        }
        return "";
    }
    public static String find(Context context,String ...strs) throws SQLException {
        String descStr = "";
        if(strs.length > 1){
            for(int i=0;i<strs.length-1;i++){
                descStr += "(case when question like '%" + strs[i] + "%' then 1 else 0 end) +\n";
            }
        }
        descStr += "(case when question like '%" + strs[strs.length-1] + "%' then 1 else 0 end)";

        String sql = "select num,question,answer from tb_answer order by " + descStr + "desc limit 1";
        DatabaseHelper helper = DatabaseHelper.getHelper(context);
        GenericRawResults<AnswerBean> results = helper.getAnswerDao().queryRaw(sql, new RawRowMapper<AnswerBean>() {
            @Override
            public AnswerBean mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                return new AnswerBean(Integer.parseInt(resultColumns[0]),resultColumns[1],resultColumns[2]);
            }
        });
        for(AnswerBean bean : results){
            return bean.getQuestion() + "\n" + bean.getAnswer();
        }
        return "";
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @return
     */
    public static String[] split(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        List<String> strList = getStrList(inputString, length, size);
        String[] strs = new String[strList.size()];
        return strList.toArray(strs);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @param size
     *            指定列表大小
     * @return
     */
    private static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str
     *            原始字符串
     * @param f
     *            开始位置
     * @param t
     *            结束位置
     * @return
     */
    private static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
