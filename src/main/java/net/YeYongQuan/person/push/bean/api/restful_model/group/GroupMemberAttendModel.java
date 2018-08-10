package net.YeYongQuan.person.push.bean.api.restful_model.group;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class GroupMemberAttendModel {
    @Expose
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public static boolean check(GroupMemberAttendModel model) {
        return !(model.users == null
                || model.users.size() == 0);
    }
}
