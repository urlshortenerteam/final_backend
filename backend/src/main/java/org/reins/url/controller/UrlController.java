package org.reins.url.controller;
import org.reins.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
@RestController
public class UrlController {
    @Autowired
    UrlService urlService;
    @RequestMapping("/getShort")
    public List<String> generateShort(@RequestBody List<String> params) {
        return urlService.generateShorten(params);
    }
}
