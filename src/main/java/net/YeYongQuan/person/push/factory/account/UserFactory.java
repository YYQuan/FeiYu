package net.YeYongQuan.person.push.factory.account;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.account.LoginModel;
import net.YeYongQuan.person.push.bean.api.restful_model.account.RegisterModel;
import net.YeYongQuan.person.push.bean.api.restful_model.user.UserInfoModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.utils.Hib;
import net.YeYongQuan.person.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

public class UserFactory {
    public static User findUserByToken(String tokenStr ){
        return Hib.query( session ->
                (User)session.createQuery("from User where token=:token")
                        .setParameter("token",tokenStr)
                        .uniqueResult());

    }

    public static User findUserByName(String name ){
        return Hib.query( session ->
                (User)session.createQuery("from User where name=:name")
                        .setParameter("name",name)
                        .uniqueResult());

    }

    public static User findUserByPhone(String phone){
        return  Hib.query(session ->
                (User) session.createQuery("from User  where phoneNum =:phone")
                    .setParameter("phone",phone)
                    .uniqueResult());
    }

    public static User login(LoginModel model){
        model.setAccount(model.getAccount().trim());
//        把密码转换为MD5 base 64的暗码
        model.setPassword(passWordConvert(model.getPassword()));

        return Hib.query(session ->{
            User u = (User)session.createQuery("from User where  phoneNum=:account and password=:password")
                    .setParameter("account",model.getAccount())
                    .setParameter("password",model.getPassword())
                    .uniqueResult();

            if(u!=null){
                u.setToken(createToken());
                session.saveOrUpdate(u);
            }
            return u;
        });

    }



    public static User register(RegisterModel model){
        User  user = new User();
        user.setPhoneNum(model.getAccount());
        user.setName(model.getName());
        user.setPassword(model.getPassword());
        if(user !=null){
            user.setToken(createToken());
        }
        return   saveOrUpdate(user);
    }

    private static String createToken() {
        String token= null;
        token = UUID.randomUUID().toString();
        token = TextUtil.encodeBase64(token);
        return token;
    }

    public static User updatePushId(final User user,final String newPushId){

        Hib.query(session ->{
            List<User> resultList= (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", newPushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();
            for (User u:resultList){
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
            session.saveOrUpdate(user);
        } );

        if(user.getPushId() == newPushId){

        }else{
            if(!Strings.isNullOrEmpty(user.getPushId())){
                //TODO  通知该账户上次登录的设备退出账户
            }
            user.setPushId(newPushId);
            saveOrUpdate(user);
        }
        return user;
    }

    public static User  saveOrUpdate(final User user){
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }


//    明码转暗码
    public static String  passWordConvert(String str){
        return TextUtil.encodeBase64(TextUtil.getMD5(str.trim()));
    }



    public static User updateUserInfo(User user ,UserInfoModel model) {
        if(checkUpdate(user,model)){
            return saveOrUpdate(user);
        }
        return user;
    }


//    判断是否需要update，并且把需要updaet的内容set到 参数User当中
    private static boolean checkUpdate(User user  ,UserInfoModel model){
        boolean isChange =false;

        if(!Strings.isNullOrEmpty(model.getName())){
            if(user.getName()!=model.getName().trim()){
                user.setName(model.getName().trim());
                isChange = true;
            }
        }
        if(!Strings.isNullOrEmpty(model.getDesc())){
            if(user.getDescription()==null||user.getDescription()!=model.getDesc().trim()){
                user.setDescription(model.getDesc().trim());
                isChange = true;
            }
        }
        if(!Strings.isNullOrEmpty(model.getPortrait())){
            if(user.getPortrait()==null||user.getPortrait()!=model.getPortrait().trim()){
                user.setPortrait(model.getPortrait().trim());
                isChange = true;
            }
        }
        if(model.getSex()>=0){
            if(user.getSex()!=model.getSex()){
                user.setSex(model.getSex());
                isChange = true;
            }
        }
        return isChange;

    }

}
