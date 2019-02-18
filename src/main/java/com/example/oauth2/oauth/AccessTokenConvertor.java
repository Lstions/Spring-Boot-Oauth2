package com.example.oauth2.oauth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenConvertor extends DefaultAccessTokenConverter {

    private UserAuthenticationConverter userTokenConverter =new DefaultUserAuthenticationConverter();

    private boolean includeGrantType;

    private String scopeAttribute = SCOPE;

    private String clientIdAttribute = CLIENT_ID;

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {

        //TODO check_token response 添加用户更多信息
        Map<String, Object> response = new HashMap<String, Object>();
        OAuth2Request clientToken = authentication.getOAuth2Request();

        if (!authentication.isClientOnly()) {
            response.putAll(userTokenConverter.convertUserAuthentication(authentication.getUserAuthentication()));
        } else {
            if (clientToken.getAuthorities()!=null && !clientToken.getAuthorities().isEmpty()) {
                response.put(UserAuthenticationConverter.AUTHORITIES,
                        AuthorityUtils.authorityListToSet(clientToken.getAuthorities()));
            }
        }

        if (token.getScope()!=null) {
            response.put(scopeAttribute, token.getScope());
        }
        if (token.getAdditionalInformation().containsKey(JTI)) {
            response.put(JTI, token.getAdditionalInformation().get(JTI));
        }

        if (token.getExpiration() != null) {
            response.put(EXP, token.getExpiration().getTime() / 1000);
        }

        if (includeGrantType && authentication.getOAuth2Request().getGrantType()!=null) {
            response.put(GRANT_TYPE, authentication.getOAuth2Request().getGrantType());
        }

        response.putAll(token.getAdditionalInformation());

        response.put(clientIdAttribute, clientToken.getClientId());
        if (clientToken.getResourceIds() != null && !clientToken.getResourceIds().isEmpty()) {
            response.put(AUD, clientToken.getResourceIds());
        }
        return response;
    }


    @Override
    public void setUserTokenConverter(UserAuthenticationConverter userTokenConverter) {
        super.setUserTokenConverter(userTokenConverter);
        this.userTokenConverter = userTokenConverter;
    }

    @Override
    public void setIncludeGrantType(boolean includeGrantType) {
        super.setIncludeGrantType(includeGrantType);
        this.includeGrantType = includeGrantType;
    }

    @Override
    public void setScopeAttribute(String scopeAttribute) {
        super.setScopeAttribute(scopeAttribute);
        this.scopeAttribute = scopeAttribute;
    }

    @Override
    public void setClientIdAttribute(String clientIdAttribute) {
        super.setClientIdAttribute(clientIdAttribute);
        this.clientIdAttribute = clientIdAttribute;
    }
}
