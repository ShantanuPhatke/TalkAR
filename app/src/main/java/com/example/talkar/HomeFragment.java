package com.example.talkar;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class HomeFragment extends Fragment {

    private ModelAnimator modelAnimator;
    private int i;
    Button button;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArFragment arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.arFragment);

//        button = view.findViewById(R.id.button);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            createModel(hitResult.createAnchor(), arFragment);
        });

    }

    private void createModel(Anchor anchor, ArFragment arFragment) {

        ModelRenderable
                .builder()
                .setSource(getActivity(), Uri.parse("cavaleiro.sfb"))
                .build()
                .thenAccept(modelRenderable -> {

                    AnchorNode anchorNode = new AnchorNode();
                    SkeletonNode skeletonNode = new SkeletonNode();

                    skeletonNode.setParent(anchorNode);
                    skeletonNode.setRenderable(modelRenderable);

                    arFragment.getArSceneView().getScene().addChild(anchorNode);

                    // Replay animation on button click
//                    button.setOnClickListener(view -> animateModel(modelRenderable));

                    // Animate automatically for the first time
                    animateModel(modelRenderable);

                });

    }

    private void animateModel(ModelRenderable modelRenderable) {

        if (modelAnimator != null && modelAnimator.isRunning())
            modelAnimator.end();

        int animationCount = modelRenderable.getAnimationDataCount();

        if (i == animationCount)
            i = 0;

        AnimationData animationData = modelRenderable.getAnimationData(i);

        modelAnimator = new ModelAnimator(animationData, modelRenderable);
        modelAnimator.start();
        i++;

    }

}
