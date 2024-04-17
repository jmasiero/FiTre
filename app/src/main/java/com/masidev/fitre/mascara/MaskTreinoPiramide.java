package com.masidev.fitre.mascara;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MaskTreinoPiramide {
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[/]", "")
                .replaceAll("[(]", "").replaceAll("[)]", "")
                .replaceAll("[+]", "").replaceAll("[,]", "")
                .replaceAll("[;]", "").replaceAll("[N]", "")
                .replaceAll("[*]", "").replaceAll("[#]", "")
                .replaceAll(" ", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            String old = "";
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String str = retornaNumeroOuIfem(s.toString());
                String mascara = "";
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                ediTxt.removeTextChangedListener(this);
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
                old = str;
                ediTxt.addTextChangedListener(this);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }

    public static String retornaNumeroOuIfem(String s){
        char retorno;
        char[] arrChar = s.toCharArray();
        int tamanho = arrChar.length;
        if(arrChar.length > 0) {
            char item = arrChar[tamanho - 1];

            switch (item) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    retorno = item;
                    break;
                default:
                    retorno = '-';
                    break;
            }
            arrChar[tamanho-1] = retorno;
        }
        return String.valueOf(arrChar);
    }
}