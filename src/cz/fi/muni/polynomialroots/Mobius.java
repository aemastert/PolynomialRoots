package cz.fi.muni.polynomialroots;

import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * Class representing Mobius transformation
 * @author Adrian E.
 *
 */
public class Mobius{
	private BigDecimal a;
	private BigDecimal b;
	private BigDecimal c;
	private BigDecimal d;
	/**
	 * Default constructor, sets (a,b,c,d) = (1,0,0,1) representing no substitution
	 */
	public Mobius(){
		a = BigDecimal.ONE;
		b = BigDecimal.ZERO;
		c = BigDecimal.ZERO;
		d = BigDecimal.ONE;
	}
	/**
	 * Mobius constructor, setting all parameters
	 * @param a a
	 * @param b b
	 * @param c c
	 * @param d d
	 */
	public Mobius(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	/**
	 * Returns a
	 * @return a
	 */
	public BigDecimal getA(){
		return a;
	}
	
	/**
	 * Returns b
	 * @return b
	 */
	public BigDecimal getB(){
		return b;
	}
	
	/**
	 * Returns c
	 * @return c
	 */
	public BigDecimal getC(){
		return c;
	}
	
	/**
	 * Returns d
	 * @return d
	 */
	public BigDecimal getD(){
		return d;
	}
	
	/**
	 * Evaluates this Mobius transformation in x with precision scale
	 * @param x	x to evaluate
	 * @param scale	scale of division
	 * @return	M(x)
	 */
	public BigDecimal value(BigDecimal x, int scale){
		return x.multiply(a).add(b).divide(x.multiply(c).add(d), scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * Evaluates this Mobius transformation in infinity or returns bound if c is 0
	 * @param bound	bound to return if c is 0
	 * @param scale division scale
	 * @return	infimum(M(infinity) bound)
	 */
	public BigDecimal valueInfinityOrBound(BigDecimal bound, int scale){
		if(c.compareTo(BigDecimal.ZERO) == 0) return bound;
		return a.divide(c, scale, RoundingMode.HALF_UP);
	}
}