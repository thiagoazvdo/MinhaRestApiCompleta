package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, " + "pois está em uso";
	private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha " + "com código %d";
	@Autowired
	private CozinhaRepository cozinhas;

	public Cozinha salvar(Cozinha cozinha) {
		return cozinhas.save(cozinha);
	}

	public void excluir(Long cozinhaId) throws Exception {
		try {
			cozinhas.deleteById(cozinhaId);
			
		} catch (EmptyResultDataAccessException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Não existe um cadastro de cozinha " + "com código %d", cozinhaId)); // dessa forma nao precisamos das classes de excpetion 
			// mas nao eh boa pratica pois temos status dentro da classe de servico que em teoria deve conter apenas regras de negocio 
			
			throw new EntidadeNaoEncontradaException(
					String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId));
		
		} catch (DataIntegrityViolationException e) {
//			throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Cozinha de código %d não pode ser removida, " + "pois está em uso", cozinhaId)); // dessa forma nao precisamos das classes de exception
			// mas nao eh boa pratica pois temos status dentro da classe de servico que em teoria deve conter apenas regras de negocio 
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}
	
	public Cozinha buscarOuFalhar(Long cozinhaId) throws EntidadeNaoEncontradaException {
		return cozinhas.findById(cozinhaId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId)));
	}
	
}
