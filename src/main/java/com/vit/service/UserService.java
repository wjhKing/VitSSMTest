package com.vit.service;

import com.vit.common.base.ApiDataResponse;
import com.vit.common.base.PageDataResponse;
import com.vit.models.user.User;
import com.vit.models.user.UserReq;

import java.util.List;

public interface UserService {

    /**
     * 查询所有user
     * @return
     */
    PageDataResponse<User> selectAllUser();
    /**
     * 通过userReq查询user列表
     * @param req
     * @return
     */
    PageDataResponse<User> selectUserList(UserReq req);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    ApiDataResponse saveUser(User user);

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    ApiDataResponse deleteUser(Integer id);
}
