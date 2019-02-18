package com.example.oauth2.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

import java.util.Set;

public class MyUserApprovalHandler extends ApprovalStoreUserApprovalHandler {

    private boolean useApprovalStore = true;

    private ClientDetailsService clientDetailsService;


    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
        super.setClientDetailsService(clientDetailsService);
    }

    public void setUseApprovalStore(boolean useApprovalStore) {
        this.useApprovalStore = useApprovalStore;
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        boolean approved=false;
        if (useApprovalStore){
            authorizationRequest= super.checkForPreApproval(authorizationRequest,userAuthentication);
            approved=authorizationRequest.isApproved();
        }
        else {
            if (clientDetailsService!=null){
                Set<String> rScopes= authorizationRequest.getScope();

                try{

                    ClientDetails clientDetails=clientDetailsService
                            .loadClientByClientId(authorizationRequest.getClientId());
                    for (String scope :rScopes){
                        if (clientDetails.isAutoApprove(scope)){
                            approved=true;
                            break;
                        }
                    }

                }
                catch (ClientRegistrationException e){
                    throw e;
                }
            }
        }
        authorizationRequest.setApproved(approved);
        return authorizationRequest;
    }
}
