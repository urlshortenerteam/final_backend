package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.Users;
import org.reins.url.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @CrossOrigin
    @RequestMapping("/getShort")
    public Map<String, Boolean> generateShort(@RequestBody  Map<String,String> params) {
        String name=params.get("name");
        String password=params.get("password");
        String email=params.get("email");
        Map<String,Boolean> res=new HashMap<>();
        res.put("data",userService.register(name,password));
        return res;
    }
    @CrossOrigin
    @RequestMapping("/loginReq")
    public Object login(@RequestBody Map<String, String> params){
        String name=params.get("name");
        String password=params.get("password");
        Users user=userService.checkUser(name,password);
        JSONObject obj=new JSONObject();
        if (user==null){
            obj.put("loginStatus",false);
            obj.put("type",1);
            obj.put("id",-1);
        }
        else {
            obj.put("id",user.getId());
            obj.put("type",user.getRole());
            if (user.getRole()==2)
                obj.put("loginStatus",false);
            else obj.put("loginStatus",true);
        }
        return obj;
    }

}
