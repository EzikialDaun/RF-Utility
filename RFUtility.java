import java.util.Scanner;

class BridgeAttenuator {
	private double sourceRes = 0, middleRes = 0, impedanceRes = 0;

	public void setInfo(double sourceRes, double middleRes, double impedanceRes) {
		this.sourceRes = sourceRes;
		this.middleRes = middleRes;
		this.impedanceRes = impedanceRes;
	}

	public double getSourceResistor() {
		return this.sourceRes;
	}

	public double getMiddleResistor() {
		return this.middleRes;
	}

	public double getImpedanceResistor() {
		return this.impedanceRes;
	}

	public void print() {
		System.out.println("*******R 1*******");
		System.out.println("   *         *   ");
		System.out.println("   *         *   ");
		System.out.println("  R o       R o  ");
		System.out.println("   *         *   ");
		System.out.println("   *         *   ");
		System.out.println("   ***********   ");
		System.out.println("        *        ");
		System.out.println("        *        ");
		System.out.println("       R 2       ");
		System.out.println("        *        ");
		System.out.println("        *        ");
		System.out.printf("R1    : %.1f目\n", this.middleRes);
		System.out.printf("Ro, Ro: %.1f目\n", this.impedanceRes);
		System.out.printf("R2    : %.1f目\n", this.sourceRes);
	}
}

class PIAttenuator {
	private double wingRes = 0, middleRes = 0;

	public void setInfo(double wingRes, double middleRes) {
		this.wingRes = wingRes;
		this.middleRes = middleRes;
	}

	public void print() {
		System.out.println("*******R 3*******");
		System.out.println("   *         *   ");
		System.out.println("   *         *   ");
		System.out.println("  R 1       R 2  ");
		System.out.println("   *         *   ");
		System.out.println("   *         *   ");
		System.out.printf("R3    : %.1f目\n", this.middleRes);
		System.out.printf("R1, R2: %.1f目\n", this.wingRes);
	}
}

class RFCalculator {
	private static double impedance = 50;

	public static double getImpedance() {
		return impedance;
	}

	public static PIAttenuator getPIAttenuator(double attenuation) {
		PIAttenuator piAttenuator = new PIAttenuator();
		double middleRes = (impedance / 2) * (Math.pow(10, (attenuation / 10)) - 1)
				* Math.pow(10, ((-1 * attenuation) / 20));
		double wingRes = 1
				/ (((Math.pow(10, attenuation / 10) + 1) / (impedance * (Math.pow(10, attenuation / 10) - 1)))
						- (1 / middleRes));
		piAttenuator.setInfo(wingRes, middleRes);
		return piAttenuator;
	}

	public static BridgeAttenuator getBridgeAttenuator(double attenuation) {
		BridgeAttenuator bridgeAttenuator = new BridgeAttenuator();
		double middleRes = impedance * (Math.pow(10, attenuation / 20) - 1);
		double sourceRes = impedance / (Math.pow(10, attenuation / 20) - 1);
		bridgeAttenuator.setInfo(sourceRes, middleRes, impedance);
		return bridgeAttenuator;
	}
}

public class RFUtility {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		double attenuation = 0, watt = 0, decibelMilliwatte = 0;
		String op;
		do {
			printMenu();
			op = scanner.next();
			printBoard();
			switch (op) {
			case "1":
				System.out.println("[PI ⑽鷓 馬潸晦 啗骯]");
				System.out.print("馬潸榆(dB)擊 殮溘п輿撮蹂. --> ");
				attenuation = scanner.nextDouble();
				PIAttenuator pi = RFCalculator.getPIAttenuator(attenuation);
				printBoard();
				pi.print();
				break;
			case "2":
				System.out.println("[粽葩雖 ⑽鷓 馬潸晦 啗骯]");
				System.out.print("馬潸榆(dB)擊 殮溘п輿撮蹂. --> ");
				attenuation = scanner.nextDouble();
				BridgeAttenuator bridge = RFCalculator.getBridgeAttenuator(attenuation);
				printBoard();
				bridge.print();
				break;
			case "3":
				System.out.println("[瞪溘 滲??(W -> dBm)]");
				System.out.print("瞪溘(W)擊 殮溘п輿撮蹂. --> ");
				watt = scanner.nextDouble();
				decibelMilliwatte = (10 * Math.log10(watt)) + 30;
				printBoard();
				System.out.printf("%.3fW = %.3fdBm\n", watt, decibelMilliwatte);
				break;
			case "4":
				System.out.println("[瞪溘 滲??(dBm -> W)]");
				System.out.print("轎溘(dBm)擊 殮溘п輿撮蹂. --> ");
				decibelMilliwatte = scanner.nextDouble();
				watt = (Math.pow(10, decibelMilliwatte / 10)) / 1000;
				printBoard();
				System.out.printf("%.3fdBm = %.3fW\n", decibelMilliwatte, watt);
				break;
			default:
				break;
			}
		} while (!op.equals("0"));
		scanner.close();
	}

	public static void printBoard() {
		System.out.println("-----------------");
	}

	public static void printMenu() {
		printBoard();
		System.out.println("[RF 嶸せ葬じ 啗骯晦 詭景]");
		System.out.println("1. PI ⑽鷓 馬潸晦 啗骯");
		System.out.println("2. 粽葩雖 ⑽鷓 馬潸晦 啗骯");
		System.out.println("3. 瞪溘 滲??(W -> dBm)");
		System.out.println("4. 瞪溘 滲??(dBm -> W)");
		System.out.println("0. 謙猿");
		System.out.print("--> ");
	}
}