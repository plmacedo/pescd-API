package br.ufscar.pescd.services;

import br.ufscar.pescd.dto.*;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.model.StatusPlano;
import br.ufscar.pescd.repositories.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private UsuarioService usuarioService;



    public List<Oferta> inscricoesDoAluno(Long alunoId){
        List<Inscricao> inscricoes = inscricaoRepository.findByAlunoId(alunoId);

        List<Oferta> ofertas;
        ofertas = inscricoes.stream().map(Inscricao::getOferta).toList();

        return ofertas;
    }

    public void salvar(Inscricao inscricao) {
        inscricaoRepository.save(inscricao);
    }

    public Inscricao buscarPorID(Long inscricaoID) {
        Optional<Inscricao> obj = inscricaoRepository.findById(inscricaoID);
        return obj.orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));
    }

    public List<Inscricao> buscarInscricoesPorAluno(Long alunoId) {
        return inscricaoRepository.findByAlunoId(alunoId);
    }

    public void enviarPlanoTrabalho(PlanoTrabalhoFormDTO planoDTO) {
        // 1. Busca a inscrição existente no banco pelo ID contido no DTO
        Inscricao inscricao = buscarPorID(planoDTO.getInscricaoID());

        // 2. Busca o Professor Supervisor selecionado a partir do ID do DTO [cite: 62]
        Usuario supervisor = usuarioService.buscarPorId(planoDTO.getProfessorSupervisorId());

        // 3. Transfere os dados textuais do DTO para a Entidade
        inscricao.setCodigoDisciplina(planoDTO.getCodigoDisciplina());
        inscricao.setNomeDisciplina(planoDTO.getNomeDisciplina());
        inscricao.setCursoDisciplina(planoDTO.getCursoDisciplina());
        inscricao.setSupervisor(supervisor);

        // 4. Converte o arquivo MultipartFile em array de bytes (byte[]) [cite: 61, 63]
        try {
            if (planoDTO.getArquivoPlano() != null && !planoDTO.getArquivoPlano().isEmpty()) {
                inscricao.setArquivoPlano(planoDTO.getArquivoPlano().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo PDF do plano de trabalho.", e);
        }

        // 5. Altera o status da inscrição para PLANO ENVIADO [cite: 64]
        inscricao.setStatusPlano(StatusPlano.ENVIADO);

        // 6. Salva as alterações de forma definitiva no banco de dados
        inscricaoRepository.save(inscricao);
    }
    // inscreve aluno ja existente no BD
    public void inscreverAluno(Long ofertaId, Long alunoId) {
        Oferta oferta = ofertaService.buscarPorId(ofertaId);
        Usuario aluno = usuarioService.buscarPorId(alunoId);

        // impede de escrever um mesmo aluno 2 vezes na mesma oferta
        if (!inscricaoRepository.existsByAlunoAndOferta(aluno, oferta)) {
            Inscricao novaInscricao = new Inscricao(aluno, oferta);

            oferta.incrementaNroEstudantes();
            inscricaoRepository.save(novaInscricao);
        }
    }

    public void enviarDocumentacao(Long inscricaoID, DocumentacaoFormDTO dto) throws IOException {
        Inscricao inscricao = buscarPorID(inscricaoID);

        // copiando os dados do DTO para a Entidade
        inscricao.setInstituicaoMinistrou(dto.getInstituicao());
        inscricao.setNomeDisciplinaMinistrada(dto.getNomeDisciplina());
        inscricao.setCursoDisciplinaMinistrada(dto.getCursoDisciplina());
        inscricao.setCargaHorariaDisciplina(dto.getCargaHoraria());

        // extraindo os bytes do MultipartFile
        if (dto.getArquivo() != null && !dto.getArquivo().isEmpty()) {
            inscricao.setArquivoDocumentacao(dto.getArquivo().getBytes());
        }

        // RN-4
        inscricao.setStatusPlano(StatusPlano.DOCUMENTACAO_ENVIADA);

        inscricaoRepository.save(inscricao);
    }

    public void aprovarPlano(br.ufscar.pescd.dto.AprovarPlanoFormDTO dto) {
        Inscricao inscricao = buscarPorID(dto.getInscricaoID());

        inscricao.setParecerPlano(dto.getParecer());

        inscricao.setStatusPlano(StatusPlano.APROVADO);

        inscricao.setDataAprovacaoPlano(java.time.LocalDateTime.now());

        inscricaoRepository.save(inscricao);
    }

    public void enviarRelatorioFinal(Long inscricaoID, RelatorioFinalFormDTO dto) throws IOException {
        Inscricao inscricao = buscarPorID(inscricaoID);

        if (dto.getArquivo() != null && !dto.getArquivo().isEmpty()) {
            inscricao.setArquivoRelatorioFinal(dto.getArquivo().getBytes());
        }

        // Atualiza status para indicar que o relatório foi enviado
        inscricao.setStatusPlano(StatusPlano.RELATORIO_ENVIADO);
        inscricao.setFrequencia(dto.getFrequencia());

        inscricaoRepository.save(inscricao);
    }

    public List<Inscricao> buscarPorSupervisor(Usuario supervisor) {
        return inscricaoRepository.findByOfertaProf(supervisor);
    }

    public void aprovarRelatorio(br.ufscar.pescd.dto.AprovarRelatorioFormDTO dto) {
        Inscricao inscricao = buscarPorID(dto.getInscricaoID());

        inscricao.setParecerRelatorio(dto.getParecer());
        inscricao.setFrequencia(dto.getFrequencia());
        inscricao.setSugestaoNota(dto.getNota());

        inscricao.setStatusPlano(StatusPlano.RELATORIO_APROVADO_SUPERVISOR);

        inscricao.setDataAprovacaoRelatorio(java.time.LocalDateTime.now());

        inscricaoRepository.save(inscricao);
    }

    public void concluirRelatorioResponsavel(ConcluirRelatorioFormDTO dto) {
        Inscricao inscricao = buscarPorID(dto.getInscricaoID());

        inscricao.setParecerResponsavel(dto.getParecer());
        inscricao.setFrequencia(dto.getFrequencia());
        inscricao.setNotaFinal(dto.getNota());

        inscricao.setStatusPlano(StatusPlano.CONCLUIDO_PELO_RESPONSAVEL);

        inscricaoRepository.save(inscricao);
    }

    // adiciona via csv
    public void processarCsvInscricoes(Long ofertaId, MultipartFile file) throws Exception {
        Oferta oferta = ofertaService.buscarPorId(ofertaId);

        // abre o arquivo CSV para leitura linha por linha
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                // Pula o cabeçalho "RA,NOME_COMPLETO,EMAIL"
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Divide a linha pela vírgula
                String[] colunas = linha.split(",");
                if (colunas.length < 3) continue; // Ignora linhas em branco ou mal formatadas

                String ra = colunas[0].trim();
                String nome = colunas[1].trim();
                String email = colunas[2].trim();

                // ve se esse aluno ja existe
                Optional<Usuario> usuarioExistente = usuarioService.buscarOptionalPorUsername(email);
                Usuario aluno;

                if (usuarioExistente.isPresent()) {
                    // aluno ja existe
                    aluno = usuarioExistente.get();
                } else {
                    // aluno nao existe, sera cadastrado (Senha = RA, Username = Email)
                    aluno = new Usuario();
                    aluno.setUsername(email);
                    aluno.setSenha(ra);
                    aluno.setNome(nome);
                    aluno.setCargos(List.of("ROLE_ALUNO"));


                    aluno = usuarioService.salvar(aluno);
                }

                // ve se o aluno ja esta inscrito nessa oferta
                if (!inscricaoRepository.existsByAlunoAndOferta(aluno, oferta)) {
                    Inscricao novaInscricao = new Inscricao(aluno, oferta);

                    oferta.incrementaNroEstudantes();
                    inscricaoRepository.save(novaInscricao);
                }
            }
        }
    }

    public void analisarDocumentacao(Long idInscricao, AnalisarDocumentacaoFormDTO dto) {
        Inscricao inscricao = inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new IllegalArgumentException("Inscrição não encontrada."));

        // Verifica se o status do aluno é "documentação enviada"
        if (inscricao.getStatusPlano() != StatusPlano.DOCUMENTACAO_ENVIADA) {
            throw new IllegalStateException("Status inválido para analisar documentação.");
        }

        // Atualiza os dados com base no formulário
        inscricao.setParecerResponsavel(dto.getParecer()); // Verifique se o nome exato do campo na sua entidade Inscricao é esse
        inscricao.setFrequenciaFinal(dto.getFrequencia());
        inscricao.setNotaFinal(dto.getNota());

        // Atualiza o status e registra o timestamp da conclusão
        inscricao.setStatusPlano(StatusPlano.CONCLUIDO_PELO_RESPONSAVEL);
        inscricao.setDataConclusaoResponsavel(LocalDateTime.now());

        inscricaoRepository.save(inscricao);
    }
}