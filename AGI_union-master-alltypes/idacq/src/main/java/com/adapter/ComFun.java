package com.adapter;

/**
 * Created by john on 2017/7/18.
 */
public class ComFun {




    public static String GetBoardName(int boardtype) {

        String name = null;

        switch (boardtype)
        {
            case Constants_Board_Model.BoardType_Board2:
                name =  "Board2";
                break;
        }

        return name;
    }

    public static String GetTypeName(int modeltype) {

        String name = null;

        switch (modeltype)
        {
            case Constants_Board_Model.ModelType_Board2_TDD:
                name =  "TDD";
                break;
        }

        return name;
    }

}
