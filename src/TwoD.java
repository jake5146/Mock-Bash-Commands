
public class TwoD {
	
	double width;
	double height;
	
	public TwoD(double w, double h) {
		this.width = w;
		this.height = h;
		
	}

}
 class Triangle extends TwoD {
	 
	 public Triangle(double w, double h) {
		 super(w, h);
		 
	 }
	 public double area() {
		 return width * height;
	 }
	 public static void main(String args[]){
		 Triangle t1 = new Triangle(6.0d, 7.0d);
		 Triangle t2 = new Triangle(4.0d, 5.0d);
		 
		 System.out.println(t1.area());
		 System.out.println(t2.area());
	}
 }