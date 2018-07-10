package net.YeYongQuan.person.push.service;


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



}
