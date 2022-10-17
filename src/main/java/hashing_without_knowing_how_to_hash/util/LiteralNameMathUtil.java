package main.java.hashing_without_knowing_how_to_hash.util;

import lombok.experimental.UtilityClass;

/**
 * The necessary math in order to compute a literal name
 * So the math to map a number like 1 to a letter, like B
 * Also 1 to AB would be possible, if there are more than 26 numbers to encode
 */
@UtilityClass
public class LiteralNameMathUtil {

    /**
     * @param factor
     * @param basis
     * @param exponent
     * @return |factor| * |basis|^|exponent|
     */
    public int potencyTerm(int factor, int basis, int exponent){
        int absFactor=Math.abs(factor);
        int absBasis=Math.abs(basis);
        int absExponent=Math.abs(exponent);
        return (absFactor * (int)Math.pow(absBasis,absExponent));
    }

    /**
     * @param basis
     * @param maxExponent
     * @return SUM_{i=0}^{maxExponent} potencyTerm(|basis|-1, |basis|, i)
     */
    public int sumOfPotencyTerms(int basis, int maxExponent){
        int sum=0;
        int absBasis=Math.abs(basis);

        for(int i=0; i<=maxExponent; i++){
            sum+=potencyTerm(absBasis-1, absBasis, i);
        }

        return sum;
    }

    /**
     * Computes how many letters are required encode every bit with an alias of letters with te same length
     * e.g. if positionalCount()=1, the encodings look like A,P,Y
     * bit if positionalCount()=2, the encodings look like AR, FX, RI
     * @param literalCount how many literals need to gat an alias of letters
     * @param basis of the new 'number system' (just using letters)
     * @return the count of how many positions are required
     */
    public int positionalCount(int literalCount,int basis) {
        boolean found = false;
        int exp = 0;
        int absLiteralCount=Math.abs(literalCount);
        int absBasis=Math.abs(basis);
        while (!found) {
            if (sumOfPotencyTerms(absBasis, exp) >= absLiteralCount) {
                found = true;
            } else {
                exp++;
            }
        }
        return exp + 1;
    }

    /**
     * For num=56, basis=10, exponent=1:    56 = 5 * 10^1 + 6 => return 5
     * @param num to be approximately (under)estimated
     * @param basis of the concerning potency term
     * @param exponent of the concerning potency term
     * @return highest factor that does not overestimate num
     */
    public int calculateFittingFactor(int num, int basis, int exponent){
        int factor=1;
        boolean isFactorFound=false;
        int absNum=Math.abs(num);

        int recentTerm=potencyTerm(factor,basis,exponent);
        int nextTerm=potencyTerm(factor+1, basis, exponent);

        boolean isNumEvenUnderestimatable=num>=recentTerm; //if num < basis^exponent, then factor should be 0

        while(!isFactorFound && isNumEvenUnderestimatable){
            if(isXBetweenAAndB(recentTerm, absNum, nextTerm))
            {
                isFactorFound=true;
            }
            else {
                factor++;
                recentTerm=potencyTerm(factor,basis,exponent);
                nextTerm=potencyTerm(factor+1, basis, exponent);
            }
        }
        return isNumEvenUnderestimatable ? factor : 0;
    }

    /**
     * Tells if x in [a,b)
     * @param a lower boundary
     * @param x elements to be tested
     * @param b upper boundary
     * @return *true* if a<=x<b, else *false*
     */
    private boolean isXBetweenAAndB(int a, int x, int b){
        boolean aLeqX=a<=x;
        boolean xLessB=x<b;
        return aLeqX && xLessB;

    }

}
