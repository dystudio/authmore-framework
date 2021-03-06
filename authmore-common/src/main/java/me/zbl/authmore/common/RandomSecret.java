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
package me.zbl.authmore.common;

import java.util.Random;

/**
 * @author ZHENG BAO LE
 * @since 2019-02-10
 */
public class RandomSecret {

    private RandomSecret() {}

    public static String create() {
        return create(32);
    }

    public static String create(int length) {
        int p = 0;
        char[] table = new char[62];
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (char i = '0'; i <= '9'; i++) {
            table[p++] = i;
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            table[p++] = i;
        }
        for (char i = 'a'; i <= 'z'; i++) {
            table[p++] = i;
        }
        for (int i = 0; i < length; i++) {
            int po = random.nextInt(62);
            sb.append(String.valueOf(table[po]));
        }
        return sb.toString();
    }
}
