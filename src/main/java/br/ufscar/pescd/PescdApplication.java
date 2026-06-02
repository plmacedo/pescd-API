package br.ufscar.pescd;

import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.model.Oferta;

import br.ufscar.pescd.services.UsuarioService;
import br.ufscar.pescd.services.OfertaService;

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
				"Aluno",
				List.of("ROLE_ALUNO"),
				"aluno",
				"123"
		);

		usuarioService.salvar(aluno);

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

		Oferta oferta1 = new Oferta(
				null,
				LocalDate.of(2026, 3, 1),
				LocalDate.of(2026, 7, 15),
				"Programação Web",
				"2026/1",
				"Prof. João",
				25
		);

		ofertaService.salvar(oferta1);

		Oferta oferta2 = new Oferta(
				null,
				LocalDate.of(2026, 8, 1),
				LocalDate.of(2026, 12, 10),
				"Banco de Dados",
				"2026/2",
				"Prof. Maria",
				30
		);

		ofertaService.salvar(oferta2);

		Oferta oferta3 = new Oferta(
				null,
				LocalDate.of(2025, 3, 1),
				LocalDate.of(2025, 7, 15),
				"Engenharia de Software",
				"2025/1",
				"Prof. Carlos",
				40
		);

		ofertaService.salvar(oferta3);

		Oferta oferta4 = new Oferta(
				null,
				LocalDate.of(2025, 8, 1),
				LocalDate.of(2025, 12, 15),
				"Inteligência Artificial",
				"2025/2",
				"Prof. Ana",
				20
		);

		ofertaService.salvar(oferta4);

		Oferta oferta5 = new Oferta(
				null,
				LocalDate.of(2024, 3, 1),
				LocalDate.of(2024, 7, 15),
				"Banco de Dados Avançado",
				"2024/1",
				"Prof. Marcos",
				18
		);

		ofertaService.salvar(oferta5);

	}
}