package br.com.daniel.aikoandroidestagio.ui.main.posicao

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.daniel.aikoandroidestagio.R
import br.com.daniel.aikoandroidestagio.databinding.FragmentPosicaoBinding
import br.com.daniel.aikoandroidestagio.model.LocalizacaoVeiculos
import br.com.daniel.aikoandroidestagio.services.ApiService
import br.com.daniel.aikoandroidestagio.ui.ListaTodosVeiculos
import br.com.daniel.aikoandroidestagio.util.Constants
import kotlinx.coroutines.launch

class PosicaoFragment : Fragment() {

    private var _binding: FragmentPosicaoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPosicaoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonPosicao.setOnClickListener {
            pegaRespostaApi()
        }

        return root
    }

    private fun pegaRespostaApi() {
        lifecycleScope.launch {
            val resposta = ApiService.getPosicoes()

            var localizacaoVeiculos: LocalizacaoVeiculos? = null
            if (resposta.isSuccessful) {
                localizacaoVeiculos = resposta.body()
            }

            localizacaoVeiculos?.let {
                abreActivityLista(it)
            } ?: listaNull()
        }
    }

    private fun abreActivityLista(it: LocalizacaoVeiculos) {
        val intent = Intent(context, ListaTodosVeiculos::class.java).apply {
            putExtra(Constants.veic, it)
        }
        startActivity(intent)
    }

    private fun listaNull() {
        Toast.makeText(context,getString(R.string.algo_errado_veiculos), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}