package net.YeYongQuan.person.push.service;

import net.YeYongQuan.person.push.bean.db.User;

import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {
    @Context
    protected  SecurityContext securityContext;

    public  User getSelf(){
        return (User) securityContext.getUserPrincipal();
    }
}
