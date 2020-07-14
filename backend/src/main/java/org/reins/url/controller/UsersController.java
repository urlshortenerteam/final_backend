package org.reins.url.controller;

import net.sf.json.JSONObject;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.reins.url.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsersController {
    @Autowired
    UsersService usersService;

    @CrossOrigin
    @RequestMapping("/register")
    public Map<String, Map<String, Boolean>> register(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String password = params.get("password");
        String email = params.get("email");
        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("success", usersService.register(name, password, email));
        obj.put("data", data);
        return obj;
    }

    @CrossOrigin
    @RequestMapping("/loginReq")
    public Object login(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String password = params.get("password");
        Users user = usersService.checkUser(name, password);
        JSONObject obj = new JSONObject();
        if (user == null) {
            obj.put("loginStatus", false);
            obj.put("type", 1);
            obj.put("id", -1);
        } else {
            obj.put("id", user.getId());
            obj.put("type", user.getRole());
            if (user.getRole() == 2) obj.put("loginStatus", false);
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
        if (auth == null) data.put("status", false);
        else data.put("status", true);
        obj.put("data", data);
        return obj;
    }
}
