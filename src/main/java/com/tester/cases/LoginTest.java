package com.tester.cases;

import com.alibaba.fastjson.JSONObject;
import com.tester.model.InterfaceName;
import com.tester.config.TestConfig;
import com.tester.model.LoginCase;
import com.tester.utils.ConfigFile;
import com.tester.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginTest {


    @BeforeTest(groups = "loginTrue",description = "测试准备工作,获取HttpClient对象")
    public void beforeTest(){
        TestConfig.defaultHttpClient = new DefaultHttpClient();
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSERINFO);
    }




    @Test(groups = "loginTrue",description = "用户成功登陆接口",dataProvider = "loginData")
    public void loginTrue(LoginCase loginCase) throws IOException {

        //SqlSession session = DatabaseUtil.getSqlSession();
        //LoginCase loginCase = session.selectOne("loginCase",1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //下边的代码为写完接口的测试代码
        String result = getResult(loginCase);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(),result);


    }

    @DataProvider(name = "loginData")
    public Iterator<Object[]> LogindataProvider() throws IOException {
        List<Object[]> result = new ArrayList<Object[]>();
        SqlSession session = DatabaseUtil.getSqlSession();
        List<Object> alldata = session.selectList("loginCase");
        System.out.print("********"+ alldata.size());
        Iterator id = alldata.iterator();
        while(id.hasNext()){
            result.add(new Object[]{id.next()});
        }
        return  result.iterator();
    }

   /* @Test(description = "用户登陆失败接口")
    public void loginFalse() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase",2);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);



        //下边的代码为写完接口的测试代码
        String result = getResult(loginCase);
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(),result);



    }*/





    private String getResult(LoginCase loginCase) throws IOException {
        //下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = JSONObject.parseObject(loginCase.getParams());
        JSONObject parm = JSONObject.parseObject(loginCase.getParm());
       // param.put("userName",parm.get("userName"));
        param.replace("userName",parm.get("userName").toString());
        //param.replace("userName",param.get("userName").toString(),parm.get("userName"));
        //param.put("userName",loginCase.getUserName());
        //param.put("password",loginCase.getPassword());

        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        System.out.print("------->" + param.toString());
        //param.toString()
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        String code = response.getStatusLine().toString();
        System.out.print("*****************" + code);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        return result = "true";
    }


}
