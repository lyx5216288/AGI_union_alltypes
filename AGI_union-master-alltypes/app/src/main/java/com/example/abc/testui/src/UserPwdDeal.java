package com.example.abc.testui.src;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserPwdDeal {

    public static List<UsrPwd> usrlist = new ArrayList<>();

    public static boolean isinit = false;

    // 3 用户名密码错误  弹提示框       1 启动管理员界面      2 启动一般操作员界面   4 用户不存在 弹提示框
    public static int Judge(String name, String pwd){

        if(isinit==false){
            return 4;
        }

        for (UsrPwd a : usrlist){
            if(a.name.equals(name)&& a.pwd.equals(pwd)){

                if(a.competence.equals("manager")){
                    return 1;
                }
                else if(a.competence.equals("operator")){
                    return 2;
                }

            }

        }
        return 3;
    }


    // 0 成功
    public static int Init(){

        try {

            //这里替换成实际的文件读写路径   sd卡/AndroidData/Phonestoreddata/data/Account.xml
            //当文件不存在 直接 return -2 不要自动去创建文件。
            final String filepath = Environment.getExternalStorageDirectory() + File.separator
                                    + "AndroidData" + File.separator
                                    + "Phonestoreddata" + File.separator
                                    + "data";
            final String filefullname = filepath + File.separator + "Account.xml";
            File dir = new File(filepath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(filefullname);
            if (!file.exists()){
                return -2;
            }

            byte[] bytes = Com.ReadFromFile(filefullname);



            byte[] newbytes = new byte[bytes.length];

            for(int i=0; i<bytes.length; i++){
                newbytes[i] = (byte)(bytes[i] ^ '$');
            }

            String res = new String(newbytes,"UTF-8");


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream stream = new ByteArrayInputStream(res.getBytes("UTF-8"));

            Document doc = builder.parse(stream);

            NodeList nl = doc.getElementsByTagName("item");

            for(int i=0; i<nl.getLength(); i++){
                String name = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
                String pwd = doc.getElementsByTagName("password").item(i).getFirstChild().getNodeValue();
                String competence = doc.getElementsByTagName("competence").item(i).getFirstChild().getNodeValue();

//                System.out.println("用户名:"+ doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue());
//                System.out.println("密码:"+ doc.getElementsByTagName("password").item(i).getFirstChild().getNodeValue());
//                System.out.println("权限:"+ doc.getElementsByTagName("competence").item(i).getFirstChild().getNodeValue());
                usrlist.add(new UsrPwd(name, pwd, competence));
            }
            isinit = true;
            return 0;
        }
        catch (Exception e){
            return -1;
        }

    }



}
