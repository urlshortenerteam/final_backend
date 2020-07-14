package org.reins.url.controller;

import net.sf.json.JSONObject;
import org.reins.url.entity.Users;
import org.reins.url.service.UserService;
import org.reins.url.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin
    @RequestMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String password = params.get("password");
        //String email = params.get("email");
        JSONObject data = new JSONObject();
        data.put("success", userService.register(name, password));
        JSONObject obj = new JSONObject();
        obj.put("data", data);
        return obj;
    }

    @CrossOrigin
    @RequestMapping("/loginReq")
    public Object login(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String password = params.get("password");
        Users user = userService.checkUser(name, password);
        JSONObject obj = new JSONObject();
        if (user == null) {
            obj.put("loginStatus", false);
            obj.put("type", 1);
            obj.put("id", -1);
        } else {
            obj.put("id", user.getId());
            obj.put("type", user.getRole());
            if (user.getRole() == 2)
                obj.put("loginStatus", false);
            else {
                obj.put("loginStatus", true);
                SessionUtil.setSession(obj);
            }
        }
        return obj;
    }

    @RequestMapping("/checkSession")
    public JSONObject checkSession() {
        JSONObject auth = SessionUtil.getAuth();
        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("status", auth != null);
        obj.put("data", data);
        return obj;
    }
}
