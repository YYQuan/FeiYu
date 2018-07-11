package net.YeYongQuan.person.push;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.YeYongQuan.person.push.provider.GsonProvider;
import net.YeYongQuan.person.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * @author qiujuer
 */
public class Application extends ResourceConfig {
    public Application(){
        //注册处理类的包名
        packages(AccountService.class.getPackage().getName());

        // 注册Json解析器
        //register(JacksonJsonProvider.class);
        register(GsonProvider.class);

        // 注册打印类
        register(Logger.class);
    }


}
