package com.example.oauth2.config;

import com.example.oauth2.oauth.AccessTokenConvertor;
import com.example.oauth2.oauth.MyUserApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class Oauth2ServerConfig {

    public static String resourceId="src_test";

    @Autowired
    public  DataSource dataSource;

    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public AccessTokenConverter accessTokenConvertor(){
        AccessTokenConvertor accessTokenConvertor=new AccessTokenConvertor();
        accessTokenConvertor.setIncludeGrantType(true);

        return accessTokenConvertor;
    }

    @Configuration
    @EnableResourceServer
    protected static class  ResourceServerConfig extends ResourceServerConfigurerAdapter {

        //TODO 资源服务器 缓存token，优化效率

        @Autowired
        private ResourceServerTokenServices tokenServices;

//        @Autowired
//        private TokenStore tokenStore;

        @Bean
        public ResourceServerTokenServices tokenServices(
                @Value("${clientSecret}") String clientSecret,
                @Value("${clientId}") String clientId,
                @Value("${checkTokenUri}") String checkTokenUri,AccessTokenConverter accessTokenConverter){

            RemoteTokenServices tokenServices=new RemoteTokenServices();
            tokenServices.setCheckTokenEndpointUrl(checkTokenUri);
            tokenServices.setClientId(clientId);
            tokenServices.setClientSecret(clientSecret);
            tokenServices.setAccessTokenConverter(accessTokenConverter);
            return tokenServices;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(resourceId).stateless(false)
                    //.tokenStore(tokenStore)
                    .tokenServices(tokenServices)
            ;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/me")
                    .authorizeRequests()
                        //.antMatchers("/wx/token").hasAuthority("WX_TOKEN")
                        .antMatchers("/me").hasAuthority("ALT_CLS_STU");
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

        @Bean
        public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        @Bean
        public MyUserApprovalHandler myUserApprovalHandler(ApprovalStore approvalStore) throws Exception {

            MyUserApprovalHandler handler=new MyUserApprovalHandler();
            handler.setClientDetailsService(clientDetailsService);
            handler.setUseApprovalStore(true);
            handler.setApprovalStore(approvalStore);
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));

            return handler;
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthorizationCodeServices authorizationCodeServices(){
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Autowired
        public  DataSource dataSource;

        @Autowired
        public  TokenStore tokenStore;

        @Autowired
        public  AccessTokenConverter accessTokenConverter;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        private AuthorizationCodeServices authorizationCodeServices;

        @Qualifier("authenticationManagerBean")
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserApprovalHandler userApprovalHandler;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            String secret=passwordEncoder.encode("11111");
            System.out.println("secret："+secret);

            clients.inMemory()
                    .withClient("mini_program")
                    .authorizedGrantTypes("authorization_code","implicit","refresh_token","password","client_credentials")
                    .secret(secret)
                    .scopes("wirte","read")
                    .redirectUris("https://www.getpostman.com/oauth2/callback?sns=test1")
                    .autoApprove(true)
                    .resourceIds(resourceId)
                    .authorities("ROLE_USER","ALT_CLS_STU","WX_TOKEN");
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
                    .checkTokenAccess("hasAuthority('ROLE_USER')")
                    .allowFormAuthenticationForClients();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .accessTokenConverter(accessTokenConverter)
                    .tokenStore(tokenStore)
                    .userApprovalHandler(userApprovalHandler)
                    .authenticationManager(authenticationManager)
                    .authorizationCodeServices(authorizationCodeServices);
        }


    }
}
