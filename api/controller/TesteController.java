package com.algaworks.algafood.api.controller;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	//classe criada apenas para testar separadamente uma consulta por nome de cozinha e usando o like no JPQL para buscar por letra, sigla ou parte do nome
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping("/cozinhas/buscar-por-nome")
	public List<Cozinha> buscarPorNome(@RequestHeader("nome") String nome){
//		return cozinhaRepository.nome(nome);
		return cozinhaRepository.findTodasByNomeContaining(nome);
	}
	
	@GetMapping("/cozinhas/unica/buscar-por-nome")
	public Optional<Cozinha> unicaPorNome(@RequestHeader("nome") String nome){
		return cozinhaRepository.findByNome(nome);
	}
	
	@GetMapping("/restaurante/por-taxa-frete")
	public List<Restaurante> buscarPorTaxaFrete(
			BigDecimal taxaInicial, BigDecimal taxaFinal, String nome){
		return restauranteRepository.find(nome, taxaInicial, taxaFinal);
	}
	
	@GetMapping("/restaurante/por-nome-e-id")
	public List<Restaurante> buscarPorNomeEId(@RequestHeader("nome") String nome, @RequestHeader("Id") Long id){
	//		return restauranteRepository.findByNomeContainingAndCozinhaId(nome, id);
		return restauranteRepository.consultarPorNome(nome, id);
	}
	
	//buscando os 2 primeiros pelo nome (contendo a letra, sigla ou palavra passada no header)
	@GetMapping("/restaurante/2primeiros-por-nome")
	public List<Restaurante> primeirosPorNome(@RequestHeader("nome") String nome){
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
	//retorna se existe a cozinha com o nome passado por parametro
	@GetMapping("/cozinha/exists")
	public boolean cozinhaExists(@RequestHeader("nome") String nome){
		return cozinhaRepository.existsByNome(nome);
	}
	
	//contando quantos restaurantes temos com o id da cozinha passada por parametro
	@GetMapping("/restaurante/count-por-cozinha")
	public int restauranteCountPorCozinha(@RequestHeader("id") Long id){
		return restauranteRepository.countByCozinhaId(id);
	}
	
	@GetMapping("/restaurante/com-frete-gratis")
	public List<Restaurante> restauranteComFreteGratis(@RequestHeader String nome){
				//para o metodo findAll() suportar as classes JpaSpecifications precisamos precisamos que o RestauranteRepository tambem herde a classe JpaSpecificationExecutor
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}
	
}
