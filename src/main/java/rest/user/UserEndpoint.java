package rest.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.ElephantDriver;
import engine.LogicEngine;
import engine.UserLE;
import model.user.SessionRequest;
import model.user.SessionResponse;
import model.user.UserLoginRequest;
import model.user.UserLoginResponse;
import services.UserSessionService;

@Path("/user")
public class UserEndpoint {
	ElephantDriver driver;
	LogicEngine engine;
	UserSessionService session;

	public UserEndpoint() throws Exception {
		this.driver = ElephantDriver.getInstance();
		this.engine = LogicEngine.getInstance();
		this.session = UserSessionService.getInstance();
	}

	public UserEndpoint(ElephantDriver driver, LogicEngine engine, UserSessionService session) {
		this.driver = driver;
		this.engine = engine;
		this.session = session;
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

		String sid = this.session.insertNewSession(request.getEmail());
		resp.setSid(sid);
		return resp;
	}

	@POST
	@Path("/session")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SessionResponse checkSession(SessionRequest request) {
		SessionResponse resp = new SessionResponse();

		String sid = request.getSid();
		if (sid == null) {
			resp.setValid(false);
			resp.setEmail(null);
			return resp;
		}

		String email = this.session.getEmailForSession(sid);
		if (email == null) {
			resp.setValid(false);
			resp.setEmail(null);
			return resp;
		}

		resp.setValid(true);
		resp.setEmail(email);
		return resp;
	}
}
