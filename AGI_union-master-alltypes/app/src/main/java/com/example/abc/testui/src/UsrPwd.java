package com.example.abc.testui.src;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class UsrPwd {


    public String name;
    public String pwd;
    public String competence;

    public UsrPwd(String name, String pwd, String competence) {
        this.name = name;
        this.pwd = pwd;
        this.competence = competence;
    }
}
