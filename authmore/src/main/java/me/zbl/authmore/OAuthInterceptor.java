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

import me.zbl.authmore.OAuthProperties.RequireTypes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static me.zbl.authmore.OAuthProperties.REQUEST_AUTHORITIES;
import static me.zbl.authmore.OAuthProperties.REQUEST_SCOPES;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author JamesZBL
 * @since 2019-02-27
 */
public class OAuthInterceptor implements HandlerInterceptor {

    private final OAuthResourceProperties oAuthResourceProperties;

    public OAuthInterceptor(OAuthResourceProperties oAuthResourceProperties) {
        this.oAuthResourceProperties = oAuthResourceProperties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        HandlerMethod method = (HandlerMethod) handler;
        ScopeRequired scope = method.getMethodAnnotation(ScopeRequired.class);
        AuthorityRequired authority = method.getMethodAnnotation(AuthorityRequired.class);
        String requireResourceId = oAuthResourceProperties.getResourceId();
        Set<String> resourceIds = (Set<String>) request.getAttribute(OAuthProperties.REQUEST_RESOURCE_IDS);
        if (scope != null) {
            if (!support(request, response, REQUEST_SCOPES, scope.value(), scope.type()))
                return false;
        }
        if (authority != null) {
            if (!support(request, response, REQUEST_AUTHORITIES, authority.value(), authority.type()))
                return false;
        }
        if (!isEmpty(requireResourceId)) {
            if(null == resourceIds || !resourceIds.contains(requireResourceId)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean support(HttpServletRequest request, HttpServletResponse response, String requestScopes,
                            String[] value, RequireTypes type) throws IOException {
        Set<String> scopes = (Set<String>) request.getAttribute(requestScopes);
        boolean support = OAuthUtil.support(type, value, scopes);
        if (null == scopes || !support) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}