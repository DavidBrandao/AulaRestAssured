package aulaRA;

import com.github.javafaker.Faker;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;

public class AppTest {

    //Gerando dados com o Faker
    Faker faker = new Faker();
    String nome = faker.name().firstName();
    String prod = faker.beer().name();

    public void printarDadosUtilizados(){
        System.out.println( "Nome cadastrado:"+ nome);
        System.out.println( "Produto cadastrado:"+ prod);
    };

    @BeforeClass
    public static void preCondition() {
        baseURI = "http://localhost";
        port = 3000;
    }

    @Test
    public void GetUsuarios() {
        when()
                .get("/usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCadastrarProduto(){
        //Printando dados randomicos criados pelo faker
        printarDadosUtilizados();

        //Instanciando a classe com as chamadas da API
        ServiceHelper services = new ServiceHelper();

        //Criando um novo usuário
        String usuarioID = services.PostUsuarios(nome);

        //Fazendo login com o usuário criado
        String usuarioToken = services.GetUsuarioToken(nome);

        //Criar novo produto
        String produtoID = services.PostProduto(prod, usuarioToken);

        //Excluir o produto criado
        services.ExcluirProduto(produtoID, usuarioToken);

        //Excluir o usuário
        services.ExcluirUsuario(usuarioID);
    }

    @Test
    public void testExercicio04(){
        //Printar dados a serem utilizados
        printarDadosUtilizados();

        //Instanciar classe de suporte com as chamadas da API
        ServiceHelper services = new ServiceHelper();

        //Criando um novo usuário
        String usuarioID = services.PostUsuarios(nome);

        //Fazendo login com o usuário criado
        String usuarioToken = services.GetUsuarioToken(nome);

        //Criar novo produto
        String produtoID = services.PostProduto(prod, usuarioToken);

        //Criar novo carrinho
        String carrinhoID = services.PostCarrinho(usuarioToken,produtoID);

        //Tentar deletar um usuário (Validar status code 400)
        services.ExcluirUsuarioCarrinhoAssociado(usuarioID);

        //Excluir carrinho para seguir com exclusão do usuário(parametro = token do usuário)
        services.ExcluirCarrinho(usuarioToken);

        //Excluir usuário (neste ponto sem associação ao carrinho
        services.ExcluirUsuario(usuarioID);
    }
}
