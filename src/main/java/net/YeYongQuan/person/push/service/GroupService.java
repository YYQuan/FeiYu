package net.YeYongQuan.person.push.service;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.ApplyCard;
import net.YeYongQuan.person.push.bean.api.ModelCard.GroupCard;
import net.YeYongQuan.person.push.bean.api.ModelCard.GroupMemberCard;
import net.YeYongQuan.person.push.bean.api.restful_model.account.AccountRspModel;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupCreateModel;
import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupMemberAttendModel;
import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupMemberUpdateModel;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.GroupMember;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.Group.GroupFactory;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import net.YeYongQuan.person.push.factory.push.PushFactory;
import net.YeYongQuan.person.push.provider.LocalDateTimeConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/group")
public class GroupService extends  BaseService{


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard>  create (GroupCreateModel model){
        if(!GroupCreateModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        User self  = getSelf();
        model.getUsers().remove(self.getId());

        if(model.getUsers().size()<=0){
            return ResponseModel.buildParameterError();
        }

        if(GroupFactory.findGroupByName(model.getName())!=null){
            return ResponseModel.buildHaveNameError();
        }

        List<User>  list = new ArrayList<>();
        for(String id :model.getUsers()){
            User  user  = UserFactory.findUserById(id);
            if(user!=null){
                list.add(user);
            }
        }

        if(list.size()<=0){
            return  ResponseModel.buildParameterError();
        }
        Group group = GroupFactory.create(self,model,list);

        if(group==null){
            return ResponseModel.buildServiceError();
        }

        GroupMember ownerMember = GroupFactory.getMember(self.getId(),group.getId());
        if(ownerMember==null){
            return ResponseModel.buildServiceError();
        }

        Set<GroupMember>  groupMembers = GroupFactory.getMembers(group.getId());

        if(groupMembers==null){
            return ResponseModel.buildServiceError();
        }

        groupMembers = groupMembers.stream()
                                .filter(groupMember -> groupMember.getUserId().equalsIgnoreCase(self.getId()))
                                .collect(Collectors.toSet());

        PushFactory.pushJoinGroup(groupMembers);

        return ResponseModel.buildOk(new GroupCard(ownerMember));
    }

    @GET
    @Path("/search/{name:(.*)?}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>>    searchGroup(@PathParam("name") @DefaultValue("") String name){

        List<Group> groups =  GroupFactory.search(name);
        if(groups!=null && groups.size()>0){
            List<GroupCard> cards = groups.stream()
                                        .map(group -> {
                                            GroupMember member =  GroupFactory.getMember(getSelf().getId(),group.getId());
                                            return new GroupCard(group,member);

                                        })
                                        .collect(Collectors.toList());
            return ResponseModel.buildOk(cards);
        }

        return ResponseModel.buildOk();
    }


    /**
     * 获取我的群 的信息
     * @param dateStr  日期 可填   只显示某日期后加入的群的群信息
     * @return
     */
    @GET
    @Path("/list/{date:(.*)?}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>>  searchMyGroup(@DefaultValue("") @PathParam("date") String dateStr){

        User self = getSelf();
        LocalDateTime dateTime = null;
        if(!Strings.isNullOrEmpty(dateStr)){
            try {
                dateTime = LocalDateTime.parse(dateStr, LocalDateTimeConverter.FORMATTER);
            }catch (Exception e){
                dateTime = null;
            }
        }

        Set<GroupMember> groupMembers = GroupFactory.getUserGroups(self.getId());
        if(groupMembers==null ||groupMembers.size()<=0)
            return ResponseModel.buildOk();

        LocalDateTime finalDateTime = dateTime;


        //用 filter的话有个bug ，如果 groupMembers.size()>0 但是其元素是早于finalDateTime的话， 会报null指针 ,上边判断的部分就是为了解决这个问题
        List<GroupCard> cards = groupMembers.stream()
                                    .filter(groupMember -> finalDateTime ==null||groupMember.getUpdateAt().isAfter(finalDateTime))
                                    .map(GroupCard::new)
                                    .collect(Collectors.toList());

        return ResponseModel.buildOk(cards);
    }



    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> getGroupInfo( @PathParam("groupId") String groupId){
        if(Strings.isNullOrEmpty(groupId))
            return ResponseModel.buildParameterError();

        User  self = getSelf();

        GroupMember member = GroupFactory.getMember(self.getId(),groupId);

        if(member ==null)
            return ResponseModel.buildParameterError();

        GroupCard card = new GroupCard(member);
        return ResponseModel.buildOk(card);
    }

    /**
     * 得到一个群的全部成员
     * @param groupId
     * @return
     */
    @GET
    @Path("/{groupId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>>   getGroupMember(@PathParam("groupId") String groupId){
        if(Strings.isNullOrEmpty(groupId)){
            return ResponseModel.buildParameterError();
        }

        User  self  =  getSelf();

        Group group = GroupFactory.findGroupById(groupId);
        if(group == null){
            return ResponseModel.buildNotFoundGroupError(null);
        }
//        需要判断  是否在本群当中
        group = GroupFactory.findGroupById(self,groupId);
        if(group == null ){
            return  ResponseModel.buildNoPermissionError();
        }

        Set<GroupMember> members = GroupFactory.getMembers(group.getId());
        if(members == null)
            return ResponseModel.buildServiceError();

        List<GroupMemberCard> cards = members.stream()
                                            .map(GroupMemberCard::new  )
                                            .collect(Collectors.toList());

        return ResponseModel.buildOk(cards);
    }


    /**
     * 给群里添加新成员
     * @param groupId
     * @param model
     * @return
     */
    @POST
    @Path("/{groupId}/member")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>>  addMember(@PathParam("groupId") String groupId ,GroupMemberAttendModel model){
        if(Strings.isNullOrEmpty(groupId)||!GroupMemberAttendModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();

        model.getUsers().remove(self.getId());
        if(model.getUsers().size()<= 0)
            return ResponseModel.buildParameterError();

        Group group =GroupFactory.findGroupById(groupId);

        if(group==null)
            return ResponseModel.buildNotFoundGroupError(null);

        GroupMember member = GroupFactory.getMember(self.getId(),groupId);
        if(member ==null || member.getPermission()==GroupMember.PERMISSION_TYPE_NONE)
            return ResponseModel.buildNoPermissionError();

        Set<GroupMember>oldMembers = GroupFactory.getMembers(groupId);
        Set<String>   oldMembersId   = oldMembers.stream()
                                                .map(GroupMember::getUserId)
                                                .collect(Collectors.toSet());

        List<User>  users = new ArrayList<>();

        for (String id : model.getUsers()){

            User  user  = UserFactory.findUserById(id);

            if (user == null)
                continue;

            if(oldMembersId.contains(user.getId()))
                continue;

            users.add(user);
        }

        if(users.size()<=0)
            return ResponseModel.buildParameterError();


        Set<GroupMember>  addGroupMember = GroupFactory.addMembers(group,users);
        if(addGroupMember == null){
            return ResponseModel.buildServiceError();
        }

        List<GroupMemberCard> groupMemberCards = addGroupMember.stream()
                                                        .map(GroupMemberCard::new)
                                                        .collect(Collectors.toList());

        PushFactory.pushJoinGroup(addGroupMember);

        PushFactory.pushGroupMemberAdd(oldMembers,groupMemberCards);

        return  ResponseModel.buildOk(groupMemberCards);

    }


    /**
     * 更改成员信息，请求的人要么是管理员，要么就是成员本人
     *
     * @param memberId 成员Id，可以查询对应的群，和人
     * @param model    修改的Model
     * @return 当前成员的信息
     */
    @PUT
    @Path("/member/{memberId}")
    //http:.../api/group/member/0000-0000-0000-0000
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupMemberCard> modifyMember(@PathParam("memberId") String memberId, GroupMemberUpdateModel model) {
        return null;
    }


    /**
     * 申请加入一个群，
     * 此时会创建一个加入的申请，并写入表；然后会给管理员发送消息
     * 管理员同意，其实就是调用添加成员的接口把对应的用户添加进去
     *
     * @param groupId 群Id
     * @return 申请的信息
     */
    @POST
    @Path("/applyJoin/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> join(@PathParam("groupId") String groupId) {
        return null;
    }








}
