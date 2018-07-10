package com.ZJin.controller;

import com.ZJin.hbase.Content;
import com.ZJin.hbase.GetDataFromHbase;
import com.ZJin.hbase.HbaseFactory;
import com.ZJin.mysql.MysqlUitls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MyController {
    @RequestMapping("/showPage")
    public String showPage(HttpServletRequest request,Model model, @RequestParam("username")String username, @RequestParam("password")String password) throws Exception{
        boolean login = MysqlUitls.login(username, password);
        if(login){
            String uid = MysqlUitls.getIdByUsername(username,password);
            List<Content> contents = HbaseFactory.getContent(uid);
//            List<Content> contentById = GetDataFromHbase.getContentById(uid);
            model.addAttribute("username",username);
            model.addAttribute("password",password);
            model.addAttribute("contents",contents);
            model.addAttribute("uid",uid);
            HttpSession session = request.getSession();
            session.setAttribute("uid",uid);

            return "showPage";
        }else {
            return "login";
        }
    }

    @RequestMapping("/region")
    public String region(Model model, @RequestParam("id")int id,@RequestParam("username")String username, @RequestParam("password")String password){
            MysqlUitls.region(id, username, password);
            return "index";

    }

    @RequestMapping("/attends")
    public String myAttends(Model model, @RequestParam("uid")String uid) throws Exception{
        List<String> atts = GetDataFromHbase.getAtt(uid);
        model.addAttribute("list",atts);
        model.addAttribute("uid",uid);
        return "attends";
    }
    @RequestMapping("/fans")
    public String myFans(Model model, @RequestParam("uid")String uid) throws Exception{
        List<String> fans = GetDataFromHbase.getfans(uid);
        model.addAttribute("fans",fans);
        return "fans";
    }
    @RequestMapping("/user")
    public String user(Model model, @RequestParam("uid")String uid) throws Exception{
        List<Content> contents = GetDataFromHbase.getContentById(uid);
        model.addAttribute("uid",uid);
        model.addAttribute("contents",contents);
        return "user";
    }
    @RequestMapping("/send")
    public String send(Model model,@RequestParam("uid")String uid, @RequestParam("msg")String msg,@RequestParam("username")String username, @RequestParam("password")String password) throws Exception{
        HbaseFactory.sendWeibo(uid,msg);
        model.addAttribute("username",username);
        model.addAttribute("password",password);
        return "showPage";
    }
    @RequestMapping("/notattent")
    public String notattent(@RequestParam("uid")String uid, @RequestParam("att_id")String att_id) throws Exception{
        HbaseFactory.deleteAttend(uid,att_id);
        return "attends";
    }
    @RequestMapping("/userTest")
    public String userTest(HttpServletRequest request,@RequestParam("uid")String uid,@RequestParam("msg")String msg) throws Exception{
//        HttpSession session = request.getSession();
//        String uid = (String)session.getAttribute("uid");
        System.out.println(uid);
        System.out.println(String.valueOf(msg));
        return "index";
    }
}
