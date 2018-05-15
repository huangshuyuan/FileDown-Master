package com.hsy.filedown;

/**
 * @author syhuang
 * @date 2018/5/4
 */

public class Response {
    private int       Status;
    private String    Message;
    private OtaResult Result;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public OtaResult getResult() {
        return Result;
    }

    public void setResult(OtaResult result) {
        Result = result;
    }

    @Override
    public String toString() {
        return "Response{" + "Status=" + Status + ", Message='" + Message + '\'' + ", Result=" +
                Result + '}';
    }
}
