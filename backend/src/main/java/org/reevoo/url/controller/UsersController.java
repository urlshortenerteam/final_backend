package org.reevoo.url.controller;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.reevoo.url.service.UsersService;
import org.reevoo.url.entity.Users;
import org.reevoo.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    /**
     * handle the request "/register" and return the result of registration.
     * It checks whether the name has existed.If yes, the registration fails.
     *
     * @param params a map that contains the name, password and email
     *               {
     *               name:String,
     *               password:String,
     *               email:String
     *               }
     * @return {data:{
     * success:Boolean
     * }
     * }
     */
    @CrossOrigin
    @RequestMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> params) throws ExecutionException, InterruptedException {
        String name = params.get("name");
        String password = params.get("password");
        String email = params.get("email");
        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("success", usersService.register(name, password, email).get());
        obj.put("data", data);
        return obj;
    }

    /**
     * handle the request "/loginReq" ,check the user's information and return the result of login.
     * If login is successful, it will sign a JWT which works for ten minutes and send it to the user.
     *
     * @param params a map that contains the name and password {name:USERNAMEEXAMPLE,password:PASSWORDEXAMPLE}
     * @return {data:{
     * loginStatus:Boolean,
     * type:Integer,
     * id:Long,
     * token:String
     * }
     * }
     */
    @CrossOrigin
    @RequestMapping("/loginReq")
    public JSONObject login(@RequestBody Map<String, String> params) throws ExecutionException, InterruptedException {
        String name = params.get("name");
        String password = params.get("password");
        Users user = usersService.checkUser(name, password).get();
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
                obj.put("token", JwtUtil.sign(user.getId(), user.getName(), user.getRole(), false));
                obj.put("refreshToken", JwtUtil.sign(user.getId(), user.getName(), user.getRole(), true));
            }
        }
        JSONObject res = new JSONObject();
        res.put("data", obj);
        return res;
    }

    /**
     * An API used for checking the user's authorization.
     * The check is completed by the interceptor.
     * It has no params and returns nothing.
     */
    @CrossOrigin
    @RequestMapping("/checkSession")
    public void checkSession() {
    }

    /**
     * handle the request "/banUser" and return the result.
     * It can only be requested by administrators.
     * It can ban or unban a user who is not an administrator
     *
     * @param jwt   the jwt in requestHeader used for checking the user's type
     * @param banId the id of the user who should be banned or unbanned
     * @param ban   wether the user should be banned or unbanned
     * @return {data:{
     * status: Boolean
     * },
     * not_administrator: Boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/banUser")
    public JSONObject banUser(@RequestHeader("Authorization") String jwt, @RequestParam("banId") long banId, @RequestParam("ban") boolean ban) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        Users banUser = usersService.findById(banId).get();
        JSONObject res = new JSONObject();
        JSONObject status = new JSONObject();
        res.put("not_administrator", false);
        if (banUser == null || banUser.getRole() == 0) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        banUser.setRole(ban ? 2 : 1);
        usersService.changeUser(banUser);
        status.put("status", true);
        res.put("data", status);
        return res;
    }

    /**
     * handle the request "/refresh" and return refreshed tokens.
     * It checks whether the token is correct and the user's type. Then it generates new access-token and refresh-token.
     *
     * @param params It cotains "refresh":the old refresh-token
     * @return {
     * data:{
     * token:String
     * refresh-token:String
     * },
     * success:boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/refresh")
    public JSONObject refresh(@RequestBody Map<String, String> params) throws Exception {
        String oldRefresh = params.get("refresh");
        JSONObject res = new JSONObject();
        if (!JwtUtil.verify(oldRefresh)) {
            res.put("success", false);
            return res;
        }
        Claims c = JwtUtil.parseJWT(oldRefresh);
        long userId = Long.parseLong(c.get("id").toString());
        Users users = usersService.findById(userId).get();
        if (users == null || users.getRole() == 2) {
            res.put("success", false);
            return res;
        }
        res.put("success", true);

        JSONObject obj = new JSONObject();
        obj.put("token", JwtUtil.sign(users.getId(), users.getName(), users.getRole(), false));
        obj.put("refreshToken", JwtUtil.sign(users.getId(), users.getName(), users.getRole(), true));
        obj.put("id", users.getId());
        obj.put("type", users.getRole());
        obj.put("loginStatus", true);
        res.put("data", obj);
        return res;
    }
}
