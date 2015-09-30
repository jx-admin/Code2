package j.wu.http;

public class Main {
	public static void main(String[] args) {
        //发送 GET 请求
        String s=HttpRequest.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
        System.out.println(s);
        
        //发送 POST 请求
//        String sr=HttpRequest.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");
        String sr=HttpRequest.sendPost("http://localhost:6144/Home/RequestPostString", file,"wjx","52aac39c0cf2a86c1c7e73fa","526f7fe70cf24fd8a418ed02");
        System.out.println(sr);
    }
}
