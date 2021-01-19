package com.example.currencyconverterv3;

public class EvaluateString {
    static char opts[] = {'+','-','x','÷'};
    static char operator;
    public static Double calculate(String expression){
        double res = 0.0;
        char exp[] = expression.toCharArray();
        int countOpts=0;
        boolean leftVar = true, isOpt;
        String var1 = "", var2 = "";
        for (char c: exp) {
            isOpt = false;
            for (char o: opts) {
                if (o == c) {
                    countOpts++;
                    isOpt = true;
                    leftVar = false;
                    if (countOpts > 1) return -1.0;
                    operator = o;
                }
            }
            if (!isOpt) {
                if (leftVar) var1 += c;
                else var2 += c;
            }
        }

        if (countOpts == 0)
            return string2DoublePls(expression);
        if (expression.indexOf(operator, 0) == 0 || expression.indexOf(operator, 0) == expression.length()-1)
            return -1.0;
        double v1 = string2DoublePls(var1);
        double v2 = string2DoublePls(var2);
        if (v1 == -1.0 || v2 == -1.0) return -1.0;
        switch (operator) {
            case '+':
                res= v1+v2;
                break;
            case '-':
                res= v1-v2;
                break;
            case 'x':
                res= v1*v2;
                break;
            case '÷':
                if (v2 == 0.0) return -1.0;
                res= v1/v2;
        }
        return res;
    }

    private static double string2DoublePls(String var) {
        int countDot=0;
        char[] v=var.toCharArray();
        for (char c: v) {
            if (c == '.') {
                countDot++;
                if (countDot>1) return -1.0;
            }
        }
        if (countDot == 1) {
            int dotIndex = var.indexOf('.',0);
            if (dotIndex == 0 || dotIndex == var.length()-1) return -1.0;
        }
        return Double.parseDouble(var);
    }

}
