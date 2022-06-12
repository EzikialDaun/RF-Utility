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
		System.out.printf("R1    : %.1fΩ\n", this.middleRes);
		System.out.printf("Ro, Ro: %.1fΩ\n", this.impedanceRes);
		System.out.printf("R2    : %.1fΩ\n", this.sourceRes);
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
		System.out.printf("R3    : %.1fΩ\n", this.middleRes);
		System.out.printf("R1, R2: %.1fΩ\n", this.wingRes);
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
				System.out.println("[PI 형태 감쇠기 계산]");
				System.out.print("감쇠량(dB)을 입력해주세요. --> ");
				attenuation = scanner.nextDouble();
				PIAttenuator pi = RFCalculator.getPIAttenuator(attenuation);
				printBoard();
				pi.print();
				break;
			case "2":
				System.out.println("[브릿지 형태 감쇠기 계산]");
				System.out.print("감쇠량(dB)을 입력해주세요. --> ");
				attenuation = scanner.nextDouble();
				BridgeAttenuator bridge = RFCalculator.getBridgeAttenuator(attenuation);
				printBoard();
				bridge.print();
				break;
			case "3":
				System.out.println("[전력 변환(W -> dBm)]");
				System.out.print("전력(W)을 입력해주세요. --> ");
				watt = scanner.nextDouble();
				decibelMilliwatte = (10 * Math.log10(watt)) + 30;
				printBoard();
				System.out.printf("%.3fW = %.3fdBm\n", watt, decibelMilliwatte);
				break;
			case "4":
				System.out.println("[전력 변환(dBm -> W)]");
				System.out.print("출력(dBm)을 입력해주세요. --> ");
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
		System.out.println("[RF 유틸리티 계산기 메뉴]");
		System.out.println("1. PI 형태 감쇠기 계산");
		System.out.println("2. 브릿지 형태 감쇠기 계산");
		System.out.println("3. 전력 변환(W -> dBm)");
		System.out.println("4. 전력 변환(dBm -> W)");
		System.out.println("0. 종료");
		System.out.print("--> ");
	}
}