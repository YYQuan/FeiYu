package net.YeYongQuan.person.push.bean.api.restful_model.account;

import com.google.gson.annotations.Expose;
import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.db.User;


//AccountRspModel存在的意义 ： 返回一些userCard中没有的较为私密的信息
//但又不至于让user暴露
public class LoginRspModel {

    @Expose
    private UserCard userCard;
    @Expose
    private String account;
    @Expose
    private String token;

    public LoginRspModel(User user) {
        this( new UserCard(user),user.getPhoneNum(),user.getToken());
    }

    public LoginRspModel(User user, boolean isBind) {
        this( new UserCard(user),user.getPhoneNum(),user.getToken());
    }

    public LoginRspModel(UserCard userCard, String account, String token) {

        this.userCard = userCard;
        this.account = account;
        this.token = token;

    }

    public UserCard getUserCard() {
        return userCard;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
