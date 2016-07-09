package com.qzl.shoujiweishi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import com.qzl.shoujiweishi.bean.BlackNumInfo;
import com.qzl.shoujiweishi.db.BlackNumOpenHlper;
import com.qzl.shoujiweishi.db.dao.AddressDao;
import com.qzl.shoujiweishi.db.dao.BlackNumDao;
import com.qzl.shoujiweishi.engine.ContactEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private BlackNumDao blackNumDao;

    public ApplicationTest() {
        super(Application.class);
    }
    //测试获取联系人
    public void testContacts(){
        List<HashMap<String,String>> list = ContactEngine.getAllContactInfo(getContext());
        for (HashMap<String, String> hashMap: list){
            System.out.println("姓名："+hashMap.get("name")+"    电话"+hashMap.get("phone"));
        }
    }
    //测试数据库操作
    public void testAddtess(){
        String queryAddress = AddressDao.queryAddress("18888888888", getContext());
        if(!TextUtils.isEmpty(queryAddress)){
            System.out.println("号码归属地："+queryAddress);
        }
    }

    /**
     * 在测试方法之前执行的方法
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        blackNumDao = new BlackNumDao(getContext());
        super.setUp();
    }

    /**
     * 在测试方法之后执行的方法
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //测试创建数据库
    public void testBlackNumOpenHelper(){
        BlackNumOpenHlper blackNumOpenHlper = new BlackNumOpenHlper(getContext());//不能创建数据库
        blackNumOpenHlper.getReadableDatabase();//创建数据库
    }
    //测试BlackNumOpenHlper添加操作
    public void testAddBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        Random random = new Random();
        //random.nextInt(3);//0-2 [0-3)
        for (int i = 0; i < 200; i++) {
            blackNumDao.addBlackNum("12345678"+i,random.nextInt(3));
        }
    }
    //测试BlackNumOpenHlper更新黑名单操作
    public void testUpdateBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.updateBlackNum("110",BlackNumDao.CALL);
    }
    //测试BlackNumOpenHlper查询黑名单拦截模式的操作
    public void testQueryBlackNumMode(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        int mode = blackNumDao.queryBlackNumMode("110");
       //断言 参数1 期望的值，参数2：实际的值，获取的值
        assertEquals(0,mode);
    }
    //测试BlackNumOpenHlper删除数据
    public void testDeleteBlackNumMode(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.deleteBlackNum("110");
    }
    //查询所有黑名单测试
    public void testQueryAllBlackNum(){
        List<BlackNumInfo> queryAllBlackNum = blackNumDao.queryAllBlackNum();
        for (BlackNumInfo blackNumInfo:queryAllBlackNum) {
            System.out.println(blackNumInfo.toString());
        }
    }
}