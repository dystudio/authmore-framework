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
package me.zbl.authmore.main.resource;

import me.zbl.authmore.main.oauth.OAuthProperties;
import me.zbl.authmore.main.oauth.OAuthProperties.RequireTypes;
import me.zbl.authmore.main.oauth.OAuthUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author ZHENG BAO LE
 * @since 2019-02-27
 */
public class ResourceServerInterceptor implements HandlerInterceptor {

    private final ResourceServerConfigurationProperties resourceServerConfigurationProperties;

    public ResourceServerInterceptor(ResourceServerConfigurationProperties resourceServerConfigurationProperties) {
        this.resourceServerConfigurationProperties = resourceServerConfigurationProperties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        HandlerMethod method = (HandlerMethod) handler;
        ScopeRequired scope = method.getMethodAnnotation(ScopeRequired.class);
        AuthorityRequired authority = method.getMethodAnnotation(AuthorityRequired.class);
        String requireResourceId = resourceServerConfigurationProperties.getResourceId();
        Set<String> resourceIds = (Set<String>) request.getAttribute(OAuthProperties.REQUEST_RESOURCE_IDS);
        if (scope != null) {
            if (!support(request, response, OAuthProperties.REQUEST_SCOPES, scope.value(), scope.type()))
                return false;
        }
        if (authority != null) {
            if (!support(request, response, OAuthProperties.REQUEST_AUTHORITIES, authority.value(), authority.type()))
                return false;
        }
        if (!isEmpty(requireResourceId)) {
            if (null == resourceIds || !resourceIds.contains(requireResourceId)) {
                response.sendError(SC_UNAUTHORIZED, "no access to resource: " + requireResourceId);
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean support(HttpServletRequest request, HttpServletResponse response, String attributeName,
                            String[] requiredValues, RequireTypes type) throws IOException {
        Set<String> requestValues = (Set<String>) request.getAttribute(attributeName);
        if (null == requestValues)
            return false;
        boolean support = OAuthUtil.support(type, requiredValues, requestValues);
        if (!support) {
            response.sendError(SC_UNAUTHORIZED, "invalid scope or authority to access this resource");
            return false;
        }
        return true;
    }
}