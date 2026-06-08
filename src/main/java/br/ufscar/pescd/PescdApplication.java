package br.ufscar.pescd;

import br.ufscar.pescd.model.FraseConfirmacao;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Inscricao;

import br.ufscar.pescd.repositories.FraseRepository;
import br.ufscar.pescd.repositories.InscricaoRepository;
import br.ufscar.pescd.services.UsuarioService;
import br.ufscar.pescd.services.OfertaService;
import br.ufscar.pescd.services.InscricaoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.time.LocalDate;

@SpringBootApplication
public class PescdApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(
						PescdApplication.class,
						args
				);

		UsuarioService usuarioService =
				context.getBean(UsuarioService.class);

		Usuario admin = new Usuario(
				null,
				"Administrador",
				List.of("ROLE_ADMINISTRADOR"),
				"admin",
				"123"
		);

		usuarioService.salvar(admin);

		Usuario aluno = new Usuario(
				null,
				"Murilo Terra",
				List.of("ROLE_ALUNO"),
				"cheesegrater",
				"123"
		);

		usuarioService.salvar(aluno);

		Usuario aluno2 = new Usuario(
				null,
				"Luis Gustavo",
				List.of("ROLE_ALUNO"),
				"lui@gmail.com",
				"123"
		);

		usuarioService.salvar(aluno2);

		Usuario secretario = new Usuario(
				null,
				"Secretario",
				List.of("ROLE_SECRETARIO"),
				"secretario",
				"123"
		);

		usuarioService.salvar(secretario);

		Usuario supervisor = new Usuario(
				null,
				"Supervisor",
				List.of("ROLE_SUPERVISOR"),
				"supervisor",
				"123"
		);

		usuarioService.salvar(supervisor);

		Usuario responsavel = new Usuario(
				null,
				"Responsavel",
				List.of("ROLE_RESPONSAVEL"),
				"responsavel",
				"123"
		);

		usuarioService.salvar(responsavel);

		OfertaService ofertaService =
				context.getBean(OfertaService.class);

		Usuario ProfMarcus = new Usuario(
				null,
				"Marcus Vinicius ",
				List.of("ROLE_SUPERVISOR"),
				"warcus",
				"123"
				);

		usuarioService.salvar(ProfMarcus);
		Oferta oferta1 = new Oferta(
				null,
				LocalDate.of(2026, 3, 1),
				LocalDate.of(2026, 7, 15),
				"Web1",
				"2026/1",
				secretario,
				ProfMarcus,
				0
		);

		ofertaService.salvar(oferta1);

		Usuario ProfCatarina = new Usuario(
				null,
				"Catarina",
				List.of("ROLE_SUPERVISOR"),
				"cati",
				"123"
		);

		usuarioService.salvar(ProfCatarina);

		Oferta oferta2 = new Oferta(
				null,
				LocalDate.of(2026, 8, 1),
				LocalDate.of(2026, 12, 10),
				"BD",
				"2026/2",
				secretario,
				ProfCatarina,
				0
		);

		ofertaService.salvar(oferta2);

		Oferta oferta3 = new Oferta(
				null,
				LocalDate.of(2025, 3, 1),
				LocalDate.of(2025, 7, 15),
				"ES1",
				"2025/1",
				secretario,
				ProfCatarina,
				0
		);

		ofertaService.salvar(oferta3);

		Oferta oferta4 = new Oferta(
				null,
				LocalDate.of(2025, 8, 1),
				LocalDate.of(2025, 12, 15),
				"IA",
				"2025/2",
				secretario,
				ProfMarcus,
				0
		);

		ofertaService.salvar(oferta4);

		Oferta oferta5 = new Oferta(
				null,
				LocalDate.of(2024, 3, 1),
				LocalDate.of(2024, 7, 15),
				"Web2",
				"2024/1",
				secretario,
				ProfCatarina,
				0
		);

		ofertaService.salvar(oferta5);

		InscricaoService inscricaoService = context.getBean(InscricaoService.class);

		Inscricao inscricaoPendente = new Inscricao(aluno, oferta1);

		inscricaoService.salvar(inscricaoPendente);


		FraseConfirmacao frase = new FraseConfirmacao("Deseja mesmo encerrar essa oferta?");
		FraseRepository fraseRepository = context.getBean(FraseRepository.class);

		fraseRepository.save(frase);
	}
}