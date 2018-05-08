package com.vetweb.controller;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

// @author Maria Jéssica
import com.vetweb.dao.AnimalDAO;
import com.vetweb.dao.AtendimentoDAO;
import com.vetweb.dao.ProntuarioDAO;
import com.vetweb.dao.VacinaDAO;
import com.vetweb.model.Animal;
import com.vetweb.model.Atendimento;
import com.vetweb.model.Patologia;
import com.vetweb.model.Prontuario;
import com.vetweb.model.ProntuarioPatologia;
import com.vetweb.model.ProntuarioVacina;
import com.vetweb.model.TipoDeAtendimento;
import com.vetweb.model.Vacina;

@Controller//Indica que a classe trata requisições
@Transactional
@RequestMapping("/prontuario")//Caminho base da controladora
public class ProntuarioController {
	
    @Autowired
    private AtendimentoDAO atendimentoDAO;
    
    @Autowired
    private ProntuarioDAO prontuarioDAO;
    
    @Autowired
    private AnimalDAO animalDAO;
    
    @Autowired
    private VacinaDAO vacinaDAO;
    
    private static final Logger LOGGER = Logger.getLogger(ProntuarioController.class);
    
    @RequestMapping(value = "/cadastroTipoAtendimento", method = RequestMethod.GET)//Caminho de uma requisição
    public ModelAndView formTipoAtendimento(TipoDeAtendimento tipoDeAtendimento) {
        ModelAndView modelAndView = new ModelAndView("prontuario/cadastroTipoAtendimento");
        Set<Duration> duracoesValidas = Stream.of(Duration.ofMinutes(30), Duration.ofHours(1), Duration.ofHours(3), Duration.ofHours(5))
                .collect(Collectors.toSet());//Cria sequência de itens em forma de Stream e converte para coleção
        Set<Period> frequenciasValidas = Stream.of(Period.ofDays(15), Period.ofMonths(1), Period.ofYears(1), Period.ofWeeks(2))
                .collect(Collectors.toSet());
        modelAndView.addObject("duracoesValidas", duracoesValidas);
        modelAndView.addObject("frequenciasValidas", frequenciasValidas);
        return modelAndView;
    }
    
    @RequestMapping(value = "/cadastroVacina", method = RequestMethod.GET)//Caminho de uma requisição
    public ModelAndView formVacina(Vacina vacina) {
        ModelAndView modelAndView = new ModelAndView("prontuario/cadastroVacina");
        return modelAndView;
    }
    
    @RequestMapping(value = "/addTipoAtendimento", method = RequestMethod.POST)
    public ModelAndView cadTipoAtendimento(@ModelAttribute("tipoDeAtendimento") TipoDeAtendimento tipoDeAtendimento) {
        ModelAndView modelAndView = new ModelAndView("redirect:tiposDeAtendimento");
        atendimentoDAO.salvarTipoDeAtendimento(tipoDeAtendimento);
        return modelAndView;
    }
    
    @RequestMapping(value = "/addVacina", method = RequestMethod.POST)
    public ModelAndView cadVacina(@ModelAttribute("vacina") Vacina vacina) {
        ModelAndView modelAndView = new ModelAndView("redirect:vacinas");
        vacinaDAO.salvar(vacina);
        return modelAndView;
    }
    
    @RequestMapping(value = "/tiposDeAtendimento", method = RequestMethod.GET)
    public ModelAndView listarTiposDeAtendimento(){
        ModelAndView modelAndView = new ModelAndView("prontuario/tiposDeAtendimento");
        modelAndView.addObject("tiposDeAtendimento", atendimentoDAO.tiposDeAtendimento());
        return modelAndView;
    }
    
    @RequestMapping(value = "/vacinas", method = RequestMethod.GET)
    public ModelAndView listarVacinas(){
        ModelAndView modelAndView = new ModelAndView("prontuario/vacinas");
        modelAndView.addObject("vacinas", vacinaDAO.listar());
        return modelAndView;
    }
    
    @RequestMapping(value = "/atualizarTipoDeAtendimento/{tipoDeAtendimentoId}", method = RequestMethod.GET)
    public ModelAndView atualizarTipoDeAtendimento(@PathVariable("tipoDeAtendimentoId")Long tipoDeAtendimentoId) {
    	ModelAndView modelAndView = new ModelAndView("prontuario/cadastroTipoAtendimento");
    	Set<Duration> duracoesValidas = Stream.of(Duration.ofMinutes(30), Duration.ofHours(1), Duration.ofHours(3), Duration.ofHours(5))
    			.collect(Collectors.toSet());//Cria sequência de itens em forma de Stream e converte para coleção
    	Set<Period> frequenciasValidas = Stream.of(Period.ofDays(15), Period.ofMonths(1), Period.ofYears(1), Period.ofWeeks(2))
    			.collect(Collectors.toSet());
    	modelAndView.addObject("duracoesValidas", duracoesValidas);
    	modelAndView.addObject("frequenciasValidas", frequenciasValidas);        
    	modelAndView.addObject("tipoDeAtendimento", atendimentoDAO.tipoDeAtendimentoPorId(tipoDeAtendimentoId));
    	return modelAndView;
    }
    
    @RequestMapping(value = "/atualizarVacina/{vacinaId}", method = RequestMethod.GET)
    public ModelAndView atualizarVacina(@PathVariable("vacinaId")Long vacinaId) {
        ModelAndView modelAndView = new ModelAndView("prontuario/cadastroVacina");
        modelAndView.addObject("vacina", vacinaDAO.consultarPorId(vacinaId));
        return modelAndView;
    }
    
    @RequestMapping(value = "/removerTipoDeAtendimento/{tipoDeAtendimentoId}", method = RequestMethod.GET)
    public ModelAndView delTipoDeAtendimento(@PathVariable("tipoDeAtendimentoId")Long tipoDeAtendimentoId) {
    	ModelAndView modelAndView = new ModelAndView("redirect:/prontuario/tiposDeAtendimento");
    	atendimentoDAO.removerTipoDeAtendimento(atendimentoDAO.tipoDeAtendimentoPorId(tipoDeAtendimentoId));
    	return modelAndView;
    }
    
    @RequestMapping(value = "/removerVacina/{vacinaId}", method = RequestMethod.GET)
    public ModelAndView delVacina(@PathVariable("vacinaId")Long vacinaId) {
        ModelAndView modelAndView = new ModelAndView("redirect:/prontuario/vacinas");
        vacinaDAO.remover(vacinaDAO.consultarPorId(vacinaId));
        return modelAndView;
    }
    
    @RequestMapping(value = "/gerarProntuario", method = RequestMethod.POST)
    public ModelAndView criarProntuarioDoAnimal(@ModelAttribute("animal") final Animal animal) {
        LOGGER.info("criarProntuarioDoAnimal".toUpperCase());
        Animal animal1 = animalDAO.consultar(animal);
        LOGGER.info(("Sendo criado prontuário do animal " + animal1).toUpperCase());
        Prontuario prontuario = new Prontuario(animal1);
        prontuarioDAO.salvar(prontuario);
        ModelAndView modelAndView = new ModelAndView("redirect:/prontuario/prontuarioDoAnimal/" + animal1.getAnimalId());
        return modelAndView;
    }
    
    @RequestMapping(value = "/prontuarioDoAnimal/{animalId}", method = RequestMethod.GET)
    public ModelAndView prontuarioDoAnimal(@PathVariable("animalId") final Long animalId, @ModelAttribute("atendimento") Atendimento atendimento,
    		@ModelAttribute("prontuarioPatologia") Patologia patologia, @ModelAttribute("prontuarioVacina") Vacina vacina) {
        ModelAndView modelAndView = new ModelAndView("prontuario/prontuario");
        modelAndView.addObject("prontuario", prontuarioDAO.prontuarioPorAnimal(animalId));
		modelAndView.addObject("tiposDeAtendimento", prontuarioDAO.tiposDeAtendimento());
		modelAndView.addObject("vacinas", prontuarioDAO.vacinas().stream()//Para que no select da página do prontuário chegue o nome das vacinas
	    		.map(vac -> vac.getNome()).collect(Collectors.toList()));
		modelAndView.addObject("patologias", animalDAO.patologias().stream()
	    		.map(pat -> pat.getNome()).collect(Collectors.toList()));
        return modelAndView;
    }
    
    @RequestMapping(value = "/adicionarAtendimento", method = RequestMethod.POST)
    public ModelAndView adcAtendimento(@ModelAttribute("atendimento") Atendimento atendimento, @RequestParam("prontuarioId") final Long prontuarioId) {
        prontuarioDAO.salvarAtendimento(atendimento);
        prontuarioDAO.adicionarAtendimento(atendimento, prontuarioId);
        return new ModelAndView("redirect:prontuarioDoAnimal/" + prontuarioDAO.consultarPorId(prontuarioId).getAnimal().getAnimalId());
    }
    
    @RequestMapping(value = "/modeloPorTipoDeAtendimento/{tipoDeAtendimento}", method = RequestMethod.GET)
    public @ResponseBody String modeloDoTipoDeAtendimento(@PathVariable("tipoDeAtendimento") String nomeTipoAtendimento) {
        return prontuarioDAO.modeloDoTipoAtendimento(nomeTipoAtendimento);
    }
    
    @RequestMapping(value="/adicionarPatologia", method=RequestMethod.POST)
    public ModelAndView adcPatologia(@RequestParam("prontuarioId") final Long prontuarioId,
    		@RequestParam("patologia") final String patologiaStr, @RequestParam("inclusaoPatologia") final String inclusaoPatologia) {
    	LOGGER.info(("Inserindo patologia " + patologiaStr + " no prontuário " + prontuarioId).toUpperCase());
    	ProntuarioPatologia prontuarioPatologia = new ProntuarioPatologia();
    	Patologia pat = animalDAO.patologiaPorDescricao(patologiaStr);
		animalDAO.salvarPatologia(pat);
		prontuarioPatologia.setPatologia(pat);
		Prontuario prontuario = prontuarioDAO.consultarPorId(prontuarioId);
		prontuarioPatologia.setProntuario(prontuario);
		prontuarioPatologia.setInclusaoPatologia(LocalDate.parse(inclusaoPatologia, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    	prontuarioDAO.adicionarPatologia(prontuarioPatologia, prontuarioId);
    	return new ModelAndView("redirect:prontuarioDoAnimal/" + prontuario.getAnimal().getAnimalId());
    }
    
    @RequestMapping(value="/adicionarVacina", method=RequestMethod.POST)
    public ModelAndView adcVacina(@RequestParam("prontuarioId") final Long prontuarioId,
    		@RequestParam("vacina") final String vacinaStr, @RequestParam("inclusaoVacina") final String inclusaoVacina) {
    	LOGGER.info(("Inserindo vacina " + vacinaStr + " no prontuário " + prontuarioId).toUpperCase());
    	ProntuarioVacina prontuarioVacina = new ProntuarioVacina();
    	Vacina vacina = vacinaDAO.consultarPorNome(vacinaStr);
		vacinaDAO.salvar(vacina);
		prontuarioVacina.setVacina(vacina);
		Prontuario prontuario = prontuarioDAO.consultarPorId(prontuarioId);
		prontuarioVacina.setProntuario(prontuario);
		prontuarioVacina.setInclusaoVacina(LocalDate.parse(inclusaoVacina, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    	prontuarioDAO.adicionarVacina(prontuarioVacina, prontuarioId);
    	return new ModelAndView("redirect:prontuarioDoAnimal/" + prontuarioDAO.consultarPorId(prontuarioId).getAnimal().getAnimalId());
    }
    
    @RequestMapping(value = "/removerAtendimentoDoProntuario/{prontuarioId}/{atendimentoId}", method = RequestMethod.GET)
    public ModelAndView remAtendimentoProntuario(@PathVariable("prontuarioId")Long prontuarioId, @PathVariable("atendimentoId")Long atendimentoId,
    		@ModelAttribute("atendimento") Atendimento atendimento,
    		@ModelAttribute("prontuarioPatologia") Patologia patologia,
    		@ModelAttribute("prontuarioVacina") Vacina vacina) {
    	ModelAndView modelAndView = new ModelAndView("prontuario/prontuario");
    	modelAndView.addObject("prontuario", prontuarioDAO.consultarPorId(prontuarioId));
    	prontuarioDAO.removerAtendimentoDoProntuario(atendimentoDAO.consultarPorId(atendimentoId), prontuarioId);
    	adicionarListasAoProntuario(modelAndView);
    	return modelAndView;
    }
    
    @RequestMapping(value = "/removerVacinaDoProntuario/{prontuarioId}/{vacinaId}", method = RequestMethod.GET)
    public ModelAndView remVacinaProntuario(@PathVariable("prontuarioId")Long prontuarioId, @PathVariable("vacinaId")Long vacinaId,
    		@RequestParam("inclusaoOcorrenciaVacina") String inclusaoVacina,
    		@ModelAttribute("atendimento") Atendimento atendimento,
    		@ModelAttribute("prontuarioPatologia") Patologia patologia,
    		@ModelAttribute("prontuarioVacina") Vacina vacina) {
    	ModelAndView modelAndView = new ModelAndView("prontuario/prontuario");
    	modelAndView.addObject("prontuario", prontuarioDAO.consultarPorId(prontuarioId));
    	prontuarioDAO.removerVacinaDoProntuario(prontuarioDAO.buscarOcorrenciaVacinaProntuarioPorId(vacinaId),prontuarioId);
    	adicionarListasAoProntuario(modelAndView);
    	return modelAndView;
    }
    
    @RequestMapping(value = "/removerPatologiaDoProntuario/{prontuarioId}/{patologiaId}", method = RequestMethod.GET)
    public ModelAndView remPatologiaProntuario(@PathVariable("prontuarioId")Long prontuarioId, @PathVariable("patologiaId")Long patologiaId,
    		@ModelAttribute("atendimento") Atendimento atendimento,
    		@ModelAttribute("prontuarioPatologia") Patologia patologia,
    		@ModelAttribute("prontuarioVacina") Vacina vacina) {
    	ModelAndView modelAndView = new ModelAndView("prontuario/prontuario");
    	modelAndView.addObject("prontuario", prontuarioDAO.consultarPorId(prontuarioId));
    	prontuarioDAO.removerPatologiaDoProntuario(prontuarioDAO.buscarOcorrenciaPatologiaProntuarioPorId(patologiaId),prontuarioId);
    	adicionarListasAoProntuario(modelAndView);
		return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = "/editarAtendimento/{atendimentoId}", method = RequestMethod.GET)
    public Atendimento atendimentoParaEdicao(@PathVariable("atendimentoId") Long atendimentoId) {
    	Atendimento atendimento = atendimentoDAO.consultarPorId(atendimentoId);
    	return atendimento;
    }
    
    private void adicionarListasAoProntuario(ModelAndView viewProntuario) {
    	Map<String, List> listasProntuario = new HashMap<>();
    	listasProntuario.put("tiposDeAtendimento", prontuarioDAO.tiposDeAtendimento());
    	LOGGER.info("ADICIONANDO LISTA DE SERVIÇOS P/ INCLUSÃO DE ATENDIMENTOS NO PRONTUÁRIO.");
    	listasProntuario.put("vacinas", prontuarioDAO.vacinas().stream()//Para que no select da página do prontuário chegue o nome das vacinas
	    		.map(v -> v.getNome()).collect(Collectors.toList()));
    	LOGGER.info("ADICIONANDO LISTA DE VACINAS DISPONÍVEIS PARA USO NO PRONTUÁRIO. ");
    	listasProntuario.put("patologias", animalDAO.patologias().stream()
	    		.map(pat -> pat.getNome()).collect(Collectors.toList()));
    	LOGGER.info("ADICIONANDO POSSÍVEIS PATOLOGIAS QUE PODEM SER ANEXADAS NO HISTÓRICO DO ANIMAL NO PRONTUÁRIO. ");
    	viewProntuario.addAllObjects(listasProntuario);
    }
    
}
