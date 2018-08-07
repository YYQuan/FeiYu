package net.YeYongQuan.person.push.factory.Message;

import net.YeYongQuan.person.push.bean.api.restful_model.message.MessageModel;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.Message;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.utils.Hib;

public class MessageFactory {


    public static Message  findMessageById(String  id){
          return Hib.query(session -> (Message)session.get(Message.class,id));
    }

    public  static Message saveUserMsg (User sender , User receiver , MessageModel model){
        return save(new Message(sender,receiver,model));
    }


    public  static Message saveGroupMsg (User sender , Group receiver , MessageModel model){
        return save(new Message(sender,receiver,model));
    }

    private static Message save(Message msg){
         return Hib.query(session -> {
             session.save(msg);

             //由于懒加载的关系 避免message与user和group关系还未建立
             session.flush();

             //查询最新的msg信息
             session.refresh(msg);

             return msg;

         });
    }
}
