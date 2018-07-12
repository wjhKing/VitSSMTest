package com.vit.controller.user;

import com.alibaba.fastjson.JSON;
import com.vit.common.base.ApiDataResponse;
import com.vit.common.base.PageDataResponse;
import com.vit.models.user.User;
import com.vit.models.user.UserReq;
import com.vit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(value = "user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 查询所有的用户
     * @return
     */
    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public PageDataResponse<User> getAllUser() {
        PageDataResponse pageDataResponse = this.userService.selectAllUser();
        String json = JSON.toJSONString(pageDataResponse);
        return pageDataResponse;
    }

    /**
     * 获取用户列表
     * @param req
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public PageDataResponse<User> getUserList(UserReq req) {
        PageDataResponse pageDataResponse = this.userService.selectUserList(req);
        return pageDataResponse;
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.GET)
    public ApiDataResponse getUserList(User user) {
        ApiDataResponse apiDataResponse = this.userService.saveUser(user);
        return apiDataResponse;
    }

    @RequestMapping(value = "/deleteUserById", method = RequestMethod.GET)
    public ApiDataResponse deleteUserById(Integer id) {
        ApiDataResponse apiDataResponse = this.userService.deleteUser(id);
        return apiDataResponse;
    }
}
