package net.YeYongQuan.person.push.provider;

import com.google.common.base.Strings;
import net.YeYongQuan.person.push.bean.api.restful_model.base.ResponseModel;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.factory.account.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;

@Provider
public class AuthRequestFilter  implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否是登录注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login")
                || relationPath.startsWith("account/register")) {
            // 直接走正常逻辑，不做拦截
            return;
        }
        String token =  requestContext.getHeaders().getFirst("token");

        if(!Strings.isNullOrEmpty(token)){
            User user = UserFactory.findUserByToken(token);
            if(user!=null) {
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return user;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                return;
            }
        }

        ResponseModel model = ResponseModel.buildAccountError();

        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        requestContext.abortWith(response);
    }
}
