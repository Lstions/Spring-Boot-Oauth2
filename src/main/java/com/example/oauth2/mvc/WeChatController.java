package com.example.oauth2.mvc;

import com.example.oauth2.rbac.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WeChatController {

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserManager userManager;

    @RequestMapping(value = "/wx/token")
    @ResponseBody
    public ResponseEntity<OAuth2AccessToken> getWxToken(@RequestParam Map<String,?> params){
        Map<String, String> parameters = new HashMap<String, String>();

        String client_id = (String) params.get("client_id");
        String user_name=(String) params.get("user_name") ;

        if ( !(StringUtils.hasText(client_id)&&StringUtils.hasText(user_name)) ){
            throw new IllegalArgumentException("bad args");
        }

        //获得客户端信息
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(client_id);

        //获得用户信息
        UserDetails user= userManager.loadUserByUsername(user_name);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        //构建用户请求 获取access_token
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user,null,authorities);

        OAuth2Request request=new OAuth2Request(parameters,client_id,authorities,true,clientDetails.getScope(),clientDetails.getResourceIds(),null,null,null);
        OAuth2Authentication oAuth2Authentication=new OAuth2Authentication(request,authentication);

        OAuth2AccessToken accessToken=tokenServices.createAccessToken(oAuth2Authentication);

        //Map<String,?> map =accessTokenConverter.convertAccessToken(accessToken,oAuth2Authentication);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
}
