package net.YeYongQuan.person.push.factory.push;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.MessageCard;
import net.YeYongQuan.person.push.bean.api.restful_model.base.PushModel;
import net.YeYongQuan.person.push.bean.db.*;
import net.YeYongQuan.person.push.factory.Group.GroupFactory;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import net.YeYongQuan.person.push.utils.Hib;
import net.YeYongQuan.person.push.utils.PushDispatcher;
import net.YeYongQuan.person.push.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PushFactory {


    public static void pushMessage(User self , Message msg){
        if(self == null || msg == null){
            return ;
        }

        MessageCard card = new MessageCard(msg);

        String entity = TextUtil.toJson(card);

        PushDispatcher  dispatcher = new PushDispatcher();

        if(msg.getReceiverGroup()==null && Strings.isNullOrEmpty(msg.getReceiverGroupId())){

            //要把push的消息保存到数据库

            //发送消息给user
            User  receiver = UserFactory.findUserById(msg.getReceiverId());

            if(receiver == null) {
                return;
            }

            PushHistory history = new PushHistory();
            // 普通消息类型
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setSenderId(self.getId());
            history.setSender(self);
            // 接收者当前的设备推送Id
            history.setReceiverPushId(receiver.getPushId());

            PushModel model  = new PushModel();
            model.add(history.getEntityType(),entity);

            dispatcher.add(receiver.getPushId(),model);

            Hib.query( session -> {
                session.saveOrUpdate(history);
                return history;
            } );
        }else{

            Group group  = msg.getReceiverGroup();

            //由于延迟加载 msg 内的group是有可能为null的
            if(group==null){
                //前面已经确定了 self是 该group的成员，有资格对该group发送消息
                group = GroupFactory.findGroupById(msg.getReceiverGroupId());
            }

            if(group ==null){
                return;
            }

            Set<GroupMember>  mMembers = GroupFactory.getMembers(group);

            if(mMembers==null || mMembers.size()<=0){
                return;
            }

            mMembers= mMembers.stream()
                    .filter(groupMember -> !groupMember.getUserId().equalsIgnoreCase(self.getId()))
                    .collect(Collectors.toSet());

            if(mMembers.size()<=0){
                return;
            }

            List<PushHistory>  list =  new ArrayList<>();

            groupPush(dispatcher,self,mMembers,list,entity,PushModel.ENTITY_TYPE_MESSAGE);

            Hib.query(session -> {
                for (PushHistory model :list){
                    session.save(model);
                }
            });

        }

        dispatcher.submit();
    }

    private static void groupPush(PushDispatcher dispatcher,
                                  User sender,
                                  Set<GroupMember> mMembers,
                                  List<PushHistory> list,
                                  String entity,
                                  int entityTypeMessage) {

            for (GroupMember member:mMembers){

                User receiver = member.getUser();
                PushHistory history = new PushHistory();
                // 普通消息类型
                history.setEntityType(entityTypeMessage);
                history.setEntity(entity);
                history.setReceiver(receiver);
                history.setSenderId(sender.getId());
                history.setSender(sender);
                // 接收者当前的设备推送Id
                history.setReceiverPushId(receiver.getPushId());
                list.add(history);

                PushModel model  = new PushModel();
                model.add(history.getEntityType(),entity);

                dispatcher.add(receiver.getPushId(),model);

            }

    }
}
