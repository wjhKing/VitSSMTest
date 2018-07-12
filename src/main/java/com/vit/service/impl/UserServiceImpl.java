package com.vit.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vit.common.base.ApiDataResponse;
import com.vit.common.base.BaseResponse;
import com.vit.common.base.PageDataResponse;
import com.vit.common.cache.CacheManager;
import com.vit.common.utils.Checker;
import com.vit.dao.user.UserMapper;
import com.vit.models.user.User;
import com.vit.models.user.UserExample;
import com.vit.models.user.UserReq;
import com.vit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional(value = "lessonTransactionManager")
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private CacheManager cacheManager;

    @Override
    public PageDataResponse<User> selectAllUser() {
        PageDataResponse<User> response = new PageDataResponse<User>();
        try {
            List<User> list = cacheManager.getList("ALLUSER",User.class);
            response.setInfo("from cache");
            if (Checker.isNone(list)) {
                list = userMapper.selectByExample(new UserExample());
                response.setInfo("from db");
                cacheManager.setList("ALLUSER",list,30*60,User.class);
            }
            response.setCode(BaseResponse.SUCCEED);
            response.setPageData(list);
        } catch (Exception e) {
            logger.error("selectAllUser is error",e);
            response.setCode(BaseResponse.APP_EXCEPTION);
            response.setInfo("获取用户列表异常");
        }
        return response;
    }

    @Override
    public PageDataResponse<User> selectUserList(UserReq req) {
        PageDataResponse<User> response = null;
        try {
            if (req == null) {
                response = new PageDataResponse<User>();
                response.setCode(BaseResponse.PARAMETER_ERROR);
                response.setInfo("参数异常，请重新检查");
                return response;
            }
            UserExample example = new UserExample();
            UserExample.Criteria criteria = example.createCriteria();
            if (req.getId() != null && req.getId() > 0) {
                criteria.andIdEqualTo(req.getId());
            }
            if (!Checker.isNone(req.getUsername())) {
                criteria.andUsernameLike("%"+req.getUsername()+"%");
            }
            if (!Checker.isNone(req.getPassword())) {
                criteria.andPasswordEqualTo(req.getPassword());
            }
            if (req.getPageIndex() == null || req.getPageIndex() < 1) {
                req.setPageIndex(1);
            }
            if (req.getPageSize() == null || req.getPageSize() < 1) {
                req.setPageSize(10);
            }
            if (req.getOrderBy() == null || "".equals(req.getOrderBy())) {
                req.setOrderBy(" id desc");
            }
            String key = "page";
            response = cacheManager.get(key,PageDataResponse.class);
            if (response == null) {
                response = new PageDataResponse<>();
                Page page = PageHelper.startPage(req.getPageIndex(),req.getPageSize(),req.getOrderBy());
                List<User> list = userMapper.selectByExample(example);
                PageInfo<User> pageInfo = new PageInfo<User>(list);
                response.setPageData(list);
                response.setCount(pageInfo.getPages());
                response.setPageIndex(req.getPageIndex());
                response.setTotal(pageInfo.getTotal());
                response.setHasNextPage(pageInfo.isHasNextPage());
                response.setHasPreviousPage(pageInfo.isHasPreviousPage());
                response.setCode(BaseResponse.SUCCEED);
                response.setInfo("from db");
                cacheManager.set(key,response,10);
            } else {
                response.setInfo("from cache");
            }

        } catch (Exception e) {
           logger.error("获取用户列表出错，req={}",JSON.toJSONString(req));
           response.setCode(BaseResponse.APP_EXCEPTION);
           response.setInfo(String.format("获取用户列表出错，req={}",JSON.toJSONString(req)));
        }
        return response;
    }

    @Override
    public ApiDataResponse saveUser(User user) {
        ApiDataResponse apiDataResponse = new ApiDataResponse();
        if (user == null) {
            apiDataResponse.setCode(BaseResponse.PARAMETER_ERROR);
            apiDataResponse.setInfo("参数异常，请重新检查");
            return apiDataResponse;
        }
        if (user.getId() == null || user.getId() < 1) {
            user.setCreateTime(new Date());
            int i = userMapper.insertSelective(user);
            apiDataResponse.setInfo(String.format("成功插入 %s 条数据",i));
            apiDataResponse.setCode(BaseResponse.SUCCEED);
        } else {
            User temp = userMapper.selectByPrimaryKey(user.getId());
            if (temp != null) {
                int i = userMapper.updateByPrimaryKeySelective(user);
                apiDataResponse.setInfo(String.format("成功修改 %s 条数据",i));
                apiDataResponse.setCode(BaseResponse.SUCCEED);
            } else {
                apiDataResponse.setInfo(String.format("找不到相应的数据，id=%s",user.getId()));
                apiDataResponse.setCode(BaseResponse.PARAMETER_ERROR);
            }
        }
        return apiDataResponse;
    }

    @Override
    public ApiDataResponse deleteUser(Integer id) {
        ApiDataResponse apiDataResponse = new ApiDataResponse();
        if (id == null || id < 1) {
            apiDataResponse.setCode(BaseResponse.PARAMETER_ERROR);
            apiDataResponse.setInfo("参数异常，请重新检查");
            return apiDataResponse;
        }
        int i = userMapper.deleteByPrimaryKey(id);
        apiDataResponse.setInfo(String.format("成功删除 %s 条数据",i));
        apiDataResponse.setCode(BaseResponse.SUCCEED);
        return apiDataResponse;
    }
}
