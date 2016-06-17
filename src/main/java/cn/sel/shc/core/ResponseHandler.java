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
package cn.sel.shc.core;

import cn.sel.shc.constant.RequestError;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Handle the responses received by {@link HttpClient}<br/>
 * <li>1.One ResponseHandler can recognize different {@link Response}s by their 'requestId's.</li>
 * <li>2.A request can be false canceled by calling 'cancelTask(int requestId)'. If the response hasn't arrived, it will be ignored.</li>
 *
 * @see HttpClient
 */
public abstract class ResponseHandler
{
    /**
     * RequestIds
     */
    private final Set<Integer> REQUEST_SET = new HashSet<>();

    /**
     * The request was finished(success/fail/error), and one of the other 3 abstract methods will be invoked according to the status code if 'false' was returned.
     *
     * @param requestId requestId
     *
     * @return <li>true</li>The response has been handled in this method.<li>false</li>Not handled yet.
     */
    protected abstract boolean onFinished(int requestId);

    /**
     * The server returned 200 and given an expected result.
     *
     * @param requestId requestId
     * @param response  response
     */
    protected abstract void onSuccess(int requestId, Response response);

    /**
     * The server had received the request,but refused to work and returned a non-200 status code.
     *
     * @param requestId  requestId
     * @param statusCode statusCode
     */
    protected abstract void onFailure(int requestId, int statusCode);

    /**
     * The request hasn't been sent out.
     *
     * @param requestId requestId
     * @param errorCode errorCode
     */
    protected abstract void onError(int requestId, RequestError errorCode);

    /**
     * @param requestId See {@link HttpClient}
     * @param response  {@link Response}
     */
    void handleResponse(int requestId, Response response)
    {
        if(response != null)
        {
            int statusCode = response.getStatusCode();
            RequestError errorCode = response.getErrorCode();
            if(REQUEST_SET.contains(requestId))
            {//This request is still in the sequence. In other case, it should be ignored.
                REQUEST_SET.remove(requestId);
                if(!onFinished(requestId))
                {
                    switch(statusCode)
                    {
                        case 0:
                            onError(requestId, errorCode);
                            break;
                        case HttpURLConnection.HTTP_OK://200
                            onSuccess(requestId, response);
                            break;
                        default:
                            onFailure(requestId, response.getStatusCode());
                            break;
                    }
                }
            }
        }
    }

    /**
     * Tell the ResponseHandler that a new request tagged with 'requestId' is about to be send out.
     *
     * @param requestId Only {@link HttpClient} can invoke this method.
     */
    void registerRequest(int requestId)
    {
        REQUEST_SET.add(requestId);
    }

    /**
     * Try to cancel a pending request.
     *
     * @param requestId requestId
     *
     * @return True: Succeeded to cancel; False: Already returned.
     */
    public boolean cancelTask(int requestId)
    {
        if(REQUEST_SET.contains(requestId))
        {
            REQUEST_SET.remove(requestId);
            return true;
        } else
        {
            return false;
        }
    }
}