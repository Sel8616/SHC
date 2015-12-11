package cn.sel.hsharp.core;

import cn.sel.hsharp.constant.RequestError;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Handle the responses received by {@link HttpClient}<br/>
 * <li>1.One ResponseHandler can recognize different {@link Response}s by their 'requestId's.</li>
 * <li>2.A request can be false canceled by calling 'cancelTask(int requestId)' before the response arrive,then the response will be ignored.</li>
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
     * The request was finished : Maybe success/fail/error
     *
     * @param requestId requestId
     */
    protected abstract void onFinished(int requestId);

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
    public void handleResponse(int requestId, Response response)
    {
        if (response != null)
        {
            int statusCode = response.StatusCode;
            RequestError errorCode = response.ErrorCode;
            if (REQUEST_SET.contains(requestId))
            {//This request is still in the sequence. In other case, it should be ignored.
                onFinished(requestId);
                switch (statusCode)
                {
                    case 0:
                        onError(requestId, errorCode);
                        break;
                    case HttpURLConnection.HTTP_OK://200
                        onSuccess(requestId, response);
                        break;
                    default:
                        onFailure(requestId, response.StatusCode);
                        break;
                }
            }
        }
    }

    /**
     * Cancel a pending request
     *
     * @param requestId requestId
     */
    public void cancelTask(int requestId)
    {
        REQUEST_SET.remove(requestId);
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
}