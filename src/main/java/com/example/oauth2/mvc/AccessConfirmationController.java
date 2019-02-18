package com.example.oauth2.mvc;

import com.example.oauth2.rbac.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class AccessConfirmationController {

    @Autowired
    private UserManager userManager;

    private ApprovalStore approvalStore;

    private AuthorizationServerTokenServices tokenServices;

    private ClientDetailsService clientDetailsService;

    private OAuth2RequestFactory requestFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model) throws Exception {
        // We can add more stuff to the model here for JSP rendering. If the client was a machine then
        // the JSON will already have been rendered.
        model.put("message", "There was a problem with the OAuth2 protocol");
        return "oauth_error";
    }

    @RequestMapping("/oauth/idtoken")
    @ResponseBody
    public String getTokenById(@RequestParam Map<String,String> params){
        String token=null;

        //tokenServices.createAccessToken();


        return params.toString();
    }

    @RequestMapping(value = "/user/create",method = RequestMethod.POST)
    @ResponseBody
    public void createUser(@RequestParam Map<String,String> params, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
//        User user= UserUtils.createUser(params,passwordEncoder);
//
//        userManager.createUser();

    }

    public AccessConfirmationController setApprovalStore(ApprovalStore approvalStore) {
        this.approvalStore = approvalStore;
        return this;
    }

    public AccessConfirmationController setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
        return this;
    }

    public AccessConfirmationController setTokenServices(AuthorizationServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
        return this;
    }

    public AccessConfirmationController setRequestFactory(OAuth2RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        return this;
    }

}
