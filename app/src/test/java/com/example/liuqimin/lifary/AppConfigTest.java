package com.example.liuqimin.lifary; /**
 * Created by liuqi on 2015-10-11.
 */

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AppConfigTest {
    @Test
    public void main() {
        System.out.println("Hello, World!");
        AppTest();
    }

    public void AppTest() {

        String formhash = "";
        //assertTrue("wrong?", AppConfig.dude(40));
        System.out.println("errrr");

        HttpClient httpClient = new DefaultHttpClient();
        //得到网页的formhash值，用Jsoup解析出来
        //HttpGet httpGet = new HttpGet("http://www.hi-pda.com/forum/logging.php?action=login");
        HttpGet httpGet = new HttpGet("http://bbs.saraba1st.com/2b");
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println("formhash");
            //HttpEntity httpEntity = httpResponse.getEntity();j
            //String s = EntityUtils.toString(httpEntity, "GBK");

            //Element formhash_Element = (Element) Jsoup.parse(s).select("input[name=formhash]").first();
            //formhash = formhash_Element.attr("value");
            System.out.println("formhash");
            System.out.println(formhash);
        }
        catch(Exception e ){
            System.out.println(e.toString());
        }
        //下面我们就可以登陆了，用HttpPost：
        //http://bbs.saraba1st.com/2b/member.php?mod=logging&amp;action=login&amp;loginsubmit=yes&amp;infloat=yes&amp;lssubmit=yes
        HttpPost httpPost=new HttpPost("http://bbs.saraba1st.com/2b/member.php?mod=logging&amp;action=login&amp;loginsubmit=yes&amp;infloat=yes&amp;lssubmit=yes");
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("formhash",formhash));
        params.add(new BasicNameValuePair("loginfield","username"));
        params.add(new BasicNameValuePair("password","love1115"));
        params.add(new BasicNameValuePair("questionid","0"));
        params.add(new BasicNameValuePair("referer","http://www.hi-pda.com/forum/index.php"));
        params.add(new BasicNameValuePair("username","关空空"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));

            HttpResponse response=httpClient.execute(httpPost);

            System.out.println(response.toString());

            //HttpEntity entity=response.getEntity();
            //String ans=EntityUtils.toString(entity);


        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

}