package com.projeto.sistema.controle;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.projeto.sistema.modelos.Produto;
import com.projeto.sistema.repositorios.ProdutoRepositorio;

@Controller
public class ProdutoControle {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;
    
    @GetMapping("/cadastroProduto")
    public ModelAndView cadastrar(Produto produto) {
        // Define o caminho do HTML e adiciona o objeto produto
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        return mv;
    }
    
    @GetMapping("/editarProduto/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        return cadastrar(produto.get());
    }
    
    @GetMapping("/removerProduto/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        if (produto.isPresent()) {
            produtoRepositorio.delete(produto.get());
        }
        return listar();
    }
    
    @GetMapping("/listarProduto")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProdutos", produtoRepositorio.findAll());
        return mv;
    }
    
    @PostMapping("/salvarProduto")
    public ModelAndView salvar(Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(produto);
        }

        // LÓGICA DE NEGÓCIO: Cálculo automático de lucro antes de salvar
        if (produto.getPrecoVenda() != null && produto.getPrecoCusto() != null) {
            produto.setLucro(produto.getPrecoVenda() - produto.getPrecoCusto());
            
            if (produto.getPrecoCusto() > 0) {
                // Cálculo da margem de lucro em porcentagem
                produto.setMargemLucro((produto.getLucro() / produto.getPrecoCusto()) * 100);
            }
        }

        // Salva no PostgreSQL
        produtoRepositorio.saveAndFlush(produto);
        
        // Redireciona para a lista para que a animação de "landing page" ocorra ao entrar na nova tela
        return new ModelAndView("redirect:/listarProduto");
    }
}