package net.YeYongQuan.person.push.factory.account;

import net.YeYongQuan.person.push.bean.api.ModelCard.UserCard;
import net.YeYongQuan.person.push.bean.api.restful_model.account.RegisterModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.utils.Hib;
import org.hibernate.Session;

public class UserFactory {

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



    public static UserCard register(RegisterModel model){
        User  user = null;
        user = findUserByPhone(model.getAccount());

        if(user !=null){
            //TODO 提示已经有该ACCOUNT了
            return new UserCard(user) ;
        }
        user = findUserByName(model.getName());
        if(user !=null){
            //TODO 提示已经有该name了
            return new UserCard(user) ;
        }

        user = createUserFromRegisterModel(model);
        if(user !=null){
            //TODO  存储成功
            return new UserCard(user) ;
        }else{
            //TODO  存储失败
            return null;
        }

    }

    public static User createUserFromRegisterModel(RegisterModel model){
        User  user = new User();
        user.setPhoneNum(model.getAccount());
        user.setName(model.getName());
        user.setPassword(model.getPassword());
        return  Hib.query(( session ->{
            session.save(user);
            return user;
        }));
    }
}
