/*
 * Copyright 2019 JamesZBL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package me.zbl.authmore;

import me.zbl.authmore.OAuthProperties.GrantTypes;
import me.zbl.reactivesecurity.auth.client.ClientDetails;
import me.zbl.reactivesecurity.common.RandomPassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author JamesZBL
 * @since 2019-02-18
 */
@RestController
public class TokenEndpoint {

    private ClientDetailsRepository clients;
    private CodeManager codeManager;

    public TokenEndpoint(ClientDetailsRepository clients, CodeManager codeManager) {
        this.clients = clients;
        this.codeManager = codeManager;
    }

    @PostMapping("/oauth/token")
    public TokenEntity token(
            @RequestParam(value = "grant_type", required = false) String grantType,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "client_id", required = false) String clientId) {
        GrantTypes realType = GrantTypes.eval(grantType);
        TokenEntity token = new TokenEntity();
        if (isEmpty(clientId))
            throw new OAuthException(OAuthException.INVALID_CLIENT);
        switch (realType) {
            case AUTHORIZATION_CDOE:
                Set<String> scopes = codeManager.getCurrentScopes(clientId, code);
                ClientDetails client = clients.findByClientId(clientId)
                        .orElseThrow(() -> new OAuthException(OAuthException.INVALID_CLIENT));
                if (isEmpty(redirectUri) || !client.getRegisteredRedirectUri().contains(redirectUri))
                    throw new OAuthException("unregistered redirect uri");
                long expiresIn = client.getAccessTokenValiditySeconds();
                token.setScope(scopes);
                token.setExpires_in(expiresIn);
                String accessToken = RandomPassword.create();
                String refreshToken = RandomPassword.create();
                token.setAccess_token(accessToken);
                token.setRefresh_token(refreshToken);
                break;
            default:
                throw new OAuthException(OAuthException.UNSUPPORTED_GRANT_TYPE);
        }
        return token;
    }
}
