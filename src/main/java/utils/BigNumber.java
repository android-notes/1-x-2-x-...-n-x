package utils;

import java.math.BigInteger;

public class BigNumber implements Cloneable {
    final BigInteger mMolecular; //分子
    final BigInteger mDenominator;//分母


    public BigNumber(long number) {
        this(String.valueOf(number));
    }

    public BigNumber(String intNumber) {
        this(intNumber, "1");
    }

    public BigNumber(String intMolecular, String intDenominator) {
        BigInteger m = new BigInteger(intMolecular);
        BigInteger d = new BigInteger(intDenominator);
        if (d.equals(new BigInteger("0"))) {
            throw new IllegalArgumentException("Denominator can not be zero !");
        }
        BigInteger gcd = m.gcd(d);
        mMolecular = m.divide(gcd);
        mDenominator = d.divide(gcd);
    }


    public BigNumber add(long number) {
        return clone().add(new BigNumber(String.valueOf(number), "1"));
    }

    public BigNumber subtract(long number) {
        return clone().subtract(new BigNumber(String.valueOf(number), "1"));
    }

    public BigNumber multiply(long number) {
        return clone().multiply(new BigNumber(String.valueOf(number), "1"));
    }

    public BigNumber divide(long number) {
        if (number == 0) {
            throw new IllegalArgumentException("Denominator can not be zero !");
        }
        return clone().divide(new BigNumber(String.valueOf(number), "1"));
    }

    public BigNumber add(BigNumber number) {
        BigInteger molecular = mDenominator.multiply(number.mMolecular).add(mMolecular.multiply(number.mDenominator));
        BigInteger denominator = mDenominator.multiply(number.mDenominator);
        return simplification(molecular, denominator);
    }


    public BigNumber subtract(BigNumber number) {
        BigInteger molecular = mMolecular.multiply(number.mDenominator).subtract(mDenominator.multiply(number.mMolecular));
        BigInteger denominator = mDenominator.multiply(number.mDenominator);
        return simplification(molecular, denominator);
    }

    public BigNumber multiply(BigNumber number) {
        BigInteger molecular = mMolecular.multiply(number.mMolecular);
        BigInteger denominator = mDenominator.multiply(number.mDenominator);
        return simplification(molecular, denominator);
    }


    public BigNumber divide(BigNumber number) {
        if (number.equals(new BigNumber(0))) {
            throw new IllegalArgumentException("Denominator can not be zero !");
        }
        return multiply(new BigNumber(number.mDenominator.toString(), number.mMolecular.toString()));
    }

    public BigNumber pow(int exponent) {
        BigInteger molecular = mMolecular.pow(exponent);
        BigInteger denominator = mDenominator.pow(exponent);
        return simplification(molecular, denominator);

    }

    private BigNumber simplification(BigInteger molecular, BigInteger denominator) {
        BigInteger gcd = molecular.gcd(denominator);
        molecular = molecular.divide(gcd);
        denominator = denominator.divide(gcd);
        return new BigNumber(molecular.toString(), denominator.toString());
    }

    @Override
    protected BigNumber clone() {
        return new BigNumber(mMolecular.toString(), mDenominator.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BigNumber)) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        if (mMolecular.mod(mDenominator.abs()).equals(new BigInteger("0"))) {
            return mMolecular.divide(mDenominator).toString();
        }
        if (mMolecular.signum() * mDenominator.signum() == -1) {
            return "-" + mMolecular.abs().toString() + "/" + mDenominator.abs().toString();
        }
        return mMolecular.abs().toString() + "/" + mDenominator.abs().toString();

    }
}
