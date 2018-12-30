package com.tide.ailab.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tide.ailab.common.log.Logger;

/**
 * 阿里云发送短信工具类
 * @author User
 */
public abstract class SmsUtil {
    /** * 用户密码通知短信code. */
    public static final String USER_PASSWD_CODE = "SMS_123798074";

    /** * 任务消除用户密码通知短信code. */
    public static final String REMOVE_TASK_PASSWD_CODE = "SMS_125015233";

    /** * 用户密码重置短信模板code. */
    public static final String RESET_ACCOUNT_ADMIN_PASSWD_CODE = "SMS_125119185";

    public static void sendRemoveTaskRemind(String time, String sourceId, String mobilePhone) {
        String param = "{\"time\":\"" + time + "\",\"sourceId\":\"" + sourceId + "\"}";
        try {
            sendSms(REMOVE_TASK_PASSWD_CODE, param, mobilePhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int sendTaskRemind(int timeOut, int timeIn, String mobilePhone) {
        String param = "{\"time1\":\"" + timeOut + "\",\"time2\":\"" + timeIn + "\"}";
        try {
            return sendSms(USER_PASSWD_CODE, param, mobilePhone);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 重置账户管理员密码短信
     * @param username
     * @param password
     * @param mobilePhone
     * @return
     */
    public static int sendResetAcntAdminRemind(String username, String password, String mobilePhone) {
        String param = "{\"user_name\":\"" + username + "\",\"password\":\"" + password + "\"}";
        try {
            return sendSms(RESET_ACCOUNT_ADMIN_PASSWD_CODE, param, mobilePhone);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int sendSms(String templateCode, String paramValue, String phoneNum) throws Exception {
        if (StringUtil.isNullOrEmpty(phoneNum)) {
            return 2;
        }
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIjt4w6iCPtrwo",
                "E0DrniBSgxWw1235jSPyJzSmZKuIHa");
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phoneNum);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("中兴力维云运维");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(paramValue);
        // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        // request.setOutId("yourOutId");

        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            // 请求成功
            Logger.i("send success,phonenumber:" + phoneNum);
            return 0;
        } else {
            Logger.e("send fail,errorcode:" + sendSmsResponse.getCode() + " phonenumber:" + phoneNum
                    + " paramValue" + paramValue);
            return -1;
        }

        /*
         * SingleSendSmsRequest request = new SingleSendSmsRequest(); try { request.setSignName("中兴力维云运维");
         * request.setTemplateCode(templateCode); request.setParamString(paramValue); request.setRecNum(phoneNum);
         * client.getAcsResponse(request); } catch (Exception e) { if (e.toString().indexOf(
         * "InvalidSendSms : Frequency limit reaches") > 0) { return 2; } e.printStackTrace(); }
         */

    }
}
