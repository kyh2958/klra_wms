package project.wms.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import project.wms.service.WmsBatchService;

@Component
public class WmsBatchController {
	
	private static Logger log = Logger.getLogger(WmsBatchController.class);
	
	@Autowired
	private WmsBatchService wmsBatchService;

	//@Scheduled(cron="0 5 8 * * *") 
	//@Scheduled(cron="*/10 * * * * *") 
	public void initMTO() throws Exception {
		log.debug("Batch Start!! [[[ Init MTO ]]]");
		wmsBatchService.initMTO();
	}

}
