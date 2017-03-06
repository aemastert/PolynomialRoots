package cz.fi.muni.polynomialroots;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.util.Pair;

public class PolynomialRootFinding {	
	public PolynomialRootFinding(){
	}
	
	public static final BigDecimal TWO = new BigDecimal("2");
	public static final BigDecimal THREE = new BigDecimal("3");
	
	//param d - must not be 0 
	public static List<Polynomial> longDivision(Polynomial n, Polynomial d, int divScale){//treba nastavit scaly podla parametru
		RoundingMode rounding = RoundingMode.DOWN;//UP sa zacykli, HALFUP da menej korenov, DOWN da presny pocet korenov
		Polynomial q = new Polynomial("0");
		Polynomial r = new Polynomial(n.toString());
		r.coeffs = new ArrayList<>(n.coeffs);

		Polynomial zeroP = new Polynomial(BigDecimal.ZERO.setScale(divScale, rounding).toString());
		
		while(zeroP.compareTo(r) != 0 && r.degree() >= d.degree()){
			BigDecimal rCoef = new BigDecimal("0");
			for (int i = r.coeffs.size() - 1; i >= 0; i--)
		        if (r.coeffs.get(i).compareTo(new BigDecimal("0")) != 0){ 
		        	rCoef = r.coeffs.get(i);
		        	break;
		        }
			BigDecimal dCoef = new BigDecimal("0");
			for (int i = d.coeffs.size() - 1; i >= 0; i--)
		        if (d.coeffs.get(i).compareTo(new BigDecimal("0")) != 0){ 
		        	dCoef = d.coeffs.get(i);
		        	break;
		        }
			BigDecimal tCoef = rCoef.divide(dCoef, divScale * 2, rounding);
			int tExpon = r.degree() - d.degree();
			Polynomial t = new Polynomial(tCoef + "x^" + tExpon);
			q.plus(t);
			t.multiply(d);
			r.minus(t);
			for(int i = 0; i < r.coeffs.size(); i++){
				r.coeffs.set(i, r.coeffs.get(i).setScale(divScale, rounding));
			}
		}
		
		List<Polynomial> returnList = new ArrayList<>();
		returnList.add(q);
		returnList.add(r);
		return returnList;
	}
	
	public static int sturm(Polynomial p0, BigDecimal start, BigDecimal end, int divisionScale){//start by mal byt mensi nez end..
		RoundingMode rounding1 = RoundingMode.DOWN;
		RoundingMode rounding2 = RoundingMode.DOWN;
		int startSignChanges = 0;
		int   endSignChanges = 0;
		List<Polynomial> division = new ArrayList<>();
		
		division.add(p0);
		division.add(p0.derivative());
		
		int startPreviousSign = division.get(0).value(start).setScale(divisionScale - 1, rounding1).compareTo(BigDecimal.ZERO);// -1 = -, 0 = 0, +1 = +
		int   endPreviousSign = division.get(0).value(end).setScale(divisionScale - 1, rounding2).compareTo(BigDecimal.ZERO);
		//System.out.println(division.get(0).value(start));
		//System.out.println(division.get(0).value(end));
		int startSign = division.get(1).value(start).setScale(divisionScale - 1, rounding1).compareTo(BigDecimal.ZERO);// -1 = -, 0 = 0, +1 = +
		int   endSign = division.get(1).value(end).setScale(divisionScale - 1, rounding2).compareTo(BigDecimal.ZERO);
		//System.out.println(division.get(1).value(start));
		//System.out.println(division.get(1).value(end));
		if(startPreviousSign != startSign && startPreviousSign != 0 && startSign != 0) startSignChanges++;
		if(  endPreviousSign !=   endSign &&   endPreviousSign != 0 &&   endSign != 0)   endSignChanges++;
		
		Polynomial zeroP = new Polynomial(BigDecimal.ZERO.setScale(divisionScale, rounding1).toString());
		//int i = 2;
		while(division.get(1).compareTo(zeroP) != 0){
			List<Polynomial> divisionRes = longDivision(division.get(0), division.get(1), divisionScale);
			//System.out.println("result p" + i);
			divisionRes.get(1).multiply(new Polynomial("-1"));
			division.remove(0);
			division.add(divisionRes.get(1));
			//for(BigDecimal d : division.get(1).coeffs) System.out.print(" " +  d.stripTrailingZeros().toPlainString());
			//System.out.println();
			if(startSign != 0) startPreviousSign = startSign;
			if(endSign != 0) endPreviousSign = endSign;
			startSign = division.get(1).value(start).setScale(divisionScale - 1, rounding1).compareTo(BigDecimal.ZERO);// -1 = -, 0 = 0, +1 = +
			endSign = division.get(1).value(end).setScale(divisionScale - 1, rounding2).compareTo(BigDecimal.ZERO);
			//System.out.println(division.get(1).value(start));
			//System.out.println(division.get(1).value(end));
			if(startPreviousSign != startSign && startPreviousSign != 0 && startSign != 0) startSignChanges++;
			if(  endPreviousSign !=   endSign &&   endPreviousSign != 0 &&   endSign != 0)   endSignChanges++;
			//i++;
		}
		return startSignChanges - endSignChanges;
	}
	
	public static List<BigDecimal> rootsOfPolynomialInIntervalUsingSturmTheoremAndBisection(Polynomial p, BigDecimal beginInterval, BigDecimal endInterval, int decimalPrecision){
		beginInterval = beginInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		endInterval = endInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		List<BigDecimal> roots = new ArrayList<>();
		int rootsCount = sturm(p, beginInterval, endInterval, decimalPrecision);
		if(rootsCount > 0){
			if(endInterval.subtract(beginInterval).setScale(decimalPrecision - 1, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
				//if(p.value(beginInterval).abs().compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(decimalPrecision), decimalPrecision, RoundingMode.HALF_UP)) <= 0){
					System.out.println("root: "  + beginInterval + " value: " + p.value(beginInterval));
					roots.add(beginInterval);
					return roots;
				//}
			}
			BigDecimal intervalHalf = ((endInterval.subtract(beginInterval)).divide(new BigDecimal("2"), decimalPrecision + 1, RoundingMode.HALF_UP)).add(beginInterval); 
			roots.addAll(rootsOfPolynomialInIntervalUsingSturmTheoremAndBisection(p, beginInterval, intervalHalf, decimalPrecision));
			roots.addAll(rootsOfPolynomialInIntervalUsingSturmTheoremAndBisection(p, intervalHalf, endInterval, decimalPrecision));
		}
		return roots;
	}
	
	public static BigDecimal bisection(Polynomial p, BigDecimal a, BigDecimal b, BigDecimal eps){
		BigDecimal c = a.add(b).divide(TWO, eps.scale()+1, RoundingMode.HALF_UP);
		System.out.print(c.toString() + " ");
		System.out.println(p.value(c).setScale(eps.scale(), RoundingMode.HALF_UP));
		if(c.subtract(a).compareTo(eps) < 0) return c;
		if(p.value(a).multiply(p.value(c)).compareTo(BigDecimal.ZERO) < 0){
			return bisection(p, a, c, eps);
		} else {
			return bisection(p, c, b, eps);
		}
	}
	
	public static List<BigDecimal> rootsOfPolynomialInIntervalUsingSturmTheoremAndBisectionAndNewtonMethod(Polynomial p, BigDecimal beginInterval, BigDecimal endInterval, int decimalPrecision){
		beginInterval = beginInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		endInterval = endInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		List<BigDecimal> roots = new ArrayList<>();
		int rootsCount = sturm(p, beginInterval, endInterval, decimalPrecision);
		BigDecimal originBegin = beginInterval;
		BigDecimal originEnd = endInterval;
		if(rootsCount > 0){
			if(rootsCount == 1){
					//BigDecimal intervalHalf = ((endInterval.subtract(beginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(beginInterval);
					Pair<BigDecimal, Boolean> root = findRootNewtonInInterval(p, decimalPrecision + 1, beginInterval, endInterval);
					//if(p.value(root).abs().setScale(decimalPrecision, RoundingMode.HALF_UP).compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(decimalPrecision), decimalPrecision, RoundingMode.HALF_UP)) <= 0){//malo by byt <= podla logiky
						/*while(root.compareTo(beginInterval) < 0 || root.compareTo(endInterval) > 0){
							int rootsCountLeft = sturm(p, beginInterval, intervalHalf, decimalPrecision);
							int rootsCountRight = sturm(p, intervalHalf, endInterval, decimalPrecision);
							if(rootsCountLeft == 1){
								endInterval = intervalHalf;
								intervalHalf = ((intervalHalf.subtract(beginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(beginInterval);
							} else if (rootsCountRight == 1){
								beginInterval = intervalHalf;
								intervalHalf = ((endInterval.subtract(intervalHalf)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(intervalHalf);
							}
							root = findRootNewton(p, intervalHalf, decimalPrecision + 1);
							System.out.println("begin: "+beginInterval+" end:"+endInterval+" half:"+intervalHalf+" root: "  + root + " value: " + p.value(root).setScale(decimalPrecision, RoundingMode.HALF_UP));
						}*/
						//if(p.value(root).abs().setScale(decimalPrecision - 1, RoundingMode.HALF_UP).compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(decimalPrecision), decimalPrecision, RoundingMode.HALF_UP)) <= 0){
					if(root.getValue()){
						//System.out.println("root: "  + root.getKey()); //+ " value: " + p.value(root.getKey()));
						roots.add(root.getKey());
						return roots;
					}
						//}
					//}
			}
			beginInterval = originBegin;
			endInterval = originEnd;
			BigDecimal intervalHalf = ((endInterval.subtract(beginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(beginInterval); 
			roots.addAll(rootsOfPolynomialInIntervalUsingSturmTheoremAndBisectionAndNewtonMethod(p, beginInterval, intervalHalf, decimalPrecision));
			roots.addAll(rootsOfPolynomialInIntervalUsingSturmTheoremAndBisectionAndNewtonMethod(p, intervalHalf, endInterval, decimalPrecision));
		}
		return roots;
	}
	
	public static Pair<BigDecimal, Boolean> findRootNewtonInInterval(Polynomial p, int nScale, BigDecimal begin, BigDecimal end){
		Boolean success = false;
		BigDecimal x0 = end.subtract(begin).divide(TWO, nScale + 1, RoundingMode.HALF_UP).add(begin);
		BigDecimal x1 = end.subtract(begin).divide(TWO, nScale + 1, RoundingMode.HALF_UP).add(begin);
		int maxIterations = 5000;
		for(int i = 0; i < maxIterations; i++)
		//while(true)
		{
			BigDecimal y = p.value(x0);
			BigDecimal yprime = p.derivative().value(x0);		

			try{
				x1 = x0.subtract(y.divide(yprime, nScale + 1, RoundingMode.HALF_UP));
			}
			catch(ArithmeticException e){
				return new Pair<>(x1, success);
			}
			if(x1.subtract(x0).abs().compareTo(x1.abs().multiply(BigDecimal.ONE.divide(BigDecimal.TEN.pow(nScale), nScale, RoundingMode.HALF_UP))) <=  0){
				success = true;
				break;  
			}
			if(x1.compareTo(begin) < 0 || x1.compareTo(end) > 0) return new Pair<>(x1, success);//dolezite, kvoli root skippingu!
	    	x0 = x1;
		}
		return new Pair<>(x1.setScale(nScale, RoundingMode.HALF_UP), success);
	}
	
	public static Pair<BigDecimal, Boolean> findRootNewton(Polynomial p, int nScale, BigDecimal startpoint){
		Boolean success = false;
		BigDecimal x0 = startpoint;
		BigDecimal x1 = startpoint;
		int maxIterations = 5000;
		for(int i = 0; i < maxIterations; i++)
		//while(true)
		{
			BigDecimal y = p.value(x0);
			BigDecimal yprime = p.derivative().value(x0);		

			try{
				x1 = x0.subtract(y.divide(yprime, nScale + 1, RoundingMode.HALF_UP));
			}
			catch(ArithmeticException e){
				return new Pair<>(x1, success);
			}
			if(x1.subtract(x0).abs().compareTo(x1.abs().multiply(BigDecimal.ONE.divide(BigDecimal.TEN.pow(nScale), nScale, RoundingMode.HALF_UP))) <=  0){
				success = true;
				//System.out.println("newton " + i);
				break;  
			}
	    	x0 = x1;
		}
		return new Pair<>(x1.setScale(nScale, RoundingMode.HALF_UP), success);
	}
	
	public static Pair<BigDecimal, Boolean> findRootHalley(Polynomial p, int nScale, BigDecimal startpoint){
		Boolean success = false;
		BigDecimal x0 = startpoint;
		BigDecimal x1 = startpoint;
		int maxIterations = 5000;
		for(int i = 0; i < maxIterations; i++)
		//while(true)
		{
			BigDecimal y = p.value(x0);
			BigDecimal yprime = p.derivative().value(x0);		
			BigDecimal yprimeprime = p.derivative().derivative().value(x0);
			
			try{
				BigDecimal numerator = TWO.multiply(y).multiply(yprime);
				BigDecimal denominator = yprime.multiply(yprime).multiply(TWO).subtract(yprimeprime.multiply(y));
				x1 = x0.subtract(numerator.divide(denominator, nScale + 1, RoundingMode.HALF_UP));
			}
			catch(ArithmeticException e){
				return new Pair<>(x1, success);
			}
			if(x1.subtract(x0).abs().compareTo(x1.abs().multiply(BigDecimal.ONE.divide(BigDecimal.TEN.pow(nScale), nScale, RoundingMode.HALF_UP))) <=  0){
				success = true;
				//System.out.println("halley " + i);
				break;  
			}
	    	x0 = x1;
		}
		return new Pair<>(x1.setScale(nScale, RoundingMode.HALF_UP), success);
	}
	
	public static Pair<BigDecimal, Boolean> findRootHalleyInInterval(Polynomial p, int nScale, BigDecimal begin, BigDecimal end){
		Boolean success = false;
		BigDecimal x0 = end.subtract(begin).divide(TWO, nScale + 1, RoundingMode.HALF_UP).add(begin);
		BigDecimal x1 = end.subtract(begin).divide(TWO, nScale + 1, RoundingMode.HALF_UP).add(begin);
		int maxIterations = 5000;
		for(int i = 0; i < maxIterations; i++)
		//while(true)
		{
			BigDecimal y = p.value(x0);
			BigDecimal yprime = p.derivative().value(x0);		
			BigDecimal yprimeprime = p.derivative().derivative().value(x0);
			
			try{
				BigDecimal numerator = TWO.multiply(y).multiply(yprime);
				BigDecimal denominator = yprime.multiply(yprime).multiply(TWO).subtract(yprimeprime.multiply(y));
				x1 = x0.subtract(numerator.divide(denominator, nScale + 1, RoundingMode.HALF_UP));
			}
			catch(ArithmeticException e){
				return new Pair<>(x1, success);
			}
			if(x1.subtract(x0).abs().compareTo(x1.abs().multiply(BigDecimal.ONE.divide(BigDecimal.TEN.pow(nScale), nScale, RoundingMode.HALF_UP))) <=  0){
				success = true;
				//System.out.println("halley " + i);
				break;  
			}
			if(x1.compareTo(begin) < 0 || x1.compareTo(end) > 0) return new Pair<>(x1, success);//dolezite, kvoli root skippingu!
	    	x0 = x1;
		}
		return new Pair<>(x1.setScale(nScale, RoundingMode.HALF_UP), success);
	}
	
	public static List<BigDecimal> findRootsNewtonHorner(Polynomial p, BigDecimal start_inter, BigDecimal end_inter, BigDecimal tolerance, BigDecimal epsilon){
		if(p.degree() == 1){
			BigDecimal roo = new BigDecimal("0");
			if(p.coeffs.get(0).compareTo(new BigDecimal("0")) == 0){
				return new ArrayList<BigDecimal>(Arrays.asList(roo));
			}
			roo = new BigDecimal("-1")
					.multiply( (p.coeffs.get(0)).divide(p.coeffs.get(1), epsilon.scale(), RoundingMode.HALF_UP));
			return new ArrayList<BigDecimal>(Arrays.asList(roo));
		}
		
		List<BigDecimal> roots = new ArrayList<>();
		
		BigDecimal x0 = end_inter;
		BigDecimal x1 = end_inter;
			//Newton
			int maxIterations = 500;
			boolean solution = false;
			
			for(int i = 0; i < maxIterations; i++)
			{
				BigDecimal y = p.value(x0);
				BigDecimal yprime = p.derivative().value(x0);
				
				//if(yprime.abs().compareTo(epsilon) < 0) 
					//break;
				x1 = x0.subtract(y.divide(yprime, epsilon.scale(), RoundingMode.HALF_UP));
				if(x1.subtract(x0).abs().compareTo(x1.abs().multiply(tolerance)) <=  0){
					solution = true;
					break;  
				}
		    	x0 = x1;
		    	if (x0.compareTo(start_inter) <= 0)
		    		break;
			}
			
			if (solution){
				   roots.add(x1);
				   List<BigDecimal> revCoef = new ArrayList<>(p.coeffs);
				   Collections.reverse(revCoef);
				   BigDecimal[] coef = (revCoef).toArray(new BigDecimal[(revCoef.size())]);
				   int n = coef.length;
				   BigDecimal divis = x1;
				   BigDecimal[] resCoef = new BigDecimal[n];
				   resCoef[0] = coef[0];
				   for(int j = 1; j < n; j++){
					   resCoef[j] = divis.multiply(resCoef[j - 1]).add(coef[j]);
				   }
				   for(int j = 0; j < n; j++){
					   resCoef[j] = resCoef[j].setScale(epsilon.scale(), RoundingMode.HALF_UP);
				   }
				   if(n == 2){
					   if(resCoef[0].compareTo(new BigDecimal("1")) == 0 &&
							   resCoef[1].compareTo(new BigDecimal("0")) == 0){
						   System.out.println("success");
						   return roots;
					   }
				   }

				   if(resCoef[n - 1].abs().compareTo(tolerance.multiply(new BigDecimal("10"))) < 0){
					   StringBuilder divPoly = new StringBuilder();
					   if(resCoef.length > 0) {
						   divPoly.append(resCoef[0]).append("x^").append(n - 2);
					   }
					   for(int j = 1; j < n - 2; j++){
							if(resCoef[j].compareTo(new BigDecimal("0")) != 0){
								if(resCoef[j].compareTo(new BigDecimal("0")) > 0){
									divPoly.append("+");
								}
								divPoly.append(resCoef[j]).append("x^").append(n - 2 - j);
							}
					   }
					   if(resCoef.length > 0) {
						   if(resCoef[n - 2].compareTo(new BigDecimal("0")) != 0){
								if(resCoef[n - 2].compareTo(new BigDecimal("0")) > 0){
									divPoly.append("+");
								}
								divPoly.append(resCoef[n - 2]);
							}
					   }

					   roots.addAll(findRootsNewtonHorner(new Polynomial(divPoly.toString()), start_inter, end_inter, tolerance, epsilon));

				   }
				   else {
					   System.out.println("remainder is not 0");
					   return roots;
				   }
			}
		Collections.sort(roots);
		List<BigDecimal> returnList = new ArrayList<>();
		
		if(roots.size() > 0) returnList.add(roots.get(0).setScale(tolerance.scale(), RoundingMode.HALF_UP));
		for(int i = 1; i < roots.size(); i++){
			BigDecimal prevRoot = roots.get(i-1);
			BigDecimal curRoot = roots.get(i);
			if((prevRoot.subtract(curRoot)).abs().compareTo(tolerance) > 0)
				returnList.add(curRoot.setScale(tolerance.scale(), RoundingMode.HALF_UP));
		}
		return returnList;
	}
	
	//a(n)*a(0) =/= 0
	public static BigDecimal rootBound(Polynomial p, int dScale){//z pdfka co mi poslal matik
		BigDecimal max = BigDecimal.ZERO;
		if(p.coeffs.get(0).compareTo(BigDecimal.ZERO) == 0) return max;
		for(int i = 1; i <= p.degree(); i++){
			if (p.coeffs.get(i).abs().compareTo(max) > 0) {
				max = p.coeffs.get(i).abs();
			}
		}
		return BigDecimal.ONE.add(max.divide(p.coeffs.get(0).abs(), dScale, RoundingMode.HALF_UP));
	}
	
	public static BigDecimal cauchyBound(Polynomial p, int dScale){
		BigDecimal max = p.coeffs.get(0).divide(p.getHighestCoeff(), RoundingMode.HALF_UP).abs();
		System.out.println("cauchy computation:" + max);
		for(int i = p.degree() - 1; i >= 0; i--){
			if (p.coeffs.get(i).divide(p.getHighestCoeff(), RoundingMode.HALF_UP).abs().compareTo(max) > 0) {
				max = p.coeffs.get(i).divide(p.getHighestCoeff(), RoundingMode.HALF_UP).abs();
				System.out.println("cauchy computation:" + max);
			}
		}
		return BigDecimal.ONE.add(max);
	}
	
	public static BigDecimal kojimaBound(Polynomial p, int dScale){
		BigDecimal max = BigDecimal.ZERO;
		if(p.degree() > 0){
			try{
				max = p.coeffs.get(0).divide(TWO.multiply(p.coeffs.get(1)), dScale, RoundingMode.HALF_UP).abs();
			}
			catch(ArithmeticException e){
			}
		}
		else {
			return BigDecimal.ZERO;
		}
		for(int i = 1; i < p.degree(); i++){
			BigDecimal cur;
			try{
				cur = p.coeffs.get(i).divide(p.coeffs.get(i+1), dScale, RoundingMode.HALF_UP).abs();
			}
			catch(ArithmeticException e){
				continue;
			}
			if (cur.compareTo(max) > 0) {
				max = cur;
			}
		}
		return max.multiply(TWO);
	}
	
	public static int budan01(Polynomial p){
		int v = 0;
		int prevSign = p.coeffs.get(0).compareTo(BigDecimal.ZERO);//-1 negative, 0 zero, 1 positive
		int sign = 0;
		
		Polynomial p1 = new Polynomial(BigDecimal.ZERO.toString());
		
		for(int i = 0; i <= p.degree(); i++){
			if(p.coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				BigDecimal c = p.coeffs.get(i);
				Polynomial toAdd = new Polynomial("1");
				for(int j = 0; j < p.degree() - i; j++){
					toAdd.multiply(new Polynomial("x+1"));
				}
				toAdd.multiplyWithScalar(c);
				p1.plus(toAdd);
			}
		}
		
		prevSign = p1.coeffs.get(0).compareTo(BigDecimal.ZERO);//-1 negative, 0 zero, 1 positive
		sign = 0;
		for(int i = 1; i <= p1.degree(); i++){
			sign = p1.coeffs.get(i).compareTo(BigDecimal.ZERO);
			if(prevSign != sign && sign !=0 && prevSign != 0){
				v++;
			}
			if(sign != 0) prevSign = sign;
		}
		return v;
	}
	
	public static List<Pair<BigDecimal, BigDecimal>> vca(Polynomial p, Pair<BigDecimal, BigDecimal> ab, int dScale){
		List<Pair<BigDecimal, BigDecimal>> intervals = new ArrayList<>();
		int var = budan01(p);
		if(var == 0) return intervals;
		if(var == 1/* && ab.getKey().subtract(ab.getValue()).abs().compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(dScale), dScale, RoundingMode.HALF_UP)) == 0*//*test*/){
			intervals.add(ab);
			return intervals;
		}
		if(ab.getKey().compareTo(ab.getValue()) == 0){//ochrana kvoli zacykleniu pri nasobnom koreni
			intervals.add(ab);//POZOR to neznamena ze je tam naozaj koren, bud je tam nasobny koren alebo tam nie je koren
			return intervals;//preto to treba overit(napr. tam spustit Newtona)
		}
		Polynomial p012 = new Polynomial(p.coeffs);
		for(int i = 0; i <= p012.degree(); i++){
			p012.coeffs.set(i, p012.coeffs.get(i).multiply(TWO.pow(p012.degree() - i)));
		}
		BigDecimal m = (ab.getKey().add(ab.getValue())).divide(TWO, dScale + 1, RoundingMode.HALF_UP);
		
		Polynomial p121 = new Polynomial(BigDecimal.ZERO.toString());
		
		for(int i = 0; i <= p.degree(); i++){
			if(p.coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				BigDecimal c = p.coeffs.get(i);
				Polynomial toAdd = new Polynomial("1");
				for(int j = 0; j < i; j++){
					toAdd.multiply(new Polynomial("x+1"));
				}
				toAdd.multiplyWithScalar(c);
				toAdd.multiplyWithScalar(TWO.pow(p012.degree() - i));
				p121.plus(toAdd);
			}
		}
		if(p.value(BigDecimal.ONE.divide(TWO, dScale + 1, RoundingMode.HALF_UP)).setScale(dScale, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) != 0){
			intervals.addAll(vca(p012, new Pair<>(ab.getKey(), m), dScale));
			intervals.addAll(vca(p121, new Pair<>(m, ab.getValue()), dScale));
			return intervals;
		} else {
			intervals.addAll(vca(p012, new Pair<>(ab.getKey(), m), dScale));
			intervals.add(new Pair<>(m, m));
			intervals.addAll(vca(p121, new Pair<>(m, ab.getValue()), dScale));
			return intervals;
		}
	}
	
	public static List<Pair<BigDecimal, BigDecimal>> VCA(Polynomial porig, BigDecimal upperBound, int dScale){
		if(upperBound.compareTo(BigDecimal.ZERO) < 0) return new ArrayList<Pair<BigDecimal, BigDecimal>>();
		Polynomial p = new Polynomial(porig.coeffs);
		for(int i = 1; i <= p.degree(); i++){
			if(p.coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				p.coeffs.set(i, p.coeffs.get(i).multiply(upperBound.pow(i)));
			}
		}
		return vca(p, new Pair<>(BigDecimal.ZERO, upperBound), dScale);
	}
	
	// a < b
	public static List<Pair<BigDecimal, BigDecimal>> VCAinterval(Polynomial porig, BigDecimal a, BigDecimal b, int dScale){
		Polynomial sub = new Polynomial("x+"+a.toString());
		Polynomial p = porig.substituteWithPolynomial(sub);
		BigDecimal newB = b.subtract(a);
		for(int i = 1; i <= p.degree(); i++){
			if(p.coeffs.get(i).compareTo(BigDecimal.ZERO) != 0){
				p.coeffs.set(i, p.coeffs.get(i).multiply(newB.pow(i)));
			}
		}
		List<Pair<BigDecimal, BigDecimal>> result = vca(p, new Pair<>(BigDecimal.ZERO, newB), dScale);
		for(int i = 0; i < result.size(); i++){
			result.set(i, new Pair<>(result.get(i).getKey().add(a), result.get(i).getValue().add(a)));
		}
		return result;
	}
	
	public static List<BigDecimal> findRootsOfPolynomialInIntervalUsingVCAAndNewton(Polynomial p, BigDecimal beginInterval, BigDecimal endInterval, int decimalPrecision){
		beginInterval = beginInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		endInterval = endInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		List<BigDecimal> roots = new ArrayList<>();
		long t0 = System.nanoTime();
		List<Pair<BigDecimal, BigDecimal>> rootsIntervals = VCAinterval(p, beginInterval, endInterval, decimalPrecision);
		long t1 = System.nanoTime();
		System.out.println("VCA intervals time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		t0 = System.nanoTime();
		for(Pair<BigDecimal, BigDecimal> interval : rootsIntervals){
			BigDecimal rootBeginInterval = interval.getKey();
			BigDecimal rootEndInterval = interval.getValue();
			Pair<BigDecimal, Boolean> root = findRootNewtonInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			if(!root.getValue() && rootBeginInterval.compareTo(rootEndInterval) == 0) continue;//ochrana proti zacykleniu pri nasobnom koreni
			while(!root.getValue()){
				BigDecimal intervalHalf = ((rootEndInterval.subtract(rootBeginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(rootBeginInterval);
				int signBegin = p.value(rootBeginInterval).signum();
				//int signEnd = p.value(rootEndInterval).signum();
				int signHalf = p.value(intervalHalf).signum();
				if(signBegin == signHalf){
					rootBeginInterval = intervalHalf;
				}
				else{
					rootEndInterval = intervalHalf;
				}
				root = findRootNewtonInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			}
			//System.out.println("VCA root: "  + root.getKey());
			roots.add(root.getKey());
		}
		t1 = System.nanoTime();
		System.out.println("VCA newton time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		return roots;
	}
	
	public static List<BigDecimal> findRootsOfPolynomialInIntervalUsingVCAAndHalley(Polynomial p, BigDecimal beginInterval, BigDecimal endInterval, int decimalPrecision){
		beginInterval = beginInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		endInterval = endInterval.setScale(decimalPrecision, RoundingMode.HALF_UP);
		List<BigDecimal> roots = new ArrayList<>();
		long t0 = System.nanoTime();
		List<Pair<BigDecimal, BigDecimal>> rootsIntervals = VCAinterval(p, beginInterval, endInterval, decimalPrecision);
		long t1 = System.nanoTime();
		System.out.println("VCA intervals time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		t0 = System.nanoTime();
		for(Pair<BigDecimal, BigDecimal> interval : rootsIntervals){
			BigDecimal rootBeginInterval = interval.getKey();
			BigDecimal rootEndInterval = interval.getValue();
			Pair<BigDecimal, Boolean> root = findRootHalleyInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			if(!root.getValue() && rootBeginInterval.compareTo(rootEndInterval) == 0) continue;//ochrana proti zacykleniu pri nasobnom koreni
			while(!root.getValue()){
				BigDecimal intervalHalf = ((rootEndInterval.subtract(rootBeginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(rootBeginInterval);
				int signBegin = p.value(rootBeginInterval).signum();
				//int signEnd = p.value(rootEndInterval).signum();
				int signHalf = p.value(intervalHalf).signum();
				if(signBegin == signHalf){
					rootBeginInterval = intervalHalf;
				}
				else{
					rootEndInterval = intervalHalf;
				}
				root = findRootHalleyInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			}
			//System.out.println("VCA root: "  + root.getKey());
			roots.add(root.getKey());
		}
		t1 = System.nanoTime();
		System.out.println("VCA halley time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		return roots;
	}
	
	public static List<Pair<BigDecimal, BigDecimal>>vas(Polynomial p, Mobius mob, int scale){
		List<Pair<BigDecimal, BigDecimal>> intervals = new ArrayList<>();
		int var = 0;
		int prevSign = p.coeffs.get(0).compareTo(BigDecimal.ZERO);//-1 negative, 0 zero, 1 positive
		int sign = 0;
		for(int i = 1; i <= p.degree(); i++){
			sign = p.coeffs.get(i).compareTo(BigDecimal.ZERO);
			if(prevSign != sign && sign != 0 && prevSign != 0){
				var++;
			}
			if(sign != 0) prevSign = sign;
		}
		if (var == 0) return intervals;
		BigDecimal upperBound = vigklasBound2(p, scale);
		//System.out.println("upper: " + upperBound.toString());
		if (var == 1){
			BigDecimal a = mob.value(BigDecimal.ZERO, scale).min(mob.valueInfinityOrBound(mob.value(BigDecimal.ZERO, scale), scale));
			BigDecimal b = mob.value(BigDecimal.ZERO, scale).max(mob.valueInfinityOrBound(upperBound, scale));
			intervals.add(new Pair<>(a, b));
			return intervals;
		}
		
		p.reverseCoefficients();
		
		BigDecimal lowerBound = BigDecimal.ONE.divide(vigklasBound2(p, scale), scale, RoundingMode.HALF_UP);
		p.reverseCoefficients();
		//System.out.println("lower: " + lowerBound.toString());
		if(lowerBound.compareTo(BigDecimal.ONE) >= 0){
			p = p.substituteWithPolynomial(new Polynomial("x+"+lowerBound.toString()));
			mob = new Mobius(mob.getA(), mob.getB().add(mob.getA().multiply(lowerBound)), mob.getC(), mob.getD().add(mob.getC().multiply(lowerBound)));
		}
		
		Polynomial p01 = new Polynomial(p.coeffs);
		
		p01.substituteThisWithOne_div_X_plus_1_multiplied_x_n();

		Mobius m01 = new Mobius(mob.getB(), mob.getA().add(mob.getB()), mob.getD(), mob.getC().add(mob.getD()));
		
		BigDecimal m = mob.value(BigDecimal.ONE, scale);
		
		Polynomial p1infinity = p.substituteWithPolynomial(new Polynomial("x+1"));
		Mobius m1infinity = new Mobius(mob.getA(), mob.getA().add(mob.getB()),  mob.getC(), mob.getC().add(mob.getD()));
		
		//toto porovnanie treba doladit, asi najlepsie p.value(x-epsilon/2) p.value(x+epsilon/2) ci maju rozdielne znamienka
		if(p.value(BigDecimal.ONE).abs().compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(scale+1), scale+1, RoundingMode.HALF_UP)) <= 0){
			/*System.out.println("value: " + p.value(BigDecimal.ONE).toString());
			System.out.println("compare to:" + BigDecimal.ONE.divide(BigDecimal.TEN.pow(scale+1), scale+1, RoundingMode.HALF_UP).toString());
			System.out.println((p.value(BigDecimal.ONE).compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(scale+1), scale+1, RoundingMode.HALF_UP)) <= 0)?"true":"false");
			*/intervals.addAll(vas(p01, m01, scale));
			intervals.add(new Pair<>(m, m));
			intervals.addAll(vas(p1infinity, m1infinity, scale));
			return intervals;
		} else {
			intervals.addAll(vas(p01, m01, scale));
			intervals.addAll(vas(p1infinity, m1infinity, scale));
		}
		
		return intervals;
	}
	
	public static List<BigDecimal> rootsVAS(Polynomial p, int decimalPrecision){
		List<BigDecimal> roots = new ArrayList<>();
		long t0 = System.nanoTime();
		List<Pair<BigDecimal, BigDecimal>> rootsIntervals = vas(p, new Mobius(), decimalPrecision);
		long t1 = System.nanoTime();
		System.out.println("VAS intervals time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		System.out.println(rootsIntervals);
		t0 = System.nanoTime();
		for(Pair<BigDecimal, BigDecimal> interval : rootsIntervals){
			BigDecimal rootBeginInterval = interval.getKey();
			BigDecimal rootEndInterval = interval.getValue();
			Pair<BigDecimal, Boolean> root = findRootHalleyInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			if(!root.getValue() && rootBeginInterval.compareTo(rootEndInterval) == 0) continue;//ochrana proti zacykleniu pri nasobnom koreni
			while(!root.getValue()){
				BigDecimal intervalHalf = ((rootEndInterval.subtract(rootBeginInterval)).divide(new BigDecimal("2"), decimalPrecision, RoundingMode.HALF_UP)).add(rootBeginInterval);
				int signBegin = p.value(rootBeginInterval).signum();
				//int signEnd = p.value(rootEndInterval).signum();
				int signHalf = p.value(intervalHalf).signum();
				if(signBegin == signHalf){
					rootBeginInterval = intervalHalf;
				}
				else{
					rootEndInterval = intervalHalf;
				}
				root = findRootHalleyInInterval(p, decimalPrecision, rootBeginInterval, rootEndInterval);
			}
			//System.out.println("VCA root: "  + root.getKey());
			roots.add(root.getKey());
		}
		t1 = System.nanoTime();
		System.out.println("VAS halley time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		return roots;
	}
	
	
	public static List<BigDecimal> findRootsVAS(Polynomial p, int decimalPrecision){
		List<BigDecimal> roots = rootsVAS(p, decimalPrecision);
		List<BigDecimal> negativeRoots = rootsVAS(p.substituteWithMinusX(), decimalPrecision);
		for(BigDecimal bd : negativeRoots){
			roots.add(bd.negate());
		}
		Collections.sort(roots);
		return roots;
	}
	
	public static BigDecimal nthRoot(BigDecimal b, int n, int scale){
		if(b.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
		Polynomial p = new Polynomial("x^" + n + "-" + b.toString());
		BigDecimal startpoint = BigDecimal.ONE;
		while(p.value(startpoint).compareTo(BigDecimal.ZERO) < 0) startpoint = startpoint.multiply(THREE);//OVELA RYCHLEJSIE nez 1
		return findRootHalley(p, scale, startpoint).getKey();
	}
	
	public static BigDecimal vigklasBound(Polynomial p, int scale){//zle, netusim preco
		int n = p.degree();
		int[] timesUsed = new int[n + 2];
		for(int i = 0; i < n + 2; i++){
			timesUsed[i] = 1;
		}
		BigDecimal[] coeffs = new BigDecimal[n + 2];
		for(int i = 1; i < n + 2; i++){
			coeffs[i] = p.coeffs.get(i-1);
		}
		BigDecimal upperBound = BigDecimal.ZERO;
		if (n + 1 <= 1) return upperBound; 
		
		for(int m = n; m >= 1; m--){
			if(coeffs[m].compareTo(BigDecimal.ZERO) < 0){
				BigDecimal tempUpperBound = BigDecimal.ZERO;
				boolean uninit = true;
				for(int k = n + 1; k >= m + 1; k--){
					if(coeffs[k].compareTo(BigDecimal.ZERO) > 0){
						BigDecimal tempNumerator = coeffs[m].negate();
						BigDecimal tempDenominator = coeffs[k].divide(TWO.pow(timesUsed[k]), scale, RoundingMode.HALF_UP);
						BigDecimal temp = nthRoot(tempNumerator.divide(tempDenominator, scale, RoundingMode.HALF_UP), k - m, scale);
						timesUsed[k]++;
						//System.out.println(temp.toString());
						if(tempUpperBound.compareTo(temp) > 0 || uninit){
							tempUpperBound = temp;
							uninit = false;
						}
					}
				}
				//System.out.println(tempUpperBound.toString());
				if(upperBound.compareTo(tempUpperBound) < 0) upperBound = tempUpperBound;
			}
		}
		return upperBound;
	}
	
	public static BigDecimal vigklasBound2(Polynomial p, int scale){//z phd thesis
		boolean changed = false;
		if (p.getHighestCoeff().compareTo(BigDecimal.ZERO) < 0){
			p.multiplyWithScalar(BigDecimal.ONE.negate());
			changed = true;
		}
		BigDecimal upperBound = BigDecimal.ZERO;
		int n = p.degree();
		if(n + 1 <= 1) return upperBound;
		int j = n;
		int t = 1;
		for(int i = n - 1; i >= 0; i--){
			if(p.coeffs.get(i).compareTo(BigDecimal.ZERO) < 0){
				BigDecimal tempUb = p.coeffs.get(i).divide(p.coeffs.get(j), scale, RoundingMode.HALF_UP);
				tempUb = tempUb.negate().multiply(TWO.pow(t));
				tempUb = nthRoot(tempUb, j - i, scale);
				if(tempUb.compareTo(upperBound) > 0) upperBound = tempUb;
				t = t + 1;
			}
			else {
				if(p.coeffs.get(i).compareTo(p.coeffs.get(j)) > 0){
					j = i;
					t = 1;
				}
			}
		}
		if (changed){
			p.multiplyWithScalar(BigDecimal.ONE.negate());
		}
		return upperBound;
	}
	
	public static Pair<BigDecimal, BigDecimal> bounds(Polynomial p, int scale){
		int n = p.degree();
		BigDecimal p1 = p.coeffs.get(n - 1).divide(p.coeffs.get(n).multiply(new BigDecimal(n)), scale, RoundingMode.HALF_UP).negate();
		BigDecimal p2 = new BigDecimal(n - 1).divide(p.coeffs.get(n).multiply(new BigDecimal(n)), scale, RoundingMode.HALF_UP).multiply(
				nthRoot(p.coeffs.get(n-1).multiply(p.coeffs.get(n-1)).subtract(
							p.coeffs.get(n).multiply(p.coeffs.get(n-2)).multiply(
									new BigDecimal(2*n).divide(new BigDecimal(n-1), scale, RoundingMode.HALF_UP))), 
						2, scale)
				);
		return new Pair<>(p1.subtract(p2), p1.add(p2));
	}
	
	public static BigDecimal fujiwaraBound(Polynomial p, int scale){
		int n = p.degree();
		BigDecimal bound = nthRoot(p.coeffs.get(0).divide(TWO.multiply(p.coeffs.get(n)), scale, RoundingMode.HALF_UP).abs(), n, scale);
		for(int i = 1; i <= n-1; i++){
			BigDecimal a = nthRoot(p.coeffs.get(n-i).divide(p.coeffs.get(n), scale, RoundingMode.HALF_UP).abs(), i, scale);
			if(bound.compareTo(a) < 0) bound = a;
		}
		
		return TWO.multiply(bound);
	}
	
	public static List<BigDecimal> findRootsEigen(Polynomial p, int scale){
		List<BigDecimal> roots = new ArrayList<>();
		
		int m = p.degree();
		int n = p.degree();
		
		BigDecimal[][] a = new BigDecimal[m][n];
		BigDecimal[][] aOrig = new BigDecimal[m][n];
		BigDecimal[][] r = new BigDecimal[n][n];
		
		for(int i = 0; i < p.degree(); i++){
			for(int j = 0; j < p.degree(); j++){
				a[i][j] = BigDecimal.ZERO;
				if((i - 1) == j) a[i][j] = BigDecimal.ONE;
				if(j == p.degree() - 1) a[i][j] = p.coeffs.get(i).negate().divide(p.getHighestCoeff(), Math.max(scale, Math.max(p.getHighestCoeff().scale(), p.coeffs.get(i).scale())), RoundingMode.HALF_UP);
				aOrig[i][j] = a[i][j];
				if(i < n) r[i][j] = BigDecimal.ZERO;
			}
		}
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(aOrig[i][j].compareTo(BigDecimal.ZERO) >= 0) System.out.print(" ");
				int newScale = 3-aOrig[i][j].precision()+aOrig[i][j].scale();
				BigDecimal bd2 = aOrig[i][j].setScale(newScale, RoundingMode.HALF_UP);
				//System.out.print(fixedLengthString(format(bd2, 2), 5) + " ");
				System.out.print(aOrig[i][j].setScale(scale, RoundingMode.HALF_UP).toPlainString() + " ");
			}
			System.out.println();
		}
		long t0 = System.nanoTime();
		boolean equalMatrix = false;
		while(!equalMatrix){
			BigDecimal d[] = new BigDecimal[m];
			
			for(int j = 0; j < n; j++){
				BigDecimal s = BigDecimal.ZERO;
				
				for(int i = j; i < m; i++){
					s = s.add(a[i][j].multiply(a[i][j]));
				}
				
				s = PolynomialRootFinding.nthRoot(s, 2, scale);
				d[j] = (a[j][j].compareTo(BigDecimal.ZERO) > 0) ? s.negate() : s;
				BigDecimal fak = PolynomialRootFinding.nthRoot((a[j][j].abs().add(s)).multiply(s), 2, scale);
				
				a[j][j] = a[j][j].subtract(d[j]);
				for(int k = j; k < m; k++) a[k][j] = a[k][j].divide(fak, scale, RoundingMode.HALF_UP);
				for(int i = j + 1; i < n; i++){
					s = BigDecimal.ZERO;
					for(int k = j; k < m; k++) s = s.add(a[k][j].multiply(a[k][i]));
					for(int k = j; k < m; k++) a[k][i] = a[k][i].subtract(a[k][j].multiply(s));
				}
			}
			
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					if(i == j){
						r[i][j] = d[j];
					}
					if(j > i) {
						r[i][j] = a[i][j];
					}
				}
			}
			
			
			BigDecimal Q[][] = new BigDecimal[m][n];
			for(int i = 0; i < m; i++){
				for(int j = 0; j < n; j++){
					Q[i][j] = BigDecimal.ZERO;
				}
			}
	
			for(int i = 0; i < m; i++){
				for(int j = 0; j < n; j++){
					BigDecimal subtr = BigDecimal.ZERO;
					for(int k = 0; k < j; k++){
						subtr = subtr.add(Q[i][k].multiply(r[k][j]));
					}
					Q[i][j] = aOrig[i][j].subtract(subtr).divide(r[j][j], scale, RoundingMode.HALF_UP);
				}
			}
			
			//square matrices...
			for(int i = 0; i < m; i++){
				for(int j = 0; j < m; j++){
					a[i][j] = BigDecimal.ZERO;
					for(int k = 0; k < m; k++){
						a[i][j] = a[i][j].add(r[i][k].multiply(Q[k][j]));
					}
				}
			}
			
			equalMatrix = true;
			
			for(int i = 0; i < m; i++){
				for(int j = 0; j < m; j++){
					if(j < i){
						if(aOrig[i][j].setScale(scale, RoundingMode.HALF_UP).compareTo(a[i][j].setScale(scale, RoundingMode.HALF_UP)) != 0){
							equalMatrix = false;
							break;
						}
					}
				}
			}
			
			for(int i = 0; i < m; i++){
				for(int j = 0; j < m; j++){
					aOrig[i][j] = a[i][j];
				}
			}
			/*
			System.out.println();
			System.out.println("Q");
			for(int i = 0; i < m; i++){
				for(int j = 0; j < n; j++){
					if(Q[i][j].compareTo(BigDecimal.ZERO) >= 0) System.out.print(" ");
					int newScale = 6-Q[i][j].precision()+Q[i][j].scale();
					BigDecimal bd2 = Q[i][j].setScale(newScale, RoundingMode.HALF_UP);
					System.out.print(fixedLengthString(format(bd2, 5), 10) + " ");
				}
				System.out.println();
			}
			
			System.out.println("R");
			System.out.println();
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					if(r[i][j].compareTo(BigDecimal.ZERO) >= 0) System.out.print(" ");
					int newScale = 6-r[i][j].precision()+r[i][j].scale();
					BigDecimal bd2 = r[i][j].setScale(newScale, RoundingMode.HALF_UP);
					System.out.print(fixedLengthString(format(bd2, 5), 10) + " ");
				}
				System.out.println();
			}*/
			
			System.out.println("R*Q");
			System.out.println();
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					if(aOrig[i][j].compareTo(BigDecimal.ZERO) >= 0) System.out.print(" ");
					int newScale = 6-aOrig[i][j].precision()+aOrig[i][j].scale();
					BigDecimal bd2 = aOrig[i][j].setScale(newScale, RoundingMode.HALF_UP);
					//System.out.print(fixedLengthString(format(bd2, 5), 10) + " ");
					System.out.print(aOrig[i][j].setScale(scale, RoundingMode.HALF_UP).toPlainString() + " ");
				}
				System.out.println();
			}
		}
		long t1 = System.nanoTime();
		System.out.println("time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		
		return roots;
	}
	
	private static String format(BigDecimal x, int scale) {
		  NumberFormat formatter = new DecimalFormat("0.0E0"); 
		  formatter.setRoundingMode(RoundingMode.HALF_UP);
		  formatter.setMinimumFractionDigits(scale);
		  return formatter.format(x);
		}
	
	public static String fixedLengthString(String string, int length) {
	    return String.format("%1$"+length+ "s", string);
	}
}