package com.example.forager.ui.confirm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.databinding.FragmentConfirmBinding;
import com.example.forager.ui.confirm.ConfirmViewModel;

public class ConfirmFragment extends Fragment {

private FragmentConfirmBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ConfirmViewModel messagesViewModel =
                new ViewModelProvider(this).get(ConfirmViewModel.class);

        binding = FragmentConfirmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }



@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}