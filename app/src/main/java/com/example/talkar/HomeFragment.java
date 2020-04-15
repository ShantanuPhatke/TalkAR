package com.example.talkar;

import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private ModelAnimator modelAnimator;
    private int i;
    Button button;
    private TextToSpeech textToSpeech;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if(status==TextToSpeech.SUCCESS){
                textToSpeech.setLanguage(Locale.ENGLISH);
//                textToSpeech.setLanguage(new Locale("nl_NL"));
                speak("Welcome to Talk AR, an augmented reality based learning application. Tap on the screen to get started");
            }
        });



        ImageView imageView=view.findViewById(R.id.imageView);

        Glide.with(this).load(R.drawable.small).into(imageView);


        final ImageView imageView2 = view.findViewById(R.id.imageView2);

        imageView.setOnClickListener(view1 -> {
            speak("Hello! I'm your tutor, Eve. Head over to the next lesson and take a step further in mastering the German language.");
//                    animateModel(modelRenderable);
            imageView2.animate().alpha(1f).setDuration(2000);
        });

        return view;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ArFragment arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.arFragment);
//
////        button = view.findViewById(R.id.button);
//
//        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
//            createModel(hitResult.createAnchor(), arFragment);
//        });
//
//    }

//    private void createModel(Anchor anchor, ArFragment arFragment) {
//
//        ModelRenderable
//                .builder()
//                .setSource(getActivity(), Uri.parse("girl_3_obj.sfb"))
//                .build()
//                .thenAccept(modelRenderable -> {
//
//                    AnchorNode anchorNode = new AnchorNode();
//                    SkeletonNode skeletonNode = new SkeletonNode();
//
//                    skeletonNode.setParent(anchorNode);
//                    skeletonNode.setRenderable(modelRenderable);
//
//                    arFragment.getArSceneView().getScene().addChild(anchorNode);
//
//                    // Replay animation on button click
////                    button.setOnClickListener(view -> animateModel(modelRenderable));
//
//                    // Animate automatically for the first time
//                    speak("Hello! I'm your tutor, Eve. Head over to the next lesson and take a step further in mastering the German language.");
//                    animateModel(modelRenderable);
//
//                });
//
//    }
//
//    private void animateModel(ModelRenderable modelRenderable) {
//
//        if (modelAnimator != null && modelAnimator.isRunning())
//            modelAnimator.end();
//
//        int animationCount = modelRenderable.getAnimationDataCount();
//
//        if (i == animationCount)
//            i = 0;
//
//        AnimationData animationData = modelRenderable.getAnimationData(i);
//
//        modelAnimator = new ModelAnimator(animationData, modelRenderable);
//        modelAnimator.start();
//        i++;
//
//    }

    // TextToSpeech function
    public void speak(String s){
        textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null, null);
    }

}
