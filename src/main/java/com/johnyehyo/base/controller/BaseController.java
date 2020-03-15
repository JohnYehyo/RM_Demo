package com.johnyehyo.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Controller
@RequestMapping(value = "base")
public class BaseController {

    @RequestMapping(value = "init")
    public String init() {
        return "test";
    }

}
