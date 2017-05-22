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
package cn.sel.shc_test;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

public class JettyServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);

    public static void main(String... args)
            throws Exception
    {
        Server server = new Server(8008);
        server.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                    throws IOException
            {
                i(request);
                LOGGER.info(request.getMethod());
                if(target.contains("upload"))
                {
                    String contentType = request.getContentType();
                    ServletInputStream inputStream = request.getInputStream();
                    MultiPartInputStreamParser parser = new MultiPartInputStreamParser(inputStream, contentType, null, null);
                    Collection<Part> parts = parser.getParts();
                    LOGGER.info("File Count: " + parts.size());
                    parts.forEach(part->
                    {
                        Collection<String> headerNames = part.getHeaderNames();
                        String name = part.getName();
                        String filename = part.getSubmittedFileName();
                        String fileContentType = part.getContentType();
                        long fileSize = part.getSize();
                        LOGGER.info("==========================================================");
                        LOGGER.info("headerNames: " + headerNames);
                        LOGGER.info("name: " + name);
                        LOGGER.info("filename: " + filename);
                        LOGGER.info("fileContentType: " + fileContentType);
                        LOGGER.info("fileSize: " + fileSize);
                        LOGGER.info("==========================================================");
                        try
                        {
                            part.write(filename);
                        } catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    response.setStatus(201);
                } else
                {
                    response.getOutputStream().write("OK".getBytes());
                    response.setStatus(200);
                }
                baseRequest.setHandled(true);
                o(request, response);
            }

            @Override
            protected void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException
            {
                LOGGER.error(request.getMethod());
                response.getOutputStream().write("ERR".getBytes());
                super.doError(target, baseRequest, request, response);
                baseRequest.setHandled(true);
            }
        });
        server.start();
        server.join();
    }

    private static void i(HttpServletRequest request)
    {
        LOGGER.info("#IN#  {} -> {}  [{}]  Headers:{}  Parameters:{}", request.getRemoteAddr(), request.getRequestURI(), request.getMethod(), getHeaders(request), request.getParameterMap());
    }

    private static void o(HttpServletRequest request, HttpServletResponse response)
    {
        LOGGER.info("#OUT# {} -> {}  [{}]  StatusCode:{}  Headers:{}", request.getRemoteAddr(), request.getRequestURI(), request.getMethod(), response.getStatus(), getHeaders(response));
    }

    private static String getHeaders(HttpServletRequest request)
    {
        Enumeration<String> headerNames = request.getHeaderNames();
        List<String> result = new ArrayList<>();
        while(headerNames.hasMoreElements())
        {
            String name = headerNames.nextElement();
            result.add(String.format("%s=%s", name, getEnumerationString(request.getHeaders(name))));
        }
        return Arrays.toString(result.toArray());
    }

    private static String getHeaders(HttpServletResponse response)
    {
        List<String> headers = new ArrayList<>(response.getHeaderNames());
        for(int i = 0; i < headers.size(); i++)
        {
            String header = headers.get(i);
            headers.set(i, String.format("%s=%s", header, response.getHeader(header)));
        }
        return Arrays.toString(headers.toArray(new String[headers.size()]));
    }

    private static String getEnumerationString(Enumeration enumeration)
    {
        List<String> result = new ArrayList<>();
        while(enumeration.hasMoreElements())
        {
            result.add(String.valueOf(enumeration.nextElement()));
        }
        return Arrays.toString(result.toArray());
    }
}