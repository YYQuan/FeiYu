package net.YeYongQuan.person.push.factory.Group;

import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.GroupMember;
import net.YeYongQuan.person.push.bean.db.User;

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

    public static Set<GroupMember> getMembers(String groupId){
        //TODO
        return null;
    }

    public static  Set<GroupMember>  getMembers(Group group){
        //TODO
        return null;
    }
}
