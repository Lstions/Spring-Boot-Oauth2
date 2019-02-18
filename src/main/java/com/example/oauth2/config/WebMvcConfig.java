package com.example.oauth2.config;

import com.example.oauth2.mvc.AccessConfirmationController;
import com.example.oauth2.mvc.HelloController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class WebMvcConfig {

    @Bean
    public HelloController helloController(String a){
        HelloController hloc=new HelloController();
        //hloc.setStr(a);
        return  hloc;
    };

    @Bean
    public String getStringRec(){
        return "hhh";
    }

    @Bean
    public DefaultOAuth2RequestFactory requestFactory(ClientDetailsService detailsService){
        return new DefaultOAuth2RequestFactory(detailsService);
    }

    @Bean
    public AccessConfirmationController accessConfirmationController(ClientDetailsService service, ApprovalStore store, AuthorizationServerTokenServices tokenServices, OAuth2RequestFactory factory){
        AccessConfirmationController controller=new AccessConfirmationController();
        controller
                .setApprovalStore(store)
                .setClientDetailsService(service)
                .setTokenServices(tokenServices)
                .setRequestFactory(factory);
        return controller;
    }
/*
    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model) throws Exception {
        // We can add more stuff to the model here for JSP rendering. If the client was a machine then
        // the JSON will already have been rendered.
        model.put("message", "There was a problem with the OAuth2 protocol");
        return "oauth_error";
    }
*/
}