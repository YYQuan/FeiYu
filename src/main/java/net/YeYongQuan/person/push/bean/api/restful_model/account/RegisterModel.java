package net.YeYongQuan.person.push.bean.api.restful_model.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.sun.org.apache.xpath.internal.operations.Mod;

public class RegisterModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String name;
    @Expose
    private String pushId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public static  boolean check(RegisterModel model){
        model.setAccount(model.getAccount().trim());
        model.setName(model.getName().trim());
        return   model!=null
                && !Strings.isNullOrEmpty(model.getAccount())
                && !Strings.isNullOrEmpty(model.getPassword())
                && !Strings.isNullOrEmpty(model.getName());
    }
}
