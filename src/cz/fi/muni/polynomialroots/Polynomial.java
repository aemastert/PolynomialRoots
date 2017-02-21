package cz.fi.muni.polynomialroots;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {

	public List<BigDecimal> coeffs;//malo by by from lowest to highest
	private Polynomial derivative;
	
	public Polynomial(List<BigDecimal> coeffs){
		List<BigDecimal> newCoeffs = new ArrayList<>(coeffs);
		this.coeffs = newCoeffs;
	}
	//zaporne exponenty sposobia fail
	public Polynomial(String s){
		ArrayList<String> minusStrTerms = new ArrayList<>();		
		for(String curString : s.split("(?<![E])-")){
			if(curString.isEmpty()) continue;
			minusStrTerms.add("-"+curString);
		}
		if(!s.startsWith("-")){
			minusStrTerms.set(0, minusStrTerms.get(0).substring(1));
		}
		List<String> plusAndMinusStrTerms = new ArrayList<>();
		for(String curMinusString : minusStrTerms){
			for( String curString : curMinusString.split("(?<![E])\\+")){
				if(curString.isEmpty()) continue;
				plusAndMinusStrTerms.add(curString);
			}
		}
		List<BigDecimal> newCoefs = new ArrayList<>(plusAndMinusStrTerms.size());
		newCoefs.add(BigDecimal.ZERO);
		for(String curString : plusAndMinusStrTerms){
			String parseString = new String(curString);
			if(parseString.startsWith("-x")) parseString = "-1"+parseString.substring(1);
			if(parseString.startsWith("x")) parseString = "1"+parseString;
			Pattern p = Pattern.compile("(-?(\\d*\\.)?\\d+)?(E(\\+|-)\\d+)?x(\\^(-?(\\d*\\.)?\\d+))?");
			Matcher m = p.matcher(parseString);
			if(!m.find()){//ziadne x nenajdene, takze je to absolutny clen
				newCoefs.set(0, new BigDecimal(parseString));
			}else{
				int exponent = new BigDecimal(m.group(6) == null ? "1" : m.group(6)).intValue();
				while(newCoefs.size() < exponent + 1){
					newCoefs.add(BigDecimal.ZERO);
				}
				newCoefs.set(exponent, new BigDecimal(m.group(1) +( (m.group(3) != null )? m.group(3) : "")));
			}
		}
		
		this.coeffs = newCoefs;
	}
		
	public Polynomial derivative(){
		if(derivative != null) return derivative;

		List<BigDecimal> derivCoeffs = new ArrayList<>();
		
		for(int i = 1; i < coeffs.size(); i++){
			derivCoeffs.add(coeffs.get(i).multiply(new BigDecimal(i)));
		}
		derivative = new Polynomial(derivCoeffs);
		return derivative;
	}
	
	public BigDecimal value(BigDecimal x){//TODO iterativne
		BigDecimal sum = new BigDecimal("0");
		BigDecimal xpower = BigDecimal.ONE;
		for(int i = 0; i < coeffs.size(); i++){
			sum = sum.add(coeffs.get(i).multiply(xpower));
			xpower = xpower.multiply(x);
		}
		return sum;
	}
	
	@Override
	public boolean equals(Object other){
		if(!other.getClass().equals(this.getClass())) return false;
		Polynomial otherPoly = (Polynomial) other;
		if(this.coeffs.size() != otherPoly.coeffs.size()) return false;
		for(int i = 0; i < this.coeffs.size(); i++){
			if(!this.coeffs.get(i).equals(otherPoly.coeffs.get(i))) return false;
		}
		return true;
	}
	
	//return -1 ak this < other, 0 ak su rovne, 1 ak this > other//bolo zmenene z term na coeffs, malo by uz fungovat spravne
	public int compareTo(Polynomial other){
		if(coeffs.size() > other.coeffs.size()){
			for(int i = other.coeffs.size(); i < coeffs.size(); i++)
				if(coeffs.get(i).compareTo(BigDecimal.ZERO) != 0) return 1;
		}
		if(coeffs.size() < other.coeffs.size()){
			for(int i = coeffs.size(); i < other.coeffs.size(); i++)
				if(other.coeffs.get(i).compareTo(BigDecimal.ZERO) != 0) return -1;
		}
		for(int i = Math.min(coeffs.size(), other.coeffs.size()) - 1; i >= 0; i--){
			if (coeffs.get(i).compareTo(other.coeffs.get(i)) < 0){
				return - 1;
			} else if (coeffs.get(i).compareTo(other.coeffs.get(i)) > 0){
				return 1;
			}
		}
		return 0;
	}
	
	public int degree(){
	    int d = 0;
	    for (int i = coeffs.size() - 1; i >= 0; i--)
	        if (coeffs.get(i).compareTo(new BigDecimal("0")) != 0){ 
	        	d = i;
	        	break;
	        }
	    return d;
	}
	
	public BigDecimal getHighestCoeff(){
		BigDecimal hc = new BigDecimal("0");
	    for (int i = coeffs.size() - 1; i >= 0; i--)
	        if (coeffs.get(i).compareTo(new BigDecimal("0")) != 0){ 
	        	hc = coeffs.get(i);
	        	break;
	        }
	    return hc;
	}
	
	public void plus(Polynomial other){
		for(int i = 0; i < coeffs.size() && i < other.coeffs.size(); i++){
			coeffs.set(i, coeffs.get(i).add(other.coeffs.get(i)));
		}
		if (coeffs.size() < other.coeffs.size()){
			for(int i = coeffs.size(); i < other.coeffs.size(); i++){
				coeffs.add(other.coeffs.get(i));
			}
		}
	}
	
	public void minus(Polynomial other){
		for(int i = 0; i < coeffs.size() && i < other.coeffs.size(); i++){
			coeffs.set(i, coeffs.get(i).subtract(other.coeffs.get(i)));
		}
		if (coeffs.size() < other.coeffs.size()){
			for(int i = coeffs.size(); i < other.coeffs.size(); i++){
				coeffs.add(other.coeffs.get(i).negate());
			}
		}
	}
	
	public void multiply(Polynomial other){
		List<BigDecimal> newc = new ArrayList<>();
		for(int i = 0; i < coeffs.size() + other.coeffs.size() - 1; i++)
			newc.add(new BigDecimal("0").setScale(10, RoundingMode.HALF_UP));
		for(int i = 0; i < coeffs.size(); i++){
			for(int j = 0; j < other.coeffs.size(); j++){
				newc.set(i+j, newc.get(i+j).add(coeffs.get(i).multiply(other.coeffs.get(j))));
			}
		}
		this.coeffs = newc;
	}
	
	public void multiplyWithScalar(BigDecimal scalar){
		for(int i = 0; i < coeffs.size(); i++){
			coeffs.set(i, coeffs.get(i).multiply(scalar));
		}
	}
	
	public Polynomial substituteWithMinusX(){
		List<BigDecimal> newc = new ArrayList<>(coeffs);
		for(int i = 0; i < newc.size(); i++){
			if(i % 2 == 1) newc.set(i, newc.get(i).negate());
		}
		return new Polynomial(newc);
	}
	
	public Polynomial substituteWithPolynomial(Polynomial q){
		Polynomial toReturn = new Polynomial(BigDecimal.ZERO.toString());
		
		for(int i = 0; i <= degree(); i++){
			if(coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				BigDecimal c = coeffs.get(i);
				Polynomial toAdd = new Polynomial("1");
				for(int j = 0; j < i; j++){
					toAdd.multiply(q);
				}
				toAdd.multiplyWithScalar(c);
				toReturn.plus(toAdd);
			}
		}
		return toReturn;
	}
	
	public void substituteThisWithOne_div_X_plus_1_multiplied_x_n(){
		this.reverseCoefficients();
		Polynomial transform = new Polynomial(BigDecimal.ZERO.toString());
		for(int i = 0; i <= degree(); i++){
			if(coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				Polynomial toAdd = new Polynomial(binomialLine(BigInteger.valueOf(i)));
				toAdd.multiplyWithScalar(coeffs.get(i));
				transform.plus(toAdd);
			}
		}
		this.coeffs = transform.coeffs;
	}
	
	public static BigInteger binomialCoeff(BigInteger n, BigInteger k){
		BigInteger l = (k.compareTo((n.subtract(k))) > 0) ? k : (n.subtract(k));
		BigInteger numerator = BigInteger.ONE;
		for(BigInteger i = n; i.compareTo(l) > 0; i = i.subtract(BigInteger.ONE)){
			numerator = numerator.multiply(i);
		}
		BigInteger denominator = BigInteger.ONE;
		for(BigInteger i = BigInteger.ONE; i.compareTo(n.subtract(l)) <= 0; i = i.add(BigInteger.ONE)){
			denominator = denominator.multiply(i);
		}
		return numerator.divide(denominator);
	}
	
	public static List<BigDecimal> binomialLine(BigInteger n){
		List<BigDecimal> blist = new ArrayList<>();
		for(BigInteger i = BigInteger.ZERO; i.compareTo(n.divide(new BigInteger("2"))) <= 0; i = i.add(BigInteger.ONE)){
		 blist.add(new BigDecimal(Polynomial.binomialCoeff(n, i)));
		}
		List<BigDecimal> blist2 = new ArrayList<>(blist);
		if(n.mod(new BigInteger("2")).compareTo(BigInteger.ZERO) == 0) blist2.remove(blist2.size() - 1);
		Collections.reverse(blist2);
		blist.addAll(blist2);
		return blist;
	}
	
	public void reverseCoefficients(){
		Collections.reverse(coeffs);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		for(int i = coeffs.size() - 1; i >= 0; i--){
			if(coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				if(i < coeffs.size() - 1){
					if(coeffs.get(i).compareTo(new BigDecimal("0")) > 0)
						sb.append("+");
				}
				sb.append(coeffs.get(i).toString());
				if(i >= 1){
					sb.append('x');
					if(i > 1) sb.append(i != 1 ? "^"+i : "");
				}
			}
		}
		return sb.toString();
	}		
}