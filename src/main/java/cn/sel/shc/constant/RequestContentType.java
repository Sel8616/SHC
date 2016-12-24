/*
 * Copyright 2015-2016 Erlu Shang (sel8616@gmail.com/philshang@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sel.shc.constant;

import java.util.UUID;

public enum RequestContentType
{
    FORM_URL_ENCODED
            {
                @Override
                public String value()
                {
                    return "application/x-www-form-urlencoded";
                }
            },
    FORM_MULTI_PART
            {
                @Override
                public String value()
                {
                    return "multipart/form-data; boundary=" + BOUNDARY;
                }
            };
    public static final String BOUNDARY = "--------SHC#Boundary#" + UUID.randomUUID();

    public abstract String value();
}