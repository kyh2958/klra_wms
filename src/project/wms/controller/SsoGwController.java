package project.wms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.airfw.PropertyUtil;
import project.common.controller.BaseController;
import project.common.util.FileUtil;
import project.wms.service.SsoGwService;

@Controller
public class SsoGwController extends BaseController {

    private static Logger log = Logger.getLogger(SsoGwController.class);
    
    @Autowired
    SsoGwService ssoGwService;

    /**
     * ssoLogin
     *
     * @return String
     * @throws Exception
     */

    @RequestMapping("/sso/assertionConsumer")
    public String ssoLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String rtnUrl = "common/error";

        // 인증요청 URL
		String strUrl = PropertyUtil.getProperty("sso.properties", "sso.ep.domain") + "rest/security/encValidateProduct";

        // json message 구성
        String productId = PropertyUtil.getProperty("sso.properties", "sso.product_id"); //WMS Productid
        String encData = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("encData")) {
                    encData = cookies[i].getValue();
                }
            }
        }


        if (cookies == null || productId == null || productId.length() == 0 || encData == null || encData.length() == 0) {
            //rtnUrl = "/common/ssoLogin"; //일반 로그인 화면
        	rtnUrl = "redirect:/sso/ssoLogin.page";
        } else {

            String jsonMessage = "{";
            jsonMessage += "\"productId\":\"" + productId + "\"";
            jsonMessage += ",\"encData\":\"" + encData + "\"";
            jsonMessage += "}";

            try {
                URL url = new URL(strUrl);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
                con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

                con.setRequestMethod("POST");

                //json으로 message를 전달하고자 할 때
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoInput(true);
                con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(jsonMessage); //json 형식의 message 전달
                wr.flush();

                StringBuilder sb = new StringBuilder();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    //Stream을 처리해줘야 하는 귀찮음이 있음.
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();

                    String returnJson = sb.toString();

                    // returnJson parsing
                    JSONObject jsonObj = new JSONObject(returnJson);

                    JSONObject jsonHead = new JSONObject(jsonObj.get("head").toString());
                    String returnCode = String.valueOf(jsonHead.get("returnCode"));
                    String returnDesc = String.valueOf(jsonHead.get("returnDesc"));
                    String sproId = "";
                    JSONObject jsonBody = new JSONObject(jsonObj.get("body").toString());
                    if (returnCode.equals("0")) {
                        sproId = String.valueOf(jsonBody.get("sproId"));
                        boolean isOk = ssoGwService.ssoLogin(sproId, request, model);
                        rtnUrl = "redirect:/main.page";
                        if (!isOk) {
                            model.addAttribute("sproId", String.valueOf(jsonBody.get("sproId")));
                            model.addAttribute("isTrySso", "Y");
                            rtnUrl = "forward:/wms/index.page";
                        }
                        ;
                    } else {
                        model.addAttribute("sproId", String.valueOf(jsonBody.get("sproId")));
                        model.addAttribute("isTrySso", "Y");
                        rtnUrl = "forward:/wms/index.page";
                    }
                } else {
                    System.out.println(con.getResponseCode());
                    rtnUrl = "redirect:/sso/ssoLogin.page";
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return rtnUrl;
    }

    // legacy -> SSO 로그인 전달요청
    @RequestMapping("/sso/ssoLoginProduct.page")
    public String ssoLoginProduct(HttpServletRequest request, ModelMap model) {
		initSSO(request, model);
    	model.addAttribute("sso_process", "loginProduct.do");
        return "/common/ssoLoginProduct";
    }

    // legacy -> SSO 로그인 요청
    @RequestMapping("/sso/ssoLogin.page")
    public String ssoLogin(HttpServletRequest request, ModelMap model) {
    	initSSO(request, model);
    	model.addAttribute("sso_process", "loginForm.do");
        return "/common/ssoProcess";
    }

    // sso -> legacy 로그아웃 요청
    @RequestMapping("/sso/ssoLogout.page")
    public String ssoLogout(HttpServletRequest request, ModelMap model) {
        return "forward:/wms/json/logout.page";
    }

    public void initSSO(HttpServletRequest request, ModelMap model)	{
		model.addAttribute("sso_domain", PropertyUtil.getProperty("sso.properties", "sso.ep.domain"));
		model.addAttribute("entityId", PropertyUtil.getProperty("sso.properties", "sso.wms.entityId"));
		model.addAttribute("recall_url", PropertyUtil.getProperty("sso.properties", "sso.wms.url"));
    }

}