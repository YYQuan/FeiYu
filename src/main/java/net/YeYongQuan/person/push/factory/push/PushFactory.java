package net.YeYongQuan.person.push.factory.push;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.GroupMemberCard;
import net.YeYongQuan.person.push.bean.api.ModelCard.MessageCard;
import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
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

            Set<GroupMember>  mMembers = GroupFactory.getMembers(group.getId());

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
                                  User sender,/*为null则代表是系统消息*/
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
                if(sender!=null) {
                    history.setSenderId(sender.getId());
                    history.setSender(sender);
                }
                // 接收者当前的设备推送Id
                history.setReceiverPushId(receiver.getPushId());
                list.add(history);

                PushModel model  = new PushModel();
                model.add(history.getEntityType(),entity);

                dispatcher.add(receiver.getPushId(),model);

            }

    }

    /**
     *  通知 被邀请进群的 user  ，你已经被邀请进群了
     */
    public static void pushJoinGroup(Set<GroupMember>  members){
        PushDispatcher  dispatcher = new PushDispatcher();

        List<GroupMember> list = new ArrayList<>(members);

        List<PushHistory> histories = new ArrayList<>();
        for (GroupMember member :list){
            String userId = member.getUserId();
            User user =  UserFactory.findUserById(userId);

            if(user ==null)
                continue;

            GroupMemberCard card = new GroupMemberCard(member);
            String entity = TextUtil.toJson(card);

            PushHistory history  = new PushHistory();
            history.setEntity(entity);
            history.setEntityType(PushModel.ENTITY_TYPE_ADD_GROUP);
            history.setReceiver(user);
            history.setReceiverPushId(user.getPushId());

            //系统消息 sender 为null
//            history.setSender();
            histories.add(history);

            PushModel  model = new PushModel()
                            .add(history.getEntityType(),history.getEntity());

            dispatcher.add(user.getPushId(),model);
        }

        Hib.query(session -> {
            for (PushHistory msg: histories){
                session.save(msg);
            }
        });

        dispatcher.submit();
    }

    /**
     * 通知旧成员， 有新成员加入
     * @param oldMembers
     * @param insertMembers
     */
    public static void  pushGroupMemberAdd(Set<GroupMember> oldMembers,List<GroupMemberCard> insertMembers){
        PushDispatcher  dispatcher = new PushDispatcher();

        List<PushHistory>  histories = new ArrayList<>();

        String entity = TextUtil.toJson(insertMembers);

        groupPush(dispatcher,null,oldMembers,histories,entity,PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS);

        Hib.query(session -> {
            for(PushHistory msg :histories) {
                session.save(msg);
            }
        });

        dispatcher.submit();
    }

    /**
     * 通知设备，你的账户已经在别处登录
     */
    public static void pushLogout(User receiver){
        PushHistory history = new PushHistory();
        history.setReceiver(receiver);
        history.setReceiverPushId(receiver.getPushId());
        history.setEntityType(PushModel.ENTITY_TYPE_LOGOUT);
        history.setEntity("Account logout!!!");

        Hib.query(session -> {session.save(history);});

        PushDispatcher dispatcher = new PushDispatcher();
        PushModel model = new PushModel().add(history.getEntityType(),history.getEntity());
        dispatcher.add(receiver.getId(),model);

        dispatcher.submit();
    }

    /**
     * 通知被关注者，你被xxx关注了
     */
    public static void pushFollow(User receiver,UserCard userCard){
        userCard.setFollow(true);
        String entity = TextUtil.toJson(userCard);

        PushHistory history = new PushHistory();
        history.setReceiver(receiver);
        history.setReceiverPushId(receiver.getPushId());
        history.setEntityType(PushModel.ENTITY_TYPE_ADD_FRIEND);
        history.setEntity(entity);

        Hib.query(session -> {session.save(history);});

        PushDispatcher dispatcher = new PushDispatcher();
        PushModel model = new PushModel().add(history.getEntityType(),history.getEntity());
        dispatcher.add(receiver.getId(),model);

        dispatcher.submit();
    }

}
