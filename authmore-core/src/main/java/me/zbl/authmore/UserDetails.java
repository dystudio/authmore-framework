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
import org.springframework.data.mongodb.core.index.Indexed;
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
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails, PasswordHolder {

    @Id
    private String id;
    private String authorities;
    private String password;
    @Indexed(unique = true)
    private String username;
    private Boolean isAccountNonExpired = true;
    private Boolean isAccountNonLocked = true;
    private Boolean isCredentialsNonExpired = true;
    private Boolean isEnabled = true;

    public UserDetails() {}

    public UserDetails(String username, String authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public UserDetails(String username, String password, String authorities) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return string2Set(authorities)
                .stream().map(a -> (GrantedAuthority) () -> a)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String encoded) {
        this.password = encoded;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public UserDetails setUserPassword(String password) {
        this.password = password;
        return this;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = String.join(",", authorities);
    }

    private Set<String> string2Set(String raw) {
        if (StringUtils.isEmpty(raw)) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(raw.split(",")));
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }
}
