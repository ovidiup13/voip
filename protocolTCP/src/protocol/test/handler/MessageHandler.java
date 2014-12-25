package protocol.test.handler;

import protocol.config.buffers.ClientRequest.Request;
import protocol.config.buffers.ClientRequest.Request.ReqType;


public class MessageHandler {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Request req = Request.newBuilder().setRqType(Request.ReqType.REG).setTimestamp("this").build();
		ReqType type = req.getRqType();
		System.out.println(type);
		System.out.println(req.getTimestamp());
		
		System.out.println("Do this");
		
		Object obj = Request.newBuilder().setRqType(Request.ReqType.REG).setTimestamp("this").build();
		
		System.out.println(obj.getClass());
	}

}
