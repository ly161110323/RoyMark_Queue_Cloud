package com.roymark.queue.util.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {
    private static final Logger logger = LogManager.getLogger(ExcelUtil.class);

    /***
     * 导出excel时在web目录下生成excel
     * @param request
     * @param areaLs 所属大厅
     * @param excelType excel所属模块名
     * @param prefixName excel文件前缀名，一般为模块名比如问卷调查
     * @return
     * @throws Exception
     */
    public static Map<String,String> generateExcel(HttpServletRequest request, Integer areaLs, String excelType, String prefixName) throws Exception {
        Map<String,String> resultMap=new HashMap<String,String>();
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String strNow = dtFormat.format(now);
        String fullFileName=prefixName+strNow+".xlsx";
        String filePath = request.getSession().getServletContext().getRealPath("")+"\\RemoteQueue\\" + areaLs + "\\report\\"+excelType+"\\";
        String returnName="/RemoteQueue/"+ areaLs + "/report/"+excelType+"/"+fullFileName;
        resultMap.put("urlname",returnName);//下载url路径文件名
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String physicalName=filePath+fullFileName;
        resultMap.put("physicalname",physicalName);//物理文件名
        return resultMap;
    }
}
