package com.example.mymessenger.ui.recruit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mymessenger.databinding.FragmentRecruitBinding

class RecruitFragment : Fragment() {

    private var _binding: FragmentRecruitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recruitViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                RecruitViewModel::class.java
            )

        _binding = FragmentRecruitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRecruit
        recruitViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}