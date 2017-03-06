package cz.fi.muni.polynomialroots;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javafx.util.Pair;

public class PolyRoots {
	public static void main2(String[] args) {
		//String line = linesFromStream(System.in).get(0);
		//Polynomial p = new Polynomial(line);
		//Polynomial p = new Polynomial("100.684989x^15+12.9987765x^5-1.657984655563x-3.587894");
		//Polynomial p = new Polynomial("x^3-2x^2-11x+12");
		Polynomial v = new Polynomial("x^3-2x+2");
		Polynomial onediv = new Polynomial("x^2+x+1");
		onediv.substituteThisWithOne_div_X_plus_1_multiplied_x_n();
		System.out.println(onediv.toString());
		Polynomial w = new Polynomial("100x^10-x");//err
		/**///Polynomial p = new Polynomial("x^150-4x+2");
		Polynomial p = new Polynomial("5x^4-10x^3+4x^2+x-0.2");//problem je ak sa nastavi 0.5 ako end interval, lebo tam je derivacia nulova..
		//Polynomial q = new Polynomial("-2.1746184423724472E-57x^51");
		Polynomial q = new Polynomial("-2.1746184423724472E-57x^51 "
		+ "+1.9287852479043222E-57x^50"
		+ "+6.94362689245552E-56x^49" 
		+ "+2.4497115676583277E-54x^48"
		+ "+8.466203177827045E-53x^47"
		+ "+2.864963155376615E-51x^46 "
		+ "+9.488757970606674E-50x^45 "
		+ "+3.0743575824770504E-48x^44 "
		+ "+9.739564821286444E-47x^43 "
		+ "+3.0153692686701596E-45x^42 "
		+ "+9.118476668458976E-44x^41 "
		+ "+2.691774312529048E-42x^40 "
		+ "+7.752310020083752E-41x^39 "
		+ "+2.1768486536394586E-39x^38 "
		+ "+5.955857916357629E-38x^37 "
		+ "+1.5866405489177422E-36x^36 "
		+ "+4.112572302794665E-35x^35 "
		+ "+1.0363682203042724E-33x^34 "
		+ "+2.537029403304727E-32x^33 "
		+ "+6.027981862252545E-31x^32 "
		+ "+1.3888470210629123E-29x^31 "
		+ "+3.09990655101247E-28x^30 "
		+ "+6.695798150186664E-27x^29 "
		+ "+1.3980826537590458E-25x^28 "
		+ "+2.8185346299781724E-24x^27 "
		+ "+5.479231320677526E-23x^26 "
		+ "+1.0257121032308399E-21x^25 "
		+ "+1.846281785815487E-20x^24 "
		+ "+3.190374925889297E-19x^23 "
		+ "+5.2832608772725294E-18x^22 "
		+ "+8.3686852295996E-17x^21 "
		+ "+1.2653452067154743E-15x^20 " 
		+ "+1.8220970976702784E-14x^19 "
		+ "+2.4926288296129526E-13x^18 "
		+ "+3.230446963178366E-12x^17 "
		+ "+3.9540670829303454E-11x^16 "
		+ "+4.5550852795357344E-10x^15 "
		+ "+4.91949210189862E-9x^14 "
		+ "+4.958848038713773E-8x^13 "
		+ "+4.641481764236126E-7x^12 "
		+ "+4.0102402443000056E-6x^11 "
		+ "+3.176110273485604E-5x^10 "
		+ "+2.286799396909629E-4x^9 "
    	+ "-1.8505213143769125E-4x^8 " 
		+ "-0.002132715086978482x^7 "  
		+ "-0.011346300331785044x^6 "
		+ "-0.04930277725343741x^5  "
		+ "-0.17760470204003656x^4  "
		+ "-0.5115382471310665x^3  "
		+ "-1.1049314230641736x^2 "
		+ "-1.5911026586938277x^1 "
		+ "-1.145594027018035");
		
		Polynomial r = new Polynomial("0.11579758847999998+ 0.16081969088102396x^1 + 0.11167319334778306x^2 "
				+ "+ 0.05169724364046703x^3 + 0.017949282991970156x^4 + 0.00498559284384963x^5 "
				+ "+ 0.001153998556923061x^6 +2.2895331369353531E-4x^7 + 3.9746295257197735E-5x^8 "
				+ "+ 6.133294983688468E-6x^9 + 8.517920073346545E-7x^10 + 1.0754261270785166E-7x^11 "
				+ "+ 1.2446265044055366E-8x^12 + 1.3296440687064687E-9x^13 + 1.319006916156817E-10x^14 "
				+ "+ 1.2212245367723915E-11x^15 + 1.0600228979184358E-12x^16 + 8.659763533112493E-14x^17 "
				+ "+ 6.68148866377035E-15x^18 + 4.883816555918033E-16x^19 + 3.391322216429482E-17x^20 "
				+ "+ 2.242794425798698E-18x^21 + 1.4158149538860143E-19x^22 + 8.549060034595204E-21x^23 "
				+ "+ 4.947056073352424E-22x^24 + 2.7481885898687387E-23x^25 + 1.467955505234502E-24x^26 "
				+ "+ 7.550728169146949E-26x^27 + 3.745161171896887E-27x^28 + 1.7935447708725506E-28x^29 "
				+ "+ 8.302916592625995E-30x^30 + 3.7197066334964457E-31x^31 + 1.6143526789374575E-32x^32 "
				+ "+ 6.793978789419216E-34x^33 + 2.7751405125721786E-35x^34 + 1.1011757553886406E-36x^35 "
				+ "+ 4.2480913585659554E-38x^36 + 1.5945268321017296E-39x^37 + 5.827575959007584E-41x^38 "
				+ "+ 2.07521474150506E-42x^39 + 7.205145582505568E-44x^40 + 2.4406112646301788E-45x^41 "
				+ "+ 8.070287915043791E-47x^42 + 2.6065153154448417E-48x^43 + 8.227110159294991E-50x^44 "
				+ "+ 2.5390690198286415E-51x^45 + 7.6657805537782985E-53x^46 +2.2651566027845323E-54x^47 "
				+ "+ 6.55385310405658E-56x^48 + 1.857549222635465E-57x^49 + 5.159528720792268E-59x^50 "
				+ "+ 1.4050104877326083E-60x^51 +3.75245877954432E-62x^52 + 9.832858024587079E-64x^53 "
				+ "+ 2.5288654119530617E-65x^54 + 6.385615062037113E-67x^55 + 1.5836325353852041E-68x^56 "
				+ "+ 3.858506780952582E-70x^57 + 9.239127961011974E-72x^58 + 2.1747967647887167E-73x^59 "
				+ "+ 5.0339295782309505E-75x^60 + 1.1460854751224824E-76x^61 + 2.5672314642743605E-78x^62 "
				+ "+ 5.659319139022591E-80x^63 + 1.2280722531679022E-81x^64 + 2.6239180695378193E-83x^65 "
				+ "+ 5.521359719657763E-85x^66 + 1.1444872206956272E-86x^67 + 2.3374468413265987E-88x^68 "
				+ "+ 4.704704598890407E-90x^69 + 9.334133924198568E-92x^70 + 1.8258091822432352E-93x^71 "
				+ "+ 3.5217830448602845E-95x^72 + 6.700071633838306E-97x^73 + 1.2574404709560325E-98x^74 "
				+ "+ 2.3284444347516507E-100x^75 + 4.2549258302409105E-102x^76 + 7.674338951998153E-104x^77 "
				+ "+ 1.3664258892993635E-105x^78 + 2.4021421203277925E-107x^79 + 4.170118720889048E-109x^80 "
				+ "+ 7.149951703173715E-111x^81 + 1.210957673825324E-112x^82 + 2.0262385751910964E-114x^83 "
				+ "+ 3.350047777649279E-116x^84 + 5.4735839454109635E-118x^85 + 8.83920160858924E-120x^86 "
				+ "+ 1.4110210567826133E-121x^87 + 2.2268477768860153E-123x^88 + 3.4748833624037053E-125x^89");
		//System.out.println(q.toString());
		//Polynomial p = new Polynomial("-2.1746184423724472E-57*x^51+1.9287852479043222E-57*x^50+6.94362689245552E-56*x^49+2.4497115676583277E-54*x^48+8.466203177827045E-53*x^47+2.864963155376615E-51*x^46+9.488757970606674E-50*x^45+3.0743575824770504E-48*x^44+9.739564821286444E-47*x^43+3.0153692686701596E-45*x^42+9.118476668458976E-44*x^41+2.691774312529048E-42*x^40+7.752310020083752E-41*x^39+2.1768486536394586E-39*x^38+5.955857916357629E-38*x^37+1.5866405489177422E-36*x^36+4.112572302794665E-35*x^35+1.0363682203042724E-33*x^34+2.537029403304727E-32*x^33+6.027981862252545E-31*x^32+1.3888470210629123E-29*x^31+3.09990655101247E-28*x^30+6.695798150186664E-27*x^29+1.3980826537590458E-25*x^28+2.8185346299781724E-24*x^27+5.479231320677526E-23*x^26+1.0257121032308399E-21*x^25+1.846281785815487E-20*x^24+3.190374925889297E-19*x^23+5.2832608772725294E-18*x^22+8.3686852295996E-17*x^21+1.2653452067154743E-15*x^20+1.8220970976702784E-14*x^19+2.4926288296129526E-13*x^18+3.230446963178366E-12*x^17+3.9540670829303454E-11*x^16+4.5550852795357344E-10*x^15+4.91949210189862E-9*x^14+4.958848038713773E-8*x^13+4.641481764236126E-7*x^12+4.0102402443000056E-6*x^11+3.176110273485604E-5*x^10+2.286799396909629E-4*x^9-1.8505213143769125E-4*x^8-0.002132715086978482*x^7-0.011346300331785044*x^6-0.04930277725343741*x^5-0.17760470204003656*x^4-0.5115382471310665*x^3-1.1049314230641736*x^2-1.5911026586938277*x^1+1.145594027018035");
		//Polynomial p = new Polynomial("2.1686548x^244-23.5684x^243-47.5846848965");
		//Polynomial p = new Polynomial("2x^14-23x^13-47");
		BigDecimal eps = new BigDecimal("0.0000000000000000000000000000000000000000000001");//45smaller than prcs
		BigDecimal prcs = new BigDecimal("0.000000000000000000000000000000000000000001");//42rovnako zarovnane
		BigDecimal eps2 = new BigDecimal("0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");//smaller than prcs
		BigDecimal prcs2 = new BigDecimal("0.000000000000000000000000000000000000000000000000000000000000000000001");//rovnako zarovnane
		
		//prcs.setScale(9, RoundingMode.HALF_UP);
		List<BigDecimal> roots = //NewtonRootFinder.findRoot(p, prcs, new BigDecimal("9"), new BigDecimal("11"), new BigDecimal("12"));
				PolynomialRootFinding.findRootsNewtonHorner(p, new BigDecimal("0"), new BigDecimal("1"), prcs2, eps2);
		List<BigDecimal> rootsq = PolynomialRootFinding.findRootsNewtonHorner(q, new BigDecimal("-0.01"), new BigDecimal("0.01"), prcs2, eps2);
		
		/*List<BigDecimal> trueroots = new ArrayList<BigDecimal>();
		
		for(int i = 0; i < roots.size(); i++){
			if(p.value(roots.get(i)).abs().compareTo(prcs.multiply(new BigDecimal("100"))) < 0){
				trueroots.add(roots.get(i));
			}
		}
		*/
		System.out.println("roots");
		for(int i = 0; i < roots.size(); i++){
			System.out.print((roots.get(i).toString().charAt(0) != '-')?" ":"");
			System.out.println(roots.get(i).stripTrailingZeros().toPlainString());
		}
		System.out.println("end");
		
		List<BigDecimal> roots2 = //NewtonRootFinder.findRoot(p, prcs, new BigDecimal("9"), new BigDecimal("11"), new BigDecimal("12"));
				PolynomialRootFinding.findRootsNewtonHorner(w, new BigDecimal("-1"), new BigDecimal("1"), prcs2, eps2);
		
		System.out.println("roots2");
		for(int i = 0; i < roots2.size(); i++){
			System.out.print((roots2.get(i).toString().charAt(0) != '-')?" ":"");
			System.out.println(roots2.get(i).stripTrailingZeros().toPlainString());
		}
		System.out.println("end");
		
		System.out.println("rootsq");
		for(int i = 0; i < rootsq.size(); i++){
			System.out.println(rootsq.get(i));
		}
		System.out.println("end");
		
		List<BigDecimal> rootsv = PolynomialRootFinding.findRootsNewtonHorner(v, new BigDecimal("-3"), new BigDecimal("-1.5"), prcs, eps);
		System.out.println("rootsv");
		for(int i = 0; i < rootsv.size(); i++){
			System.out.println(rootsv.get(i));
		}
		System.out.println("end");
		
		System.out.println("compare poly test");
		
		Polynomial x1 = new Polynomial("2x^3+x^2+x-1");
		Polynomial x2 = new Polynomial("3x^3+x^2+x-1");
		Polynomial x3 = new Polynomial("x^3+x^2+x-1");
		Polynomial x4 = new Polynomial("x^3+x^2+x");
		Polynomial x5 = new Polynomial("x^3-3x");
		Polynomial x6 = new Polynomial("0x^2+0.0x+0");
		Polynomial x7 = new Polynomial("0");
		Polynomial x8 = new Polynomial("5x-3");
		System.out.println("Cauchy " + x8.toString() + ": " + PolynomialRootFinding.cauchyBound(x8, 10));
		System.out.println(x1.compareTo(x2));
		System.out.println(x1.compareTo(x3));
		System.out.println(x4.compareTo(x5));
		System.out.println(x5.compareTo(x4));
		System.out.println(x3.compareTo(x7));
		System.out.println(x6.compareTo(x7));
		x5.multiply(x1);
		//for(BigDecimal d : x5.coeffs) System.out.println("coef: " + d);
		
		Polynomial p0 = new Polynomial("x^4+x^3-x-1");
		BigDecimal s = new BigDecimal("-2");
		BigDecimal e = new BigDecimal("2");
		/*int numberOfRoots = sturm(p0, s, e, 20);
		System.out.println("The number of roots of p0 in ("+ s +", "+ e +"> = " + numberOfRoots);
		List<BigDecimal> rootsP = rootsOfPolynomialInIntervalUsingSturmTheoremAndBisection(p0, s, e, 10);
		System.out.println("The roots of p0 in ("+ s +", "+ e +"> = " + rootsP.toString());
		System.out.println("Roots of p0: " + findRootNewtonInInterval(p0, 10, s, e).getKey());
		
		*/
		BigDecimal sq = new BigDecimal("-20");
		BigDecimal eq = new BigDecimal("35");/*
		System.out.println("The number of roots of q in ("+ sq +", "+ eq +"> = " + sturm(q, sq, eq, 6));
		System.out.println("Cauchy q : " + cauchyBound(q, 10));
		System.out.println("Kojima q : " + kojimaBound(q, 10));
		System.out.println("PDF q : " + rootBound(q, 10));
		long t0 = System.nanoTime();
		List<BigDecimal> rootsQ2 = rootsOfPolynomialInIntervalUsingSturmTheoremAndBisectionAndNewtonMethod(q, sq, eq, 100);
		long t1 = System.nanoTime();
		System.out.println("Sturm+Newton The roots of q in ("+ sq +", "+ eq +"> = " + rootsQ2.toString());
		System.out.println("Sturm time: " + TimeUnit.SECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS));
		*/
		BigDecimal sr = new BigDecimal("-20");
		BigDecimal er = new BigDecimal("0");/*
		List<BigDecimal> rootsR = rootsOfPolynomialInIntervalUsingSturmTheoremAndBisectionAndNewtonMethod(r, sr, er, 75);
		System.out.println("Sturm+Newton The roots of r in ("+ sr +", "+ er +"> = " + rootsR.toString());
		System.out.println("Newton root of R: " + findRootNewton(r, 100, sr.add(er).divide(TWO, 10, RoundingMode.HALF_UP)).getKey());
		System.out.println("Halley root of R: " + findRootHalley(r, 100, sr.add(er).divide(TWO, 10, RoundingMode.HALF_UP)).getKey());
		System.out.println("Cauchy r : " + cauchyBound(r, 10));
		System.out.println("Kojima r : " + kojimaBound(r, 10));
		System.out.println("PDF r : " + rootBound(r, 10));
		//pripomenut ze Maple 3/5 korenov bola chyba, boli to rozne polynomy(lisili sa o znamienko na absolutnom clenovi
		
		Polynomial VCAtest = new Polynomial("x^3-7x+7");
		System.out.println("Cauchy VCAtest : " + cauchyBound(VCAtest, 10));
		System.out.println("Kojima VCAtest : " + kojimaBound(VCAtest, 10));
		System.out.println("PDF VCAtest : " + rootBound(VCAtest, 10));
		List<Pair<BigDecimal, BigDecimal>> VCAresult = VCA(VCAtest, new BigDecimal("4"), 10);
		System.out.println("VCA result");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vcai : VCAresult){
			System.out.print("(" + vcai.getKey() + ", " + vcai.getValue() + ") ");
		}
		System.out.println("}");
		
		List<Pair<BigDecimal, BigDecimal>> VCAresultInterval = VCAinterval(VCAtest, BigDecimal.ONE, new BigDecimal("4"), 10);
		System.out.println("VCA interval result");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vcai : VCAresultInterval){
			System.out.print("(" + vcai.getKey() + ", " + vcai.getValue() + ") ");
		}
		System.out.println("}");
		
		List<Pair<BigDecimal, BigDecimal>> VCAresultsPositive = VCA(q, new BigDecimal("50"), 1);
		System.out.println("VCA positive results");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vcai : VCAresultsPositive){
			System.out.print("(" + vcai.getKey() + ", " + vcai.getValue() + ") ");
		}
		System.out.println("}");
		
		List<Pair<BigDecimal, BigDecimal>> VCAresultsNegative = VCA(q.substituteWithMinusX(), new BigDecimal("50"), 1);
		System.out.println("VCA negative results");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vcai : VCAresultsNegative){
			System.out.print("(" + vcai.getKey().negate() + ", " + vcai.getValue().negate() + ") ");
		}
		System.out.println("}");
		
		List<Pair<BigDecimal, BigDecimal>> VCAresultInterval2 = VCAinterval(q, sq, eq, 10);
		System.out.println("VCA interval result");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vcai : VCAresultInterval2){
			System.out.print("(" + vcai.getKey() + ", " + vcai.getValue() + ") ");
		}
		System.out.println("}");
		*/
		long t0 = System.nanoTime();
		List<BigDecimal> rootsQ3 = PolynomialRootFinding.findRootsOfPolynomialInIntervalUsingVCAAndNewton(q, sq, eq, 20);
		long t1 = System.nanoTime();

		System.out.println("VCA+Newton The roots of q in ("+ sq +", "+ eq +") = " + rootsQ3.toString());
		System.out.println("VCA+newton time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		/*List<BigDecimal> rootsQd = findRootsOfPolynomialInIntervalUsingVCAAndNewton(q.derivative(), sq, eq, 20);
		System.out.println("VCA+Newton The roots of derivative q in ("+ sq +", "+ eq +") = " + rootsQd.toString());
		for(BigDecimal bd : rootsQd) System.out.println(bd.toString() + ": " + q.value(bd).setScale(20, RoundingMode.HALF_UP).toString());
		List<BigDecimal> rootsQdd = findRootsOfPolynomialInIntervalUsingVCAAndNewton(q.derivative().derivative(), sq, eq, 20);
		System.out.println("VCA+Newton The roots of double derivative q in ("+ sq +", "+ eq +") = " + rootsQdd.toString());
		for(BigDecimal bd : rootsQdd) System.out.println(bd.toString() + ": " + q.value(bd).setScale(20, RoundingMode.HALF_UP).toString());*/
		
		t0 = System.nanoTime();
		List<BigDecimal> rootsQ4 = PolynomialRootFinding.findRootsOfPolynomialInIntervalUsingVCAAndHalley(q, sq, eq, 100);
		t1 = System.nanoTime();

		System.out.println("VCA+Halley The roots of q in ("+ sq +", "+ eq +") = " + rootsQ4.toString());
		System.out.println("VCA+halley time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		List<BigDecimal> rootsR = PolynomialRootFinding.findRootsOfPolynomialInIntervalUsingVCAAndNewton(r, sr, er, 100);
		System.out.println("VCA+Newton The roots of r in ("+ sr +", "+ er +") = " + rootsR.toString());
/*		List<BigDecimal> rootsP = findRootsOfPolynomialInIntervalUsingVCAAndNewton(p, TWO.negate(), TWO, 100);
		System.out.println("VCA+Newton The roots of p in ("+ TWO.negate() +", "+ TWO +") = " + rootsP.toString());
		List<BigDecimal> rootsW = findRootsOfPolynomialInIntervalUsingVCAAndNewton(w, BigDecimal.ONE.negate(), TWO, 100);
		System.out.println("VCA+Newton The roots of w in ("+ BigDecimal.ONE.negate() +", "+ TWO +") = " + rootsW.toString());
		
		Polynomial multipl = new Polynomial("x^4-8x^2+16");
		List<BigDecimal> rootsMultipl = findRootsOfPolynomialInIntervalUsingVCAAndNewton(multipl, THREE.negate(), THREE, 10);
		System.out.println("VCA+Newton The roots of "+multipl.toString()+" in ("+ THREE.negate() +", "+ THREE +") = " + rootsMultipl.toString());
		*/
		t0 = System.nanoTime();
		List<Pair<BigDecimal, BigDecimal>> VASresultInterval = PolynomialRootFinding.vas(new Polynomial("8x^4-18x^3+9x-2"), new Mobius(), 20);
		System.out.println("VAS interval result");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vasi : VASresultInterval){
			System.out.print("(" + vasi.getKey() + ", " + vasi.getValue() + ") ");
		}
		System.out.println("}");
		t1 = System.nanoTime();

		System.out.println("VAS time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		
		System.out.println("Cauchy q : " + PolynomialRootFinding.cauchyBound(q, 10));
		System.out.println("Matik pdf q : " + PolynomialRootFinding.rootBound(q, 10));
		
		t0 = System.nanoTime();
		List<BigDecimal> rootsQ5 = PolynomialRootFinding.findRootsVAS(q, 20);
		t1 = System.nanoTime();

		System.out.println("VAS+Halley The roots of q in ("+ sq +", "+ eq +") = " + rootsQ5.toString());
		System.out.println("VAS+halley time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");
		
		/*
		t0 = System.nanoTime();
		List<Pair<BigDecimal, BigDecimal>> VASresultIntervalder2 = vas(q.derivative().derivative(), new Mobius(), 20, BigDecimal.ZERO,new BigDecimal("35"));
		System.out.println("VAS interval result");
		System.out.print("{ ");
		for(Pair<BigDecimal, BigDecimal> vasi : VASresultIntervalder2){
			System.out.print("(" + vasi.getKey() + ", " + vasi.getValue() + ") ");
		}
		System.out.println("}");
		t1 = System.nanoTime();

		System.out.println("VAS time: " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS) + "ms");*/
		Mobius testm = new Mobius();
		System.out.println(testm.value(new BigDecimal("47.31337"), 10));
		//TODO : OSETRIT VSETKY DELENIA PRE PRIPAD DELENIA NULOU! treba odchytit a osetrit vynimku
		System.out.println("0.1 with 15-1 zeros: " + BigDecimal.ONE.divide(BigDecimal.TEN.pow(15), 15, RoundingMode.HALF_UP).toPlainString() );
		System.out.println(x3.toString());
		System.out.println(x3.substituteWithMinusX().toString());
		System.out.println("50ta odmocina z 2.1746184423724472E-57: " + PolynomialRootFinding.nthRoot(new BigDecimal("2.1746184423724472E-57"), 50, 100));
		System.out.println("druha odmocina z 2: " + PolynomialRootFinding.nthRoot(PolynomialRootFinding.TWO, 2, 10));
		System.out.println("tretia odmocina z 8: " + PolynomialRootFinding.nthRoot(new BigDecimal("8"), 3, 10));
		//System.out.println("upper bound vigklas: " + vigklasBound(q, 65).toString());
		q.multiplyWithScalar(BigDecimal.ONE.negate());
		System.out.println("upper bound vigklas: " + PolynomialRootFinding.vigklasBound2(q, 100).toString());
		q.multiplyWithScalar(BigDecimal.ONE.negate());
		System.out.println("upper bound vigklas: " + PolynomialRootFinding.vigklasBound2(q.substituteWithMinusX(), 100).toString());
		System.out.println("upper bound fujiwara: " + PolynomialRootFinding.fujiwaraBound(r, 15).toString());
		//System.out.println("bounds: " + bounds(q, 150).toString());
		Polynomial o = new Polynomial("5x^4-10x^3+4x^2+x-0.25");
		PolynomialRootFinding.findRootsEigen(o, 6);
	}
	public static void main(String[] args) {
		Polynomial x1 = new Polynomial("2x^4-3x-2");
		Polynomial x2 = new Polynomial("x^6-x-1");
		PolynomialRootFinding.bisection(x2, new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("0.00005")).toString();
	}
}
