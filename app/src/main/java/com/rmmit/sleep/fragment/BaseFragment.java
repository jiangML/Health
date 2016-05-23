package com.rmmit.sleep.fragment;

import android.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BaseFragment extends Fragment {
    protected Toast toast;
    protected  void showToast(String message)
    {
        if(toast==null)
        {
            toast=Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }
}
