package net.YeYongQuan.person.push.service;

import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.api.restful_model.user.UserInfoModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.account.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserService extends BaseService{

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  ResponseModel<UserCard>  update(UserInfoModel  model){
        if(!UserInfoModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        User user =UserFactory.updateUserInfo(getSelf(),model);
        UserCard card = new UserCard(user,true);
        return ResponseModel.buildOk(card);

    }
}
