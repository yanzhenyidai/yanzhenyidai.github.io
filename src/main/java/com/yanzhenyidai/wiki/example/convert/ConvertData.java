package com.yanzhenyidai.wiki.example.convert;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author frank
 * @version 1.0
 * @date 2020-05-14 15:15
 */
public class ConvertData {
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Admin\\Desktop\\readdata.xlsx");
        InputStream inputStream = new FileInputStream(file);

        Workbook wb = new XSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheetAt(0);

        sheet.getLastRowNum();

        OkHttpClient ok = new OkHttpClient();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {

            String path = "";
            String edocNo = sheet.getRow(i).getCell(4).toString();
            String zipPath = "";
            String url = "http://fsscshpapi.wilmar.cn/ImagesSystemInterface/IInvoiceAttachmentOperation.asmx";
            String name = "";
            String dirPath = "";

            Request request = new Request.Builder()
                    .url("https://fsscshpt.wilmar.cn/sites/ImagesCenter/_layouts/15/ImagesSystem/DisplayAttachment.aspx?ATT=" + sheet.getRow(i).getCell(22).toString())
                    .build();

            Response response = ok.newCall(request).execute();

            InputStream byteStream = response.body().byteStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(byteStream);

            zipPath = "D:/edoc/" + sheet.getRow(i).getCell(4).toString() + ".zip";

            FileCopyUtils.copy(bytes, new File(zipPath));

            ZipFile zf = new ZipFile(zipPath);

            Enumeration en = zf.entries();
            FileOutputStream fos = null;
            InputStream is = null;

            while (en.hasMoreElements()) {
                ZipEntry zn = (ZipEntry) en.nextElement();
                if (!zn.isDirectory()) {
                    is = zf.getInputStream(zn);
                    File f = new File("D:/edoc/" + sheet.getRow(i).getCell(4).toString() + ".zip");
                    File file1 = f.getParentFile();
                    file1.mkdirs();

                    name = zn.getName();

//                    dirPath = DateUt.dateToString(new Date(), DateStyleEnum.YYYYMMDD2) + "/BIZ_AP/" + edocNo;

                    File file2 = new File("D:/edoc/" + dirPath);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }

                    path = "D:/edoc/" + dirPath + "/" + zn.getName();
                    fos = new FileOutputStream(path);

                    int len = 0;
                    byte bufer[] = new byte[2 << 10];
                    while (-1 != (len = is.read(bufer))) {
                        fos.write(bufer, 0, len);
                    }

                    fos.close();
                }
            }

//            NotBarcodeImage notBarcodeImage = new NotBarcodeImage();
//            notBarcodeImage.setInvoiceNum(sheet.getRow(i).getCell(36).toString()); // 发票号码
//            notBarcodeImage.setOperatorFlag(Constant.ThirdInterface.OPT_ADD);
//            notBarcodeImage.setSupplierID("");
//            notBarcodeImage.setInvoiceID(sheet.getRow(i).getCell(37).toString()); // 发票代码
//            notBarcodeImage.setUrl(url);
//
//            ExecuteResult<String> result = PushNew.pushNoBarCode(path, zipPath, edocNo, notBarcodeImage, "", "0");
//
//            System.out.println(JSON.toJSONString(result));
//
            System.out.println("update bill_images set img_url='" + dirPath + "/" + name + "' where id = " + sheet.getRow(i).getCell(0).toString() + ";");
            sheet.getRow(i).createCell(69).setCellValue(dirPath + "/" + name);

        }
    }
}
