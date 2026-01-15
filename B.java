import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class B {
  public static void main(String[] args){
    var enc=new BCryptPasswordEncoder();
    String[] pw={"Developer123","Hospital123","Rahul123","Neha123","Amit123","Priya123"};
    for(String p:pw){
      System.out.println(p+" => "+enc.encode(p));
    }
  }
}
