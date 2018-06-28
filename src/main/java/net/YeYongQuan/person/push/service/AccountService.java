package net.YeYongQuan.person.push.service;


import net.YeYongQuan.person.push.bean.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Path("/account")
public class AccountService {

    @GET
    @Path("/login")
    public  String get(){
        return  "hello  FeiYYYU";
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User post(){
        User user = new User();
        user.setName("YYQ");
        user.setSex("man");
        return  user;
    }


}
