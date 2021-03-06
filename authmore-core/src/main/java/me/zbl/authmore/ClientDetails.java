/*
 * Copyright 2019 ZHENG BAO LE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zbl.authmore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZHENG BAO LE
 * @since 2019-01-28
 */
@Document
public class ClientDetails implements org.springframework.security.oauth2.provider.ClientDetails, PasswordHolder {

    @Id
    private String clientId;
    private String authorizedGrantTypes;
    private Boolean scoped;
    private String scope;
    private String resourceIds;
    private Boolean isSecretRequired = true;
    private String clientSecret;
    private String authorities;
    private String registeredRedirectUri;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Boolean isAutoApprove = true;
    private String additionalInformation;

    /* customized fields **/
    private String clientName;

    public ClientDetails() {
    }

    public ClientDetails(String clientId, String grantTypes, String scopes, String clientSecret,
                         Integer accessTokenValiditySeconds) {
        this.clientId = clientId;
        this.authorizedGrantTypes = grantTypes;
        this.scope = scopes;
        this.clientSecret = clientSecret;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public ClientDetails(String clientId, String grantTypes, String scopes, String clientSecret,
                         String registeredRedirectUri, Integer accessTokenValiditySeconds) {
        this.clientId = clientId;
        this.authorizedGrantTypes = grantTypes;
        this.scope = scopes;
        this.clientSecret = clientSecret;
        this.registeredRedirectUri = registeredRedirectUri;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public ClientDetails(String clientId, String grantTypes, String scopes, String clientSecret,
                         String registeredRedirectUri, Integer accessTokenValiditySeconds, String resourceIds) {
        this.clientId = clientId;
        this.authorizedGrantTypes = grantTypes;
        this.scope = scopes;
        this.clientSecret = clientSecret;
        this.registeredRedirectUri = registeredRedirectUri;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.resourceIds = resourceIds;
    }

    public ClientDetails(
            String clientId, String grantTypes, String scopes, String clientSecret,
            String registeredRedirectUri, Integer accessTokenValiditySeconds, String resourceIds,
            String authorities) {
        this.clientId = clientId;
        this.authorizedGrantTypes = grantTypes;
        this.scope = scopes;
        this.clientSecret = clientSecret;
        this.registeredRedirectUri = registeredRedirectUri;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.resourceIds = resourceIds;
        this.authorities = authorities;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return string2Set(resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return isSecretRequired;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return string2Set(scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return string2Set(authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return string2Set(registeredRedirectUri);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return string2Set(authorities)
                .stream().map(a -> (GrantedAuthority) () -> a)
                .collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    @Override
    public String getPassword() {
        return getClientSecret();
    }

    @Override
    public void setPassword(String encoded) {
        setClientSecret(encoded);
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = String.join(",", authorities);
    }

    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = String.join(",", authorizedGrantTypes);
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = String.join(",", resourceIds);
    }

    public void setRegisteredRedirectUri(List<String> registeredRedirectUri) {
        this.registeredRedirectUri = String.join(",", registeredRedirectUri);
    }

    public void setScope(List<String> scope) {
        this.scope = String.join(",", scope);
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setScoped(Boolean scoped) {
        this.scoped = scoped;
    }

    public Boolean getScoped() {
        return scoped;
    }

    public Boolean isAutoApprove() {
        return isAutoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        isAutoApprove = autoApprove;
    }

    public Set<String> getAuthoritySet() {
        return string2Set(this.authorities);
    }

    private Set<String> string2Set(String raw) {
        if (StringUtils.isEmpty(raw)) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(raw.split(",")));
    }
}
