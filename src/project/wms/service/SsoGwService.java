package project.wms.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import project.common.airfw.PropertyUtil;
import project.common.airfw.User;
import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.bean.SystemConfig;
import project.common.bean.UriInfo;
import project.common.dao.CommonDAO;
import project.common.service.CommonService;
import project.common.util.FileUtil;
import project.wms.controller.WmsLoginController;

@Service
public class SsoGwService {

	static final Logger log = LogManager.getLogger(SsoGwService.class.getName());
	@Autowired
	public CommonDAO commonDao;

	@Autowired
	private SystemConfig systemConfig;

	@Autowired
	private CommonService commonService;


	public boolean ssoLogin(String sproId, HttpServletRequest request, ModelMap model) throws SQLException {
		HttpSession session = request.getSession();

		String plntky = "";// 플랜트
		DataMap map = new DataMap();
		map.put("USERID", sproId);
		map.setModuleCommand(systemConfig.getCmmonType(), "USERCHECK");
		User user = (User)commonService.getObj(map);
		if (user != null) {
			
			WmsLoginController login = new WmsLoginController();
			
			UriInfo uriInfo = (UriInfo)request.getAttribute(CommonConfig.REQUEST_URI_INFO_KEY);
			
			String pdaModule = uriInfo.getUri()!=null&&!"".equals(uriInfo.getUri().trim())&&uriInfo.getUri().indexOf("/") > -1?uriInfo.getUri().split("/")[1]:"";
			boolean isPda = "mobile".equals(pdaModule)?true:false;
			
			
			login.setLoginUser(session, model, map, user, isPda, commonService, systemConfig.getCmmonType(), "SSO");

			return true;
		} else {
			model.put("data", "N");
			return false;
		}
	}

	public Map post(String strUrl, String jsonMessage){

		Map result = null;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
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


				result = new HashMap();

				String returnJson = sb.toString();

				// returnJson parsing
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(returnJson);
				JSONObject jsonObj = (JSONObject) obj;

				// head parsing
				Map headMap = (Map)jsonObj.get("head");
				String returnCode = Long.toString((Long)headMap.get("returnCode"));
				String returnDesc = (String)headMap.get("returnDesc");

				if (returnCode.equals("0")) {
					// body parsing
					Map bodyMap = (Map)jsonObj.get("body");

					result.put("bodyMap", bodyMap);
				}

				result.put("headMap", headMap);

			} else {
				result.put("errorMap", con.getResponseMessage());
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return result;

	}

	public boolean isSsoEpUser(String id, String pw) {
		try {
			// TODO Auto-generated method stub
			
	        // 인증요청 URL
			String strUrl = PropertyUtil.getProperty("sso.properties", "sso.ep.domain") + "rest/security/authenticate";
			String productId = PropertyUtil.getProperty("sso.properties", "sso.product_id"); //WMS Productid

			// json message 구성
			String sproId = id;
			String password = pw;
			String loginIp = "127.0.0.1"; //request.getRemoteAddr()
			String productName = "xxx"; //request.getHeader("user-agent")
			String application = productId;
			String useEncData = "false";
			String useEncPw = "true"; //패스워드 암호화 없이 보냄

			String jsonMessage = "{";
			jsonMessage += "\"sproId\":\"" + sproId + "\"";
			jsonMessage += ",\"password\":\"" + password + "\"";
			jsonMessage += ",\"loginIp\":\"" + loginIp + "\"";
			jsonMessage += ",\"productId\":\"" + productId + "\"";
			jsonMessage += ",\"productName\":\"" + productName + "\"";
			jsonMessage += ",\"application\":\"" + application + "\"";
			jsonMessage += ",\"useEncPw\":\"" + useEncPw + "\"";
			jsonMessage += ",\"useEncData\":\"" + useEncData + "\"";
			jsonMessage += "}";

			Map m = this.post(strUrl, jsonMessage);
			if (m != null && m.get("bodyMap") != null) {
				return true;
			}
			return false;
		}catch (Exception e) {
			return  false;
		}
	}
	/*
	public static void main(String args[]) {
		SsoGwService s = new SsoGwService();
		boolean m = s.isSsoEpUser("hnw.shyou", "hnw.shyou1");
		System.out.println(m);
	}
	 */
}