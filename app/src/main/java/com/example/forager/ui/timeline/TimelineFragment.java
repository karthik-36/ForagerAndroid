package com.example.forager.ui.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.databinding.FragmentTimelineBinding;

public class TimelineFragment extends Fragment {

private FragmentTimelineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        com.example.forager.ui.settings.SettingsViewModel SettingsViewModel =
                new ViewModelProvider(this).get(com.example.forager.ui.settings.SettingsViewModel.class);

    binding = FragmentTimelineBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textTimeline;
        SettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}