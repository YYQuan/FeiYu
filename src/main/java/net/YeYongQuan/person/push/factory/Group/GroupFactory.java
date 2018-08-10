package net.YeYongQuan.person.push.factory.Group;

import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupCreateModel;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.GroupMember;
import net.YeYongQuan.person.push.bean.db.User;

import java.util.List;
import java.util.Set;

public class GroupFactory {

    public static Group  findGroupById(String id){
        //TODO

        return null;
    }

    public static Group  findGroupById(User user, String id){
        //TODO

        return null;
    }


    public static Group findGroupByName(String name){
        //TODO
        return null;
    }


    public static Set<GroupMember> getMembers(String groupId){
        //TODO
        return null;
    }

    public static  Set<GroupMember>  getMembers(Group group){
        //TODO
        return null;
    }

    /**
     *
     * @param creater   创建者
     * @param model     创建信息
     * @param list      创建信息中 成员 所对应的user对象（创建信息中只有id信息的）
     * @return
     */
    public static Group create(User creater, GroupCreateModel model,List<User> list){
        //TODO
        return null;
    }

    /**
     * 获取一个群中 群成员的信息
     * @param userId        哪个人
     * @param groupId       哪个群
     * @return
     */
    public static GroupMember getMember(String userId ,String groupId){
        //TODO
        return null;
    }

//    获取一个user的全部群信息
    public static Set<GroupMember> getUserGroups(String selfId){
        //TODO
        return null;
    }

    public static List<Group> search(String name){
        //TODO
        return null;
    }

    public static Set<GroupMember> addMembers(Group group, List<User> insertUsers){
        //TODO
        return null;
    }

}
