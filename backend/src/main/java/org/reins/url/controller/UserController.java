package org.reins.url.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired

    @CrossOrigin
    @RequestMapping("/getShort")
    public Map<String, Boolean> generateShort(@RequestBody  Map<String,String> params) {
        String username=params.get("name");
        String password=params.get("password");
        String email=params.get("email");
        Map<String,Boolean> res=new HashMap<>();
        res.put("data",)
    }

}
