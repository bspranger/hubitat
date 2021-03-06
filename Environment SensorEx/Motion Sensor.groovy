metadata {
    definition (name: "Environment Sensor EX", namespace: "iharyadi", author: "iharyadi", ocfDeviceType: "oic.r.temperature") {
        capability "Configuration"
        capability "Refresh"
        capability "Temperature Measurement"
        capability "RelativeHumidityMeasurement"
        capability "Illuminance Measurement"
        capability "PressureMeasurement"
        capability "Switch"
        capability "Sensor"
                
        MapDiagAttributes().each{ k, v -> attribute "$v", "number" }
        
        attribute "BinaryOutput", "BOOLEAN"
        attribute "BinaryInput", "BOOLEAN"
        attribute "AnalogInput", "number"

    	fingerprint profileId: "0104", inClusters: "0000, 0003, 0006, 0402, 0403, 0405, 0400, 0B05, 000F, 000C, 0010, 1001", manufacturer: "KMPCIL", model: "RES001", deviceJoinName: "Environment Sensor"
        fingerprint profileId: "0104", inClusters: "0000, 0003, 0006, 0402, 0403, 0405, 0B05, 000F, 000C, 0010, 1001", manufacturer: "KMPCIL", model: "RES002", deviceJoinName: "Environment Sensor"
    	fingerprint profileId: "0104", inClusters: "0000, 0003, 0006, 0400, 0B05, 000F, 000C, 0010, 1001", manufacturer: "KMPCIL", model: "RES003", deviceJoinName: "Environment Sensor"
    	fingerprint profileId: "0104", inClusters: "0000, 0003, 0006, 0B05, 000F, 000C, 0010, 1001", manufacturer: "KMPCIL", model: "RES004", deviceJoinName: "Environment Sensor"

    }
    
    tiles(scale: 2) {
        multiAttributeTile(name: "temperature", type: "generic", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState "temperature", label: '${currentValue}°',
                        backgroundColors: [
                                [value: 31, color: "#153591"],
                                [value: 44, color: "#1e9cbb"],
                                [value: 59, color: "#90d2a7"],
                                [value: 74, color: "#44b621"],
                                [value: 84, color: "#f1d801"],
                                [value: 95, color: "#d04e00"],
                                [value: 96, color: "#bc2323"]
                        ]
            }
        }
        valueTile("humidity", "device.humidity", inactiveLabel: false, width: 3, height: 2, wordWrap: true) {
            state "humidity", label: 'Humidity ${currentValue}${unit}', unit:"%", defaultState: true
        }
        valueTile("pressure", "device.pressureMeasurement", inactiveLabel: false, width: 3, height: 2, wordWrap: true) {
            state "pressure", label: 'Pressure ${currentValue}${unit}', unit:"kPa", defaultState: true
        }
        
        valueTile("illuminance", "device.illuminance", width:6, height: 2) {
            state "illuminance", label: 'illuminance ${currentValue}${unit}', unit:"Lux", defaultState: true
        }
        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
        }
        
        def tiles_detail = [];
        tiles_detail.add("temperature")
        tiles_detail.add("humidity")
        tiles_detail.add("pressure")
        tiles_detail.add("illuminance")
        MapDiagAttributes().each{ k, v -> valueTile("$v", "device.$v", width: 2, height: 2, wordWrap: true) {
                state "val", label: "$v \n"+'${currentValue}', defaultState: true
            };
            tiles_detail.add(v);
        }
        tiles_detail.add("refresh")
        tiles_detail.add("switch")
                
        main "temperature"        
        details(tiles_detail)        
    }

    preferences {
    
    	section("Environment Sensor")
        {
            input "tempOffset", "decimal", title: "Degrees", description: "Adjust temperature by this many degrees in Celcius",
                  range: "*..*", displayDuringSetup: false
            input "tempFilter", "decimal", title: "Coeficient", description: "Temperature filter between 0.0 and 1.0",
                  range: "0..1", displayDuringSetup: false
            input "humOffset", "decimal", title: "Percent", description: "Adjust humidity by this many percent",
                  range: "*..*", displayDuringSetup: false
            input "illumAdj", "decimal", title: "Factor", description: "Adjust illuminace base on formula illum / Factor", 
                range: "1..*", displayDuringSetup: false
    	}
        
        section("Expansion Sensor")
        {
        	input "enableAnalogInput", "boolean", title: "Analog Input", description: "Enable Analog Input",
              defaultValue: "false", displayDuringSetup: false 
            
            input "childAnalogInput", "text", title: "Analog Input Handler", description: "Analog Input Child Handler",
              displayDuringSetup: false
              
            input "enableBinaryInput", "boolean", title: "Binary Input", description: "Enable Binary Input",
              defaultValue: "false", displayDuringSetup: false
            
            input "childBinaryInput", "string", title: "Binary Input Handler", description: "Binary Input Device Handler",
              displayDuringSetup: false
              
            input "enableBinaryOutput", "boolean", title: "Binary Output", description: "Enable Binary Output",
              defaultValue: "false", displayDuringSetup: false  
            
           	input "childBinaryOutput", "text", title: "Binary Output Handler", description: "Binary Output Child Handler",
              displayDuringSetup: false
        }
    }
}

private def NUMBER_OF_RESETS_ID()
{
    return 0x0000;
}

private def MAC_TX_UCAST_RETRY_ID()
{
    return 0x0104;
}

private def MAC_TX_UCAST_FAIL_ID()
{
    return 0x0105;
}

private def NWK_DECRYPT_FAILURES_ID()
{
    return 0x0115;
}

private def PACKET_VALIDATE_DROP_COUNT_ID()
{
    return 0x011A;
}

private def PARENT_COUNT_ID()
{
    return 0x011D+1;
}

private def CHILD_COUNT_ID()
{
    return 0x011D+2;
}

private def NEIGHBOR_COUNT_ID()
{
    return 0x011D+3;
}

private def LAST_RSSI_ID()
{
    return 0x011D;
}

private def DIAG_CLUSTER_ID()
{
    return 0x0B05;
}

private def TEMPERATURE_CLUSTER_ID()
{
    return 0x0402;
}

private def PRESSURE_CLUSTER_ID()
{
    return 0x0403;
}

private def HUMIDITY_CLUSTER_ID()
{
    return 0x0405;
}

private def ILLUMINANCE_CLUSTER_ID()
{
    return 0x0400;
}

private def SENSOR_VALUE_ATTRIBUTE()
{
    return 0x0000;
}

private def MapDiagAttributes()
{
    def result = [(CHILD_COUNT_ID()):'Children',
        (NEIGHBOR_COUNT_ID()):'Neighbor',
        (NUMBER_OF_RESETS_ID()):'ResetCount',
        (MAC_TX_UCAST_RETRY_ID()):'TXRetry',
        (MAC_TX_UCAST_FAIL_ID()):'TXFail',
        (LAST_RSSI_ID()):'RSSI',
        (NWK_DECRYPT_FAILURES_ID()):'DecryptFailure',
        (PACKET_VALIDATE_DROP_COUNT_ID()):'PacketDrop'] 

    return result;
}

private def createDiagnosticEvent( String attr_name, type, value )
{
    def result = [:]
    result.name = attr_name
    result.translatable = true
    
    def converter = [(DataType.INT8):{int val -> return (byte) val},
    (DataType.INT16):{int val -> return val},
    (DataType.UINT16):{int val -> return (long)val}] 
    
    result.value = converter[zigbee.convertHexToInt(type)]( zigbee.convertHexToInt(value));
    
	result.descriptionText = "${device.displayName} ${result.name} is ${result.value}"

    return createEvent(result)
}

private def parseDiagnosticEvent(def descMap)
{       
    def attr_name = MapDiagAttributes()[descMap.attrInt];
    if(!attr_name)
    {
        return null;
    }
    
    return createDiagnosticEvent(attr_name, descMap.encoding, descMap.value)
}

private def createPressureEvent(float pressure)
{
    def result = [:]
    result.name = "pressureMeasurement"
    result.translatable = true
    result.unit = "kPa"
    result.value = pressure.round(1)
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value} ${result.unit}"
	return result
}

private def parsePressureEvent(def descMap)
{       
    if(zigbee.convertHexToInt(descMap.attrId) != SENSOR_VALUE_ATTRIBUTE())
    {
        return null
    }
    float pressure = (float)zigbee.convertHexToInt(descMap.value) / 10.0
    return createPressureEvent(pressure)
}

private def createHumidityEvent(float humidity)
{
    def result = [:]
    result.name = "humidity"
    result.translatable = true
    result.value = humidity
    result.unit = "%"
    
    if (humOffset) {
        result.value = result.value + humOffset
    }
    
    result.value = result.value.round(2) 
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value} ${result.unit}"
    return result
}
    
private def parseHumidityEvent(def descMap)
{       
    if(zigbee.convertHexToInt(descMap.attrId) != SENSOR_VALUE_ATTRIBUTE())
    {
        return null
    }
    
    float humidity = (float)zigbee.convertHexToInt(descMap.value)/100.0
    return createHumidityEvent(humidity)
}

private def createIlluminanceEvent(int illum)
{
    def result = [:]
    result.name = "illuminance"
    result.translatable = true
    result.unit = "Lux"
    
    if(!illumAdj ||  illumAdj < 1.0)
    {
        if(ilumm == 0)
        {
            result.value = 0.0
        }
        else
        {
            result.value = 10.0 ** (((double) illum / 10000.0) -1.0)
        }
        
    	result.value = result.value.round(2)  
    }
    else
    {
        result.value = ((double)illum / illumAdj).toInteger()
    }
    
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value} ${result.unit}"
    return result
}

private String ilummStringPrefix()
{
    return "illuminance: "
}

def parseIlluminanceEvent(def descMap)
{       
    if(zigbee.convertHexToInt(descMap.attrId) != SENSOR_VALUE_ATTRIBUTE())
    {
        return null
    }
    
    int res =  zigbee.convertHexToInt(descMap.value)
    
    return createIlluminanceEvent(res)
}


private def createBinaryOutputEvent(boolean val)
{
    def result = [:]
    result.name = "BinaryOutput"
    result.translatable = true
    result.value = val ? "true" : "false"
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value}"
    return result
}

def parseBinaryOutputEvent(def descMap)
{     
    def present_value = descMap.attrInt?.equals(0x0055)?
    	descMap.value:
    	descMap.additionalAttrs?.find { item -> item.attrInt?.equals(0x0055)}?.value
    
    if(!present_value)
    {
        return null
    }
        
    return createBinaryOutputEvent(zigbee.convertHexToInt(present_value) > 0)
}

private def createAnalogInputEvent(float value)
{
    def result = [:]
    result.name = "AnalogInput"
    result.translatable = true
    result.value = value
    result.unit = "Volt"
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value}"
    return result
}

private Float ConvertStringIntBitsToFloat(String val)
{
    Long i = Long.parseLong(val, 16);
    Float f = Float.intBitsToFloat(i.intValue());
    return f;
}

def parseAnalogInputEvent(def descMap)
{       
    def adc;
    def vdd;
    
    if(descMap.attrInt?.equals(0x0103))
    {
    	adc = descMap.value
    }
    else if (descMap.attrInt?.equals(0x0104))
    {
        vdd  = descMap.value
    }
    else
    {   
        adc = descMap.additionalAttrs?.find { item -> item.attrInt?.equals(0x0103)}.value
        vdd = descMap.additionalAttrs?.find { item -> item.attrInt?.equals(0x0104)}.value        
	}
    
    if(vdd)
    {
    	state.lastVdd = (((float)zigbee.convertHexToInt(vdd)*3.45)/0x1FFF)
    }   
    
    if(!adc)
    {
    	return null   
    }

    float volt = 0;
    if(state.lastVdd)
    {
   		volt = (zigbee.convertHexToInt(adc) * state.lastVdd)/0x1FFF
    }
    
    return createAnalogInputEvent(volt)
}

private def createBinaryInputEvent(boolean val)
{    
    def result = [:]
    result.name = "BinaryInput"
    result.translatable = true
    result.value = val
    result.descriptionText = "${device.displayName} ${result.name} is ${result.value}"  
    return result
}

def parseBinaryInputEvent(def descMap)
{       
    def value = descMap.attrInt?.equals(0x0055) ? 
        descMap.value : 
    	descMap.additionalAttrs?.find { item -> item.attrInt?.equals(0x0055)}.value
    
    if(!value)
    {
        return null
    }
           
    return createBinaryInputEvent(zigbee.convertHexToInt(value)>0)
}

private def reflectToChild(String childtype, String description)
{
	if(!childtype)
    {
    	return    
    }
    
    def childDevice = getChildDevice("${device.deviceNetworkId}-$childtype")
    
    if(!childDevice)
    {
    	return    
    }
        
    def childEvent = childDevice.parse(description)
    if(!childEvent)
    {
        return
    }
    
    childDevice.sendEvent(childEvent)    
}

def parseCustomEvent(String description)
{
    def event = null
    
    if(description?.startsWith("read attr - raw:"))
    {
        def descMap = zigbee.parseDescriptionAsMap(description)
        if(descMap?.clusterInt == DIAG_CLUSTER_ID())
        {
            event = parseDiagnosticEvent(descMap);
        }
        else if(descMap?.clusterInt == PRESSURE_CLUSTER_ID())
        {
            event = parsePressureEvent(descMap);
        }
        else if(descMap?.clusterInt == HUMIDITY_CLUSTER_ID())
        {
         	event = parseHumidityEvent(descMap); 
        }
        else if(descMap?.clusterInt == ILLUMINANCE_CLUSTER_ID())
        {
         	event = parseIlluminanceEvent(descMap); 
        }
        else if(descMap?.clusterInt == 0x000F)
        {
            event = parseBinaryInputEvent(descMap);
            reflectToChild(childBinaryInput,description)
        }
        else if(descMap?.clusterInt == 0x000C)
        {
        	event = parseAnalogInputEvent(descMap)
            reflectToChild(childAnalogInput,description)
        }
        else if(descMap?.clusterInt == 0x0010)
        {
        	event = parseBinaryOutputEvent(descMap)
            reflectToChild(childBinaryOutput,description)
        }
   }
   return event
}

private String tempStringPrefix()
{
    return "temperature:"
}

private String humidityStringPrefix()
{
    return "humidity:"
}

private def adjustTemp(double val)
{
    if (tempOffset) {
        val = val + tempOffset
    }
        
    if(tempFilter)
    {
    	if(state.tempCelcius)
        {
    		val = tempFilter*val + (1.0-tempFilter)*state.tempCelcius
        }
        state.tempCelcius = val
    }
    
    return zigbee.convertToHexString((int)(val*100),4)
}

private def adjustTempValue(String description)
{    
    if(!description?.startsWith("read attr - raw:"))
    {
        return description   
    }
    
    def descMap = zigbee.parseDescriptionAsMap(description) 
    
    if( descMap.clusterInt != TEMPERATURE_CLUSTER_ID() )
    {  
        return description
    }

    if(descMap.attrInt != SENSOR_VALUE_ATTRIBUTE())
    {
        return description
    }
    
    String newValue = adjustTemp((double) zigbee.convertHexToInt(descMap.value) / 100.00)
    
    return description.replaceAll("value: [0-9A-F]{4}", "value: $newValue")    
 }

// Parse incoming device messages to generate events
def parse(String description) {
    log.debug "description is $description"
    
    
    event = parseCustomEvent(description)
    if(event)
    {
        sendEvent(event)
        return
    }
    
    description = adjustTempValue(description)
    def event = zigbee.getEvent(description)
    if(event)
    {
        sendEvent(event)
        return
    }
    
    log.warn "DID NOT PARSE MESSAGE : $description"
}

def off() {
    zigbee.Off()
}

def on() {
    zigbee.On()
    //zigbee.command(0x1001,0x17,"0102030405")
}

def sendCommandPDelay(data)
{
	return data   
}
    
def sendCommandP(def cmd)
{    
     runIn(0, sendCommandPDelay, [overwrite: false,data: cmd])
}

def command(Integer Cluster, Integer Command, String payload)
{
	return zigbee.command(Cluster,Command,payload)
}

def command(Integer Cluster, Integer Command)
{
	return zigbee.command(Cluster,Command)
}

def readAttribute(Integer Cluster, Integer attributeId, Map additionalParams)
{
	return zigbee.readAttribute(Cluster, attributeId, additionalParams)
}

def readAttribute(Integer Cluster, Integer attributeId)
{
	return zigbee.readAttribute(Cluster, attributeId)
}

def writeAttribute(Integer Cluster, Integer attributeId, 
                   Integer dataType, Integer value, 
                   Map additionalParams)
{
	return zigbee.writeAttribute(Cluster, attributeId, 
                                 dataType, value, 
                                 additionalParams)
}

def writeAttribute(Integer Cluster, Integer attributeId, 
                   Integer dataType, Integer value)
{
	return zigbee.writeAttribute(Cluster, attributeId, 
                                 dataType, value)
}
    
def configureReporting(Integer Cluster,
    Integer attributeId, Integer dataType,
    Integer minReportTime, Integer MaxReportTime,
    Integer reportableChange,
    Map additionalParams)
{
    return zigbee.configureReporting( Cluster,
    	attributeId,  dataType,
     	minReportTime,  MaxReportTime,
    	reportableChange,
    	aditionalParams)
}

def configureReporting(Integer Cluster,
    Integer attributeId, Integer dataType,
    Integer minReportTime, Integer MaxReportTime,
    Integer reportableChange)
{
    return zigbee.configureReporting( Cluster,
    	attributeId,  dataType,
     	minReportTime,  MaxReportTime,
    	reportableChange)
}

def configureReporting(Integer Cluster,
    Integer attributeId, Integer dataType,
    Integer minReportTime, Integer MaxReportTime)
{
    return zigbee.configureReporting( Cluster,
    	attributeId,  dataType,
     	minReportTime,  MaxReportTime)
}

def binaryoutputOff()
{
	zigbee.writeAttribute(0x0010, 0x0055, DataType.BOOLEAN, 0)
}

def binaryoutputOn()
{
    zigbee.writeAttribute(0x0010, 0x0055, DataType.BOOLEAN, 1)
}

private def refreshExpansionSensor()
{
	def cmds = []
    
    def mapExpansionRefresh = [[0x0010,Boolean.parseBoolean(enableBinaryOutput),0x0055],
    	[0x000F,Boolean.parseBoolean(enableBinaryInput),0x0055],
        [0x000C, Boolean.parseBoolean(enableAnalogInput),0x00104],
        [0x000C, Boolean.parseBoolean(enableAnalogInput),0x00103]]
        
    mapExpansionRefresh.findAll { return it[1] }.each{
    	cmds = cmds + zigbee.readAttribute(it[0],it[2])
        }
        
    return cmds
}

private def refreshOnBoardSensor()
{
	def model = device.getDataValue("model")
    
    def cmds = [];
    
     def mapRefresh = ["RES001":[TEMPERATURE_CLUSTER_ID(), HUMIDITY_CLUSTER_ID(), PRESSURE_CLUSTER_ID(),ILLUMINANCE_CLUSTER_ID()],
     	"RES002":[TEMPERATURE_CLUSTER_ID(), HUMIDITY_CLUSTER_ID(), PRESSURE_CLUSTER_ID()],
        "RES003":[ILLUMINANCE_CLUSTER_ID()]]
     
    mapRefresh[model]?.each{
    	cmds = cmds + zigbee.readAttribute(it,SENSOR_VALUE_ATTRIBUTE());
    }
    
    return cmds
}

private def refreshDiagnostic()
{
	def cmds = [];
    MapDiagAttributes().each{ k, v -> cmds +=  zigbee.readAttribute(DIAG_CLUSTER_ID(), k) } 
    return cmds
}

def refresh() {
    log.debug "Refresh"
    state.lastRefreshAt = new Date(now()).format("yyyy-MM-dd HH:mm:ss", location.timeZone)
     
    return refreshOnBoardSensor() + 
    	refreshExpansionSensor() + 
        refreshDiagnostic()    
}

private def reportBME280Parameters()
{
    def reportParameters = [];
    reportParameters = reportParameters + [[TEMPERATURE_CLUSTER_ID(),DataType.INT16, 5, 300, 10]]
    reportParameters = reportParameters + [[HUMIDITY_CLUSTER_ID(),DataType.UINT16, 5, 301, 100]]
    reportParameters = reportParameters + [[PRESSURE_CLUSTER_ID(),DataType.UINT16, 5, 302, 2]]
    return reportParameters
}

private def reportTEMT6000Parameters()
{
    def reportParameters = [];
    reportParameters = reportParameters + [[ILLUMINANCE_CLUSTER_ID(),DataType.UINT16, 5, 303, 500]]
    return reportParameters
}

def configure() {

    log.debug "Configuring Reporting and Bindings."
    state.remove("tempCelcius")
    
    def mapConfigure = ["RES001":reportBME280Parameters()+reportTEMT6000Parameters(),
    	"RES002":reportBME280Parameters(),
        "RES003":reportTEMT6000Parameters()]
    
    def model = device.getDataValue("model")
    
    def cmds = [];
    mapConfigure[model]?.each{
    	cmds = cmds + zigbee.configureReporting(it[0], SENSOR_VALUE_ATTRIBUTE(), it[1],it[2],it[3],it[4])
    }
    
    cmds = cmds + refresh();
 
    return cmds
}

private def createChild(String childDH, String component)
{    
    if(!childDH)
    {
        return null
    }
    
    def childDevice = getChildDevice("${device.deviceNetworkId}-$childDH")
    if(!childDevice)
    {
        childDevice = addChildDevice("iharyadi", 
                       "$childDH", 
                       "${device.deviceNetworkId}-$childDH",
                       [label: "${device.displayName} $childDH",
                        isComponent: false, 
                        componentName: component, 
                        componentLabel: "${device.displayName} $childDH"])
    }
    
    return childDevice.configure_child()
}

private updateExpansionSensorSetting()
{    
    def cmds = []
    
    boolean bBinaryOutput = Boolean.parseBoolean(enableBinaryOutput)
    boolean bBinaryInput = Boolean.parseBoolean(enableBinaryInput)
    boolean bAnalogInput = Boolean.parseBoolean(enableAnalogInput)
    
    def mapExpansionEnable = [[0x0010,bBinaryOutput,DataType.BOOLEAN,0x0055],
    	[0x000F,bBinaryInput,DataType.BOOLEAN,0x0055],
        [0x000C, bAnalogInput,DataType.UINT16,0x0103]]
        
    mapExpansionEnable.each{ 
    	cmds = cmds + zigbee.writeAttribute(it[0], 0x0051, DataType.BOOLEAN, it[1]?1:0)
        if(!it[1])
        {
        	cmds = cmds + zigbee.configureReporting(it[0], it[3], it[2], 0xFFFF, 0xFFFF,1)
        }
    }
    
    def mapExpansionChildrenCreate = [[bBinaryOutput,childBinaryOutput,"BinaryOutput"],
    	[bBinaryInput,childBinaryInput,"BinaryInput"],
        [bAnalogInput,childAnalogInput,"AnalogInput"]]

    mapExpansionChildrenCreate.findAll{return (it[0] && it[1])}.each{
    	cmds = cmds + createChild(it[1],it[2])
    }
    
    return cmds
}

def updated() {
    log.debug "updated():"

    if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 2000) {
        state.updatedLastRanAt = now()
        state.remove("tempCelcius")
        return updateExpansionSensorSetting() + refresh()
    }
    else {
        log.trace "updated(): Ran within last 2 seconds so aborting."
    }
}