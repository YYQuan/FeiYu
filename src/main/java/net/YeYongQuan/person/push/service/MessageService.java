package net.YeYongQuan.person.push.service;


import net.YeYongQuan.person.push.bean.api.ModelCard.MessageCard;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.api.restful_model.message.MessageModel;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.Message;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.Group.GroupFactory;
import net.YeYongQuan.person.push.factory.Message.MessageFactory;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import net.YeYongQuan.person.push.factory.push.PushFactory;
import net.YeYongQuan.person.push.utils.TextUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/Message")
public class MessageService extends BaseService{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public  ResponseModel<MessageCard> pushMessage(MessageModel model){
        if(!MessageModel.check(model)){
            return ResponseModel.buildParameterError();
        }

        Message msg = MessageFactory.findMessageById(model.getId());

        if(msg!=null){
            return ResponseModel.buildOk(new MessageCard(msg));
        }

        User self = getSelf();

        if(model.getReceiveType()== Message.MSG_TYPE_USER){
            return pushToUser(self,model);
        }else if(model.getReceiveType()== Message.MSG_TYPE_GROUP){
            return pushToGroup(self,model);
        }

        return ResponseModel.buildNotFoundUserError("Con't find receiver user");
    }

    private  ResponseModel<MessageCard> pushToUser(User self ,MessageModel model){

        User  receiver = UserFactory.findUserById(model.getReceiverId());
        if(receiver ==null){
            return ResponseModel.buildNotFoundUserError("Con't find receiver user");
        }

        if(receiver.getId().equalsIgnoreCase(self.getId())){
            // 发送者接收者是同一个人就返回创建消息失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        Message msg = MessageFactory.saveUserMsg(self,receiver,model);

        return buildAndPushMessage( self , msg);
    }

    private  ResponseModel<MessageCard> pushToGroup(User self ,MessageModel model){
        Group group   = GroupFactory.findGroupById(self,model.getReceiverId());
        if(group ==null){
            return ResponseModel.buildNotFoundUserError("Con't find receiver user");
        }

        Message msg = MessageFactory.saveGroupMsg(self,group,model);

        return buildAndPushMessage( self , msg);
    }

    private static ResponseModel<MessageCard> buildAndPushMessage(User self ,Message msg){
        if(msg ==null){
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        PushFactory.pushMessage(self,msg);

        return ResponseModel.buildOk(new MessageCard(msg));
    }
}
