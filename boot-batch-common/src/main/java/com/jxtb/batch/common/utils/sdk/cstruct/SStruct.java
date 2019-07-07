package com.jxtb.batch.common.utils.sdk.cstruct;

import com.jxtb.batch.common.utils.sdk.annotation.CBinaryInt;
import com.jxtb.batch.common.utils.sdk.annotation.CChar;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基于分隔符的处理
 * Created by jxtb on 2019/7/3.
 */
public class SStruct<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<Field> fields = new ArrayList<>();
    private String charset = "UTF-8";
    private String seperator = "@@";
    private Class<T> clazz;
    private int byteLength;

    public SStruct(Class<T> clazz){
        this(clazz,"UTF-8");
    }

    public SStruct(Class<T> clazz, String charset){
        this(clazz,"UTF-8","@@");
    }

    public SStruct(Class<T> clazz, String charset, String seperator){
        this.clazz = clazz;
        Collections.addAll(this.fields, clazz.getFields()); //缓存一下
        this.charset = charset;
        this.seperator = seperator;
        byteLength = calcByteLength();
        //排序字段
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                int order1 = getOrder(o1);
                int order2 = getOrder(o2);
                //这段复制自Integer.compareTo
                return (order1 < order2 ? -1 : (order1 == order2 ? 0 : 1));
            }
            private int getOrder(Field field){
                CChar cc = field.getAnnotation(CChar.class);
                if(cc != null)
                    return cc.order();
                CBinaryInt cbi = field.getAnnotation(CBinaryInt.class);
                if(cbi != null)
                    return cbi.order();
                return 0;
            }
        });
    }

    /**
     * 计算整个结构体长度
     * @return
     */
    private int calcByteLength() {
        int sum = 0;
        for(Field field : fields){
            if((field.getModifiers() & Modifier.STATIC )> 0)
                continue;
            CChar annoCChar = field.getAnnotation(CChar.class);
            CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);
            if(annoCChar != null){
                sum += annoCChar.value();
            }else if(annoInt != null){
                sum += annoInt.length();
            }
        }
        return sum;
    }

    /**
     * 生成数据缓存，并且额外增加指定字节数
     */
    public void writeByBuffer(T source, ByteBuffer buffer){
        assert source != null;
        try{
            for(Field field : fields){
                if((field.getModifiers() & Modifier.STATIC) > 0)
                    continue;
                Class<?> type = field.getType();
                Object value = field.get(source);
                CChar annoCChar = field.getAnnotation(CChar.class);
                CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);
                if(annoCChar != null){
                    String out;
                    //按字段类型分别出来，优先处理null以及有formatPattern的字段
                    if(value == null){
                        out = "";
                    }else if(StringUtils.isNotBlank(annoCChar.formatPattern())){ //优先考虑pattern,有pattern就使用pattern来格式化了
                        out = MessageFormat.format(annoCChar.formatPattern(), value);
                    }else if(type.equals(Date.class)){ //日期类型
                        if(StringUtils.isNotBlank(annoCChar.datePattern())){ //有日期版
                            SimpleDateFormat sdf = new SimpleDateFormat(annoCChar.datePattern());
                            out = sdf.format((Date)value);
                        }else{
                            out = value.toString(); //直接toString(),机会肯定是不要的格式
                            logger.warn("使用toString格式化Date类型字段[{}/{}](是否漏加了datePattern属性？)", clazz.getCanonicalName(), field.getName());
                        }
                    }else if(type.equals(String.class)){ //字符串
                        out = (String)value;
                    }else if(Number.class.isAssignableFrom(type) || type == int.class || type == long.class){ //数字
                        //转成字符串
                        //记录符合，只有附属才在最左边加符号
                        String sign = "";
                        if(type.equals(BigDecimal.class)){
                            //不能出小数点
                            BigDecimal bd = (BigDecimal)value;
                            bd = bd.setScale(annoCChar.precision(), annoCChar.rounding());
                            if(bd.signum() == -1){
                                //负数
                                sign = "-";
                                bd = bd.abs();
                            }
                            out = MessageFormat.format("{0,number,0}", bd.unscaledValue());
                        }else{
                            //其它类型转成long型处理
                            long num = ((Number)value).longValue();
                            //用long型来判负
                            if(num <0){
                                sign = "-";
                                num = -num;
                            }
                            out = MessageFormat.format("{0,number,0}", num);
                        }
                        //补完0后再拼符号
                        if(annoCChar.zeroPadding()){
                            //这里由于都是数字，所以长度于字节数一样，可以直接leftPad
                            out = StringUtils.leftPad(out, annoCChar.value() - sign.length(), "0");
                        }
                        out = sign + out;
                    }else{
                        //其它类型就直接toString了
                        out = value.toString();
                    }
                    out = out + seperator;
                    byte bytes[] = out.getBytes(charset);
                    buffer.put(bytes);
                }else if(annoInt != null){
                    assert  value instanceof  Number;
                    assert annoInt.length() > -1 && annoInt.length() <= 8 : "二进制长度必须在1到8之间";
                    long l = ((Number)value).longValue();
                    byte bytes[] = new byte[annoInt.length()];
                    for(int i = 0; i < bytes.length; i ++)
                        bytes[i] = (byte)(l & 0xff);
                    if(annoInt.bigEndian())
                        for (int i = bytes.length - 1; i >= 0; i--)
                            buffer.put(bytes[i]);
                    else
                        buffer.put(bytes);
                    buffer.put(seperator.getBytes());
                }else{
                    assert false : field.getName() + "必须制定字段类型注释";
                }
            }
        }catch (Exception e){
            logger.error("结构体解析出错：" + clazz.getCanonicalName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 生成数据缓存，并且额外增加指定字节数
     */
    public void writeSperatorByBuffer(T source, ByteBuf buffer){
        assert source != null;
        try{
            int fieldLen = fields.size();
            int index = 0;
            for(Field field : fields){
                index ++;
                if((field.getModifiers() & Modifier.STATIC) > 0)
                    continue;
                Class<?> type = field.getType();
                Object value = field.get(source);
                CChar annoCChar = field.getAnnotation(CChar.class);
                CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);
                if(annoCChar != null){
                    String out;
                    //按字段类型分别出来，优先处理null以及有formatPattern的字段
                    if(value == null){
                        out = "";
                    }else if(StringUtils.isNotBlank(annoCChar.formatPattern())){ //优先考虑pattern,有pattern就使用pattern来格式化了
                        out = MessageFormat.format(annoCChar.formatPattern(), value);
                    }else if(type.equals(Date.class)){ //日期类型
                        if(StringUtils.isNotBlank(annoCChar.datePattern())){ //有日期版
                            SimpleDateFormat sdf = new SimpleDateFormat(annoCChar.datePattern());
                            out = sdf.format((Date)value);
                        }else{
                            out = value.toString(); //直接toString(),机会肯定是不要的格式
                            logger.warn("使用toString格式化Date类型字段[{}/{}](是否漏加了datePattern属性？)", clazz.getCanonicalName(), field.getName());
                        }
                    }else if(type.equals(String.class)){ //字符串
                        out = (String)value;
                    }else if(Number.class.isAssignableFrom(type) || type == int.class || type == long.class){ //数字
                        //转成字符串
                        //记录符合，只有附属才在最左边加符号
                        String sign = "";
                        if(type.equals(BigDecimal.class)){
                            //不能出小数点
                            BigDecimal bd = (BigDecimal)value;
                            bd = bd.setScale(annoCChar.precision(), annoCChar.rounding());
                            if(bd.signum() == -1){
                                //负数
                                sign = "-";
                                bd = bd.abs();
                            }
                            out = MessageFormat.format("{0,number,0}", bd.unscaledValue());
                        }else{
                            //其它类型转成long型处理
                            long num = ((Number)value).longValue();
                            //用long型来判负
                            if(num <0){
                                sign = "-";
                                num = -num;
                            }
                            out = MessageFormat.format("{0,number,0}", num);
                        }
                        //补完0后再拼符号
                        if(annoCChar.zeroPadding()){
                            //这里由于都是数字，所以长度于字节数一样，可以直接leftPad
                            out = StringUtils.leftPad(out, annoCChar.value() - sign.length(), "0");
                        }
                        out = sign + out;
                    }else{
                        //其它类型就直接toString了
                        out = value.toString();
                    }
                    if(index < fieldLen){
                        out = out + seperator;
                    }
                    byte bytes[] = out.getBytes(charset);
                    buffer.writeBytes(bytes);
                }else if(annoInt != null){
                    assert  value instanceof  Number;
                    assert annoInt.length() > -1 && annoInt.length() <= 8 : "二进制长度必须在1到8之间";
                    long l = ((Number)value).longValue();
                    byte bytes[] = new byte[annoInt.length()];
                    for(int i = 0; i < bytes.length; i ++)
                        bytes[i] = (byte)(l & 0xff);
                    if(annoInt.bigEndian())
                        for (int i = bytes.length - 1; i >= 0; i--)
                            buffer.writeByte(bytes[i]);
                    else
                        buffer.writeBytes(bytes);
                    if(index < fieldLen){
                        buffer.writeBytes(seperator.getBytes());
                    }
                }else{
                    assert false : field.getName() + "必须制定字段类型注释";
                }
            }
        }catch (Exception e){
            logger.error("结构体解析出错：" + clazz.getCanonicalName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    public String[] readValue(ByteBuffer buffer, ByteCode byteCode){
        int start = buffer.position();
        while (buffer.get() != '\n'){
            ;
        }
        int now = buffer.position();
        int len = now - start;
        buffer.position(start);
        if(byteCode != null){
            byteCode.bytes = len;
        }
        byte buf[] = new byte[len - 1];
        buffer.get(buf);
        String line = null;
        try {
            line = new String(buf, charset);
        } catch (UnsupportedEncodingException e) {
            ;
        }
        return line.split(seperator, -1);
    }

}
