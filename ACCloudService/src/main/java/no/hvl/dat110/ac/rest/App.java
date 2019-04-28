package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();

		 	return gson.toJson("im the best");
		});
		
		// TODO: implement the routes required for the access control service
		//lagrer
		post("/accessdevice/log/", (request, response) -> {
			Gson gson = new Gson();
			AccessMessage body = gson.fromJson(request.body(), AccessMessage.class);
			int id = accesslog.add(body.getMessage());
			AccessEntry accessEntry = accesslog.get(id);
			//System.out.println(accessEntry.getId() + " " + accessEntry.getMessage());
			//System.out.println(accesslog.toJson());
			response.body(gson.toJson(accessEntry));
			return gson.toJson(accessEntry);
		});

		//viser alt i accesslog
		get("/accessdevice/log/", (req, res) -> accesslog.toJson());

		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson = new Gson();
			Integer id = Integer.parseInt(req.params(":id"));
			AccessEntry accessEntry = accesslog.get(id);
			return gson.toJson(accessEntry);
		});
		//oppdaterer tilgangskoden
		put("/accessdevice/code", (request, response) -> {
			Gson gson = new Gson();
			AccessCode newAccesscode = gson.fromJson(request.body(), AccessCode.class);
			accesscode.setAccesscode(newAccesscode.getAccesscode());
			response.body(gson.toJson(newAccesscode.getAccesscode()));
			//System.out.println(gson.toJson(newAccesscode.getAccesscode()));
			return gson.toJson(newAccesscode.getAccesscode());
		});
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode.getAccesscode());
		});
		delete("/accessdevice/log/", ((request, response) -> {
			accesslog.clear();
			return accesslog.toJson();
		}));
    }
    
}
