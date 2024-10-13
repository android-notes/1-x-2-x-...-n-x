public class Test {
    private static boolean isDebug = false;

    public static void main(String[] args) {

        for (int i = 1; i < 30; i++) {
            System.out.println("\n### x = " + i);
            Enpx(i);
        }


//        for (long x = 1; x < 1000; x += 2) {
//            System.out.println();
//            System.out.println(x);
//            double yb = x + 1;
//            double ya = x * x / -6f - x / 3f - 0.5;
//            System.out.println(yb + "  " + ya);
//
//            double sum = (x + 1) * (x + 1) / 4f * yb + (x + 1) / 2f * ya;
//
//            System.out.println(sum);
//        }

    }

//   b: x次幂--x-1次函数--x对点---x行x+1列数组
//   a: x次幂--x次函数--x+1对点---x+1行x+2列数组

    /**
     * #1
     * 4  2 0  y1
     * 9  3 0  y2
     * 16 4 0  y3
     * <p>
     * <p>
     * 5 1 0  y2-y1
     * 7 1 0  y3-y2
     * <p>
     * 2 0 0  y3-y2-y2+y1
     * <p>
     * <p>
     * #2
     * 0   2   4   8    y1
     * 0   3   9   27   y2
     * 0   4  16   64   y3
     * 0   5  25   125  y4
     * <p>
     * <p>
     * 0   1  5  19   y2-y1
     * 0   1  7  37   y3-y2
     * 0   1  9  61   y4-y3
     * <p>
     * <p>
     * 0   0  2   18  y3-y2-y2+y1
     * 0   0  2   24  y4-y3-y3+y2
     * <p>
     * <p>
     * 0   0  0    6  y4-y3-y3+y2-y3+y2+y2-y1
     * <p>
     * <p>
     * ##
     *
     * @param power
     */
    private static void Enpx(int power) {

        BigNumber[] y = new BigNumber[power + 3];
        for (int i = 1; i < y.length; i++) {
            y[i] = new BigNumber(i).pow(power);
        }

        BigNumber[] b = new BigNumber[power + 3];
        BigNumber[] a = new BigNumber[power + 3];
        for (int i = 3; i < power + 4; i++) {
            // x = 1,2,3...i-1
            BigNumber x_avg = new BigNumber(1 + (i - 1)).multiply(i - 1).divide(2).divide(i - 1);
            BigNumber y_avg = avg(y, 1, i);
            b[i - 1] = b(1, i, y, x_avg, y_avg);
            a[i - 1] = y_avg.subtract(b[i - 1].multiply(x_avg));//a尖
        }
//        System.out.println("\n-------" + power);
//        for (int i = 3; i < power + 4; i++) {
//            System.out.print(b[i-1] + ",");
//        }
//        System.out.println();
//        for (int i = 3; i < power + 4; i++) {
//            System.out.print(a[i-1] + ",");
//        }
//        if (true) {
//            return;
//        }
        BigNumber[][] bDeterminant = new BigNumber[power][power + 1];
        determinant(power, b, bDeterminant);
        printDeterminant(bDeterminant);

        BigNumber[][] aDeterminant = new BigNumber[power + 1][power + 2];
        determinant(power + 1, a, aDeterminant);
        printDeterminant(aDeterminant);


        simplifiedeterminant(power, bDeterminant);
        printDeterminant(bDeterminant);
        simplifiedeterminant(power + 1, aDeterminant);
        printDeterminant(aDeterminant);

        BigNumber[] yb = new BigNumber[power];
        y(power, bDeterminant, yb);//求Yb系数
        BigNumber[] ya = new BigNumber[power + 1];
        y(power + 1, aDeterminant, ya);//求Ya系数

        BigNumber[] coefficients = new BigNumber[power + 2];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = new BigNumber(0);
        }
        System.out.print("Yb = ");
        for (int i = 0; i < yb.length - 1; i++) {
            String v = yb[i] + "*n^" + (power - i - 1) + " + ";
            System.out.print(v);
            coefficients[power - i - 1 + 2] = coefficients[power - i - 1 + 2].add(yb[i].divide(2));
            coefficients[power - i - 1 + 1] = coefficients[power - i - 1 + 1].add(yb[i].divide(2));
        }
        System.out.println(yb[yb.length - 1]);
        coefficients[2] = coefficients[2].add(yb[yb.length - 1].divide(2));
        coefficients[1] = coefficients[1].add(yb[yb.length - 1].divide(2));

        System.out.print("Ya = ");
        for (int i = 0; i < ya.length - 1; i++) {
            String v = ya[i] + "*n^" + (power - i) + " + ";
            System.out.print(v);
            coefficients[power - i + 1] = coefficients[power - i + 1].add(ya[i]);
        }
        System.out.println(ya[ya.length - 1]);
        coefficients[1] = coefficients[1].add(ya[ya.length - 1]);

        System.out.println("1^" + power + " + 2^" + power + " + ... + n^" + power + " = Yb * ( ( 1 + n ) * n / 2 ) + Ya * n");
        System.out.print("= ");
        for (int i = coefficients.length - 1; i > 0; i--) {
            System.out.print(coefficients[i] + "*n^" + i + " + ");
        }
        System.out.println(coefficients[0]);

        check(power, ya, yb, coefficients);
    }

    private static void check(int power, BigNumber[] ya, BigNumber[] yb, BigNumber[] coefficients) {
        long n = 100000;
        System.out.println("\ncheck n=" + n + "  power=" + power);
        BigNumber number1 = new BigNumber(0);
        for (int i = 1; i <= n; i++) {
            number1 = number1.add(new BigNumber(i).pow(power));
        }
        System.out.println("sum " + number1);


        BigNumber numberYb = new BigNumber(0);
        for (int i = 0; i < yb.length; i++) {
            numberYb = numberYb.add(yb[i].multiply(new BigNumber(n).pow(power - i - 1)));
//          yb[i] + "*n^" + (power - i - 1) + " + ";
        }
        numberYb = numberYb.multiply(new BigNumber(1).add(n).multiply(n).divide(2));

        BigNumber numberYa = new BigNumber(0);
        for (int i = 0; i < ya.length; i++) {
            numberYa = numberYa.add(ya[i].multiply(new BigNumber(n).pow(power - i)));
//            String v = ya[i] + "*n^" + (power - i) + " + ";
        }
        numberYa = numberYa.multiply(n);

        BigNumber number2 = numberYb.add(numberYa);

        if (!number1.equals(number2)) {
            System.out.println("sum2 " + number2);
            throw new RuntimeException();
        }

        BigNumber sum = new BigNumber(0);
        BigNumber v = new BigNumber(n);
        for (int i = 1; i < coefficients.length; i++) {
            sum = sum.add(coefficients[i].multiply(v));
            v = v.multiply(n);
        }
        if (!number1.equals(sum)) {
            System.out.println("sum3 " + sum);
            throw new RuntimeException();
        }
    }

    private static void y(int power, BigNumber[][] bDeterminant, BigNumber[] y) {
        for (int i = 0; i < power; i++) {
            for (int j = 0; j < i; j++) {
                bDeterminant[i][power] = bDeterminant[i][power].subtract(bDeterminant[i][j].multiply(y[j]));
            }
            if (bDeterminant[i][i].equals(new BigNumber(0))) {
                y[i] = bDeterminant[i][power];
            } else {
                y[i] = bDeterminant[i][power].divide(bDeterminant[i][i]);
            }

        }
    }

    /**
     * 化简行列式，逐行相减
     *
     * @param power
     * @param bDeterminant
     */
    private static void simplifiedeterminant(int power, BigNumber[][] bDeterminant) {
        for (int i = power - 1; i > -1; i--) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k <= power; k++) {
                    bDeterminant[j][k] = bDeterminant[j + 1][k].subtract(bDeterminant[j][k]);
                }
            }
        }
    }

    private static void printDeterminant(BigNumber[][] determinant) {

        if (isDebug == false) {
            return;
        }
        System.out.println("# determinant");
        for (int i = 0; i < determinant.length; i++) {
            for (int j = 0; j < determinant[i].length; j++) {
                System.out.print(String.format("%12s", determinant[i][j].toString()));
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * 构造行列式
     * 例如：
     * 4  2 0  y1
     * 9  3 0  y2
     * 16 4 0  y3
     *
     * @param power
     * @param b
     * @param determinant
     */
    private static void determinant(int power, BigNumber[] b, BigNumber[][] determinant) {
        for (int i = 0; i < power; i++) {
            for (int j = power; j > -1; j--) {
                if (j == power) {
                    determinant[i][j] = b[i + 2];//yn
                    continue;
                }
                if (j == power - 1) {
                    determinant[i][j] = new BigNumber(0);//倒数第二个始终是0
                    continue;
                }
                if (j == power - 2) {
                    determinant[i][j] = new BigNumber(i + 2);//倒数第三个始终是i+2
                    continue;
                }
                determinant[i][j] = determinant[i][j + 1].multiply(i + 2);//累乘
            }
        }
    }

    /**
     * 求b尖
     */
    private static BigNumber b(int startIndex, int endIndex, BigNumber[] y, BigNumber x_avg, BigNumber y_avg) {
        BigNumber molecular = new BigNumber(0);
        for (int i = startIndex; i < endIndex; i++) {
            molecular = molecular.add(y[i].multiply(i));
        }
        molecular = molecular.subtract(x_avg.multiply(y_avg).multiply(endIndex - startIndex));

        BigNumber denominator = new BigNumber(0);
        for (int i = startIndex; i < endIndex; i++) {
            denominator = denominator.add(new BigNumber(i).pow(2));
        }
        denominator = denominator.subtract(x_avg.pow(2).multiply(endIndex - startIndex));

        return molecular.divide(denominator);
    }

    private static BigNumber avg(BigNumber[] y, int startIndex, int endIndex) {
        BigNumber number = new BigNumber(0);
        for (int i = startIndex; i < endIndex; i++) {
            number = number.add(y[i]);
        }
        return number.divide(endIndex - startIndex);
    }


}
