package net.YeYongQuan.person.push.bean.api.restful_model.user;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class UserInfoModel {
    @Expose
    private String name ;
    @Expose
    private String desc;
    @Expose
    private String portrait;
    @Expose
    private int sex = -1;

    public static boolean check(UserInfoModel model){
        return model!=null
                && (!Strings.isNullOrEmpty(model.name)
                    ||!Strings.isNullOrEmpty(model.desc)
                    ||!Strings.isNullOrEmpty(model.portrait)
                    ||model.getSex()>=0);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
