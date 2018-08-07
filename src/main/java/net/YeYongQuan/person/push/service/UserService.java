package net.YeYongQuan.person.push.service;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.api.restful_model.user.UserInfoModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.account.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

    @GET
    @Path("/contact")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  ResponseModel<List<UserCard>>  contact(){

        List<UserCard> cards =null;
        List<User> users =null;
        User self = getSelf();
        users =  UserFactory.getContact(self);

        cards =users.stream()
                .map(user ->new UserCard(user,true)
                )
                .collect(Collectors.toList());

        return ResponseModel.buildOk(cards);
    }


    @PUT
    @Path("/follow/{followId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  ResponseModel<UserCard>  follow(@PathParam("followId")  String id){
        User self = getSelf();

        if(self.getId().equalsIgnoreCase(id)|| Strings.isNullOrEmpty(id)){
            return ResponseModel.buildParameterError();
        }

        User target  = UserFactory.findUserById(id);

        if(target == null ){
            return ResponseModel.buildNotFoundUserError(null);
        }

//        默认备注为 null
        User user =UserFactory.follow(self,target,null);
        UserCard  card = new UserCard( user ,true);

        //TODO 后面要 通知 被关注的人

        return ResponseModel.buildOk(card);
    }

    @GET
    @Path("/info/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  ResponseModel<UserCard>  getUserCardInfo(@PathParam("targetId")String id){

        User user = UserFactory.findUserById(id);
        List<User>  list =  UserFactory.getContact(getSelf());
        boolean  isFollow = UserFactory.getUserFollow(getSelf(),user)!=null;
        UserCard card =  new UserCard(user,isFollow);
        return ResponseModel.buildOk(card);
    }

    //搜索人
    @GET  //不设计数据更改 的用get
    @Path("/search/{name:(.*)?}")   //名字可以为任意字符，并且长度可以为null
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public  ResponseModel<List<UserCard>>  search(@DefaultValue("") @PathParam("name") String name) {
            List<User> list  = UserFactory.search(name);
            List<User> contacts = UserFactory.getContact(getSelf());
            List<UserCard> cards = list.stream().map(
                    user -> {
//                      和已经关注的列表来对比，  不要用getUserFollow来对比，因为这里等效于一个双重循环， getUserFollow的体量太大
                        boolean isFollow =false;
                        isFollow = user.getId().equalsIgnoreCase(getSelf().getId())||
                                contacts.stream().anyMatch(user1 -> user1.getId().equalsIgnoreCase(user.getId()));
                        return  new UserCard(user ,isFollow);
                    }
                    )
            .collect(Collectors.toList());

        return ResponseModel.buildOk(cards);
    }
}
