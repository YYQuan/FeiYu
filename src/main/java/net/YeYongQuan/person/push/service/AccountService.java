package net.YeYongQuan.person.push.service;


import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.account.AccountRspModel;
import net.YeYongQuan.person.push.bean.api.restful_model.account.LoginModel;
import net.YeYongQuan.person.push.bean.api.restful_model.account.LoginRspModel;
import net.YeYongQuan.person.push.bean.api.restful_model.account.RegisterModel;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import net.YeYongQuan.person.push.utils.Hib;
import net.YeYongQuan.person.push.utils.TextUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountService extends BaseService{

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<LoginRspModel> login(LoginModel model){
        if(!LoginModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        User user =  UserFactory.login(model);
        if(user !=null ){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                user = UserFactory.updatePushId(user,model.getPushId());
            }
            LoginRspModel  rspModel = new LoginRspModel(user);
            return  ResponseModel.buildOk(rspModel);
        }else{
            return  ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model){

        if(!RegisterModel.check(model)){
            return  ResponseModel.buildParameterError();
        }
        User  user = null;
        user = UserFactory.findUserByPhone(model.getAccount());

        if(user !=null){
            //提示已经有该ACCOUNT了
            return ResponseModel.buildHaveAccountError() ;
        }
        user = UserFactory.findUserByName(model.getName());
        if(user !=null){
            //提示已经有该name了
            return ResponseModel.buildHaveNameError();
        }
        passwordEncryption(model);

        user = UserFactory.register(model);

        if(user !=null){
            if(!Strings.isNullOrEmpty(model.getPushId())){
                UserFactory.updatePushId(user,model.getPushId());
                AccountRspModel rspModel =  new AccountRspModel(user,true);
                return ResponseModel.buildOk(rspModel);
            }
            // 存储成功
            AccountRspModel rspModel =  new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }else{
            //存储失败
            return ResponseModel.buildRegisterError();
        }
    }

    private RegisterModel passwordEncryption(RegisterModel model){
        String password =TextUtil.getMD5(model.getPassword());
        password = TextUtil.encodeBase64(password);
        model.setPassword(password);
        return model;
    }


    @POST
    @Path("/bindPushId/{pushId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel bindPushId(@HeaderParam("token") String token,
                           @PathParam("pushId") String pushID){

        User user = UserFactory.findUserByToken(token);
        user = UserFactory.updatePushId(user,pushID);
        if(user!=null){
            return  ResponseModel.buildOk();
        }else{
            return ResponseModel.buildAccountError();
        }
    }


}
