/*
 * Copyright 2015-2017 Erlu Shang (sel8616@gmail.com/philshang@163.com)
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
package cn.sel.shc.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hold attributes for a new http request which is going to be send out but still has other attributes to add.
 */
public class RequestHolder
{
    protected final Map<String, String> HEADERS_SET = new HashMap<>();
    protected final Map<String, List<String>> HEADERS_ADD = new HashMap<>();
    protected String REQUEST_ENCODING;
    protected int TIMEOUT_CONN;
    protected int TIMEOUT_READ;

    public void setRequestEncoding(String encoding)
    {
        REQUEST_ENCODING = encoding;
    }

    public void setConnTimeout(int connTimeout)
    {
        TIMEOUT_CONN = connTimeout;
    }

    public void setReadTimeout(int readTimeout)
    {
        TIMEOUT_READ = readTimeout;
    }

    public void setHeader(String name, String value)
    {
        if(name != null && !name.isEmpty() && value != null)
        {
            HEADERS_SET.put(name, value);
        }
    }

    public void addHeader(String name, String value)
    {
        if(name != null && !name.isEmpty() && value != null)
        {
            List<String> values = HEADERS_ADD.get(name);
            if(values != null)
            {
                values.add(value);
            } else
            {
                values = new ArrayList<>(1);
                values.add(value);
                HEADERS_ADD.put(name, values);
            }
        }
    }
}