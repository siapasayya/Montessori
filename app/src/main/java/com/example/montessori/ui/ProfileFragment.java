package com.example.montessori.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.montessori.ui.auth.LoginActivity;
import com.example.montessori.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private Button Btn;
    ImageView imageView;
    TextView username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


//        return inflater.inflate(R.layout.fragment_profile, container, false);

        Btn = view.findViewById(R.id.button);
        Btn.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = getActivity().findViewById(R.id.iv_cp);
        username = getActivity().findViewById(R.id.tv_name);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String currentid = user.getUid();
//        DocumentReference reference;
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        reference = firestore.collection("User").document(currentid);
//
//        reference.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.getResult().exists()) {
//
//
//                        }else {
//                            Intent intent = new Intent(getActivity(), CreateProfile.class);
//
//                        }
//                    }
//                });
//    }
}