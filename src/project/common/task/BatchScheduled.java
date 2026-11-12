package project.common.task;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import project.common.bean.DataMap;
import project.common.dao.CommonDAO;

@Service
public class BatchScheduled {

	private static Logger log = Logger.getLogger(BatchScheduled.class);	
	
	@Autowired
	private CommonDAO commonDAO;
	
	//@Scheduled(fixedRate=10000)
	public void tenSecond(){
		//log.debug("tenSecond");
	}
	
	//@Scheduled(cron="0 0 0 * * *")
	public void batchDayCalulate(){
		try {
			SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar sc = Calendar.getInstance();
			String sTime = sdt.format(sc.getTime());
			
			long startTime = System.currentTimeMillis();
			
			//log.debug(">>Start Batch: Day Calulate -> " + sTime);
			
			DataMap map = new DataMap();
			map.setModuleCommand("System", "PRCS_BAT_DAY_CCL");
			commonDAO.getMap(map);
			
			long endTime = System.currentTimeMillis();
			
			//log.debug("[ Execute Time ] " + ((endTime - startTime)/1000.0) + " Second");
			
			SimpleDateFormat edt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar ec = Calendar.getInstance();
			String eTime = edt.format(ec.getTime());
			
			//log.debug("<<End Batch : Day Calulate ->" + eTime);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}