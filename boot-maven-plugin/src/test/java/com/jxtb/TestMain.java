package com.jxtb;

/**
 * Created by jxtb on 2019/7/10.
 */
public class TestMain {

    public static void main(String args[]){
        int count=0;
        for(int m=0;m<28;m++){
            for(int i=0;i<=9;i++)
                for(int j=0;j<=9;j++)
                    for(int k=0;k<=9;k++)
                        if(m==k+j+i){
                            count++;
                        }

            System.out.println(m+"的个数是:"+count);
            float gl= (float)count;
            System.out.println(m+"的概率是："+(gl/10)+"%");
            count=0;}


    }

}
