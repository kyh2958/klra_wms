Wms = function() {
	this.$inputObj;
	this.searchOpenGridId;;
	this.param;
	this.popData;
};

Wms.prototype = {
	getDocSeq : function(docuty) {
		var param = new DataMap();
		param.put("DOCUTY", docuty);
		
		var json = netUtil.sendData({
			module : "Wms",
			command : "FN_DOCUSEQ",
			sendType : "map",
			param : param
		});
		
		return json.data.DOCSEQ;
	},	
	getNumSeq : function(numobj) {
		var param = new DataMap();
		param.put("NUMOBJ", numobj);
		
		var json = netUtil.sendData({
			module : "Wms",
			command : "FN_NROBJ",
			sendType : "map",
			param : param
		});
		
		return json.data.NUMSEQ;
	},
	toString : function() {
		return "Wms";
	}
};

var wms = new Wms();