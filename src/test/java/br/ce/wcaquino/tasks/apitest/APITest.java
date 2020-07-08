package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
			.log().all()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveAdicionarTarefas() {
		RestAssured.given()
			.body("{ \"task\":\"teste via API\", \"dueDate\": \"2021-10-10\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefasInvalida() {
		RestAssured.given()
			.body("{ \"task\":\"teste via API\", \"dueDate\": \"2010-10-10\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		//Inserir
		Integer id = RestAssured.given()
			.body("{ \"task\":\"tarefa para remoção\", \"dueDate\": \"2021-10-10\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			//.log().all()
			.statusCode(201)
			.extract().path("id")
		;
		System.out.println(id);
		
		//Remover
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
}
