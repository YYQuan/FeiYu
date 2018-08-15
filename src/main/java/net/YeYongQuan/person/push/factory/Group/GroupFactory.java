package net.YeYongQuan.person.push.factory.Group;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupCreateModel;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.GroupMember;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.utils.Hib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupFactory {

    public static Group  findGroupById(String id){
        return Hib.query(  session -> (Group)session.get(Group.class,id));
    }

    //用户查询群， 要先判断 该用户是不是在该群内
    public static Group  findGroupById(User user, String id){
       GroupMember member  =getMember(user.getId(),id);
       if(member!=null){
           return member.getGroup();
       }
        return null;
    }


    public static Group findGroupByName(String groupName){
        return Hib.query(session ->
            (Group)session.createQuery("from Group where lower(name)=:name")
                        .setParameter("name",groupName.toLowerCase())
                        .uniqueResult()

        );
    }


    /**
     * 由群ID获取全部 群成员的set
     * @param groupId
     * @return
     */
    public static Set<GroupMember> getMembers(String groupId){
        return Hib.query(session -> {
            List members = session.createQuery("from GroupMember where groupId=:groupId")
                    .setParameter("groupId",groupId)
                    .list();
            return new HashSet<GroupMember>(members);
        });
    }


    /**
     * 获取一个user的全部群信息
     */
    public static Set<GroupMember> getUserGroups(String selfId){
        return Hib.query(session -> {
            List members = session.createQuery("from GroupMember where userId=:id")
                    .setParameter("id",selfId)
                    .list();
            return new HashSet<GroupMember>(members);
        });
    }

    /**
     * 创建一个群
     * @param creater   创建者
     * @param model     创建信息
     * @param list      创建信息中 成员 所对应的user对象（创建信息中只有id信息的）
     * @return
     */
    public static Group create(User creater, GroupCreateModel model,List<User> list){

        return Hib.query(session -> {

            Group  group  = new Group(creater,model);
            session.save(group);

            GroupMember member = new GroupMember(creater,group);
            member.setPermission(GroupMember.PERMISSION_TYPE_ADMIN_SU);
            session.save(member);

            for (User user :list){
                GroupMember  newMember = new GroupMember(user,group);
                session.save(newMember);
            }

            return group;
        });
    }

    /**
     * 获取一个群中 某一个群成员的信息
     * @param userId        哪个人
     * @param groupId       哪个群
     * @return
     */
    public static GroupMember getMember(String userId ,String groupId){
        return Hib.query(session ->
                (GroupMember) session.createQuery("from   GroupMember  where groupId =:groupId and userId =:userId")
                    .setParameter("groupId",groupId)
                    .setParameter("userId",userId)
                    .uniqueResult()
        );

    }

    /**
     * 根据参数name 来搜索群
     * @param name
     * @return
     */
    public static List<Group> search(String name){
        if(Strings.isNullOrEmpty(name)){
            name = "";
        }
        String searchContent = "%" +name +"%";

        return Hib.query(session -> {
                return (List<Group>) session.createQuery("from Group  where   lower(name) like :name")
                        .setParameter("name",searchContent)
                        .list();
            }
        );
    }


    /**
     * 给群添加 group
     * @param group
     * @param insertUsers
     * @return
     */
    public static Set<GroupMember> addMembers(Group group, List<User> insertUsers){
        return Hib.query(session -> {
            Set<GroupMember>  members  = new HashSet<>();

            for(User u :insertUsers){
                GroupMember member = new GroupMember(u,group);
                session.save(member);
                members.add(member);

                //此时并没有进行关联查询 ，需要进行refresh之后 才会有关联查询  ,但是消耗较高，不建议放在循环中来做
                // 对于什么叫没有进行关联查询， 我的理解是  别的表和 这个groupMember表的这条信息没有建立关联
                //比如说 group表中有一个  getMember的懒加载，   加入给groupA add了 一个memberA ，但是还没有建立关联的话，那么 使用groupA的.getMember是获取不好这个memberA的
                //session .refresh（member）
            }
            return members;
        });
    }

}
