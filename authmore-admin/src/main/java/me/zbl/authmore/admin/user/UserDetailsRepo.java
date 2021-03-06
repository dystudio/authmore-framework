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
package me.zbl.authmore.admin.user;

import me.zbl.authmore.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ZHENG BAO LE
 * @since 2019-01-28
 */
@Repository
public interface UserDetailsRepo extends MongoRepository<UserDetails, String> {

    Optional<UserDetails> findByUsername(String username);

    List<UserDetails> findAllByOrderByIdDesc();

    void deleteByIdIn(List<String> id);
}
