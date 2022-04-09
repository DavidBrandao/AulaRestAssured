package aulaRA;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class ServiceHelper {

    public String PostUsuarios(String nome){
        String email = nome + "@cesar.school";

        String usuarioID =
                given()
                        .body("{\n" +
                        "  \"nome\":\""+nome+"\",\n" +
                        "  \"email\": \""+ email+"\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                        .contentType(ContentType.JSON)
                .when()
                        .post("/usuarios")
                .then().log().all()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("message", is("Cadastro realizado com sucesso"))
                        .extract().path("_id");
        return usuarioID;
    }

    public void ExcluirUsuario(String usuarioID){
        given()
                .pathParam("_id", usuarioID)
                .when()
                .delete("/usuarios/{_id}")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    public void ExcluirUsuarioCarrinhoAssociado(String usuarioID){
        given()
                .pathParam("_id", usuarioID)
                .when()
                .delete("/usuarios/{_id}")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    public String GetUsuarioToken(String nome){
        String email = nome + "@cesar.school";
        String usuarioToken =  given()
                .body("{\n" +
                        "  \"email\": \""+ email+"\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", is("Login realizado com sucesso"))
                .extract().path("authorization");
        return usuarioToken;
    }

    public String PostProduto(String prod, String usuarioToken){
        String produtoID =
                given()
                        .header("authorization", usuarioToken)
                        .body("{\n" +
                                "  \"nome\": \""+prod+"\",\n" +
                                "  \"preco\": 100,\n" +
                                "  \"descricao\": \"Massa\",\n" +
                                "  \"quantidade\": 100\n" +
                                "}")
                        .contentType(ContentType.JSON)
                .when()
                        .post("/produtos")
                .then().log().all()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("message", is("Cadastro realizado com sucesso"))
                        .extract().path("_id");
        return produtoID;
    }

    public void ExcluirProduto(String produtoID, String usuarioToken){
        given()
                .pathParam("_id", produtoID)
                .header("authorization", usuarioToken)

                .when()
                .delete("/produtos/{_id}")

                .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    public String PostCarrinho(String usuarioToken, String produtoTokenID){
        String carrinhoID =
            given()
                    .header("authorization", usuarioToken)
                    .body("{\n" +
                            "  \"produtos\": [\n" +
                            "    {\n" +
                            "      \"idProduto\": \""+produtoTokenID+"\",\n" +
                            "      \"quantidade\": 100\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")
                    .contentType(ContentType.JSON)
            .when()
                    .post("/carrinhos")
            .then().log().all()
                    .statusCode(HttpStatus.SC_CREATED)
                    .body("message", is("Cadastro realizado com sucesso"))
                    .extract().path("_id");
        return carrinhoID;
    }

    public void ExcluirCarrinho(String usuarioToken){
        given()
                .header("authorization", usuarioToken)
        .when()
                .delete("/carrinhos/cancelar-compra")
        .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

}