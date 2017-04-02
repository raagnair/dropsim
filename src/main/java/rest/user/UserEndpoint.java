package rest.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.ElephantDriver;
import engine.LogicEngine;
import engine.UserLE;
import model.user.UserLoginRequest;
import model.user.UserLoginResponse;

@Path("/user")
public class UserEndpoint {
	ElephantDriver driver;
	LogicEngine engine;

	public UserEndpoint() throws Exception {
		this.driver = ElephantDriver.getInstance();
		this.engine = LogicEngine.getInstance(this.driver);
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserLoginResponse login(UserLoginRequest request) {
		UserLoginResponse resp = new UserLoginResponse();

		boolean loginResult = UserLE.checkLogin(engine, request.getEmail(), request.getPassword());
		if (!loginResult) {
			resp.setResult(false);
			return resp;
		}

		resp.setResult(true);
		resp.setSid("123");
		return resp;

	}
}
