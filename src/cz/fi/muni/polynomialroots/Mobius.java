package cz.fi.muni.polynomialroots;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Mobius{
	private BigDecimal a;
	private BigDecimal b;
	private BigDecimal c;
	private BigDecimal d;
	
	public Mobius(){
		a = BigDecimal.ONE;
		b = BigDecimal.ZERO;
		c = BigDecimal.ZERO;
		d = BigDecimal.ONE;
	}
	
	public Mobius(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public BigDecimal getA(){
		return a;
	}
	
	public BigDecimal getB(){
		return b;
	}
	
	public BigDecimal getC(){
		return c;
	}
	
	public BigDecimal getD(){
		return d;
	}
	public BigDecimal value(BigDecimal x, int scale){
		return x.multiply(a).add(b).divide(x.multiply(c).add(d), scale, RoundingMode.HALF_UP);
	}
	
	public BigDecimal valueInfinityOrBound(BigDecimal bound, int scale){
		if(c.compareTo(BigDecimal.ZERO) == 0) return bound;
		return a.divide(c, scale, RoundingMode.HALF_UP);
	}
}