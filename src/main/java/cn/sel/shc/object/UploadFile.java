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

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadFile extends UploadObject
{
    private final File file;

    public UploadFile(String file)
    {
        this(null, null, new File(file));
    }

    public UploadFile(File file)
    {
        this(null, null, file);
    }

    public UploadFile(String name, String file)
    {
        this(name, null, new File(file));
    }

    public UploadFile(String name, File file)
    {
        this(name, null, file);
    }

    public UploadFile(String name, String filename, String file)
    {
        this(name, filename, new File(file));
    }

    public UploadFile(String name, String filename, File file)
    {
        if(file == null || !file.exists())
        {
            throw new IllegalArgumentException("The uploading file is null or it does not exist!");
        }
        if(file.isDirectory())
        {
            throw new IllegalArgumentException("Upload a directory is not supported!");
        }
        if(!file.canRead())
        {
            throw new IllegalArgumentException("The file is not readable!");
        }
        this.file = file;
        setFilename(filename == null || filename.isEmpty() ? file.getName() : filename);
        setName(name == null || name.isEmpty() ? getDefaultName() : name);
    }

    @Override
    public InputStream getInputStream()
            throws IOException
    {
        return new FileInputStream(file);
    }

    @Override
    public String getContentType()
    {
        return new MimetypesFileTypeMap().getContentType(file);
    }

    private String getDefaultName()
    {
        return filename.substring(filename.lastIndexOf(File.separator) + 1).substring(0, filename.lastIndexOf('.'));
    }
}