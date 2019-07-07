package com.jxtb.web;

import com.jxtb.sdk.*;
import com.jxtb.sdk.config.ConfigurationManager;
import com.jxtb.sdk.context.ApplicationContextHolder;
import com.jxtb.sdk.core.Request;
import com.jxtb.sdk.core.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by jxtb on 2019/6/14.
 */
@WebServlet(name = "getWayService",urlPatterns = "/getWayService", loadOnStartup = 1)
public class HttpGatWayService extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(HttpGatWayService.class);
    private RequestProcess process;
    private static final String REQUEST_STR = "jsonStr";
    public HttpGatWayService (){
        super();
    }

    @Override
    public void init() throws ServletException {
        try{
            logger.debug("start to intitail ");
            // 获取ApplicationContext
//            ServletContext application = getServletContext();
//            ApplicationContext context = WebApplicationContextUtils
//                    .getWebApplicationContext(application);
//            ApplicationContextHolder.setContext(context);
            process = ConfigurationManager.getInstance().config();
        }catch (Exception e){
            ;
        }
        if(process == null){
            logger.info("初始化文件配置错误，系统启动失败！");
            System.exit(1);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        if(contentType == null || contentType.startsWith("multipart/form-data;") == false){
            doCommonRequest(req,resp);
        }else{
            doUploadFileRequest(req,resp,contentType);
        }
    }

    private void doCommonRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        String jsonRequest = sb.toString();
        if(StringUtils.isEmpty(jsonRequest)){
            logger.error("请求报文为空");
            SendResponseFactory.createSendResponse(RequestContext.getResponse())
                    .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010010, "请求报文问空"));
            return;
        }
        String reqData = jsonRequest;
        reqData = URLDecoder.decode(reqData,"UTF-8");
        logger.info("请求报文：" + reqData);
        try{
            RequestContext.setRequest(new Request(req));
            RequestContext.setResponse(new Response(resp));
            long st = System.currentTimeMillis();
            process.process(reqData);
            logger.error("total={}", (System.currentTimeMillis() - st));
        }catch (Throwable t){
            SendResponseFactory.createSendResponse(RequestContext.getResponse())
                    .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010011, "服务器发生错误"));
        }finally {
            RequestContext.removeRequest();
            RequestContext.removeResponse();
            RequestContext.removeJsonObject();
        }
    }

    private void doUploadFileRequest(HttpServletRequest req, HttpServletResponse resp, String contentType) throws ServletException, IOException{
        String boundary = "--" + contentType.substring(contentType.indexOf("boundary=") + 9);
        logger.debug(boundary);
        int pos = boundary.indexOf(":");
        if(pos > 0){
            boundary = boundary.substring(0, pos);
        }
        if(boundary == null || boundary.trim().length() < 1){
            SendResponseFactory.createSendResponse(RequestContext.getResponse())
                    .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010012, "文件传输格式不正确"));
            return;
        }
        try{
            InputStream in = req.getInputStream();
            byte buff[] = new byte[req.getContentLength()];
            byte tmpBuff[] = new byte[4096];
            int readCount = 0;
            int totalCount = 0;
            while ((readCount = in.read(tmpBuff)) != -1){
                System.arraycopy(tmpBuff, 0, buff, totalCount, readCount);
                totalCount += readCount;
            }
            Hashtable requestTab = new Hashtable();
            Multipart mp;
            try{
                requestTab = Multipart.parseRequest(boundary, buff);
            }catch (Exception e){
                e.printStackTrace();
                resp.setStatus(400);
                return;
            }finally {
               try{
                   if(in != null){
                       in.close();
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }

            Enumeration paramEnum = requestTab.elements();
            String reqData = null;
            while (paramEnum.hasMoreElements()){
                mp = (Multipart)paramEnum.nextElement();
                if(REQUEST_STR.equals(mp.getName())){
                    reqData = new String(mp.getContent(), "UTF-8");
                }
            }
            if(reqData == null || reqData.trim().length() <1){
                SendResponseFactory.createSendResponse(RequestContext.getResponse())
                        .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010010, "请求报文为空"));
                return;
            }
            try{
                RequestContext.setRequest(new Request(req));
                RequestContext.setResponse(new Response(resp));
                long st = System.currentTimeMillis();
                process.process(reqData);
                logger.error("total={}", (System.currentTimeMillis() - st));
            }catch (Throwable t){
                SendResponseFactory.createSendResponse(RequestContext.getResponse())
                        .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010011, "服务器发生错误"));
                return;
            }finally {
                RequestContext.removeRequest();
                RequestContext.removeResponse();
                RequestContext.removeJsonObject();
            }
        }catch (Exception e){
            SendResponseFactory.createSendResponse(RequestContext.getResponse())
                    .sendError(MessageUtil.buildNoBodyNoHeadMessage(Constants.ERROR_CODE_010011, "服务器发生错误"));
        }
    }

    @Override
    public void destroy() {
        process.destory();
    }
}
