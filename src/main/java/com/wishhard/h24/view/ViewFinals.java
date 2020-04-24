package com.wishhard.h24.view;



import com.wishhard.h24.R;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewFinals {

    public static List<Integer> viewIdsForTopOptions()  {
        List<Integer> ids = new ArrayList<>(Arrays.asList(R.id.settingsBtn,R.id.setTimerBtn));
        List<Integer> not_modifiable = ListUtils.unmodifiableList(ids);
        return  not_modifiable;
    }


     public static List<Integer> viewIdsForCenterOptions() {
         List<Integer> ids = new ArrayList<>(Arrays.asList(R.id.co_reset_btn,R.id.co_play_n_pase_btn));
         List<Integer> not_modifiable = ListUtils.unmodifiableList(ids);
         return  not_modifiable;
     }


}
