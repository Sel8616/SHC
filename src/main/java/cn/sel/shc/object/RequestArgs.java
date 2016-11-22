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
package cn.sel.shc.object;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestArgs
{
    private final Map<String, Object> args = new HashMap<>();

    public void put(String name, Object value)
    {
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("The arg name must not be null nor empty!");
        }
        if(value == null)
        {
            args.remove(name);
        } else
        {
            args.put(name, value);
        }
    }

    public Object get(String name)
    {
        return args.get(name);
    }

    public Map<String, Object> getAll()
    {
        return args;
    }

    public int size()
    {
        return args.size();
    }

    public boolean isEmpty()
    {
        return args.isEmpty();
    }

    public String toString(String encoding)
            throws UnsupportedEncodingException
    {
        int size = args.size();
        if(size > 0)
        {
            StringBuilder stringBuilder = new StringBuilder(size * 10);
            Iterator<Map.Entry<String, Object>> iterator = args.entrySet().iterator();
            Map.Entry<String, Object> arg = iterator.next();
            String name = arg.getKey();
            Object value = arg.getValue();
            if(name != null && !name.isEmpty())
            {
                stringBuilder.append(name).append('=').append(encode(value, encoding));
            }
            while(iterator.hasNext())
            {
                arg = iterator.next();
                name = arg.getKey();
                value = arg.getValue();
                if(name != null && !name.isEmpty())
                {
                    stringBuilder.append('&').append(name).append('=').append(encode(value, encoding));
                }
            }
            return stringBuilder.toString();
        }
        return "";
    }

    private String encode(Object object, String encoding)
            throws UnsupportedEncodingException
    {
        return URLEncoder.encode(object == null ? "" : String.valueOf(object), encoding);
    }
}