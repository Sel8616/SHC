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

import java.security.SecureRandom;

public enum RequestContentType
{
    FORM_MULTI_PART
            {
                private String boundary = defaultBoundary();

                public RequestContentType withBoundary(String boundary)
                {
                    this.boundary = boundary;
                    return this;
                }

                @Override
                public String value()
                {
                    return "multipart/form-data; boundary=" + boundary;
                }

                @Override
                public String boundary()
                {
                    return boundary;
                }

                private String defaultBoundary()
                {
                    byte[] bytes = new byte[8];
                    return "----SHC#Boundary" + random16();
                }

                private String random16()
                {
                    SecureRandom random = new SecureRandom();
                    int length = 16;
                    String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-=_+";
                    char[] result = new char[length];
                    for(int i = 0; i < length; i++)
                    {
                        result[i] = digits.charAt(random.nextInt(66));
                    }
                    return new String(result);
                }
            },
    FORM_URL_ENCODED
            {
                @Override
                public String value()
                {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                public String boundary()
                {
                    throw new UnsupportedOperationException();
                }
            };

    public abstract String value();

    public abstract String boundary();
}