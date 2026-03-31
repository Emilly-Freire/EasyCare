package br.edu.ifpr.pgua.eic.tads;

import br.edu.ifpr.pgua.eic.tads.controllers.CadastroAlunoController;
import br.edu.ifpr.pgua.eic.tads.controllers.CadastroAtividadeController; 
import br.edu.ifpr.pgua.eic.tads.controllers.CadastroDisciplinaController;
import br.edu.ifpr.pgua.eic.tads.controllers.CadastroTopicoController;
import br.edu.ifpr.pgua.eic.tads.controllers.IndexController;
import br.edu.ifpr.pgua.eic.tads.controllers.ListarDisciplinaController;
import br.edu.ifpr.pgua.eic.tads.controllers.LoginController;
import br.edu.ifpr.pgua.eic.tads.controllers.LogoutController;
import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes;
import br.edu.ifpr.pgua.eic.tads.models.daos.AlunoDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.AtividadeDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.DisciplinaDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.InformacaoDisciplinaDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.JDBCAlunoDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.JDBCAtividadeDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.JDBCDisciplinaDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.JDBCInformacaoDisciplinaDAO; 
import br.edu.ifpr.pgua.eic.tads.models.daos.JDBCTopicoDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.TopicoDAO;
import br.edu.ifpr.pgua.eic.tads.models.repository.AlunoRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.AtividadeRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.DisciplinaRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.TopicoRepository;
import br.edu.ifpr.pgua.eic.tads.utils.JavalinUtils;
import br.service.AuthHandler; 

public class App {
    public static void main( String[] args ) {
    
        var app = JavalinUtils.makeApp(7073);

        // --- Instanciando Repositórios e DAOs ---
        FabricaConexoes fabrica = FabricaConexoes.getInstance();
        
        // Aluno
        AlunoDAO alunoDAO = new JDBCAlunoDAO(fabrica);
        AlunoRepository repositorioAluno = new AlunoRepository(alunoDAO);

        // Disciplina
        DisciplinaDAO disciplinaDAO = new JDBCDisciplinaDAO(fabrica);
        InformacaoDisciplinaDAO informacaoDao = new JDBCInformacaoDisciplinaDAO(fabrica); 
        DisciplinaRepository repositorioDisciplina = new DisciplinaRepository(disciplinaDAO, informacaoDao);

        // Topico
        TopicoDAO topicoDAO = new JDBCTopicoDAO(fabrica);
        TopicoRepository repositorioTopico = new TopicoRepository(topicoDAO);

        // Atividade
        AtividadeDAO atividadeDAO = new JDBCAtividadeDAO(fabrica);
        AtividadeRepository repositorioAtividade = new AtividadeRepository(atividadeDAO);

        // --- Instanciando Controllers ---
        IndexController indexController = new IndexController();

        CadastroAlunoController cadastroAlunoController = new CadastroAlunoController(repositorioAluno);

        LoginController loginController = new LoginController(repositorioAluno);
        LogoutController logoutController = new LogoutController();

        CadastroDisciplinaController cadastroDisciplinaController = new CadastroDisciplinaController(repositorioDisciplina);
        ListarDisciplinaController listarDisciplinaController = new ListarDisciplinaController(repositorioDisciplina, repositorioTopico); 
        
        CadastroTopicoController topicoController = new CadastroTopicoController(repositorioTopico, repositorioDisciplina);

        CadastroAtividadeController atividadeController = new CadastroAtividadeController(repositorioAtividade, repositorioTopico, repositorioDisciplina);
        
        
        // --- Definição das Rotas ---
        
        app.get("/", indexController.get);

        // Rotas de Login/Cadastro/Logout
        app.get("/login", loginController.get);
        app.post("/login", loginController.login);
        app.get("/cadastro_aluno", cadastroAlunoController.get);
        app.post("/cadastro_aluno", cadastroAlunoController.post);
        app.get("/logout", logoutController.logout);

        // Autenticação (Protege rotas)
        app.before("/disciplinas", AuthHandler.eAutenticado()); 
        app.before("/disciplinas/*", AuthHandler.eAutenticado()); 
        app.before("/topicos/*", AuthHandler.eAutenticado()); 
        app.before("/atividades/*", AuthHandler.eAutenticado()); 

        // Rotas de Disciplina (Lista e Detalhe/Checklist)
        
        app.get("/disciplinas", listarDisciplinaController.getListar); 
        app.get("/disciplinas/{id}", listarDisciplinaController.getDetalhe); 
        app.get("/disciplinas/{id}/editar", cadastroDisciplinaController.getEditar); 
      
        app.post("/disciplinas/cadastro", cadastroDisciplinaController.postCadastrar); 
        app.post("/disciplinas/{id}/editar", cadastroDisciplinaController.postEditar); 
        app.post("/disciplinas/{id}/excluir", cadastroDisciplinaController.postExcluir); 

        // Rotas de Tópicos (CRUD)
        
        app.get("/topicos/{id}/editar", topicoController.getEditar);

        app.post("/topicos/cadastro", topicoController.post);
        app.post("/topicos/{id}/editar", topicoController.postEditar);
        app.post("/topicos/{id}/excluir", topicoController.postExclusao);


        // Rotas de Atividades (Criação, Status e Exclusão)
        
        app.get("/atividades/cadastro", atividadeController.getCadastrar);
        
        app.post("/atividades/cadastro", atividadeController.postCadastar);
        app.post("/atividades/{id}/concluir", atividadeController.postAlternarStatus);
        app.post("/atividades/{id}/excluir", atividadeController.postExcluir);
        
    }
}