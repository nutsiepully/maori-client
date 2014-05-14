package io.pulkit.ubicomp.maori.test;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import io.pulkit.ubicomp.maori.AllModels;
import io.pulkit.ubicomp.maori.Model;
import io.pulkit.ubicomp.maori.ModelInfo;
import io.pulkit.ubicomp.maori.ModelNotFoundException;
import io.pulkit.ubicomp.maori.R;

public class TestDBActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Model model1 = new Model("name1", "version1", false, null);
        Model model2 = new Model("name1", "version2", true, null);
        Model model3 = new Model("name2", "version1", true, null);

        AllModels allModels = new AllModels(this);
        allModels.add(model1);
        allModels.add(model2);
        allModels.add(model3);

        Model model = allModels.getActive("name1");
        assert(model.isActive());
        assert(model != null);

        try {
            allModels.getActive("null");
        } catch (ModelNotFoundException m) {}

        List<ModelInfo> existingModels = allModels.getModelInfo();
        assert(existingModels.size() == 3);
    }
}
