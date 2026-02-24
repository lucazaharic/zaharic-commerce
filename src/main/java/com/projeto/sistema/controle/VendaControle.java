package com.projeto.sistema.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistema.modelos.ItensVenda;
import com.projeto.sistema.modelos.Produto;
import com.projeto.sistema.modelos.Venda;
import com.projeto.sistema.repositorios.ClienteRepositorio;
import com.projeto.sistema.repositorios.FuncionarioRepositorio;
import com.projeto.sistema.repositorios.ItensVendaRepositorio;
import com.projeto.sistema.repositorios.ProdutoRepositorio;
import com.projeto.sistema.repositorios.VendaRepositorio;

@Controller
public class VendaControle {

	@Autowired
	private VendaRepositorio vendaRepositorio;
	@Autowired
	private ItensVendaRepositorio itensVendaRepositorio;
	@Autowired
	private ProdutoRepositorio produtoRepositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;

	private List<ItensVenda> listaItensVenda = new ArrayList<>();
	private Venda venda = new Venda();

	@GetMapping("/cadastroVenda")
	public ModelAndView cadastrar(Venda venda, ItensVenda itensVenda) {
		ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro");
		mv.addObject("venda", this.venda);
		mv.addObject("itensVenda", itensVenda);
		mv.addObject("listaItensVenda", this.listaItensVenda);
		mv.addObject("listaClientes", clienteRepositorio.findAll());
		mv.addObject("listaFuncionarios", funcionarioRepositorio.findAll());
		mv.addObject("listaProdutos", produtoRepositorio.findAll());
		return mv;
	}

	@PostMapping("/salvarVenda")
	@Transactional
	public ModelAndView salvar(String acao, Venda venda, ItensVenda itensVenda) {

		if (acao.equals("itens")) {
			// Busca o produto real no banco para evitar objetos transientes e garantir o preço
			Optional<Produto> prodOp = produtoRepositorio.findById(itensVenda.getProduto().getId());
			if (prodOp.isPresent()) {
				Produto prod = prodOp.get();
				itensVenda.setProduto(prod);
				itensVenda.setValorUnitario(prod.getPrecoVenda());
				itensVenda.setSubtotal(itensVenda.getQuantidade() * itensVenda.getValorUnitario());

				this.listaItensVenda.add(itensVenda);
				
				// Acumula o valor total na venda da memória
				this.venda.setValorTotal(this.venda.getValorTotal() + itensVenda.getSubtotal());
			}
			return cadastrar(this.venda, new ItensVenda());

		} else if (acao.equals("salvar")) {
			// Vincula os dados fixos (Cliente/Funcionário) que vieram do formulário
			this.venda.setCliente(venda.getCliente());
			this.venda.setFuncionario(venda.getFuncionario());
			
			// 1. Salva a Venda (Pai) primeiro para gerar o ID
			Venda vendaSalva = vendaRepositorio.saveAndFlush(this.venda);

			for (ItensVenda it : listaItensVenda) {
				// 2. Vincula o item à venda que acabou de ganhar ID
				it.setVenda(vendaSalva);
				itensVendaRepositorio.saveAndFlush(it);

				// 3. Baixa de stock: Busca o produto atualizado para evitar erro de concorrência
				Produto p = produtoRepositorio.findById(it.getProduto().getId()).get();
				p.setEstoque(p.getEstoque() - it.getQuantidade());
				produtoRepositorio.saveAndFlush(p);
			}

			// 4. Limpa o estado da memória para a próxima venda
			this.listaItensVenda = new ArrayList<>();
			this.venda = new Venda();
			return new ModelAndView("redirect:/listarVenda");
		}
		
		return new ModelAndView("redirect:/cadastroVenda");
	}
}