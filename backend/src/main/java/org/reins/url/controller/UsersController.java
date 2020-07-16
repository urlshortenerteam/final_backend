package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.reins.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin
    @RequestMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> params) {
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
    public JSONObject login(@RequestBody Map<String, String> params) {
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
                obj.put("token", jwtUtil.sign(user.getId(), user.getName()));
            }
        }
        JSONObject res = new JSONObject();
        res.put("data", obj);
        return res;
    }

    @CrossOrigin
    @RequestMapping("/checkSession")
    public void checkSession() {
    }

    @CrossOrigin
    @RequestMapping("/banUser")
    public JSONObject banUser(@RequestParam("id") long id, @RequestParam("ban_id") long ban_id, @RequestParam("ban") boolean ban) {
        Users admin = usersService.findById(id);
        Users banUser = usersService.findById(ban_id);
        JSONObject res = new JSONObject();
        JSONObject status = new JSONObject();
        if (admin == null || banUser == null || admin.getRole() != 0 || banUser.getRole() == 0) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        usersService.changeRole(ban_id, ban ? 2 : 1);
        status.put("status", true);
        res.put("data", status);
        return res;
    }
}
