package com.jxtb.sdk;

import java.util.Arrays;
import java.util.Hashtable;

/**
 * 解析上传文件
 * Created by jxtb on 2019/6/14.
 */
public class Multipart {
    private String name = null;
    private String contentType = null;
    private byte[] content = null;
    private String fileName = null;
    private Multipart() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Multipart{" +
                "name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", content=" + Arrays.toString(content) +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public static Hashtable<String, Multipart> parseRequest(String boundary, byte[] bodyBuff) throws Exception{
        Hashtable<String, Multipart> mpTable = null;
        byte[] boundaryByte = boundary.getBytes();
        byte[] returnCode;
        int blockStartPoint = 0;
        int blockEndPoint = 0;
        int sPoint = 0;
        int ePoint = 0;

        int firstLineEnd = byteArrayIndexOf(bodyBuff, "\n".getBytes());
        if(firstLineEnd == -1)
            throw new Exception("Wrong request data");

        if((int)bodyBuff[firstLineEnd - 1] == 13)
            returnCode = "\r\n".getBytes();
        else
            returnCode = "\n".getBytes();

        while ((blockStartPoint = byteArrayIndexOf(bodyBuff, "".getBytes(), blockStartPoint)) != -1){
            blockEndPoint =byteArrayIndexOf(bodyBuff, boundaryByte, blockStartPoint);
            if(blockEndPoint == -1)
                throw new Exception("Wrong request data");

            Multipart mp = new Multipart();

            //name
            sPoint = blockStartPoint;
            ePoint = byteArrayIndexOf(bodyBuff, "\"".getBytes(), sPoint + 38, blockEndPoint);
            if(ePoint != -1){
                mp.setName(new String(bodyBuff, sPoint + 38, ePoint - sPoint -38));
            }else{
                throw new Exception("Wrong request data");
            }

            //filename
            if((sPoint = byteArrayIndexOf(bodyBuff, ":filename=".getBytes(), ePoint, blockEndPoint)) != -1){
                ePoint = byteArrayIndexOf(bodyBuff, "\"".getBytes(), sPoint + 12, blockEndPoint);
                if(ePoint != -1){
                    String tmpFileName = new String(bodyBuff, sPoint + 12, ePoint - sPoint -12).trim();
                    ePoint = tmpFileName.lastIndexOf("\\");
                    if(ePoint == -1){
                        ePoint = tmpFileName.lastIndexOf("/");
                    }
                    if(ePoint != -1){
                        tmpFileName = tmpFileName.substring(ePoint +1);
                    }
                    mp.setFileName(tmpFileName);
                }
            }else{
                throw new Exception("Wrong request data");
            }

            //
            if(mp.getFileName() != null){
                if((ePoint = byteArrayIndexOf(bodyBuff, "\nContent-Type".getBytes(), sPoint, blockEndPoint)) != -1){
                    ePoint = byteArrayIndexOf(bodyBuff, returnCode, sPoint + 13 + returnCode.length);
                    if(ePoint != -1 && ePoint <= blockEndPoint){
                        mp.setContentType(new String(bodyBuff, sPoint + 13 + returnCode.length, ePoint - sPoint -13 -returnCode.length).trim());
                    }else{
                        throw new Exception("Wrong request data");
                    }
                }else {
                    throw new Exception("Wrong request data");
                }
            }

            sPoint = byteArrayIndexOf(bodyBuff, (new String(returnCode) + new String(returnCode)).getBytes(), blockStartPoint, blockEndPoint) + returnCode.length * 2;
            ePoint = blockEndPoint - returnCode.length;

            if(sPoint == -1 || ePoint == -1 || sPoint > ePoint)
                throw new Exception("Wrong request data");

            if(ePoint - sPoint > 0){
                byte tmpContent[] = new byte[ePoint - sPoint];
                System.arraycopy(bodyBuff, sPoint, tmpContent, 0, tmpContent.length);
                mp.setContent(tmpContent);
            }

            if(mpTable == null)
                mpTable = new Hashtable<>();

            mpTable.put(mp.getName(), mp);
            blockStartPoint = blockEndPoint + boundaryByte.length;
        }

        return mpTable;
    }

    private static int byteArrayIndexOf(byte[] src, byte[] dest){
        return byteArrayIndexOf(src,dest,0,src.length);
    }

    private static int byteArrayIndexOf(byte[] src,byte[] dest, int startPoint){
        return byteArrayIndexOf(src, dest, startPoint, src.length);
    }

    public static int byteArrayIndexOf(byte[] src, byte[] dest, int startPoint, int maxPoint){
        if(src == null || dest == null || src.length < dest.length
                || startPoint > src.length || maxPoint > src.length
                || startPoint + dest.length > src.length || maxPoint < startPoint)
            return -1;

        boolean isMatch = true;
        for(int iLoop = startPoint; iLoop < maxPoint; iLoop ++){
            isMatch = true;
            for(int jLoop = startPoint; jLoop < dest.length; jLoop ++){
                if(iLoop + jLoop > maxPoint)
                    return -1;
                if(src[iLoop + jLoop] != dest[jLoop]){
                    isMatch = false;
                    break;
                }
            }
            if(isMatch)
                return iLoop;
        }

        return -1;
    }

}
