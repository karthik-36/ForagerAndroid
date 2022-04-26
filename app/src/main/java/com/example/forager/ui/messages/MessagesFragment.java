package com.example.forager.ui.messages;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {

private FragmentMessagesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        MessagesViewModel messagesViewModel =
                new ViewModelProvider(this).get(MessagesViewModel.class);

    binding = FragmentMessagesBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textMessage;
        messagesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);



//
//        String url = "https://api.whatsapp.com/";
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);

        String url = "https://api.whatsapp.com/send?phone="+"13129004175";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

       // openWhatsApp(root);

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void openWhatsApp(View v)
    {
        PackageManager pm = getContext().getPackageManager();
        try
        {
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            startActivity(intent);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Toast.makeText(getContext(), "Please install WhatsApp App", Toast.LENGTH_SHORT).show();
        }
        catch(NullPointerException exception){}

    }
}

