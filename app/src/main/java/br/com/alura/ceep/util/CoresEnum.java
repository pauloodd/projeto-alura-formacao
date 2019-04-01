package br.com.alura.ceep.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CoresEnum {
    AZUL("#408EC9"),BRANCO ("#FFFFFF"), VERMELHO("#EC2F4B"), VERDE("#9ACD32"), AMARELO("#F9F256"), LILAS("#F1CBFF"), CINZA("#D2D4DC"), MARROM("#A47C48"), ROXO("#BE29EC");

    public String hexadecimal;

    CoresEnum(String hex){
        this.hexadecimal = hex;
    }

    public static List<String> todasCores(){
        List<String> todasCores = new ArrayList<String>();
        CoresEnum[] arrCores = CoresEnum.values();
        for(int i = 0; i < arrCores.length; i++){
            todasCores.add(arrCores[i].hexadecimal);
        }
        return todasCores;
    }


}
