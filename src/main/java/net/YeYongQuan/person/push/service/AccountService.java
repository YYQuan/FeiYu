package net.YeYongQuan.person.push.service;


import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.account.RegisterModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public int post(){
         SessionFactory sf = null;
        try{
            Configuration cfg = new Configuration().configure();
            sf = cfg.buildSessionFactory();
            Session session = sf.openSession();
            System.out.println("能打开session，那就没错了");

        }catch(HibernateException e){
            e.printStackTrace();
        }
        return  19;
    }


    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserCard register(RegisterModel model){
        if(!RegisterModel.check(model)){
            return  null;
        }

        UserCard card = UserFactory.register(model);
        return card;
    }


}
