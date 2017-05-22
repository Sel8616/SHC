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
package cn.sel.shc.object;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadData extends UploadObject
{
    private final byte[] data;

    public UploadData(String name, byte[] data)
    {
        this(name, name, data);
    }

    public UploadData(String name, String filename, byte[] data)
    {
        if(data == null || data.length < 1)
        {
            throw new IllegalArgumentException("The uploading data must not be null or empty!");
        }
        this.data = data;
        setName(name);
        setFilename(filename);
    }

    @Override
    public InputStream getInputStream()
            throws IOException
    {
        return new ByteArrayInputStream(data);
    }

    @Override
    public String getContentType()
    {
        return "text/plain";
    }
}