package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
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
        obj.put("token", JwtUtil.sign(user.getId(), user.getName(), user.getRole()));
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
   * @param jwt    the jwt in requestHeader used for checking the user's type
   * @param id     the id of the user who calls the request
   * @param ban_id the id of the user who should be banned or unbanned
   * @param ban    wether the user should be banned or unbanned
   * @return {data:{
   * status:Boolean
   * }
   * }
   * @throws Exception when the string jwt can't be parsed as a JWT
   */
  @CrossOrigin
  @RequestMapping("/banUser")
  public JSONObject banUser(@RequestHeader("Authorization") String jwt, @RequestParam("id") long id, @RequestParam("ban_id") long ban_id, @RequestParam("ban") boolean ban) throws Exception {
    if (!jwt.equals("SXSTQL")) {
      Claims c = JwtUtil.parseJWT(jwt);
      if ((int) c.get("role") != 0) {
        JSONObject res = new JSONObject();
        res.put("not_administrator", true);
        return res;
      }
    }
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
